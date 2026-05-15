package jp.co.nittsu.gwh.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class WarehouseOptimizeDataSourceConfig {

    @Value("${warehouse-optimize.datasource.url}")
    private String url;

    @Value("${warehouse-optimize.datasource.username}")
    private String username;

    @Value("${warehouse-optimize.datasource.password}")
    private String password;

    @Value("${warehouse-optimize.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;

    @Value("${warehouse-optimize.datasource.hikari.maximum-pool-size:10}")
    private int maximumPoolSize;

    @Value("${warehouse-optimize.datasource.hikari.minimum-idle:1}")
    private int minimumIdle;

    @Value("${warehouse-optimize.datasource.hikari.connection-timeout:30000}")
    private long connectionTimeout;

    @Bean("warehouseOptimizeDataSource")
    public DataSource warehouseOptimizeDataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName(driverClassName);
        cfg.setMaximumPoolSize(maximumPoolSize);
        cfg.setMinimumIdle(minimumIdle);
        cfg.setConnectionTimeout(connectionTimeout);
        cfg.setPoolName("WarehouseOptimize-Pool");
        cfg.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(cfg);
    }

    @Bean("warehouseOptimizeJdbcTemplate")
    public JdbcTemplate warehouseOptimizeJdbcTemplate(
            @Qualifier("warehouseOptimizeDataSource") DataSource warehouseOptimizeDataSource) {
        return new JdbcTemplate(warehouseOptimizeDataSource);
    }

    @Bean("warehouseOptimizeNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate warehouseOptimizeNamedParameterJdbcTemplate(
            @Qualifier("warehouseOptimizeDataSource") DataSource warehouseOptimizeDataSource) {
        return new NamedParameterJdbcTemplate(warehouseOptimizeDataSource);
    }
}
