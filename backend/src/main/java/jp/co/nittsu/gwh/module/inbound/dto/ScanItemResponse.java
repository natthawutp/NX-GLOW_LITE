package jp.co.nittsu.gwh.module.inbound.dto;

/**
 * Response returned after a successful item scan on the receiving screen.
 * Contains the updated running totals for the matched line and the arrival header.
 */
public class ScanItemResponse {

    private String arrivalNumber;
    private int lineNumber;
    private String productCode;
    private String productName;

    /** Updated running total received for this line (after this scan) */
    private int resultTotalQty;
    /** Original planned total for this line */
    private int planTotalQty;
    /** resultTotalQty - planTotalQty (positive = surplus, negative = shortage) */
    private int variance;

    /** LPN this item was assigned to (null if none) */
    private String lpnNumber;

    /** Updated AVH header status */
    private String headerStatus;

    /** Receive status for this line: "OK", "SHORT", or "OVER" */
    private String receiveStatus;
    /** Human-readable warning when SHORT or OVER; null when OK */
    private String receiveWarning;

    /** Updated arrival-wide totals */
    private int totalPlannedQty;
    private int totalReceivedQty;

    /** Full refreshed arrival data (header + all lines) for frontend sync */
    private ReceiveArrivalDto arrival;

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int v) { this.lineNumber = v; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String v) { this.productCode = v; }
    public String getProductName() { return productName; }
    public void setProductName(String v) { this.productName = v; }
    public int getResultTotalQty() { return resultTotalQty; }
    public void setResultTotalQty(int v) { this.resultTotalQty = v; }
    public int getPlanTotalQty() { return planTotalQty; }
    public void setPlanTotalQty(int v) { this.planTotalQty = v; }
    public int getVariance() { return variance; }
    public void setVariance(int v) { this.variance = v; }
    public String getLpnNumber() { return lpnNumber; }
    public void setLpnNumber(String v) { this.lpnNumber = v; }
    public String getHeaderStatus() { return headerStatus; }
    public void setHeaderStatus(String v) { this.headerStatus = v; }
    public String getReceiveStatus() { return receiveStatus; }
    public void setReceiveStatus(String v) { this.receiveStatus = v; }
    public String getReceiveWarning() { return receiveWarning; }
    public void setReceiveWarning(String v) { this.receiveWarning = v; }
    public int getTotalPlannedQty() { return totalPlannedQty; }
    public void setTotalPlannedQty(int v) { this.totalPlannedQty = v; }
    public int getTotalReceivedQty() { return totalReceivedQty; }
    public void setTotalReceivedQty(int v) { this.totalReceivedQty = v; }
    public ReceiveArrivalDto getArrival() { return arrival; }
    public void setArrival(ReceiveArrivalDto v) { this.arrival = v; }
}
