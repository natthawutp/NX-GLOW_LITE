package jp.co.nittsu.gwh.module.outbound.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Shipment Header entity.
 * Maps to VGWH_TJ_SP_H view (read) / GWH_TJ_SP_H table (write).
 */
@Entity
@Table(name = "VGWH_TJ_SP_H")
public class ShipmentHeader extends BaseEntity {

    @Id
    @Column(name = "SPH_SP_NUM", length = 30)
    private String spNum;

    @Column(name = "SPH_CPNY_COD", length = 20)
    private String companyCode;

    @Column(name = "SPH_WHS_COD", length = 20)
    private String warehouseCode;

    @Column(name = "SPH_CUST_COD", length = 20)
    private String customerCode;

    @Column(name = "SPH_SCDL_YMD")
    private LocalDate scheduledDate;

    @Column(name = "SPH_SP_STS", length = 10)
    private String shipmentStatus;

    @Column(name = "SPH_BT_NUM", length = 30)
    private String batchNumber;

    @Column(name = "SPH_TRN_KND", length = 10)
    private String transactionKind;

    @Column(name = "SPH_DLV_COD", length = 20)
    private String deliveryCode;

    @Column(name = "SPH_DLV_NAM", length = 200)
    private String deliveryName;

    @Column(name = "SPH_ROUT_COD", length = 20)
    private String routeCode;

    @Column(name = "SPH_IV_NUM", length = 30)
    private String invoiceNumber;

    @Column(name = "SPH_ORD_NUM", length = 30)
    private String orderNumber;

    @Column(name = "SPH_RMK", length = 500)
    private String remarks;

    @Column(name = "SPH_TTL_QTY")
    private Integer totalQuantity;

    @Column(name = "SPH_TTL_CSE")
    private Integer totalCases;

    // --- Getters & Setters ---
    public String getSpNum() { return spNum; }
    public void setSpNum(String spNum) { this.spNum = spNum; }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public String getShipmentStatus() { return shipmentStatus; }
    public void setShipmentStatus(String shipmentStatus) { this.shipmentStatus = shipmentStatus; }
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public String getTransactionKind() { return transactionKind; }
    public void setTransactionKind(String transactionKind) { this.transactionKind = transactionKind; }
    public String getDeliveryCode() { return deliveryCode; }
    public void setDeliveryCode(String deliveryCode) { this.deliveryCode = deliveryCode; }
    public String getDeliveryName() { return deliveryName; }
    public void setDeliveryName(String deliveryName) { this.deliveryName = deliveryName; }
    public String getRouteCode() { return routeCode; }
    public void setRouteCode(String routeCode) { this.routeCode = routeCode; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getTotalCases() { return totalCases; }
    public void setTotalCases(Integer totalCases) { this.totalCases = totalCases; }
}
