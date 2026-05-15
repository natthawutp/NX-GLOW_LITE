package jp.co.nittsu.gwh.module.inbound.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Arrival Detail entity — one line per SKU/PO line in an arrival.
 * Maps to VGWH_TJ_AV_D view.
 */
@Entity
@Table(name = "VGWH_TJ_AV_D")
@IdClass(ArrivalDetailId.class)
public class ArrivalDetail extends BaseEntity {

    @Id
    @Column(name = "AVD_CPNY_COD", length = 20)
    private String companyCode;

    @Id
    @Column(name = "AVD_WHS_COD", length = 20)
    private String warehouseCode;

    @Id
    @Column(name = "AVD_CUST_COD", length = 20)
    private String customerCode;

    @Id
    @Column(name = "AVD_AV_NUM", length = 30)
    private String arrivalNumber;

    @Id
    @Column(name = "AVD_AV_LIN_NUM")
    private Integer lineNumber;

    @Column(name = "AVD_AV_STS", length = 10)
    private String lineStatus;

    @Column(name = "AVD_PRD_COD", length = 30)
    private String productCode;

    @Column(name = "AVD_PRD_NAM", length = 200)
    private String productName;

    @Column(name = "AVD_SBIV_COD", length = 10)
    private String subInventoryCode;

    @Column(name = "AVD_PO_NUM", length = 30)
    private String poNumber;

    @Column(name = "AVD_LT_NUM", length = 30)
    private String lotNumber;

    /** Planned CS quantity */
    @Column(name = "AVD_PLAN_CS_QTY")
    private Integer planCsQty;

    /** Planned PC quantity */
    @Column(name = "AVD_PLAN_PC_QTY")
    private Integer planPcQty;

    /** Planned BL quantity */
    @Column(name = "AVD_PLAN_BL_QTY")
    private Integer planBlQty;

    /** Pieces per case */
    @Column(name = "AVD_PRD_PPC_NUM")
    private Integer piecesPerCase;

    /** Pieces per bulk */
    @Column(name = "AVD_PRD_PPB_NUM")
    private Integer piecesPerBulk;

    @Column(name = "AVD_AREA_COD", length = 20)
    private String areaCode;

    @Column(name = "AVD_RACK_COD", length = 20)
    private String rackCode;

    @Column(name = "AVD_LVL_COD", length = 10)
    private String levelCode;

    @Column(name = "AVD_PSTN_COD", length = 10)
    private String positionCode;

    @Column(name = "AVD_CGID_NUM")
    private Integer cargoIdCount;

    @Column(name = "AVD_TTL_WGT", precision = 15, scale = 4)
    private BigDecimal totalWeight;

    @Column(name = "AVD_TTL_M3", precision = 15, scale = 6)
    private BigDecimal totalVolume;

    // --- Getters & Setters ---
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
    public String getLineStatus() { return lineStatus; }
    public void setLineStatus(String lineStatus) { this.lineStatus = lineStatus; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getSubInventoryCode() { return subInventoryCode; }
    public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public Integer getPlanCsQty() { return planCsQty; }
    public void setPlanCsQty(Integer planCsQty) { this.planCsQty = planCsQty; }
    public Integer getPlanPcQty() { return planPcQty; }
    public void setPlanPcQty(Integer planPcQty) { this.planPcQty = planPcQty; }
    public Integer getPlanBlQty() { return planBlQty; }
    public void setPlanBlQty(Integer planBlQty) { this.planBlQty = planBlQty; }
    public Integer getPiecesPerCase() { return piecesPerCase; }
    public void setPiecesPerCase(Integer piecesPerCase) { this.piecesPerCase = piecesPerCase; }
    public Integer getPiecesPerBulk() { return piecesPerBulk; }
    public void setPiecesPerBulk(Integer piecesPerBulk) { this.piecesPerBulk = piecesPerBulk; }
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getRackCode() { return rackCode; }
    public void setRackCode(String rackCode) { this.rackCode = rackCode; }
    public String getLevelCode() { return levelCode; }
    public void setLevelCode(String levelCode) { this.levelCode = levelCode; }
    public String getPositionCode() { return positionCode; }
    public void setPositionCode(String positionCode) { this.positionCode = positionCode; }
    public Integer getCargoIdCount() { return cargoIdCount; }
    public void setCargoIdCount(Integer cargoIdCount) { this.cargoIdCount = cargoIdCount; }
    public BigDecimal getTotalWeight() { return totalWeight; }
    public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }
    public BigDecimal getTotalVolume() { return totalVolume; }
    public void setTotalVolume(BigDecimal totalVolume) { this.totalVolume = totalVolume; }
}
