package jp.co.nittsu.gwh.module.lpn.repository;

import jp.co.nittsu.gwh.module.lpn.dto.LpnContentDto;
import jp.co.nittsu.gwh.module.lpn.dto.LpnDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LpnRepository {

    private static final String[] LPN_SOURCES = {
        "GWH.GWH_TJ_LPN"
    };

    private static final String[] LPN_D_SOURCES = {
        "GWH.GWH_TJ_LPN_D"
    };

    private static final String[] LPN_SEQ_SOURCES = {
        "GWH.SEQGWH_TJ_LPN"
    };

    private static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter HMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    @PersistenceContext
    private EntityManager em;

    // --- LPN Number Generation ---

    public String generateLpnNumber(String warehouseCode) {
        String datePart = LocalDate.now().format(YMD_FORMATTER);
        long seq = getNextSequenceValue();
        return warehouseCode + "-" + datePart + "-" + String.format("%06d", seq);
    }

    private long getNextSequenceValue() {
        for (String seqName : LPN_SEQ_SOURCES) {
            try {
                Query q = em.createNativeQuery("SELECT " + seqName + ".NEXTVAL FROM DUAL");
                Number val = (Number) q.getSingleResult();
                return val.longValue();
            } catch (Exception ignored) {}
        }
        return System.currentTimeMillis() % 1000000;
    }

    // --- LPN Existence Check ---

    public boolean existsLpn(String cpny, String whs, String cust, String lpnNumber) {
        for (String src : LPN_SOURCES) {
            try {
                String sql = "SELECT COUNT(*) FROM " + src +
                    " WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust" +
                    " AND LPN_NUM = :lpn AND DEL_FLG = '0'";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("lpn", lpnNumber);
                Number count = (Number) q.getSingleResult();
                return count != null && count.longValue() > 0;
            } catch (Exception ignored) {}
        }
        return false;
    }

    // --- LPN Existence Check (by LPN_NUM or LPN_RF_NUM) ---

    public boolean existsByLpnNumOrRfNum(String cpny, String whs, String cust, String barcode) {
        for (String src : LPN_SOURCES) {
            try {
                String sql = "SELECT COUNT(*) FROM " + src +
                    " WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust" +
                    " AND (LPN_NUM = :barcode OR LPN_RF_NUM = :barcode) AND DEL_FLG = '0'";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("barcode", barcode);
                Number count = (Number) q.getSingleResult();
                return count != null && count.longValue() > 0;
            } catch (Exception ignored) {}
        }
        return false;
    }

    // --- Insert LPN Header ---

    public void insertLpnHeader(String cpny, String whs, String cust, String lpnNumber,
                                 String lpnRfNumber,
                                 String lpnType, String status, String parentLpn,
                                 String locationCode, String arrivalNumber,
                                 Integer totalQty, Double totalWeight, Double totalVolume,
                                 String barcodeFormat, String remarks, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);

        for (String src : LPN_SOURCES) {
            try {
                String sql = "INSERT INTO " + src + " (" +
                    "LPN_CPNY_COD, LPN_WHS_COD, LPN_CUST_COD, LPN_NUM, LPN_RF_NUM, " +
                    "LPN_TYPE, LPN_STS, LPN_PRNT_NUM, LPN_LOC_COD, LPN_AV_NUM, " +
                    "LPN_TTL_QTY, LPN_TTL_WGT, LPN_TTL_VOL, LPN_BARCD_FMT, LPN_RCV_YMD, LPN_RMK, " +
                    "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                    "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS" +
                    ") VALUES (" +
                    ":cpny, :whs, :cust, :lpn, :rfNum, " +
                    ":type, :sts, :prnt, :loc, :avNum, " +
                    ":ttlQty, :ttlWgt, :ttlVol, :bFmt, :rcvYmd, :rmk, " +
                    "'0', :crtYmd, :crtTim, :crtTmid, :crtUser, 'LPN_CREATE', :crtTmZone, SYSTIMESTAMP, SYSTIMESTAMP, " +
                    ":updYmd, :updTim, :updTmid, :updUser, 'LPN_CREATE', :updTmZone, SYSTIMESTAMP, SYSTIMESTAMP" +
                    ")";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("lpn", lpnNumber);
                q.setParameter("rfNum", lpnRfNumber);
                q.setParameter("type", lpnType != null ? lpnType : "PLT");
                q.setParameter("sts", status != null ? status : "200");
                q.setParameter("prnt", parentLpn);
                q.setParameter("loc", locationCode);
                q.setParameter("avNum", arrivalNumber);
                q.setParameter("ttlQty", totalQty != null ? totalQty : 0);
                q.setParameter("ttlWgt", totalWeight != null ? totalWeight : 0.0);
                q.setParameter("ttlVol", totalVolume != null ? totalVolume : 0.0);
                q.setParameter("bFmt", barcodeFormat != null ? barcodeFormat : "CUSTOM");
                q.setParameter("rcvYmd", now);
                q.setParameter("rmk", remarks);
                q.setParameter("crtYmd", now);
                q.setParameter("crtTim", time);
                q.setParameter("crtTmid", "GWH-WMS");
                q.setParameter("crtUser", user);
                q.setParameter("crtTmZone", "+0900");
                q.setParameter("updYmd", now);
                q.setParameter("updTim", time);
                q.setParameter("updTmid", "GWH-WMS");
                q.setParameter("updUser", user);
                q.setParameter("updTmZone", "+0900");
                q.executeUpdate();
                return;
            } catch (Exception ignored) {}
        }
        throw new RuntimeException("Failed to insert LPN header - no valid table source found");
    }

    // --- Insert LPN Content Line ---

    public void insertLpnContent(String cpny, String whs, String cust, String lpnNumber,
                                  int lineNumber, String productCode, String lotNumber,
                                  Integer qty, String subInvCode, String arrivalNumber,
                                  Integer arrivalLineNumber, Integer arrivalSeqNumber,
                                  String pik1, String pik2, String pik3, String pik4,
                                  String pik5, String pik6, String pik7,
                                  String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);
        int safeQty = qty != null ? qty : 0;

        for (String src : LPN_D_SOURCES) {
            try {
                String sql = "INSERT INTO " + src + " (" +
                    "LPND_CPNY_COD, LPND_WHS_COD, LPND_CUST_COD, LPND_LPN_NUM, LPND_LN_NUM, " +
                    "LPND_PROD_COD, LPND_LOT_NUM, LPND_QTY, LPND_ALC_QTY, LPND_AVL_QTY, " +
                    "LPND_SBIV_COD, LPND_AV_NUM, LPND_AVLN_NUM, LPND_AVSQ_NUM, " +
                    "LPND_PIK1, LPND_PIK2, LPND_PIK3, LPND_PIK4, LPND_PIK5, LPND_PIK6, LPND_PIK7, " +
                    "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                    "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS" +
                    ") VALUES (" +
                    ":cpny, :whs, :cust, :lpn, :ln, " +
                    ":prod, :lot, :qty, 0, :avlQty, " +
                    ":sbiv, :avNum, :avLn, :avSq, " +
                    ":p1, :p2, :p3, :p4, :p5, :p6, :p7, " +
                    "'0', :crtYmd, :crtTim, :crtTmid, :crtUser, 'LPN_CREATE', :crtTmZone, SYSTIMESTAMP, SYSTIMESTAMP, " +
                    ":updYmd, :updTim, :updTmid, :updUser, 'LPN_CREATE', :updTmZone, SYSTIMESTAMP, SYSTIMESTAMP" +
                    ")";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("lpn", lpnNumber);
                q.setParameter("ln", lineNumber);
                q.setParameter("prod", productCode);
                q.setParameter("lot", lotNumber);
                q.setParameter("qty", safeQty);
                q.setParameter("avlQty", safeQty);
                q.setParameter("sbiv", subInvCode != null ? subInvCode : "-");
                q.setParameter("avNum", arrivalNumber);
                q.setParameter("avLn", arrivalLineNumber != null ? arrivalLineNumber : 0);
                q.setParameter("avSq", arrivalSeqNumber != null ? arrivalSeqNumber : 0);
                q.setParameter("p1", pik1);
                q.setParameter("p2", pik2);
                q.setParameter("p3", pik3);
                q.setParameter("p4", pik4);
                q.setParameter("p5", pik5);
                q.setParameter("p6", pik6);
                q.setParameter("p7", pik7);
                q.setParameter("crtYmd", now);
                q.setParameter("crtTim", time);
                q.setParameter("crtTmid", "GWH-WMS");
                q.setParameter("crtUser", user);
                q.setParameter("crtTmZone", "+0900");
                q.setParameter("updYmd", now);
                q.setParameter("updTim", time);
                q.setParameter("updTmid", "GWH-WMS");
                q.setParameter("updUser", user);
                q.setParameter("updTmZone", "+0900");
                q.executeUpdate();
                return;
            } catch (Exception ignored) {}
        }
        throw new RuntimeException("Failed to insert LPN content - no valid table source found");
    }

    // --- Update LPN_TTL_QTY = COUNT of detail lines ---

    public void updateLpnTotalQtyFromDetail(String cpny, String whs, String cust,
                                             String lpnNumber, String user) {
        String sql =
            "UPDATE GWH.GWH_TJ_LPN " +
            "SET LPN_TTL_QTY = (SELECT NVL(SUM(LPND_QTY), 0) FROM GWH.GWH_TJ_LPN_D " +
            "                   WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
            "                     AND LPND_LPN_NUM = :lpn AND DEL_FLG = '0'), " +
            "    LPN_LOC_COD = (SELECT TRIM(R.AVR_AREA_COD) || '-' || R.AVR_RACK_COD || '-' || R.AVR_PSTN_COD || '-' || R.AVR_LVL_COD " +
            "                   FROM GWH.GWH_TJ_AV_R R " +
            "                   INNER JOIN GWH.GWH_TJ_LPN_D D " +
            "                     ON D.LPND_AV_NUM = R.AVR_AS_NUM AND D.LPND_AVLN_NUM = R.AVR_ASLN_NUM AND D.LPND_AVSQ_NUM = R.AVR_ASSQ_NUM " +
            "                     AND D.LPND_CPNY_COD = R.AVR_CPNY_COD AND D.LPND_WHS_COD = R.AVR_WHS_COD AND D.LPND_CUST_COD = R.AVR_CUST_COD " +
            "                   WHERE D.LPND_CPNY_COD = :cpny AND D.LPND_WHS_COD = :whs AND D.LPND_CUST_COD = :cust " +
            "                     AND D.LPND_LPN_NUM = :lpn AND D.DEL_FLG = '0' AND R.DEL_FLG = '0' " +
            "                     AND R.AVR_AREA_COD IS NOT NULL AND ROWNUM = 1), " +
            "    UPD_USER = :user, UPD_YMDHMS = SYSTIMESTAMP " +
            "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
            "  AND LPN_NUM = :lpn AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("lpn", lpnNumber); q.setParameter("user", user);
        q.executeUpdate();
    }

    // --- Update LPN Status ---

    public void updateLpnStatus(String cpny, String whs, String cust, String lpnNumber,
                                 String newStatus, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);

        for (String src : LPN_SOURCES) {
            try {
                String sql = "UPDATE " + src + " SET LPN_STS = :sts, " +
                    "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, UPD_USER = :updUser, " +
                    "UPD_PGM = 'LPN_UPDATE', UPD_TM_ZONE = :updTmZone, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
                    "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
                    "AND LPN_NUM = :lpn AND DEL_FLG = '0'";
                Query q = em.createNativeQuery(sql);
                q.setParameter("sts", newStatus);
                q.setParameter("updYmd", now);
                q.setParameter("updTim", time);
                q.setParameter("updTmid", "GWH-WMS");
                q.setParameter("updUser", user);
                q.setParameter("updTmZone", "+0900");
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("lpn", lpnNumber);
                q.executeUpdate();
                return;
            } catch (Exception ignored) {}
        }
    }

    // --- Update LPN Location (Putaway) ---

    public void updateLpnLocation(String cpny, String whs, String cust, String lpnNumber,
                                   String locationCode, String newStatus, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);

        for (String src : LPN_SOURCES) {
            try {
                String sql = "UPDATE " + src + " SET LPN_LOC_COD = :loc, LPN_STS = :sts, " +
                    "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, UPD_USER = :updUser, " +
                    "UPD_PGM = 'LPN_PUTAWAY', UPD_TM_ZONE = :updTmZone, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
                    "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
                    "AND LPN_NUM = :lpn AND DEL_FLG = '0'";
                Query q = em.createNativeQuery(sql);
                q.setParameter("loc", locationCode);
                q.setParameter("sts", newStatus);
                q.setParameter("updYmd", now);
                q.setParameter("updTim", time);
                q.setParameter("updTmid", "GWH-WMS");
                q.setParameter("updUser", user);
                q.setParameter("updTmZone", "+0900");
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("lpn", lpnNumber);
                q.executeUpdate();
                return;
            } catch (Exception ignored) {}
        }
    }

    // --- Search LPNs ---

    public LpnSearchResult search(String cpny, String whs, String cust,
                                   String lpnNumber, String lpnRfNumber, String status, String lpnType,
                                   String locationCode, String arrivalNumber,
                                   int page, int size) {
        for (String src : LPN_SOURCES) {
            try {
                return doSearch(src, cpny, whs, cust, lpnNumber, lpnRfNumber, status, lpnType,
                               locationCode, arrivalNumber, page, size);
            } catch (Exception ignored) {}
        }
        return new LpnSearchResult(new ArrayList<>(), 0);
    }

    private LpnSearchResult doSearch(String tableName, String cpny, String whs, String cust,
                                      String lpnNumber, String lpnRfNumber, String status, String lpnType,
                                      String locationCode, String arrivalNumber,
                                      int page, int size) {
        String joinClause = " LEFT JOIN GWH.GWH_TJ_AV_H AH" +
            " ON AH.AVH_AV_NUM = L.LPN_AV_NUM" +
            " AND AH.AVH_CPNY_COD = L.LPN_CPNY_COD" +
            " AND AH.AVH_WHS_COD = L.LPN_WHS_COD" +
            " AND AH.AVH_CUST_COD = L.LPN_CUST_COD" +
            " AND AH.DEL_FLG = '0'";

        StringBuilder where = new StringBuilder();
        where.append(" WHERE L.LPN_CPNY_COD = :cpny AND L.LPN_WHS_COD = :whs AND L.LPN_CUST_COD = :cust AND L.DEL_FLG = '0'");

        if (lpnNumber != null && !lpnNumber.isEmpty()) {
            where.append(" AND (L.LPN_NUM LIKE :lpn OR L.LPN_RF_NUM LIKE :lpn)");
        }
        if (lpnRfNumber != null && !lpnRfNumber.isEmpty()) {
            where.append(" AND L.LPN_RF_NUM LIKE :rfNum");
        }
        if (status != null && !status.isEmpty()) {
            where.append(" AND NVL(AH.AVH_AV_STS, L.LPN_STS) = :sts");
        }
        if (lpnType != null && !lpnType.isEmpty()) {
            where.append(" AND L.LPN_TYPE = :type");
        }
        if (locationCode != null && !locationCode.isEmpty()) {
            where.append(" AND L.LPN_LOC_COD LIKE :loc");
        }
        if (arrivalNumber != null && !arrivalNumber.isEmpty()) {
            where.append(" AND L.LPN_AV_NUM = :avNum");
        }

        // Count
        String countSql = "SELECT COUNT(*) FROM " + tableName + " L" + joinClause + where;
        Query countQuery = em.createNativeQuery(countSql);
        setSearchParams(countQuery, cpny, whs, cust, lpnNumber, lpnRfNumber, status, lpnType, locationCode, arrivalNumber);
        long totalRecords = ((Number) countQuery.getSingleResult()).longValue();

        // Data โ€” status from Arrival Header, location from AVR (Area-Rack-Level-Position)
        String locationSubquery =
            "NVL(" +
            "(SELECT TRIM(R.AVR_AREA_COD) || '-' || R.AVR_RACK_COD || '-' || R.AVR_PSTN_COD || '-' || R.AVR_LVL_COD" +
            " FROM GWH.GWH_TJ_AV_R R" +
            " WHERE R.AVR_COI1 = L.LPN_NUM" +
            " AND R.AVR_CPNY_COD = L.LPN_CPNY_COD" +
            " AND R.AVR_WHS_COD = L.LPN_WHS_COD" +
            " AND R.AVR_CUST_COD = L.LPN_CUST_COD" +
            " AND R.DEL_FLG = '0'" +
            " AND R.AVR_AREA_COD IS NOT NULL" +
            " AND ROWNUM = 1" +
            "), L.LPN_LOC_COD)";

        String dataSql = "SELECT L.LPN_NUM, L.LPN_TYPE, " +
            "NVL(AH.AVH_AV_STS, L.LPN_STS), " +
            "L.LPN_PRNT_NUM, " +
            locationSubquery + ", " +
            "L.LPN_AV_NUM, L.LPN_SP_NUM, L.LPN_TTL_QTY, L.LPN_TTL_WGT, L.LPN_TTL_VOL, " +
            "L.LPN_BARCD_FMT, L.LPN_PRNT_YMD, L.LPN_RCV_YMD, L.LPN_RMK, L.LPN_RF_NUM " +
            "FROM " + tableName + " L" + joinClause + where +
            " ORDER BY L.UPD_YMDHMS DESC " +
            "OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";
        Query dataQuery = em.createNativeQuery(dataSql);
        setSearchParams(dataQuery, cpny, whs, cust, lpnNumber, lpnRfNumber, status, lpnType, locationCode, arrivalNumber);
        dataQuery.setParameter("offset", page * size);
        dataQuery.setParameter("limit", size);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = dataQuery.getResultList();
        List<LpnDto> results = new ArrayList<>();
        for (Object[] row : rows) {
            LpnDto dto = new LpnDto();
            dto.setLpnNumber(str(row[0]));
            dto.setLpnType(str(row[1]));
            dto.setLpnTypeLabel(mapTypeLabel(str(row[1])));
            dto.setLpnStatus(str(row[2]));
            dto.setLpnStatusLabel(mapStatusLabel(str(row[2])));
            dto.setParentLpnNumber(str(row[3]));
            dto.setLocationCode(str(row[4]));
            dto.setArrivalNumber(str(row[5]));
            dto.setShipmentNumber(str(row[6]));
            dto.setTotalQuantity(intVal(row[7]));
            dto.setTotalWeight(dblVal(row[8]));
            dto.setTotalVolume(dblVal(row[9]));
            dto.setBarcodeFormat(str(row[10]));
            dto.setPrintDate(str(row[11]));
            dto.setReceiveDate(str(row[12]));
            dto.setRemarks(str(row[13]));
            dto.setLpnRfNumber(str(row[14]));
            results.add(dto);
        }
        return new LpnSearchResult(results, totalRecords);
    }

    private void setSearchParams(Query q, String cpny, String whs, String cust,
                                  String lpnNumber, String lpnRfNumber, String status, String lpnType,
                                  String locationCode, String arrivalNumber) {
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        if (lpnNumber != null && !lpnNumber.isEmpty()) {
            q.setParameter("lpn", "%" + lpnNumber + "%");
        }
        if (lpnRfNumber != null && !lpnRfNumber.isEmpty()) {
            q.setParameter("rfNum", "%" + lpnRfNumber + "%");
        }
        if (status != null && !status.isEmpty()) {
            q.setParameter("sts", status);
        }
        if (lpnType != null && !lpnType.isEmpty()) {
            q.setParameter("type", lpnType);
        }
        if (locationCode != null && !locationCode.isEmpty()) {
            q.setParameter("loc", "%" + locationCode + "%");
        }
        if (arrivalNumber != null && !arrivalNumber.isEmpty()) {
            q.setParameter("avNum", arrivalNumber);
        }
    }

    // --- Get LPN Detail with Contents ---

    public LpnDto getLpnDetail(String cpny, String whs, String cust, String lpnNumber) {
        LpnSearchResult result = search(cpny, whs, cust, lpnNumber, null, null, null, null, null, 0, 1);
        if (result.getRecords().isEmpty()) {
            return null;
        }
        LpnDto dto = result.getRecords().get(0);
        dto.setContents(getLpnContents(cpny, whs, cust, lpnNumber));
        return dto;
    }

    public List<LpnContentDto> getLpnContents(String cpny, String whs, String cust, String lpnNumber) {
        for (String src : LPN_D_SOURCES) {
            try {
                String sql = "SELECT LPND_LN_NUM, LPND_PROD_COD, LPND_LOT_NUM, " +
                    "LPND_QTY, LPND_ALC_QTY, LPND_AVL_QTY, LPND_SBIV_COD, LPND_ST_STS, " +
                    "LPND_AV_NUM, LPND_AVLN_NUM, LPND_AVSQ_NUM, " +
                    "LPND_PIK1, LPND_PIK2, LPND_PIK3, LPND_PIK4, LPND_PIK5, LPND_PIK6, LPND_PIK7 " +
                    "FROM " + src +
                    " WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
                    "AND LPND_LPN_NUM = :lpn AND DEL_FLG = '0' ORDER BY LPND_LN_NUM";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("lpn", lpnNumber);

                @SuppressWarnings("unchecked")
                List<Object[]> rows = q.getResultList();
                List<LpnContentDto> contents = new ArrayList<>();
                for (Object[] row : rows) {
                    LpnContentDto c = new LpnContentDto();
                    c.setLineNumber(intVal(row[0]));
                    c.setProductCode(str(row[1]));
                    c.setLotNumber(str(row[2]));
                    c.setQuantity(intVal(row[3]));
                    c.setAllocatedQuantity(intVal(row[4]));
                    c.setAvailableQuantity(intVal(row[5]));
                    c.setSubInventoryCode(str(row[6]));
                    c.setStockStatus(str(row[7]));
                    c.setArrivalNumber(str(row[8]));
                    c.setArrivalLineNumber(intVal(row[9]));
                    c.setArrivalSeqNumber(intVal(row[10]));
                    c.setPik1(str(row[11]));
                    c.setPik2(str(row[12]));
                    c.setPik3(str(row[13]));
                    c.setPik4(str(row[14]));
                    c.setPik5(str(row[15]));
                    c.setPik6(str(row[16]));
                    c.setPik7(str(row[17]));
                    contents.add(c);
                }
                return contents;
            } catch (Exception ignored) {}
        }
        return new ArrayList<>();
    }

    // --- Update Stock Location by LPN (Putaway) ---
    // Joins LPN content lines to stock via arrival key (AV_NUM, AVLN_NUM, AVSQ_NUM)
    // to update location for all stock records associated with this LPN.

    public void updateStockLocationByLpn(String cpny, String whs, String cust,
                                          String lpnNumber, String newAreaCode,
                                          String newRackCode, String newPositionCode,
                                          String newLevelCode, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);

        String sql = "UPDATE GWH.GWH_TJ_ST s SET " +
            "s.ST_AREA_COD = :newArea, s.ST_RACK_COD = :newRack, " +
            "s.ST_PSTN_COD = :newPstn, s.ST_LVL_COD = :newLvl, " +
            "s.UPD_YMD = :updYmd, s.UPD_TIM = :updTim, s.UPD_TMID = :updTmid, s.UPD_USER = :updUser, " +
            "s.UPD_PGM = 'LPN_PUTAWAY', s.UPD_TM_ZONE = :updTmZone, s.UPD_YMDHMS = SYSTIMESTAMP, s.UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE EXISTS (" +
            "SELECT 1 FROM GWH.GWH_TJ_LPN_D d " +
            "WHERE d.LPND_CPNY_COD = s.ST_CPNY_COD AND d.LPND_WHS_COD = s.ST_WHS_COD " +
            "AND d.LPND_CUST_COD = s.ST_CUST_COD AND d.LPND_AV_NUM = s.ST_AV_NUM " +
            "AND d.LPND_AVLN_NUM = s.ST_AVLN_NUM AND d.LPND_AVSQ_NUM = s.ST_AVSQ_NUM " +
            "AND d.LPND_LPN_NUM = :lpn AND d.DEL_FLG = '0'" +
            ") AND s.DEL_FLG = '0'";

        Query q = em.createNativeQuery(sql);
        q.setParameter("newArea", newAreaCode != null ? newAreaCode : "");
        q.setParameter("newRack", newRackCode);
        q.setParameter("newPstn", newPositionCode);
        q.setParameter("newLvl", newLevelCode);
        q.setParameter("updYmd", now);
        q.setParameter("updTim", time);
        q.setParameter("updTmid", "GWH-WMS");
        q.setParameter("updUser", user);
        q.setParameter("updTmZone", "+0900");
        q.setParameter("lpn", lpnNumber);
        q.executeUpdate();
    }

    // --- Remove LPN: Reduce AV_R qty then delete zero-qty rows ---

    public void reduceArrivalResultQtyByLpn(String cpny, String whs, String cust,
                                             String lpnNumber, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);
        String sql = "UPDATE GWH.GWH_TJ_AV_R R " +
            "SET R.AVR_RTPC_QTY = R.AVR_RTPC_QTY - (" +
            "  SELECT D.LPND_QTY FROM GWH.GWH_TJ_LPN_D D " +
            "  WHERE D.LPND_CPNY_COD = R.AVR_CPNY_COD AND D.LPND_WHS_COD = R.AVR_WHS_COD " +
            "    AND D.LPND_CUST_COD = R.AVR_CUST_COD " +
            "    AND D.LPND_AV_NUM = R.AVR_AS_NUM AND D.LPND_AVLN_NUM = R.AVR_ASLN_NUM " +
            "    AND D.LPND_AVSQ_NUM = R.AVR_ASSQ_NUM " +
            "    AND D.LPND_LPN_NUM = :lpn AND D.DEL_FLG = '0'" +
            "), " +
            "R.UPD_YMD = :updYmd, R.UPD_TIM = :updTim, R.UPD_TMID = 'GWH-WMS', " +
            "R.UPD_USER = :user, R.UPD_PGM = 'LPN_REMOVE', " +
            "R.UPD_YMDHMS = SYSTIMESTAMP, R.UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE EXISTS (" +
            "  SELECT 1 FROM GWH.GWH_TJ_LPN_D D " +
            "  WHERE D.LPND_CPNY_COD = R.AVR_CPNY_COD AND D.LPND_WHS_COD = R.AVR_WHS_COD " +
            "    AND D.LPND_CUST_COD = R.AVR_CUST_COD " +
            "    AND D.LPND_AV_NUM = R.AVR_AS_NUM AND D.LPND_AVLN_NUM = R.AVR_ASLN_NUM " +
            "    AND D.LPND_AVSQ_NUM = R.AVR_ASSQ_NUM " +
            "    AND D.LPND_LPN_NUM = :lpn AND D.DEL_FLG = '0'" +
            ") AND R.AVR_CPNY_COD = :cpny AND R.AVR_WHS_COD = :whs AND R.AVR_CUST_COD = :cust " +
            "  AND R.DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("lpn", lpnNumber);
        q.setParameter("updYmd", now);
        q.setParameter("updTim", time);
        q.setParameter("user", user);
        q.executeUpdate();
    }

    public void deleteZeroQtyArrivalResultByLpn(String cpny, String whs, String cust, String lpnNumber) {
        String sql = "DELETE FROM GWH.GWH_TJ_AV_R R " +
            "WHERE EXISTS (" +
            "  SELECT 1 FROM GWH.GWH_TJ_LPN_D D " +
            "  WHERE D.LPND_CPNY_COD = R.AVR_CPNY_COD AND D.LPND_WHS_COD = R.AVR_WHS_COD " +
            "    AND D.LPND_CUST_COD = R.AVR_CUST_COD " +
            "    AND D.LPND_AV_NUM = R.AVR_AS_NUM AND D.LPND_AVLN_NUM = R.AVR_ASLN_NUM " +
            "    AND D.LPND_AVSQ_NUM = R.AVR_ASSQ_NUM " +
            "    AND D.LPND_LPN_NUM = :lpn AND D.DEL_FLG = '0'" +
            ") AND R.AVR_CPNY_COD = :cpny AND R.AVR_WHS_COD = :whs AND R.AVR_CUST_COD = :cust " +
            "  AND R.AVR_RTPC_QTY <= 0";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("lpn", lpnNumber);
        q.executeUpdate();
    }

    public boolean existsArrivalResultForArrival(String cpny, String whs, String cust, String arrivalNumber) {
        String sql = "SELECT COUNT(*) FROM GWH.GWH_TJ_AV_R " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "AND AVR_AS_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", arrivalNumber);
        Number count = (Number) q.getSingleResult();
        return count != null && count.longValue() > 0;
    }

    public void updateArrivalHeaderStatus(String cpny, String whs, String cust,
                                           String arrivalNumber, String status, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);
        String sql = "UPDATE GWH.GWH_TJ_AV_H " +
            "SET AVH_AV_STS = :sts, " +
            "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = 'GWH-WMS', " +
            "UPD_USER = :user, UPD_PGM = 'LPN_REMOVE', UPD_TM_ZONE = 'JST', " +
            "UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
            "AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("sts", status);
        q.setParameter("updYmd", now);
        q.setParameter("updTim", time);
        q.setParameter("user", user);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", arrivalNumber);
        q.executeUpdate();
    }

    public void updateArrivalDetailStatus(String cpny, String whs, String cust,
                                           String arrivalNumber, String status, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);
        String sql = "UPDATE GWH.GWH_TJ_AV_D " +
            "SET AVD_AV_STS = :sts, " +
            "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = 'GWH-WMS', " +
            "UPD_USER = :user, UPD_PGM = 'LPN_REMOVE', UPD_TM_ZONE = 'JST', " +
            "UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "AND AVD_AV_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("sts", status);
        q.setParameter("updYmd", now);
        q.setParameter("updTim", time);
        q.setParameter("user", user);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", arrivalNumber);
        q.executeUpdate();
    }

    public void deleteLpnDetail(String cpny, String whs, String cust, String lpnNumber) {
        String sql = "DELETE FROM GWH.GWH_TJ_LPN_D " +
            "WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
            "AND LPND_LPN_NUM = :lpn";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("lpn", lpnNumber);
        q.executeUpdate();
    }

    public void deleteLpnHeader(String cpny, String whs, String cust, String lpnNumber) {
        String sql = "DELETE FROM GWH.GWH_TJ_LPN " +
            "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
            "AND LPN_NUM = :lpn";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("lpn", lpnNumber);
        q.executeUpdate();
    }

    // --- Helpers ---

    private String str(Object o) {
        return o != null ? o.toString().trim() : null;
    }

    private Integer intVal(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).intValue();
        try { return Integer.parseInt(o.toString().trim()); } catch (Exception e) { return null; }
    }

    private Double dblVal(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).doubleValue();
        try { return Double.parseDouble(o.toString().trim()); } catch (Exception e) { return null; }
    }

    private String mapTypeLabel(String type) {
        if (type == null) return "";
        switch (type.toUpperCase()) {
            case "PLT": return "Pallet";
            case "CSE": return "Case";
            case "TOT": return "Tote";
            case "OTH": return "Other";
            default: return type;
        }
    }

    private String mapStatusLabel(String status) {
        if (status == null) return "";
        switch (status.trim()) {
            case "100": return "Pre";
            case "200": return "Ready for Receiving";
            case "205": return "Inspecting";
            case "209": return "Inspected";
            case "300": return "Receiving";
            case "305": return "Locating";
            case "306": return "Located";
            case "500": return "Putaway";
            case "609": return "Stored";
            case "809": return "Confirmed";
            default: return status;
        }
    }

    // --- Search Result ---

    public static class LpnSearchResult {
        private final List<LpnDto> records;
        private final long totalRecords;

        public LpnSearchResult(List<LpnDto> records, long totalRecords) {
            this.records = records;
            this.totalRecords = totalRecords;
        }

        public List<LpnDto> getRecords() { return records; }
        public long getTotalRecords() { return totalRecords; }
    }
}
