package jp.co.nittsu.gwh.module.dashboard.dto;

import java.util.List;

/**
 * Date-range inbound productivity trend payload for dashboard analysis.
 */
public class DashboardProductivityTrendDto {

    private String dateFrom;
    private String dateTo;
    private List<TrendPoint> trendPoints;

    public static class TrendPoint {
        private String date;
        private long milestones;
        private long productivityEvents;
        private long processedLines;

        public TrendPoint() {}

        public TrendPoint(String date, long milestones, long productivityEvents, long processedLines) {
            this.date = date;
            this.milestones = milestones;
            this.productivityEvents = productivityEvents;
            this.processedLines = processedLines;
        }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public long getMilestones() { return milestones; }
        public void setMilestones(long milestones) { this.milestones = milestones; }
        public long getProductivityEvents() { return productivityEvents; }
        public void setProductivityEvents(long productivityEvents) { this.productivityEvents = productivityEvents; }
        public long getProcessedLines() { return processedLines; }
        public void setProcessedLines(long processedLines) { this.processedLines = processedLines; }
    }

    public String getDateFrom() { return dateFrom; }
    public void setDateFrom(String dateFrom) { this.dateFrom = dateFrom; }
    public String getDateTo() { return dateTo; }
    public void setDateTo(String dateTo) { this.dateTo = dateTo; }
    public List<TrendPoint> getTrendPoints() { return trendPoints; }
    public void setTrendPoints(List<TrendPoint> trendPoints) { this.trendPoints = trendPoints; }
}
