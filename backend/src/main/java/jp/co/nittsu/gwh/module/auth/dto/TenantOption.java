package jp.co.nittsu.gwh.module.auth.dto;

/**
 * Represents a tenant option (warehouse + customer) the user can operate in.
 * Populated from VGWH_TM_CUST based on the user's company code.
 */
public class TenantOption {

    private String warehouseCode;
    private String customerCode;
    private String customerName;

    public TenantOption() {}

    public TenantOption(String warehouseCode, String customerCode, String customerName) {
        this.warehouseCode = warehouseCode;
        this.customerCode = customerCode;
        this.customerName = customerName;
    }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
