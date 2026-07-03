package jp.co.nittsu.gwh.module.warehouseoptimize.service;

import jp.co.nittsu.gwh.module.auth.dto.TenantOption;
import jp.co.nittsu.gwh.module.auth.repository.CustomerMasterRepository;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeRequests;
import jp.co.nittsu.gwh.module.warehouseoptimize.repository.WarehouseOptimizeRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseOptimizeServiceTest {

    @Mock
    private WarehouseOptimizeRepository repository;

    @Mock
    private CustomerMasterRepository customerMasterRepository;

    @Mock
    private TenantContext tenantContext;

    @InjectMocks
    private WarehouseOptimizeService service;

    @BeforeEach
    void setUp() {
        when(tenantContext.getCompanyCode()).thenReturn("520010");
        when(tenantContext.getWarehouseCode()).thenReturn("BKK");
        lenient().when(tenantContext.getCustomerCode()).thenReturn("TESA");

        lenient().when(customerMasterRepository.findTenantsByCompanyCode("520010")).thenReturn(Collections.singletonList(
                new TenantOption("BKK", "TESA", "TESA TAPE (THAILAND) LTD.")
        ));
    }

    @Test
    void syncStockIncludesEmptyLocationsAndUsesVelocityColorPrecedence() {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = profileDetail();
        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());
        when(repository.findProfileDetail(135L)).thenReturn(profile);
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(null))).thenReturn(Collections.singletonList(
                stockRow("LOC-A", "SKU-A", "11", "9", LocalDateTime.of(2026, 5, 15, 10, 0, 0))
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.singletonMap("SKU-A", product("SKU-A", "Fallback Category")));
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.singletonList(
                assignment("LOC-A", "SKU-A", "B", "Assigned Category")
        ));

        WarehouseOptimizeModels.StockSyncSnapshot snapshot = service.syncStock(135L, "TESA", null);

        assertEquals(135L, snapshot.getProfileId());
        assertEquals("TESA", snapshot.getCustomerCode());
        assertEquals(2, snapshot.getLocations().size());
        WarehouseOptimizeModels.LiveLocationState mapped = snapshot.getLocations().stream()
                .filter(item -> "LOC-A".equals(item.getLocation()))
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals("velocity-b", mapped.getColorClass());
        assertEquals("Assigned Category", mapped.getProductCategory());
        assertEquals(new BigDecimal("11"), mapped.getPhysicalQty());
        assertEquals(Collections.singletonList("LOC-B"), snapshot.getDiagnostics().getUnmatchedLayoutLocations());
    }

    @Test
    void loadStockDeltaMarksChangedLocationsBlinkingAndUsesCategoryFallback() {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = profileDetail();
        LocalDateTime previousCursor = LocalDateTime.of(2026, 5, 15, 10, 5, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 5, 15, 10, 6, 0);

        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());
        when(repository.findProfileDetail(135L)).thenReturn(profile);
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(previousCursor))).thenReturn(Arrays.asList(
                stockRow("LOC-B", "SKU-B", "4", "3", updatedAt),
                stockRow("UNMAPPED-01", "SKU-Z", "1", "1", updatedAt)
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.singletonMap("SKU-B", product("SKU-B", "Tape")));
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.emptyList());

        WarehouseOptimizeModels.StockDelta delta = service.loadStockDelta(135L, "TESA", previousCursor, null);

        assertEquals(previousCursor, delta.getPreviousCursor());
        assertEquals(updatedAt, delta.getCursor());
        assertEquals(1, delta.getChangedLocations().size());
        WarehouseOptimizeModels.LiveLocationState changed = delta.getChangedLocations().get(0);
        assertEquals("LOC-B", changed.getLocation());
        assertTrue(changed.isBlink());
        assertTrue(changed.getColorClass().startsWith("category-"));
        assertEquals(Collections.singletonList("UNMAPPED-01"), delta.getDiagnostics().getUnmatchedStockLocations());
        assertTrue(delta.getDiagnostics().getUnmatchedLayoutLocations().isEmpty());
    }

    @Test
    void rejectsUnauthorizedCustomerFilter() {
        when(customerMasterRepository.findTenantsByCompanyCode("520010")).thenReturn(Collections.singletonList(
                new TenantOption("BKK", "OTHER", "Other Customer")
        ));
        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());

        try {
            service.syncStock(135L, "TESA", null);
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("not authorized"));
            return;
        }
        throw new AssertionError("Expected unauthorized customer filter exception");
    }

    @Test
    void syncStockUsesLayoutOverrideWhenDraftIsOpen() {
        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(null))).thenReturn(Collections.singletonList(
                stockRow("DRAFT-LOC", "SKU-A", "5", "4", LocalDateTime.of(2026, 5, 15, 10, 0, 0))
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.emptyMap());
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.emptyList());
        when(repository.toJson(any())).thenReturn("{\"aisles\":[{\"locations\":[{\"location\":\"DRAFT-LOC\",\"zone\":\"ZONE-1\",\"type\":\"HIGH_RACK\",\"level\":1,\"x\":0,\"y\":0}]}]}");
        when(repository.parseLocations(anyString())).thenReturn(Collections.singletonList(
                layoutLocation("DRAFT-LOC", "ZONE-1", 1, "0", "0")
        ));

        Map<String, Object> layoutOverride = new HashMap<>();
        List<Map<String, Object>> aisles = new ArrayList<>();
        Map<String, Object> aisle = new HashMap<>();
        List<Map<String, Object>> locations = new ArrayList<>();
        Map<String, Object> location = new HashMap<>();
        location.put("location", "DRAFT-LOC");
        location.put("zone", "ZONE-1");
        location.put("type", "HIGH_RACK");
        location.put("level", 1);
        location.put("x", 0);
        location.put("y", 0);
        locations.add(location);
        aisle.put("locations", locations);
        aisles.add(aisle);
        layoutOverride.put("aisles", aisles);

        WarehouseOptimizeModels.StockSyncSnapshot snapshot = service.syncStock(135L, "TESA", layoutOverride);

        assertEquals(1, snapshot.getLocations().size());
        assertEquals("DRAFT-LOC", snapshot.getLocations().get(0).getLocation());
        assertEquals(new BigDecimal("5"), snapshot.getLocations().get(0).getPhysicalQty());
        assertTrue(snapshot.getDiagnostics().getUnmatchedLayoutLocations().isEmpty());
        assertTrue(snapshot.getDiagnostics().getUnmatchedStockLocations().isEmpty());
        verify(repository, never()).findProfileDetail(135L);
    }

    @Test
    void loadStockDeltaShortCircuitsWhenNoOracleChangesExist() {
        LocalDateTime previousCursor = LocalDateTime.of(2026, 5, 15, 10, 5, 0);

        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(previousCursor))).thenReturn(Collections.emptyList());
        when(repository.toJson(any())).thenReturn("{\"aisles\":[{\"locations\":[{\"location\":\"DRAFT-LOC\",\"zone\":\"ZONE-1\",\"type\":\"HIGH_RACK\",\"level\":1,\"x\":0,\"y\":0}]}]}");
        when(repository.parseLocations(anyString())).thenReturn(Collections.singletonList(
                layoutLocation("DRAFT-LOC", "ZONE-1", 1, "0", "0")
        ));

        WarehouseOptimizeModels.StockDelta delta = service.loadStockDelta(135L, "TESA", previousCursor, Collections.singletonMap("aisles", Collections.emptyList()));

        assertEquals(previousCursor, delta.getCursor());
        assertTrue(delta.getChangedLocations().isEmpty());
        verify(repository, never()).findProfileDetail(135L);
        verify(repository, never()).loadProductsBySku(anyString(), anyString(), anyString());
    }

    @Test
    void saveProfileReturnsLeanDetailWithoutReloadingParsedLocations() {
        WarehouseOptimizeRequests.SaveProfileRequest request = new WarehouseOptimizeRequests.SaveProfileRequest();
        request.setProfileName("MUJI Layout");
        request.setDescription("Generated layout");
        request.setWarehouseLength(new BigDecimal("423"));
        request.setWarehouseWidth(new BigDecimal("1655"));
        request.setLayoutData(Collections.singletonMap("aisles", Collections.emptyList()));

        WarehouseOptimizeModels.WarehouseProfileSummary savedSummary = profileSummary();
        savedSummary.setProfileName("MUJI Layout");
        savedSummary.setDescription("Generated layout");
        savedSummary.setWarehouseLength(new BigDecimal("423"));
        savedSummary.setWarehouseWidth(new BigDecimal("1655"));
        savedSummary.setUpdatedAt(LocalDateTime.of(2026, 6, 17, 11, 0, 0));

        when(repository.toJson(any())).thenReturn("{\"aisles\":[]}");
        when(repository.saveProfile(eq("520010"), eq("BKK"), eq(request), eq("{\"aisles\":[]}"))).thenReturn(135L);
        when(repository.findProfileSummary(135L)).thenReturn(savedSummary);

        WarehouseOptimizeModels.WarehouseProfileDetail response = service.saveProfile(request);

        assertEquals(135L, response.getId());
        assertEquals("{\"aisles\":[]}", response.getLayoutData());
        assertTrue(response.getLocations().isEmpty());
        assertEquals(savedSummary.getUpdatedAt(), response.getUpdatedAt());
        verify(repository, never()).findProfileDetail(135L);
    }

    @Test
    void workingStatusSnapshotCombinesInboundAndOutboundMetrics() {
        when(repository.loadInboundStatusAggregateRows("520010", "BKK", "TESA")).thenReturn(Arrays.asList(
                statusAggregateRow("200", 12, "45", "320"),
                statusAggregateRow("205", 3, "8", "64")
        ));
        when(repository.loadOutboundStatusAggregateRows("520010", "BKK", "TESA")).thenReturn(Collections.singletonList(
                statusAggregateRow("500", 7, "11", "90")
        ));

        WarehouseOptimizeModels.WorkingStatusSnapshotResponse response = service.loadWorkingStatusSnapshot("TESA");

        assertNotNull(response.getGeneratedAt());
        assertFalse(response.getStatuses().isEmpty());

        WarehouseOptimizeModels.WorkingStatusSnapshotItem inboundLocated = response.getStatuses().stream()
                .filter(item -> "INBOUND".equals(item.getFlow()) && "200".equals(item.getStatusCode()))
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals("Located", inboundLocated.getStatusLabel());
        assertEquals(12, inboundLocated.getOrderCount());
        assertEquals(15, inboundLocated.getTotalOrders());
        assertEquals(0, new BigDecimal("45").compareTo(inboundLocated.getCsQty()));
        assertEquals(0, new BigDecimal("320").compareTo(inboundLocated.getPcsQty()));

        WarehouseOptimizeModels.WorkingStatusSnapshotItem outboundPicking = response.getStatuses().stream()
                .filter(item -> "OUTBOUND".equals(item.getFlow()) && "500".equals(item.getStatusCode()))
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals("Picking", outboundPicking.getStatusLabel());
        assertEquals(7, outboundPicking.getOrderCount());
        assertEquals(7, outboundPicking.getTotalOrders());
        assertEquals(0, new BigDecimal("11").compareTo(outboundPicking.getCsQty()));
        assertEquals(0, new BigDecimal("90").compareTo(outboundPicking.getPcsQty()));

        WarehouseOptimizeModels.WorkingStatusSnapshotItem outboundAllocated = response.getStatuses().stream()
                .filter(item -> "OUTBOUND".equals(item.getFlow()) && "300".equals(item.getStatusCode()))
                .findFirst()
                .orElseThrow(AssertionError::new);
        assertEquals(0, outboundAllocated.getOrderCount());
        assertEquals(7, outboundAllocated.getTotalOrders());
    }

    @Test
    void autoGenerateLayoutCreatesMultipleAreasWithCanonicalLocations() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("L-A-001", "A01", "Rack A", "R1", "01", "1", "1.4", "1.2", "2.0"),
                locationMasterRow("L-B-001", "B02", "Rack B", "R2", "02", "3", "1.6", "1.4", "2.2")
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();

        assertEquals(2, response.getSummary().getAreas());
        assertEquals(2, response.getSummary().getRacks());
        assertEquals(2, response.getSummary().getLocations());
        assertEquals(2, response.getLayout().getAisles().size());
        assertEquals(0, response.getSummary().getNormalizedDimensionRows());
        assertEquals(0, response.getSummary().getMixedAreasDetected());
        assertEquals(0, response.getSummary().getMixedAreasUncertain());
        assertEquals(0, response.getSummary().getWarehouseWidth().compareTo(response.getLayout().getWarehouseWidth()));
        assertEquals(0, response.getSummary().getWarehouseHeight().compareTo(response.getLayout().getWarehouseHeight()));
        assertEquals("A01", response.getLayout().getAisles().get(0).getZone());
        assertEquals("B02", response.getLayout().getAisles().get(1).getZone());
        assertEquals("A01R1011", response.getLayout().getAisles().get(0).getLocations().get(0).getLocation());
        assertEquals("B02R2023", response.getLayout().getAisles().get(1).getLocations().get(0).getLocation());
        assertNull(response.getLayout().getBoundaryPolygon());
    }

    @Test
    void autoGenerateLayoutAppliesMissingStructureFallbacksAndDimensionDefaults() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("LOC-001", "FLOOR-A", "Floor Area", null, null, null, null, null, null),
                locationMasterRow("LOC-002", "FLOOR-A", "Floor Area", null, null, "2", "0", "0", null)
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(1, response.getSummary().getAreas());
        assertEquals(1, response.getSummary().getRacks());
        assertEquals(2, response.getSummary().getLocations());
        assertEquals(2, response.getSummary().getInferredPositions());
        assertEquals(1, response.getSummary().getInferredLevels());
        assertEquals("FLOOR", aisle.getType());
        assertEquals(0, new BigDecimal("1.0").compareTo(aisle.getBayDepth()));
        assertEquals(0, new BigDecimal("1.2").compareTo(aisle.getBayWidth()));
        assertEquals("FLOOR-A_NO_RACKAUTO_PSTN_000011", aisle.getLocations().get(0).getLocation());
        assertEquals("FLOOR-A_NO_RACKAUTO_PSTN_000022", aisle.getLocations().get(1).getLocation());
    }

    @Test
    void autoGenerateLayoutPacksAreasWithoutOverlap() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("A-001", "A", null, "R1", "01", "1", "1.0", "1.2", null),
                locationMasterRow("A-002", "A", null, "R2", "01", "1", "1.0", "1.2", null),
                locationMasterRow("B-001", "B", null, "R1", "01", "1", "1.0", "1.2", null),
                locationMasterRow("C-001", "C", null, "R1", "01", "1", "1.0", "1.2", null),
                locationMasterRow("D-001", "D", null, "R1", "01", "1", "1.0", "1.2", null)
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();

        assertTrue(response.getLayout().getWarehouseWidth().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(response.getLayout().getWarehouseHeight().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(Arrays.asList("A", "B", "C", "D"),
                Arrays.asList(
                        response.getLayout().getAisles().get(0).getZone(),
                        response.getLayout().getAisles().get(1).getZone(),
                        response.getLayout().getAisles().get(2).getZone(),
                        response.getLayout().getAisles().get(3).getZone()));

        for (int left = 0; left < response.getLayout().getAisles().size(); left++) {
            WarehouseOptimizeModels.WarehouseAisle leftAisle = response.getLayout().getAisles().get(left);
            for (int right = left + 1; right < response.getLayout().getAisles().size(); right++) {
                WarehouseOptimizeModels.WarehouseAisle rightAisle = response.getLayout().getAisles().get(right);
                assertFalse(overlaps(leftAisle, rightAisle),
                        "Expected packed areas not to overlap: " + leftAisle.getZone() + " vs " + rightAisle.getZone());
            }
        }
    }

    @Test
    void autoGenerateLayoutNormalizesDimensionsAndDetectsMixedShelfLevels() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("MIX-001", "MIX-A", "Mixed Area", "R1", "01", "1", "120", "100", "0.3"),
                locationMasterRow("MIX-002", "MIX-A", "Mixed Area", "R1", "01", "2", "120", "100", "0.3"),
                locationMasterRow("MIX-003", "MIX-A", "Mixed Area", "R1", "01", "3", "120", "100", "1.2"),
                locationMasterRow("MIX-004", "MIX-A", "Mixed Area", "R1", "01", "4", "120", "100", "1.2"),
                locationMasterRow("MIX-005", "MIX-A", "Mixed Area", "R2", "01", "1", "120", "100", "0.3"),
                locationMasterRow("MIX-006", "MIX-A", "Mixed Area", "R2", "01", "2", "120", "100", "0.3"),
                locationMasterRow("MIX-007", "MIX-A", "Mixed Area", "R2", "01", "3", "120", "100", "1.2"),
                locationMasterRow("MIX-008", "MIX-A", "Mixed Area", "R2", "01", "4", "120", "100", "1.2")
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(0, new BigDecimal("1.2").compareTo(aisle.getBayDepth()));
        assertEquals(0, new BigDecimal("1").compareTo(aisle.getBayWidth()));
        assertEquals(2, aisle.getLowerShelfLevels());
        assertEquals(8, response.getSummary().getNormalizedDimensionRows());
        assertEquals(0, response.getSummary().getNormalizedHeightRows());
        assertEquals(1, response.getSummary().getMixedAreasDetected());
        assertEquals(0, response.getSummary().getMixedAreasUncertain());
        assertEquals(4, aisle.getLevels());
        assertEquals("SHELF", aisle.getLocations().get(0).getType());
        assertEquals("SHELF", aisle.getLocations().get(1).getType());
        assertEquals("HIGH_RACK", aisle.getLocations().get(2).getType());
        assertTrue(response.getWarnings().stream().anyMatch(message -> message.contains("normalized LOC_LEN or LOC_WID")));
    }

    @Test
    void autoGenerateLayoutDetectsMixedShelfLevelsFromLowerLevelLocationDensity() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("DENS-001", "MIX-C", "Mixed Area", "R1", "01", "1", "1.2", "1.0", null),
                locationMasterRow("DENS-002", "MIX-C", "Mixed Area", "R1", "02", "1", "1.2", "1.0", null),
                locationMasterRow("DENS-003", "MIX-C", "Mixed Area", "R1", "01", "2", "1.2", "1.0", null),
                locationMasterRow("DENS-004", "MIX-C", "Mixed Area", "R1", "02", "2", "1.2", "1.0", null),
                locationMasterRow("DENS-005", "MIX-C", "Mixed Area", "R1", "01", "3", "1.2", "1.0", null),
                locationMasterRow("DENS-006", "MIX-C", "Mixed Area", "R1", "01", "4", "1.2", "1.0", null),
                locationMasterRow("DENS-007", "MIX-C", "Mixed Area", "R2", "01", "1", "1.2", "1.0", null),
                locationMasterRow("DENS-008", "MIX-C", "Mixed Area", "R2", "02", "1", "1.2", "1.0", null),
                locationMasterRow("DENS-009", "MIX-C", "Mixed Area", "R2", "01", "2", "1.2", "1.0", null),
                locationMasterRow("DENS-010", "MIX-C", "Mixed Area", "R2", "02", "2", "1.2", "1.0", null),
                locationMasterRow("DENS-011", "MIX-C", "Mixed Area", "R2", "01", "3", "1.2", "1.0", null),
                locationMasterRow("DENS-012", "MIX-C", "Mixed Area", "R2", "01", "4", "1.2", "1.0", null)
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(2, aisle.getLowerShelfLevels());
        assertEquals(1, response.getSummary().getMixedAreasDetected());
        assertEquals(0, response.getSummary().getMixedAreasUncertain());
        assertEquals(0, response.getSummary().getNormalizedHeightRows());
        assertEquals(0, new BigDecimal("1").compareTo(aisle.getHeight().min(aisle.getWidth())));
        assertTrue(aisle.getLocations().stream()
                .filter(location -> location.getLevel() <= 2)
                .allMatch(location -> "SHELF".equals(location.getType())));
        assertTrue(aisle.getLocations().stream()
                .filter(location -> location.getLevel() >= 3)
                .allMatch(location -> "HIGH_RACK".equals(location.getType())));
        assertTrue(aisle.getLocations().stream().anyMatch(location -> location.getFootprintDepth() != null || location.getFootprintWidth() != null));
    }

    @Test
    void autoGenerateLayoutLeavesMixedLevelsUnchangedWhenLocationDensityConflicts() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("CONF-001", "MIX-D", "Mixed Area", "R1", "01", "1", "1.2", "1.0", null),
                locationMasterRow("CONF-002", "MIX-D", "Mixed Area", "R1", "02", "1", "1.2", "1.0", null),
                locationMasterRow("CONF-003", "MIX-D", "Mixed Area", "R1", "01", "2", "1.2", "1.0", null),
                locationMasterRow("CONF-004", "MIX-D", "Mixed Area", "R1", "02", "2", "1.2", "1.0", null),
                locationMasterRow("CONF-005", "MIX-D", "Mixed Area", "R1", "01", "3", "1.2", "1.0", null),
                locationMasterRow("CONF-006", "MIX-D", "Mixed Area", "R1", "01", "4", "1.2", "1.0", null),
                locationMasterRow("CONF-007", "MIX-D", "Mixed Area", "R2", "01", "1", "1.2", "1.0", null),
                locationMasterRow("CONF-008", "MIX-D", "Mixed Area", "R2", "01", "2", "1.2", "1.0", null),
                locationMasterRow("CONF-009", "MIX-D", "Mixed Area", "R2", "01", "3", "1.2", "1.0", null),
                locationMasterRow("CONF-010", "MIX-D", "Mixed Area", "R2", "02", "3", "1.2", "1.0", null),
                locationMasterRow("CONF-011", "MIX-D", "Mixed Area", "R2", "01", "4", "1.2", "1.0", null),
                locationMasterRow("CONF-012", "MIX-D", "Mixed Area", "R2", "02", "4", "1.2", "1.0", null)
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(0, aisle.getLowerShelfLevels());
        assertEquals(0, response.getSummary().getMixedAreasDetected());
        assertEquals(1, response.getSummary().getMixedAreasUncertain());
        assertTrue(response.getWarnings().stream().anyMatch(message -> message.contains("not clear enough")));
        assertTrue(aisle.getLocations().stream().allMatch(location -> "HIGH_RACK".equals(location.getType())));
    }

    @Test
    void autoGenerateLayoutLeavesMixedLevelsUnchangedWhenHeightSignalConflicts() {
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("MIX-001", "MIX-B", "Mixed Area", "R1", "01", "1", "1.2", "1.0", "0.3"),
                locationMasterRow("MIX-002", "MIX-B", "Mixed Area", "R1", "01", "2", "1.2", "1.0", "0.3"),
                locationMasterRow("MIX-003", "MIX-B", "Mixed Area", "R1", "01", "3", "1.2", "1.0", "1.2"),
                locationMasterRow("MIX-004", "MIX-B", "Mixed Area", "R1", "01", "4", "1.2", "1.0", "1.2"),
                locationMasterRow("MIX-005", "MIX-B", "Mixed Area", "R2", "01", "1", "1.2", "1.0", "1.2"),
                locationMasterRow("MIX-006", "MIX-B", "Mixed Area", "R2", "01", "2", "1.2", "1.0", "1.2"),
                locationMasterRow("MIX-007", "MIX-B", "Mixed Area", "R2", "01", "3", "1.2", "1.0", "1.2"),
                locationMasterRow("MIX-008", "MIX-B", "Mixed Area", "R2", "01", "4", "1.2", "1.0", "1.2")
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(0, aisle.getLowerShelfLevels());
        assertEquals(0, response.getSummary().getMixedAreasDetected());
        assertEquals(1, response.getSummary().getMixedAreasUncertain());
        assertTrue(response.getWarnings().stream().anyMatch(message -> message.contains("not clear enough")));
        assertTrue(aisle.getLocations().stream().allMatch(location -> "HIGH_RACK".equals(location.getType())));
    }

    @Test
    void autoGenerateLayoutWrapsVeryWideRackRunsIntoCompactBlocks() {
        List<WarehouseOptimizeModels.OracleLocationMasterRow> rows = new ArrayList<>();
        for (int rack = 1; rack <= 40; rack++) {
            rows.add(locationMasterRow(
                    "WIDE-" + rack,
                    "WIDE-A",
                    "Wide Area",
                    "R" + rack,
                    "01",
                    "1",
                    "1.0",
                    "1.0",
                    null));
        }
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(rows);

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout();
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);
        BigDecimal singleStripWidth = new BigDecimal("1.0")
                .add(new BigDecimal("1.2"))
                .multiply(new BigDecimal("40"))
                .subtract(new BigDecimal("1.2"));

        assertTrue(aisle.getWidth().min(aisle.getHeight()).compareTo(new BigDecimal("4")) >= 0);
        assertTrue(aisle.getWidth().max(aisle.getHeight()).compareTo(singleStripWidth) < 0);
    }

    @Test
    void getLocationHierarchyPrefersCustomerOverrideOverWarehouseDefault() {
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting warehouseDefault =
                hierarchySetting(null, hierarchyMapping("AREA_CODE", null, "RACK_CODE", "POSITION_CODE", "LEVEL_CODE"));
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting customerOverride =
                hierarchySetting("TESA", hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", "LEVEL_CODE", "LEVEL_CODE"));

        when(repository.findLocationHierarchySetting("520010", "BKK", null)).thenReturn(warehouseDefault);
        when(repository.findLocationHierarchySetting("520010", "BKK", "TESA")).thenReturn(customerOverride);

        WarehouseOptimizeModels.WarehouseLocationHierarchyResponse response = service.getLocationHierarchy("TESA");

        assertEquals(warehouseDefault.getId(), response.getWarehouseDefault().getId());
        assertEquals(customerOverride.getId(), response.getCustomerOverride().getId());
        assertEquals(customerOverride.getId(), response.getEffective().getId());
    }

    @Test
    void saveLocationHierarchyAllowsOptionalSlotMapping() {
        WarehouseOptimizeRequests.WarehouseLocationHierarchyRequest request =
                new WarehouseOptimizeRequests.WarehouseLocationHierarchyRequest();
        request.setScope(WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE_CUSTOMER);
        request.setCustomerCode("TESA");
        request.setMapping(hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", null, "LEVEL_CODE"));

        WarehouseOptimizeModels.WarehouseLocationHierarchySetting customerOverride =
                hierarchySetting("TESA", hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", null, "LEVEL_CODE"));
        when(repository.findLocationHierarchySetting("520010", "BKK", null)).thenReturn(null);
        when(repository.findLocationHierarchySetting("520010", "BKK", "TESA")).thenReturn(customerOverride);

        WarehouseOptimizeModels.WarehouseLocationHierarchyResponse response = service.saveLocationHierarchy(request);

        ArgumentCaptor<WarehouseOptimizeModels.WarehouseLocationHierarchyMapping> mappingCaptor =
                ArgumentCaptor.forClass(WarehouseOptimizeModels.WarehouseLocationHierarchyMapping.class);
        verify(repository).saveLocationHierarchySetting(eq("520010"), eq("BKK"), eq("TESA"), mappingCaptor.capture());
        assertEquals("POSITION_CODE", mappingCaptor.getValue().getBay());
        assertNull(mappingCaptor.getValue().getSlot());
        assertNotNull(response.getEffective());
        assertNull(response.getEffective().getMapping().getSlot());
    }

    @Test
    void saveWarehouseDefaultHierarchyDoesNotRequireCustomerContext() {
        WarehouseOptimizeRequests.WarehouseLocationHierarchyRequest request =
                new WarehouseOptimizeRequests.WarehouseLocationHierarchyRequest();
        request.setScope(WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE);
        request.setCustomerCode(null);
        request.setMapping(hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", null, "LEVEL_CODE"));

        WarehouseOptimizeModels.WarehouseLocationHierarchySetting warehouseDefault =
                hierarchySetting(null, hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", null, "LEVEL_CODE"));
        when(repository.findLocationHierarchySetting("520010", "BKK", null)).thenReturn(warehouseDefault);

        WarehouseOptimizeModels.WarehouseLocationHierarchyResponse response = service.saveLocationHierarchy(request);

        verify(repository).saveLocationHierarchySetting(eq("520010"), eq("BKK"), eq(null), any());
        assertNotNull(response.getWarehouseDefault());
        assertEquals(warehouseDefault.getId(), response.getWarehouseDefault().getId());
        assertNull(response.getCustomerOverride());
    }

    @Test
    void autoGenerateLayoutUsesHierarchySettingToKeepMultipleSlotsInsideOneBayFootprint() {
        when(repository.findLocationHierarchySetting("520010", "BKK", "TESA")).thenReturn(
                hierarchySetting("TESA", hierarchyMapping("AREA_CODE", null, "RACK_CODE", "POSITION_CODE", "LEVEL_CODE"))
        );
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("H-001", "ZONE-A", null, "BAY-01", "S1", "1", "1.0", "1.2", null),
                locationMasterRow("H-002", "ZONE-A", null, "BAY-01", "S2", "1", "1.0", "1.2", null),
                locationMasterRow("H-003", "ZONE-A", null, "BAY-02", "S1", "1", "1.0", "1.2", null),
                locationMasterRow("H-004", "ZONE-A", null, "BAY-02", "S2", "1", "1.0", "1.2", null)
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout("TESA");
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(1, response.getSummary().getAreas());
        assertEquals(1, response.getSummary().getRacks());
        assertEquals(4, response.getSummary().getLocations());
        assertEquals("ZONE-A", aisle.getZone());
        assertEquals(4, aisle.getLocations().size());
        assertEquals("ZONE-ABAY-01S11", aisle.getLocations().get(0).getLocation());
        assertTrue(aisle.getLocations().stream().anyMatch(location -> location.getFootprintDepth() != null || location.getFootprintWidth() != null));
        assertTrue(aisle.getWidth().max(aisle.getHeight()).compareTo(new BigDecimal("4.0")) < 0);
    }

    @Test
    void autoGenerateLayoutWithOptionalSlotMappingOmitsSlotFromCanonicalKey() {
        when(repository.findLocationHierarchySetting("520010", "BKK", "TESA")).thenReturn(
                hierarchySetting("TESA", hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", null, "LEVEL_CODE"))
        );
        when(repository.loadOracleLocationMasterRows("520010", "BKK")).thenReturn(Arrays.asList(
                locationMasterRow("H-001", "ZONE-A", null, "R1", "B1", "1", "1.0", "1.2", null),
                locationMasterRow("H-002", "ZONE-A", null, "R1", "B1", "1", "1.0", "1.2", null),
                locationMasterRow("H-003", "ZONE-A", null, "R1", "B2", "1", "1.0", "1.2", null)
        ));

        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = service.autoGenerateLayout("TESA");
        WarehouseOptimizeModels.WarehouseAisle aisle = response.getLayout().getAisles().get(0);

        assertEquals(1, response.getSummary().getAreas());
        assertEquals(1, response.getSummary().getRacks());
        assertEquals(2, response.getSummary().getLocations());
        assertEquals(1, response.getSummary().getSkippedRows());
        assertEquals("ZONE-A", aisle.getZone());
        assertEquals("R1", aisle.getAisleCode());
        assertEquals(2, aisle.getLocations().size());
        assertEquals("ZONE-AR1B11", aisle.getLocations().get(0).getLocation());
        assertEquals("ZONE-AR1B21", aisle.getLocations().get(1).getLocation());
        assertTrue(response.getWarnings().stream().anyMatch(message -> message.contains("Skipped duplicate canonical location ZONE-AR1B11")));
        assertTrue(aisle.getWidth().max(aisle.getHeight()).compareTo(new BigDecimal("4.0")) < 0);
    }

    @Test
    void syncStockResolvesHierarchyMappedKeysBeforeMatchingViewerLocations() {
        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());
        when(repository.findLocationHierarchySetting("520010", "BKK", "TESA")).thenReturn(
                hierarchySetting("TESA", hierarchyMapping("AREA_CODE", null, "RACK_CODE", "POSITION_CODE", "LEVEL_CODE"))
        );
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(null))).thenReturn(Collections.singletonList(
                stockRowRaw(null, "ZONE-A", "BAY-01", "S1", "1", "SKU-A", "8", "5", LocalDateTime.of(2026, 5, 15, 11, 0, 0))
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.emptyMap());
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.emptyList());
        when(repository.toJson(any())).thenReturn("{\"aisles\":[{\"locations\":[{\"location\":\"ZONE-ABAY-01S11\",\"zone\":\"ZONE-A\",\"type\":\"HIGH_RACK\",\"level\":1,\"x\":0,\"y\":0}]}]}");
        when(repository.parseLocations(anyString())).thenReturn(Collections.singletonList(
                layoutLocation("ZONE-ABAY-01S11", "ZONE-A", 1, "0", "0")
        ));

        WarehouseOptimizeModels.StockSyncSnapshot snapshot = service.syncStock(135L, "TESA", Collections.singletonMap("aisles", Collections.emptyList()));

        assertEquals(1, snapshot.getLocations().size());
        assertEquals("ZONE-ABAY-01S11", snapshot.getLocations().get(0).getLocation());
        assertEquals(new BigDecimal("8"), snapshot.getLocations().get(0).getPhysicalQty());
        assertTrue(snapshot.getDiagnostics().getUnmatchedStockLocations().isEmpty());
    }

    @Test
    void syncStockAggregatesHierarchyMappedRowsWhenSlotIsUnused() {
        when(repository.findProfileSummary(135L)).thenReturn(profileSummary());
        when(repository.findLocationHierarchySetting("520010", "BKK", "TESA")).thenReturn(
                hierarchySetting("TESA", hierarchyMapping("AREA_CODE", "RACK_CODE", "POSITION_CODE", null, "LEVEL_CODE"))
        );
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(null))).thenReturn(Arrays.asList(
                stockRowRaw(null, "ZONE-A", "R1", "B1", "1", "SKU-A", "8", "5", LocalDateTime.of(2026, 5, 15, 11, 0, 0)),
                stockRowRaw(null, "ZONE-A", "R1", "B1", "1", "SKU-A", "2", "1", LocalDateTime.of(2026, 5, 15, 11, 5, 0))
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.emptyMap());
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.emptyList());
        when(repository.toJson(any())).thenReturn("{\"aisles\":[{\"locations\":[{\"location\":\"ZONE-AR1B11\",\"zone\":\"ZONE-A\",\"type\":\"HIGH_RACK\",\"level\":1,\"x\":0,\"y\":0}]}]}");
        when(repository.parseLocations(anyString())).thenReturn(Collections.singletonList(
                layoutLocation("ZONE-AR1B11", "ZONE-A", 1, "0", "0")
        ));

        WarehouseOptimizeModels.StockSyncSnapshot snapshot = service.syncStock(135L, "TESA", Collections.singletonMap("aisles", Collections.emptyList()));

        assertEquals(1, snapshot.getLocations().size());
        assertEquals("ZONE-AR1B11", snapshot.getLocations().get(0).getLocation());
        assertEquals(new BigDecimal("10"), snapshot.getLocations().get(0).getPhysicalQty());
        assertEquals(new BigDecimal("6"), snapshot.getLocations().get(0).getAvailableQty());
        assertTrue(snapshot.getDiagnostics().getUnmatchedStockLocations().isEmpty());
    }

    private WarehouseOptimizeModels.WarehouseProfileDetail profileDetail() {
        WarehouseOptimizeModels.WarehouseProfileDetail detail = new WarehouseOptimizeModels.WarehouseProfileDetail();
        detail.setId(135L);
        detail.setCompanyCode("520010");
        detail.setWarehouseCode("BKK");
        detail.setCustomerCode("*");
        detail.setProfileName("TESA Migration Smoke Layout");
        detail.setLayoutData("{}");
        detail.setLocations(Arrays.asList(
                layoutLocation("LOC-A", "ZONE-1", 1, "0", "0"),
                layoutLocation("LOC-B", "ZONE-1", 1, "1", "0")
        ));
        return detail;
    }

    private WarehouseOptimizeModels.WarehouseProfileSummary profileSummary() {
        WarehouseOptimizeModels.WarehouseProfileSummary summary = new WarehouseOptimizeModels.WarehouseProfileSummary();
        summary.setId(135L);
        summary.setCompanyCode("520010");
        summary.setWarehouseCode("BKK");
        summary.setCustomerCode("*");
        summary.setProfileName("TESA Migration Smoke Layout");
        return summary;
    }

    private WarehouseOptimizeModels.WarehouseLayoutLocation layoutLocation(
            String code, String zone, int level, String x, String y) {
        WarehouseOptimizeModels.WarehouseLayoutLocation location = new WarehouseOptimizeModels.WarehouseLayoutLocation();
        location.setLocation(code);
        location.setZone(zone);
        location.setType("HIGH_RACK");
        location.setLevel(level);
        location.setX(new BigDecimal(x));
        location.setY(new BigDecimal(y));
        return location;
    }

    private WarehouseOptimizeModels.ProductRecord product(String sku, String category) {
        WarehouseOptimizeModels.ProductRecord product = new WarehouseOptimizeModels.ProductRecord();
        product.setSku(sku);
        product.setName(sku);
        product.setCategory(category);
        return product;
    }

    private WarehouseOptimizeModels.SlottingAssignment assignment(
            String locationCode,
            String sku,
            String velocityClass,
            String productCategory) {
        WarehouseOptimizeModels.SlottingAssignment assignment = new WarehouseOptimizeModels.SlottingAssignment();
        assignment.setLocation(locationCode);
        assignment.setProductSku(sku);
        assignment.setProductName(sku);
        assignment.setVelocityClass(velocityClass);
        assignment.setProductCategory(productCategory);
        return assignment;
    }

    private WarehouseOptimizeModels.OracleStockRow stockRow(
            String locationCode,
            String sku,
            String physicalQty,
            String availableQty,
            LocalDateTime updatedAt) {
        WarehouseOptimizeModels.OracleStockRow row = new WarehouseOptimizeModels.OracleStockRow();
        row.setLocationCode(locationCode);
        row.setAreaCode(locationCode);
        row.setRackCode("");
        row.setPositionCode("");
        row.setLevelCode("");
        row.setProductCode(sku);
        row.setPhysicalQty(new BigDecimal(physicalQty));
        row.setAvailableQty(new BigDecimal(availableQty));
        row.setUpdatedAt(updatedAt);
        return row;
    }

    private WarehouseOptimizeModels.OracleStockRow stockRowRaw(
            String locationCode,
            String areaCode,
            String rackCode,
            String positionCode,
            String levelCode,
            String sku,
            String physicalQty,
            String availableQty,
            LocalDateTime updatedAt) {
        WarehouseOptimizeModels.OracleStockRow row = new WarehouseOptimizeModels.OracleStockRow();
        row.setLocationCode(locationCode);
        row.setAreaCode(areaCode);
        row.setRackCode(rackCode);
        row.setPositionCode(positionCode);
        row.setLevelCode(levelCode);
        row.setProductCode(sku);
        row.setPhysicalQty(new BigDecimal(physicalQty));
        row.setAvailableQty(new BigDecimal(availableQty));
        row.setUpdatedAt(updatedAt);
        return row;
    }

    private WarehouseOptimizeModels.OracleLocationMasterRow locationMasterRow(
            String locationCode,
            String areaCode,
            String areaName,
            String rackCode,
            String positionCode,
            String levelCode,
            String length,
            String width,
            String height) {
        WarehouseOptimizeModels.OracleLocationMasterRow row = new WarehouseOptimizeModels.OracleLocationMasterRow();
        row.setLocationCode(locationCode);
        row.setAreaCode(areaCode);
        row.setAreaName(areaName);
        row.setRackCode(rackCode);
        row.setPositionCode(positionCode);
        row.setLevelCode(levelCode);
        row.setLength(length == null ? null : new BigDecimal(length));
        row.setWidth(width == null ? null : new BigDecimal(width));
        row.setHeight(height == null ? null : new BigDecimal(height));
        return row;
    }

    private WarehouseOptimizeModels.OracleStatusAggregateRow statusAggregateRow(
            String statusCode,
            long orderCount,
            String csQty,
            String pcsQty) {
        WarehouseOptimizeModels.OracleStatusAggregateRow row = new WarehouseOptimizeModels.OracleStatusAggregateRow();
        row.setStatusCode(statusCode);
        row.setOrderCount(orderCount);
        row.setCsQty(new BigDecimal(csQty));
        row.setPcsQty(new BigDecimal(pcsQty));
        return row;
    }

    private WarehouseOptimizeModels.WarehouseLocationHierarchyMapping hierarchyMapping(
            String zone,
            String aisle,
            String bay,
            String slot,
            String level) {
        WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping = new WarehouseOptimizeModels.WarehouseLocationHierarchyMapping();
        mapping.setZone(zone);
        mapping.setAisle(aisle);
        mapping.setBay(bay);
        mapping.setSlot(slot);
        mapping.setLevel(level);
        return mapping;
    }

    private WarehouseOptimizeModels.WarehouseLocationHierarchySetting hierarchySetting(
            String customerCode,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping) {
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting setting = new WarehouseOptimizeModels.WarehouseLocationHierarchySetting();
        setting.setId(customerCode == null ? 1L : 2L);
        setting.setCompanyCode("520010");
        setting.setWarehouseCode("BKK");
        setting.setCustomerCode(customerCode);
        setting.setScope(customerCode == null
                ? WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE
                : WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE_CUSTOMER);
        setting.setMapping(mapping);
        return setting;
    }

    private boolean overlaps(
            WarehouseOptimizeModels.WarehouseAisle left,
            WarehouseOptimizeModels.WarehouseAisle right) {
        BigDecimal leftRight = left.getX().add(left.getWidth());
        BigDecimal rightRight = right.getX().add(right.getWidth());
        BigDecimal leftTop = left.getY().add(left.getHeight());
        BigDecimal rightTop = right.getY().add(right.getHeight());
        boolean separatedHorizontally = leftRight.compareTo(right.getX()) <= 0 || rightRight.compareTo(left.getX()) <= 0;
        boolean separatedVertically = leftTop.compareTo(right.getY()) <= 0 || rightTop.compareTo(left.getY()) <= 0;
        return !(separatedHorizontally || separatedVertically);
    }
}
