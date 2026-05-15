package jp.co.nittsu.gwh.common.service;

import jp.co.nittsu.gwh.common.exception.WmsBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Centralized document number generation service.
 *
 * Mirrors legacy GwhCmnumBLC exactly:
 *   - Uses DataSource.getConnection() directly (NOT DataSourceUtils / JPA EntityManager)
 *     so each call obtains a FRESH, unmanaged JDBC connection with its own transaction.
 *   - Calls conn.commit() to commit the GWH_TM_NUM update INDEPENDENTLY of any
 *     surrounding Spring-managed (@Transactional) transaction.
 *
 * This is the Spring-Boot equivalent of the legacy's
 *   @PersistenceUnit(unitName = NON_JTA) + manual EntityManager/EntityTransaction.
 *
 * Number code examples: "AV_NUM" (arrival), "SP_NUM" (shipping), etc.
 */
@Service
public class WmsNumberingService {

    private static final Logger log = LoggerFactory.getLogger(WmsNumberingService.class);



    /** Used for lock-wait retries (ORA-30006). */
    private static final int MAX_LOCK_RETRIES = 3;
    private static final long LOCK_RETRY_SLEEP_MS = 100;

    /**
     * Dedicated, isolated HikariCP pool ("numberingDataSource") — NOT the main
     * Spring JPA DataSource.  Mirrors the legacy NON_JTA persistence unit:
     * connections from this pool are invisible to Spring's JpaTransactionManager,
     * so conn.commit() is always a genuine, independent Oracle COMMIT.
     */
    @Autowired
    @Qualifier("numberingDataSource")
    private DataSource dataSource;

    // ── SQL ───────────────────────────────────────────────────────────────────

    /** 4-tier tenant-fallback SELECT with pessimistic row lock. */
    private static final String SELECT_FOR_UPDATE =
        "SELECT NUM_CPNY_COD, NUM_WHS_COD, NUM_CUST_COD, " +
        "       NUM_PRFX, NUM_STR_NUM, NUM_END_NUM, NUM_CUR_NUM, NUM_TYP, NUM_NLOP_FLG " +
        "FROM SGWH0001.GWH_TM_NUM " +
        "WHERE TRIM(NUM_CPNY_COD) = ? AND TRIM(NUM_WHS_COD) = ? AND TRIM(NUM_CUST_COD) = ? " +
        "  AND TRIM(NUM_COD) = ? AND TRIM(DEL_FLG) = '0' " +
        "FOR UPDATE WAIT 1";

    private static final String UPDATE_CUR_NUM =
        "UPDATE SGWH0001.GWH_TM_NUM " +
        "SET NUM_CUR_NUM = ?, UPD_YMDHMS = SYSTIMESTAMP " +
        "WHERE TRIM(NUM_CPNY_COD) = ? AND TRIM(NUM_WHS_COD) = ? AND TRIM(NUM_CUST_COD) = ? " +
        "  AND TRIM(NUM_COD) = ? AND TRIM(DEL_FLG) = '0'";

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Generates the next sequential number for the given number code.
     * Uses 4-tier fallback: CPNY+WHS+CUST → CPNY+WHS → CPNY → global (*).
     *
     * @param numCode       e.g. "AV_NUM"
     * @param companyCode   tenant company code
     * @param warehouseCode tenant warehouse code
     * @param customerCode  tenant customer code
     * @return formatted number string, e.g. "AV000001234"
     */
    public String generateNumber(String numCode,
                                 String companyCode,
                                 String warehouseCode,
                                 String customerCode) {
        log.debug("generateNumber START: code={} cpny={} whs={} cust={}",
                numCode, companyCode, warehouseCode, customerCode);

        for (int attempt = 0; attempt < MAX_LOCK_RETRIES; attempt++) {
            try {
                String result = doGenerateNumber(numCode, companyCode, warehouseCode, customerCode);
                log.debug("generateNumber DONE: code={} → result={}", numCode, result);
                return result;
            } catch (LockWaitTimeoutException e) {
                log.warn("generateNumber: lock wait timeout (attempt {}/{}), retrying...",
                        attempt + 1, MAX_LOCK_RETRIES);
                if (attempt == MAX_LOCK_RETRIES - 1) {
                    throw new WmsBusinessException("ERR1006",
                        "Could not acquire lock on GWH_TM_NUM after " + MAX_LOCK_RETRIES +
                        " attempts for code [" + numCode + "]. Please retry.");
                }
                sleep(LOCK_RETRY_SLEEP_MS);
            }
        }
        // unreachable
        throw new WmsBusinessException("ERR1006", "generateNumber: unexpected state for code [" + numCode + "]");
    }

    // ── Core (raw JDBC, own connection + commit) ──────────────────────────────

    private String doGenerateNumber(String numCode,
                                    String companyCode,
                                    String warehouseCode,
                                    String customerCode) {
        /*
         * dataSource.getConnection() — fetches a FRESH connection from HikariCP
         * that is NOT enrolled in the current Spring transaction.
         * This is identical to legacy GwhCmnumBLC's factory.createEntityManager()
         * with a NON_JTA persistence unit.
         */
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            log.debug("doGenerateNumber: got fresh JDBC connection, autoCommit=false");

            try {
                NumRow row = fetchWithLock(conn, numCode, companyCode, warehouseCode, customerCode);

                if (row == null) {
                    log.error("doGenerateNumber: GWH_TM_NUM row NOT FOUND for code={} " +
                              "cpny={} whs={} cust={} (tried all 4 tiers including '*').",
                              numCode, companyCode, warehouseCode, customerCode);
                    rollbackQuietly(conn);
                    throw new WmsBusinessException("ERR1006",
                        "GWH_TM_NUM row not found for code [" + numCode + "] — " +
                        "check that a row exists with NUM_COD='" + numCode + "' and DEL_FLG='0'.");
                }

                log.debug("doGenerateNumber: found row cpny={} whs={} cust={} prefix={} " +
                          "curNum={} strNum={} endNum={} typ={} nlop={}",
                          row.cpny, row.whs, row.cust, row.prefix,
                          row.curNum, row.startNum, row.endNum, row.typ, row.noLoopFlag);

                int    padLen       = row.endNum.length();
                String newNum       = increment(row.curNum, row.endNum, row.startNum,
                                               row.typ, row.noLoopFlag, numCode);
                // Store PADDED value — matches legacy GwhCmnumBLC convertLong2String output
                String paddedNewNum = padLeft(newNum, padLen);

                log.debug("doGenerateNumber: curNum={} → newNum={} → paddedNewNum={}",
                          row.curNum, newNum, paddedNewNum);

                updateSequence(conn, row.cpny, row.whs, row.cust, numCode, paddedNewNum);

                // ← INDEPENDENT Oracle COMMIT: persists NUM_CUR_NUM regardless of
                //   whether the caller's Spring transaction later rolls back.
                conn.commit();
                log.debug("doGenerateNumber: COMMITTED. NUM_CUR_NUM={}, returning {}{}",
                          paddedNewNum, row.prefix, paddedNewNum);

                return row.prefix + paddedNewNum;

            } catch (LockWaitTimeoutException | WmsBusinessException e) {
                rollbackQuietly(conn);
                throw e;
            } catch (Exception e) {
                log.error("doGenerateNumber: unexpected error for code [{}]: {}", numCode, e.getMessage(), e);
                rollbackQuietly(conn);
                throw new WmsBusinessException("ERR1006",
                    "generateNumber failed for code [" + numCode + "]: " + e.getMessage());
            }
        } catch (SQLException connEx) {
            log.error("doGenerateNumber: failed to get JDBC connection: {}", connEx.getMessage(), connEx);
            throw new WmsBusinessException("ERR1006",
                "Cannot obtain DB connection for numbering [" + numCode + "]: " + connEx.getMessage());
        }
    }

    // ── SELECT FOR UPDATE (4-tier fallback) ───────────────────────────────────

    private NumRow fetchWithLock(Connection conn,
                                 String numCode,
                                 String companyCode,
                                 String warehouseCode,
                                 String customerCode) throws SQLException {
        // Tier 1: exact tenant match
        log.debug("fetchWithLock: Tier1 cpny={} whs={} cust={}", companyCode, warehouseCode, customerCode);
        NumRow row = tryFetch(conn, numCode, companyCode, warehouseCode, customerCode);
        if (row != null) { log.debug("fetchWithLock: found at Tier1"); return row; }

        // Tier 2: any customer
        log.debug("fetchWithLock: Tier2 cpny={} whs={} cust=*", companyCode, warehouseCode);
        row = tryFetch(conn, numCode, companyCode, warehouseCode, "*");
        if (row != null) { log.debug("fetchWithLock: found at Tier2"); return row; }

        // Tier 3: any warehouse + customer
        log.debug("fetchWithLock: Tier3 cpny={} whs=* cust=*", companyCode);
        row = tryFetch(conn, numCode, companyCode, "*", "*");
        if (row != null) { log.debug("fetchWithLock: found at Tier3"); return row; }

        // Tier 4: global
        log.debug("fetchWithLock: Tier4 cpny=* whs=* cust=*");
        row = tryFetch(conn, numCode, "*", "*", "*");
        if (row != null) { log.debug("fetchWithLock: found at Tier4"); return row; }

        log.warn("fetchWithLock: NO ROW found in any tier for numCode={}", numCode);
        return null;
    }

    private NumRow tryFetch(Connection conn,
                            String numCode,
                            String cpny, String whs, String cust) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_FOR_UPDATE)) {
            ps.setString(1, cpny);
            ps.setString(2, whs);
            ps.setString(3, cust);
            ps.setString(4, numCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                NumRow row      = new NumRow();
                row.cpny        = str(rs.getString(1));   // NUM_CPNY_COD
                row.whs         = str(rs.getString(2));   // NUM_WHS_COD
                row.cust        = str(rs.getString(3));   // NUM_CUST_COD
                row.prefix      = str(rs.getString(4));   // NUM_PRFX
                row.startNum    = str(rs.getString(5));   // NUM_STR_NUM
                row.endNum      = str(rs.getString(6));   // NUM_END_NUM
                row.curNum      = str(rs.getString(7));   // NUM_CUR_NUM
                row.typ         = str(rs.getString(8));   // NUM_TYP
                row.noLoopFlag  = str(rs.getString(9));   // NUM_NLOP_FLG
                return row;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 30006) {   // ORA-30006: FOR UPDATE WAIT 1 timed out
                log.warn("tryFetch: ORA-30006 lock wait timeout for cpny={} whs={} cust={} code={}",
                         cpny, whs, cust, numCode);
                throw new LockWaitTimeoutException(e);
            }
            log.error("tryFetch: SQL error (ORA-{}) for cpny={} whs={} cust={} code={}: {}",
                      e.getErrorCode(), cpny, whs, cust, numCode, e.getMessage(), e);
            if (e.getErrorCode() == 942 || e.getErrorCode() == 904) {
                return null;   // table/column not found → skip tier
            }
            throw e;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    private void updateSequence(Connection conn,
                                String cpny, String whs, String cust,
                                String numCode, String newNum) throws SQLException {
        log.debug("updateSequence: SET NUM_CUR_NUM='{}' WHERE cpny={} whs={} cust={} code={}",
                  newNum, cpny, whs, cust, numCode);
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_CUR_NUM)) {
            ps.setString(1, newNum);
            ps.setString(2, cpny);
            ps.setString(3, whs);
            ps.setString(4, cust);
            ps.setString(5, numCode);
            int updated = ps.executeUpdate();
            log.debug("updateSequence: {} row(s) updated", updated);
            if (updated == 0) {
                throw new WmsBusinessException("ERR1014",
                    "GWH_TM_NUM UPDATE matched 0 rows for code [" + numCode + "] " +
                    "cpny=" + cpny + " whs=" + whs + " cust=" + cust +
                    ". Check GWH_TM_NUM data and DEL_FLG='0'.");
            }
        }
    }

    // ── Increment logic (mirrors legacy GwhCmnumBLC.gwhNewNumbering) ──────────

    private String increment(String curNum, String endNum, String startNum,
                             String typ, String noLoopFlag, String numCode) {
        int num_end_num_length = endNum.length();
        Long num_value = 0L;

        if (convertString2Long(startNum, typ) <= convertString2Long(curNum, typ)) {
            num_value = convertString2Long(curNum, typ);
        } else {
            num_value = convertString2Long(startNum, typ) - 1;
        }

        if ((num_value + 1) <= convertString2Long(endNum, typ)) {
            return convertLong2String(num_value + 1, typ, num_end_num_length);
        } else if ("Y".equalsIgnoreCase(noLoopFlag)) {
            log.error("gwhNumberingUpdate ER999 Can not Update GWH_TM_NUM");
            throw new WmsBusinessException("ERR1006", "Sequence exhausted for code [" + numCode + "]. Increase END_NUM or allow looping.");
        } else {
            return convertLong2String(convertString2Long(startNum, typ), typ, num_end_num_length);
        }
    }

    private static Long convertString2Long(String str, String num_type) {
        try {
            Long cal_num = 0L;
            Integer str_len = str.length();
            for (int i = str_len - 1; i >= 0; i--) {
                switch (num_type) {
                case "1":
                    cal_num = cal_num
                            + (convertChar2Int(str.charAt(str_len - i - 1), num_type) * (long) Math.pow(32, i));
                    break;
                case "2":
                    cal_num = cal_num
                            + (convertChar2Int(str.charAt(str_len - i - 1), num_type) * (long) Math.pow(10, i));
                    break;
                case "3":
                    cal_num = cal_num
                            + (convertChar2Int(str.charAt(str_len - i - 1), num_type) * (long) Math.pow(22, i));
                    break;
                }
            }
            return cal_num;
        } catch (Exception e) {
            return null;
        }
    }

    private static Integer convertChar2Int(char num, String num_type) {
        try {
            Integer cal_num = 0;
            switch (num_type) {
            case "1":
                if (num >= 48 && num <= 57) { // ASCII dec 48 is char '0' and dec 57 is char '9'
                    cal_num = Character.getNumericValue(num);
                } else if (num >= 65 && num <= 72) { // ASCII dec 65 is char 'A' and dec 90 is char 'Z'
                    cal_num = num - 55;
                } else if (num >= 74 && num <= 78) {
                    cal_num = num - 56;
                } else if (num == 80) {
                    cal_num = num - 57;
                } else if (num >= 82 && num <= 84) {
                    cal_num = num - 58;
                } else if (num >= 86 && num <= 90) {
                    cal_num = num - 59;
                } else {
                    log.error("convertChar2Int ER999 Can not convert Char to Integer");
                    throw new Exception("ER999 Can not convert Char to Integer");
                }
                break;
            case "2":
                if (num >= 48 && num <= 57) { // ASCII dec 48 is char '0' and dec 57 is char '9'
                    cal_num = Character.getNumericValue(num);
                } else {
                    log.error("ER999 Can not convert Char to Integer");
                    throw new Exception("ER999 Can not convert Char to Integer");
                }
                break;
            case "3":
                if (num >= 65 && num <= 72) { // ASCII dec 65 is char 'A' and dec 90 is char 'Z'
                    cal_num = num - 65; // minus by 65 becase "A" in dec is 65 must value 0
                } else if (num >= 74 && num <= 78) {
                    cal_num = num - 66;
                } else if (num == 80) {
                    cal_num = num - 67;
                } else if (num >= 82 && num <= 84) {
                    cal_num = num - 68;
                } else if (num >= 86 && num <= 90) {
                    cal_num = num - 69;
                } else {
                    log.error("ER999 Can not convert Char to Integer");
                    throw new Exception("ER999 Can not convert Char to Integer");
                }
                break;
            }
            return cal_num;
        } catch (Exception e) {
            return null;
        }
    }

    private static String convertLong2String(long num, String num_type, int num_end_num_length) {
        try {
            String cal_string = "";
            char[] res_char = new char[num_end_num_length];
            java.util.Arrays.fill(res_char, '0');
            long denominator = 0;
            long numerator = 0;
            switch (num_type) {
            case "1": // case1 have number and char(A-Z) is Base Number 32
                for (int i = num_end_num_length - 1; i >= 0; i--) {
                    numerator = num / (long) Math.pow(32, i);
                    denominator = num % (long) Math.pow(32, i);
                    res_char[i] = convertLong2Char(numerator, num_type);
                    num = denominator;
                    cal_string = cal_string + String.valueOf(res_char[i]);
                }
                break;
            case "2": // case2 have only number is Base Number 10
                for (int i = num_end_num_length - 1; i >= 0; i--) {
                    numerator = num / (long) Math.pow(10, i);
                    denominator = num % (long) Math.pow(10, i);
                    res_char[i] = convertLong2Char(numerator, num_type);
                    num = denominator;
                    cal_string = cal_string + String.valueOf(res_char[i]);
                }
                break;
            case "3": // case2 have only char(A-Z) is Base Number 22
                for (int i = num_end_num_length - 1; i >= 0; i--) {
                    numerator = num / (long) Math.pow(22, i);
                    denominator = num % (long) Math.pow(22, i);
                    res_char[i] = convertLong2Char(numerator, num_type);
                    num = denominator;
                    cal_string = cal_string + String.valueOf(res_char[i]);
                }
                break;
            }
            return cal_string;
        } catch (Exception e) {
            return null;
        }
    }

    private static char convertLong2Char(Long numerator, String num_type) {
        try {
            char res_char = '0';
            switch (num_type) {
            case "1":
                if (numerator >= 0 && numerator <= 9) {
                    res_char = (char) (numerator + '0');
                } else if (numerator >= 10 && numerator <= 17) { // 10 must return 'A' and 31 must return 'Z'
                    res_char = (char) (numerator + 55);
                } else if (numerator >= 18 && numerator <= 22) {
                    res_char = (char) (numerator + 56);
                } else if (numerator == 23) {
                    res_char = (char) (numerator + 57);
                } else if (numerator >= 24 && numerator <= 26) {
                    res_char = (char) (numerator + 58);
                } else if (numerator >= 27 && numerator <= 31) {
                    res_char = (char) (numerator + 59);
                } else {
                    log.error("ER999 Can not convert Integer to char");
                    throw new Exception("ER999 Can not convert Integer to char");
                }
                break;
            case "2":
                if (numerator >= 0 && numerator <= 9) {
                    res_char = (char) (numerator + '0');
                } else {
                    log.error("ER999 Can not convert Integer to char");
                    throw new Exception("ER999 Can not convert Integer to char");
                }
                break;
            case "3":
                if (numerator >= 0 && numerator <= 7) { // 0 must return 'A' and 21 must return 'Z'
                    res_char = (char) (numerator + 65);
                } else if (numerator >= 8 && numerator <= 12) {
                    res_char = (char) (numerator + 66);
                } else if (numerator == 13) {
                    res_char = (char) (numerator + 67);
                } else if (numerator >= 14 && numerator <= 16) {
                    res_char = (char) (numerator + 68);
                } else if (numerator >= 17 && numerator <= 21) {
                    res_char = (char) (numerator + 69);
                } else {
                    log.error("ER999 Can not convert Integer to char");
                    throw new Exception("ER999 Can not convert Integer to char");
                }
                break;
            }
            return res_char;
        } catch (Exception e) {
            return '0';
        }
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    private String padLeft(String s, int len) {
        if (s == null) s = "";
        if (s.length() >= len) return s;
        StringBuilder sb = new StringBuilder(len);
        for (int i = s.length(); i < len; i++) sb.append('0');
        sb.append(s);
        return sb.toString();
    }

    private String str(String s) {
        return s == null ? "" : s.trim();
    }



    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ── Internal classes ──────────────────────────────────────────────────────

    /** Thrown when ORA-30006 (FOR UPDATE WAIT n timeout) occurs. */
    private static class LockWaitTimeoutException extends RuntimeException {
        LockWaitTimeoutException(Throwable cause) {
            super("GWH_TM_NUM row locked: FOR UPDATE WAIT 1 timed out", cause);
        }
    }

    private static class NumRow {
        String cpny, whs, cust;
        String prefix, startNum, endNum, curNum, typ, noLoopFlag;
    }
}
