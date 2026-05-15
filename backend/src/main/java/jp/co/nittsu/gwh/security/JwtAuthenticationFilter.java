package jp.co.nittsu.gwh.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT authentication filter.
 * Extracts JWT from Authorization header, validates it, and sets SecurityContext.
 * Also populates TenantContext with company/warehouse/customer from the token.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private TenantContext tenantContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                List<String> roles = tokenProvider.getRoles(jwt);

                // Reconstruct WmsUserPrincipal from JWT claims so downstream
                // code (e.g. AuthService.selectTenant) can cast correctly.
                Long userId = tokenProvider.getUserId(jwt);
                String displayName = tokenProvider.getDisplayName(jwt);

                // Strip "ROLE_" prefix for constructor, authorities will re-add it
                List<String> cleanRoles = roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                        .collect(Collectors.toList());

                WmsUserPrincipal principal = new WmsUserPrincipal(
                        userId, username, "", displayName, true, cleanRoles);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Set tenant context from JWT claims
                tenantContext.setCompanyCode(tokenProvider.getCompanyCode(jwt));
                tenantContext.setWarehouseCode(tokenProvider.getWarehouseCode(jwt));
                tenantContext.setCustomerCode(tokenProvider.getCustomerCode(jwt));
                tenantContext.setUserId(tokenProvider.getUsernameFromToken(jwt));
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
