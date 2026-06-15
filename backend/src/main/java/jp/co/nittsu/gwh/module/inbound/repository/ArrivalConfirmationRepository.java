package jp.co.nittsu.gwh.module.inbound.repository;

import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationDto;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationSearchRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArrivalConfirmationRepository {

    private static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter HMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    private static final String[] AVH_SOURCES = {
        "GWH.GWH_TJ_AV_H",
        "GWH_TJ_AV_H"
    };

    private static final String[] AVD_SOURCES = {
        "GWH.GWH_TJ_AV_D",
        "GWH_TJ_AV_D"
    };

    private static final String[] AVR_SOURCES = {
        "GWH.GWH_TJ_AV_R",
        "GWH_TJ_AV_R"
    };

    private static final String[] ST_SOURCES = {
        "GWH.GWH_TJ_ST",
        "GWH_TJ_ST"
    };

    private static final String[] CUST_SOURCES = {
        "GWH.GWH_TM_CUST",
        "GWH_TM_CUST"
    };

    private static final String[] PRCC_SOURCES = {
        "GWH.VGWH_TM_PRCC",
        "VGWH_TM_PRCC",
        "GWH.GWH_TM_PRCC",
        "GWH_TM_PRCC"
    };

    @PersistenceContext
    private EntityManager em;

    public SearchResult search(
        String companyCode,
        String warehouseCode,
        String customerCode,
        ArrivalConfirmationSearchRequest request
    ) {
        StringBuilder where = new StringBuilder();
        Map<String, Object> params = new LinkedHashMap<>();

        where.append(" WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust AND DEL_FLG = '0'");
        params.put("cpny", companyCode);
        params.put("whs", warehouseCode);
        params.put("cust", customerCode);

        if (trimToNull(request.getArrivalStatus()) != null) {
            where.append(" AND AVH_AV_STS = :arrivalStatus ");
            params.put("arrivalStatus", request.getArrivalStatus().trim());
        }
        if (trimToNull(request.getTransactionKind()) != null) {
            where.append(" AND AVH_TRN_KND = :trnKnd ");
            params.put("trnKnd", request.getTransactionKind().trim());
        }
        if (trimToNull(request.getArrivalNumberFrom()) != null && trimToNull(request.getArrivalNumberTo()) != null) {
            where.append(" AND AVH_AV_NUM BETWEEN :arrivalNoFrom AND :arrivalNoTo ");
            params.put("arrivalNoFrom", request.getArrivalNumberFrom().trim());
            params.put("arrivalNoTo", request.getArrivalNumberTo().trim());
        } else if (trimToNull(request.getArrivalNumberFrom()) != null) {
            where.append(" AND AVH_AV_NUM >= :arrivalNoFrom ");
            params.put("arrivalNoFrom", request.getArrivalNumberFrom().trim());
        } else if (trimToNull(request.getArrivalNumberTo()) != null) {
            where.append(" AND AVH_AV_NUM <= :arrivalNoTo ");
            params.put("arrivalNoTo", request.getArrivalNumberTo().trim());
        }
        if (trimToNull(request.getSupplier()) != null) {
            where.append(" AND (UPPER(NVL(AVH_SPL_COD, '')) LIKE :supplier OR UPPER(NVL(AVH_SPL_NAM1, '')) LIKE :supplier) ");
            params.put("supplier", "%" + request.getSupplier().trim().toUpperCase() + "%");
        }
        if (trimToNull(request.getPoNumber()) != null) {
            where.append(" AND UPPER(NVL(AVH_PO_NUM, '')) LIKE :poNo ");
            params.put("poNo", "%" + request.getPoNumber().trim().toUpperCase() + "%");
        }
        if (trimToNull(request.getReferenceNumber()) != null) {
            where.append(" AND UPPER(NVL(AVH_RF_NUM, '')) LIKE :rfNo ");
            params.put("rfNo", "%" + request.getReferenceNumber().trim().toUpperCase() + "%");
        }
        if (request.getScheduledDateFrom() != null) {
            where.append(" AND TRUNC(AVH_SCDL_YMD) >= :scdlFrom ");
            params.put("scdlFrom", Date.valueOf(request.getScheduledDateFrom()));
        }
        if (request.getScheduledDateTo() != null) {
            where.append(" AND TRUNC(AVH_SCDL_YMD) <= :scdlTo ");
            params.put("scdlTo", Date.valueOf(request.getScheduledDateTo()));
        }

        RuntimeException lastError = null;
        for (String source : AVH_SOURCES) {
            try {
                return searchFromSource(source, where.toString(), params, request.getPage(), request.getSize());
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
                lastError = ex;
            }
        }

        if (lastError != null) {
            throw lastError;
        }
        return new SearchResult(new ArrayList<>(), 0L);
    }

    private SearchResult searchFromSource(
        String source,
        String where,
        Map<String, Object> params,
        int page,
        int size
    ) {
        String countSql = "SELECT COUNT(*) FROM " + source + where;
        Query countQuery = em.createNativeQuery(countSql);
        applyParams(countQuery, params);
        @SuppressWarnings("unchecked")
        List<Object> countRows = countQuery.getResultList();
        long total = (countRows != null && !countRows.isEmpty()) ? toLong(countRows.get(0)) : 0;

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 200));
        int offset = safePage * safeSize;

        String selectSql =
            "SELECT AVH_AV_NUM, AVH_AV_STS, AVH_SPL_COD, AVH_SPL_NAM1, AVH_PO_NUM, AVH_RF_NUM, " +
            "AVH_SCDL_YMD, AVH_ARV_YMD, AVH_PTDV_FLG, AVH_RMKS " +
            "FROM " + source + where +
            " ORDER BY AVH_SCDL_YMD DESC, AVH_AV_NUM DESC " +
            "OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY";

        Query listQuery = em.createNativeQuery(selectSql);
        applyParams(listQuery, params);
        listQuery.setParameter("offset", offset);
        listQuery.setParameter("size", safeSize);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = listQuery.getResultList();
        List<ArrivalConfirmationDto> data = new ArrayList<>();

        for (Object[] row : rows) {
            ArrivalConfirmationDto dto = new ArrivalConfirmationDto();
            dto.setArrivalNumber(toStringValue(row[0]));
            dto.setArrivalStatus(toStringValue(row[1]));
            dto.setSupplierCode(toStringValue(row[2]));
            dto.setSupplierName(toStringValue(row[3]));
            dto.setPoNumber(toStringValue(row[4]));
            dto.setReferenceNumber(toStringValue(row[5]));
            dto.setScheduledDate(toLocalDate(row[6]));
            dto.setArrivalDate(toLocalDate(row[7]));
            dto.setPartialDeliveryFlag(toStringValue(row[8]));
            dto.setRemarks(toStringValue(row[9]));
            data.add(dto);
        }

        return new SearchResult(data, total);
    }

    public String findProcessFinalStatus(String companyCode, String warehouseCode, String customerCode, String processCode) {
        if (trimToNull(processCode) == null) {
            return null;
        }

        RuntimeException lastError = null;
        for (String source : PRCC_SOURCES) {
            try {
                String sql = "SELECT PRCC_FSTS FROM (" +
                    "  SELECT PRCC_FSTS, ROW_NUMBER() OVER (PARTITION BY PRCC_PRCS_COD" +
                    "    ORDER BY LENGTH(PRCC_CPNY_COD || PRCC_WHS_COD || PRCC_CUST_COD) DESC) PRIORITY_SEQ" +
                    "  FROM " + source +
                    "  WHERE (PRCC_CPNY_COD = :cpny OR PRCC_CPNY_COD = '*')" +
                    "    AND (PRCC_WHS_COD = :whs OR PRCC_WHS_COD = '*')" +
                    "    AND (PRCC_CUST_COD = :cust OR PRCC_CUST_COD = '*')" +
                    "    AND PRCC_PRCS_COD = :prcsCod" +
                    ") WHERE PRIORITY_SEQ = 1";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                query.setParameter("prcsCod", processCode);
                query.setMaxResults(1);
                List<?> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object first = rows.get(0);
                    if (first instanceof Object[]) {
                        Object[] values = (Object[]) first;
                        return values.length > 0 ? trimToNull(toStringValue(values[0])) : null;
                    }
                    return trimToNull(toStringValue(first));
                }
                return null;
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
                lastError = ex;
            }
        }

        if (lastError != null) {
            return null;
        }
        return null;
    }

    /**
     * Query GWH_TM_PRCC for full process control statuses.
     * Returns [PRCC_RSTS, PRCC_PSTS, PRCC_FSTS] or null if not found.
     * Uses legacy wildcard partition pattern: (CPNY = :cpny OR CPNY = '*')
     */
    public String[] findProcessControlStatuses(String companyCode, String warehouseCode,
                                                String customerCode, String processCode) {
        if (trimToNull(processCode) == null) return null;
        RuntimeException lastError = null;
        for (String source : PRCC_SOURCES) {
            try {
                String sql = "SELECT PRCC_RSTS, PRCC_PSTS, PRCC_FSTS FROM (" +
                    "  SELECT PRCC_RSTS, PRCC_PSTS, PRCC_FSTS, ROW_NUMBER() OVER (PARTITION BY PRCC_PRCS_COD" +
                    "    ORDER BY LENGTH(PRCC_CPNY_COD || PRCC_WHS_COD || PRCC_CUST_COD) DESC) PRIORITY_SEQ" +
                    "  FROM " + source +
                    "  WHERE (PRCC_CPNY_COD = :cpny OR PRCC_CPNY_COD = '*')" +
                    "    AND (PRCC_WHS_COD = :whs OR PRCC_WHS_COD = '*')" +
                    "    AND (PRCC_CUST_COD = :cust OR PRCC_CUST_COD = '*')" +
                    "    AND PRCC_PRCS_COD = :prcsCod" +
                    ") WHERE PRIORITY_SEQ = 1";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                query.setParameter("prcsCod", processCode);
                @SuppressWarnings("unchecked")
                List<Object[]> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object[] row = rows.get(0);
                    return new String[]{
                        row[0] != null ? row[0].toString().trim() : null,
                        row[1] != null ? row[1].toString().trim() : null,
                        row[2] != null ? row[2].toString().trim() : null
                    };
                }
                lastError = null; continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) { lastError = ex; continue; }
                throw ex;
            }
        }
        return null;
    }

    /** Get current AVH_AV_STS for an arrival */
    public String getHeaderStatus(String cpny, String whs, String cust, String avNum) {
        for (String source : AVH_SOURCES) {
            try {
                String sql = "SELECT AVH_AV_STS FROM " + source +
                    " WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust" +
                    " AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object result = rows.get(0);
                    return result != null ? result.toString().trim() : null;
                }
                continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return null;
    }

    /** Get all AVD_AV_STS for an arrival's detail lines */
    public List<String> getDetailStatuses(String cpny, String whs, String cust, String avNum) {
        for (String source : AVD_SOURCES) {
            try {
                String sql = "SELECT AVD_AV_STS FROM " + source +
                    " WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust" +
                    " AND AVD_AV_NUM = :avNum AND DEL_FLG = '0' ORDER BY AVD_AVLN_NUM";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                List<String> statuses = new ArrayList<>();
                for (Object row : rows) {
                    statuses.add(row != null ? row.toString().trim() : "");
                }
                return statuses;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return new ArrayList<>();
    }

    public int confirmArrivals(
        String companyCode,
        String warehouseCode,
        String customerCode,
        List<String> arrivalNumbers,
        LocalDate arrivalDate,
        String finalStatus,
        String userCode,
        String programCode
    ) {
        if (arrivalNumbers == null || arrivalNumbers.isEmpty()) {
            return 0;
        }

        LocalDate effectiveArrivalDate = arrivalDate != null ? arrivalDate : LocalDate.now();
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();
        String updYmd = formatYmd(nowDate);
        String updTim = formatHms(nowTime);
        String updTmid = defaultText(trimToNull(userCode), "SYSTEM");
        String updUser = defaultText(trimToNull(userCode), "SYSTEM");
        String updPgm = defaultText(trimToNull(programCode), "MODERN_APCNF");

        RuntimeException lastError = null;
        for (String source : AVH_SOURCES) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE ").append(source).append(" SET ");
                sql.append("AVH_AV_STS = :status, AVH_ARV_FLG = 'Y', AVH_ARV_YMD = :arvYmd, AVH_CNOP_YMD = :cnopYmd, ");
                sql.append("UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, UPD_USER = :updUser, UPD_PGM = :updPgm, ");
                sql.append("UPD_TM_ZONE = 'JST', UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms ");
                sql.append("WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust AND DEL_FLG = '0' ");
                sql.append("AND AVH_AV_NUM IN (");
                for (int i = 0; i < arrivalNumbers.size(); i++) {
                    if (i > 0) {
                        sql.append(", ");
                    }
                    sql.append(":avNum").append(i);
                }
                sql.append(")");

                Query query = em.createNativeQuery(sql.toString());
                query.setParameter("status", finalStatus);
                query.setParameter("arvYmd", Date.valueOf(effectiveArrivalDate));
                query.setParameter("cnopYmd", Date.valueOf(nowDate));
                query.setParameter("updYmd", updYmd);
                query.setParameter("updTim", updTim);
                query.setParameter("updTmid", updTmid);
                query.setParameter("updUser", updUser);
                query.setParameter("updPgm", updPgm);
                query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
                query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                for (int i = 0; i < arrivalNumbers.size(); i++) {
                    query.setParameter("avNum" + i, arrivalNumbers.get(i));
                }

                int updated = query.executeUpdate();
                updateArrivalDetailsStatus(
                    companyCode,
                    warehouseCode,
                    customerCode,
                    arrivalNumbers,
                    finalStatus,
                    updYmd,
                    updTim,
                    updTmid,
                    updUser,
                    updPgm,
                    nowDateTime
                );
                updateArrivalResultsStatus(
                    companyCode,
                    warehouseCode,
                    customerCode,
                    arrivalNumbers,
                    effectiveArrivalDate,
                    finalStatus,
                    updYmd,
                    updTim,
                    updTmid,
                    updUser,
                    updPgm,
                    nowDateTime
                );
                return updated;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                // Try fallback for ANY exception (column mismatch, schema issues, etc.)
                try {
                    int result = confirmArrivalsFallbackMinimal(
                        source,
                        companyCode,
                        warehouseCode,
                        customerCode,
                        arrivalNumbers,
                        finalStatus
                    );
                    return result;  // Fallback succeeded
                } catch (RuntimeException fallbackEx) {
                    lastError = fallbackEx;
                    continue;  // Continue to next source
                }
            }
        }

        if (lastError != null) {
            throw lastError;
        }
        return 0;
    }

    /** Update header status for a single arrival (used after per-arrival header status computation) */
    public void updateSingleHeaderStatus(String cpny, String whs, String cust, String avNum,
                                          String status, String userCode, String programCode) {
        LocalDateTime now = LocalDateTime.now();
        for (String source : AVH_SOURCES) {
            try {
                String sql = "UPDATE " + source + " SET AVH_AV_STS = :status, " +
                    "UPD_YMD = :updYmd, UPD_USER = :updUser, UPD_PGM = :updPgm, " +
                    "UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updYmdhms " +
                    "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                    "AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("status", status);
                query.setParameter("updYmd", formatYmd(LocalDate.now()));
                query.setParameter("updUser", userCode != null ? userCode : "SYSTEM");
                query.setParameter("updPgm", programCode != null ? programCode : "MODERN_APCNF");
                query.setParameter("updYmdhms", Timestamp.valueOf(now));
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                query.executeUpdate();
                return;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
    }

    /**
     * Get customer stock control kind (CUST_ST_KND) from GWH_TM_CUST.
     * Returns 'S' (serial/forever) or other value. Defaults to 'N' if not found.
     */
    public String getCustomerStockKind(String cpny, String whs, String cust) {
        for (String source : CUST_SOURCES) {
            try {
                String sql = "SELECT CUST_ST_KND FROM " + source +
                    " WHERE CUST_CPNY_COD = :cpny AND CUST_WHS_COD = :whs AND CUST_COD = :cust AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object result = rows.get(0);
                    return result != null ? result.toString().trim() : "N";
                }
                continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return "N";
    }

    /**
     * Get AVR result rows for confirmed arrivals (for stock creation).
     * Returns list of Object[] with columns needed for GWH_TJ_ST insert.
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getArrivalResultsForStock(String cpny, String whs, String cust, List<String> arrivalNumbers) {
        if (arrivalNumbers == null || arrivalNumbers.isEmpty()) return new ArrayList<>();

        for (String avrSource : AVR_SOURCES) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT r.AVR_AS_NUM, r.AVR_ASLN_NUM, r.AVR_ASSQ_NUM, ");
                sql.append("r.AVR_PROD_COD, r.AVR_ORGN_COD, r.AVR_SBIV_COD, ");
                sql.append("r.AVR_PIK1, r.AVR_PIK2, r.AVR_PIK3, r.AVR_PIK4, r.AVR_PIK5, r.AVR_PIK6, r.AVR_PIK7, ");
                sql.append("r.AVR_AREA_COD, r.AVR_RACK_COD, r.AVR_PSTN_COD, r.AVR_LVL_COD, ");
                sql.append("r.AVR_RTPC_QTY, r.AVR_PPCS_QTY, r.AVR_PPB_QTY, ");
                sql.append("r.AVR_DMG_FLG ");
                sql.append("FROM ").append(avrSource).append(" r ");
                sql.append("WHERE r.AVR_CPNY_COD = :cpny AND r.AVR_WHS_COD = :whs AND r.AVR_CUST_COD = :cust ");
                sql.append("AND r.DEL_FLG = '0' ");
                sql.append("AND r.AVR_AS_NUM IN (").append(buildInClause(arrivalNumbers.size())).append(")");

                Query query = em.createNativeQuery(sql.toString());
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                applyArrivalNumberParameters(query, arrivalNumbers);
                return query.getResultList();
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Get AVD PSSA flag for a specific detail line.
     */
    public String getDetailPssaFlag(String cpny, String whs, String cust, String avNum, int lineNum) {
        for (String source : AVD_SOURCES) {
            try {
                String sql = "SELECT AVD_PSSA_FLG FROM " + source +
                    " WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
                    "AND AVD_AV_NUM = :avNum AND AVD_AVLN_NUM = :lineNum AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                query.setParameter("lineNum", lineNum);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object result = rows.get(0);
                    return result != null ? result.toString().trim() : "0";
                }
                continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return "0";
    }

    /**
     * Get AVH bond flag and arrival date for stock creation.
     * Returns [AVH_BOND_FLG, AVH_ARV_YMD] or null.
     */
    public Object[] getHeaderBondAndArrivalDate(String cpny, String whs, String cust, String avNum) {
        for (String source : AVH_SOURCES) {
            try {
                String sql = "SELECT AVH_BOND_FLG, AVH_ARV_YMD FROM " + source +
                    " WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                    "AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                @SuppressWarnings("unchecked")
                List<Object[]> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    return rows.get(0);
                }
                continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return null;
    }

    /**
     * Insert one stock record into GWH_TJ_ST.
     * Maps AVR result data to stock fields per legacy GwhAggj1Apcnf010BLC.
     */
    public void insertStock(String cpny, String whs, String cust,
                            String avNum, int lineNum, int seqNum,
                            String stKnd, java.util.Date avYmd,
                            String prodCode, String orgnCode, String sbivCode,
                            String pik1, String pik2, String pik3, String pik4,
                            String pik5, String pik6, String pik7,
                            String areaCod, String rackCod, String pstnCod, String lvlCod,
                            BigDecimal rtpcQty, BigDecimal ppcsQty, BigDecimal ppbQty,
                            String dmgFlg, String bondFlg, String pssaFlg,
                            String userId, String programCode) {
        for (String source : ST_SOURCES) {
            try {
                String sql =
                    "INSERT INTO " + source + " (" +
                    "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                    "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
                    "ST_CPNY_COD, ST_WHS_COD, ST_CUST_COD, ST_AV_NUM, ST_AVLN_NUM, ST_AVSQ_NUM, " +
                    "ST_ST_KND, ST_AV_YMD, ST_PROD_COD, ST_ORGN_COD, ST_SBIV_COD, " +
                    "ST_PIK1, ST_PIK2, ST_PIK3, ST_PIK4, ST_PIK5, ST_PIK6, ST_PIK7, " +
                    "ST_AREA_COD, ST_RACK_COD, ST_PSTN_COD, ST_LVL_COD, " +
                    "ST_PYST_QTY, ST_AVST_QTY, ST_ALST_QTY, ST_PPCS_QTY, ST_PPB_QTY, ST_ORGN_QTY, " +
                    "ST_PSSA_FLG, ST_PSSA_QTY, ST_BOND_FLG, ST_DMG_FLG, ST_LOCK_FLG" +
                    ") VALUES (" +
                    "'0', TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-MODERN', :userId, :pgm, 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
                    "TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-MODERN', :userId, :pgm, 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
                    ":cpny, :whs, :cust, :avNum, :lineNum, :seqNum, " +
                    ":stKnd, :avYmd, :prodCode, :orgnCode, :sbivCode, " +
                    ":pik1, :pik2, :pik3, :pik4, :pik5, :pik6, :pik7, " +
                    ":areaCod, :rackCod, :pstnCod, :lvlCod, " +
                    ":pystQty, :avstQty, 0, :ppcsQty, :ppbQty, :orgnQty, " +
                    ":pssaFlg, 0, :bondFlg, :dmgFlg, 'N'" +
                    ")";

                Query query = em.createNativeQuery(sql);
                query.setParameter("userId", userId != null ? userId : "SYSTEM");
                query.setParameter("pgm", programCode != null ? programCode : "MODERN_APCNF");
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                query.setParameter("lineNum", lineNum);
                query.setParameter("seqNum", seqNum);
                query.setParameter("stKnd", stKnd != null ? stKnd : "N");
                query.setParameter("avYmd", avYmd);
                query.setParameter("prodCode", prodCode);
                query.setParameter("orgnCode", orgnCode);
                query.setParameter("sbivCode", sbivCode != null ? sbivCode : "-");
                query.setParameter("pik1", pik1);
                query.setParameter("pik2", pik2);
                query.setParameter("pik3", pik3);
                query.setParameter("pik4", pik4);
                query.setParameter("pik5", pik5);
                query.setParameter("pik6", pik6);
                query.setParameter("pik7", pik7);
                query.setParameter("areaCod", areaCod != null ? areaCod : "---");
                query.setParameter("rackCod", rackCod);
                query.setParameter("pstnCod", pstnCod);
                query.setParameter("lvlCod", lvlCod);
                query.setParameter("pystQty", rtpcQty != null ? rtpcQty : BigDecimal.ZERO);
                query.setParameter("avstQty", rtpcQty != null ? rtpcQty : BigDecimal.ZERO);
                query.setParameter("ppcsQty", ppcsQty);
                query.setParameter("ppbQty", ppbQty);
                query.setParameter("orgnQty", rtpcQty != null ? rtpcQty : BigDecimal.ZERO);
                query.setParameter("pssaFlg", pssaFlg != null ? pssaFlg : "0");
                query.setParameter("bondFlg", bondFlg);
                query.setParameter("dmgFlg", dmgFlg != null ? dmgFlg : "0");
                query.executeUpdate();
                return;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
    }

    private int confirmArrivalsFallbackMinimal(
        String source,
        String companyCode,
        String warehouseCode,
        String customerCode,
        List<String> arrivalNumbers,
        String finalStatus
    ) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(source).append(" SET AVH_AV_STS = :status ");
        LocalDate nowDate = LocalDate.now();
        sql.append(", UPD_YMD = :updYmd ");
        sql.append("WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust AND DEL_FLG = '0' ");
        sql.append("AND AVH_AV_NUM IN (");
        for (int i = 0; i < arrivalNumbers.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(":avNum").append(i);
        }
        sql.append(")");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("status", finalStatus);
        query.setParameter("updYmd", formatYmd(nowDate));
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        for (int i = 0; i < arrivalNumbers.size(); i++) {
            query.setParameter("avNum" + i, arrivalNumbers.get(i));
        }
        return query.executeUpdate();
    }

    public List<String> findExistingArrivalNumbers(
        String companyCode,
        String warehouseCode,
        String customerCode,
        List<String> arrivalNumbers
    ) {
        if (arrivalNumbers == null || arrivalNumbers.isEmpty()) {
            return new ArrayList<>();
        }

        RuntimeException lastError = null;
        for (String source : AVH_SOURCES) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT AVH_AV_NUM FROM ").append(source).append(" ");
                sql.append("WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust AND DEL_FLG = '0' ");
                sql.append("AND AVH_AV_NUM IN (");
                for (int i = 0; i < arrivalNumbers.size(); i++) {
                    if (i > 0) {
                        sql.append(", ");
                    }
                    sql.append(":avNum").append(i);
                }
                sql.append(")");

                Query query = em.createNativeQuery(sql.toString());
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                for (int i = 0; i < arrivalNumbers.size(); i++) {
                    query.setParameter("avNum" + i, arrivalNumbers.get(i));
                }

                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                List<String> result = new ArrayList<>();
                for (Object row : rows) {
                    result.add(toStringValue(row));
                }
                return result;
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
                lastError = ex;
            }
        }

        if (lastError != null) {
            throw lastError;
        }
        return new ArrayList<>();
    }

    public List<String> findQuantityMismatchArrivalNumbers(
        String companyCode,
        String warehouseCode,
        String customerCode,
        List<String> arrivalNumbers
    ) {
        if (arrivalNumbers == null || arrivalNumbers.isEmpty()) {
            return new ArrayList<>();
        }

        RuntimeException lastError = null;
        for (String avdSource : AVD_SOURCES) {
            for (String avrSource : AVR_SOURCES) {
                try {
                    StringBuilder inClause = new StringBuilder();
                    for (int i = 0; i < arrivalNumbers.size(); i++) {
                        if (i > 0) {
                            inClause.append(", ");
                        }
                        inClause.append(":avNum").append(i);
                    }

                    String sql =
                        "SELECT D.AVD_AV_NUM " +
                        "FROM " + avdSource + " D " +
                        "LEFT JOIN " +
                        "(SELECT AVR_AS_NUM, AVR_ASLN_NUM, SUM(NVL(AVR_RTPC_QTY, 0)) AS TOTAL_RTPC " +
                        " FROM " + avrSource +
                        " WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust AND DEL_FLG = '0' " +
                        " GROUP BY AVR_AS_NUM, AVR_ASLN_NUM) R " +
                        "ON D.AVD_AV_NUM = R.AVR_AS_NUM AND D.AVD_AVLN_NUM = R.AVR_ASLN_NUM " +
                        "WHERE D.AVD_CPNY_COD = :cpny AND D.AVD_WHS_COD = :whs AND D.AVD_CUST_COD = :cust AND D.DEL_FLG = '0' " +
                        "AND D.AVD_AV_NUM IN (" + inClause + ") " +
                        "GROUP BY D.AVD_AV_NUM, D.AVD_AVLN_NUM, D.AVD_STPC_QTY, R.TOTAL_RTPC " +
                        "HAVING NVL(D.AVD_STPC_QTY, 0) <> NVL(R.TOTAL_RTPC, 0)";

                    Query query = em.createNativeQuery(sql);
                    query.setParameter("cpny", companyCode);
                    query.setParameter("whs", warehouseCode);
                    query.setParameter("cust", customerCode);
                    for (int i = 0; i < arrivalNumbers.size(); i++) {
                        query.setParameter("avNum" + i, arrivalNumbers.get(i));
                    }

                    @SuppressWarnings("unchecked")
                    List<Object> rows = query.getResultList();
                    List<String> mismatches = new ArrayList<>();
                    for (Object row : rows) {
                        String avNum = toStringValue(row);
                        if (avNum != null && !mismatches.contains(avNum)) {
                            mismatches.add(avNum);
                        }
                    }
                    return mismatches;
                } catch (RuntimeException ex) {
                    if (isObjectNotFound(ex) || isInvalidIdentifier(ex) || isSqlGrammarIssue(ex)) {
                        lastError = ex;
                        continue;
                    }
                    throw ex;
                }
            }
        }

        if (lastError != null) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    private void applyParams(Query query, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    private boolean isObjectNotFound(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && message.contains("ORA-00942")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private boolean isInvalidIdentifier(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && message.contains("ORA-00904")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private boolean isSqlGrammarIssue(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && (message.contains("ORA-00933") || message.contains("ORA-01747"))) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private String toStringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String defaultText(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }

    private long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (Exception ex) {
            return 0L;
        }
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return ((Date) value).toLocalDate();
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime().toLocalDate();
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        return null;
    }

    private void updateArrivalDetailsStatus(
        String companyCode,
        String warehouseCode,
        String customerCode,
        List<String> arrivalNumbers,
        String finalStatus,
        String updYmd,
        String updTim,
        String updTmid,
        String updUser,
        String updPgm,
        LocalDateTime nowDateTime
    ) {
        RuntimeException lastSchemaError = null;
        for (String source : AVD_SOURCES) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE ").append(source).append(" SET ");
                sql.append("AVD_AV_STS = :status, ");
                sql.append("UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, UPD_USER = :updUser, UPD_PGM = :updPgm, ");
                sql.append("UPD_TM_ZONE = 'JST', UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms ");
                sql.append("WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust AND DEL_FLG = '0' ");
                sql.append("AND AVD_AV_NUM IN (").append(buildInClause(arrivalNumbers.size())).append(")");

                Query query = em.createNativeQuery(sql.toString());
                applyAuditParameters(query, updYmd, updTim, updTmid, updUser, updPgm, nowDateTime);
                query.setParameter("status", finalStatus);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                applyArrivalNumberParameters(query, arrivalNumbers);
                query.executeUpdate();
                return;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex) || isInvalidIdentifier(ex) || isSqlGrammarIssue(ex)) {
                    lastSchemaError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastSchemaError != null) {
            return;
        }
    }

    private void updateArrivalResultsStatus(
        String companyCode,
        String warehouseCode,
        String customerCode,
        List<String> arrivalNumbers,
        LocalDate effectiveArrivalDate,
        String finalStatus,
        String updYmd,
        String updTim,
        String updTmid,
        String updUser,
        String updPgm,
        LocalDateTime nowDateTime
    ) {
        RuntimeException lastSchemaError = null;
        for (String source : AVR_SOURCES) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE ").append(source).append(" SET ");
                sql.append("AVR_AVSP_YMD = :avspYmd, AVR_AVSP_STS = :status, AVR_CNFM_FLG = 'Y', ");
                sql.append("UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, UPD_USER = :updUser, UPD_PGM = :updPgm, ");
                sql.append("UPD_TM_ZONE = 'JST', UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms ");
                sql.append("WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust AND DEL_FLG = '0' ");
                sql.append("AND AVR_AS_NUM IN (").append(buildInClause(arrivalNumbers.size())).append(")");

                Query query = em.createNativeQuery(sql.toString());
                applyAuditParameters(query, updYmd, updTim, updTmid, updUser, updPgm, nowDateTime);
                query.setParameter("avspYmd", Date.valueOf(effectiveArrivalDate));
                query.setParameter("status", finalStatus);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                applyArrivalNumberParameters(query, arrivalNumbers);
                query.executeUpdate();
                return;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex) || isInvalidIdentifier(ex) || isSqlGrammarIssue(ex)) {
                    lastSchemaError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastSchemaError != null) {
            return;
        }
    }

    private void applyAuditParameters(
        Query query,
        String updYmd,
        String updTim,
        String updTmid,
        String updUser,
        String updPgm,
        LocalDateTime nowDateTime
    ) {
        query.setParameter("updYmd", updYmd);
        query.setParameter("updTim", updTim);
        query.setParameter("updTmid", updTmid);
        query.setParameter("updUser", updUser);
        query.setParameter("updPgm", updPgm);
        query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));
    }

    private void applyArrivalNumberParameters(Query query, List<String> arrivalNumbers) {
        for (int i = 0; i < arrivalNumbers.size(); i++) {
            query.setParameter("avNum" + i, arrivalNumbers.get(i));
        }
    }

    private String buildInClause(int size) {
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                inClause.append(", ");
            }
            inClause.append(":avNum").append(i);
        }
        return inClause.toString();
    }

    private String formatYmd(LocalDate date) {
        return date.format(YMD_FORMATTER);
    }

    private String formatHms(LocalTime time) {
        return time.format(HMS_FORMATTER);
    }

    // =========================================================================
    // Standard confirmation queries (matching GwhSrvcApWeb/GwhApcnf010DAO)
    // =========================================================================

    /**
     * Get customer master fields needed for confirmation.
     * Returns [CUST_ST_KND, CUST_GET_KND, CUST_PCSM_FLG, CUST_PADS_KND] or null if not found.
     */
    public Object[] getCustomerMaster(String cpny, String whs, String cust) {
        for (String source : CUST_SOURCES) {
            try {
                String sql = "SELECT CUST_ST_KND, CUST_GET_KND, CUST_PCSM_FLG, CUST_PADS_KND FROM " + source +
                    " WHERE CUST_CPNY_COD = :cpny AND CUST_WHS_COD = :whs AND CUST_COD = :cust AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                @SuppressWarnings("unchecked")
                List<Object[]> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    return rows.get(0);
                }
                continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return null;
    }

    /**
     * Get operation date (OPD_YMD) from GWH_TM_OPD.
     */
    public String getOperationDate(String cpny, String whs, String cust) {
        String[] sources = {"GWH.GWH_TM_OPD", "GWH_TM_OPD"};
        for (String source : sources) {
            try {
                String sql = "SELECT OPD_YMD FROM " + source +
                    " WHERE OPD_CPNY_COD = :cpny AND OPD_WHS_COD = :whs AND OPD_CUST_COD = :cust AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object result = rows.get(0);
                    return result != null ? result.toString().trim() : null;
                }
                continue;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return null;
    }

    /**
     * Get picking key auto flag number from GWH_TM_PIK.
     * Returns PIK_NUM where PIK_AUTO_FLG = 'Y', or null if not configured.
     */
    public Integer getPikAutoFlag(String cpny, String whs, String cust) {
        String[] sources = {"GWH.GWH_TM_PIK", "GWH_TM_PIK"};
        for (String source : sources) {
            try {
                String sql = "SELECT PIK_NUM FROM " + source +
                    " WHERE PIK_CPNY_COD = :cpny AND PIK_WHS_COD = :whs AND PIK_CUST_COD = :cust" +
                    " AND PIK_AUTO_FLG = 'Y' ORDER BY PIK_NUM ASC";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setMaxResults(1);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object val = rows.get(0);
                    if (val instanceof Number) return ((Number) val).intValue();
                    return Integer.parseInt(val.toString().trim());
                }
                return null;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return null;
    }

    /**
     * Get future arrival date permission flag from GWH_TM_CODE.
     * CODE_KND='367', CODE_COD='01'. Returns CODE_INFO1 ('Y' or 'N').
     */
    public String getFutureArrivalDatePermission(String cpny, String whs, String cust) {
        String[] sources = {"GWH.VGWH_TM_CODE", "VGWH_TM_CODE", "GWH.GWH_TM_CODE", "GWH_TM_CODE"};
        for (String source : sources) {
            try {
                String sql = "SELECT CODE_INFO1 FROM " + source +
                    " WHERE CODE_KND = '367' AND CODE_COD = '01'" +
                    " AND (CODE_CPNY_COD = :cpny OR CODE_CPNY_COD = '*')" +
                    " AND (CODE_WHS_COD = :whs OR CODE_WHS_COD = '*')" +
                    " AND (CODE_CUST_COD = :cust OR CODE_CUST_COD = '*')";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setMaxResults(1);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                if (rows != null && !rows.isEmpty()) {
                    Object val = rows.get(0);
                    return val != null ? val.toString().trim() : "Y";
                }
                return "Y";
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return "Y";
    }

    /**
     * Get arrival data joined (AVR+AVD+AVH+TRN) for a single arrival.
     * Matches standard DAO getArrivalData query.
     * Returns list of Object[] with fields per AVR row:
     * [0]AVD_STPC_QTY, [1]TOTAL_AVR_RTPC_QTY, [2]ARV_UPD_YMDHMS, [3]AVH_BOND_FLG,
     * [4]TRN_SPPA_FLG, [5]TRN_SHPA_FLG, [6]AVR_AS_NUM, [7]AVR_ASLN_NUM, [8]AVR_ASSQ_NUM,
     * [9]AVR_PROD_COD, [10]AVR_ORGN_COD, [11]AVR_SBIV_COD,
     * [12-18]AVR_PIK1..7, [19]AVR_AREA_COD, [20]AVR_RACK_COD, [21]AVR_PSTN_COD, [22]AVR_LVL_COD,
     * [23]AVR_RTPC_QTY, [24]AVR_PPCS_QTY, [25]AVR_PPB_QTY, [26]AVR_DMG_FLG,
     * [27]AVD_PCUM_PCS, [28]AVD_PCUM_CS, [29]AVD_PSSA_FLG, [30]AVD_AVLN_NUM
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getArrivalData(String cpny, String whs, String cust, String avNum) {
        String sql =
            "SELECT T2.AVD_STPC_QTY" +                                             // [0]
            ", T4.AVR_RTPC_QTY AS TOTAL_AVR_RTPC_QTY" +                              // [1] TOTAL per line
            ", T3.UPD_YMDHMS AS ARV_UPD_YMDHMS" +                                   // [2]
            ", T3.AVH_BOND_FLG" +                                                   // [3]
            ", M1.TRN_SPPA_FLG" +                                                   // [4]
            ", M1.TRN_SHPA_FLG" +                                                   // [5]
            ", T1.AVR_AS_NUM, T1.AVR_ASLN_NUM, T1.AVR_ASSQ_NUM" +                   // [6-8]
            ", T1.AVR_PROD_COD, T1.AVR_ORGN_COD, T1.AVR_SBIV_COD" +                 // [9-11]
            ", T1.AVR_PIK1, T1.AVR_PIK2, T1.AVR_PIK3, T1.AVR_PIK4" +               // [12-15]
            ", T1.AVR_PIK5, T1.AVR_PIK6, T1.AVR_PIK7" +                             // [16-18]
            ", T1.AVR_AREA_COD, T1.AVR_RACK_COD, T1.AVR_PSTN_COD, T1.AVR_LVL_COD" + // [19-22]
            ", T1.AVR_RTPC_QTY, T1.AVR_PPCS_QTY, T1.AVR_PPB_QTY" +                  // [23-25]
            ", T1.AVR_DMG_FLG" +                                                     // [26]
            ", T2.AVD_PCUM_PCS, T2.AVD_PCUM_CS, T2.AVD_PSSA_FLG" +                   // [27-29]
            ", T2.AVD_AVLN_NUM" +                                                     // [30]
            " FROM GWH.GWH_TJ_AV_R T1" +
            " INNER JOIN GWH.GWH_TJ_AV_D T2 ON" +
            " T1.AVR_CPNY_COD = T2.AVD_CPNY_COD" +
            " AND T1.AVR_WHS_COD = T2.AVD_WHS_COD" +
            " AND T1.AVR_CUST_COD = T2.AVD_CUST_COD" +
            " AND T1.AVR_AS_NUM = T2.AVD_AV_NUM" +
            " AND T1.AVR_ASLN_NUM = T2.AVD_AVLN_NUM" +
            " INNER JOIN GWH.GWH_TJ_AV_H T3 ON" +
            " T1.AVR_CPNY_COD = T3.AVH_CPNY_COD" +
            " AND T1.AVR_WHS_COD = T3.AVH_WHS_COD" +
            " AND T1.AVR_CUST_COD = T3.AVH_CUST_COD" +
            " AND T1.AVR_AS_NUM = T3.AVH_AV_NUM" +
            " INNER JOIN (" +
            "   SELECT AVR_CPNY_COD, AVR_WHS_COD, AVR_CUST_COD, AVR_AS_NUM, AVR_ASLN_NUM," +
            "     SUM(AVR_RTPC_QTY) AS AVR_RTPC_QTY" +
            "   FROM GWH.GWH_TJ_AV_R" +
            "   WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust" +
            "     AND AVR_AS_NUM = :avNum AND DEL_FLG = '0'" +
            "   GROUP BY AVR_CPNY_COD, AVR_WHS_COD, AVR_CUST_COD, AVR_AS_NUM, AVR_ASLN_NUM" +
            " ) T4 ON" +
            " T1.AVR_CPNY_COD = T4.AVR_CPNY_COD" +
            " AND T1.AVR_WHS_COD = T4.AVR_WHS_COD" +
            " AND T1.AVR_CUST_COD = T4.AVR_CUST_COD" +
            " AND T1.AVR_AS_NUM = T4.AVR_AS_NUM" +
            " AND T1.AVR_ASLN_NUM = T4.AVR_ASLN_NUM" +
            " LEFT JOIN GWH.GWH_TM_TRN M1 ON T3.AVH_TRN_KND = M1.TRN_KND" +
            "   AND M1.TRN_CPNY_COD = :cpny AND M1.TRN_WHS_COD = :whs AND M1.TRN_CUST_COD = :cust" +
            "   AND M1.DEL_FLG = '0'" +
            " WHERE T1.AVR_CPNY_COD = :cpny AND T1.AVR_WHS_COD = :whs AND T1.AVR_CUST_COD = :cust" +
            " AND T1.AVR_AS_NUM = :avNum AND T1.AVR_CNFM_FLG = 'N' AND T1.DEL_FLG = '0'" +
            " ORDER BY T1.AVR_CPNY_COD, T1.AVR_WHS_COD, T1.AVR_CUST_COD, T1.AVR_AS_NUM, T1.AVR_ASLN_NUM, T1.AVR_ASSQ_NUM";

        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", cpny);
        query.setParameter("whs", whs);
        query.setParameter("cust", cust);
        query.setParameter("avNum", avNum);
        return query.getResultList();
    }

    /**
     * Check AVD status gate โ€” returns true if any detail line has status < PRCC_RSTS
     * with STS_WKST_KND in ('2','3'). Matches standard getAvdStatus.
     */
    public boolean hasDetailsBelowRequiredStatus(String cpny, String whs, String cust,
                                                  String avNum, String prccRsts) {
        for (String source : AVD_SOURCES) {
            try {
                String sql = "SELECT COUNT(*) FROM " + source + " T1" +
                    " INNER JOIN GWH.GWH_TM_STS T2 ON T1.AVD_AV_STS = T2.STS_COD" +
                    " AND (T2.STS_WKST_KND = '2' OR T2.STS_WKST_KND = '3')" +
                    " AND T2.STS_BSNS_COD = 'AV'" +
                    " AND T2.STS_CPNY_COD = :cpny AND T2.STS_WHS_COD = :whs AND T2.STS_CUST_COD = :cust" +
                    " AND T2.DEL_FLG = '0'" +
                    " WHERE T1.AVD_CPNY_COD = :cpny AND T1.AVD_WHS_COD = :whs AND T1.AVD_CUST_COD = :cust" +
                    " AND T1.AVD_AV_NUM = :avNum AND T1.AVD_AV_STS < :prccRsts AND T1.DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                query.setParameter("prccRsts", prccRsts);
                @SuppressWarnings("unchecked")
                List<Object> rows = query.getResultList();
                long count = (rows != null && !rows.isEmpty()) ? toLong(rows.get(0)) : 0;
                return count > 0;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
        return false;
    }

    /**
     * Check serial completeness per arrival. Matches standard checkSerialComplete.
     * Returns [TOTAL_INSPECT_QTY, TOTAL_INPUT_SERIAL_QTY] or null.
     */
    public Object[] checkSerialComplete(String cpny, String whs, String cust, String avNum) {
        try {
            String sql =
                "SELECT NVL(SUM(TJ1.AVR_RTPC_QTY), 0) AS TOTAL_INSPECT_QTY" +
                ", NVL(SUM(TJ2.SRL_TINP_QTY), 0) AS TOTAL_INPUT_SERIAL_QTY" +
                " FROM GWH.GWH_TJ_AV_R TJ1" +
                " INNER JOIN GWH.GWH_TM_PROD TM1 ON" +
                " TJ1.AVR_CPNY_COD = TM1.PROD_CPNY_COD" +
                " AND TJ1.AVR_WHS_COD = TM1.PROD_WHS_COD" +
                " AND TJ1.AVR_CUST_COD = TM1.PROD_CUST_COD" +
                " AND TJ1.AVR_PROD_COD = TM1.PROD_COD" +
                " AND TM1.PROD_SLGT_FLG = 'Y' AND TM1.DEL_FLG = '0'" +
                " LEFT JOIN (" +
                "   SELECT SRL_CPNY_COD, SRL_WHS_COD, SRL_CUST_COD, SRL_AV_NUM, SRL_AVLN_NUM," +
                "     SRL_AVSQ_NUM, SRL_PROD_COD, SRL_ORGN_COD, SRL_AS_NUM," +
                "     COUNT(1) AS SRL_TINP_QTY" +
                "   FROM GWH.GWH_TJ_SRL" +
                "   WHERE SRL_CPNY_COD = :cpny AND SRL_WHS_COD = :whs AND SRL_CUST_COD = :cust" +
                "     AND SRL_AS_NUM = :avNum AND SRL_AS_KND = 'AV' AND DEL_FLG = '0'" +
                "   GROUP BY SRL_CPNY_COD, SRL_WHS_COD, SRL_CUST_COD, SRL_AV_NUM," +
                "     SRL_AVLN_NUM, SRL_AVSQ_NUM, SRL_PROD_COD, SRL_ORGN_COD, SRL_AS_NUM" +
                " ) TJ2 ON" +
                " TJ1.AVR_CPNY_COD = TJ2.SRL_CPNY_COD" +
                " AND TJ1.AVR_WHS_COD = TJ2.SRL_WHS_COD" +
                " AND TJ1.AVR_CUST_COD = TJ2.SRL_CUST_COD" +
                " AND TJ1.AVR_AS_NUM = TJ2.SRL_AV_NUM" +
                " AND TJ1.AVR_ASLN_NUM = TJ2.SRL_AVLN_NUM" +
                " AND TJ1.AVR_ASSQ_NUM = TJ2.SRL_AVSQ_NUM" +
                " AND TJ1.AVR_PROD_COD = TJ2.SRL_PROD_COD" +
                " AND NVL(TJ1.AVR_ORGN_COD, '  ') = NVL(TJ2.SRL_ORGN_COD, '  ')" +
                " AND TJ1.AVR_AS_NUM = TJ2.SRL_AS_NUM" +
                " WHERE TJ1.AVR_CPNY_COD = :cpny AND TJ1.AVR_WHS_COD = :whs" +
                " AND TJ1.AVR_CUST_COD = :cust AND TJ1.AVR_AS_NUM = :avNum AND TJ1.DEL_FLG = '0'";
            Query query = em.createNativeQuery(sql);
            query.setParameter("cpny", cpny);
            query.setParameter("whs", whs);
            query.setParameter("cust", cust);
            query.setParameter("avNum", avNum);
            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();
            if (rows != null && !rows.isEmpty()) {
                return rows.get(0);
            }
            return null;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Get stock-taking status (STKH_STK_STS) and UI/UO flags. Matches standard getUIUOFlag + getStockTakingStatus.
     * Returns [STKH_STK_STS, TRN_UIUS_FLG, TRN_UOUS_FLG] or null.
     */
    public Object[] getStockTakingInfo(String cpny, String whs, String cust) {
        try {
            String sql =
                "SELECT S.STKH_STK_STS, T.TRN_UIUS_FLG, T.TRN_UOUS_FLG" +
                " FROM GWH.GWH_TJ_STK_H S" +
                " LEFT JOIN GWH.GWH_TM_TRN T ON T.TRN_CPNY_COD = :cpny AND T.TRN_WHS_COD = :whs" +
                "   AND T.TRN_CUST_COD = :cust AND T.DEL_FLG = '0'" +
                " WHERE S.STKH_CPNY_COD = :cpny AND S.STKH_WHS_COD = :whs" +
                " AND S.STKH_CUST_COD = :cust AND S.DEL_FLG = '0'" +
                " AND ROWNUM = 1";
            Query query = em.createNativeQuery(sql);
            query.setParameter("cpny", cpny);
            query.setParameter("whs", whs);
            query.setParameter("cust", cust);
            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();
            if (rows != null && !rows.isEmpty()) {
                return rows.get(0);
            }
            return null;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Insert XT (trace) record per AVR line. Matches standard gwhCpxt001Reg.
     * XT unique key: XT_CPNY_COD, XT_WHS_COD, XT_CUST_COD, XT_AS_NUM, XT_ASLN_NUM, XT_ASSQ_NUM, UPD_YMDHMS
     */
    public void insertXt(String cpny, String whs, String cust,
                          String asNum, int aslnNum, int assqNum,
                          String asKnd, String opKnd, String oprtUser,
                          String endYmd, String endTim,
                          String userCode, String programCode) {
        String[] sources = {"GWH.GWH_TJ_XT", "GWH_TJ_XT"};
        for (String source : sources) {
            try {
                String sql =
                    "INSERT INTO " + source + " (" +
                    "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                    "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
                    "XT_CPNY_COD, XT_WHS_COD, XT_CUST_COD, XT_AS_NUM, XT_ASLN_NUM, XT_ASSQ_NUM, " +
                    "XT_AS_KND, XT_OP_KND, XT_OPRT_USER, XT_EVNT_RMKS, " +
                    "XT_STR_YMD, XT_STR_TIM, XT_END_YMD, XT_END_TIM" +
                    ") VALUES (" +
                    "'0', :crtYmd, :crtTim, 'GWH-MODERN', :crtUser, :pgm, 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
                    ":updYmd, :updTim, 'GWH-MODERN', :updUser, :pgm, 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
                    ":cpny, :whs, :cust, :asNum, :aslnNum, :assqNum, " +
                    ":asKnd, :opKnd, :oprtUser, NULL, " +
                    "NULL, NULL, :endYmd, :endTim" +
                    ")";

                LocalDate nowDate = LocalDate.now();
                LocalTime nowTime = LocalTime.now();
                String ymd = formatYmd(nowDate);
                String tim = formatHms(nowTime);
                String userId = userCode != null ? userCode : "SYSTEM";

                Query query = em.createNativeQuery(sql);
                query.setParameter("crtYmd", ymd);
                query.setParameter("crtTim", tim);
                query.setParameter("crtUser", userId);
                query.setParameter("pgm", programCode != null ? programCode : "MODERN_APCNF");
                query.setParameter("updYmd", ymd);
                query.setParameter("updTim", tim);
                query.setParameter("updUser", userId);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("asNum", asNum);
                query.setParameter("aslnNum", aslnNum);
                query.setParameter("assqNum", assqNum);
                query.setParameter("asKnd", asKnd);
                query.setParameter("opKnd", opKnd);
                query.setParameter("oprtUser", oprtUser != null ? oprtUser : userId);
                query.setParameter("endYmd", endYmd != null ? endYmd : ymd);
                query.setParameter("endTim", endTim != null ? endTim : tim);
                query.executeUpdate();
                return;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                // Duplicate XT โ€” log and skip
                String msg = ex.getMessage();
                if (msg != null && (msg.contains("ORA-00001") || msg.contains("unique constraint"))) {
                    return;
                }
                throw ex;
            }
        }
    }

    /**
     * Get transaction kind partial delivery flag (TRN_PDAC_FLG) for an arrival.
     * Matches standard getTransactionKind.
     */
    public String getTransactionKindPartialFlag(String cpny, String whs, String cust, String avNum) {
        try {
            String sql = "SELECT T2.TRN_PDAC_FLG FROM GWH.GWH_TJ_AV_H T1" +
                " LEFT JOIN GWH.GWH_TM_TRN T2 ON T1.AVH_TRN_KND = T2.TRN_KND" +
                "   AND T2.TRN_CPNY_COD = :cpny AND T2.TRN_WHS_COD = :whs AND T2.TRN_CUST_COD = :cust" +
                "   AND T2.DEL_FLG = '0'" +
                " WHERE T1.AVH_AV_NUM = :avNum AND T1.AVH_CPNY_COD = :cpny" +
                " AND T1.AVH_WHS_COD = :whs AND T1.AVH_CUST_COD = :cust AND T1.DEL_FLG = '0'";
            Query query = em.createNativeQuery(sql);
            query.setParameter("cpny", cpny);
            query.setParameter("whs", whs);
            query.setParameter("cust", cust);
            query.setParameter("avNum", avNum);
            @SuppressWarnings("unchecked")
            List<Object> rows = query.getResultList();
            if (rows != null && !rows.isEmpty()) {
                Object result = rows.get(0);
                return result != null ? result.toString().trim() : null;
            }
            return null;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Update single AVR with PIK key override. Used when pikNum is set.
     */
    public void updateAvrPikKey(String cpny, String whs, String cust, String avNum,
                                 int pikNum, String pikValue) {
        for (String source : AVR_SOURCES) {
            try {
                String sql = "UPDATE " + source + " SET AVR_PIK" + pikNum + " = :pikVal" +
                    " WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust" +
                    " AND AVR_AS_NUM = :avNum AND DEL_FLG = '0'";
                Query query = em.createNativeQuery(sql);
                query.setParameter("pikVal", pikValue);
                query.setParameter("cpny", cpny);
                query.setParameter("whs", whs);
                query.setParameter("cust", cust);
                query.setParameter("avNum", avNum);
                query.executeUpdate();
                return;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) continue;
                throw ex;
            }
        }
    }

    public static class SearchResult {
        private final List<ArrivalConfirmationDto> records;
        private final long totalRecords;

        public SearchResult(List<ArrivalConfirmationDto> records, long totalRecords) {
            this.records = records;
            this.totalRecords = totalRecords;
        }

        public List<ArrivalConfirmationDto> getRecords() {
            return records;
        }

        public long getTotalRecords() {
            return totalRecords;
        }
    }
}
