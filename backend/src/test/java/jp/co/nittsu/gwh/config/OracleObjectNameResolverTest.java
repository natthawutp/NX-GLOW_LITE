package jp.co.nittsu.gwh.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OracleObjectNameResolverTest {

    @Test
    void preferredCandidatesIncludeConfiguredSchemaDefaultSchemaAndUnqualifiedFallback() {
        OracleObjectNameResolver resolver = new OracleObjectNameResolver("SGWH0001", "GWH");

        assertArrayEquals(
                new String[] {
                        "SGWH0001.GWH_TJ_ST",
                        "GWH.GWH_TJ_ST",
                        "GWH_TJ_ST",
                        "SGWH0001.VGWH_TJ_ST",
                        "GWH.VGWH_TJ_ST",
                        "VGWH_TJ_ST"
                },
                resolver.preferredCandidates("GWH_TJ_ST", "VGWH_TJ_ST")
        );
    }

    @Test
    void preferredCandidatesAvoidDuplicateSchemaEntries() {
        OracleObjectNameResolver resolver = new OracleObjectNameResolver("SGWH0001", "SGWH0001");

        assertArrayEquals(
                new String[] {
                        "SGWH0001.GWH_TM_LOC",
                        "GWH_TM_LOC"
                },
                resolver.preferredCandidates("GWH_TM_LOC")
        );
    }
}
