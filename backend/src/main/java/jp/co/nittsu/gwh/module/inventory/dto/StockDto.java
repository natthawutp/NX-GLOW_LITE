package jp.co.nittsu.gwh.module.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Stock inquiry result row DTO.
 * Maps legacy GwhLpski020BdyDTO fields with @JsonProperty for Oracle column compatibility.
 */
public class StockDto {

    @JsonProperty("ST_CPNY_COD")
    private String companyCode;

    @JsonProperty("ST_WHS_COD")
    private String warehouseCode;

    @JsonProperty("ST_CUST_COD")
    private String customerCode;

    @JsonProperty("ST_PROD_COD")
    private String productCode;

    @JsonProperty("PROD_NAM1")
    private String productName;

    @JsonProperty("ST_ORGN_COD")
    private String originCode;

    @JsonProperty("ST_PIK1")
    private String pik1;

    @JsonProperty("ST_PIK2")
    private String pik2;

    @JsonProperty("ST_PIK3")
    private String pik3;

    @JsonProperty("ST_PIK4")
    private String pik4;

    @JsonProperty("ST_PIK5")
    private String pik5;

    @JsonProperty("ST_PIK6")
    private String pik6;

    @JsonProperty("ST_PIK7")
    private String pik7;

    @JsonProperty("ST_AREA_COD")
    private String areaCode;

    @JsonProperty("ST_RACK_COD")
    private String rackCode;

    @JsonProperty("ST_PSTN_COD")
    private String positionCode;

    @JsonProperty("ST_LVL_COD")
    private String levelCode;

    @JsonProperty("LOCATION")
    private String location;

    @JsonProperty("ST_SBIV_COD")
    private String subInventoryCode;

    @JsonProperty("SBIV_NAM")
    private String subInventoryName;

    @JsonProperty("ST_BOND_FLG")
    private String bondFlag;

    @JsonProperty("ST_DMG_FLG")
    private String damageFlag;

    @JsonProperty("ST_LOCK_FLG")
    private String lockFlag;

    @JsonProperty("ST_PYST_QTY")
    private BigDecimal physicalStockQty;

    @JsonProperty("ST_AVST_QTY")
    private BigDecimal availableStockQty;

    @JsonProperty("ST_ALST_QTY")
    private BigDecimal allocatedStockQty;

    @JsonProperty("ST_PYCS_QTY")
    private BigDecimal physicalCasesQty;

    @JsonProperty("ST_PYPC_QTY")
    private BigDecimal physicalPiecesQty;

    @JsonProperty("ST_AVCS_QTY")
    private BigDecimal availableCasesQty;

    @JsonProperty("ST_AVPC_QTY")
    private BigDecimal availablePiecesQty;

    @JsonProperty("ST_PPCS_QTY")
    private BigDecimal piecesPerCase;

    // --- Getters & Setters ---

    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getOriginCode() { return originCode; }
    public void setOriginCode(String originCode) { this.originCode = originCode; }
    public String getPik1() { return pik1; }
    public void setPik1(String pik1) { this.pik1 = pik1; }
    public String getPik2() { return pik2; }
    public void setPik2(String pik2) { this.pik2 = pik2; }
    public String getPik3() { return pik3; }
    public void setPik3(String pik3) { this.pik3 = pik3; }
    public String getPik4() { return pik4; }
    public void setPik4(String pik4) { this.pik4 = pik4; }
    public String getPik5() { return pik5; }
    public void setPik5(String pik5) { this.pik5 = pik5; }
    public String getPik6() { return pik6; }
    public void setPik6(String pik6) { this.pik6 = pik6; }
    public String getPik7() { return pik7; }
    public void setPik7(String pik7) { this.pik7 = pik7; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getRackCode() { return rackCode; }
    public void setRackCode(String rackCode) { this.rackCode = rackCode; }
    public String getPositionCode() { return positionCode; }
    public void setPositionCode(String positionCode) { this.positionCode = positionCode; }
    public String getLevelCode() { return levelCode; }
    public void setLevelCode(String levelCode) { this.levelCode = levelCode; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSubInventoryCode() { return subInventoryCode; }
    public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
    public String getSubInventoryName() { return subInventoryName; }
    public void setSubInventoryName(String subInventoryName) { this.subInventoryName = subInventoryName; }
    public String getBondFlag() { return bondFlag; }
    public void setBondFlag(String bondFlag) { this.bondFlag = bondFlag; }
    public String getDamageFlag() { return damageFlag; }
    public void setDamageFlag(String damageFlag) { this.damageFlag = damageFlag; }
    public String getLockFlag() { return lockFlag; }
    public void setLockFlag(String lockFlag) { this.lockFlag = lockFlag; }
    public BigDecimal getPhysicalStockQty() { return physicalStockQty; }
    public void setPhysicalStockQty(BigDecimal physicalStockQty) { this.physicalStockQty = physicalStockQty; }
    public BigDecimal getAvailableStockQty() { return availableStockQty; }
    public void setAvailableStockQty(BigDecimal availableStockQty) { this.availableStockQty = availableStockQty; }
    public BigDecimal getAllocatedStockQty() { return allocatedStockQty; }
    public void setAllocatedStockQty(BigDecimal allocatedStockQty) { this.allocatedStockQty = allocatedStockQty; }
    public BigDecimal getPhysicalCasesQty() { return physicalCasesQty; }
    public void setPhysicalCasesQty(BigDecimal physicalCasesQty) { this.physicalCasesQty = physicalCasesQty; }
    public BigDecimal getPhysicalPiecesQty() { return physicalPiecesQty; }
    public void setPhysicalPiecesQty(BigDecimal physicalPiecesQty) { this.physicalPiecesQty = physicalPiecesQty; }
    public BigDecimal getAvailableCasesQty() { return availableCasesQty; }
    public void setAvailableCasesQty(BigDecimal availableCasesQty) { this.availableCasesQty = availableCasesQty; }
    public BigDecimal getAvailablePiecesQty() { return availablePiecesQty; }
    public void setAvailablePiecesQty(BigDecimal availablePiecesQty) { this.availablePiecesQty = availablePiecesQty; }
    public BigDecimal getPiecesPerCase() { return piecesPerCase; }
    public void setPiecesPerCase(BigDecimal piecesPerCase) { this.piecesPerCase = piecesPerCase; }
}
