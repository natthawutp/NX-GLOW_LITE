package jp.co.nittsu.gwh.module.inbound.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Arrival Result entity — inspection result per detail line/cargo.
 * Maps to VGWH_TJ_AV_R view (written via GWH_TJ_AV_R table).
 */
@Entity
@Table(name = "VGWH_TJ_AV_R")
@IdClass(ArrivalResultId.class)
public class ArrivalResult extends BaseEntity {

    @Id
    @Column(name = "AVR_CPNY_COD", length = 20)
    private String companyCode;

    @Id
    @Column(name = "AVR_WHS_COD", length = 20)
    private String warehouseCode;

    @Id
    @Column(name = "AVR_CUST_COD", length = 20)
    private String customerCode;

    @Id
    @Column(name = "AVR_AV_NUM", length = 30)
    private String arrivalNumber;

    @Id
    @Column(name = "AVR_AV_LIN_NUM")
    private Integer lineNumber;

    @Id
    @Column(name = "AVR_SEQ_NUM")
    private Integer sequenceNumber;

    @Column(name = "AVR_PRD_COD", length = 30)
    private String productCode;

    @Column(name = "AVR_LT_NUM", length = 30)
    private String lotNumber;

    /** Actual received CS quantity */
    @Column(name = "AVR_RST_CS_QTY")
    private Integer resultCsQty;

    /** Actual received PC quantity */
    @Column(name = "AVR_RST_PC_QTY")
    private Integer resultPcQty;

    /** Actual received BL quantity */
    @Column(name = "AVR_RST_BL_QTY")
    private Integer resultBlQty;

    /** Total pieces (computed: PPC×CS + PC + PPB×BL) */
    @Column(name = "AVR_RST_TTL_QTY")
    private Integer resultTotalQty;

    @Column(name = "AVR_AREA_COD", length = 20)
    private String areaCode;

    @Column(name = "AVR_RACK_COD", length = 20)
    private String rackCode;

    @Column(name = "AVR_LVL_COD", length = 10)
    private String levelCode;

    @Column(name = "AVR_PSTN_COD", length = 10)
    private String positionCode;

    /** LPN number assigned during inspection (nullable) */
    @Column(name = "AVR_LPN_NUM", length = 30)
    private String lpnNumber;

    @Column(name = "AVR_RST_WGT", precision = 15, scale = 4)
    private BigDecimal resultWeight;

    @Column(name = "AVR_RST_M3", precision = 15, scale = 6)
    private BigDecimal resultVolume;

    // --- Getters & Setters ---
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String v) { this.companyCode = v; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String v) { this.warehouseCode = v; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String v) { this.customerCode = v; }
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer v) { this.lineNumber = v; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer v) { this.sequenceNumber = v; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String v) { this.productCode = v; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String v) { this.lotNumber = v; }
    public Integer getResultCsQty() { return resultCsQty; }
    public void setResultCsQty(Integer v) { this.resultCsQty = v; }
    public Integer getResultPcQty() { return resultPcQty; }
    public void setResultPcQty(Integer v) { this.resultPcQty = v; }
    public Integer getResultBlQty() { return resultBlQty; }
    public void setResultBlQty(Integer v) { this.resultBlQty = v; }
    public Integer getResultTotalQty() { return resultTotalQty; }
    public void setResultTotalQty(Integer v) { this.resultTotalQty = v; }
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
    public BigDecimal getResultWeight() { return resultWeight; }
    public void setResultWeight(BigDecimal v) { this.resultWeight = v; }
    public BigDecimal getResultVolume() { return resultVolume; }
    public void setResultVolume(BigDecimal v) { this.resultVolume = v; }
}
