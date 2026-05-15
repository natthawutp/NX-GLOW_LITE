package jp.co.nittsu.gwh.module.lpn.dto;

public class LpnConfigDto {

    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private boolean lpnEnabled;
    private boolean autoGenerate;
    private String barcodeFormat;
    private String barcodePrefix;
    private String printTemplate;
    private boolean hierarchyEnabled;
    private boolean mixedSkuAllowed;
    private boolean outboundLpnEnabled;

    // --- Getters & Setters ---
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public boolean isLpnEnabled() { return lpnEnabled; }
    public void setLpnEnabled(boolean lpnEnabled) { this.lpnEnabled = lpnEnabled; }
    public boolean isAutoGenerate() { return autoGenerate; }
    public void setAutoGenerate(boolean autoGenerate) { this.autoGenerate = autoGenerate; }
    public String getBarcodeFormat() { return barcodeFormat; }
    public void setBarcodeFormat(String barcodeFormat) { this.barcodeFormat = barcodeFormat; }
    public String getBarcodePrefix() { return barcodePrefix; }
    public void setBarcodePrefix(String barcodePrefix) { this.barcodePrefix = barcodePrefix; }
    public String getPrintTemplate() { return printTemplate; }
    public void setPrintTemplate(String printTemplate) { this.printTemplate = printTemplate; }
    public boolean isHierarchyEnabled() { return hierarchyEnabled; }
    public void setHierarchyEnabled(boolean hierarchyEnabled) { this.hierarchyEnabled = hierarchyEnabled; }
    public boolean isMixedSkuAllowed() { return mixedSkuAllowed; }
    public void setMixedSkuAllowed(boolean mixedSkuAllowed) { this.mixedSkuAllowed = mixedSkuAllowed; }
    public boolean isOutboundLpnEnabled() { return outboundLpnEnabled; }
    public void setOutboundLpnEnabled(boolean outboundLpnEnabled) { this.outboundLpnEnabled = outboundLpnEnabled; }
}
