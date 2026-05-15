package jp.co.nittsu.gwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * GWH Modern WMS Application
 * 
 * Enterprise Warehouse Management System
 * Deployable as WAR on WebLogic or standalone with embedded Tomcat.
 */
@SpringBootApplication
public class GwhModernWmsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GwhModernWmsApplication.class)
                      .profiles("weblogic");
    }

    public static void main(String[] args) {
        SpringApplication.run(GwhModernWmsApplication.class, args);
    }
}
