package jp.co.nittsu.gwh.module.warehouseoptimize.service;

import jp.co.nittsu.gwh.module.auth.dto.TenantOption;
import jp.co.nittsu.gwh.module.auth.repository.CustomerMasterRepository;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;
import jp.co.nittsu.gwh.module.warehouseoptimize.repository.WarehouseOptimizeRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
        when(tenantContext.getCustomerCode()).thenReturn("TESA");

        when(customerMasterRepository.findTenantsByCompanyCode("520010")).thenReturn(Collections.singletonList(
                new TenantOption("BKK", "TESA", "TESA TAPE (THAILAND) LTD.")
        ));
    }

    @Test
    void syncStockIncludesEmptyLocationsAndUsesVelocityColorPrecedence() {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = profileDetail();
        when(repository.findProfileDetail(135L)).thenReturn(profile);
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(null))).thenReturn(Collections.singletonList(
                stockRow("LOC-A", "SKU-A", "11", "9", LocalDateTime.of(2026, 5, 15, 10, 0, 0))
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.singletonMap("SKU-A", product("SKU-A", "Fallback Category")));
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.singletonList(
                assignment("LOC-A", "SKU-A", "B", "Assigned Category")
        ));

        WarehouseOptimizeModels.StockSyncSnapshot snapshot = service.syncStock(135L, "TESA");

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

        when(repository.findProfileDetail(135L)).thenReturn(profile);
        when(repository.loadOracleStockRows(eq("520010"), eq("BKK"), eq("TESA"), eq(previousCursor))).thenReturn(Arrays.asList(
                stockRow("LOC-B", "SKU-B", "4", "3", updatedAt),
                stockRow("UNMAPPED-01", "SKU-Z", "1", "1", updatedAt)
        ));
        when(repository.loadProductsBySku("520010", "BKK", "TESA")).thenReturn(Collections.singletonMap("SKU-B", product("SKU-B", "Tape")));
        when(repository.loadAssignments(eq(135L), eq("520010"), eq("BKK"), eq("TESA"))).thenReturn(Collections.emptyList());

        WarehouseOptimizeModels.StockDelta delta = service.loadStockDelta(135L, "TESA", previousCursor);

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
        when(repository.findProfileDetail(135L)).thenReturn(profileDetail());

        try {
            service.syncStock(135L, "TESA");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("not authorized"));
            return;
        }
        throw new AssertionError("Expected unauthorized customer filter exception");
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

    private Map<String, Object> stockRow(
            String locationCode,
            String sku,
            String physicalQty,
            String availableQty,
            LocalDateTime updatedAt) {
        Map<String, Object> row = new HashMap<>();
        row.put("location", locationCode);
        row.put("productCode", sku);
        row.put("physicalQty", new BigDecimal(physicalQty));
        row.put("availableQty", new BigDecimal(availableQty));
        row.put("updatedAt", updatedAt);
        return row;
    }
}
