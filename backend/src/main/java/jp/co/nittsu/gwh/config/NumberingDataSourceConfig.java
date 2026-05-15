package jp.co.nittsu.gwh.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Provides an ISOLATED connection pool dedicated solely to GWH_TM_NUM
 * sequence number generation.
 *
 * WHY THIS IS NECESSARY:
 *   The legacy GwhCmnumBLC uses @PersistenceUnit(unitName = "NON_JTA") — a
 *   separate persistence unit with its own JDBC pool that is NEVER registered
 *   with the JTA/Spring transaction manager.  This guarantees that
 *   tran.commit() on that pool's connection always commits to Oracle
 *   independently, even when called from inside a JTA/Spring transaction.
 *
 *   In Spring Boot there is only one auto-configured DataSource, which Spring's
 *   JpaTransactionManager binds to the current thread when @Transactional is
 *   active.  A second, qualifier-scoped DataSource created here replicates the
 *   legacy NON_JTA pool exactly: it is invisible to Spring's transaction
 *   machinery, so conn.commit() is always a real Oracle COMMIT.
 */
@Configuration
public class NumberingDataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Dedicated, isolated HikariCP pool used only by WmsNumberingService.
     * Not registered with any Spring PlatformTransactionManager.
     */
    @Bean("numberingDataSource")
    public DataSource numberingDataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setDriverClassName("oracle.jdbc.OracleDriver");
        // Small pool — numbering is a single-row lock operation
        cfg.setMaximumPoolSize(5);
        cfg.setMinimumIdle(1);
        cfg.setConnectionTimeout(30_000);
        // Keep autoCommit=false by default so every borrow starts a clean transaction
        cfg.setAutoCommit(false);
        cfg.setPoolName("GWH-Numbering-Pool");
        // Validate connection health
        cfg.setConnectionTestQuery("SELECT 1 FROM DUAL");
        return new HikariDataSource(cfg);
    }
}
