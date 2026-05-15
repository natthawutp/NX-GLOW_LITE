package jp.co.nittsu.gwh.module.auth.dto;

import javax.validation.constraints.NotBlank;

/**
 * Request DTO for step 2 of the login flow: selecting a tenant.
 */
public class TenantSelectionRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "Customer code is required")
    private String customerCode;

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
}
