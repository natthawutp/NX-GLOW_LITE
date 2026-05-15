package jp.co.nittsu.gwh.module.dashboard.dto;

import java.util.List;

/**
 * Dashboard summary response DTO.
 * Structure matches Angular frontend DashboardSummary interface exactly.
 */
public class DashboardSummaryDto {

    private List<KpiCard> kpiCards;
    private List<StatusCount> inboundByStatus;
    private List<StatusCount> outboundByStatus;
    private List<ActivityItem> recentActivities;
    private WarehouseUtilization warehouseUtilization;
    private ProductivitySummary inboundProductivity;
    private List<AlertItem> alerts;

    // --- Inner DTOs matching frontend interfaces ---

    public static class KpiCard {
        private String title;
        private long value;
        private String unit;
        private double trend;
        private String icon;

        public KpiCard() {}
        public KpiCard(String title, long value, String unit, double trend, String icon) {
            this.title = title;
            this.value = value;
            this.unit = unit;
            this.trend = trend;
            this.icon = icon;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public long getValue() { return value; }
        public void setValue(long value) { this.value = value; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public double getTrend() { return trend; }
        public void setTrend(double trend) { this.trend = trend; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }

    public static class StatusCount {
        private String status;
        private long count;
        private String color;

        public StatusCount() {}
        public StatusCount(String status, long count, String color) {
            this.status = status;
            this.count = count;
            this.color = color;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }

    public static class ActivityItem {
        private String description;
        private String user;
        private String module;     // INBOUND, OUTBOUND, INVENTORY, SYSTEM
        private String timestamp;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    public static class WarehouseUtilization {
        private long totalLocations;
        private long usedLocations;
        private double utilizationPercent;

        public long getTotalLocations() { return totalLocations; }
        public void setTotalLocations(long totalLocations) { this.totalLocations = totalLocations; }
        public long getUsedLocations() { return usedLocations; }
        public void setUsedLocations(long usedLocations) { this.usedLocations = usedLocations; }
        public double getUtilizationPercent() { return utilizationPercent; }
        public void setUtilizationPercent(double utilizationPercent) { this.utilizationPercent = utilizationPercent; }
    }

    public static class ProductivitySummary {
        private long milestoneCapturedToday;
        private long productivityCapturedToday;
        private double averageLinesPerEvent;
        private List<StatusCount> operationBreakdown;

        public long getMilestoneCapturedToday() { return milestoneCapturedToday; }
        public void setMilestoneCapturedToday(long milestoneCapturedToday) { this.milestoneCapturedToday = milestoneCapturedToday; }
        public long getProductivityCapturedToday() { return productivityCapturedToday; }
        public void setProductivityCapturedToday(long productivityCapturedToday) { this.productivityCapturedToday = productivityCapturedToday; }
        public double getAverageLinesPerEvent() { return averageLinesPerEvent; }
        public void setAverageLinesPerEvent(double averageLinesPerEvent) { this.averageLinesPerEvent = averageLinesPerEvent; }
        public List<StatusCount> getOperationBreakdown() { return operationBreakdown; }
        public void setOperationBreakdown(List<StatusCount> operationBreakdown) { this.operationBreakdown = operationBreakdown; }
    }

    public static class AlertItem {
        private String severity;   // ERROR, WARN, INFO
        private String message;
        private String module;
        private String timestamp;

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    // --- Root Getters & Setters ---
    public List<KpiCard> getKpiCards() { return kpiCards; }
    public void setKpiCards(List<KpiCard> kpiCards) { this.kpiCards = kpiCards; }
    public List<StatusCount> getInboundByStatus() { return inboundByStatus; }
    public void setInboundByStatus(List<StatusCount> inboundByStatus) { this.inboundByStatus = inboundByStatus; }
    public List<StatusCount> getOutboundByStatus() { return outboundByStatus; }
    public void setOutboundByStatus(List<StatusCount> outboundByStatus) { this.outboundByStatus = outboundByStatus; }
    public List<ActivityItem> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<ActivityItem> recentActivities) { this.recentActivities = recentActivities; }
    public WarehouseUtilization getWarehouseUtilization() { return warehouseUtilization; }
    public void setWarehouseUtilization(WarehouseUtilization warehouseUtilization) { this.warehouseUtilization = warehouseUtilization; }
    public ProductivitySummary getInboundProductivity() { return inboundProductivity; }
    public void setInboundProductivity(ProductivitySummary inboundProductivity) { this.inboundProductivity = inboundProductivity; }
    public List<AlertItem> getAlerts() { return alerts; }
    public void setAlerts(List<AlertItem> alerts) { this.alerts = alerts; }
}
