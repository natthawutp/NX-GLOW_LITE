package jp.co.nittsu.gwh.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom UserDetails implementation carrying WMS-specific user info.
 */
public class WmsUserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String email;
    private final String password;
    private final String displayName;
    private final boolean active;
    private final List<GrantedAuthority> authorities;

    public WmsUserPrincipal(Long userId, String email, String password,
                            String displayName, boolean active,
                            List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.active = active;
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public Long getUserId() { return userId; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }
}
