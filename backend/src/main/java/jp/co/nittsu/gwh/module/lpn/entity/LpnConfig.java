package jp.co.nittsu.gwh.module.lpn.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name = "VGWH_TM_LPN_CFG")
@IdClass(LpnConfigId.class)
public class LpnConfig extends BaseEntity {

    @Id
    @Column(name = "LCFG_CPNY_COD", length = 20)
    private String companyCode;

    @Id
    @Column(name = "LCFG_WHS_COD", length = 20)
    private String warehouseCode;

    @Id
    @Column(name = "LCFG_CUST_COD", length = 20)
    private String customerCode;

    @Column(name = "LCFG_LPN_USE_FLG", length = 1)
    private String lpnUseFlag;

    @Column(name = "LCFG_AUTO_GEN_FLG", length = 1)
    private String autoGenerateFlag;

    @Column(name = "LCFG_BARCD_FMT", length = 10)
    private String barcodeFormat;

    @Column(name = "LCFG_BARCD_PFX", length = 10)
    private String barcodePrefix;

    @Column(name = "LCFG_PRNT_TPL", length = 50)
    private String printTemplate;

    @Column(name = "LCFG_HIR_FLG", length = 1)
    private String hierarchyFlag;

    @Column(name = "LCFG_MIX_FLG", length = 1)
    private String mixedSkuFlag;

    @Column(name = "LCFG_OBND_LPN_FLG", length = 1)
    private String outboundLpnFlag;

    // --- Getters & Setters ---
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getLpnUseFlag() { return lpnUseFlag; }
    public void setLpnUseFlag(String lpnUseFlag) { this.lpnUseFlag = lpnUseFlag; }
    public String getAutoGenerateFlag() { return autoGenerateFlag; }
    public void setAutoGenerateFlag(String autoGenerateFlag) { this.autoGenerateFlag = autoGenerateFlag; }
    public String getBarcodeFormat() { return barcodeFormat; }
    public void setBarcodeFormat(String barcodeFormat) { this.barcodeFormat = barcodeFormat; }
    public String getBarcodePrefix() { return barcodePrefix; }
    public void setBarcodePrefix(String barcodePrefix) { this.barcodePrefix = barcodePrefix; }
    public String getPrintTemplate() { return printTemplate; }
    public void setPrintTemplate(String printTemplate) { this.printTemplate = printTemplate; }
    public String getHierarchyFlag() { return hierarchyFlag; }
    public void setHierarchyFlag(String hierarchyFlag) { this.hierarchyFlag = hierarchyFlag; }
    public String getMixedSkuFlag() { return mixedSkuFlag; }
    public void setMixedSkuFlag(String mixedSkuFlag) { this.mixedSkuFlag = mixedSkuFlag; }
    public String getOutboundLpnFlag() { return outboundLpnFlag; }
    public void setOutboundLpnFlag(String outboundLpnFlag) { this.outboundLpnFlag = outboundLpnFlag; }

    public boolean isLpnEnabled() { return "Y".equals(lpnUseFlag); }
    public boolean isAutoGenerate() { return "Y".equals(autoGenerateFlag); }
    public boolean isHierarchyEnabled() { return "Y".equals(hierarchyFlag); }
    public boolean isMixedSkuAllowed() { return "Y".equals(mixedSkuFlag); }
    public boolean isOutboundLpnEnabled() { return "Y".equals(outboundLpnFlag); }
}
