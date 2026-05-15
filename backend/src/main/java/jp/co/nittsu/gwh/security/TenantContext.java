package jp.co.nittsu.gwh.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Request-scoped bean holding multi-tenant context from JWT.
 * Every database query uses these values for partition filtering.
 */
@Component
@RequestScope
public class TenantContext {

    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private String userId;

    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
