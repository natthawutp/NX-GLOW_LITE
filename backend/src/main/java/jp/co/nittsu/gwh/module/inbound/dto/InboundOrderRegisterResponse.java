package jp.co.nittsu.gwh.module.inbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InboundOrderRegisterResponse {

    @JsonProperty("AVH_AV_NUM")
    private String arrivalNumber;

    @JsonProperty("AVH_AV_STS")
    private String arrivalStatus;

    @JsonProperty("SAVED_LINES")
    private Integer savedLines;

    @JsonProperty("MILESTONE_CAPTURED")
    private Integer milestoneCaptured;

    @JsonProperty("PRODUCTIVITY_CAPTURED")
    private Integer productivityCaptured;

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

    public Integer getSavedLines() {
        return savedLines;
    }

    public void setSavedLines(Integer savedLines) {
        this.savedLines = savedLines;
    }

    public Integer getMilestoneCaptured() {
        return milestoneCaptured;
    }

    public void setMilestoneCaptured(Integer milestoneCaptured) {
        this.milestoneCaptured = milestoneCaptured;
    }

    public Integer getProductivityCaptured() {
        return productivityCaptured;
    }

    public void setProductivityCaptured(Integer productivityCaptured) {
        this.productivityCaptured = productivityCaptured;
    }
}