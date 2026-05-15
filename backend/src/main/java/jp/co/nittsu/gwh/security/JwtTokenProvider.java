package jp.co.nittsu.gwh.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT token provider for generating and validating access/refresh tokens.
 * Embeds tenant context (company, warehouse, customer) into token claims.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${gwh.jwt.secret}")
    private String jwtSecret;

    @Value("${gwh.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${gwh.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Value("${gwh.jwt.issuer}")
    private String issuer;

    private Key signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate access token with user details and tenant context.
     */
    public String generateAccessToken(Authentication authentication, String companyCode,
                                       String warehouseCode, String customerCode) {
        WmsUserPrincipal userPrincipal = (WmsUserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("userId", userPrincipal.getUserId())
                .claim("displayName", userPrincipal.getDisplayName())
                .claim("companyCode", companyCode)
                .claim("warehouseCode", warehouseCode)
                .claim("customerCode", customerCode)
                .claim("roles", roles)
                .claim("tokenType", "ACCESS")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generate refresh token (longer-lived, minimal claims).
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("tokenType", "REFRESH")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public Long getUserId(String token) {
        Object val = parseToken(token).get("userId");
        if (val instanceof Number) return ((Number) val).longValue();
        return null;
    }

    public String getDisplayName(String token) {
        return (String) parseToken(token).get("displayName");
    }

    public String getCompanyCode(String token) {
        return (String) parseToken(token).get("companyCode");
    }

    public String getWarehouseCode(String token) {
        return (String) parseToken(token).get("warehouseCode");
    }

    public String getCustomerCode(String token) {
        return (String) parseToken(token).get("customerCode");
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) parseToken(token).get("roles");
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
