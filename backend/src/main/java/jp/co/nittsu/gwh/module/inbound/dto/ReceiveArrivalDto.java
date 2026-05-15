package jp.co.nittsu.gwh.module.inbound.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Combined arrival header + detail lines returned when a user scans an arrival number
 * on the receiving screen.
 */
public class ReceiveArrivalDto {

    private String arrivalNumber;
    private String arrivalStatus;
    private String arrivalStatusLabel;
    private LocalDate scheduledDate;
    private LocalDate arrivalDate;
    private String supplierCode;
    private String supplierName;
    private String poNumber;
    private String transactionKind;
    private String remarks;

    /** Sum of all AVD planned total quantities */
    private int totalPlannedQty;
    /** Sum of all AVR result total quantities already received */
    private int totalReceivedQty;

    /** Detail lines — reuses InspectionLineDto (planQty, resultQty, location, lpnNumber) */
    private List<InspectionLineDto> lines;

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public String getArrivalStatus() { return arrivalStatus; }
    public void setArrivalStatus(String v) { this.arrivalStatus = v; }
    public String getArrivalStatusLabel() { return arrivalStatusLabel; }
    public void setArrivalStatusLabel(String v) { this.arrivalStatusLabel = v; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate v) { this.scheduledDate = v; }
    public LocalDate getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(LocalDate v) { this.arrivalDate = v; }
    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String v) { this.supplierCode = v; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String v) { this.supplierName = v; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String v) { this.poNumber = v; }
    public String getTransactionKind() { return transactionKind; }
    public void setTransactionKind(String v) { this.transactionKind = v; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String v) { this.remarks = v; }
    public int getTotalPlannedQty() { return totalPlannedQty; }
    public void setTotalPlannedQty(int v) { this.totalPlannedQty = v; }
    public int getTotalReceivedQty() { return totalReceivedQty; }
    public void setTotalReceivedQty(int v) { this.totalReceivedQty = v; }
    public List<InspectionLineDto> getLines() { return lines; }
    public void setLines(List<InspectionLineDto> v) { this.lines = v; }
}
