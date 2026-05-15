package jp.co.nittsu.gwh.module.inbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Inbound order row DTO for /inbound/orders list API.
 */
public class InboundOrderDto {

    @JsonProperty("AVH_AV_NUM")
    private String arrivalNumber;

    @JsonProperty("AVH_AV_STS")
    private String arrivalStatus;

    @JsonProperty("AVH_AV_STS_LBL")
    private String arrivalStatusLabel;

    @JsonProperty("AVH_SUPLR_COD")
    private String supplierCode;

    private String supplierName;

    @JsonProperty("AVH_PO_NUM")
    private String poNumber;

    @JsonProperty("AVH_SCDL_YMD")
    private LocalDate scheduledDate;

    @JsonProperty("AVH_AV_YMD")
    private LocalDate actualDate;

    @JsonProperty("AVH_TTL_QTY")
    private Integer totalQuantity;

    @JsonProperty("AVH_TRN_KND")
    private String transactionKind;

    @JsonProperty("AVH_RMK")
    private String remarks;

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getArrivalStatus() { return arrivalStatus; }
    public void setArrivalStatus(String arrivalStatus) { this.arrivalStatus = arrivalStatus; }
    public String getArrivalStatusLabel() { return arrivalStatusLabel; }
    public void setArrivalStatusLabel(String arrivalStatusLabel) { this.arrivalStatusLabel = arrivalStatusLabel; }
    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public LocalDate getActualDate() { return actualDate; }
    public void setActualDate(LocalDate actualDate) { this.actualDate = actualDate; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public String getTransactionKind() { return transactionKind; }
    public void setTransactionKind(String transactionKind) { this.transactionKind = transactionKind; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
