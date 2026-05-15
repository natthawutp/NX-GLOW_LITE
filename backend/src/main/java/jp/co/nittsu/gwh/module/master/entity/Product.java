package jp.co.nittsu.gwh.module.master.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;

/**
 * Product Master entity.
 * Maps to VGWH_TM_PROD view.
 */
@Entity
@Table(name = "VGWH_TM_PROD")
public class Product extends BaseEntity {

    @Id
    @Column(name = "PROD_COD", length = 30)
    private String productCode;

    @Column(name = "PROD_CPNY_COD", length = 20)
    private String companyCode;

    @Column(name = "PROD_WHS_COD", length = 20)
    private String warehouseCode;

    @Column(name = "PROD_CUST_COD", length = 20)
    private String customerCode;

    @Column(name = "PROD_NAM", length = 200)
    private String productName;

    @Column(name = "PROD_NAM2", length = 200)
    private String productName2;

    @Column(name = "PROD_BARCD", length = 50)
    private String barcode;

    @Column(name = "PROD_GRP_COD", length = 20)
    private String groupCode;

    @Column(name = "PROD_UNT", length = 10)
    private String unit;

    @Column(name = "PROD_WGT")
    private Double weight;

    @Column(name = "PROD_VOL")
    private Double volume;

    @Column(name = "PROD_LEN")
    private Double length;

    @Column(name = "PROD_WID")
    private Double width;

    @Column(name = "PROD_HGT")
    private Double height;

    @Column(name = "PROD_CSE_QTY")
    private Integer caseQuantity;

    @Column(name = "PROD_RMK", length = 500)
    private String remarks;

    // --- Getters & Setters ---
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductName2() { return productName2; }
    public void setProductName2(String productName2) { this.productName2 = productName2; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getGroupCode() { return groupCode; }
    public void setGroupCode(String groupCode) { this.groupCode = groupCode; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getVolume() { return volume; }
    public void setVolume(Double volume) { this.volume = volume; }
    public Double getLength() { return length; }
    public void setLength(Double length) { this.length = length; }
    public Double getWidth() { return width; }
    public void setWidth(Double width) { this.width = width; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public Integer getCaseQuantity() { return caseQuantity; }
    public void setCaseQuantity(Integer caseQuantity) { this.caseQuantity = caseQuantity; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
