package jp.co.nittsu.gwh.module.lpn.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name = "VGWH_TJ_LPN_D")
@IdClass(LpnContentId.class)
public class LpnContent extends BaseEntity {

    @Id
    @Column(name = "LPND_CPNY_COD", length = 20)
    private String companyCode;

    @Id
    @Column(name = "LPND_WHS_COD", length = 20)
    private String warehouseCode;

    @Id
    @Column(name = "LPND_CUST_COD", length = 20)
    private String customerCode;

    @Id
    @Column(name = "LPND_LPN_NUM", length = 30)
    private String lpnNumber;

    @Id
    @Column(name = "LPND_LN_NUM")
    private Integer lineNumber;

    @Column(name = "LPND_PROD_COD", length = 30)
    private String productCode;

    @Column(name = "LPND_LOT_NUM", length = 30)
    private String lotNumber;

    @Column(name = "LPND_QTY")
    private Integer quantity;

    @Column(name = "LPND_ALC_QTY")
    private Integer allocatedQuantity;

    @Column(name = "LPND_AVL_QTY")
    private Integer availableQuantity;

    @Column(name = "LPND_SBIV_COD", length = 20)
    private String subInventoryCode;

    @Column(name = "LPND_ST_STS", length = 10)
    private String stockStatus;

    @Column(name = "LPND_AV_NUM", length = 30)
    private String arrivalNumber;

    @Column(name = "LPND_AVLN_NUM")
    private Integer arrivalLineNumber;

    @Column(name = "LPND_AVSQ_NUM")
    private Integer arrivalSeqNumber;

    @Column(name = "LPND_PIK1", length = 40)
    private String pik1;

    @Column(name = "LPND_PIK2", length = 40)
    private String pik2;

    @Column(name = "LPND_PIK3", length = 40)
    private String pik3;

    @Column(name = "LPND_PIK4", length = 40)
    private String pik4;

    @Column(name = "LPND_PIK5", length = 40)
    private String pik5;

    @Column(name = "LPND_PIK6", length = 40)
    private String pik6;

    @Column(name = "LPND_PIK7", length = 40)
    private String pik7;

    @Column(name = "LPND_RMK", length = 500)
    private String remarks;

    // --- Getters & Setters ---
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getLpnNumber() { return lpnNumber; }
    public void setLpnNumber(String lpnNumber) { this.lpnNumber = lpnNumber; }
    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getAllocatedQuantity() { return allocatedQuantity; }
    public void setAllocatedQuantity(Integer allocatedQuantity) { this.allocatedQuantity = allocatedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public String getSubInventoryCode() { return subInventoryCode; }
    public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public Integer getArrivalLineNumber() { return arrivalLineNumber; }
    public void setArrivalLineNumber(Integer arrivalLineNumber) { this.arrivalLineNumber = arrivalLineNumber; }
    public Integer getArrivalSeqNumber() { return arrivalSeqNumber; }
    public void setArrivalSeqNumber(Integer arrivalSeqNumber) { this.arrivalSeqNumber = arrivalSeqNumber; }
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
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
