package jp.co.nittsu.gwh.module.lpn.dto;

public class LpnPutawayRequest {

    private String lpnNumber;
    private String targetLocationCode;
    private String targetAreaCode;
    private String targetRackCode;
    private String targetPositionCode;
    private String targetLevelCode;

    // --- Getters & Setters ---
    public String getLpnNumber() { return lpnNumber; }
    public void setLpnNumber(String lpnNumber) { this.lpnNumber = lpnNumber; }
    public String getTargetLocationCode() { return targetLocationCode; }
    public void setTargetLocationCode(String targetLocationCode) { this.targetLocationCode = targetLocationCode; }
    public String getTargetAreaCode() { return targetAreaCode; }
    public void setTargetAreaCode(String targetAreaCode) { this.targetAreaCode = targetAreaCode; }
    public String getTargetRackCode() { return targetRackCode; }
    public void setTargetRackCode(String targetRackCode) { this.targetRackCode = targetRackCode; }
    public String getTargetPositionCode() { return targetPositionCode; }
    public void setTargetPositionCode(String targetPositionCode) { this.targetPositionCode = targetPositionCode; }
    public String getTargetLevelCode() { return targetLevelCode; }
    public void setTargetLevelCode(String targetLevelCode) { this.targetLevelCode = targetLevelCode; }
}
