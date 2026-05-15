package jp.co.nittsu.gwh.module.warehouseoptimize.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeRequests;
import jp.co.nittsu.gwh.module.warehouseoptimize.service.WarehouseOptimizeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/warehouse-optimize")
public class WarehouseOptimizeController {

    private final WarehouseOptimizeService service;

    public WarehouseOptimizeController(WarehouseOptimizeService service) {
        this.service = service;
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<WarehouseOptimizeModels.CustomerOption>>> getCustomers() {
        return ResponseEntity.ok(ApiResponse.success(service.getAuthorizedCustomers()));
    }

    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<List<WarehouseOptimizeModels.WarehouseProfileSummary>>> getProfiles(
            @RequestParam(required = false) String profileName,
            @RequestParam(required = false) String q) {
        List<WarehouseOptimizeModels.WarehouseProfileSummary> profiles = service.getProfiles(profileName, q);
        return ResponseEntity.ok(ApiResponse.success(profiles, profiles.size(), 0, profiles.size()));
    }

    @GetMapping("/profiles/{profileId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.WarehouseProfileDetail>> getProfile(
            @PathVariable Long profileId) {
        return ResponseEntity.ok(ApiResponse.success(service.getProfile(profileId)));
    }

    @PostMapping("/profiles")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.WarehouseProfileDetail>> saveProfile(
            @Valid @RequestBody WarehouseOptimizeRequests.SaveProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.saveProfile(request)));
    }

    @PostMapping("/products/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateProducts(
            @RequestBody(required = false) WarehouseOptimizeRequests.GenerateProductsRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.generateProducts(
                request == null ? new WarehouseOptimizeRequests.GenerateProductsRequest() : request)));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<WarehouseOptimizeModels.ProductRecord>>> getProducts() {
        List<WarehouseOptimizeModels.ProductRecord> products = service.getProducts();
        return ResponseEntity.ok(ApiResponse.success(products, products.size(), 0, products.size()));
    }

    @PostMapping("/products/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadProducts(
            @Valid @RequestBody WarehouseOptimizeRequests.UploadProductsRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.uploadProducts(request)));
    }

    @PostMapping("/slotting/init-db")
    public ResponseEntity<ApiResponse<Map<String, Object>>> initDb() {
        return ResponseEntity.ok(ApiResponse.success(service.initializeSlotting()));
    }

    @PostMapping("/slotting/optimize")
    public ResponseEntity<ApiResponse<Map<String, Object>>> optimizeSlotting(
            @Valid @RequestBody WarehouseOptimizeRequests.SlottingOptimizeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.optimizeSlotting(request)));
    }

    @GetMapping("/slotting/assignments/{profileId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAssignments(@PathVariable Long profileId) {
        return ResponseEntity.ok(ApiResponse.success(service.getAssignments(profileId)));
    }

    @DeleteMapping("/assignments")
    public ResponseEntity<ApiResponse<Map<String, Object>>> clearAssignments(@RequestParam Long profileId) {
        service.clearAssignments(profileId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/orders/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateOrders(
            @Valid @RequestBody WarehouseOptimizeRequests.GenerateOrdersRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.generateOrders(request)));
    }

    @PostMapping("/orders/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadOrders(
            @Valid @RequestBody WarehouseOptimizeRequests.UploadOrdersRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.uploadOrders(request)));
    }

    @GetMapping("/orders/{profileId}")
    public ResponseEntity<ApiResponse<List<WarehouseOptimizeModels.PickOrder>>> getOrders(@PathVariable Long profileId) {
        List<WarehouseOptimizeModels.PickOrder> orders = service.getOrders(profileId);
        return ResponseEntity.ok(ApiResponse.success(orders, orders.size(), 0, orders.size()));
    }

    @GetMapping("/orders/detail/{orderId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.PickOrder>> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(service.getOrderDetail(orderId)));
    }

    @PostMapping("/routes/optimize/{orderId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.RouteOptimizationResult>> optimizeRoute(
            @PathVariable Long orderId,
            @RequestBody(required = false) WarehouseOptimizeRequests.RouteOptimizeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.optimizeRoute(
                orderId,
                request == null ? new WarehouseOptimizeRequests.RouteOptimizeRequest() : request)));
    }

    @GetMapping("/routes/results/{orderId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRouteResults(@PathVariable Long orderId) {
        List<Map<String, Object>> results = service.getRouteResults(orderId);
        return ResponseEntity.ok(ApiResponse.success(results, results.size(), 0, results.size()));
    }

    @GetMapping("/analytics/{profileId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.WarehouseOptimizeAnalytics>> getAnalytics(
            @PathVariable Long profileId) {
        return ResponseEntity.ok(ApiResponse.success(service.getAnalytics(profileId)));
    }

    @GetMapping("/analytics/export")
    public ResponseEntity<String> exportAnalytics(@RequestParam Long profileId) {
        String csv = service.exportAnalyticsCsv(profileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=warehouse-analytics.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }

    @PostMapping("/viewer/{profileId}/sync-stock")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.StockSyncSnapshot>> syncStock(
            @PathVariable Long profileId,
            @RequestBody(required = false) WarehouseOptimizeRequests.ViewerSyncRequest request) {
        String customerCode = request == null ? null : request.getCustomerCode();
        return ResponseEntity.ok(ApiResponse.success(service.syncStock(profileId, customerCode)));
    }

    @PostMapping("/viewer/{profileId}/stock-delta")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.StockDelta>> stockDelta(
            @PathVariable Long profileId,
            @RequestBody(required = false) WarehouseOptimizeRequests.ViewerDeltaRequest request) {
        String customerCode = request == null ? null : request.getCustomerCode();
        return ResponseEntity.ok(ApiResponse.success(service.loadStockDelta(
                profileId,
                customerCode,
                request == null ? null : request.getCursor())));
    }

    // Legacy-compatible aliases for migrated asset pages
    @GetMapping("/warehouse/profiles")
    public ResponseEntity<ApiResponse<List<WarehouseOptimizeModels.WarehouseProfileSummary>>> legacyProfiles(
            @RequestParam(required = false, name = "op_pf_name") String profileName,
            @RequestParam(required = false, name = "q") String q) {
        return getProfiles(profileName, q);
    }

    @GetMapping("/warehouse/profile/{profileId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> legacyProfile(@PathVariable Long profileId) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("profile", service.getProfile(profileId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/warehouse/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> legacySaveProfile(
            @RequestBody WarehouseOptimizeRequests.LegacyProfileRequest legacyRequest) {
        WarehouseOptimizeRequests.SaveProfileRequest request = new WarehouseOptimizeRequests.SaveProfileRequest();
        request.setId(legacyRequest.getId());
        request.setProfileName(legacyRequest.getOp_pf_name() == null ? legacyRequest.getName() : legacyRequest.getOp_pf_name());
        request.setDescription(legacyRequest.getDescription());
        request.setWarehouseLength(legacyRequest.getWarehouseLength());
        request.setWarehouseWidth(legacyRequest.getWarehouseWidth());
        request.setLayoutData(legacyRequest.getLayoutData());
        WarehouseOptimizeModels.WarehouseProfileDetail profile = service.saveProfile(request);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", profile.getId());
        response.put("profile", profile);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/warehouse/submit")
    public ResponseEntity<ApiResponse<Map<String, Object>>> legacySubmit(@RequestBody Map<String, Object> ignored) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Warehouse design submission acknowledged");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/picking/optimize/{orderId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.RouteOptimizationResult>> legacyOptimizeRoute(
            @PathVariable Long orderId,
            @RequestBody(required = false) WarehouseOptimizeRequests.RouteOptimizeRequest request) {
        return optimizeRoute(orderId, request);
    }

    @GetMapping("/picking/results/{orderId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> legacyResults(@PathVariable Long orderId) {
        return getRouteResults(orderId);
    }

    @GetMapping("/analytics/slotting/{profileId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.SlottingAnalytics>> legacySlottingAnalytics(@PathVariable Long profileId) {
        return ResponseEntity.ok(ApiResponse.success(service.getAnalytics(profileId).getSlotting()));
    }

    @GetMapping("/analytics/picking/{profileId}")
    public ResponseEntity<ApiResponse<WarehouseOptimizeModels.PickingAnalytics>> legacyPickingAnalytics(@PathVariable Long profileId) {
        return ResponseEntity.ok(ApiResponse.success(service.getAnalytics(profileId).getPicking()));
    }
}
