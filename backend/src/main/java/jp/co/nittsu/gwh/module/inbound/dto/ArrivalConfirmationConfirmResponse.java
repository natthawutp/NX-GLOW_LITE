package jp.co.nittsu.gwh.module.inbound.dto;

import java.util.ArrayList;
import java.util.List;

public class ArrivalConfirmationConfirmResponse {

    private int requestedCount;
    private int confirmedCount;
    private String appliedStatus;
    private List<String> notFoundArrivalNumbers = new ArrayList<>();

    public int getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(int requestedCount) {
        this.requestedCount = requestedCount;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(int confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public String getAppliedStatus() {
        return appliedStatus;
    }

    public void setAppliedStatus(String appliedStatus) {
        this.appliedStatus = appliedStatus;
    }

    public List<String> getNotFoundArrivalNumbers() {
        return notFoundArrivalNumbers;
    }

    public void setNotFoundArrivalNumbers(List<String> notFoundArrivalNumbers) {
        this.notFoundArrivalNumbers = notFoundArrivalNumbers;
    }
}
