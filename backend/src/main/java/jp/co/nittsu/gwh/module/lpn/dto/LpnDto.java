package jp.co.nittsu.gwh.module.lpn.dto;

import java.util.List;

public class LpnDto {

    private String lpnNumber;
    private String lpnRfNumber;
    private String lpnType;
    private String lpnTypeLabel;
    private String lpnStatus;
    private String lpnStatusLabel;
    private String parentLpnNumber;
    private String locationCode;
    private String arrivalNumber;
    private String shipmentNumber;
    private Integer totalQuantity;
    private Double totalWeight;
    private Double totalVolume;
    private String barcodeFormat;
    private String printDate;
    private String receiveDate;
    private String remarks;
    private List<LpnContentDto> contents;
    private int childCount;

    // --- Getters & Setters ---
    public String getLpnNumber() { return lpnNumber; }
    public void setLpnNumber(String lpnNumber) { this.lpnNumber = lpnNumber; }
    public String getLpnRfNumber() { return lpnRfNumber; }
    public void setLpnRfNumber(String lpnRfNumber) { this.lpnRfNumber = lpnRfNumber; }
    public String getLpnType() { return lpnType; }
    public void setLpnType(String lpnType) { this.lpnType = lpnType; }
    public String getLpnTypeLabel() { return lpnTypeLabel; }
    public void setLpnTypeLabel(String lpnTypeLabel) { this.lpnTypeLabel = lpnTypeLabel; }
    public String getLpnStatus() { return lpnStatus; }
    public void setLpnStatus(String lpnStatus) { this.lpnStatus = lpnStatus; }
    public String getLpnStatusLabel() { return lpnStatusLabel; }
    public void setLpnStatusLabel(String lpnStatusLabel) { this.lpnStatusLabel = lpnStatusLabel; }
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
    public List<LpnContentDto> getContents() { return contents; }
    public void setContents(List<LpnContentDto> contents) { this.contents = contents; }
    public int getChildCount() { return childCount; }
    public void setChildCount(int childCount) { this.childCount = childCount; }
}
