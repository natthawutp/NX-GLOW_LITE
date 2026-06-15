package jp.co.nittsu.gwh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

@Component
public class OracleObjectNameResolver {

    private final String configuredSchema;
    private final String defaultSchema;

    public OracleObjectNameResolver(
            @Value("${gwh.oracle.schema:SGWH0001}") String configuredSchema,
            @Value("${spring.jpa.properties.hibernate.default_schema:}") String defaultSchema) {
        this.configuredSchema = normalizeSchema(configuredSchema);
        this.defaultSchema = normalizeSchema(defaultSchema);
    }

    public String[] preferredCandidates(String... objectNames) {
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        if (objectNames == null) {
            return new String[0];
        }

        for (String objectName : objectNames) {
            String normalizedObjectName = normalizeObjectName(objectName);
            if (normalizedObjectName == null) {
                continue;
            }
            addQualifiedCandidate(candidates, configuredSchema, normalizedObjectName);
            addQualifiedCandidate(candidates, defaultSchema, normalizedObjectName);
            candidates.add(normalizedObjectName);
        }
        return candidates.toArray(new String[0]);
    }

    private void addQualifiedCandidate(LinkedHashSet<String> candidates, String schema, String objectName) {
        if (schema == null || objectName == null) {
            return;
        }
        candidates.add(schema + "." + objectName);
    }

    private String normalizeSchema(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private String normalizeObjectName(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
