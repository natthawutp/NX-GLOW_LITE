package jp.co.nittsu.gwh.module.inventory.repository;

import jp.co.nittsu.gwh.module.inventory.dto.StockDto;
import jp.co.nittsu.gwh.module.inventory.dto.StockSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stock inquiry repository using native SQL queries.
 * Ports legacy GwhLpski020DAO logic to modern Spring Boot pattern.
 * Queries VGWH_TJ_ST with LEFT JOINs to VGWH_TM_PROD and VGWH_TM_SBIV.
 */
@Repository
public class StockRepository {

    private static final Logger log = LoggerFactory.getLogger(StockRepository.class);

    private static final String[] SOURCE_CANDIDATES = {
            "VGWH_TJ_ST",
            "SGWH0001.VGWH_TJ_ST"
    };

    private static final String[] PROD_CANDIDATES = {
            "VGWH_TM_PROD",
            "SGWH0001.VGWH_TM_PROD"
    };

    private static final String[] SBIV_CANDIDATES = {
            "VGWH_TM_SBIV",
            "SGWH0001.VGWH_TM_SBIV"
    };

    private static final String[] CUST_CANDIDATES = {
            "VGWH_TM_CUST",
            "SGWH0001.VGWH_TM_CUST"
    };

    @PersistenceContext
    private EntityManager em;

    // ---- Public methods ----

    /**
     * Main paginated search query with dynamic GROUP BY based on DROP flags.
     */
    public StockSearchResultData search(
            String cpny, String whs, String cust,
            StockSearchRequest request, boolean pieceMode, int maxRows) {

        for (int i = 0; i < SOURCE_CANDIDATES.length; i++) {
            try {
                return searchFromSource(
                        SOURCE_CANDIDATES[i],
                        i < PROD_CANDIDATES.length ? PROD_CANDIDATES[i] : PROD_CANDIDATES[0],
                        i < SBIV_CANDIDATES.length ? SBIV_CANDIDATES[i] : SBIV_CANDIDATES[0],
                        cpny, whs, cust, request, pieceMode, maxRows);
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
                if (i == SOURCE_CANDIDATES.length - 1) {
                    throw ex;
                }
            }
        }
        throw new IllegalStateException("Stock source object not found");
    }

    /**
     * Quantity totals aggregation query (no pagination).
     */
    public List<StockDto> searchQtyTotals(
            String cpny, String whs, String cust,
            StockSearchRequest request, boolean pieceMode) {

        Map<String, Object> params = new LinkedHashMap<>();
        String where = buildWhereClause(cpny, whs, cust, request, params);
        String groupBy = buildGroupByClause(request);
        String having = buildHavingClause(request);

        String stSource = resolveSource(SOURCE_CANDIDATES);
        String prodSource = resolveSource(PROD_CANDIDATES);
        String sbivSource = resolveSource(SBIV_CANDIDATES);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("SUM(T1.ST_PYST_QTY) AS ST_PYST_QTY, ");
        sql.append("SUM(T1.ST_AVST_QTY) AS ST_AVST_QTY, ");
        sql.append("SUM(T1.ST_ALST_QTY) AS ST_ALST_QTY, ");
        sql.append("TRUNC(SUM(T1.ST_PYST_QTY) / T1.ST_PPCS_QTY) AS ST_PYCS_QTY, ");
        sql.append("MOD(SUM(T1.ST_PYST_QTY), T1.ST_PPCS_QTY) AS ST_PYPC_QTY, ");
        if (!pieceMode) {
            sql.append("TRUNC(SUM(T1.ST_AVST_QTY) / T1.ST_PPCS_QTY) AS ST_AVCS_QTY, ");
            sql.append("MOD(SUM(T1.ST_AVST_QTY), T1.ST_PPCS_QTY) AS ST_AVPC_QTY, ");
        }
        sql.append("T1.ST_PPCS_QTY ");
        sql.append("FROM ").append(stSource).append(" T1 ");
        appendJoins(sql, prodSource, sbivSource);
        sql.append(where);
        sql.append(" GROUP BY ").append(groupBy);
        sql.append(having);

        Query query = em.createNativeQuery(sql.toString());
        applyParams(query, params);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        List<StockDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            StockDto dto = new StockDto();
            int col = 0;
            dto.setPhysicalStockQty(toBigDecimal(row[col++]));
            dto.setAvailableStockQty(toBigDecimal(row[col++]));
            dto.setAllocatedStockQty(toBigDecimal(row[col++]));
            dto.setPhysicalCasesQty(toBigDecimal(row[col++]));
            dto.setPhysicalPiecesQty(toBigDecimal(row[col++]));
            if (!pieceMode) {
                dto.setAvailableCasesQty(toBigDecimal(row[col++]));
                dto.setAvailablePiecesQty(toBigDecimal(row[col++]));
            }
            dto.setPiecesPerCase(toBigDecimal(row[col++]));
            result.add(dto);
        }
        return result;
    }

    /**
     * Count of grouped records for overflow detection.
     */
    public long searchCount(
            String cpny, String whs, String cust,
            StockSearchRequest request) {

        Map<String, Object> params = new LinkedHashMap<>();
        String where = buildWhereClause(cpny, whs, cust, request, params);
        String groupBy = buildGroupByClause(request);
        String having = buildHavingClause(request);

        String stSource = resolveSource(SOURCE_CANDIDATES);
        String prodSource = resolveSource(PROD_CANDIDATES);
        String sbivSource = resolveSource(SBIV_CANDIDATES);

        StringBuilder inner = new StringBuilder();
        inner.append("SELECT 1 FROM ").append(stSource).append(" T1 ");
        appendJoins(inner, prodSource, sbivSource);
        inner.append(where).append(" GROUP BY ").append(groupBy).append(having);

        String sql = "SELECT COUNT(1) FROM (" + inner + ")";

        Query query = em.createNativeQuery(sql);
        applyParams(query, params);
        return toLong(query.getSingleResult());
    }

    /**
     * Load customer preferences (PCSM flag, SBIV flag).
     */
    public CustomerPrefs loadCustomerPrefs(String cpny, String whs, String cust) {
        String source = resolveSource(CUST_CANDIDATES);
        String sql = "SELECT CUST_PCSM_FLG, CUST_SBIV_FLG FROM " + source
                + " WHERE CUST_CPNY_COD = :cpny AND CUST_WHS_COD = :whs"
                + " AND CUST_COD = :cust AND DEL_FLG = '0'";

        try {
            Query query = em.createNativeQuery(sql);
            query.setParameter("cpny", cpny);
            query.setParameter("whs", whs);
            query.setParameter("cust", cust);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();
            if (!rows.isEmpty()) {
                Object[] row = rows.get(0);
                return new CustomerPrefs(
                        toStringValue(row[0]),
                        toStringValue(row[1])
                );
            }
        } catch (RuntimeException ex) {
            log.warn("Failed to load customer preferences: {}", ex.getMessage());
        }
        return new CustomerPrefs(null, null);
    }

    // ---- Private query execution ----

    private StockSearchResultData searchFromSource(
            String stSource, String prodSource, String sbivSource,
            String cpny, String whs, String cust,
            StockSearchRequest request, boolean pieceMode, int maxRows) {

        Map<String, Object> params = new LinkedHashMap<>();
        String where = buildWhereClause(cpny, whs, cust, request, params);
        String groupBy = buildGroupByClause(request);
        String having = buildHavingClause(request);
        String orderBy = buildOrderByClause(request);

        // --- Count query ---
        StringBuilder countInner = new StringBuilder();
        countInner.append("SELECT 1 FROM ").append(stSource).append(" T1 ");
        appendJoins(countInner, prodSource, sbivSource);
        countInner.append(where).append(" GROUP BY ").append(groupBy).append(having);
        String countSql = "SELECT COUNT(1) FROM (" + countInner + ")";

        Query countQuery = em.createNativeQuery(countSql);
        applyParams(countQuery, params);
        long total = toLong(countQuery.getSingleResult());

        // --- Main select query ---
        StringBuilder select = new StringBuilder("SELECT ");
        select.append("T1.ST_CPNY_COD, T1.ST_WHS_COD, T1.ST_CUST_COD, ");
        select.append("T1.ST_PROD_COD, M1.PROD_NAM1, T1.ST_ORGN_COD, ");

        // PIK columns with DROP logic
        appendDropColumn(select, "T1.ST_PIK1", "ST_PIK1", request.getPik1Drop());
        appendDropColumn(select, "T1.ST_PIK2", "ST_PIK2", request.getPik2Drop());
        appendDropColumn(select, "T1.ST_PIK3", "ST_PIK3", request.getPik3Drop());
        appendDropColumn(select, "T1.ST_PIK4", "ST_PIK4", request.getPik4Drop());
        appendDropColumn(select, "T1.ST_PIK5", "ST_PIK5", request.getPik5Drop());
        select.append("T1.ST_PIK6, T1.ST_PIK7, ");

        // Location columns with DROP logic
        String locExpr = buildLocationExpr(
                request.getAreaDrop(), request.getRackDrop(),
                request.getPosDrop(), request.getLevDrop());
        select.append(locExpr).append(" AS LOCATION, ");
        appendDropColumn(select, "T1.ST_AREA_COD", "ST_AREA_COD", request.getAreaDrop());
        appendDropColumn(select, "T1.ST_RACK_COD", "ST_RACK_COD", request.getRackDrop());
        appendDropColumn(select, "T1.ST_PSTN_COD", "ST_PSTN_COD", request.getPosDrop());
        appendDropColumnNoComma(select, "T1.ST_LVL_COD", "ST_LVL_COD", request.getLevDrop());
        select.append(", ");

        // Sub-inventory with DROP logic
        if (isDistinct(request.getSubDrop())) {
            select.append("T1.ST_SBIV_COD, M2.SBIV_NAM, ");
        } else {
            select.append("'*' AS ST_SBIV_COD, '*' AS SBIV_NAM, ");
        }

        // Flags
        select.append("T1.ST_BOND_FLG, ");
        appendDropColumn(select, "T1.ST_DMG_FLG", "ST_DMG_FLG", request.getDmgDrop());
        select.append("T1.ST_LOCK_FLG, ");

        // Quantity columns
        select.append("TRUNC(SUM(T1.ST_PYST_QTY) / T1.ST_PPCS_QTY) AS ST_PYCS_QTY, ");
        select.append("MOD(SUM(T1.ST_PYST_QTY), T1.ST_PPCS_QTY) AS ST_PYPC_QTY, ");
        select.append("SUM(T1.ST_PYST_QTY) AS ST_PYST_QTY, ");
        if (!pieceMode) {
            select.append("TRUNC(SUM(T1.ST_AVST_QTY) / T1.ST_PPCS_QTY) AS ST_AVCS_QTY, ");
            select.append("MOD(SUM(T1.ST_AVST_QTY), T1.ST_PPCS_QTY) AS ST_AVPC_QTY, ");
        }
        select.append("SUM(T1.ST_AVST_QTY) AS ST_AVST_QTY, ");
        select.append("T1.ST_PPCS_QTY, ");
        select.append("SUM(T1.ST_ALST_QTY) AS ST_ALST_QTY ");

        // FROM + JOINs
        select.append("FROM ").append(stSource).append(" T1 ");
        appendJoins(select, prodSource, sbivSource);

        select.append(where);
        select.append(" GROUP BY ").append(groupBy);
        select.append(having);
        select.append(" ORDER BY ").append(orderBy);

        // Pagination
        int offset = Math.max(request.getPage(), 0) * Math.max(maxRows, 1);
        select.append(" OFFSET :offset ROWS FETCH NEXT :fetchSize ROWS ONLY");

        Query listQuery = em.createNativeQuery(select.toString());
        applyParams(listQuery, params);
        listQuery.setParameter("offset", offset);
        listQuery.setParameter("fetchSize", Math.max(maxRows, 1));

        @SuppressWarnings("unchecked")
        List<Object[]> rows = listQuery.getResultList();
        List<StockDto> data = new ArrayList<>();

        for (Object[] row : rows) {
            data.add(mapRowToDto(row, pieceMode));
        }

        return new StockSearchResultData(data, total);
    }

    // ---- WHERE clause builder ----

    private String buildWhereClause(
            String cpny, String whs, String cust,
            StockSearchRequest request,
            Map<String, Object> params) {

        StringBuilder where = new StringBuilder();
        where.append(" WHERE T1.DEL_FLG = '0'");
        where.append(" AND T1.ST_CPNY_COD = :cpny");
        where.append(" AND T1.ST_WHS_COD = :whs");
        where.append(" AND T1.ST_CUST_COD = :cust");

        params.put("cpny", cpny);
        params.put("whs", whs);
        params.put("cust", cust);

        addEqualCondition(where, params, "T1.ST_AV_NUM", "avNum", request.getArrivalNumber());
        addEqualCondition(where, params, "T1.ST_AVLN_NUM", "avLn", request.getArrivalLineNumber());
        addEqualCondition(where, params, "T1.ST_AVSQ_NUM", "avSq", request.getArrivalSeqNumber());

        if (request.getDateFrom() != null) {
            where.append(" AND T1.ST_AV_YMD >= :dateFrom");
            params.put("dateFrom", Date.valueOf(request.getDateFrom()));
        }
        if (request.getDateTo() != null) {
            where.append(" AND T1.ST_AV_YMD <= :dateTo");
            params.put("dateTo", Date.valueOf(request.getDateTo()));
        }

        // Product code with forward search support
        if (isNotEmpty(request.getProductCode())) {
            if (request.isForwardSearch()) {
                where.append(" AND T1.ST_PROD_COD LIKE :prodCod");
                params.put("prodCod", request.getProductCode().trim() + "%");
            } else {
                where.append(" AND T1.ST_PROD_COD = :prodCod");
                params.put("prodCod", request.getProductCode().trim());
            }
        }

        addEqualCondition(where, params, "T1.ST_ORGN_COD", "orgn", request.getOriginCode());
        addEqualCondition(where, params, "T1.ST_PIK1", "pik1", request.getPik1());
        addEqualCondition(where, params, "T1.ST_PIK2", "pik2", request.getPik2());
        addEqualCondition(where, params, "T1.ST_PIK3", "pik3", request.getPik3());
        addEqualCondition(where, params, "T1.ST_PIK4", "pik4", request.getPik4());
        addEqualCondition(where, params, "T1.ST_PIK5", "pik5", request.getPik5());
        addEqualCondition(where, params, "T1.ST_PIK6", "pik6", request.getPik6());
        addEqualCondition(where, params, "T1.ST_PIK7", "pik7", request.getPik7());

        // Area code with right-pad (legacy pads to 3 chars)
        if (isNotEmpty(request.getAreaCode())) {
            String padded = String.format("%-3s", request.getAreaCode().trim());
            where.append(" AND T1.ST_AREA_COD = :areaCod");
            params.put("areaCod", padded);
        }
        addEqualCondition(where, params, "T1.ST_RACK_COD", "rackCod", request.getRackCode());
        addEqualCondition(where, params, "T1.ST_PSTN_COD", "pstnCod", request.getPositionCode());
        addEqualCondition(where, params, "T1.ST_LVL_COD", "lvlCod", request.getLevelCode());

        // Temp location filter (UNPIVOT subquery)
        if (request.isTempLocationOnly()) {
            String custSource = resolveSource(CUST_CANDIDATES);
            where.append(" AND (NVL(ST_AREA_COD, '') || NVL(TRIM(ST_RACK_COD), '') || NVL(TRIM(ST_PSTN_COD), '') || NVL(TRIM(ST_LVL_COD), '')");
            where.append(" IN (SELECT NVL(TRIM(LOCATION), '') AS LOCATION FROM (");
            where.append(" SELECT TEMP_CODE, AREA||RACK||PSTN||LVL AS LOCATION FROM ").append(custSource);
            where.append(" UNPIVOT ((AREA,RACK,PSTN,LVL) FOR TEMP_CODE IN (");
            where.append("(CUST_AV_AREA,CUST_AV_RACK,CUST_AV_PSTN,CUST_AV_LVL) AS 'AV',");
            where.append("(CUST_ALBK_AREA,CUST_ALBK_RACK,CUST_ALBK_PSTN,CUST_ALBK_LVL) AS 'ALBK',");
            where.append("(CUST_KITP_AREA,CUST_KITP_RACK,CUST_KITP_PSTN,CUST_KITP_LVL) AS 'KITP',");
            where.append("(CUST_UREP_AREA,CUST_UREP_RACK,CUST_UREP_PSTN,CUST_UREP_LVL) AS 'UREP',");
            where.append("(CUST_RTRN_AREA,CUST_RTRN_RACK,CUST_RTRN_PSTN,CUST_RTRN_LVL) AS 'RTRN',");
            where.append("(CUST_EXCP_AREA,CUST_EXCP_RACK,CUST_EXCP_PSTN,CUST_EXCP_LVL) AS 'EXCP'");
            where.append("))");
            where.append(" WHERE CUST_CPNY_COD = :tmpCpny AND CUST_WHS_COD = :tmpWhs AND CUST_COD = :tmpCust");
            where.append(") WHERE LOCATION IS NOT NULL))");
            params.put("tmpCpny", cpny);
            params.put("tmpWhs", whs);
            params.put("tmpCust", cust);
        }

        addEqualCondition(where, params, "T1.ST_SBIV_COD", "sbiv", request.getSubInventoryCode());
        addEqualCondition(where, params, "T1.ST_PSSA_FLG", "pssa", request.getPassaFlag());
        addEqualCondition(where, params, "T1.ST_BOND_FLG", "bond", request.getBondFlag());
        addEqualCondition(where, params, "T1.ST_DMG_FLG", "dmg", request.getDamageFlag());
        addEqualCondition(where, params, "T1.ST_LOCK_FLG", "lock", request.getLockFlag());

        return where.toString();
    }

    // ---- GROUP BY clause builder ----

    private String buildGroupByClause(StockSearchRequest request) {
        StringBuilder gb = new StringBuilder();
        gb.append("T1.ST_CPNY_COD, T1.ST_WHS_COD, T1.ST_CUST_COD, ");
        gb.append("T1.ST_PROD_COD, T1.ST_ORGN_COD, ");

        // PIK columns: include in GROUP BY only if distinct
        appendGroupByIfDistinct(gb, "T1.ST_PIK1", request.getPik1Drop());
        appendGroupByIfDistinct(gb, "T1.ST_PIK2", request.getPik2Drop());
        appendGroupByIfDistinct(gb, "T1.ST_PIK3", request.getPik3Drop());
        appendGroupByIfDistinct(gb, "T1.ST_PIK4", request.getPik4Drop());
        appendGroupByIfDistinct(gb, "T1.ST_PIK5", request.getPik5Drop());
        gb.append("T1.ST_PIK6, T1.ST_PIK7, ");

        // Location columns
        appendGroupByIfDistinct(gb, "T1.ST_AREA_COD", request.getAreaDrop());
        appendGroupByIfDistinct(gb, "T1.ST_RACK_COD", request.getRackDrop());
        appendGroupByIfDistinct(gb, "T1.ST_PSTN_COD", request.getPosDrop());
        appendGroupByIfDistinct(gb, "T1.ST_LVL_COD", request.getLevDrop());

        // Sub-inventory
        if (isDistinct(request.getSubDrop())) {
            gb.append("T1.ST_SBIV_COD, ");
        }

        gb.append("T1.ST_BOND_FLG, ");
        appendGroupByIfDistinct(gb, "T1.ST_DMG_FLG", request.getDmgDrop());
        gb.append("T1.ST_LOCK_FLG, ");

        // Master table columns and PPCS_QTY in GROUP BY
        gb.append("M1.PROD_NAM1, ");
        if (isDistinct(request.getSubDrop())) {
            gb.append("M2.SBIV_NAM, ");
        }
        gb.append("T1.ST_PPCS_QTY");

        return gb.toString();
    }

    // ---- HAVING clause builder ----

    private String buildHavingClause(StockSearchRequest request) {
        StringBuilder having = new StringBuilder(" HAVING 1 = 1");

        String pyFilter = request.getPhysicalQtyFilter();
        if ("2".equals(pyFilter)) {
            having.append(" AND SUM(ST_PYST_QTY) > 0");
        } else if ("3".equals(pyFilter)) {
            having.append(" AND SUM(ST_PYST_QTY) = 0");
        }

        String avFilter = request.getAvailableQtyFilter();
        if ("2".equals(avFilter)) {
            having.append(" AND SUM(ST_AVST_QTY) > 0");
        } else if ("3".equals(avFilter)) {
            having.append(" AND SUM(ST_AVST_QTY) = 0");
        }

        return having.toString();
    }

    // ---- ORDER BY clause builder ----

    private String buildOrderByClause(StockSearchRequest request) {
        if (isNotEmpty(request.getSortField()) && !"ROWNUM".equals(request.getSortField())) {
            String dir = "DESC".equalsIgnoreCase(request.getSortOrder()) ? "DESC" : "ASC";
            return request.getSortField() + " " + dir;
        }
        // Default sort matching legacy
        return "T1.ST_PROD_COD, T1.ST_ORGN_COD, T1.ST_BOND_FLG, "
                + "T1.ST_LOCK_FLG, T1.ST_PIK1, T1.ST_PIK2, T1.ST_PIK3, "
                + "T1.ST_PIK4, T1.ST_PIK5, T1.ST_PIK6, T1.ST_PIK7, "
                + "T1.ST_AREA_COD, T1.ST_RACK_COD, T1.ST_PSTN_COD, T1.ST_LVL_COD";
    }

    // ---- JOIN helper ----

    private void appendJoins(StringBuilder sql, String prodSource, String sbivSource) {
        sql.append("LEFT JOIN ").append(prodSource).append(" M1 ON ");
        sql.append("T1.ST_CPNY_COD = M1.PROD_CPNY_COD ");
        sql.append("AND T1.ST_WHS_COD = M1.PROD_WHS_COD ");
        sql.append("AND T1.ST_CUST_COD = M1.PROD_CUST_COD ");
        sql.append("AND T1.ST_PROD_COD = M1.PROD_COD ");
        sql.append("LEFT JOIN ").append(sbivSource).append(" M2 ON ");
        sql.append("T1.ST_CPNY_COD = M2.SBIV_CPNY_COD ");
        sql.append("AND T1.ST_WHS_COD = M2.SBIV_WHS_COD ");
        sql.append("AND T1.ST_CUST_COD = M2.SBIV_CUST_COD ");
        sql.append("AND T1.ST_SBIV_COD = M2.SBIV_COD ");
    }

    // ---- DROP flag helpers ----

    private boolean isDistinct(String dropFlag) {
        return dropFlag == null || dropFlag.isEmpty() || "01".equals(dropFlag);
    }

    private boolean isAggregate(String dropFlag) {
        return "02".equals(dropFlag);
    }

    private void appendDropColumn(StringBuilder sb, String col, String alias, String dropFlag) {
        if (isDistinct(dropFlag)) {
            sb.append(col).append(", ");
        } else {
            sb.append("'*' AS ").append(alias).append(", ");
        }
    }

    private void appendDropColumnNoComma(StringBuilder sb, String col, String alias, String dropFlag) {
        if (isDistinct(dropFlag)) {
            sb.append(col);
        } else {
            sb.append("'*' AS ").append(alias);
        }
    }

    private void appendGroupByIfDistinct(StringBuilder gb, String col, String dropFlag) {
        if (isDistinct(dropFlag)) {
            gb.append(col).append(", ");
        }
    }

    /**
     * Builds LOCATION expression dynamically: replaces aggregated parts with '*'.
     * This replaces the legacy's 16 if-else branches.
     */
    private String buildLocationExpr(String areaDrop, String rackDrop, String posDrop, String levDrop) {
        String area = isAggregate(areaDrop) ? "'*'" : "TRIM(T1.ST_AREA_COD)";
        String rack = isAggregate(rackDrop) ? "'*'" : "T1.ST_RACK_COD";
        String pos = isAggregate(posDrop) ? "'*'" : "T1.ST_PSTN_COD";
        String lev = isAggregate(levDrop) ? "'*'" : "T1.ST_LVL_COD";
        return "(" + area + " || '-' || " + rack + " || '-' || " + pos + " || '-' || " + lev + ")";
    }

    // ---- Row mapping ----

    private StockDto mapRowToDto(Object[] row, boolean pieceMode) {
        StockDto dto = new StockDto();
        int col = 0;
        dto.setCompanyCode(toStringValue(row[col++]));
        dto.setWarehouseCode(toStringValue(row[col++]));
        dto.setCustomerCode(toStringValue(row[col++]));
        dto.setProductCode(toStringValue(row[col++]));
        dto.setProductName(toStringValue(row[col++]));
        dto.setOriginCode(toStringValue(row[col++]));
        dto.setPik1(toStringValue(row[col++]));
        dto.setPik2(toStringValue(row[col++]));
        dto.setPik3(toStringValue(row[col++]));
        dto.setPik4(toStringValue(row[col++]));
        dto.setPik5(toStringValue(row[col++]));
        dto.setPik6(toStringValue(row[col++]));
        dto.setPik7(toStringValue(row[col++]));
        dto.setLocation(toStringValue(row[col++]));
        dto.setAreaCode(toStringValue(row[col++]));
        dto.setRackCode(toStringValue(row[col++]));
        dto.setPositionCode(toStringValue(row[col++]));
        dto.setLevelCode(toStringValue(row[col++]));
        dto.setSubInventoryCode(toStringValue(row[col++]));
        dto.setSubInventoryName(toStringValue(row[col++]));
        dto.setBondFlag(toStringValue(row[col++]));
        dto.setDamageFlag(toStringValue(row[col++]));
        dto.setLockFlag(toStringValue(row[col++]));
        dto.setPhysicalCasesQty(toBigDecimal(row[col++]));
        dto.setPhysicalPiecesQty(toBigDecimal(row[col++]));
        dto.setPhysicalStockQty(toBigDecimal(row[col++]));
        if (!pieceMode) {
            dto.setAvailableCasesQty(toBigDecimal(row[col++]));
            dto.setAvailablePiecesQty(toBigDecimal(row[col++]));
        }
        dto.setAvailableStockQty(toBigDecimal(row[col++]));
        dto.setPiecesPerCase(toBigDecimal(row[col++]));
        dto.setAllocatedStockQty(toBigDecimal(row[col++]));
        return dto;
    }

    // ---- Utility methods ----

    private String resolveSource(String[] candidates) {
        for (String source : candidates) {
            try {
                Query q = em.createNativeQuery("SELECT 1 FROM " + source + " WHERE ROWNUM = 0");
                q.getResultList();
                return source;
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
            }
        }
        return candidates[candidates.length - 1];
    }

    private void addEqualCondition(StringBuilder where, Map<String, Object> params,
                                   String column, String paramName, String value) {
        if (isNotEmpty(value)) {
            where.append(" AND ").append(column).append(" = :").append(paramName);
            params.put(paramName, value.trim());
        }
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
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

    private String toStringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
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
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }

    // ---- Inner classes ----

    public static class StockSearchResultData {
        private final List<StockDto> records;
        private final long totalRecords;

        public StockSearchResultData(List<StockDto> records, long totalRecords) {
            this.records = records;
            this.totalRecords = totalRecords;
        }

        public List<StockDto> getRecords() { return records; }
        public long getTotalRecords() { return totalRecords; }
    }

    public static class CustomerPrefs {
        private final String custPcsmFlg;
        private final String custSbivFlg;

        public CustomerPrefs(String custPcsmFlg, String custSbivFlg) {
            this.custPcsmFlg = custPcsmFlg;
            this.custSbivFlg = custSbivFlg;
        }

        public String getCustPcsmFlg() { return custPcsmFlg; }
        public String getCustSbivFlg() { return custSbivFlg; }
    }
}
