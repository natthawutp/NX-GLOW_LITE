package jp.co.nittsu.gwh.module.auth.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.auth.dto.LoginRequest;
import jp.co.nittsu.gwh.module.auth.dto.LoginResponse;
import jp.co.nittsu.gwh.module.auth.dto.TenantSelectionRequest;
import jp.co.nittsu.gwh.module.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Authentication REST controller.
 * Two-step login flow:
 *   1. POST /login      – email + password → temp token + tenant list
 *   2. POST /select-tenant – chosen warehouse + customer → final token
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @PostMapping("/select-tenant")
    public ResponseEntity<ApiResponse<LoginResponse>> selectTenant(
            @Valid @RequestBody TenantSelectionRequest request) {
        LoginResponse loginResponse = authService.selectTenant(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        String refreshToken = request.get("refreshToken");
        // Extract current access token to preserve tenant context
        String authHeader = httpRequest.getHeader("Authorization");
        String currentAccessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            currentAccessToken = authHeader.substring(7);
        }
        LoginResponse loginResponse = authService.refresh(refreshToken, currentAccessToken);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // JWT is stateless - client simply discards token
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
