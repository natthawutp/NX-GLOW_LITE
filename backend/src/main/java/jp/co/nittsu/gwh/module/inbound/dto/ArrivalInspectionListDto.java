package jp.co.nittsu.gwh.module.inbound.dto;

import java.time.LocalDate;

/**
 * Arrival header summary returned by the inspection list screen.
 * Corresponds to legacy apins Screen 010 body row.
 */
public class ArrivalInspectionListDto {

    private String arrivalNumber;
    private String arrivalStatus;
    private String arrivalStatusLabel;
    private LocalDate scheduledDate;
    private LocalDate arrivalDate;
    private String supplierCode;
    private String supplierName;
    private String poNumber;
    private String transactionKind;
    private Integer totalLines;
    private Integer inspectedLines;
    private String remarks;
    /** Optimistic-lock timestamp (UPD_YMDHMS) for concurrent-update protection */
    private String updTimestamp;

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
    public Integer getTotalLines() { return totalLines; }
    public void setTotalLines(Integer v) { this.totalLines = v; }
    public Integer getInspectedLines() { return inspectedLines; }
    public void setInspectedLines(Integer v) { this.inspectedLines = v; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String v) { this.remarks = v; }
    public String getUpdTimestamp() { return updTimestamp; }
    public void setUpdTimestamp(String v) { this.updTimestamp = v; }
}
