package jp.co.nittsu.gwh.module.inbound.dto;

import java.util.List;

public class InspectionRegisterResponse {

    private String arrivalNumber;
    private String arrivalStatus;
    private int registeredLines;
    /** LPN numbers created or updated during this inspection */
    private List<String> lpnNumbers;

    public InspectionRegisterResponse() {}

    public InspectionRegisterResponse(String arrivalNumber, String arrivalStatus,
                                      int registeredLines, List<String> lpnNumbers) {
        this.arrivalNumber = arrivalNumber;
        this.arrivalStatus = arrivalStatus;
        this.registeredLines = registeredLines;
        this.lpnNumbers = lpnNumbers;
    }

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public String getArrivalStatus() { return arrivalStatus; }
    public void setArrivalStatus(String v) { this.arrivalStatus = v; }
    public int getRegisteredLines() { return registeredLines; }
    public void setRegisteredLines(int v) { this.registeredLines = v; }
    public List<String> getLpnNumbers() { return lpnNumbers; }
    public void setLpnNumbers(List<String> v) { this.lpnNumbers = v; }
}
