package jp.co.nittsu.gwh.module.dashboard.service;

import jp.co.nittsu.gwh.module.dashboard.dto.DashboardProductivityTrendDto;
import jp.co.nittsu.gwh.module.dashboard.dto.DashboardSummaryDto;
import jp.co.nittsu.gwh.module.dashboard.dto.DashboardSummaryDto.*;
import jp.co.nittsu.gwh.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Dashboard service providing aggregated KPIs and metrics.
 * Uses native Oracle queries against VGWH_* views filtered by tenant context.
 */
@Service
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TenantContext tenantContext;

        private static final String[] XT_SOURCES = {
            "SGWH0001.GWH_TJ_XT",
            "GWH_TJ_XT"
        };

        private static final String[] XT_KPI_SOURCES = {
            "SGWH0001.GWH_TJ_XT_KPI",
            "GWH_TJ_XT_KPI"
        };

    // Inbound status color palette
    private static final Map<String, String> INBOUND_COLORS = new LinkedHashMap<>();
    static {
        INBOUND_COLORS.put("Created", "#5BC2E7");
        INBOUND_COLORS.put("Located", "#FF9E1B");
        INBOUND_COLORS.put("Inspected", "#1A005D");
        INBOUND_COLORS.put("Confirmed", "#8EC400");
        INBOUND_COLORS.put("Cancelled", "#EF4444");
    }

    // Outbound status color palette
    private static final Map<String, String> OUTBOUND_COLORS = new LinkedHashMap<>();
    static {
        OUTBOUND_COLORS.put("New", "#5BC2E7");
        OUTBOUND_COLORS.put("Allocated", "#1A005D");
        OUTBOUND_COLORS.put("Picking", "#FF9E1B");
        OUTBOUND_COLORS.put("Packed", "#8EC400");
        OUTBOUND_COLORS.put("Shipped", "#4CAF50");
        OUTBOUND_COLORS.put("Cancelled", "#EF4444");
    }

    /**
     * Get complete dashboard summary with KPIs, status charts, activity, and alerts.
     * All queries are scoped to the current tenant (company + warehouse + customer).
     */
    public DashboardSummaryDto getSummary() {
        DashboardSummaryDto summary = new DashboardSummaryDto();
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        logger.info("Loading dashboard for tenant: cpny={}, whs={}, cust={}", cpny, whs, cust);

        Map<String, String> inboundStatusLabels = loadStatusLabels(cpny, whs, cust, true);
        Map<String, String> outboundStatusLabels = loadStatusLabels(cpny, whs, cust, false);

        summary.setKpiCards(getKpiCards(cpny, whs, cust));
        summary.setInboundByStatus(getInboundStatusCounts(cpny, whs, cust, inboundStatusLabels));
        summary.setOutboundByStatus(getOutboundStatusCounts(cpny, whs, cust, outboundStatusLabels));
        summary.setRecentActivities(getRecentActivities(cpny, whs, cust));
        summary.setWarehouseUtilization(getWarehouseUtil(cpny, whs, cust));
        summary.setInboundProductivity(getInboundProductivity(cpny, whs, cust));
        summary.setAlerts(getAlerts(cpny, whs, cust));
        return summary;
    }

    public DashboardProductivityTrendDto getInboundProductivityTrend(LocalDate dateFrom, LocalDate dateTo) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        LocalDate to = dateTo != null ? dateTo : LocalDate.now();
        LocalDate from = dateFrom != null ? dateFrom : to.minusDays(6);
        if (from.isAfter(to)) {
            LocalDate tmp = from;
            from = to;
            to = tmp;
        }

        DashboardProductivityTrendDto dto = new DashboardProductivityTrendDto();
        dto.setDateFrom(from.toString());
        dto.setDateTo(to.toString());

        Map<String, Long> milestoneByDay = getInboundMilestoneTrend(cpny, whs, cust, from, to);
        Map<String, long[]> productivityByDay = getInboundProductivityTrendMap(cpny, whs, cust, from, to);

        List<DashboardProductivityTrendDto.TrendPoint> points = new ArrayList<>();
        LocalDate cursor = from;
        while (!cursor.isAfter(to)) {
            String ymd = cursor.format(DateTimeFormatter.BASIC_ISO_DATE);
            long milestones = milestoneByDay.getOrDefault(ymd, 0L);
            long[] prod = productivityByDay.getOrDefault(ymd, new long[]{0L, 0L});
            points.add(new DashboardProductivityTrendDto.TrendPoint(cursor.toString(), milestones, prod[0], prod[1]));
            cursor = cursor.plusDays(1);
        }
        dto.setTrendPoints(points);
        return dto;
    }

    private Map<String, Long> getInboundMilestoneTrend(String cpny, String whs, String cust, LocalDate from, LocalDate to) {
        RuntimeException lastError = null;
        for (String source : XT_SOURCES) {
            try {
                String sql = "SELECT XT_STR_YMD, COUNT(*) FROM " + source + " " +
                        "WHERE XT_CPNY_COD = :cpny AND XT_WHS_COD = :whs AND XT_CUST_COD = :cust " +
                        "AND XT_AS_KND = 'AV' AND XT_STR_YMD BETWEEN :fromYmd AND :toYmd AND DEL_FLG = 0 " +
                        "GROUP BY XT_STR_YMD";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("fromYmd", from.format(DateTimeFormatter.BASIC_ISO_DATE));
                q.setParameter("toYmd", to.format(DateTimeFormatter.BASIC_ISO_DATE));

                @SuppressWarnings("unchecked")
                List<Object[]> rows = q.getResultList();
                Map<String, Long> result = new HashMap<>();
                for (Object[] row : rows) {
                    String ymd = row[0] != null ? row[0].toString().trim() : "";
                    if (!ymd.isEmpty()) {
                        result.put(ymd, toLong(row[1]));
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

    private Map<String, long[]> getInboundProductivityTrendMap(String cpny, String whs, String cust, LocalDate from, LocalDate to) {
        RuntimeException lastError = null;
        for (String source : XT_KPI_SOURCES) {
            try {
                String sql = "SELECT KPI_OP_YMD, COUNT(*) AS EV_CNT, NVL(SUM(NVL(KPI_AVLN_NUM, 0)), 0) AS LN_SUM FROM " + source + " " +
                        "WHERE KPI_CPNY_COD = :cpny AND KPI_WHS_COD = :whs AND KPI_CUST_COD = :cust " +
                        "AND KPI_AS_KND = 'AV' AND KPI_OP_YMD BETWEEN :fromYmd AND :toYmd AND DEL_FLG = 0 " +
                        "GROUP BY KPI_OP_YMD";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("fromYmd", from.format(DateTimeFormatter.BASIC_ISO_DATE));
                q.setParameter("toYmd", to.format(DateTimeFormatter.BASIC_ISO_DATE));

                @SuppressWarnings("unchecked")
                List<Object[]> rows = q.getResultList();
                Map<String, long[]> result = new HashMap<>();
                for (Object[] row : rows) {
                    String ymd = row[0] != null ? row[0].toString().trim() : "";
                    if (!ymd.isEmpty()) {
                        result.put(ymd, new long[]{toLong(row[1]), toLong(row[2])});
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

    private ProductivitySummary getInboundProductivity(String cpny, String whs, String cust) {
        ProductivitySummary summary = new ProductivitySummary();
        try {
            String todayYmd = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            summary.setMilestoneCapturedToday(countInboundMilestonesToday(cpny, whs, cust, todayYmd));
            summary.setProductivityCapturedToday(countInboundProductivityToday(cpny, whs, cust, todayYmd));
            summary.setAverageLinesPerEvent(averageInboundLinesPerEvent(cpny, whs, cust, todayYmd));
            summary.setOperationBreakdown(getInboundProductivityBreakdown(cpny, whs, cust, todayYmd));
        } catch (Exception ex) {
            logger.error("Failed to load inbound productivity summary: {}", ex.getMessage(), ex);
            summary.setMilestoneCapturedToday(0);
            summary.setProductivityCapturedToday(0);
            summary.setAverageLinesPerEvent(0);
            summary.setOperationBreakdown(Collections.singletonList(new StatusCount("No Data", 0, "#D1D5DB")));
        }
        return summary;
    }

    private long countInboundMilestonesToday(String cpny, String whs, String cust, String ymd) {
        RuntimeException lastError = null;
        for (String source : XT_SOURCES) {
            try {
                String sql = "SELECT COUNT(*) FROM " + source + " " +
                        "WHERE XT_CPNY_COD = :cpny AND XT_WHS_COD = :whs AND XT_CUST_COD = :cust " +
                        "AND XT_AS_KND = 'AV' AND XT_STR_YMD = :ymd AND DEL_FLG = 0";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("ymd", ymd);
                return toLong(q.getSingleResult());
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }
        if (lastError != null) {
            return 0;
        }
        return 0;
    }

    private long countInboundProductivityToday(String cpny, String whs, String cust, String ymd) {
        RuntimeException lastError = null;
        for (String source : XT_KPI_SOURCES) {
            try {
                String sql = "SELECT COUNT(*) FROM " + source + " " +
                        "WHERE KPI_CPNY_COD = :cpny AND KPI_WHS_COD = :whs AND KPI_CUST_COD = :cust " +
                        "AND KPI_AS_KND = 'AV' AND KPI_OP_YMD = :ymd AND DEL_FLG = 0";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("ymd", ymd);
                return toLong(q.getSingleResult());
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }
        if (lastError != null) {
            return 0;
        }
        return 0;
    }

    private double averageInboundLinesPerEvent(String cpny, String whs, String cust, String ymd) {
        RuntimeException lastError = null;
        for (String source : XT_KPI_SOURCES) {
            try {
                String sql = "SELECT NVL(AVG(NVL(KPI_AVLN_NUM, 0)), 0) FROM " + source + " " +
                        "WHERE KPI_CPNY_COD = :cpny AND KPI_WHS_COD = :whs AND KPI_CUST_COD = :cust " +
                        "AND KPI_AS_KND = 'AV' AND KPI_OP_YMD = :ymd AND DEL_FLG = 0";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("ymd", ymd);
                Object raw = q.getSingleResult();
                BigDecimal value = raw instanceof BigDecimal ? (BigDecimal) raw : new BigDecimal(toLong(raw));
                return value.setScale(1, RoundingMode.HALF_UP).doubleValue();
            } catch (RuntimeException ex) {
                if (isObjectNotFound(ex)) {
                    lastError = ex;
                    continue;
                }
                throw ex;
            }
        }
        if (lastError != null) {
            return 0;
        }
        return 0;
    }

    private List<StatusCount> getInboundProductivityBreakdown(String cpny, String whs, String cust, String ymd) {
        RuntimeException lastError = null;
        for (String source : XT_KPI_SOURCES) {
            try {
                String sql = "SELECT KPI_OP_KND, COUNT(*) FROM " + source + " " +
                        "WHERE KPI_CPNY_COD = :cpny AND KPI_WHS_COD = :whs AND KPI_CUST_COD = :cust " +
                        "AND KPI_AS_KND = 'AV' AND KPI_OP_YMD = :ymd AND DEL_FLG = 0 " +
                        "GROUP BY KPI_OP_KND ORDER BY KPI_OP_KND";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);
                q.setParameter("ymd", ymd);

                @SuppressWarnings("unchecked")
                List<Object[]> rows = q.getResultList();
                if (rows == null || rows.isEmpty()) {
                    return Collections.singletonList(new StatusCount("No Data", 0, "#D1D5DB"));
                }

                String[] colors = {"#5BC2E7", "#1A005D", "#8EC400", "#FF9E1B", "#9333EA"};
                List<StatusCount> result = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    Object[] row = rows.get(i);
                    String opKind = row[0] != null ? row[0].toString().trim() : "Unknown";
                    long count = toLong(row[1]);
                    result.add(new StatusCount("Operation " + opKind, count, colors[i % colors.length]));
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
            return Collections.singletonList(new StatusCount("No Data", 0, "#D1D5DB"));
        }
        return Collections.singletonList(new StatusCount("No Data", 0, "#D1D5DB"));
    }

    // ========================================================================
    // KPI Cards  (Today Inbound / Today Outbound / Total SKU / Alerts count)
    // ========================================================================

    private List<KpiCard> getKpiCards(String cpny, String whs, String cust) {
        List<KpiCard> cards = new ArrayList<>();
        try {
            long todayInbound = countTodayInbound(cpny, whs, cust);
            long todayOutbound = countTodayOutbound(cpny, whs, cust);
            long totalSkus = countTotalSkus(cpny, whs, cust);

            cards.add(new KpiCard("Today Inbound", todayInbound, "orders", 0, "pi-download"));
            cards.add(new KpiCard("Today Outbound", todayOutbound, "orders", 0, "pi-upload"));
            cards.add(new KpiCard("Total SKU", totalSkus, "items", 0, "pi-box"));
            // Alert count is derived from alerts list, placeholder here
            cards.add(new KpiCard("Active Alerts", 0, "", 0, "pi-exclamation-triangle"));
        } catch (Exception ex) {
            logger.error("Failed to load KPI cards: {}", ex.getMessage(), ex);
            cards.add(new KpiCard("Today Inbound", 0, "orders", 0, "pi-download"));
            cards.add(new KpiCard("Today Outbound", 0, "orders", 0, "pi-upload"));
            cards.add(new KpiCard("Total SKU", 0, "items", 0, "pi-box"));
            cards.add(new KpiCard("Active Alerts", 0, "", 0, "pi-exclamation-triangle"));
        }
        return cards;
    }

    private long countTodayInbound(String cpny, String whs, String cust) {
        String sql = "SELECT COUNT(*) FROM SGWH0001.VGWH_TJ_AV_H " +
                        "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                     "AND TRUNC(AVH_SCDL_YMD) = TRUNC(SYSDATE) AND DEL_FLG = 0";
        return executeCountQuery(sql, cpny, whs, cust);
    }

    private long countTodayOutbound(String cpny, String whs, String cust) {
        String sql = "SELECT COUNT(*) FROM SGWH0001.VGWH_TJ_SP_H " +
                        "WHERE SPH_CPNY_COD = :cpny AND SPH_WHS_COD = :whs AND SPH_CUST_COD = :cust " +
                     "AND TRUNC(SPH_SCDL_YMD) = TRUNC(SYSDATE) AND DEL_FLG = 0";
        return executeCountQuery(sql, cpny, whs, cust);
    }

    private long countTotalSkus(String cpny, String whs, String cust) {
        String sql = "SELECT COUNT(DISTINCT PROD_COD) FROM SGWH0001.VGWH_TM_PROD " +
                        "WHERE PROD_CPNY_COD = :cpny AND PROD_WHS_COD = :whs AND PROD_CUST_COD = :cust " +
                     "AND DEL_FLG = 0";
        return executeCountQuery(sql, cpny, whs, cust);
    }

    // ========================================================================
    // Inbound status breakdown (from VGWH_TJ_AV_H grouped by AVH_AV_STS)
    // ========================================================================

    private List<StatusCount> getInboundStatusCounts(String cpny, String whs, String cust, Map<String, String> statusLabels) {
        try {
            String sql = "SELECT AVH_AV_STS, COUNT(*) AS CNT FROM SGWH0001.VGWH_TJ_AV_H " +
                             "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                         "AND DEL_FLG = 0 " +
                         "GROUP BY AVH_AV_STS ORDER BY AVH_AV_STS";
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            List<StatusCount> result = new ArrayList<>();
            String[] colors = {"#5BC2E7", "#FF9E1B", "#1A005D", "#8EC400", "#4CAF50", "#EF4444", "#9333EA", "#F59E0B"};

            for (int i = 0; i < rows.size(); i++) {
                Object[] row = rows.get(i);
                String stsCode = row[0] != null ? row[0].toString().trim() : "Unknown";
                long count = toLong(row[1]);
                String label = statusLabels.getOrDefault(stsCode, mapInboundStatus(stsCode));
                String color = colors[i % colors.length];
                result.add(new StatusCount(label, count, color));
            }

            if (result.isEmpty()) {
                result.add(new StatusCount("No Data", 0, "#D1D5DB"));
            }
            return result;
        } catch (Exception ex) {
            logger.error("Failed to load inbound status: {}", ex.getMessage(), ex);
            return Arrays.asList(new StatusCount("No Data", 0, "#D1D5DB"));
        }
    }

    // ========================================================================
    // Outbound status breakdown (from VGWH_TJ_SP_H grouped by SPH_SP_STS)
    // ========================================================================

    private List<StatusCount> getOutboundStatusCounts(String cpny, String whs, String cust, Map<String, String> statusLabels) {
        try {
            String sql = "SELECT SPH_SP_STS, COUNT(*) AS CNT FROM SGWH0001.VGWH_TJ_SP_H " +
                             "WHERE SPH_CPNY_COD = :cpny AND SPH_WHS_COD = :whs AND SPH_CUST_COD = :cust " +
                         "AND DEL_FLG = 0 " +
                         "GROUP BY SPH_SP_STS ORDER BY SPH_SP_STS";
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            List<StatusCount> result = new ArrayList<>();
            String[] colors = {"#5BC2E7", "#1A005D", "#FF9E1B", "#8EC400", "#4CAF50", "#EF4444", "#9333EA", "#F59E0B"};

            for (int i = 0; i < rows.size(); i++) {
                Object[] row = rows.get(i);
                String stsCode = row[0] != null ? row[0].toString().trim() : "Unknown";
                long count = toLong(row[1]);
                String label = statusLabels.getOrDefault(stsCode, mapOutboundStatus(stsCode));
                String color = colors[i % colors.length];
                result.add(new StatusCount(label, count, color));
            }

            if (result.isEmpty()) {
                result.add(new StatusCount("No Data", 0, "#D1D5DB"));
            }
            return result;
        } catch (Exception ex) {
            logger.error("Failed to load outbound status: {}", ex.getMessage(), ex);
            return Arrays.asList(new StatusCount("No Data", 0, "#D1D5DB"));
        }
    }

    // ========================================================================
    // Recent Activities — last 10 inbound/outbound transactions by UPD_YMDHMS
    // ========================================================================

    private List<ActivityItem> getRecentActivities(String cpny, String whs, String cust) {
        try {
            // Union of recent inbound and outbound activity, ordered by update timestamp
            String sql =
                "SELECT * FROM (" +
                "  SELECT 'INBOUND' AS MODULE, " +
                "    'Arrival ' || AVH_AV_NUM || ' - Status: ' || AVH_AV_STS AS DESCRIPTION, " +
                "    NVL(UPD_USER, CRT_USER) AS ACT_USER, " +
                "    UPD_YMDHMS AS ACT_TS " +
                    "  FROM SGWH0001.VGWH_TJ_AV_H " +
                "  WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                "    AND DEL_FLG = 0 " +
                "  UNION ALL " +
                "  SELECT 'OUTBOUND' AS MODULE, " +
                "    'Shipment ' || SPH_SP_NUM || ' - Status: ' || SPH_SP_STS AS DESCRIPTION, " +
                "    NVL(UPD_USER, CRT_USER) AS ACT_USER, " +
                "    UPD_YMDHMS AS ACT_TS " +
                    "  FROM SGWH0001.VGWH_TJ_SP_H " +
                "  WHERE SPH_CPNY_COD = :cpny AND SPH_WHS_COD = :whs AND SPH_CUST_COD = :cust " +
                "    AND DEL_FLG = 0 " +
                ") ORDER BY ACT_TS DESC FETCH FIRST 10 ROWS ONLY";

            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            List<ActivityItem> result = new ArrayList<>();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            for (Object[] row : rows) {
                ActivityItem item = new ActivityItem();
                item.setModule(row[0] != null ? row[0].toString() : "");
                item.setDescription(row[1] != null ? row[1].toString() : "");
                item.setUser(row[2] != null ? row[2].toString() : "System");
                if (row[3] != null) {
                    try {
                        java.sql.Timestamp ts = (java.sql.Timestamp) row[3];
                        item.setTimestamp(ts.toLocalDateTime().format(fmt));
                    } catch (Exception e) {
                        item.setTimestamp(row[3].toString());
                    }
                } else {
                    item.setTimestamp(LocalDateTime.now().format(fmt));
                }
                result.add(item);
            }

            if (result.isEmpty()) {
                ActivityItem empty = new ActivityItem();
                empty.setModule("SYSTEM");
                empty.setDescription("No recent activity found for this tenant");
                empty.setUser("System");
                empty.setTimestamp(LocalDateTime.now().format(fmt));
                result.add(empty);
            }
            return result;
        } catch (Exception ex) {
            logger.error("Failed to load recent activities: {}", ex.getMessage(), ex);
            ActivityItem fallback = new ActivityItem();
            fallback.setModule("SYSTEM");
            fallback.setDescription("Dashboard connected to database");
            fallback.setUser("System");
            fallback.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            return Arrays.asList(fallback);
        }
    }

    // ========================================================================
    // Warehouse Utilization — total locations vs locations holding stock
    // ========================================================================

    private WarehouseUtilization getWarehouseUtil(String cpny, String whs, String cust) {
        WarehouseUtilization util = new WarehouseUtilization();
        try {
            // Total locations (location master is company+warehouse level, no customer key)
            String sqlTotal = "SELECT COUNT(*) FROM SGWH0001.VGWH_TM_LOC " +
                                  "WHERE LOC_CPNY_COD = :cpny AND LOC_WHS_COD = :whs AND DEL_FLG = 0";
            Query qTotal = em.createNativeQuery(sqlTotal);
            qTotal.setParameter("cpny", cpny);
            qTotal.setParameter("whs", whs);
            long total = toLong(qTotal.getSingleResult());

            // Occupied stock rows (schema-safe fallback across tenants/views)
            String sqlUsed = "SELECT COUNT(*) FROM SGWH0001.VGWH_TJ_ST " +
                                 "WHERE ST_CPNY_COD = :cpny AND ST_WHS_COD = :whs AND ST_CUST_COD = :cust " +
                             "AND DEL_FLG = 0";
            Query qUsed = em.createNativeQuery(sqlUsed);
            qUsed.setParameter("cpny", cpny);
            qUsed.setParameter("whs", whs);
            qUsed.setParameter("cust", cust);
            long used = toLong(qUsed.getSingleResult());

            util.setTotalLocations(total);
            util.setUsedLocations(used);
            util.setUtilizationPercent(total > 0 ? Math.round((double) used / total * 1000.0) / 10.0 : 0);
        } catch (Exception ex) {
            logger.error("Failed to load warehouse utilization: {}", ex.getMessage(), ex);
            util.setTotalLocations(0);
            util.setUsedLocations(0);
            util.setUtilizationPercent(0);
        }
        return util;
    }

    // ========================================================================
    // Alerts — overdue shipments, pending inbound, low-stock indicators
    // ========================================================================

    private List<AlertItem> getAlerts(String cpny, String whs, String cust) {
        List<AlertItem> alerts = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String now = LocalDateTime.now().format(fmt);

        try {
            // 1. Overdue outbound shipments (scheduled date < today, not yet shipped/cancelled)
                String sqlOverdue = "SELECT COUNT(*) FROM SGWH0001.VGWH_TJ_SP_H " +
                    "WHERE SPH_CPNY_COD = :cpny AND SPH_WHS_COD = :whs AND SPH_CUST_COD = :cust " +
                    "AND TRUNC(SPH_SCDL_YMD) < TRUNC(SYSDATE) " +
                    "AND SPH_SP_STS NOT IN ('609', '999') " +
                    "AND DEL_FLG = 0";
            long overdueCount = executeCountQuery(sqlOverdue, cpny, whs, cust);
            if (overdueCount > 0) {
                AlertItem a = new AlertItem();
                a.setSeverity("ERROR");
                a.setMessage(overdueCount + " shipment(s) are past their scheduled delivery date");
                a.setModule("Outbound");
                a.setTimestamp(now);
                alerts.add(a);
            }

            // 2. Pending inbound (arrivals with early status codes — not yet confirmed)
                String sqlPending = "SELECT COUNT(*) FROM SGWH0001.VGWH_TJ_AV_H " +
                    "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
                    "AND AVH_AV_STS < '209' " +
                    "AND DEL_FLG = 0";
            long pendingInbound = executeCountQuery(sqlPending, cpny, whs, cust);
            if (pendingInbound > 0) {
                AlertItem a = new AlertItem();
                a.setSeverity("WARN");
                a.setMessage(pendingInbound + " inbound order(s) pending confirmation");
                a.setModule("Inbound");
                a.setTimestamp(now);
                alerts.add(a);
            }

            // 3. Low stock disabled for schema-variant environments where stock quantity/status columns differ.

            if (alerts.isEmpty()) {
                AlertItem ok = new AlertItem();
                ok.setSeverity("INFO");
                ok.setMessage("All operations running smoothly — no issues detected");
                ok.setModule("System");
                ok.setTimestamp(now);
                alerts.add(ok);
            }
        } catch (Exception ex) {
            logger.error("Failed to load alerts: {}", ex.getMessage(), ex);
            AlertItem err = new AlertItem();
            err.setSeverity("INFO");
            err.setMessage("Dashboard connected to database");
            err.setModule("System");
            err.setTimestamp(now);
            alerts.add(err);
        }
        return alerts;
    }

    // ========================================================================
    // Utility methods
    // ========================================================================

    private long executeCountQuery(String sql, String cpny, String whs, String cust) {
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        if (sql.contains(":cust")) {
            q.setParameter("cust", cust);
        }
        return toLong(q.getSingleResult());
    }

    private long toLong(Object val) {
        if (val == null) return 0L;
        if (val instanceof BigDecimal) return ((BigDecimal) val).longValue();
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.parseLong(val.toString()); } catch (Exception e) { return 0L; }
    }

    private Map<String, String> loadStatusLabels(String cpny, String whs, String cust, boolean inbound) {
        Map<String, LabelCandidate> bestByCode = new HashMap<>();
        try {
            String sql = "SELECT STS_CPNY_COD, STS_WHS_COD, STS_CUST_COD, STS_COD, STS_BSNS_COD, STS_LBL_COD, STS_RMKS " +
                    "FROM SGWH0001.GWH_TM_STS " +
                    "WHERE STS_CPNY_COD IN (:cpny, '000000000000', '*') " +
                    "AND STS_WHS_COD IN (:whs, '00000000', '*') " +
                    "AND STS_CUST_COD IN (:cust, '00000000000000000000000000', '*') " +
                    "AND DEL_FLG = '0'";

            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);

            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            for (Object[] row : rows) {
                String rowCpny = row[0] != null ? row[0].toString().trim() : "";
                String rowWhs = row[1] != null ? row[1].toString().trim() : "";
                String rowCust = row[2] != null ? row[2].toString().trim() : "";
                String code = row[3] != null ? row[3].toString().trim() : "";
                String bsns = row[4] != null ? row[4].toString().trim() : "";
                String lblCode = row[5] != null ? row[5].toString().trim() : "";
                String remarks = row[6] != null ? row[6].toString().trim() : "";

                if (code.isEmpty()) {
                    continue;
                }

                String display = !remarks.isEmpty() ? remarks : lblCode;
                if (display.isEmpty()) {
                    continue;
                }

                int score = moduleScore(bsns, inbound) + specificityScore(cpny, whs, cust, rowCpny, rowWhs, rowCust);
                LabelCandidate current = bestByCode.get(code);
                if (current == null || score > current.score) {
                    bestByCode.put(code, new LabelCandidate(display, score));
                }
            }
        } catch (Exception ex) {
            logger.warn("Failed to load status labels from GWH_TM_STS: {}", ex.getMessage());
        }

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, LabelCandidate> entry : bestByCode.entrySet()) {
            result.put(entry.getKey(), entry.getValue().label);
        }
        return result;
    }

    private int moduleScore(String bsns, boolean inbound) {
        if (bsns == null || bsns.isEmpty()) {
            return 1;
        }
        String upper = bsns.toUpperCase();
        if (inbound) {
            if (upper.contains("AV") || upper.contains("IN")) {
                return 3;
            }
        } else {
            if (upper.contains("SP") || upper.contains("OUT")) {
                return 3;
            }
        }
        return 2;
    }

    private int specificityScore(String cpny, String whs, String cust, String rowCpny, String rowWhs, String rowCust) {
        int score = 0;
        if (cpny != null && cpny.equals(rowCpny)) {
            score += 6;
        }
        if (whs != null && whs.equals(rowWhs)) {
            score += 4;
        }
        if (cust != null && cust.equals(rowCust)) {
            score += 2;
        }
        return score;
    }

    private static class LabelCandidate {
        private final String label;
        private final int score;

        private LabelCandidate(String label, int score) {
            this.label = label;
            this.score = score;
        }
    }

    /**
     * Map inbound numeric status codes to display labels.
     * Based on GWH convention: 100=Created, 200=Located, 205=Inspected, 209=Confirmed, 999=Cancelled
     */
    private String mapInboundStatus(String code) {
        switch (code) {
            case "100": return "Created";
            case "200": return "Located";
            case "205": return "Inspected";
            case "209": return "Confirmed";
            case "300": return "Receiving";
            case "500": return "Putaway";
            case "605": return "Putaway Done";
            case "609": return "Stored";
            case "700": return "Closed";
            case "809": return "Completed";
            case "999": return "Cancelled";
            default: return "Status " + code;
        }
    }

    /**
     * Map outbound numeric status codes to display labels.
     * Based on GWH convention: 100=New, 300=Allocated, 500=Picking, 605=Pick Complete, 609=Packed, 700=Shipped, 999=Cancelled
     */
    private String mapOutboundStatus(String code) {
        switch (code) {
            case "100": return "New";
            case "200": return "Processing";
            case "209": return "Ready";
            case "300": return "Allocated";
            case "500": return "Picking";
            case "605": return "Pick Complete";
            case "609": return "Packed";
            case "700": return "Shipped";
            case "809": return "Completed";
            case "999": return "Cancelled";
            default: return "Status " + code;
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
}
