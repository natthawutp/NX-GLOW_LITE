package jp.co.nittsu.gwh.module.inbound.dto;

import java.time.LocalDate;

public class ArrivalConfirmationDto {

    private String arrivalNumber;
    private String arrivalStatus;
    private String supplierCode;
    private String supplierName;
    private String poNumber;
    private String referenceNumber;
    private LocalDate scheduledDate;
    private LocalDate arrivalDate;
    private String partialDeliveryFlag;
    private String remarks;

    public String getArrivalNumber() {
        return arrivalNumber;
    }

    public void setArrivalNumber(String arrivalNumber) {
        this.arrivalNumber = arrivalNumber;
    }

    public String getArrivalStatus() {
        return arrivalStatus;
    }

    public void setArrivalStatus(String arrivalStatus) {
        this.arrivalStatus = arrivalStatus;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getPartialDeliveryFlag() {
        return partialDeliveryFlag;
    }

    public void setPartialDeliveryFlag(String partialDeliveryFlag) {
        this.partialDeliveryFlag = partialDeliveryFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
