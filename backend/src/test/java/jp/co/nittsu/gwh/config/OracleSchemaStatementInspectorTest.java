package jp.co.nittsu.gwh.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OracleSchemaStatementInspectorTest {

    @Test
    void inspectRewritesLegacySchemaPrefixesToConfiguredSchema() {
        OracleSchemaStatementInspector inspector = new OracleSchemaStatementInspector("ALTSCHEMA");

        String sql = "SELECT * FROM SGWH0001.GWH_TJ_ST s LEFT JOIN GWH.GWH_TM_TRN t ON t.TRN_COD = s.ST_TRN_COD";

        assertEquals(
                "SELECT * FROM ALTSCHEMA.GWH_TJ_ST s LEFT JOIN ALTSCHEMA.GWH_TM_TRN t ON t.TRN_COD = s.ST_TRN_COD",
                inspector.inspect(sql)
        );
    }

    @Test
    void inspectLeavesSqlUnchangedWhenNoLegacySchemaPrefixExists() {
        OracleSchemaStatementInspector inspector = new OracleSchemaStatementInspector("ALTSCHEMA");

        assertEquals(
                "SELECT * FROM GWH_TJ_ST",
                inspector.inspect("SELECT * FROM GWH_TJ_ST")
        );
    }
}
