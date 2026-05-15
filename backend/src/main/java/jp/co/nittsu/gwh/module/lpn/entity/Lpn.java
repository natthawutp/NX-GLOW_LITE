package jp.co.nittsu.gwh.module.lpn.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name = "SGWH0001.GWH_TJ_LPN")
@IdClass(LpnId.class)
public class Lpn extends BaseEntity {

    @Id
    @Column(name = "LPN_CPNY_COD", length = 20)
    private String companyCode;

    @Id
    @Column(name = "LPN_WHS_COD", length = 20)
    private String warehouseCode;

    @Id
    @Column(name = "LPN_CUST_COD", length = 20)
    private String customerCode;

    @Id
    @Column(name = "LPN_NUM", length = 30)
    private String lpnNumber;

    @Column(name = "LPN_RF_NUM", length = 30)
    private String lpnRfNumber;

    @Column(name = "LPN_TYPE", length = 10)
    private String lpnType;

    @Column(name = "LPN_STS", length = 10)
    private String lpnStatus;

    @Column(name = "LPN_PRNT_NUM", length = 30)
    private String parentLpnNumber;

    @Column(name = "LPN_LOC_COD", length = 20)
    private String locationCode;

    @Column(name = "LPN_AV_NUM", length = 30)
    private String arrivalNumber;

    @Column(name = "LPN_SP_NUM", length = 30)
    private String shipmentNumber;

    @Column(name = "LPN_TTL_QTY")
    private Integer totalQuantity;

    @Column(name = "LPN_TTL_WGT")
    private Double totalWeight;

    @Column(name = "LPN_TTL_VOL")
    private Double totalVolume;

    @Column(name = "LPN_BARCD_FMT", length = 10)
    private String barcodeFormat;

    @Column(name = "LPN_PRNT_YMD", length = 8)
    private String printDate;

    @Column(name = "LPN_RCV_YMD", length = 8)
    private String receiveDate;

    @Column(name = "LPN_RMK", length = 500)
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
    public String getLpnRfNumber() { return lpnRfNumber; }
    public void setLpnRfNumber(String lpnRfNumber) { this.lpnRfNumber = lpnRfNumber; }
    public String getLpnType() { return lpnType; }
    public void setLpnType(String lpnType) { this.lpnType = lpnType; }
    public String getLpnStatus() { return lpnStatus; }
    public void setLpnStatus(String lpnStatus) { this.lpnStatus = lpnStatus; }
    public String getParentLpnNumber() { return parentLpnNumber; }
    public void setParentLpnNumber(String parentLpnNumber) { this.parentLpnNumber = parentLpnNumber; }
    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getShipmentNumber() { return shipmentNumber; }
    public void setShipmentNumber(String shipmentNumber) { this.shipmentNumber = shipmentNumber; }
    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Double getTotalWeight() { return totalWeight; }
    public void setTotalWeight(Double totalWeight) { this.totalWeight = totalWeight; }
    public Double getTotalVolume() { return totalVolume; }
    public void setTotalVolume(Double totalVolume) { this.totalVolume = totalVolume; }
    public String getBarcodeFormat() { return barcodeFormat; }
    public void setBarcodeFormat(String barcodeFormat) { this.barcodeFormat = barcodeFormat; }
    public String getPrintDate() { return printDate; }
    public void setPrintDate(String printDate) { this.printDate = printDate; }
    public String getReceiveDate() { return receiveDate; }
    public void setReceiveDate(String receiveDate) { this.receiveDate = receiveDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
