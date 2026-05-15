package jp.co.nittsu.gwh.module.inbound.repository;

import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InboundOrderRegistrationRepository {

    private static final String[] SUPPLIER_SOURCES = {
        "SGWH0001.GWH_TM_SPL",
        "GWH_TM_SPL"
    };

    private static final String[] PRODUCT_SOURCES = {
        "SGWH0001.GWH_TM_PROD",
        "GWH_TM_PROD",
        "SGWH0001.GWH_TM_PRD",
        "GWH_TM_PRD"
    };

    private static final String[] ORIGIN_SOURCES = {
        "SGWH0001.GWH_TM_ORG",
        "SGWH0001.GWH_TM_ORGN",
        "GWH_TM_ORG",
        "GWH_TM_ORGN"
    };

    private static final String[] TRN_SOURCES = {
        "SGWH0001.VGWH_TM_TRN",
        "VGWH_TM_TRN",
        "SGWH0001.GWH_TM_TRN",
        "GWH_TM_TRN"
    };

    private static final String[] PIK_SOURCES = {
        "SGWH0001.VGWH_TM_PIK",
        "VGWH_TM_PIK",
        "SGWH0001.GWH_TM_PIK",
        "GWH_TM_PIK"
    };

    private static final String[] CODE_SOURCES = {
        "SGWH0001.VGWH_TM_CODE",
        "VGWH_TM_CODE",
        "SGWH0001.GWH_TM_CODE",
        "GWH_TM_CODE"
    };

    private static final String[] PRCC_SOURCES = {
        "SGWH0001.VGWH_TM_PRCC",
        "VGWH_TM_PRCC",
        "SGWH0001.GWH_TM_PRCC",
        "GWH_TM_PRCC"
    };

    private static final String[] XT_SOURCES = {
        "SGWH0001.GWH_TJ_XT",
        "GWH_TJ_XT"
    };

    private static final String[] XT_KPI_SOURCES = {
        "SGWH0001.GWH_TJ_XT_KPI",
        "GWH_TJ_XT_KPI"
    };

    private static final String[] XT_KPI_SEQUENCES = {
        "SGWH0001.SEQGWH_TJ_XT_KPI",
        "SEQGWH_TJ_XT_KPI"
    };

    private static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter HMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    private static final String DEFAULT_FALSE_FLAG = "N";
    private static final String DEFAULT_SUB_INVENTORY_CODE = "-";

    @PersistenceContext
    private EntityManager em;

    public boolean existsArrivalNumber(String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {
        String sql = "SELECT COUNT(*) FROM SGWH0001.GWH_TJ_AV_H " +
                "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                "AND AVH_AV_NUM = :arrivalNo AND DEL_FLG = 0";
        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("arrivalNo", arrivalNumber);

        Number count = (Number) query.getSingleResult();
        return count != null && count.longValue() > 0;
    }

    public boolean existsReferenceNumber(String companyCode, String warehouseCode, String customerCode, String referenceNumber) {
        if (trimToNull(referenceNumber) == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM SGWH0001.GWH_TJ_AV_H " +
                "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                "AND AVH_RF_NUM = :rfNo AND DEL_FLG = 0";
        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("rfNo", referenceNumber);
        Number count = (Number) query.getSingleResult();
        return count != null && count.longValue() > 0;
    }

    public boolean existsPurchaseOrderNumber(String companyCode, String warehouseCode, String customerCode, String poNumber) {
        if (trimToNull(poNumber) == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM SGWH0001.GWH_TJ_AV_H " +
                "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                "AND AVH_PO_NUM = :poNo AND DEL_FLG = 0";
        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("poNo", poNumber);
        Number count = (Number) query.getSingleResult();
        return count != null && count.longValue() > 0;
    }

    public boolean existsSupplierCode(String companyCode, String warehouseCode, String customerCode, String supplierCode) {
        return existsInSources(
                SUPPLIER_SOURCES,
                "SPL_COD",
                supplierCode,
                companyCode,
                warehouseCode,
                customerCode,
                "SPL_CPNY_COD",
                "SPL_WHS_COD",
                "SPL_CUST_COD",
                "DEL_FLG"
        );
    }

    public boolean existsProductCode(String companyCode, String warehouseCode, String customerCode, String productCode) {
        return existsInSources(
                PRODUCT_SOURCES,
                "PROD_COD",
                productCode,
                companyCode,
                warehouseCode,
                customerCode,
                "PROD_CPNY_COD",
                "PROD_WHS_COD",
                "PROD_CUST_COD",
                "DEL_FLG"
        );
    }

    public boolean existsOriginCode(String companyCode, String warehouseCode, String customerCode, String originCode) {
        return existsInSources(
                ORIGIN_SOURCES,
                "COMCNTRY_COD",
                originCode,
                companyCode,
                warehouseCode,
                customerCode,
                null,
                null,
                null,
                "DEL_FLG"
        );
    }

    public TransactionRule getTransactionRule(String companyCode, String warehouseCode, String customerCode, String transactionKind) {
        String trnKind = trimToNull(transactionKind);
        if (trnKind == null) {
            return null;
        }

        RuntimeException lastError = null;
        for (String source : TRN_SOURCES) {
            try {
                String sql = "SELECT TRN_RSNR_FLG, TRN_PKDP_FLG FROM (" +
                        "  SELECT TRN_RSNR_FLG, TRN_PKDP_FLG, ROW_NUMBER() OVER (PARTITION BY TRN_KND" +
                        "    ORDER BY LENGTH(TRN_CPNY_COD || TRN_WHS_COD || TRN_CUST_COD) DESC) PRIORITY_SEQ" +
                        "  FROM " + source +
                        "  WHERE (TRN_CPNY_COD = :cpny OR TRN_CPNY_COD = '*')" +
                        "    AND (TRN_WHS_COD = :whs OR TRN_WHS_COD = '*')" +
                        "    AND (TRN_CUST_COD = :cust OR TRN_CUST_COD = '*')" +
                        "    AND TRN_KND = :trnKind AND TRN_AVUS_FLG = 'Y'" +
                        ") WHERE PRIORITY_SEQ = 1";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                query.setParameter("trnKind", trnKind);

                List<?> rows = query.getResultList();
                if (rows == null || rows.isEmpty()) {
                    return null;
                }

                Object first = rows.get(0);
                if (!(first instanceof Object[])) {
                    return null;
                }
                Object[] columns = (Object[]) first;
                String reasonRequiredFlag = columns.length > 0 ? trimToNull(valueAsString(columns[0])) : null;
                String pkdpFlag = columns.length > 1 ? trimToNull(valueAsString(columns[1])) : null;
                return new TransactionRule(reasonRequiredFlag, pkdpFlag);
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastError != null) {
            return null;
        }
        return null;
    }

    public List<PikSetting> getPikSettings(String companyCode, String warehouseCode, String customerCode) {
        RuntimeException lastError = null;
        for (String source : PIK_SOURCES) {
            try {
                String sql = "SELECT PIK_NUM, PIK_USE_FLG, PIK_IPLT_KND, PIK_AVSC_KND, PIK_TYPE_KND, PIK_SGDG, PIK_IPFM_KND " +
                        "FROM " + source + " " +
                        "WHERE PIK_CPNY_COD = :cpny AND PIK_WHS_COD = :whs AND PIK_CUST_COD = :cust " +
                        "ORDER BY PIK_NUM";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);

                List<?> rows = query.getResultList();
                if (rows == null || rows.isEmpty()) {
                    return Collections.emptyList();
                }

                List<PikSetting> result = new ArrayList<>();
                for (Object row : rows) {
                    if (!(row instanceof Object[])) {
                        continue;
                    }
                    Object[] columns = (Object[]) row;
                    int pikNum = columns.length > 0 ? toInt(columns[0], 0) : 0;
                    if (pikNum <= 0) {
                        continue;
                    }
                    String useFlag = columns.length > 1 ? trimToNull(valueAsString(columns[1])) : null;
                    String inputRequiredFlag = columns.length > 2 ? trimToNull(valueAsString(columns[2])) : null;
                    String scanKind = columns.length > 3 ? trimToNull(valueAsString(columns[3])) : null;
                    String typeKind = columns.length > 4 ? trimToNull(valueAsString(columns[4])) : null;
                    Integer maxLength = columns.length > 5 ? toNullableInt(columns[5]) : null;
                    String inputFormatKind = columns.length > 6 ? trimToNull(valueAsString(columns[6])) : null;
                    result.add(new PikSetting(pikNum, useFlag, inputRequiredFlag, scanKind, typeKind, maxLength, inputFormatKind));
                }
                return result;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastError != null) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public Map<String, String> getDateFormatByCode() {
        RuntimeException lastError = null;
        for (String source : CODE_SOURCES) {
            try {
                String sql = "SELECT CODE_COD, CODE_INFO1 FROM " + source + " WHERE CODE_KND = '042' ORDER BY CODE_COD";
                Query query = em.createNativeQuery(sql);
                List<?> rows = query.getResultList();
                if (rows == null || rows.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, String> result = new HashMap<>();
                for (Object row : rows) {
                    if (!(row instanceof Object[])) {
                        continue;
                    }
                    Object[] columns = (Object[]) row;
                    String code = columns.length > 0 ? trimToNull(valueAsString(columns[0])) : null;
                    String format = columns.length > 1 ? trimToNull(valueAsString(columns[1])) : null;
                    if (code != null && format != null) {
                        result.put(code, format);
                    }
                }
                return result;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastError != null) {
            return Collections.emptyMap();
        }
        return Collections.emptyMap();
    }

    public String getProcessFinalStatus(String companyCode, String warehouseCode, String customerCode, String processCode) {
        String prcsCode = trimToNull(processCode);
        if (prcsCode == null) {
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
                query.setParameter("prcsCod", prcsCode);
                query.setMaxResults(1);

                List<?> rows = query.getResultList();
                if (rows == null || rows.isEmpty()) {
                    return null;
                }
                Object first = rows.get(0);
                if (first instanceof Object[]) {
                    Object[] columns = (Object[]) first;
                    return columns.length > 0 ? trimToNull(valueAsString(columns[0])) : null;
                }
                return trimToNull(valueAsString(first));
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastError != null) {
            return null;
        }
        return null;
    }

    public int insertMilestones(
            String companyCode,
            String warehouseCode,
            String customerCode,
            String arrivalNumber,
            String arrivalStatus,
            List<InboundOrderRegisterRequest.Line> lines,
            String userCode,
            String programCode
    ) {
        String xtOpKind = deriveOperationKind(arrivalStatus);
        String operator = defaultText(trimToNull(userCode), "SYSTEM");
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        int inserted = 0;
        RuntimeException lastObjectNotFound = null;
        for (String xtTable : XT_SOURCES) {
            try {
                String sql = "INSERT INTO " + xtTable + " (" +
                    "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                    "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
                        "XT_CPNY_COD, XT_WHS_COD, XT_CUST_COD, XT_AS_NUM, XT_ASLN_NUM, XT_ASSQ_NUM, " +
                        "XT_AS_KND, XT_OP_KND, XT_OPRT_USER, XT_EVNT_RMKS, XT_STR_YMD, XT_STR_TIM, XT_END_YMD, XT_END_TIM" +
                        ") VALUES (" +
                    "0, :crtYmd, :crtTim, :crtTmid, :crtUser, :crtPgm, 'JST', :crtYmdhms, :crtLocalYmdhms, " +
                    ":updYmd, :updTim, :updTmid, :updUser, :updPgm, 'JST', :updYmdhms, :updLocalYmdhms, " +
                        ":cpny, :whs, :cust, :asNum, :asLineNum, :asSqNum, " +
                        "'AV', :opKnd, :oprtUser, :eventRemarks, :strYmd, :strTim, :endYmd, :endTim" +
                        ")";

                Query headerQuery = em.createNativeQuery(sql);
                bindMilestoneCommon(
                        headerQuery,
                        companyCode,
                        warehouseCode,
                        customerCode,
                        arrivalNumber,
                        BigDecimal.ZERO,
                        xtOpKind,
                        arrivalStatus,
                        today,
                        nowTime,
                        nowDateTime,
                        operator,
                        programCode
                );
                inserted += headerQuery.executeUpdate();

                if (lines != null) {
                    for (InboundOrderRegisterRequest.Line line : lines) {
                        if (line == null) {
                            continue;
                        }
                        Query lineQuery = em.createNativeQuery(sql);
                        bindMilestoneCommon(
                                lineQuery,
                                companyCode,
                                warehouseCode,
                                customerCode,
                                arrivalNumber,
                                line.getArrivalLineNumber() == null ? BigDecimal.ZERO : line.getArrivalLineNumber(),
                                xtOpKind,
                                arrivalStatus,
                                today,
                                nowTime,
                                nowDateTime,
                                operator,
                                programCode
                        );
                        inserted += lineQuery.executeUpdate();
                    }
                }

                return inserted;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastObjectNotFound = ex;
                    inserted = 0;
                    continue;
                }
                throw ex;
            }
        }

        if (lastObjectNotFound != null) {
            return 0;
        }
        return inserted;
    }

    public int insertWarehouseProductivity(
            String companyCode,
            String warehouseCode,
            String customerCode,
            String arrivalNumber,
            String arrivalStatus,
            int lineCount,
            String userCode,
            String programCode
    ) {
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        String ymd = today.format(YMD_FORMATTER);
        String hms = nowTime.format(HMS_FORMATTER);
        String operator = defaultText(trimToNull(userCode), "SYSTEM");
        String opKind = deriveOperationKind(arrivalStatus);
        String opCod = "110";
        String opName = "Inbound Registration";

        RuntimeException lastObjectNotFound = null;
        for (String kpiTable : XT_KPI_SOURCES) {
            for (String sequence : XT_KPI_SEQUENCES) {
                try {
                    String sql = "INSERT INTO " + kpiTable + " (" +
                            "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                            "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
                            "KPI_CPNY_COD, KPI_WHS_COD, KPI_CUST_COD, KPI_LGSQ_NUM, " +
                            "KPI_AS_KND, KPI_OP_KND, KPI_OP_COD, KPI_OP_NAM, KPI_OP_YMD, KPI_OP_TIM, KPI_OP_USER, KPI_OP_TYPE, " +
                            "KPI_AS_NUM, KPI_ASLN_NUM, KPI_ASSQ_NUM, KPI_AV_NUM, KPI_AVLN_NUM, KPI_AVSQ_NUM" +
                            ") VALUES (" +
                            "0, :crtYmd, :crtTim, :crtTmid, :crtUser, :crtPgm, :crtTmZone, :crtYmdhms, :crtLocalYmdhms, " +
                            ":updYmd, :updTim, :updTmid, :updUser, :updPgm, :updTmZone, :updYmdhms, :updLocalYmdhms, " +
                            ":cpny, :whs, :cust, TO_CHAR(" + sequence + ".NEXTVAL), " +
                            "'AV', :opKnd, :opCod, :opNam, :opYmd, :opTim, :opUser, 'S', " +
                            ":asNum, 0, 0, :avNum, :avLnNum, :avSqNum" +
                            ")";

                    Query query = em.createNativeQuery(sql);
                        bindStandardAuditColumns(query, today, nowTime, nowDateTime, operator, programCode);
                    query.setParameter("cpny", companyCode);
                    query.setParameter("whs", warehouseCode);
                    query.setParameter("cust", customerCode);
                    query.setParameter("opKnd", opKind);
                    query.setParameter("opCod", opCod);
                    query.setParameter("opNam", opName);
                    query.setParameter("opYmd", ymd);
                    query.setParameter("opTim", hms);
                    query.setParameter("opUser", operator);
                    query.setParameter("asNum", arrivalNumber);
                    query.setParameter("avNum", arrivalNumber);
                    query.setParameter("avLnNum", BigDecimal.valueOf(Math.max(lineCount, 0)));
                    query.setParameter("avSqNum", BigDecimal.ZERO);

                    return query.executeUpdate();
                } catch (RuntimeException ex) {
                    if (isObjectNotFound(ex) || isSequenceNotFound(ex)) {
                        lastObjectNotFound = ex;
                        continue;
                    }
                    throw ex;
                }
            }
        }

        if (lastObjectNotFound != null) {
            return 0;
        }
        return 0;
    }

    public void insertHeader(
            String companyCode,
            String warehouseCode,
            String customerCode,
            String arrivalNumber,
            InboundOrderRegisterRequest.Header header,
            String userCode,
            String programCode
    ) {
        String sql = "INSERT INTO SGWH0001.GWH_TJ_AV_H (" +
                "CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
                "AVH_CPNY_COD, AVH_WHS_COD, AVH_CUST_COD, AVH_AV_NUM, AVH_SCDL_YMD, AVH_TRN_KND, AVH_AV_STS, " +
                "AVH_ARV_FLG, AVH_PO_NUM, AVH_RF_NUM, AVH_RMKS, AVH_SPL_COD, AVH_SPL_NAM1, AVH_WGT, AVH_M3, AVH_PTDV_FLG, " +
                "AVH_DAPR_FLG, AVH_CLPR_FLG, AVH_IPPR_FLG, AVH_IRPR_FLG, AVH_IDPR_FLG, AVH_CLP1_FLG, AVH_CLP2_FLG, AVH_CLP3_FLG, AVH_CLP4_FLG, AVH_CLP5_FLG, " +
                "AVH_EDI_FLG, AVH_PLPR_FLG, AVH_ISPR_FLG, AVH_SRT_FLG, AVH_SRPR_FLG, " +
                "AVH_CLI1, AVH_CLI2, AVH_CLI3, AVH_CLI4, AVH_CLI5, AVH_CLI6, AVH_CLI7, AVH_CLI8, AVH_CLI9, AVH_CLI10, AVH_CLI11, AVH_CLI12, AVH_CLI13, AVH_CLI14, AVH_CLI15, " +
                "AVH_CFG1_FLG, AVH_CFG2_FLG, AVH_CFG3_FLG, AVH_CFG4_FLG, AVH_CFG5_FLG, AVH_CFG6_FLG, AVH_CFG7_FLG, AVH_CFG8_FLG, AVH_CFG9_FLG, AVH_CFG10_FLG, AVH_CFG11_FLG, AVH_CFG12_FLG, AVH_CFG13_FLG, AVH_CFG14_FLG, AVH_CFG15_FLG, " +
                "AVH_CNI1_NUM, AVH_CNI2_NUM, AVH_CNI3_NUM, AVH_CNI4_NUM, AVH_CNI5_NUM, AVH_CNI6_NUM, AVH_CNI7_NUM, AVH_CNI8_NUM, AVH_CNI9_NUM, AVH_CNI10_NUM, AVH_CNI11_NUM, AVH_CNI12_NUM, AVH_CNI13_NUM, AVH_CNI14_NUM, AVH_CNI15_NUM, " +
                "DEL_FLG" +
                ") VALUES (" +
                ":crtYmd, :crtTim, :crtTmid, :crtUser, :crtPgm, :crtTmZone, :crtYmdhms, :crtLocalYmdhms, " +
                ":updYmd, :updTim, :updTmid, :updUser, :updPgm, :updTmZone, :updYmdhms, :updLocalYmdhms, " +
                ":cpny, :whs, :cust, :arrivalNo, :scheduleDate, :trnKind, :status, " +
                ":arrivedFlag, :poNo, :refNo, :remarks, :supplierCode, :supplierName, :weight, :volumeM3, :partialDeliveryFlag, " +
                ":documentArrivalPrintFlag, :cargoIdPrintFlag, :inspectionPrintFlag, :inspectionResultPrintFlag, :inspectionDiffPrintFlag, :customListPrintFlag1, :customListPrintFlag2, :customListPrintFlag3, :customListPrintFlag4, :customListPrintFlag5, " +
                ":ediFlag, :productLabelPrintFlag, :arrivalSheetPrintFlag, :sortingFlag, :sortingPrintFlag, " +
                ":cli1, :cli2, :cli3, :cli4, :cli5, :cli6, :cli7, :cli8, :cli9, :cli10, :cli11, :cli12, :cli13, :cli14, :cli15, " +
                ":cfg1, :cfg2, :cfg3, :cfg4, :cfg5, :cfg6, :cfg7, :cfg8, :cfg9, :cfg10, :cfg11, :cfg12, :cfg13, :cfg14, :cfg15, " +
                ":cni1, :cni2, :cni3, :cni4, :cni5, :cni6, :cni7, :cni8, :cni9, :cni10, :cni11, :cni12, :cni13, :cni14, :cni15, " +
                "0" +
                ")";

        Query query = em.createNativeQuery(sql);
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("arrivalNo", arrivalNumber);
        query.setParameter("scheduleDate", toSqlDate(header.getScheduledDate()));
        query.setParameter("trnKind", defaultText(header.getTransactionKind(), "001"));
        query.setParameter("status", defaultText(header.getArrivalStatus(), "100"));
        query.setParameter("arrivedFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("poNo", trimToNull(header.getPoNumber()));
        query.setParameter("refNo", trimToNull(header.getReferenceNumber()));
        query.setParameter("remarks", trimToNull(header.getRemarks()));
        query.setParameter("supplierCode", trimToNull(header.getSupplierCode()));
        query.setParameter("supplierName", trimToNull(header.getSupplierName()));
        query.setParameter("weight", defaultNumber(header.getWeight(), BigDecimal.ZERO));
        query.setParameter("volumeM3", defaultNumber(header.getVolumeM3(), BigDecimal.ZERO));
        query.setParameter("partialDeliveryFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("documentArrivalPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("cargoIdPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("inspectionPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("inspectionResultPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("inspectionDiffPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("customListPrintFlag1", DEFAULT_FALSE_FLAG);
        query.setParameter("customListPrintFlag2", DEFAULT_FALSE_FLAG);
        query.setParameter("customListPrintFlag3", DEFAULT_FALSE_FLAG);
        query.setParameter("customListPrintFlag4", DEFAULT_FALSE_FLAG);
        query.setParameter("customListPrintFlag5", DEFAULT_FALSE_FLAG);
        query.setParameter("ediFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("productLabelPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("arrivalSheetPrintFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("sortingFlag", DEFAULT_FALSE_FLAG);
        query.setParameter("sortingPrintFlag", DEFAULT_FALSE_FLAG);

        bindStringArray(query, "cli", header.getCli(), 15);
        bindFlagArray(query, "cfg", header.getCfg(), 15);
        bindNumberArray(query, "cni", header.getCni(), 15);

        bindStandardAuditColumns(query, nowDate, nowTime, nowDateTime, userCode, programCode);

        query.executeUpdate();
        }

        public void insertLines(
            String companyCode,
            String warehouseCode,
            String customerCode,
            String arrivalNumber,
            List<InboundOrderRegisterRequest.Line> lines,
            String userCode,
            String programCode
        ) {
        String sql = "INSERT INTO SGWH0001.GWH_TJ_AV_D (" +
            "CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
            "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
            "AVD_CPNY_COD, AVD_WHS_COD, AVD_CUST_COD, AVD_AV_NUM, AVD_AVLN_NUM, AVD_AV_STS, AVD_INSP_STS, " +
            "AVD_PROD_COD, AVD_PROD_NAM, AVD_ORGN_COD, AVD_SPPR_COD, AVD_SPPR_NAM, AVD_PPCS_QTY, AVD_SCS_QTY, AVD_SPC_QTY, AVD_STPC_QTY, AVD_SRPC_QTY, AVD_WGT, AVD_M3, AVD_SBIV_COD, AVD_SBL_QTY, " +
            "AVD_PIK1, AVD_PIK2, AVD_PIK3, AVD_PIK4, AVD_PIK5, AVD_PIK6, AVD_PIK7, AVD_PSSA_FLG, AVD_CLPR_FLG, AVD_DMG_FLG, AVD_RMKS, AVD_PLPR_FLG, AVD_ISPR_FLG, " +
            "AVD_CLI1, AVD_CLI2, AVD_CLI3, AVD_CLI4, AVD_CLI5, AVD_CLI6, AVD_CLI7, AVD_CLI8, AVD_CLI9, AVD_CLI10, AVD_CLI11, AVD_CLI12, AVD_CLI13, AVD_CLI14, AVD_CLI15, " +
            "AVD_CFG1_FLG, AVD_CFG2_FLG, AVD_CFG3_FLG, AVD_CFG4_FLG, AVD_CFG5_FLG, AVD_CFG6_FLG, AVD_CFG7_FLG, AVD_CFG8_FLG, AVD_CFG9_FLG, AVD_CFG10_FLG, AVD_CFG11_FLG, AVD_CFG12_FLG, AVD_CFG13_FLG, AVD_CFG14_FLG, AVD_CFG15_FLG, " +
            "AVD_CNI1_NUM, AVD_CNI2_NUM, AVD_CNI3_NUM, AVD_CNI4_NUM, AVD_CNI5_NUM, AVD_CNI6_NUM, AVD_CNI7_NUM, AVD_CNI8_NUM, AVD_CNI9_NUM, AVD_CNI10_NUM, AVD_CNI11_NUM, AVD_CNI12_NUM, AVD_CNI13_NUM, AVD_CNI14_NUM, AVD_CNI15_NUM, " +
            "DEL_FLG" +
            ") VALUES (" +
            ":crtYmd, :crtTim, :crtTmid, :crtUser, :crtPgm, :crtTmZone, :crtYmdhms, :crtLocalYmdhms, " +
            ":updYmd, :updTim, :updTmid, :updUser, :updPgm, :updTmZone, :updYmdhms, :updLocalYmdhms, " +
            ":cpny, :whs, :cust, :arrivalNo, :lineNo, :status, :inspStatus, " +
            ":productCode, :productName, :originCode, :shipperCode, :shipperName, :ppcsQty, :scsQty, :spcQty, :stpcQty, :srpcQty, :weight, :volumeM3, :subInventoryCode, :sblQty, " +
            ":pik1, :pik2, :pik3, :pik4, :pik5, :pik6, :pik7, :pssaFlag, :cargoIdPrintFlag, :damageFlag, :remarks, :productLabelPrintFlag, :arrivalSheetPrintFlag, " +
            ":cli1, :cli2, :cli3, :cli4, :cli5, :cli6, :cli7, :cli8, :cli9, :cli10, :cli11, :cli12, :cli13, :cli14, :cli15, " +
            ":cfg1, :cfg2, :cfg3, :cfg4, :cfg5, :cfg6, :cfg7, :cfg8, :cfg9, :cfg10, :cfg11, :cfg12, :cfg13, :cfg14, :cfg15, " +
            ":cni1, :cni2, :cni3, :cni4, :cni5, :cni6, :cni7, :cni8, :cni9, :cni10, :cni11, :cni12, :cni13, :cni14, :cni15, " +
            "0" +
            ")";

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        int lineIndex = 1;
        for (InboundOrderRegisterRequest.Line line : lines) {
            ProductPackagingDefaults productPackaging = getProductPackagingDefaults(
                companyCode,
                warehouseCode,
                customerCode,
                line.getProductCode()
            );
            BigDecimal plannedCaseQuantity = defaultNumber(line.getPlannedCaseQuantity(), BigDecimal.ZERO);
            BigDecimal plannedPieceQuantity = defaultNumber(line.getPlannedPieceQuantity(), BigDecimal.ZERO);
            BigDecimal plannedBallQuantity = BigDecimal.ZERO;
            BigDecimal piecesPerCase = productPackaging.getPiecesPerCase();
            BigDecimal piecesPerBall = productPackaging.getPiecesPerBall();
            BigDecimal totalPieceQuantity = plannedCaseQuantity.multiply(piecesPerCase)
                .add(plannedBallQuantity.multiply(piecesPerBall))
                .add(plannedPieceQuantity);

            Query query = em.createNativeQuery(sql);
            query.setParameter("cpny", companyCode);
            query.setParameter("whs", warehouseCode);
            query.setParameter("cust", customerCode);
            query.setParameter("arrivalNo", arrivalNumber);
            query.setParameter("lineNo", line.getArrivalLineNumber() != null ? line.getArrivalLineNumber() : BigDecimal.valueOf(lineIndex));
            query.setParameter("status", defaultText(line.getArrivalStatus(), "100"));
            query.setParameter("inspStatus", DEFAULT_FALSE_FLAG);
            query.setParameter("productCode", trimToNull(line.getProductCode()));
            query.setParameter("productName", trimToNull(line.getProductName()));
            query.setParameter("originCode", trimToNull(line.getOriginCode()));
            query.setParameter("shipperCode", trimToNull(line.getShipperCode()));
            query.setParameter("shipperName", trimToNull(line.getShipperName()));
            query.setParameter("ppcsQty", piecesPerCase);
            query.setParameter("scsQty", plannedCaseQuantity);
            query.setParameter("spcQty", plannedPieceQuantity);
            query.setParameter("stpcQty", totalPieceQuantity);
            query.setParameter("srpcQty", BigDecimal.ZERO);
            query.setParameter("weight", defaultNumber(line.getWeight(), BigDecimal.ZERO));
            query.setParameter("volumeM3", defaultNumber(line.getVolumeM3(), BigDecimal.ZERO));
            query.setParameter("subInventoryCode", defaultText(trimToNull(line.getSubInventoryCode()), DEFAULT_SUB_INVENTORY_CODE));
            query.setParameter("sblQty", plannedBallQuantity);
            query.setParameter("pik1", trimToNull(line.getPik1()));
            query.setParameter("pik2", trimToNull(line.getPik2()));
            query.setParameter("pik3", trimToNull(line.getPik3()));
            query.setParameter("pik4", trimToNull(line.getPik4()));
            query.setParameter("pik5", trimToNull(line.getPik5()));
            query.setParameter("pik6", trimToNull(line.getPik6()));
            query.setParameter("pik7", trimToNull(line.getPik7()));
            query.setParameter("pssaFlag", DEFAULT_FALSE_FLAG);
            query.setParameter("cargoIdPrintFlag", DEFAULT_FALSE_FLAG);
            query.setParameter("damageFlag", DEFAULT_FALSE_FLAG);
            query.setParameter("remarks", trimToNull(line.getRemarks()));
            query.setParameter("productLabelPrintFlag", DEFAULT_FALSE_FLAG);
            query.setParameter("arrivalSheetPrintFlag", DEFAULT_FALSE_FLAG);

            bindStringArray(query, "cli", line.getCli(), 15);
            bindFlagArray(query, "cfg", line.getCfg(), 15);
            bindNumberArray(query, "cni", line.getCni(), 15);
            bindStandardAuditColumns(query, nowDate, nowTime, nowDateTime, userCode, programCode);

            query.executeUpdate();
            lineIndex++;
        }
        }

    /**
     * Find the current arrival status for a given order. Returns null if not found or deleted.
     */
    public String findCurrentStatus(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        String sql = "SELECT AVH_AV_STS FROM SGWH0001.GWH_TJ_AV_H " +
            "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
            "AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";

        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("avNum", arrivalNumber);

        @SuppressWarnings("unchecked")
        List<?> rows = query.getResultList();
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        Object val = rows.get(0);
        return val != null ? val.toString().trim() : null;
    }

    /**
     * Check optimistic lock: verify UPD_YMDHMS matches expected value.
     */
    public boolean checkOptimisticLock(
            String companyCode, String warehouseCode, String customerCode,
            String arrivalNumber, String expectedTimestamp) {

        String sql = "SELECT COUNT(*) FROM SGWH0001.GWH_TJ_AV_H " +
            "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
            "AND AVH_AV_NUM = :avNum AND DEL_FLG = '0' " +
            "AND TO_CHAR(UPD_YMDHMS, 'YYYY-MM-DD HH24:MI:SS.FF3') = :expectedTs";

        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("avNum", arrivalNumber);
        query.setParameter("expectedTs", expectedTimestamp);

        Number count = (Number) query.getSingleResult();
        return count.longValue() > 0;
    }

    /**
     * Update arrival header fields.
     */
    public void updateHeader(
            String companyCode, String warehouseCode, String customerCode,
            String arrivalNumber, InboundOrderRegisterRequest.Header header,
            String userCode, String programCode) {

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        String sql = "UPDATE SGWH0001.GWH_TJ_AV_H SET " +
            "AVH_SCDL_YMD = :scheduleDate, AVH_TRN_KND = :trnKind, " +
            "AVH_PO_NUM = :poNo, AVH_RF_NUM = :refNo, AVH_RMKS = :remarks, " +
            "AVH_SPL_COD = :supplierCode, AVH_SPL_NAM1 = :supplierName, " +
            "AVH_WGT = :weight, AVH_M3 = :volumeM3, " +
            "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, " +
            "UPD_USER = :updUser, UPD_PGM = :updPgm, UPD_TM_ZONE = 'JST', " +
            "UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms " +
            "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
            "AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";

        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("avNum", arrivalNumber);
        query.setParameter("scheduleDate", toSqlDate(header.getScheduledDate()));
        query.setParameter("trnKind", defaultText(header.getTransactionKind(), "001"));
        query.setParameter("poNo", trimToNull(header.getPoNumber()));
        query.setParameter("refNo", trimToNull(header.getReferenceNumber()));
        query.setParameter("remarks", trimToNull(header.getRemarks()));
        query.setParameter("supplierCode", trimToNull(header.getSupplierCode()));
        query.setParameter("supplierName", trimToNull(header.getSupplierName()));
        query.setParameter("weight", defaultNumber(header.getWeight(), BigDecimal.ZERO));
        query.setParameter("volumeM3", defaultNumber(header.getVolumeM3(), BigDecimal.ZERO));

        String auditDate = nowDate.format(YMD_FORMATTER);
        String auditTime = nowTime.format(HMS_FORMATTER);
        String auditUser = defaultText(trimToNull(userCode), "SYSTEM");
        String auditTerminal = defaultText(trimToNull(programCode), "MODERN_APINQ");

        query.setParameter("updYmd", auditDate);
        query.setParameter("updTim", auditTime);
        query.setParameter("updTmid", auditTerminal);
        query.setParameter("updUser", auditUser);
        query.setParameter("updPgm", programCode);
        query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));

        query.executeUpdate();
    }

    /**
     * Soft-delete all detail lines for an arrival order (DEL_FLG = '1').
     */
    public int softDeleteDetailLines(
            String companyCode, String warehouseCode, String customerCode,
            String arrivalNumber, String userCode, String programCode) {

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        String sql = "UPDATE SGWH0001.GWH_TJ_AV_D SET DEL_FLG = '1', " +
            "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, " +
            "UPD_USER = :updUser, UPD_PGM = :updPgm, UPD_TM_ZONE = 'JST', " +
            "UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "AND AVD_AV_NUM = :avNum AND DEL_FLG = '0'";

        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("avNum", arrivalNumber);

        String auditDate = nowDate.format(YMD_FORMATTER);
        String auditTime = nowTime.format(HMS_FORMATTER);
        String auditUser = defaultText(trimToNull(userCode), "SYSTEM");
        String auditTerminal = defaultText(trimToNull(programCode), "MODERN_APINQ");

        query.setParameter("updYmd", auditDate);
        query.setParameter("updTim", auditTime);
        query.setParameter("updTmid", auditTerminal);
        query.setParameter("updUser", auditUser);
        query.setParameter("updPgm", programCode);
        query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));

        return query.executeUpdate();
    }

    /**
     * Upsert detail lines using Oracle MERGE.
     * Handles re-saves of the same line numbers without violating IGWH_TJ_AV_D_1U.
     */
    public void mergeDetailLines(
            String companyCode, String warehouseCode, String customerCode,
            String arrivalNumber, List<InboundOrderRegisterRequest.Line> lines,
            String userCode, String programCode) {

        String sql =
            "MERGE INTO SGWH0001.GWH_TJ_AV_D t " +
            "USING (SELECT :cpny AS CPNY, :whs AS WHS, :cust AS CUST, " +
            "  :arrivalNo AS AV_NUM, :lineNo AS AVLN_NUM FROM DUAL) s " +
            "ON (t.AVD_CPNY_COD = s.CPNY AND t.AVD_WHS_COD = s.WHS " +
            "  AND t.AVD_CUST_COD = s.CUST AND t.AVD_AV_NUM = s.AV_NUM " +
            "  AND t.AVD_AVLN_NUM = s.AVLN_NUM) " +
            "WHEN MATCHED THEN UPDATE SET " +
            "  DEL_FLG = '0', AVD_AV_STS = :status, " +
            "  AVD_PROD_COD = :productCode, AVD_PROD_NAM = :productName, " +
            "  AVD_ORGN_COD = :originCode, AVD_SPPR_COD = :shipperCode, AVD_SPPR_NAM = :shipperName, " +
            "  AVD_PPCS_QTY = :ppcsQty, AVD_SCS_QTY = :scsQty, AVD_SPC_QTY = :spcQty, " +
            "  AVD_STPC_QTY = :stpcQty, AVD_WGT = :weight, AVD_M3 = :volumeM3, " +
            "  AVD_SBIV_COD = :subInventoryCode, " +
            "  AVD_PIK1 = :pik1, AVD_PIK2 = :pik2, AVD_PIK3 = :pik3, AVD_PIK4 = :pik4, " +
            "  AVD_PIK5 = :pik5, AVD_PIK6 = :pik6, AVD_PIK7 = :pik7, " +
            "  AVD_RMKS = :remarks, " +
            "  UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, " +
            "  UPD_USER = :updUser, UPD_PGM = :updPgm, UPD_TM_ZONE = 'JST', " +
            "  UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms " +
            "WHEN NOT MATCHED THEN INSERT (" +
            "  CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
            "  UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
            "  AVD_CPNY_COD, AVD_WHS_COD, AVD_CUST_COD, AVD_AV_NUM, AVD_AVLN_NUM, AVD_AV_STS, AVD_INSP_STS, " +
            "  AVD_PROD_COD, AVD_PROD_NAM, AVD_ORGN_COD, AVD_SPPR_COD, AVD_SPPR_NAM, " +
            "  AVD_PPCS_QTY, AVD_SCS_QTY, AVD_SPC_QTY, AVD_STPC_QTY, AVD_SRPC_QTY, " +
            "  AVD_WGT, AVD_M3, AVD_SBIV_COD, AVD_SBL_QTY, " +
            "  AVD_PIK1, AVD_PIK2, AVD_PIK3, AVD_PIK4, AVD_PIK5, AVD_PIK6, AVD_PIK7, " +
            "  AVD_PSSA_FLG, AVD_CLPR_FLG, AVD_DMG_FLG, AVD_RMKS, AVD_PLPR_FLG, AVD_ISPR_FLG, " +
            "  AVD_CLI1, AVD_CLI2, AVD_CLI3, AVD_CLI4, AVD_CLI5, AVD_CLI6, AVD_CLI7, " +
            "  AVD_CLI8, AVD_CLI9, AVD_CLI10, AVD_CLI11, AVD_CLI12, AVD_CLI13, AVD_CLI14, AVD_CLI15, " +
            "  AVD_CFG1_FLG, AVD_CFG2_FLG, AVD_CFG3_FLG, AVD_CFG4_FLG, AVD_CFG5_FLG, " +
            "  AVD_CFG6_FLG, AVD_CFG7_FLG, AVD_CFG8_FLG, AVD_CFG9_FLG, AVD_CFG10_FLG, " +
            "  AVD_CFG11_FLG, AVD_CFG12_FLG, AVD_CFG13_FLG, AVD_CFG14_FLG, AVD_CFG15_FLG, " +
            "  AVD_CNI1_NUM, AVD_CNI2_NUM, AVD_CNI3_NUM, AVD_CNI4_NUM, AVD_CNI5_NUM, " +
            "  AVD_CNI6_NUM, AVD_CNI7_NUM, AVD_CNI8_NUM, AVD_CNI9_NUM, AVD_CNI10_NUM, " +
            "  AVD_CNI11_NUM, AVD_CNI12_NUM, AVD_CNI13_NUM, AVD_CNI14_NUM, AVD_CNI15_NUM, DEL_FLG" +
            ") VALUES (" +
            "  :crtYmd, :crtTim, :crtTmid, :crtUser, :crtPgm, :crtTmZone, :crtYmdhms, :crtLocalYmdhms, " +
            "  :updYmd, :updTim, :updTmid, :updUser, :updPgm, 'JST', :updYmdhms, :updLocalYmdhms, " +
            "  :cpny, :whs, :cust, :arrivalNo, :lineNo, :status, 'N', " +
            "  :productCode, :productName, :originCode, :shipperCode, :shipperName, " +
            "  :ppcsQty, :scsQty, :spcQty, :stpcQty, 0, " +
            "  :weight, :volumeM3, :subInventoryCode, 0, " +
            "  :pik1, :pik2, :pik3, :pik4, :pik5, :pik6, :pik7, " +
            "  'N', 'N', 'N', :remarks, 'N', 'N', " +
            "  null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, " +
            "  'N','N','N','N','N','N','N','N','N','N','N','N','N','N','N', " +
            "  null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, " +
            "  '0'" +
            ")";

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();
        String auditDate = nowDate.format(YMD_FORMATTER);
        String auditTime = nowTime.format(HMS_FORMATTER);
        String auditUser = defaultText(trimToNull(userCode), "SYSTEM");
        String auditTerminal = defaultText(trimToNull(programCode), "MODERN_APINQ");

        int lineIndex = 1;
        for (InboundOrderRegisterRequest.Line line : lines) {
            ProductPackagingDefaults productPackaging = getProductPackagingDefaults(
                companyCode, warehouseCode, customerCode, line.getProductCode());
            BigDecimal plannedCaseQty = defaultNumber(line.getPlannedCaseQuantity(), BigDecimal.ZERO);
            BigDecimal plannedPieceQty = defaultNumber(line.getPlannedPieceQuantity(), BigDecimal.ZERO);
            BigDecimal piecesPerCase = productPackaging.getPiecesPerCase();
            BigDecimal totalPieceQty = plannedCaseQty.multiply(piecesPerCase).add(plannedPieceQty);

            Query query = em.createNativeQuery(sql);
            query.setParameter("cpny", companyCode);
            query.setParameter("whs", warehouseCode);
            query.setParameter("cust", customerCode);
            query.setParameter("arrivalNo", arrivalNumber);
            query.setParameter("lineNo", line.getArrivalLineNumber() != null
                ? line.getArrivalLineNumber() : BigDecimal.valueOf(lineIndex));
            query.setParameter("status", defaultText(line.getArrivalStatus(), "100"));
            query.setParameter("productCode", trimToNull(line.getProductCode()));
            query.setParameter("productName", trimToNull(line.getProductName()));
            query.setParameter("originCode", trimToNull(line.getOriginCode()));
            query.setParameter("shipperCode", trimToNull(line.getShipperCode()));
            query.setParameter("shipperName", trimToNull(line.getShipperName()));
            query.setParameter("ppcsQty", piecesPerCase);
            query.setParameter("scsQty", plannedCaseQty);
            query.setParameter("spcQty", plannedPieceQty);
            query.setParameter("stpcQty", totalPieceQty);
            query.setParameter("weight", defaultNumber(line.getWeight(), BigDecimal.ZERO));
            query.setParameter("volumeM3", defaultNumber(line.getVolumeM3(), BigDecimal.ZERO));
            query.setParameter("subInventoryCode", defaultText(trimToNull(line.getSubInventoryCode()), DEFAULT_SUB_INVENTORY_CODE));
            query.setParameter("pik1", trimToNull(line.getPik1()));
            query.setParameter("pik2", trimToNull(line.getPik2()));
            query.setParameter("pik3", trimToNull(line.getPik3()));
            query.setParameter("pik4", trimToNull(line.getPik4()));
            query.setParameter("pik5", trimToNull(line.getPik5()));
            query.setParameter("pik6", trimToNull(line.getPik6()));
            query.setParameter("pik7", trimToNull(line.getPik7()));
            query.setParameter("remarks", trimToNull(line.getRemarks()));
            query.setParameter("crtYmd", auditDate);
            query.setParameter("crtTim", auditTime);
            query.setParameter("crtTmid", auditTerminal);
            query.setParameter("crtUser", auditUser);
            query.setParameter("crtPgm", programCode);
            query.setParameter("crtTmZone", "JST");
            query.setParameter("crtYmdhms", Timestamp.valueOf(nowDateTime));
            query.setParameter("crtLocalYmdhms", Timestamp.valueOf(nowDateTime));
            query.setParameter("updYmd", auditDate);
            query.setParameter("updTim", auditTime);
            query.setParameter("updTmid", auditTerminal);
            query.setParameter("updUser", auditUser);
            query.setParameter("updPgm", programCode);
            query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
            query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));

            query.executeUpdate();
            lineIndex++;
        }
    }

    /**
     * Soft-delete lines whose line numbers are NOT in keptLineNumbers.
     * Used after mergeDetailLines to remove lines the user deleted from the order.
     */
    public void softDeleteRemovedLines(
            String companyCode, String warehouseCode, String customerCode,
            String arrivalNumber, List<java.math.BigDecimal> keptLineNumbers,
            String userCode, String programCode) {

        if (keptLineNumbers == null || keptLineNumbers.isEmpty()) {
            softDeleteDetailLines(companyCode, warehouseCode, customerCode, arrivalNumber, userCode, programCode);
            return;
        }

        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < keptLineNumbers.size(); i++) {
            if (i > 0) inClause.append(", ");
            inClause.append(":ln").append(i);
        }

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime nowDateTime = LocalDateTime.now();

        String sql = "UPDATE SGWH0001.GWH_TJ_AV_D SET DEL_FLG = '1', " +
            "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, " +
            "UPD_USER = :updUser, UPD_PGM = :updPgm, UPD_TM_ZONE = 'JST', " +
            "UPD_YMDHMS = :updYmdhms, UPD_L_YMDHMS = :updLocalYmdhms " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "AND AVD_AV_NUM = :avNum AND DEL_FLG = '0' " +
            "AND AVD_AVLN_NUM NOT IN (" + inClause + ")";

        Query query = em.createNativeQuery(sql);
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("avNum", arrivalNumber);
        for (int i = 0; i < keptLineNumbers.size(); i++) {
            query.setParameter("ln" + i, keptLineNumbers.get(i));
        }
        String auditDate = nowDate.format(YMD_FORMATTER);
        String auditTime = nowTime.format(HMS_FORMATTER);
        query.setParameter("updYmd", auditDate);
        query.setParameter("updTim", auditTime);
        query.setParameter("updTmid", defaultText(trimToNull(programCode), "MODERN_APINQ"));
        query.setParameter("updUser", defaultText(trimToNull(userCode), "SYSTEM"));
        query.setParameter("updPgm", programCode);
        query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));

        query.executeUpdate();
    }

        private void bindStandardAuditColumns(
            Query query,
            LocalDate nowDate,
            LocalTime nowTime,
            LocalDateTime nowDateTime,
            String userCode,
            String programCode
        ) {
        String auditDate = nowDate.format(YMD_FORMATTER);
        String auditTime = nowTime.format(HMS_FORMATTER);
        String auditUser = defaultText(trimToNull(userCode), "SYSTEM");
        String auditTerminal = defaultText(trimToNull(programCode), "MODERN_APPRE");

        query.setParameter("crtYmd", auditDate);
        query.setParameter("crtTim", auditTime);
        query.setParameter("crtTmid", auditTerminal);
        query.setParameter("crtUser", auditUser);
        query.setParameter("crtPgm", programCode);
        query.setParameter("crtTmZone", "JST");
        query.setParameter("crtYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("crtLocalYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updYmd", auditDate);
        query.setParameter("updTim", auditTime);
        query.setParameter("updTmid", auditTerminal);
        query.setParameter("updUser", auditUser);
        query.setParameter("updPgm", programCode);
        query.setParameter("updTmZone", "JST");
        query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));
    }

    private void bindStringArray(Query query, String prefix, List<String> values, int size) {
        for (int index = 1; index <= size; index++) {
            String value = null;
            if (values != null && values.size() >= index) {
                value = trimToNull(values.get(index - 1));
            }
            query.setParameter(prefix + index, value);
        }
    }

    private void bindFlagArray(Query query, String prefix, List<String> values, int size) {
        for (int index = 1; index <= size; index++) {
            String value = DEFAULT_FALSE_FLAG;
            if (values != null && values.size() >= index) {
                value = defaultText(trimToNull(values.get(index - 1)), DEFAULT_FALSE_FLAG);
            }
            query.setParameter(prefix + index, value);
        }
    }

    private ProductPackagingDefaults getProductPackagingDefaults(
            String companyCode,
            String warehouseCode,
            String customerCode,
            String productCode
    ) {
        String normalizedProductCode = trimToNull(productCode);
        if (normalizedProductCode == null) {
            return new ProductPackagingDefaults(BigDecimal.ONE, BigDecimal.ZERO);
        }

        RuntimeException lastError = null;
        for (String source : PRODUCT_SOURCES) {
            try {
                String sql = "SELECT PROD_PPC_NUM, PROD_PPB_NUM FROM " + source + " " +
                        "WHERE PROD_CPNY_COD = :cpny AND PROD_WHS_COD = :whs AND PROD_CUST_COD = :cust " +
                        "AND PROD_COD = :prodCod AND DEL_FLG = 0";
                Query query = em.createNativeQuery(sql);
                query.setParameter("cpny", companyCode);
                query.setParameter("whs", warehouseCode);
                query.setParameter("cust", customerCode);
                query.setParameter("prodCod", normalizedProductCode);

                List<?> rows = query.getResultList();
                if (rows == null || rows.isEmpty()) {
                    return new ProductPackagingDefaults(BigDecimal.ONE, BigDecimal.ZERO);
                }

                Object first = rows.get(0);
                if (!(first instanceof Object[])) {
                    return new ProductPackagingDefaults(BigDecimal.ONE, BigDecimal.ZERO);
                }

                Object[] columns = (Object[]) first;
                BigDecimal piecesPerCase = toBigDecimal(columns.length > 0 ? columns[0] : null, BigDecimal.ONE);
                BigDecimal piecesPerBall = toBigDecimal(columns.length > 1 ? columns[1] : null, BigDecimal.ZERO);
                return new ProductPackagingDefaults(piecesPerCase, piecesPerBall);
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (lastError != null) {
            return new ProductPackagingDefaults(BigDecimal.ONE, BigDecimal.ZERO);
        }
        return new ProductPackagingDefaults(BigDecimal.ONE, BigDecimal.ZERO);
    }

    private boolean existsInSources(
            String[] sources,
            String codeColumn,
            String codeValue,
            String companyCode,
            String warehouseCode,
            String customerCode,
            String companyColumn,
            String warehouseColumn,
            String customerColumn,
            String deleteFlagColumn
    ) {
        String normalized = trimToNull(codeValue);
        if (normalized == null) {
            return false;
        }

        RuntimeException lastError = null;
        boolean checkedAnySource = false;
        for (String source : sources) {
            try {
                StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ").append(source)
                        .append(" WHERE ").append(codeColumn).append(" = :code ");
                if (companyColumn != null) {
                    sql.append(" AND ").append(companyColumn).append(" = :cpny ");
                }
                if (warehouseColumn != null) {
                    sql.append(" AND ").append(warehouseColumn).append(" = :whs ");
                }
                if (customerColumn != null) {
                    sql.append(" AND ").append(customerColumn).append(" = :cust ");
                }
                if (deleteFlagColumn != null) {
                    sql.append(" AND ").append(deleteFlagColumn).append(" = 0 ");
                }

                Query query = em.createNativeQuery(sql.toString());
                checkedAnySource = true;
                query.setParameter("code", normalized);
                if (companyColumn != null) {
                    query.setParameter("cpny", companyCode);
                }
                if (warehouseColumn != null) {
                    query.setParameter("whs", warehouseCode);
                }
                if (customerColumn != null) {
                    query.setParameter("cust", customerCode);
                }

                Number count = (Number) query.getSingleResult();
                return count != null && count.longValue() > 0;
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }

        if (!checkedAnySource && lastError != null) {
            return true;
        }
        return false;
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

    private boolean isSequenceNotFound(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && message.contains("ORA-02289")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private void bindMilestoneCommon(
            Query query,
            String companyCode,
            String warehouseCode,
            String customerCode,
            String arrivalNumber,
            BigDecimal lineNumber,
            String operationKind,
            String eventRemarks,
            LocalDate nowDate,
            LocalTime nowTime,
            LocalDateTime nowDateTime,
            String operator,
            String programCode
    ) {
        String ymd = nowDate.format(YMD_FORMATTER);
        String hms = nowTime.format(HMS_FORMATTER);
        String terminalId = defaultText(trimToNull(programCode), "MODERN_APPRE");

        query.setParameter("crtYmd", ymd);
        query.setParameter("crtTim", hms);
        query.setParameter("crtTmid", terminalId);
        query.setParameter("crtUser", operator);
        query.setParameter("crtPgm", programCode);
        query.setParameter("crtYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("crtLocalYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updYmd", ymd);
        query.setParameter("updTim", hms);
        query.setParameter("updTmid", terminalId);
        query.setParameter("updUser", operator);
        query.setParameter("updPgm", programCode);
        query.setParameter("updYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("updLocalYmdhms", Timestamp.valueOf(nowDateTime));
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        query.setParameter("asNum", arrivalNumber);
        query.setParameter("asLineNum", lineNumber == null ? BigDecimal.ZERO : lineNumber);
        query.setParameter("asSqNum", BigDecimal.ZERO);
        query.setParameter("opKnd", operationKind);
        query.setParameter("oprtUser", operator);
        query.setParameter("eventRemarks", trimToNull(eventRemarks));
        query.setParameter("strYmd", ymd);
        query.setParameter("strTim", hms);
        query.setParameter("endYmd", ymd);
        query.setParameter("endTim", hms);
    }

    private String deriveOperationKind(String status) {
        String normalized = trimToNull(status);
        if (normalized == null || normalized.length() < 2) {
            return "10";
        }
        return normalized.substring(0, 2);
    }

    private void bindNumberArray(Query query, String prefix, List<BigDecimal> values, int size) {
        for (int index = 1; index <= size; index++) {
            BigDecimal value = BigDecimal.ZERO;
            if (values != null && values.size() >= index) {
                value = defaultNumber(values.get(index - 1), BigDecimal.ZERO);
            }
            query.setParameter(prefix + index, value);
        }
    }

    private Date toSqlDate(LocalDate value) {
        return value == null ? null : Date.valueOf(value);
    }

    private String defaultText(String value, String defaultValue) {
        String normalized = trimToNull(value);
        return normalized == null ? defaultValue : normalized;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String valueAsString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private int toInt(Object value, int defaultValue) {
        Integer result = toNullableInt(value);
        return result == null ? defaultValue : result;
    }

    private Integer toNullableInt(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        String text = trimToNull(String.valueOf(value));
        if (text == null) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        String text = trimToNull(String.valueOf(value));
        if (text == null) {
            return defaultValue;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private BigDecimal defaultNumber(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static class TransactionRule {
        private final String reasonRequiredFlag;
        private final String pickDependencyFlag;

        public TransactionRule(String reasonRequiredFlag, String pickDependencyFlag) {
            this.reasonRequiredFlag = reasonRequiredFlag;
            this.pickDependencyFlag = pickDependencyFlag;
        }

        public String getReasonRequiredFlag() {
            return reasonRequiredFlag;
        }

        public String getPickDependencyFlag() {
            return pickDependencyFlag;
        }
    }

    public static class PikSetting {
        private final int pikNumber;
        private final String useFlag;
        private final String inputRequiredFlag;
        private final String scanKind;
        private final String typeKind;
        private final Integer maxLength;
        private final String inputFormatKind;

        public PikSetting(int pikNumber, String useFlag, String inputRequiredFlag, String scanKind,
                          String typeKind, Integer maxLength, String inputFormatKind) {
            this.pikNumber = pikNumber;
            this.useFlag = useFlag;
            this.inputRequiredFlag = inputRequiredFlag;
            this.scanKind = scanKind;
            this.typeKind = typeKind;
            this.maxLength = maxLength;
            this.inputFormatKind = inputFormatKind;
        }

        public int getPikNumber() {
            return pikNumber;
        }

        public String getUseFlag() {
            return useFlag;
        }

        public String getInputRequiredFlag() {
            return inputRequiredFlag;
        }

        public String getScanKind() {
            return scanKind;
        }

        public String getTypeKind() {
            return typeKind;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public String getInputFormatKind() {
            return inputFormatKind;
        }
    }

    private static class ProductPackagingDefaults {
        private final BigDecimal piecesPerCase;
        private final BigDecimal piecesPerBall;

        private ProductPackagingDefaults(BigDecimal piecesPerCase, BigDecimal piecesPerBall) {
            this.piecesPerCase = piecesPerCase == null ? BigDecimal.ONE : piecesPerCase;
            this.piecesPerBall = piecesPerBall == null ? BigDecimal.ZERO : piecesPerBall;
        }

        private BigDecimal getPiecesPerCase() {
            return piecesPerCase;
        }

        private BigDecimal getPiecesPerBall() {
            return piecesPerBall;
        }
    }
}