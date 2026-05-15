package jp.co.nittsu.gwh.module.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserInfo user;
    private List<TenantOption> tenants;
    private Boolean tenantSelected;

    public static class UserInfo {
        private Long userId;
        private String displayName;
        private String email;
        private String companyCode;
        private String warehouseCode;
        private String customerCode;
        private String locale;
        private List<String> roles;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCompanyCode() { return companyCode; }
        public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
        public String getWarehouseCode() { return warehouseCode; }
        public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public String getLocale() { return locale; }
        public void setLocale(String locale) { this.locale = locale; }
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public Long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
    public List<TenantOption> getTenants() { return tenants; }
    public void setTenants(List<TenantOption> tenants) { this.tenants = tenants; }
    public Boolean getTenantSelected() { return tenantSelected; }
    public void setTenantSelected(Boolean tenantSelected) { this.tenantSelected = tenantSelected; }
}
