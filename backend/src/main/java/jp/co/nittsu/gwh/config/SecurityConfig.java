package jp.co.nittsu.gwh.config;

import jp.co.nittsu.gwh.security.JwtAuthenticationFilter;
import jp.co.nittsu.gwh.security.JwtAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration.
 * Stateless JWT-based authentication with role-based access control.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint)
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Public endpoints
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/api-docs/**", "/api/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/*.js", "/*.css", "/*.ico", "/*.json",
                        "/assets/**", "/favicon.ico").permitAll()
                // Dashboard
                .antMatchers("/api/v1/dashboard/**").authenticated()
                // Module-specific authorization
                .antMatchers("/api/v1/inbound/**").hasAnyRole("ADMIN", "INBOUND", "OPERATOR")
                .antMatchers("/api/v1/outbound/**").hasAnyRole("ADMIN", "OUTBOUND", "OPERATOR")
                .antMatchers("/api/v1/inventory/**").hasAnyRole("ADMIN", "INVENTORY", "OPERATOR")
                .antMatchers("/api/v1/master/**").hasAnyRole("ADMIN", "MASTER")
                .antMatchers("/api/v1/reports/**").hasAnyRole("ADMIN", "REPORTS", "OPERATOR")
                // All other API requests require authentication
                .antMatchers("/api/**").authenticated()
                // Allow Angular routes
                .anyRequest().permitAll();

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
