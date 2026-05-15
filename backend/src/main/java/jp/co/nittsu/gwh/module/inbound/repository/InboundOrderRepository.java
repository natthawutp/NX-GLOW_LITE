package jp.co.nittsu.gwh.module.inbound.repository;

import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDetailDto;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDto;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InboundOrderRepository {

    private static final Logger log = LoggerFactory.getLogger(InboundOrderRepository.class);

    private static final String[] INBOUND_SOURCE_CANDIDATES = {
            "VGWH_TJ_AV_H",
            "GWH.VGWH_TJ_AV_H",
            "SGWH0001.VGWH_TJ_AV_H",
            "GWH_TJ_AV_H",
            "GWH.GWH_TJ_AV_H",
            "SGWH0001.GWH_TJ_AV_H"
    };

    @PersistenceContext
    private EntityManager em;

    public InboundOrderSearchResult search(
            String companyCode,
            String warehouseCode,
            String customerCode,
            String arrivalNo,
            String status,
            String supplier,
            LocalDate dateFrom,
            LocalDate dateTo,
            int page,
            int size
    ) {
        StringBuilder where = new StringBuilder();
        Map<String, Object> params = new LinkedHashMap<>();

        where.append(" WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust AND DEL_FLG = 0 ");
        params.put("cpny", companyCode);
        params.put("whs", warehouseCode);
        params.put("cust", customerCode);

        if (arrivalNo != null && !arrivalNo.trim().isEmpty()) {
            where.append(" AND UPPER(AVH_AV_NUM) LIKE :arrivalNo ");
            params.put("arrivalNo", "%" + arrivalNo.trim().toUpperCase() + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            where.append(" AND AVH_AV_STS = :status ");
            params.put("status", status.trim());
        }
        if (supplier != null && !supplier.trim().isEmpty()) {
            where.append(" AND (UPPER(NVL(AVH_SPL_COD, '')) LIKE :supplier OR UPPER(NVL(AVH_SPL_NAM1, '')) LIKE :supplier) ");
            params.put("supplier", "%" + supplier.trim().toUpperCase() + "%");
        }
        if (dateFrom != null) {
            where.append(" AND TRUNC(AVH_SCDL_YMD) >= :dateFrom ");
            params.put("dateFrom", Date.valueOf(dateFrom));
        }
        if (dateTo != null) {
            where.append(" AND TRUNC(AVH_SCDL_YMD) <= :dateTo ");
            params.put("dateTo", Date.valueOf(dateTo));
        }

        RuntimeException lastError = null;
        for (String source : INBOUND_SOURCE_CANDIDATES) {
            try {
                return searchFromSource(source, where.toString(), params, page, size);
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
        throw new IllegalStateException("Inbound source object not found");
    }

    private InboundOrderSearchResult searchFromSource(
            String source,
            String where,
            Map<String, Object> params,
            int page,
            int size
    ) {
        String countSql = "SELECT COUNT(*) FROM " + source + where;
        Query countQuery = em.createNativeQuery(countSql);
        applyParams(countQuery, params);
        long total = toLong(countQuery.getSingleResult());

        int offset = Math.max(page, 0) * Math.max(size, 1);
        String selectSql =
            "SELECT AVH_AV_NUM, AVH_AV_STS, NVL(AVH_SPL_NAM1, AVH_SPL_COD), AVH_PO_NUM, " +
            "AVH_SCDL_YMD, AVH_ARV_YMD, 0, AVH_RMKS " +
                "FROM " + source + where +
                " ORDER BY AVH_SCDL_YMD DESC, AVH_AV_NUM DESC " +
                "OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY";

        Query listQuery = em.createNativeQuery(selectSql);
        applyParams(listQuery, params);
        listQuery.setParameter("offset", offset);
        listQuery.setParameter("size", Math.max(size, 1));

        @SuppressWarnings("unchecked")
        List<Object[]> rows = listQuery.getResultList();
        List<InboundOrderDto> data = new ArrayList<>();

        for (Object[] row : rows) {
            InboundOrderDto dto = new InboundOrderDto();
            dto.setArrivalNumber(toStringValue(row[0]));
            dto.setArrivalStatus(toStringValue(row[1]));
            dto.setSupplierCode(toStringValue(row[2]));
            dto.setPoNumber(toStringValue(row[3]));
            dto.setScheduledDate(toLocalDate(row[4]));
            dto.setActualDate(toLocalDate(row[5]));
            dto.setTotalQuantity(toInteger(row[6]));
            dto.setRemarks(toStringValue(row[7]));
            data.add(dto);
        }

        return new InboundOrderSearchResult(data, total);
    }

    /**
     * Exact-match lookup for a single arrival number.
     * Returns null if not found.
     */
    public InboundOrderDto findByArrivalNumber(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        log.info("[findByArrivalNumber] cpny={} whs={} cust={} avNum={}", companyCode, warehouseCode, customerCode, arrivalNumber);

        String sql =
            "SELECT h.AVH_AV_NUM, h.AVH_AV_STS, NVL(h.AVH_SPL_NAM1, h.AVH_SPL_COD), h.AVH_SPL_COD, " +
            "h.AVH_PO_NUM, h.AVH_TRN_KND, h.AVH_SCDL_YMD, h.AVH_ARV_YMD, h.AVH_RMKS " +
            "FROM SGWH0001.GWH_TJ_AV_H h " +
            "WHERE h.AVH_CPNY_COD = :cpny AND h.AVH_WHS_COD = :whs AND h.AVH_CUST_COD = :cust " +
            "  AND h.AVH_AV_NUM = :avNum AND h.DEL_FLG = '0'";

        log.info("[findByArrivalNumber] SQL: {}", sql);

        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", companyCode);
            q.setParameter("whs", warehouseCode);
            q.setParameter("cust", customerCode);
            q.setParameter("avNum", arrivalNumber);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            log.info("[findByArrivalNumber] rows returned: {}", rows.size());

            if (rows.isEmpty()) {
                log.warn("[findByArrivalNumber] No row found for avNum={} cpny={} whs={} cust={}", arrivalNumber, companyCode, warehouseCode, customerCode);
                return null;
            }

            Object[] row = rows.get(0);
            log.info("[findByArrivalNumber] row[0]={} row[1]={} row[2]={} row[3]={}", row[0], row[1], row[2], row[3]);

            InboundOrderDto dto = new InboundOrderDto();
            dto.setArrivalNumber(toStringValue(row[0]));
            dto.setArrivalStatus(toStringValue(row[1]));
            dto.setSupplierCode(toStringValue(row[3]));
            dto.setSupplierName(toStringValue(row[2]));
            dto.setPoNumber(toStringValue(row[4]));
            dto.setTransactionKind(toStringValue(row[5]));
            dto.setScheduledDate(toLocalDate(row[6]));
            dto.setActualDate(toLocalDate(row[7]));
            dto.setRemarks(toStringValue(row[8]));
            log.info("[findByArrivalNumber] returning DTO arrivalNumber={} status={}", dto.getArrivalNumber(), dto.getArrivalStatus());
            return dto;
        } catch (Exception ex) {
            log.error("[findByArrivalNumber] EXCEPTION: {}", ex.getMessage(), ex);
            throw ex;
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

    private void applyParams(Query query, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    private String toStringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private int toInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return ((Date) value).toLocalDate();
        }
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime().toLocalDate();
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        return null;
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

    /**
     * Fetch full arrival header with transaction kind label for detail/edit screens.
     */
    public InboundOrderDetailDto findDetailByArrivalNumber(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        String sql =
            "SELECT h.AVH_AV_NUM, h.AVH_AV_STS, h.AVH_TRN_KND, " +
            "  h.AVH_SPL_COD, NVL(h.AVH_SPL_NAM1, h.AVH_SPL_COD), " +
            "  h.AVH_PO_NUM, h.AVH_RF_NUM, h.AVH_SCDL_YMD, h.AVH_ARV_YMD, " +
            "  h.AVH_WGT, h.AVH_M3, h.AVH_RMKS, " +
            "  TO_CHAR(h.UPD_YMDHMS, 'YYYY-MM-DD HH24:MI:SS.FF3'), " +
            "  t.TRN_NAM " +
            "FROM SGWH0001.GWH_TJ_AV_H h " +
            "LEFT JOIN SGWH0001.GWH_TM_TRN t ON t.TRN_CPNY_COD = h.AVH_CPNY_COD " +
            "  AND t.TRN_WHS_COD = h.AVH_WHS_COD AND t.TRN_CUST_COD = h.AVH_CUST_COD " +
            "  AND t.TRN_KND = h.AVH_TRN_KND AND t.DEL_FLG = '0' " +
            "WHERE h.AVH_CPNY_COD = :cpny AND h.AVH_WHS_COD = :whs " +
            "  AND h.AVH_CUST_COD = :cust AND h.AVH_AV_NUM = :avNum AND h.DEL_FLG = '0'";

        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", companyCode);
            q.setParameter("whs", warehouseCode);
            q.setParameter("cust", customerCode);
            q.setParameter("avNum", arrivalNumber);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            if (rows.isEmpty()) {
                return null;
            }

            Object[] row = rows.get(0);
            InboundOrderDetailDto dto = new InboundOrderDetailDto();
            dto.setArrivalNumber(toStringValue(row[0]));
            dto.setArrivalStatus(toStringValue(row[1]));
            dto.setTransactionKind(toStringValue(row[2]));
            dto.setSupplierCode(toStringValue(row[3]));
            dto.setSupplierName(toStringValue(row[4]));
            dto.setPoNumber(toStringValue(row[5]));
            dto.setReferenceNumber(toStringValue(row[6]));
            dto.setScheduledDate(toLocalDate(row[7]));
            dto.setActualDate(toLocalDate(row[8]));
            dto.setWeight(toBigDecimal(row[9]));
            dto.setVolumeM3(toBigDecimal(row[10]));
            dto.setRemarks(toStringValue(row[11]));
            dto.setUpdTimestamp(toStringValue(row[12]));
            dto.setTransactionKindLabel(toStringValue(row[13]));
            return dto;
        } catch (Exception ex) {
            log.error("[findDetailByArrivalNumber] EXCEPTION: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Fetch all active detail lines for an arrival order.
     */
    public List<InboundOrderDetailDto.Line> findDetailLines(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        String sql =
            "SELECT d.AVD_AVLN_NUM, d.AVD_AV_STS, d.AVD_INSP_STS, " +
            "  d.AVD_PROD_COD, d.AVD_PROD_NAM, d.AVD_ORGN_COD, " +
            "  d.AVD_SPPR_COD, d.AVD_SPPR_NAM, " +
            "  d.AVD_PPCS_QTY, d.AVD_SCS_QTY, d.AVD_STPC_QTY, " +
            "  d.AVD_WGT, d.AVD_M3, d.AVD_SBIV_COD, " +
            "  d.AVD_PIK1, d.AVD_PIK2, d.AVD_PIK3, d.AVD_PIK4, " +
            "  d.AVD_PIK5, d.AVD_PIK6, d.AVD_PIK7, d.AVD_RMKS " +
            "FROM SGWH0001.GWH_TJ_AV_D d " +
            "WHERE d.AVD_CPNY_COD = :cpny AND d.AVD_WHS_COD = :whs " +
            "  AND d.AVD_CUST_COD = :cust AND d.AVD_AV_NUM = :avNum AND d.DEL_FLG = '0' " +
            "ORDER BY d.AVD_AVLN_NUM ASC";

        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", companyCode);
            q.setParameter("whs", warehouseCode);
            q.setParameter("cust", customerCode);
            q.setParameter("avNum", arrivalNumber);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            List<InboundOrderDetailDto.Line> lines = new ArrayList<>();

            for (Object[] row : rows) {
                InboundOrderDetailDto.Line line = new InboundOrderDetailDto.Line();
                line.setLineNumber(toBigDecimal(row[0]));
                line.setLineStatus(toStringValue(row[1]));
                line.setInspectionStatus(toStringValue(row[2]));
                line.setProductCode(toStringValue(row[3]));
                line.setProductName(toStringValue(row[4]));
                line.setOriginCode(toStringValue(row[5]));
                line.setShipperCode(toStringValue(row[6]));
                line.setShipperName(toStringValue(row[7]));
                line.setPlannedPieceQuantity(toBigDecimal(row[8]));
                line.setPlannedCaseQuantity(toBigDecimal(row[9]));
                line.setTotalPieceQuantity(toBigDecimal(row[10]));
                line.setWeight(toBigDecimal(row[11]));
                line.setVolumeM3(toBigDecimal(row[12]));
                line.setSubInventoryCode(toStringValue(row[13]));
                line.setPik1(toStringValue(row[14]));
                line.setPik2(toStringValue(row[15]));
                line.setPik3(toStringValue(row[16]));
                line.setPik4(toStringValue(row[17]));
                line.setPik5(toStringValue(row[18]));
                line.setPik6(toStringValue(row[19]));
                line.setPik7(toStringValue(row[20]));
                line.setRemarks(toStringValue(row[21]));
                lines.add(line);
            }
            return lines;
        } catch (Exception ex) {
            log.error("[findDetailLines] EXCEPTION: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        try {
            return new BigDecimal(value.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Fetch arrival result records (inspection/receiving data) for an arrival order.
     */
    public List<InboundOrderDetailDto.ArrivalResult> findArrivalResults(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        String sql =
            "SELECT r.AVR_ASLN_NUM, r.AVR_ASSQ_NUM, r.AVR_AVSP_STS, " +
            "  r.AVR_PROD_COD, r.AVR_RCS_QTY, r.AVR_RPC_QTY, r.AVR_RTPC_QTY, " +
            "  r.AVR_AREA_COD, r.AVR_RACK_COD, r.AVR_PSTN_COD, r.AVR_LVL_COD, " +
            "  r.AVR_DMG_FLG, r.AVR_RMKS " +
            "FROM SGWH0001.GWH_TJ_AV_R r " +
            "WHERE r.AVR_CPNY_COD = :cpny AND r.AVR_WHS_COD = :whs " +
            "  AND r.AVR_CUST_COD = :cust AND r.AVR_AS_NUM = :avNum AND r.DEL_FLG = '0' " +
            "ORDER BY r.AVR_ASLN_NUM, r.AVR_ASSQ_NUM";

        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", companyCode);
            q.setParameter("whs", warehouseCode);
            q.setParameter("cust", customerCode);
            q.setParameter("avNum", arrivalNumber);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            List<InboundOrderDetailDto.ArrivalResult> results = new ArrayList<>();

            for (Object[] row : rows) {
                InboundOrderDetailDto.ArrivalResult ar = new InboundOrderDetailDto.ArrivalResult();
                ar.setLineNumber(toBigDecimal(row[0]));
                ar.setSequenceNumber(toBigDecimal(row[1]));
                ar.setResultStatus(toStringValue(row[2]));
                ar.setProductCode(toStringValue(row[3]));
                ar.setResultCaseQty(toBigDecimal(row[4]));
                ar.setResultPieceQty(toBigDecimal(row[5]));
                ar.setResultTotalPieceQty(toBigDecimal(row[6]));
                ar.setAreaCode(toStringValue(row[7]));
                ar.setRackCode(toStringValue(row[8]));
                ar.setPositionCode(toStringValue(row[9]));
                ar.setLevelCode(toStringValue(row[10]));
                ar.setDamageFlag(toStringValue(row[11]));
                ar.setRemarks(toStringValue(row[12]));

                // Build combined location string
                String area = toStringValue(row[7]);
                String rack = toStringValue(row[8]);
                String pos = toStringValue(row[9]);
                String lvl = toStringValue(row[10]);
                StringBuilder loc = new StringBuilder();
                if (area != null) loc.append(area);
                if (rack != null) { if (loc.length() > 0) loc.append("-"); loc.append(rack); }
                if (pos != null) { if (loc.length() > 0) loc.append("-"); loc.append(pos); }
                if (lvl != null) { if (loc.length() > 0) loc.append("-"); loc.append(lvl); }
                ar.setLocation(loc.length() > 0 ? loc.toString() : null);

                results.add(ar);
            }
            return results;
        } catch (Exception ex) {
            log.error("[findArrivalResults] EXCEPTION: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Fetch LPN records linked to this arrival order.
     */
    public List<InboundOrderDetailDto.LpnInfo> findArrivalLpns(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        String sql =
            "SELECT l.LPN_NUM, l.LPN_TYPE, l.LPN_STS, l.LPN_LOC_COD, " +
            "  l.LPN_TTL_QTY, l.LPN_TTL_WGT, l.LPN_TTL_VOL, " +
            "  l.LPN_RCV_YMD, l.LPN_RMK " +
            "FROM SGWH0001.GWH_TJ_LPN l " +
            "WHERE l.LPN_CPNY_COD = :cpny AND l.LPN_WHS_COD = :whs " +
            "  AND l.LPN_CUST_COD = :cust AND l.LPN_AV_NUM = :avNum AND l.DEL_FLG = '0' " +
            "ORDER BY l.LPN_NUM";

        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", companyCode);
            q.setParameter("whs", warehouseCode);
            q.setParameter("cust", customerCode);
            q.setParameter("avNum", arrivalNumber);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            List<InboundOrderDetailDto.LpnInfo> lpns = new ArrayList<>();

            for (Object[] row : rows) {
                InboundOrderDetailDto.LpnInfo lpn = new InboundOrderDetailDto.LpnInfo();
                lpn.setLpnNumber(toStringValue(row[0]));
                lpn.setLpnType(toStringValue(row[1]));
                lpn.setLpnStatus(toStringValue(row[2]));
                lpn.setLocationCode(toStringValue(row[3]));
                lpn.setTotalQty(toBigDecimal(row[4]));
                lpn.setTotalWeight(toBigDecimal(row[5]));
                lpn.setTotalVolume(toBigDecimal(row[6]));
                lpn.setReceiveDate(toStringValue(row[7]));
                lpn.setRemarks(toStringValue(row[8]));
                lpns.add(lpn);
            }
            return lpns;
        } catch (Exception ex) {
            log.error("[findArrivalLpns] EXCEPTION: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public static class InboundOrderSearchResult {
        private final List<InboundOrderDto> records;
        private final long totalRecords;

        public InboundOrderSearchResult(List<InboundOrderDto> records, long totalRecords) {
            this.records = records;
            this.totalRecords = totalRecords;
        }

        public List<InboundOrderDto> getRecords() {
            return records;
        }

        public long getTotalRecords() {
            return totalRecords;
        }
    }
}
