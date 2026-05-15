package jp.co.nittsu.gwh.module.inbound.dto;

import java.time.LocalDate;

public class ArrivalConfirmationSearchRequest {

    private String arrivalNumberFrom;
    private String arrivalNumberTo;
    private String arrivalStatus;
    private String transactionKind;
    private String supplier;
    private String poNumber;
    private String referenceNumber;
    private LocalDate scheduledDateFrom;
    private LocalDate scheduledDateTo;
    private int page = 0;
    private int size = 20;

    public String getArrivalNumberFrom() {
        return arrivalNumberFrom;
    }

    public void setArrivalNumberFrom(String arrivalNumberFrom) {
        this.arrivalNumberFrom = arrivalNumberFrom;
    }

    public String getArrivalNumberTo() {
        return arrivalNumberTo;
    }

    public void setArrivalNumberTo(String arrivalNumberTo) {
        this.arrivalNumberTo = arrivalNumberTo;
    }

    public String getArrivalStatus() {
        return arrivalStatus;
    }

    public void setArrivalStatus(String arrivalStatus) {
        this.arrivalStatus = arrivalStatus;
    }

    public String getTransactionKind() {
        return transactionKind;
    }

    public void setTransactionKind(String transactionKind) {
        this.transactionKind = transactionKind;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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

    public LocalDate getScheduledDateFrom() {
        return scheduledDateFrom;
    }

    public void setScheduledDateFrom(LocalDate scheduledDateFrom) {
        this.scheduledDateFrom = scheduledDateFrom;
    }

    public LocalDate getScheduledDateTo() {
        return scheduledDateTo;
    }

    public void setScheduledDateTo(LocalDate scheduledDateTo) {
        this.scheduledDateTo = scheduledDateTo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
