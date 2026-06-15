package jp.co.nittsu.gwh.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OracleSchemaStatementInspector implements StatementInspector, HibernatePropertiesCustomizer {

    private static final Pattern LEGACY_SCHEMA_PATTERN = Pattern.compile("(?i)\\b(?:SGWH0001|GWH)\\.");

    private final String configuredSchemaPrefix;

    public OracleSchemaStatementInspector(@Value("${gwh.oracle.schema:SGWH0001}") String configuredSchema) {
        String normalizedSchema = configuredSchema == null ? "" : configuredSchema.trim();
        this.configuredSchemaPrefix = normalizedSchema.isEmpty() ? "" : normalizedSchema + ".";
    }

    @Override
    public String inspect(String sql) {
        if (sql == null || configuredSchemaPrefix.isEmpty()) {
            return sql;
        }
        return LEGACY_SCHEMA_PATTERN.matcher(sql).replaceAll(Matcher.quoteReplacement(configuredSchemaPrefix));
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.statement_inspector", this);
    }
}
