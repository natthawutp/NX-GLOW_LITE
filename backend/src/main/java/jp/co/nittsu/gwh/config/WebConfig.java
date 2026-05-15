package jp.co.nittsu.gwh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration.
 * Handles CORS, static resources (Angular SPA), and SPA fallback routing.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve Angular static assets from webapp root (WAR deployment)
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("/assets/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // SPA fallback: forward Angular routes to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/index.html");
        registry.addViewController("/select-tenant").setViewName("forward:/index.html");
        registry.addViewController("/dashboard").setViewName("forward:/index.html");
        registry.addViewController("/inbound/**").setViewName("forward:/index.html");
        registry.addViewController("/outbound/**").setViewName("forward:/index.html");
        registry.addViewController("/inventory/**").setViewName("forward:/index.html");
        registry.addViewController("/master/**").setViewName("forward:/index.html");
        registry.addViewController("/reports/**").setViewName("forward:/index.html");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
