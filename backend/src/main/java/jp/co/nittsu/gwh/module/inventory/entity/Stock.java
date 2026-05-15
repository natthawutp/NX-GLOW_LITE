package jp.co.nittsu.gwh.module.inventory.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Stock/Inventory entity.
 * Maps to VGWH_TJ_ST view - current stock positions.
 */
@Entity
@Table(name = "VGWH_TJ_ST")
@IdClass(StockId.class)
public class Stock extends BaseEntity {

    @Id
    @Column(name = "ST_CPNY_COD", length = 20)
    private String companyCode;

    @Id
    @Column(name = "ST_WHS_COD", length = 20)
    private String warehouseCode;

    @Id
    @Column(name = "ST_CUST_COD", length = 20)
    private String customerCode;

    @Id
    @Column(name = "ST_PROD_COD", length = 30)
    private String productCode;

    @Id
    @Column(name = "ST_LOC_COD", length = 20)
    private String locationCode;

    @Id
    @Column(name = "ST_LOT_NUM", length = 30)
    private String lotNumber;

    @Column(name = "ST_QTY")
    private Integer quantity;

    @Column(name = "ST_ALC_QTY")
    private Integer allocatedQuantity;

    @Column(name = "ST_AVL_QTY")
    private Integer availableQuantity;

    @Column(name = "ST_STS", length = 10)
    private String stockStatus;

    @Column(name = "ST_SBIV_COD", length = 20)
    private String subInventoryCode;

    @Column(name = "ST_ORGN_COD", length = 20)
    private String originCode;

    @Column(name = "ST_PIK1", length = 30)
    private String pik1;

    @Column(name = "ST_PIK2", length = 30)
    private String pik2;

    @Column(name = "ST_PIK3", length = 30)
    private String pik3;

    @Column(name = "ST_PIK4", length = 30)
    private String pik4;

    @Column(name = "ST_PIK5", length = 30)
    private String pik5;

    @Column(name = "ST_PIK6", length = 30)
    private String pik6;

    @Column(name = "ST_PIK7", length = 30)
    private String pik7;

    @Column(name = "ST_AREA_COD", length = 10)
    private String areaCode;

    @Column(name = "ST_RACK_COD", length = 10)
    private String rackCode;

    @Column(name = "ST_PSTN_COD", length = 10)
    private String positionCode;

    @Column(name = "ST_LVL_COD", length = 10)
    private String levelCode;

    @Column(name = "ST_BOND_FLG", length = 1)
    private String bondFlag;

    @Column(name = "ST_DMG_FLG", length = 1)
    private String damageFlag;

    @Column(name = "ST_LOCK_FLG", length = 1)
    private String lockFlag;

    @Column(name = "ST_PSSA_FLG", length = 1)
    private String passaFlag;

    @Column(name = "ST_PYST_QTY")
    private BigDecimal physicalStockQty;

    @Column(name = "ST_AVST_QTY")
    private BigDecimal availableStockQty;

    @Column(name = "ST_ALST_QTY")
    private BigDecimal allocatedStockQty;

    @Column(name = "ST_PPCS_QTY")
    private BigDecimal piecesPerCaseQty;

    @Column(name = "ST_AV_NUM", length = 20)
    private String arrivalNumber;

    @Column(name = "ST_AVLN_NUM", length = 10)
    private String arrivalLineNumber;

    @Column(name = "ST_AVSQ_NUM", length = 10)
    private String arrivalSeqNumber;

    @Column(name = "ST_AV_YMD")
    private Timestamp arrivalDate;

    // --- Getters & Setters ---
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getAllocatedQuantity() { return allocatedQuantity; }
    public void setAllocatedQuantity(Integer allocatedQuantity) { this.allocatedQuantity = allocatedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
    public String getSubInventoryCode() { return subInventoryCode; }
    public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
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
    public String getBondFlag() { return bondFlag; }
    public void setBondFlag(String bondFlag) { this.bondFlag = bondFlag; }
    public String getDamageFlag() { return damageFlag; }
    public void setDamageFlag(String damageFlag) { this.damageFlag = damageFlag; }
    public String getLockFlag() { return lockFlag; }
    public void setLockFlag(String lockFlag) { this.lockFlag = lockFlag; }
    public String getPassaFlag() { return passaFlag; }
    public void setPassaFlag(String passaFlag) { this.passaFlag = passaFlag; }
    public BigDecimal getPhysicalStockQty() { return physicalStockQty; }
    public void setPhysicalStockQty(BigDecimal physicalStockQty) { this.physicalStockQty = physicalStockQty; }
    public BigDecimal getAvailableStockQty() { return availableStockQty; }
    public void setAvailableStockQty(BigDecimal availableStockQty) { this.availableStockQty = availableStockQty; }
    public BigDecimal getAllocatedStockQty() { return allocatedStockQty; }
    public void setAllocatedStockQty(BigDecimal allocatedStockQty) { this.allocatedStockQty = allocatedStockQty; }
    public BigDecimal getPiecesPerCaseQty() { return piecesPerCaseQty; }
    public void setPiecesPerCaseQty(BigDecimal piecesPerCaseQty) { this.piecesPerCaseQty = piecesPerCaseQty; }
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getArrivalLineNumber() { return arrivalLineNumber; }
    public void setArrivalLineNumber(String arrivalLineNumber) { this.arrivalLineNumber = arrivalLineNumber; }
    public String getArrivalSeqNumber() { return arrivalSeqNumber; }
    public void setArrivalSeqNumber(String arrivalSeqNumber) { this.arrivalSeqNumber = arrivalSeqNumber; }
    public Timestamp getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(Timestamp arrivalDate) { this.arrivalDate = arrivalDate; }
}
