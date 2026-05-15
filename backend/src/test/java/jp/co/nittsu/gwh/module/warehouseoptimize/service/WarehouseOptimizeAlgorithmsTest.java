package jp.co.nittsu.gwh.module.warehouseoptimize.service;

import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarehouseOptimizeAlgorithmsTest {

    @Test
    void assignProductsRanksFastMovingItemsIntoBestLocations() {
        WarehouseOptimizeModels.ProductRecord productA = product("SKU-A", "A", "95");
        WarehouseOptimizeModels.ProductRecord productB = product("SKU-B", "B", "60");
        WarehouseOptimizeModels.ProductRecord productC = product("SKU-C", "C", "15");

        WarehouseOptimizeModels.WarehouseLayoutLocation lowLevel = location("LOC-01", "HIGH_RACK", 1, "ZONE-A", "0", "0");
        WarehouseOptimizeModels.WarehouseLayoutLocation midLevel = location("LOC-02", "HIGH_RACK", 2, "ZONE-A", "1", "0");
        WarehouseOptimizeModels.WarehouseLayoutLocation highLevel = location("LOC-03", "HIGH_RACK", 4, "ZONE-A", "2", "0");

        List<WarehouseOptimizeModels.SlottingAssignment> assignments = WarehouseOptimizeAlgorithms.assignProducts(
                Arrays.asList(productC, productB, productA),
                Arrays.asList(highLevel, midLevel, lowLevel),
                99L,
                "TESA",
                "abc");

        assertEquals(3, assignments.size());
        assertEquals("SKU-A", assignments.get(0).getProductSku());
        assertEquals("LOC-01", assignments.get(0).getLocation());
        assertEquals("SKU-B", assignments.get(1).getProductSku());
        assertEquals("LOC-02", assignments.get(1).getLocation());
        assertEquals("SKU-C", assignments.get(2).getProductSku());
        assertEquals("LOC-03", assignments.get(2).getLocation());
    }

    @Test
    void optimizeRouteProducesBestCandidateAndImprovementMetrics() {
        WarehouseOptimizeModels.RouteNode start = routeNode("START", "0", "0");
        List<WarehouseOptimizeModels.RouteNode> stops = Arrays.asList(
                routeNode("A", "1", "0"),
                routeNode("B", "4", "0"),
                routeNode("C", "4", "3")
        );

        WarehouseOptimizeModels.RouteOptimizationResult result =
                WarehouseOptimizeAlgorithms.optimizeRoute(101L, start, stops);

        assertNotNull(result.getBest());
        assertNotNull(result.getOriginal());
        assertFalse(result.getOptimized().isEmpty());
        assertEquals(3, result.getBest().getRoute().size());
        assertTrue(result.getBest().getDistance().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.getBest().getDistance().compareTo(result.getOriginal().getDistance()) <= 0);
        assertTrue(result.getComparison().stream().allMatch(item -> item.getPercent() != null));
    }

    private WarehouseOptimizeModels.ProductRecord product(String sku, String velocityClass, String demandFrequency) {
        WarehouseOptimizeModels.ProductRecord product = new WarehouseOptimizeModels.ProductRecord();
        product.setSku(sku);
        product.setName(sku);
        product.setCategory("General");
        product.setVelocityClass(velocityClass);
        product.setDemandFrequency(new BigDecimal(demandFrequency));
        return product;
    }

    private WarehouseOptimizeModels.WarehouseLayoutLocation location(
            String code,
            String type,
            int level,
            String zone,
            String x,
            String y) {
        WarehouseOptimizeModels.WarehouseLayoutLocation location = new WarehouseOptimizeModels.WarehouseLayoutLocation();
        location.setLocation(code);
        location.setType(type);
        location.setLevel(level);
        location.setZone(zone);
        location.setX(new BigDecimal(x));
        location.setY(new BigDecimal(y));
        return location;
    }

    private WarehouseOptimizeModels.RouteNode routeNode(String name, String x, String y) {
        WarehouseOptimizeModels.RouteNode node = new WarehouseOptimizeModels.RouteNode();
        node.setName(name);
        node.setX(new BigDecimal(x));
        node.setY(new BigDecimal(y));
        node.setLevel(1);
        return node;
    }
}
