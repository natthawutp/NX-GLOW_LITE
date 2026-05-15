package jp.co.nittsu.gwh.module.inbound.dto;

import java.math.BigDecimal;

/**
 * One arrival detail line shown on the inspection detail screen.
 * Corresponds to legacy apins Screen 020 body row (GwhApins020BdyDTO).
 */
public class InspectionLineDto {

    private Integer lineNumber;
    private String lineStatus;
    private String productCode;
    private String productName;
    private String subInventoryCode;
    private String poNumber;
    private String lotNumber;

    // Planned quantities
    private Integer planCsQty;
    private Integer planPcQty;
    private Integer planBlQty;
    private Integer planTotalQty;

    // Actual (result) quantities — filled in by user
    private Integer resultCsQty;
    private Integer resultPcQty;
    private Integer resultBlQty;
    private Integer resultTotalQty;

    // Pieces per unit for calculation
    private Integer piecesPerCase;
    private Integer piecesPerBulk;

    // Recommended storage location
    private String areaCode;
    private String rackCode;
    private String levelCode;
    private String positionCode;

    // LPN assignment (optional — set by user during inspection)
    private String lpnNumber;
    private String lpnType;

    private BigDecimal lineWeight;
    private BigDecimal lineVolume;

    // Legacy columns needed for AVR insert (from AVD/AVH)
    private String originCode;
    private String trnKnd;
    private String pik1;
    private String pik2;
    private String pik3;
    private String pik4;
    private String pik5;
    private String pik6;
    private String pik7;
    private String dmgFlg;

    /** Whether this line has multiple cargo IDs (AVD_CGID_NUM > 1) */
    private boolean multiCargo;

    private String updTimestamp;

    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer v) { this.lineNumber = v; }
    public String getLineStatus() { return lineStatus; }
    public void setLineStatus(String v) { this.lineStatus = v; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String v) { this.productCode = v; }
    public String getProductName() { return productName; }
    public void setProductName(String v) { this.productName = v; }
    public String getSubInventoryCode() { return subInventoryCode; }
    public void setSubInventoryCode(String v) { this.subInventoryCode = v; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String v) { this.poNumber = v; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String v) { this.lotNumber = v; }
    public Integer getPlanCsQty() { return planCsQty; }
    public void setPlanCsQty(Integer v) { this.planCsQty = v; }
    public Integer getPlanPcQty() { return planPcQty; }
    public void setPlanPcQty(Integer v) { this.planPcQty = v; }
    public Integer getPlanBlQty() { return planBlQty; }
    public void setPlanBlQty(Integer v) { this.planBlQty = v; }
    public Integer getPlanTotalQty() { return planTotalQty; }
    public void setPlanTotalQty(Integer v) { this.planTotalQty = v; }
    public Integer getResultCsQty() { return resultCsQty; }
    public void setResultCsQty(Integer v) { this.resultCsQty = v; }
    public Integer getResultPcQty() { return resultPcQty; }
    public void setResultPcQty(Integer v) { this.resultPcQty = v; }
    public Integer getResultBlQty() { return resultBlQty; }
    public void setResultBlQty(Integer v) { this.resultBlQty = v; }
    public Integer getResultTotalQty() { return resultTotalQty; }
    public void setResultTotalQty(Integer v) { this.resultTotalQty = v; }
    public Integer getPiecesPerCase() { return piecesPerCase; }
    public void setPiecesPerCase(Integer v) { this.piecesPerCase = v; }
    public Integer getPiecesPerBulk() { return piecesPerBulk; }
    public void setPiecesPerBulk(Integer v) { this.piecesPerBulk = v; }
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
    public BigDecimal getLineWeight() { return lineWeight; }
    public void setLineWeight(BigDecimal v) { this.lineWeight = v; }
    public BigDecimal getLineVolume() { return lineVolume; }
    public void setLineVolume(BigDecimal v) { this.lineVolume = v; }
    public String getOriginCode() { return originCode; }
    public void setOriginCode(String v) { this.originCode = v; }
    public String getTrnKnd() { return trnKnd; }
    public void setTrnKnd(String v) { this.trnKnd = v; }
    public String getPik1() { return pik1; }
    public void setPik1(String v) { this.pik1 = v; }
    public String getPik2() { return pik2; }
    public void setPik2(String v) { this.pik2 = v; }
    public String getPik3() { return pik3; }
    public void setPik3(String v) { this.pik3 = v; }
    public String getPik4() { return pik4; }
    public void setPik4(String v) { this.pik4 = v; }
    public String getPik5() { return pik5; }
    public void setPik5(String v) { this.pik5 = v; }
    public String getPik6() { return pik6; }
    public void setPik6(String v) { this.pik6 = v; }
    public String getPik7() { return pik7; }
    public void setPik7(String v) { this.pik7 = v; }
    public String getDmgFlg() { return dmgFlg; }
    public void setDmgFlg(String v) { this.dmgFlg = v; }
    public boolean isMultiCargo() { return multiCargo; }
    public void setMultiCargo(boolean v) { this.multiCargo = v; }
    public String getUpdTimestamp() { return updTimestamp; }
    public void setUpdTimestamp(String v) { this.updTimestamp = v; }
}
