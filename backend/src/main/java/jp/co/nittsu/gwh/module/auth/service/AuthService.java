package jp.co.nittsu.gwh.module.auth.service;

import jp.co.nittsu.gwh.module.auth.dto.LoginRequest;
import jp.co.nittsu.gwh.module.auth.dto.LoginResponse;
import jp.co.nittsu.gwh.module.auth.dto.TenantOption;
import jp.co.nittsu.gwh.module.auth.dto.TenantSelectionRequest;
import jp.co.nittsu.gwh.module.auth.entity.WmsUser;
import jp.co.nittsu.gwh.module.auth.repository.CustomerMasterRepository;
import jp.co.nittsu.gwh.module.auth.repository.WmsUserRepository;
import jp.co.nittsu.gwh.security.JwtTokenProvider;
import jp.co.nittsu.gwh.security.WmsUserPrincipal;
import jp.co.nittsu.gwh.common.exception.WmsBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final String DEV_BYPASS_EMAIL = "admin@wms.com";
    private static final String DEV_BYPASS_COMPANY = "520002";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private WmsUserRepository userRepository;

    @Autowired
    private CustomerMasterRepository customerMasterRepository;

    @Value("${gwh.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    /**
     * Step 1: Authenticate with email + password.
     * Returns a temporary access token (no warehouse/customer claims)
     * and the list of available tenants for the user's company.
     */
    public LoginResponse login(LoginRequest request) {
        // --- DEV BYPASS: skip real auth for admin@wms.com ---
        if (DEV_BYPASS_EMAIL.equalsIgnoreCase(request.getEmail())) {
            return buildDevBypassLoginResponse(request);
        }

        // Authenticate using email as the Spring Security username
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        WmsUserPrincipal principal = (WmsUserPrincipal) authentication.getPrincipal();

        // Load user to get company code
        WmsUser user = userRepository.findByEmailAndDelFlg(request.getEmail(), 0)
                .orElseThrow(() -> new WmsBusinessException("ERR0174", "User not found"));

        // Generate temporary token (no warehouse/customer yet)
        String accessToken = tokenProvider.generateAccessToken(
                authentication, user.getCompanyCode(), null, null);
        String refreshToken = tokenProvider.generateRefreshToken(principal.getEmail());

        // Load available tenants from VGWH_TM_CUST
        List<TenantOption> tenants = customerMasterRepository
                .findTenantsByCompanyCode(user.getCompanyCode());

        // Build response
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(accessTokenExpiration);
        response.setTenantSelected(false);
        response.setTenants(tenants);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(principal.getUserId());
        userInfo.setDisplayName(principal.getDisplayName());
        userInfo.setEmail(principal.getEmail());
        userInfo.setCompanyCode(user.getCompanyCode());
        userInfo.setLocale(request.getLocale() != null ? request.getLocale() : "en");
        userInfo.setRoles(tokenProvider.getRoles(accessToken));
        response.setUser(userInfo);

        return response;
    }

    /**
     * Step 2: Select a tenant (warehouse + customer) after initial login.
     * Requires a valid access token from step 1.
     * Returns a new access token with full tenant claims embedded.
     */
    public LoginResponse selectTenant(TenantSelectionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // --- DEV BYPASS: handle mock user ---
        if (authentication != null && authentication.getPrincipal() instanceof WmsUserPrincipal) {
            WmsUserPrincipal p = (WmsUserPrincipal) authentication.getPrincipal();
            if (DEV_BYPASS_EMAIL.equalsIgnoreCase(p.getEmail())) {
                return buildDevBypassSelectTenantResponse(authentication, p, request);
            }
        }

        if (authentication == null || !(authentication.getPrincipal() instanceof WmsUserPrincipal)) {
            throw new WmsBusinessException("AUTH_INVALID", "Not authenticated");
        }

        WmsUserPrincipal principal = (WmsUserPrincipal) authentication.getPrincipal();

        // Load user for company code
        WmsUser user = userRepository.findByEmailAndDelFlg(principal.getEmail(), 0)
                .orElseThrow(() -> new WmsBusinessException("ERR0174", "User not found"));

        // Validate the selected tenant belongs to the user's company
        List<TenantOption> tenants = customerMasterRepository
                .findTenantsByCompanyCode(user.getCompanyCode());
        boolean validTenant = tenants.stream().anyMatch(t ->
                t.getWarehouseCode().equals(request.getWarehouseCode()) &&
                t.getCustomerCode().equals(request.getCustomerCode()));

        if (!validTenant) {
            throw new WmsBusinessException("ERR4214", "Invalid tenant selection");
        }

        // Generate final access token WITH warehouse + customer claims
        String accessToken = tokenProvider.generateAccessToken(
                authentication, user.getCompanyCode(),
                request.getWarehouseCode(), request.getCustomerCode());
        String refreshToken = tokenProvider.generateRefreshToken(principal.getEmail());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(accessTokenExpiration);
        response.setTenantSelected(true);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(principal.getUserId());
        userInfo.setDisplayName(principal.getDisplayName());
        userInfo.setEmail(principal.getEmail());
        userInfo.setCompanyCode(user.getCompanyCode());
        userInfo.setWarehouseCode(request.getWarehouseCode());
        userInfo.setCustomerCode(request.getCustomerCode());
        userInfo.setLocale(user.getLocale());
        userInfo.setRoles(tokenProvider.getRoles(accessToken));
        response.setUser(userInfo);

        return response;
    }

    /**
     * Refresh access token using a valid refresh token.
     * Preserves the current tenant context from the existing token.
     */
    public LoginResponse refresh(String refreshToken, String currentAccessToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new WmsBusinessException("AUTH_INVALID", "Invalid or expired refresh token");
        }

        String email = tokenProvider.getUsernameFromToken(refreshToken);
        WmsUser user = userRepository.findByEmailAndDelFlg(email, 0)
                .orElseThrow(() -> new WmsBusinessException("ERR0174", "User not found"));

        WmsUserPrincipal principal = new WmsUserPrincipal(
                user.getUserId(), user.getEmail(), user.getPasswordHash(),
                user.getDisplayName(), user.getIsActive(),
                Arrays.asList(user.getUserRole().split(","))
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        // Try to preserve warehouse/customer from existing access token
        String warehouseCode = null;
        String customerCode = null;
        boolean tenantSelected = false;
        if (currentAccessToken != null) {
            try {
                warehouseCode = tokenProvider.getWarehouseCode(currentAccessToken);
                customerCode = tokenProvider.getCustomerCode(currentAccessToken);
                tenantSelected = warehouseCode != null && customerCode != null;
            } catch (Exception ignored) {
                // Token might be expired, that's ok
            }
        }

        String newAccessToken = tokenProvider.generateAccessToken(
                auth, user.getCompanyCode(), warehouseCode, customerCode);
        String newRefreshToken = tokenProvider.generateRefreshToken(email);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(accessTokenExpiration);
        response.setTenantSelected(tenantSelected);

        if (tenantSelected) {
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
            userInfo.setUserId(user.getUserId());
            userInfo.setDisplayName(user.getDisplayName());
            userInfo.setEmail(user.getEmail());
            userInfo.setCompanyCode(user.getCompanyCode());
            userInfo.setWarehouseCode(warehouseCode);
            userInfo.setCustomerCode(customerCode);
            userInfo.setLocale(user.getLocale());
            userInfo.setRoles(tokenProvider.getRoles(newAccessToken));
            response.setUser(userInfo);
        }

        return response;
    }

    // ========================================================================
    // DEV BYPASS helpers — mock authentication for admin@wms.com
    // ========================================================================

    private LoginResponse buildDevBypassLoginResponse(LoginRequest request) {
        WmsUserPrincipal mockPrincipal = new WmsUserPrincipal(
                999L, DEV_BYPASS_EMAIL, "",
                "Dev Administrator", true,
                Arrays.asList("ADMIN")
        );

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                mockPrincipal, null, mockPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        String accessToken = tokenProvider.generateAccessToken(
                mockAuth, DEV_BYPASS_COMPANY, null, null);
        String refreshToken = tokenProvider.generateRefreshToken(DEV_BYPASS_EMAIL);

        // Fetch real tenant list from VGWH_TM_CUST
        List<TenantOption> tenants;
        try {
            tenants = customerMasterRepository.findTenantsByCompanyCode(DEV_BYPASS_COMPANY);
        } catch (Exception ex) {
            // Fallback to mock data if DB is not available
            logger.warn("Failed to query VGWH_TM_CUST for company {}: {}. Using mock data.", DEV_BYPASS_COMPANY, ex.getMessage(), ex);
            tenants = new ArrayList<>();
            tenants.add(new TenantOption("TK01", "muji", "MUJI Retail"));
            tenants.add(new TenantOption("TK01", "gga1", "GGA1 Corporation"));
            tenants.add(new TenantOption("TK01", "abcd", "ABCD Trading Co."));
            tenants.add(new TenantOption("OS01", "muji", "MUJI Retail"));
            tenants.add(new TenantOption("OS01", "xyz1", "XYZ Logistics"));
            tenants.add(new TenantOption("NG01", "gga1", "GGA1 Corporation"));
        }

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(accessTokenExpiration);
        response.setTenantSelected(false);
        response.setTenants(tenants);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(999L);
        userInfo.setDisplayName("Dev Administrator");
        userInfo.setEmail(DEV_BYPASS_EMAIL);
        userInfo.setCompanyCode(DEV_BYPASS_COMPANY);
        userInfo.setLocale(request.getLocale() != null ? request.getLocale() : "en");
        userInfo.setRoles(Arrays.asList("ADMIN"));
        response.setUser(userInfo);

        return response;
    }

    private LoginResponse buildDevBypassSelectTenantResponse(
            Authentication authentication, WmsUserPrincipal principal,
            TenantSelectionRequest request) {

        String accessToken = tokenProvider.generateAccessToken(
                authentication, DEV_BYPASS_COMPANY,
                request.getWarehouseCode(), request.getCustomerCode());
        String refreshToken = tokenProvider.generateRefreshToken(DEV_BYPASS_EMAIL);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(accessTokenExpiration);
        response.setTenantSelected(true);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setUserId(999L);
        userInfo.setDisplayName("Dev Administrator");
        userInfo.setEmail(DEV_BYPASS_EMAIL);
        userInfo.setCompanyCode(DEV_BYPASS_COMPANY);
        userInfo.setWarehouseCode(request.getWarehouseCode());
        userInfo.setCustomerCode(request.getCustomerCode());
        userInfo.setLocale("en");
        userInfo.setRoles(Arrays.asList("ADMIN"));
        response.setUser(userInfo);

        return response;
    }
}
