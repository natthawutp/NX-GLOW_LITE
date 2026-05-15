package jp.co.nittsu.gwh.module.inbound.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request body for registering arrival inspection results.
 *
 * Supports two modes (equivalent to legacy apins process codes):
 *  - "NEW"      (apinsReg)  — first-time inspection registration
 *  - "CHANGE"   (apinsChg)  — modify existing results
 *  - "LOCATION" (apinsLoc)  — location assignment only
 *
 * Each line may optionally carry an lpnNumber / lpnType to link the
 * inspected goods to an LPN (new or existing).
 * A single collective lpnNumber / lpnType can also be supplied at
 * the header level to group all lines into one LPN.
 */
public class InspectionRegisterRequest {

    @NotBlank
    private String arrivalNumber;

    /** "NEW" | "CHANGE" | "LOCATION" */
    private String processMode = "NEW";

    /** Header-level LPN (optional) — applies to all lines if set */
    private String collectiveLpnNumber;
    private String collectiveLpnType;

    /** Optimistic-lock timestamp from the search screen */
    private String updTimestamp;

    @NotEmpty
    private List<InspectionLineRequest> lines;

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public String getProcessMode() { return processMode; }
    public void setProcessMode(String v) { this.processMode = v; }
    public String getCollectiveLpnNumber() { return collectiveLpnNumber; }
    public void setCollectiveLpnNumber(String v) { this.collectiveLpnNumber = v; }
    public String getCollectiveLpnType() { return collectiveLpnType; }
    public void setCollectiveLpnType(String v) { this.collectiveLpnType = v; }
    public String getUpdTimestamp() { return updTimestamp; }
    public void setUpdTimestamp(String v) { this.updTimestamp = v; }
    public List<InspectionLineRequest> getLines() { return lines; }
    public void setLines(List<InspectionLineRequest> v) { this.lines = v; }

    /** Per-line inspection data */
    public static class InspectionLineRequest {
        private Integer lineNumber;
        private Integer resultCsQty;
        private Integer resultPcQty;
        private Integer resultBlQty;
        private String areaCode;
        private String rackCode;
        private String levelCode;
        private String positionCode;
        /** LPN for this specific line (overrides collective LPN if set) */
        private String lpnNumber;
        private String lpnType;
        private String lotNumber;
        private String updTimestamp;

        public Integer getLineNumber() { return lineNumber; }
        public void setLineNumber(Integer v) { this.lineNumber = v; }
        public Integer getResultCsQty() { return resultCsQty; }
        public void setResultCsQty(Integer v) { this.resultCsQty = v; }
        public Integer getResultPcQty() { return resultPcQty; }
        public void setResultPcQty(Integer v) { this.resultPcQty = v; }
        public Integer getResultBlQty() { return resultBlQty; }
        public void setResultBlQty(Integer v) { this.resultBlQty = v; }
        public String getAreaCode() { return areaCode; }
        public void setAreaCode(String v) { this.areaCode = v; }
        public String getRackCode() { return rackCode; }
        public void setRackCode(String v) { this.rackCode = v; }
        public String getLevelCode() { return levelCode; }
        public void setLevelCode(String v) { this.levelCode = v; }
        public String getPositionCode() { return positionCode; }
        public void setPositionCode(String v) { this.positionCode = v; }
        public String getLpnNumber() { return lpnNumber; }
        public void setLpnNumber(String v) { this.lpnNumber = v; }
        public String getLpnType() { return lpnType; }
        public void setLpnType(String v) { this.lpnType = v; }
        public String getLotNumber() { return lotNumber; }
        public void setLotNumber(String v) { this.lotNumber = v; }
        public String getUpdTimestamp() { return updTimestamp; }
        public void setUpdTimestamp(String v) { this.updTimestamp = v; }
    }
}
