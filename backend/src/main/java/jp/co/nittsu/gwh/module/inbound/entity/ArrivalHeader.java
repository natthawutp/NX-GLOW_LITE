package jp.co.nittsu.gwh.module.inbound.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Arrival (Inbound) Header entity.
 * Maps to VGWH_TJ_AV_H view.
 */
@Entity
@Table(name = "VGWH_TJ_AV_H")
public class ArrivalHeader extends BaseEntity {

    @Id
    @Column(name = "AVH_AV_NUM", length = 30)
    private String arrivalNumber;

    @Column(name = "AVH_CPNY_COD", length = 20)
    private String companyCode;

    @Column(name = "AVH_WHS_COD", length = 20)
    private String warehouseCode;

    @Column(name = "AVH_CUST_COD", length = 20)
    private String customerCode;

    @Column(name = "AVH_SCDL_YMD")
    private LocalDate scheduledDate;

    @Column(name = "AVH_AV_STS", length = 10)
    private String arrivalStatus;

    @Column(name = "AVH_TRN_KND", length = 10)
    private String transactionKind;

    @Column(name = "AVH_SUPLR_COD", length = 20)
    private String supplierCode;

    @Column(name = "AVH_SUPLR_NAM", length = 200)
    private String supplierName;

    @Column(name = "AVH_PO_NUM", length = 30)
    private String poNumber;

    @Column(name = "AVH_RMK", length = 500)
    private String remarks;

    @Column(name = "AVH_TTL_QTY")
    private Integer totalQuantity;

    // --- Getters & Setters ---
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public String getArrivalStatus() { return arrivalStatus; }
    public void setArrivalStatus(String arrivalStatus) { this.arrivalStatus = arrivalStatus; }
    public String getTransactionKind() { return transactionKind; }
    public void setTransactionKind(String transactionKind) { this.transactionKind = transactionKind; }
    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
}
