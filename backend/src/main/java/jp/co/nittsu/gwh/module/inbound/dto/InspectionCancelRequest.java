package jp.co.nittsu.gwh.module.inbound.dto;

import javax.validation.constraints.NotBlank;

/**
 * Request body for cancelling an arrival inspection.
 * Equivalent to legacy apins Screen 040.
 */
public class InspectionCancelRequest {

    @NotBlank
    private String arrivalNumber;

    private String updTimestamp;

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public String getUpdTimestamp() { return updTimestamp; }
    public void setUpdTimestamp(String v) { this.updTimestamp = v; }
}
