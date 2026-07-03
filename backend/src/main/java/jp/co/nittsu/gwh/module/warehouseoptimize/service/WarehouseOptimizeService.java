package jp.co.nittsu.gwh.module.warehouseoptimize.service;

import jp.co.nittsu.gwh.module.auth.dto.TenantOption;
import jp.co.nittsu.gwh.module.auth.repository.CustomerMasterRepository;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeRequests;
import jp.co.nittsu.gwh.module.warehouseoptimize.repository.WarehouseOptimizeRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WarehouseOptimizeService {

    private static final String SYNTHETIC_RACK_KEY = "_NO_RACK";
    private static final BigDecimal DEFAULT_BAY_DEPTH = new BigDecimal("1.0");
    private static final BigDecimal DEFAULT_BAY_WIDTH = new BigDecimal("1.2");
    private static final BigDecimal DEFAULT_AISLE_WIDTH = new BigDecimal("1.2");
    private static final BigDecimal OUTER_PADDING = new BigDecimal("8.0");
    private static final BigDecimal AREA_GAP = new BigDecimal("6.0");
    private static final BigDecimal INTERNAL_RACK_ROW_GAP = new BigDecimal("3.0");
    private static final BigDecimal MIN_BAY_DEPTH = new BigDecimal("0.6");
    private static final BigDecimal MAX_BAY_DEPTH = new BigDecimal("3.5");
    private static final BigDecimal MIN_BAY_WIDTH = new BigDecimal("0.6");
    private static final BigDecimal MAX_BAY_WIDTH = new BigDecimal("2.5");
    private static final BigDecimal MIN_LEVEL_HEIGHT = new BigDecimal("0.15");
    private static final BigDecimal MAX_LEVEL_HEIGHT = new BigDecimal("4.0");
    private static final BigDecimal MIN_MIXED_HEIGHT_GAP = new BigDecimal("0.20");
    private static final BigDecimal MAX_MIXED_LOWER_RATIO = new BigDecimal("0.75");
    private static final BigDecimal MIXED_HEIGHT_HINT_GAP = new BigDecimal("0.10");
    private static final BigDecimal MIXED_HEIGHT_HINT_RATIO = new BigDecimal("0.85");
    private static final BigDecimal MIN_POSITION_BOUNDARY_GAP = BigDecimal.ONE;
    private static final BigDecimal MIN_POSITION_BOUNDARY_RATIO = new BigDecimal("1.50");
    private static final BigDecimal MIN_POSITION_AVERAGE_GAP = new BigDecimal("0.50");
    private static final BigDecimal POSITION_HINT_RATIO = new BigDecimal("1.10");
    private static final BigDecimal SUBSLOT_FILL_RATIO = new BigDecimal("0.82");
    private static final BigDecimal MIN_SUBSLOT_SPAN = new BigDecimal("0.22");
    private static final double MIXED_RACK_SUPPORT_THRESHOLD = 0.70d;
    private static final BigDecimal LARGE_FOOTPRINT_SIDE_THRESHOLD = new BigDecimal("500");
    private static final BigDecimal LARGE_FOOTPRINT_AREA_THRESHOLD = new BigDecimal("100000");
    private static final Set<String> ALLOWED_HIERARCHY_SOURCES = new HashSet<>(Arrays.asList(
            WarehouseOptimizeModels.WarehouseLocationHierarchySource.AREA_CODE,
            WarehouseOptimizeModels.WarehouseLocationHierarchySource.RACK_CODE,
            WarehouseOptimizeModels.WarehouseLocationHierarchySource.POSITION_CODE,
            WarehouseOptimizeModels.WarehouseLocationHierarchySource.LEVEL_CODE
    ));
    private static final Map<String, StatusDefinition> INBOUND_WORKING_STATUS_DEFINITIONS = new LinkedHashMap<>();
    private static final Map<String, StatusDefinition> OUTBOUND_WORKING_STATUS_DEFINITIONS = new LinkedHashMap<>();

    static {
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "100", "Created", "#5BC2E7");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "200", "Located", "#FF9E1B");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "205", "Inspected", "#1A005D");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "209", "Confirmed", "#8EC400");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "300", "Receiving", "#0F766E");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "500", "Putaway", "#7C3AED");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "605", "Putaway Done", "#2563EB");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "609", "Stored", "#0EA5E9");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "700", "Closed", "#475569");
        registerStatusDefinition(INBOUND_WORKING_STATUS_DEFINITIONS, "809", "Completed", "#16A34A");

        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "100", "New", "#5BC2E7");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "200", "Processing", "#8B5CF6");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "209", "Ready", "#14B8A6");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "300", "Allocated", "#1A005D");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "500", "Picking", "#FF9E1B");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "605", "Pick Complete", "#C2410C");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "609", "Packed", "#8EC400");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "700", "Shipped", "#16A34A");
        registerStatusDefinition(OUTBOUND_WORKING_STATUS_DEFINITIONS, "809", "Completed", "#0F766E");
    }

    private final WarehouseOptimizeRepository repository;
    private final CustomerMasterRepository customerMasterRepository;
    private final TenantContext tenantContext;

    public WarehouseOptimizeService(
            WarehouseOptimizeRepository repository,
            CustomerMasterRepository customerMasterRepository,
            TenantContext tenantContext) {
        this.repository = repository;
        this.customerMasterRepository = customerMasterRepository;
        this.tenantContext = tenantContext;
    }

    public List<WarehouseOptimizeModels.CustomerOption> getAuthorizedCustomers() {
        List<TenantOption> all = customerMasterRepository.findTenantsByCompanyCode(tenantContext.getCompanyCode());
        return all.stream()
                .filter(option -> tenantContext.getWarehouseCode().equals(option.getWarehouseCode()))
                .map(option -> {
                    WarehouseOptimizeModels.CustomerOption customerOption = new WarehouseOptimizeModels.CustomerOption();
                    customerOption.setWarehouseCode(option.getWarehouseCode());
                    customerOption.setCustomerCode(option.getCustomerCode());
                    customerOption.setCustomerName(option.getCustomerName());
                    return customerOption;
                })
                .collect(Collectors.toList());
    }

    public List<WarehouseOptimizeModels.WarehouseProfileSummary> getProfiles(String profileName, String queryText) {
        return repository.findProfiles(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                profileName,
                queryText);
    }

    public WarehouseOptimizeModels.WarehouseProfileDetail getProfile(Long profileId) {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = repository.findProfileDetail(profileId);
        ensureProfileAccessible(profile);
        return profile;
    }

    public WarehouseOptimizeModels.WarehouseProfileSummary getProfileSummary(Long profileId) {
        WarehouseOptimizeModels.WarehouseProfileSummary profile = repository.findProfileSummary(profileId);
        ensureProfileAccessible(profile);
        return profile;
    }

    public WarehouseOptimizeModels.WarehouseProfileDetail saveProfile(WarehouseOptimizeRequests.SaveProfileRequest request) {
        String layoutJson = repository.toJson(request.getLayoutData());
        Long profileId = repository.saveProfile(tenantContext.getCompanyCode(), tenantContext.getWarehouseCode(), request, layoutJson);
        WarehouseOptimizeModels.WarehouseProfileSummary savedProfile = getProfileSummary(profileId);
        return buildSavedProfileResponse(savedProfile, layoutJson);
    }

    public WarehouseOptimizeModels.WarehouseLocationHierarchyResponse getLocationHierarchy(String requestedCustomerCode) {
        String companyCode = requireCompanyCode();
        String warehouseCode = requireWarehouseCode();
        String customerCode = resolveRequestedHierarchyCustomerCode(requestedCustomerCode);
        return buildHierarchyResponse(companyCode, warehouseCode, customerCode);
    }

    public WarehouseOptimizeModels.WarehouseLocationHierarchyResponse saveLocationHierarchy(
            WarehouseOptimizeRequests.WarehouseLocationHierarchyRequest request) {
        String companyCode = requireCompanyCode();
        String warehouseCode = requireWarehouseCode();
        String normalizedScope = normalizeHierarchyScope(request == null ? null : request.getScope());
        WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping = copyHierarchyMapping(
                request == null ? null : request.getMapping());
        validateHierarchyMapping(mapping);
        String scopedCustomerCode = WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE.equals(normalizedScope)
                ? null
                : resolveScopedHierarchyCustomerCode(request == null ? null : request.getCustomerCode());
        repository.saveLocationHierarchySetting(companyCode, warehouseCode, scopedCustomerCode, mapping);
        return buildHierarchyResponse(
                companyCode,
                warehouseCode,
                resolveRequestedHierarchyCustomerCode(request == null ? null : request.getCustomerCode()));
    }

    public WarehouseOptimizeModels.WarehouseLocationHierarchyResponse deleteLocationHierarchy(
            String scope,
            String requestedCustomerCode) {
        String companyCode = requireCompanyCode();
        String warehouseCode = requireWarehouseCode();
        String normalizedScope = normalizeHierarchyScope(scope);
        String scopedCustomerCode = WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE.equals(normalizedScope)
                ? null
                : resolveScopedHierarchyCustomerCode(requestedCustomerCode);
        repository.deleteLocationHierarchySetting(companyCode, warehouseCode, scopedCustomerCode);
        return buildHierarchyResponse(companyCode, warehouseCode, resolveRequestedHierarchyCustomerCode(requestedCustomerCode));
    }

    public WarehouseOptimizeModels.AutoGeneratedLayoutResponse autoGenerateLayout() {
        return autoGenerateLayout(null);
    }

    public WarehouseOptimizeModels.AutoGeneratedLayoutResponse autoGenerateLayout(String requestedCustomerCode) {
        String companyCode = defaultIfBlank(tenantContext.getCompanyCode(), null);
        String warehouseCode = defaultIfBlank(tenantContext.getWarehouseCode(), null);
        if (companyCode == null || warehouseCode == null) {
            throw new IllegalStateException("Tenant warehouse context is not available for layout generation");
        }

        List<WarehouseOptimizeModels.OracleLocationMasterRow> rows =
                repository.loadOracleLocationMasterRows(companyCode, warehouseCode);
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting hierarchySetting =
                resolveEffectiveHierarchySetting(companyCode, warehouseCode, resolveScopedHierarchyCustomerCode(requestedCustomerCode));
        return synthesizeLayout(rows, hierarchySetting == null ? null : hierarchySetting.getMapping());
    }

    public List<WarehouseOptimizeModels.ProductRecord> getProducts() {
        return repository.loadProducts(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
    }

    public Map<String, Object> generateProducts(WarehouseOptimizeRequests.GenerateProductsRequest request) {
        int count = request.getCount() == null ? 100 : Math.max(1, request.getCount());
        List<WarehouseOptimizeModels.ProductRecord> products = randomProducts(count);
        repository.upsertProducts(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                products);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("a_class", products.stream().filter(p -> "A".equalsIgnoreCase(p.getVelocityClass())).count());
        summary.put("b_class", products.stream().filter(p -> "B".equalsIgnoreCase(p.getVelocityClass())).count());
        summary.put("c_class", products.stream().filter(p -> "C".equalsIgnoreCase(p.getVelocityClass())).count());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        response.put("summary", summary);
        return response;
    }

    public Map<String, Object> uploadProducts(WarehouseOptimizeRequests.UploadProductsRequest request) {
        List<WarehouseOptimizeModels.ProductRecord> products = new ArrayList<>();
        for (Map<String, Object> raw : request.getProducts()) {
            WarehouseOptimizeModels.ProductRecord product = new WarehouseOptimizeModels.ProductRecord();
            product.setSku(stringValue(raw, "sku"));
            product.setName(defaultIfBlank(stringValue(raw, "name"), product.getSku()));
            product.setCategory(defaultIfBlank(stringValue(raw, "category"), "General"));
            product.setWeight(decimalValue(raw.get("weight")));
            product.setVolume(decimalValue(raw.get("volume")));
            product.setWidth(decimalValue(raw.get("width")));
            product.setHeight(decimalValue(raw.get("height")));
            product.setDepth(decimalValue(raw.get("depth")));
            product.setDemandFrequency(decimalValue(raw.get("demand_frequency")));
            product.setVelocityClass(defaultIfBlank(stringValue(raw, "velocity_class"), "C").toUpperCase());
            product.setVariabilityClass(defaultIfBlank(stringValue(raw, "variability_class"), "Z").toUpperCase());
            product.setUom(defaultIfBlank(stringValue(raw, "uom"), "EACH"));
            product.setPicksPerDay(decimalValue(raw.get("picks_per_day")));
            product.setAvgQtyPerPick(decimalValue(raw.get("avg_qty_per_pick")));
            products.add(product);
        }

        repository.upsertProducts(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                products);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        return response;
    }

    public Map<String, Object> initializeSlotting() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        return response;
    }

    public Map<String, Object> optimizeSlotting(WarehouseOptimizeRequests.SlottingOptimizeRequest request) {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = getProfile(request.getProfileId());
        List<WarehouseOptimizeModels.ProductRecord> products = getProducts();
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> locations =
                profile.getLocations().isEmpty() ? repository.parseLocations(profile.getLayoutData()) : profile.getLocations();

        List<WarehouseOptimizeModels.SlottingAssignment> assignments =
                WarehouseOptimizeAlgorithms.assignProducts(
                        products,
                        locations,
                        profile.getId(),
                        tenantContext.getCustomerCode(),
                        request.getAlgorithm());

        repository.replaceAssignments(
                profile.getId(),
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                assignments);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("a_class", assignments.stream().filter(a -> "A".equalsIgnoreCase(a.getVelocityClass())).count());
        summary.put("b_class", assignments.stream().filter(a -> "B".equalsIgnoreCase(a.getVelocityClass())).count());
        summary.put("c_class", assignments.stream().filter(a -> "C".equalsIgnoreCase(a.getVelocityClass())).count());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("assignments", assignments);
        response.put("count", assignments.size());
        response.put("summary", summary);
        return response;
    }

    public Map<String, Object> getAssignments(Long profileId) {
        List<WarehouseOptimizeModels.SlottingAssignment> assignments = repository.loadAssignments(
                profileId,
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("a_class", assignments.stream().filter(a -> "A".equalsIgnoreCase(a.getVelocityClass())).count());
        summary.put("b_class", assignments.stream().filter(a -> "B".equalsIgnoreCase(a.getVelocityClass())).count());
        summary.put("c_class", assignments.stream().filter(a -> "C".equalsIgnoreCase(a.getVelocityClass())).count());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("assignments", assignments);
        response.put("count", assignments.size());
        response.put("summary", summary);
        return response;
    }

    public void clearAssignments(Long profileId) {
        repository.clearAssignments(
                profileId,
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
    }

    public Map<String, Object> generateOrders(WarehouseOptimizeRequests.GenerateOrdersRequest request) {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = getProfile(request.getProfileId());
        List<WarehouseOptimizeModels.SlottingAssignment> assignments = repository.loadAssignments(
                request.getProfileId(),
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
        if (assignments.isEmpty()) {
            throw new IllegalStateException("Please run slotting optimization first before generating orders.");
        }

        Random random = new Random(tenantContext.getCustomerCode().hashCode() ^ profile.getId().intValue());
        int orderCount = request.getCount() == null ? 10 : Math.max(1, request.getCount());
        List<WarehouseOptimizeModels.PickOrder> orders = new ArrayList<>();
        for (int orderIndex = 0; orderIndex < orderCount; orderIndex++) {
            WarehouseOptimizeModels.PickOrder order = new WarehouseOptimizeModels.PickOrder();
            order.setProfileId(profile.getId());
            order.setCustomerCode(tenantContext.getCustomerCode());
            order.setOrderNumber(String.format("ORD-%03d", orderIndex + 1));
            order.setPriority(1 + random.nextInt(9));
            order.setStatus("PENDING");

            int lineCount = Math.min(6, Math.max(2, 2 + random.nextInt(4)));
            List<WarehouseOptimizeModels.OrderLine> lines = new ArrayList<>();
            int totalQty = 0;
            Collections.shuffle(assignments, random);
            for (int lineIndex = 0; lineIndex < lineCount && lineIndex < assignments.size(); lineIndex++) {
                WarehouseOptimizeModels.SlottingAssignment assignment = assignments.get(lineIndex);
                WarehouseOptimizeModels.OrderLine line = new WarehouseOptimizeModels.OrderLine();
                line.setSku(assignment.getProductSku());
                line.setProductName(assignment.getProductName());
                line.setLocation(assignment.getLocation());
                line.setQuantity(1 + random.nextInt(5));
                line.setOriginalSequence(lineIndex + 1);
                lines.add(line);
                totalQty += line.getQuantity();
            }
            order.setLines(lines);
            order.setLinesCount(lines.size());
            order.setTotalQuantity(totalQty);
            orders.add(order);
        }

        repository.replaceOrders(
                profile.getId(),
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                orders);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("ordersCreated", orders.size());
        response.put("linesCreated", orders.stream().mapToInt(o -> o.getLines().size()).sum());
        return response;
    }

    public Map<String, Object> uploadOrders(WarehouseOptimizeRequests.UploadOrdersRequest request) {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = getProfile(request.getProfileId());
        Map<String, WarehouseOptimizeModels.ProductRecord> products = repository.loadProductsBySku(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());

        List<WarehouseOptimizeModels.PickOrder> orders = new ArrayList<>();
        for (WarehouseOptimizeRequests.OrderUpload upload : request.getOrders()) {
            WarehouseOptimizeModels.PickOrder order = new WarehouseOptimizeModels.PickOrder();
            order.setProfileId(profile.getId());
            order.setCustomerCode(tenantContext.getCustomerCode());
            order.setOrderNumber(upload.getOrderNumber());
            order.setPriority(upload.getPriority() == null ? 5 : upload.getPriority());
            order.setStatus("PENDING");

            List<WarehouseOptimizeModels.OrderLine> lines = new ArrayList<>();
            int totalQty = 0;
            int index = 1;
            for (WarehouseOptimizeRequests.OrderUploadLine uploadLine : upload.getLines()) {
                WarehouseOptimizeModels.ProductRecord product = products.get(uploadLine.getSku());
                WarehouseOptimizeModels.OrderLine line = new WarehouseOptimizeModels.OrderLine();
                line.setSku(uploadLine.getSku());
                line.setProductName(product == null ? uploadLine.getSku() : product.getName());
                line.setLocation(uploadLine.getLocation());
                line.setQuantity(uploadLine.getQuantity() == null ? 1 : uploadLine.getQuantity());
                line.setOriginalSequence(index++);
                lines.add(line);
                totalQty += line.getQuantity();
            }
            order.setLines(lines);
            order.setLinesCount(lines.size());
            order.setTotalQuantity(totalQty);
            orders.add(order);
        }

        repository.replaceOrders(
                profile.getId(),
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                orders);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("ordersCreated", orders.size());
        response.put("linesCreated", orders.stream().mapToInt(o -> o.getLines().size()).sum());
        return response;
    }

    public List<WarehouseOptimizeModels.PickOrder> getOrders(Long profileId) {
        return repository.loadOrders(
                profileId,
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
    }

    public WarehouseOptimizeModels.PickOrder getOrderDetail(Long orderId) {
        WarehouseOptimizeModels.PickOrder order = repository.loadOrderDetail(
                orderId,
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        return order;
    }

    public WarehouseOptimizeModels.RouteOptimizationResult optimizeRoute(Long orderId, WarehouseOptimizeRequests.RouteOptimizeRequest request) {
        WarehouseOptimizeModels.PickOrder order = getOrderDetail(orderId);
        WarehouseOptimizeModels.RouteNode startPoint = request.getStartPoint();
        if (startPoint == null) {
            startPoint = new WarehouseOptimizeModels.RouteNode();
            startPoint.setName("START");
            startPoint.setX(BigDecimal.ZERO);
            startPoint.setY(BigDecimal.ZERO);
            startPoint.setLevel(1);
        }

        List<WarehouseOptimizeModels.RouteNode> locations = request.getLocations();
        if (locations == null || locations.isEmpty()) {
            WarehouseOptimizeModels.WarehouseProfileDetail profile = getProfile(order.getProfileId());
            Map<String, WarehouseOptimizeModels.WarehouseLayoutLocation> locationMap = profile.getLocations().stream()
                    .collect(Collectors.toMap(WarehouseOptimizeModels.WarehouseLayoutLocation::getLocation, location -> location, (left, right) -> left, LinkedHashMap::new));
            locations = new ArrayList<>();
            for (WarehouseOptimizeModels.OrderLine line : order.getLines()) {
                WarehouseOptimizeModels.WarehouseLayoutLocation layoutLocation = locationMap.get(line.getLocation());
                WarehouseOptimizeModels.RouteNode node = new WarehouseOptimizeModels.RouteNode();
                node.setName(line.getLocation());
                node.setSku(line.getSku());
                node.setX(layoutLocation == null ? BigDecimal.ZERO : layoutLocation.getX());
                node.setY(layoutLocation == null ? BigDecimal.ZERO : layoutLocation.getY());
                node.setLevel(layoutLocation == null ? 1 : layoutLocation.getLevel());
                locations.add(node);
            }
        }

        WarehouseOptimizeModels.RouteOptimizationResult result =
                WarehouseOptimizeAlgorithms.optimizeRoute(orderId, startPoint, locations);
        repository.saveOptimizationResults(orderId, result);
        return result;
    }

    public List<Map<String, Object>> getRouteResults(Long orderId) {
        getOrderDetail(orderId);
        return repository.loadStoredOptimizationResults(orderId);
    }

    public WarehouseOptimizeModels.WarehouseOptimizeAnalytics getAnalytics(Long profileId) {
        getProfile(profileId);
        return repository.loadAnalytics(
                profileId,
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode());
    }

    public String exportAnalyticsCsv(Long profileId) {
        WarehouseOptimizeModels.WarehouseOptimizeAnalytics analytics = getAnalytics(profileId);
        StringBuilder csv = new StringBuilder();
        csv.append("section,key,value\n");
        for (WarehouseOptimizeModels.VelocityDistribution item : analytics.getSlotting().getVelocityDistribution()) {
            csv.append("slotting_velocity,").append(item.getVelocityClass()).append(",").append(item.getCount()).append("\n");
        }
        for (WarehouseOptimizeModels.ZoneDistribution item : analytics.getSlotting().getZoneDistribution()) {
            csv.append("slotting_zone,").append(item.getZone()).append(",").append(item.getCount()).append("\n");
        }
        for (WarehouseOptimizeModels.AlgorithmComparison item : analytics.getPicking().getAlgorithmComparison()) {
            csv.append("picking_algorithm,").append(item.getAlgorithm()).append(",").append(item.getAvgImprovement()).append("\n");
        }
        csv.append("picking_summary,total_orders,").append(analytics.getPicking().getTotalOrders()).append("\n");
        csv.append("picking_summary,optimized_orders,").append(analytics.getPicking().getOptimizedOrders()).append("\n");
        return csv.toString();
    }

    public WarehouseOptimizeModels.StockSyncSnapshot syncStock(Long profileId, String requestedCustomerCode, Object layoutDataOverride) {
        getProfileSummary(profileId);
        String customerCode = resolveAuthorizedCustomer(requestedCustomerCode);
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting hierarchySetting = resolveEffectiveHierarchySetting(
                requireCompanyCode(),
                requireWarehouseCode(),
                customerCode);
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> viewerLocations =
                resolveViewerLocations(profileId, layoutDataOverride);
        List<WarehouseOptimizeModels.OracleStockRow> rows = repository.loadOracleStockRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode,
                null);
        return buildSnapshot(profileId, viewerLocations, customerCode, rows, hierarchySetting == null ? null : hierarchySetting.getMapping(), null, true);
    }

    public WarehouseOptimizeModels.StockDelta loadStockDelta(
            Long profileId,
            String requestedCustomerCode,
            LocalDateTime previousCursor,
            Object layoutDataOverride) {
        getProfileSummary(profileId);
        String customerCode = resolveAuthorizedCustomer(requestedCustomerCode);
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting hierarchySetting = resolveEffectiveHierarchySetting(
                requireCompanyCode(),
                requireWarehouseCode(),
                customerCode);
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> viewerLocations =
                resolveViewerLocations(profileId, layoutDataOverride);
        List<WarehouseOptimizeModels.OracleStockRow> rows = repository.loadOracleStockRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode,
                previousCursor);
        if (rows.isEmpty()) {
            WarehouseOptimizeModels.StockDelta delta = new WarehouseOptimizeModels.StockDelta();
            delta.setProfileId(profileId);
            delta.setCustomerCode(customerCode);
            delta.setPreviousCursor(previousCursor);
            delta.setCursor(previousCursor == null ? LocalDateTime.now() : previousCursor);
            return delta;
        }
        WarehouseOptimizeModels.StockSyncSnapshot snapshot = buildSnapshot(
                profileId,
                viewerLocations,
                customerCode,
                rows,
                hierarchySetting == null ? null : hierarchySetting.getMapping(),
                previousCursor,
                false);
        WarehouseOptimizeModels.StockDelta delta = new WarehouseOptimizeModels.StockDelta();
        delta.setProfileId(profileId);
        delta.setCustomerCode(customerCode);
        delta.setPreviousCursor(previousCursor);
        delta.setCursor(snapshot.getCursor());
        List<WarehouseOptimizeModels.LiveLocationState> changed = new ArrayList<>(snapshot.getLocations());
        changed.forEach(state -> state.setBlink(true));
        delta.setChangedLocations(changed);
        delta.setDiagnostics(snapshot.getDiagnostics());
        return delta;
    }

    public WarehouseOptimizeModels.WorkingStatusSnapshotResponse loadWorkingStatusSnapshot(String requestedCustomerCode) {
        String customerCode = resolveAuthorizedCustomer(requestedCustomerCode);
        List<WarehouseOptimizeModels.OracleStatusAggregateRow> inboundRows = repository.loadInboundStatusAggregateRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode);
        List<WarehouseOptimizeModels.OracleStatusAggregateRow> outboundRows = repository.loadOutboundStatusAggregateRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode);

        WarehouseOptimizeModels.WorkingStatusSnapshotResponse response = new WarehouseOptimizeModels.WorkingStatusSnapshotResponse();
        response.setGeneratedAt(LocalDateTime.now());

        List<WarehouseOptimizeModels.WorkingStatusSnapshotItem> statuses = new ArrayList<>();
        statuses.addAll(buildWorkingStatusItems("INBOUND", INBOUND_WORKING_STATUS_DEFINITIONS, inboundRows));
        statuses.addAll(buildWorkingStatusItems("OUTBOUND", OUTBOUND_WORKING_STATUS_DEFINITIONS, outboundRows));
        response.setStatuses(statuses);
        return response;
    }

    private WarehouseOptimizeModels.AutoGeneratedLayoutResponse synthesizeLayout(
            List<WarehouseOptimizeModels.OracleLocationMasterRow> sourceRows,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping hierarchyMapping) {
        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = new WarehouseOptimizeModels.AutoGeneratedLayoutResponse();
        WarehouseOptimizeModels.AutoGeneratedLayoutSummary summary = response.getSummary();
        LinkedHashSet<String> warnings = new LinkedHashSet<>();
        boolean hierarchyActive = hierarchyMapping != null;
        boolean hierarchyUsesSlot = hierarchyActive && defaultIfBlank(hierarchyMapping.getSlot(), null) != null;

        if (sourceRows == null || sourceRows.isEmpty()) {
            WarehouseOptimizeModels.WarehouseLayout emptyLayout = new WarehouseOptimizeModels.WarehouseLayout();
            emptyLayout.setWarehouseWidth(roundLayoutNumber(OUTER_PADDING.multiply(new BigDecimal("2"))));
            emptyLayout.setWarehouseHeight(roundLayoutNumber(OUTER_PADDING.multiply(new BigDecimal("2"))));
            emptyLayout.setBoundaryPolygon(null);
            emptyLayout.setAisles(new ArrayList<>());
            response.setLayout(emptyLayout);
            summary.setWarehouseWidth(emptyLayout.getWarehouseWidth());
            summary.setWarehouseHeight(emptyLayout.getWarehouseHeight());
            warnings.add("No active GWH_TM_LOC rows were found for the current warehouse.");
            response.setWarnings(new ArrayList<>(warnings));
            return response;
        }

        Map<String, List<GeneratedLocationSeed>> areaMap = new LinkedHashMap<>();
        Map<String, String> areaDisplayLabels = new LinkedHashMap<>();
        int inferredPositions = 0;
        int inferredLevels = 0;
        int skippedRows = 0;
        int normalizedDimensionRows = 0;
        int normalizedHeightRows = 0;

        for (WarehouseOptimizeModels.OracleLocationMasterRow row : sourceRows) {
            ResolvedHierarchyParts resolved = hierarchyActive
                    ? resolveMappedLocationMasterParts(row, hierarchyMapping)
                    : resolveDefaultLocationMasterParts(row);
            if (!resolved.valid) {
                skippedRows++;
                warnings.add("Skipped location " + defaultIfBlank(trimmed(row.getLocationCode()), "(blank LOC_COD)") + " because " + resolved.invalidReason + ".");
                continue;
            }

            GeneratedLocationSeed seed = new GeneratedLocationSeed();
            seed.locationCode = trimmed(row.getLocationCode());
            seed.groupKey = resolved.groupKey;
            seed.areaCode = resolved.zone;
            seed.aisleCode = resolved.aisle;
            seed.areaName = trimmed(row.getAreaName());
            seed.rackCode = hierarchyActive
                    ? SYNTHETIC_RACK_KEY
                    : defaultIfBlank(trimmed(row.getRackCode()), SYNTHETIC_RACK_KEY);
            seed.positionCode = hierarchyActive ? resolved.bay : resolved.bay;
            seed.slotCode = hierarchyUsesSlot ? resolved.slot : null;
            seed.levelCode = resolved.level;
            NormalizedMeasurement normalizedLength = normalizeMeasurement(row.getLength(), MIN_BAY_DEPTH, MAX_BAY_DEPTH);
            NormalizedMeasurement normalizedWidth = normalizeMeasurement(row.getWidth(), MIN_BAY_WIDTH, MAX_BAY_WIDTH);
            NormalizedMeasurement normalizedHeight = normalizeMeasurement(row.getHeight(), MIN_LEVEL_HEIGHT, MAX_LEVEL_HEIGHT);
            seed.length = normalizedLength.value;
            seed.width = normalizedWidth.value;
            seed.height = normalizedHeight.value;
            seed.dimensionNormalized = normalizedLength.scaled || normalizedWidth.scaled;
            seed.heightNormalized = normalizedHeight.scaled;
            seed.sourceRackCode = hierarchyActive ? resolved.bay : trimmed(row.getRackCode());
            seed.sourcePositionCode = hierarchyActive ? resolved.slot : trimmed(row.getPositionCode());
            seed.sourceLevelCode = trimmed(row.getLevelCode());

            if (!hierarchyActive && seed.sourcePositionCode == null) {
                inferredPositions++;
            }
            if (!hierarchyActive && seed.sourceLevelCode == null) {
                inferredLevels++;
            }
            if (seed.dimensionNormalized) {
                normalizedDimensionRows++;
            }
            if (seed.heightNormalized) {
                normalizedHeightRows++;
            }

            areaMap.computeIfAbsent(seed.groupKey, key -> new ArrayList<>()).add(seed);
            areaDisplayLabels.putIfAbsent(seed.groupKey, areaDisplayLabel(seed.areaCode, seed.aisleCode));
        }

        List<String> orderedAreas = new ArrayList<>(areaMap.keySet());
        orderedAreas.sort((left, right) -> naturalCompare(
                defaultIfBlank(areaDisplayLabels.get(left), left),
                defaultIfBlank(areaDisplayLabels.get(right), right)));

        Set<String> seenCanonicalLocations = new HashSet<>();
        List<GeneratedAreaLayout> generatedAreas = new ArrayList<>();
        int totalRacks = 0;
        int totalLocations = 0;
        int rotatedAreas = 0;
        int mixedAreasDetected = 0;
        int mixedAreasUncertain = 0;

        for (int areaIndex = 0; areaIndex < orderedAreas.size(); areaIndex++) {
            String groupKey = orderedAreas.get(areaIndex);
            List<GeneratedLocationSeed> areaRows = areaMap.get(groupKey);
            String areaLabel = defaultIfBlank(areaDisplayLabels.get(groupKey), groupKey);

            if (!hierarchyActive) {
                List<GeneratedLocationSeed> missingPositionRows = areaRows.stream()
                        .filter(seed -> seed.positionCode == null)
                        .sorted(Comparator.comparing(
                                seed -> defaultIfBlank(seed.locationCode, ""),
                                this::naturalCompare))
                        .collect(Collectors.toList());
                for (int positionIndex = 0; positionIndex < missingPositionRows.size(); positionIndex++) {
                    missingPositionRows.get(positionIndex).positionCode = String.format("AUTO_PSTN_%05d", positionIndex + 1);
                }
            }

            List<GeneratedLocationSeed> deduplicatedRows = new ArrayList<>();
            for (GeneratedLocationSeed seed : areaRows) {
                String canonicalLocation = hierarchyActive
                        ? canonicalLocation(seed.areaCode, seed.aisleCode, seed.positionCode, seed.slotCode, seed.levelCode)
                        : canonicalLocation(seed.areaCode, seed.rackCode, seed.positionCode, seed.levelCode);
                if (!seenCanonicalLocations.add(canonicalLocation)) {
                    skippedRows++;
                    warnings.add("Skipped duplicate canonical location " + canonicalLocation + " in " + areaLabel + ".");
                    continue;
                }
                seed.canonicalLocation = canonicalLocation;
                deduplicatedRows.add(seed);
            }

            if (deduplicatedRows.isEmpty()) {
                warnings.add(areaLabel + " was ignored because no unique locations remained after normalization.");
                continue;
            }

            GeneratedAreaLayout generatedArea = buildGeneratedArea(
                    deduplicatedRows.get(0).areaCode,
                    deduplicatedRows.get(0).aisleCode,
                    areaIndex + 1,
                    deduplicatedRows,
                    hierarchyUsesSlot);
            generatedAreas.add(generatedArea);
            totalRacks += generatedArea.rackCount;
            totalLocations += generatedArea.aisle.getLocations().size();
            rotatedAreas += generatedArea.rotated ? 1 : 0;
            mixedAreasDetected += generatedArea.mixedDetected ? 1 : 0;
            mixedAreasUncertain += generatedArea.mixedUncertain ? 1 : 0;
            warnings.addAll(generatedArea.warnings);
        }

        WarehouseOptimizeModels.WarehouseLayout layout = packGeneratedAreas(generatedAreas);
        response.setLayout(layout);

        summary.setAreas(generatedAreas.size());
        summary.setRacks(totalRacks);
        summary.setLocations(totalLocations);
        summary.setInferredPositions(inferredPositions);
        summary.setInferredLevels(inferredLevels);
        summary.setSkippedRows(skippedRows);
        summary.setWarehouseWidth(layout.getWarehouseWidth());
        summary.setWarehouseHeight(layout.getWarehouseHeight());
        summary.setNormalizedDimensionRows(normalizedDimensionRows);
        summary.setNormalizedHeightRows(normalizedHeightRows);
        summary.setRotatedAreas(rotatedAreas);
        summary.setMixedAreasDetected(mixedAreasDetected);
        summary.setMixedAreasUncertain(mixedAreasUncertain);

        if (generatedAreas.isEmpty()) {
            warnings.add("No layout areas could be generated from the current warehouse location master.");
        }
        if (normalizedDimensionRows > 0) {
            warnings.add(normalizedDimensionRows + " location rows used normalized LOC_LEN or LOC_WID values to keep the generated layout realistic.");
        }
        if (normalizedHeightRows > 0) {
            warnings.add(normalizedHeightRows + " location rows used normalized LOC_HIG values for mixed shelf-to-rack detection.");
        }
        if (mixedAreasUncertain > 0) {
            warnings.add(mixedAreasUncertain + " areas showed possible mixed shelf-to-rack patterns but were left unchanged because the signal was not clear enough.");
        }
        BigDecimal footprintArea = zeroIfNull(layout.getWarehouseWidth()).multiply(zeroIfNull(layout.getWarehouseHeight()));
        if (zeroIfNull(layout.getWarehouseWidth()).compareTo(LARGE_FOOTPRINT_SIDE_THRESHOLD) > 0
                || zeroIfNull(layout.getWarehouseHeight()).compareTo(LARGE_FOOTPRINT_SIDE_THRESHOLD) > 0
                || footprintArea.compareTo(LARGE_FOOTPRINT_AREA_THRESHOLD) > 0) {
            warnings.add("Generated warehouse footprint is still unusually large. Review area master granularity and dimension quality for this warehouse.");
        }

        response.setWarnings(new ArrayList<>(warnings));
        return response;
    }

    private GeneratedAreaLayout buildGeneratedArea(
            String areaCode,
            String aisleCode,
            int areaOrdinal,
            List<GeneratedLocationSeed> areaRows,
            boolean explicitBaySlotMode) {
        List<String> rackCodes = explicitBaySlotMode
                ? Collections.singletonList(SYNTHETIC_RACK_KEY)
                : areaRows.stream()
                        .map(seed -> seed.rackCode)
                        .distinct()
                        .sorted(this::naturalCompare)
                        .collect(Collectors.toList());
        List<String> positionCodes = areaRows.stream()
                .map(seed -> seed.positionCode)
                .distinct()
                .sorted(this::naturalCompare)
                .collect(Collectors.toList());
        List<String> levelCodes = areaRows.stream()
                .map(seed -> seed.levelCode)
                .distinct()
                .sorted(this::naturalCompare)
                .collect(Collectors.toList());

        Map<String, Integer> rackIndexMap = new LinkedHashMap<>();
        for (int index = 0; index < rackCodes.size(); index++) {
            rackIndexMap.put(rackCodes.get(index), index);
        }

        Map<String, Integer> positionIndexMap = new LinkedHashMap<>();
        for (int index = 0; index < positionCodes.size(); index++) {
            positionIndexMap.put(positionCodes.get(index), index + 1);
        }

        Map<String, Integer> levelIndexMap = new LinkedHashMap<>();
        for (int index = 0; index < levelCodes.size(); index++) {
            levelIndexMap.put(levelCodes.get(index), index + 1);
        }

        BigDecimal bayDepth = medianPositive(
                areaRows.stream().map(seed -> seed.length).collect(Collectors.toList()),
                DEFAULT_BAY_DEPTH);
        BigDecimal bayWidth = medianPositive(
                areaRows.stream().map(seed -> seed.width).collect(Collectors.toList()),
                DEFAULT_BAY_WIDTH);
        BigDecimal aisleWidth = DEFAULT_AISLE_WIDTH;
        int rackCount = explicitBaySlotMode ? 1 : Math.max(1, rackCodes.size());
        int positionCount = Math.max(1, positionCodes.size());
        boolean hasUsableRackStructure = explicitBaySlotMode || areaRows.stream()
                .anyMatch(seed -> seed.sourceRackCode != null || seed.sourcePositionCode != null);
        int levels = Math.max(1, levelCodes.size());
        MixedDetectionResult mixedDetection = hasUsableRackStructure
                ? detectMixedLowerShelfLevels(areaCode, rackCodes, areaRows, levelIndexMap)
                : MixedDetectionResult.none();
        PositionLayoutPlan positionLayoutPlan = explicitBaySlotMode
                ? buildSlotPlacementPlan(areaRows, positionCodes, levelIndexMap, bayWidth)
                : buildPositionLayoutPlan(
                        areaRows,
                        rackCodes,
                        positionCodes,
                        levelIndexMap,
                        mixedDetection.lowerShelfLevels,
                        bayWidth);
        positionCount = Math.max(1, positionLayoutPlan.structuralPositionCount);
        BigDecimal positionSpan = roundLayoutNumber(bayWidth.multiply(BigDecimal.valueOf(positionCount)));
        RackPackingPlan rackPackingPlan = chooseRackPackingPlan(rackCount, bayDepth, aisleWidth, positionSpan);

        BigDecimal verticalWidth = roundLayoutNumber(rackPackingPlan.width);
        BigDecimal verticalHeight = roundLayoutNumber(rackPackingPlan.height);
        BigDecimal horizontalWidth = roundLayoutNumber(verticalHeight);
        BigDecimal horizontalHeight = roundLayoutNumber(verticalWidth);
        boolean rotateArea = shouldRotateArea(verticalWidth, verticalHeight, horizontalWidth, horizontalHeight);

        WarehouseOptimizeModels.WarehouseAisle aisle = new WarehouseOptimizeModels.WarehouseAisle();
        aisle.setId(areaOrdinal);
        aisle.setX(BigDecimal.ZERO);
        aisle.setY(BigDecimal.ZERO);
        aisle.setWidth(rotateArea ? horizontalWidth : verticalWidth);
        aisle.setHeight(rotateArea ? horizontalHeight : verticalHeight);
        aisle.setType(hasUsableRackStructure ? "HIGH_RACK" : "FLOOR");
        aisle.setDirection(rotateArea ? "HORIZONTAL" : "VERTICAL");
        aisle.setLevels(levels);
        aisle.setZone(areaCode);
        aisle.setAisleCode(aisleCode);
        aisle.setBayDepth(roundLayoutNumber(bayDepth));
        aisle.setBayWidth(roundLayoutNumber(bayWidth));
        aisle.setAisleWidth(roundLayoutNumber(aisleWidth));
        aisle.setTunnelLevelFrom(1);
        aisle.setTunnelLevelTo(aisle.getLevels());
        aisle.setLowerShelfLevels(aisle.getType().equals("HIGH_RACK") ? mixedDetection.lowerShelfLevels : 0);
        aisle.setPickFaceLevels(Collections.singletonList(1));
        aisle.setStartPointX(BigDecimal.ZERO);
        aisle.setStartPointY(BigDecimal.ZERO);

        List<GeneratedLocationSeed> orderedRows = new ArrayList<>(areaRows);
        orderedRows.sort((left, right) -> {
            int rackCompare = naturalCompare(left.rackCode, right.rackCode);
            if (rackCompare != 0) {
                return rackCompare;
            }
            int positionCompare = naturalCompare(left.positionCode, right.positionCode);
            if (positionCompare != 0) {
                return positionCompare;
            }
            int slotCompare = naturalCompare(left.slotCode, right.slotCode);
            if (slotCompare != 0) {
                return slotCompare;
            }
            int levelCompare = naturalCompare(left.levelCode, right.levelCode);
            if (levelCompare != 0) {
                return levelCompare;
            }
            return naturalCompare(defaultIfBlank(left.locationCode, ""), defaultIfBlank(right.locationCode, ""));
        });

        List<WarehouseOptimizeModels.WarehouseLayoutLocation> locations = new ArrayList<>();
        for (GeneratedLocationSeed seed : orderedRows) {
            int rackIndex = rackIndexMap.get(seed.rackCode);
            PositionPlacement placement = positionLayoutPlan.placements.get(seed.canonicalLocation);
            int positionOrdinal = placement == null
                    ? positionIndexMap.get(seed.positionCode)
                    : placement.positionOrdinal;
            int levelOrdinal = levelIndexMap.get(seed.levelCode);
            int rackRow = rackPackingPlan.rackRow(rackIndex);
            int rackColumn = rackPackingPlan.rackColumn(rackIndex);
            BigDecimal rackOffset = BigDecimal.valueOf(rackColumn).multiply(bayDepth.add(aisleWidth));
            BigDecimal rowOffset = BigDecimal.valueOf(rackRow).multiply(positionSpan.add(INTERNAL_RACK_ROW_GAP));
            BigDecimal positionOffset = placement == null
                    ? BigDecimal.valueOf(positionOrdinal - 1).multiply(bayWidth)
                    : placement.axisOffset;

            WarehouseOptimizeModels.WarehouseLayoutLocation location = new WarehouseOptimizeModels.WarehouseLayoutLocation();
            location.setLocation(seed.canonicalLocation);
            location.setZone(areaCode);
            String locationType = aisle.getType();
            if ("HIGH_RACK".equals(aisle.getType())
                    && aisle.getLowerShelfLevels() != null
                    && levelOrdinal <= aisle.getLowerShelfLevels()) {
                locationType = "SHELF";
            }
            location.setType(locationType);
            location.setLevel(levelOrdinal);
            location.setAisle(areaOrdinal);
            location.setPosition(positionOrdinal);
            location.setSide(null);
            if (rotateArea) {
                location.setX(roundLayoutNumber(rowOffset.add(positionOffset)));
                location.setY(roundLayoutNumber(rackOffset));
                if (placement != null && placement.axisSpan != null) {
                    location.setFootprintWidth(roundLayoutNumber(placement.axisSpan));
                }
            } else {
                location.setX(roundLayoutNumber(rackOffset));
                location.setY(roundLayoutNumber(rowOffset.add(positionOffset)));
                if (placement != null && placement.axisSpan != null) {
                    location.setFootprintDepth(roundLayoutNumber(placement.axisSpan));
                }
            }
            locations.add(location);
        }
        aisle.setLocations(locations);

        GeneratedAreaLayout generatedArea = new GeneratedAreaLayout();
        generatedArea.aisle = aisle;
        generatedArea.rackCount = rackCount;
        generatedArea.rotated = rotateArea;
        generatedArea.mixedDetected = mixedDetection.lowerShelfLevels > 0;
        generatedArea.mixedUncertain = mixedDetection.uncertain;
        generatedArea.warnings.addAll(mixedDetection.warnings);
        return generatedArea;
    }

    private PositionLayoutPlan buildPositionLayoutPlan(
            List<GeneratedLocationSeed> areaRows,
            List<String> rackCodes,
            List<String> positionCodes,
            Map<String, Integer> levelIndexMap,
            int lowerShelfLevels,
            BigDecimal bayWidth) {
        PositionLayoutPlan plan = new PositionLayoutPlan();
        plan.structuralPositionCount = Math.max(1, positionCodes.size());
        if (lowerShelfLevels <= 0) {
            return plan;
        }

        Map<String, Map<Integer, List<GeneratedLocationSeed>>> rowsByRackAndLevel = new LinkedHashMap<>();
        for (String rackCode : rackCodes) {
            rowsByRackAndLevel.put(rackCode, new LinkedHashMap<>());
        }
        for (GeneratedLocationSeed seed : areaRows) {
            Integer levelOrdinal = levelIndexMap.get(seed.levelCode);
            if (levelOrdinal == null) {
                continue;
            }
            rowsByRackAndLevel
                    .computeIfAbsent(seed.rackCode, key -> new LinkedHashMap<>())
                    .computeIfAbsent(levelOrdinal, key -> new ArrayList<>())
                    .add(seed);
        }
        for (Map<Integer, List<GeneratedLocationSeed>> rowsByLevel : rowsByRackAndLevel.values()) {
            for (List<GeneratedLocationSeed> seeds : rowsByLevel.values()) {
                seeds.sort((left, right) -> {
                    int positionCompare = naturalCompare(left.positionCode, right.positionCode);
                    if (positionCompare != 0) {
                        return positionCompare;
                    }
                    return naturalCompare(defaultIfBlank(left.locationCode, ""), defaultIfBlank(right.locationCode, ""));
                });
            }
        }

        if (lowerShelfLevels > 0) {
            plan.structuralPositionCount = inferStructuralPositionCount(rowsByRackAndLevel, lowerShelfLevels, plan.structuralPositionCount);
        }

        for (Map<Integer, List<GeneratedLocationSeed>> rowsByLevel : rowsByRackAndLevel.values()) {
            for (Map.Entry<Integer, List<GeneratedLocationSeed>> entry : rowsByLevel.entrySet()) {
                assignPositionPlacements(plan, entry.getValue(), bayWidth);
            }
        }

        return plan;
    }

    private PositionLayoutPlan buildSlotPlacementPlan(
            List<GeneratedLocationSeed> areaRows,
            List<String> positionCodes,
            Map<String, Integer> levelIndexMap,
            BigDecimal bayWidth) {
        PositionLayoutPlan plan = new PositionLayoutPlan();
        plan.structuralPositionCount = Math.max(1, positionCodes.size());

        Map<String, Integer> positionIndexMap = new LinkedHashMap<>();
        for (int index = 0; index < positionCodes.size(); index++) {
            positionIndexMap.put(positionCodes.get(index), index + 1);
        }

        Map<String, Map<Integer, List<GeneratedLocationSeed>>> rowsByPositionAndLevel = new LinkedHashMap<>();
        for (GeneratedLocationSeed seed : areaRows) {
            Integer levelOrdinal = levelIndexMap.get(seed.levelCode);
            Integer positionOrdinal = positionIndexMap.get(seed.positionCode);
            if (levelOrdinal == null || positionOrdinal == null) {
                continue;
            }
            rowsByPositionAndLevel
                    .computeIfAbsent(seed.positionCode, key -> new LinkedHashMap<>())
                    .computeIfAbsent(levelOrdinal, key -> new ArrayList<>())
                    .add(seed);
        }

        for (Map.Entry<String, Map<Integer, List<GeneratedLocationSeed>>> positionEntry : rowsByPositionAndLevel.entrySet()) {
            int positionOrdinal = positionIndexMap.get(positionEntry.getKey());
            for (List<GeneratedLocationSeed> seeds : positionEntry.getValue().values()) {
                seeds.sort((left, right) -> {
                    int slotCompare = naturalCompare(left.slotCode, right.slotCode);
                    if (slotCompare != 0) {
                        return slotCompare;
                    }
                    return naturalCompare(defaultIfBlank(left.locationCode, ""), defaultIfBlank(right.locationCode, ""));
                });
                assignSlotPlacements(plan, seeds, positionOrdinal, bayWidth);
            }
        }

        return plan;
    }

    private int inferStructuralPositionCount(
            Map<String, Map<Integer, List<GeneratedLocationSeed>>> rowsByRackAndLevel,
            int lowerShelfLevels,
            int fallback) {
        List<Integer> candidateCounts = new ArrayList<>();
        for (Map<Integer, List<GeneratedLocationSeed>> rowsByLevel : rowsByRackAndLevel.values()) {
            for (Map.Entry<Integer, List<GeneratedLocationSeed>> entry : rowsByLevel.entrySet()) {
                if (entry.getKey() <= lowerShelfLevels || entry.getValue().isEmpty()) {
                    continue;
                }
                candidateCounts.add(entry.getValue().size());
            }
        }
        if (candidateCounts.isEmpty()) {
            return Math.max(1, fallback);
        }
        Collections.sort(candidateCounts);
        int middle = candidateCounts.size() / 2;
        int median = candidateCounts.size() % 2 == 0
                ? (candidateCounts.get(middle - 1) + candidateCounts.get(middle)) / 2
                : candidateCounts.get(middle);
        return Math.max(1, Math.min(Math.max(median, 1), fallback));
    }

    private void assignPositionPlacements(
            PositionLayoutPlan plan,
            List<GeneratedLocationSeed> orderedLevelSeeds,
            BigDecimal bayWidth) {
        if (orderedLevelSeeds == null || orderedLevelSeeds.isEmpty()) {
            return;
        }

        int structuralCount = Math.max(1, plan.structuralPositionCount);
        int levelCount = orderedLevelSeeds.size();
        Map<Integer, List<GeneratedLocationSeed>> seedsByOrdinal = new LinkedHashMap<>();
        for (int index = 0; index < levelCount; index++) {
            int ordinal = Math.min(structuralCount, (index * structuralCount) / levelCount + 1);
            seedsByOrdinal.computeIfAbsent(ordinal, key -> new ArrayList<>()).add(orderedLevelSeeds.get(index));
        }

        for (Map.Entry<Integer, List<GeneratedLocationSeed>> entry : seedsByOrdinal.entrySet()) {
            int ordinal = entry.getKey();
            List<GeneratedLocationSeed> bucket = entry.getValue();
            BigDecimal bucketSpan = bayWidth.divide(BigDecimal.valueOf(bucket.size()), 6, RoundingMode.HALF_UP);
            BigDecimal axisSpan = clampSpan(bucketSpan.multiply(SUBSLOT_FILL_RATIO), bayWidth);
            BigDecimal padding = bucketSpan.subtract(axisSpan).divide(new BigDecimal("2"), 6, RoundingMode.HALF_UP);
            BigDecimal baseOffset = bayWidth.multiply(BigDecimal.valueOf(ordinal - 1));
            for (int slotIndex = 0; slotIndex < bucket.size(); slotIndex++) {
                GeneratedLocationSeed seed = bucket.get(slotIndex);
                PositionPlacement placement = new PositionPlacement();
                placement.positionOrdinal = ordinal;
                placement.axisOffset = baseOffset
                        .add(bucketSpan.multiply(BigDecimal.valueOf(slotIndex)))
                        .add(padding);
                if (bucket.size() > 1) {
                    placement.axisSpan = axisSpan;
                }
                plan.placements.put(seed.canonicalLocation, placement);
            }
        }
    }

    private void assignSlotPlacements(
            PositionLayoutPlan plan,
            List<GeneratedLocationSeed> orderedSlotSeeds,
            int positionOrdinal,
            BigDecimal bayWidth) {
        if (orderedSlotSeeds == null || orderedSlotSeeds.isEmpty()) {
            return;
        }

        BigDecimal bucketSpan = bayWidth.divide(BigDecimal.valueOf(orderedSlotSeeds.size()), 6, RoundingMode.HALF_UP);
        BigDecimal axisSpan = clampSpan(bucketSpan.multiply(SUBSLOT_FILL_RATIO), bayWidth);
        BigDecimal padding = bucketSpan.subtract(zeroIfNull(axisSpan)).divide(new BigDecimal("2"), 6, RoundingMode.HALF_UP);
        BigDecimal baseOffset = bayWidth.multiply(BigDecimal.valueOf(positionOrdinal - 1));

        for (int slotIndex = 0; slotIndex < orderedSlotSeeds.size(); slotIndex++) {
            GeneratedLocationSeed seed = orderedSlotSeeds.get(slotIndex);
            PositionPlacement placement = new PositionPlacement();
            placement.positionOrdinal = positionOrdinal;
            placement.axisOffset = baseOffset
                    .add(bucketSpan.multiply(BigDecimal.valueOf(slotIndex)))
                    .add(axisSpan == null ? BigDecimal.ZERO : padding);
            if (orderedSlotSeeds.size() > 1 && axisSpan != null) {
                placement.axisSpan = axisSpan;
            }
            plan.placements.put(seed.canonicalLocation, placement);
        }
    }

    private BigDecimal clampSpan(BigDecimal preferred, BigDecimal maximum) {
        BigDecimal safePreferred = zeroIfNull(preferred);
        BigDecimal safeMaximum = zeroIfNull(maximum);
        if (safeMaximum.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        BigDecimal clamped = safePreferred.max(MIN_SUBSLOT_SPAN);
        if (clamped.compareTo(safeMaximum) > 0) {
            clamped = safeMaximum;
        }
        return clamped;
    }

    private RackPackingPlan chooseRackPackingPlan(
            int rackCount,
            BigDecimal bayDepth,
            BigDecimal aisleWidth,
            BigDecimal positionSpan) {
        RackPackingPlan bestPlan = null;
        int maxRackRows = Math.min(Math.max(rackCount, 1), 24);
        for (int rackRows = 1; rackRows <= maxRackRows; rackRows++) {
            RackPackingPlan candidate = new RackPackingPlan();
            candidate.rackRows = rackRows;
            candidate.racksPerRow = Math.max(1, (int) Math.ceil(rackCount / (double) rackRows));
            candidate.width = stripSpan(candidate.racksPerRow, bayDepth, aisleWidth);
            candidate.height = positionSpan.multiply(BigDecimal.valueOf(candidate.rackRows));
            if (candidate.rackRows > 1) {
                candidate.height = candidate.height.add(INTERNAL_RACK_ROW_GAP.multiply(BigDecimal.valueOf(candidate.rackRows - 1)));
            }
            candidate.maxSide = candidate.width.max(candidate.height);
            candidate.footprintArea = candidate.width.multiply(candidate.height);
            candidate.aspectPenalty = aspectPenalty(candidate.width, candidate.height);

            if (bestPlan == null || compareRackPackingPlans(candidate, bestPlan) < 0) {
                bestPlan = candidate;
            }
        }

        if (bestPlan == null) {
            bestPlan = new RackPackingPlan();
            bestPlan.rackRows = 1;
            bestPlan.racksPerRow = Math.max(1, rackCount);
            bestPlan.width = stripSpan(bestPlan.racksPerRow, bayDepth, aisleWidth);
            bestPlan.height = positionSpan;
            bestPlan.maxSide = bestPlan.width.max(bestPlan.height);
            bestPlan.footprintArea = bestPlan.width.multiply(bestPlan.height);
            bestPlan.aspectPenalty = aspectPenalty(bestPlan.width, bestPlan.height);
        }

        return bestPlan;
    }

    private int compareRackPackingPlans(RackPackingPlan left, RackPackingPlan right) {
        int maxSideCompare = left.maxSide.compareTo(right.maxSide);
        if (maxSideCompare != 0) {
            return maxSideCompare;
        }
        int areaCompare = left.footprintArea.compareTo(right.footprintArea);
        if (areaCompare != 0) {
            return areaCompare;
        }
        int aspectCompare = Double.compare(left.aspectPenalty, right.aspectPenalty);
        if (aspectCompare != 0) {
            return aspectCompare;
        }
        return Integer.compare(left.rackRows, right.rackRows);
    }

    private WarehouseOptimizeModels.WarehouseLayout packGeneratedAreas(List<GeneratedAreaLayout> generatedAreas) {
        WarehouseOptimizeModels.WarehouseLayout layout = new WarehouseOptimizeModels.WarehouseLayout();
        layout.setBoundaryPolygon(null);
        if (generatedAreas == null || generatedAreas.isEmpty()) {
            layout.setWarehouseWidth(roundLayoutNumber(OUTER_PADDING.multiply(new BigDecimal("2"))));
            layout.setWarehouseHeight(roundLayoutNumber(OUTER_PADDING.multiply(new BigDecimal("2"))));
            layout.setAisles(new ArrayList<>());
            return layout;
        }

        int areaCount = generatedAreas.size();
        PackedGrid bestGrid = null;
        int maxColumns = Math.min(areaCount, 60);
        for (int columnCount = 1; columnCount <= maxColumns; columnCount++) {
            PackedGrid candidate = buildPackedGrid(generatedAreas, columnCount);
            if (bestGrid == null || comparePackedGrids(candidate, bestGrid) < 0) {
                bestGrid = candidate;
            }
        }
        if (bestGrid == null) {
            bestGrid = buildPackedGrid(generatedAreas, Math.max(1, (int) Math.ceil(Math.sqrt(areaCount))));
        }

        List<WarehouseOptimizeModels.WarehouseAisle> aisles = new ArrayList<>();
        for (int index = 0; index < generatedAreas.size(); index++) {
            GeneratedAreaLayout generatedArea = generatedAreas.get(index);
            WarehouseOptimizeModels.WarehouseAisle aisle = generatedArea.aisle;
            int row = index / bestGrid.columns;
            int column = index % bestGrid.columns;

            BigDecimal offsetX = OUTER_PADDING;
            for (int cursor = 0; cursor < column; cursor++) {
                offsetX = offsetX.add(bestGrid.columnWidths[cursor]).add(AREA_GAP);
            }

            BigDecimal offsetY = OUTER_PADDING;
            for (int cursor = 0; cursor < row; cursor++) {
                offsetY = offsetY.add(bestGrid.rowHeights[cursor]).add(AREA_GAP);
            }

            aisle.setX(roundLayoutNumber(offsetX));
            aisle.setY(roundLayoutNumber(offsetY));
            aisle.setStartPointX(aisle.getX());
            aisle.setStartPointY(aisle.getY());

            for (WarehouseOptimizeModels.WarehouseLayoutLocation location : aisle.getLocations()) {
                location.setX(roundLayoutNumber(zeroIfNull(location.getX()).add(offsetX)));
                location.setY(roundLayoutNumber(zeroIfNull(location.getY()).add(offsetY)));
            }
            aisles.add(aisle);
        }

        layout.setWarehouseWidth(roundLayoutNumber(bestGrid.totalWidth));
        layout.setWarehouseHeight(roundLayoutNumber(bestGrid.totalHeight));
        layout.setAisles(aisles);
        return layout;
    }

    private WarehouseOptimizeModels.WarehouseLocationHierarchyResponse buildHierarchyResponse(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting warehouseDefault =
                repository.findLocationHierarchySetting(companyCode, warehouseCode, null);
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting customerOverride =
                customerCode == null ? null : repository.findLocationHierarchySetting(companyCode, warehouseCode, customerCode);
        WarehouseOptimizeModels.WarehouseLocationHierarchyResponse response = new WarehouseOptimizeModels.WarehouseLocationHierarchyResponse();
        response.setWarehouseDefault(warehouseDefault);
        response.setCustomerOverride(customerOverride);
        response.setEffective(customerOverride != null ? customerOverride : warehouseDefault);
        return response;
    }

    private WarehouseOptimizeModels.WarehouseLocationHierarchySetting resolveEffectiveHierarchySetting(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting customerOverride =
                customerCode == null ? null : repository.findLocationHierarchySetting(companyCode, warehouseCode, customerCode);
        if (customerOverride != null) {
            return customerOverride;
        }
        return repository.findLocationHierarchySetting(companyCode, warehouseCode, null);
    }

    private String requireCompanyCode() {
        String companyCode = defaultIfBlank(tenantContext.getCompanyCode(), null);
        if (companyCode == null) {
            throw new IllegalStateException("Tenant company context is not available");
        }
        return companyCode;
    }

    private WarehouseOptimizeModels.WarehouseProfileDetail buildSavedProfileResponse(
            WarehouseOptimizeModels.WarehouseProfileSummary summary,
            String layoutJson) {
        WarehouseOptimizeModels.WarehouseProfileDetail detail = new WarehouseOptimizeModels.WarehouseProfileDetail();
        detail.setId(summary.getId());
        detail.setCompanyCode(summary.getCompanyCode());
        detail.setWarehouseCode(summary.getWarehouseCode());
        detail.setCustomerCode(summary.getCustomerCode());
        detail.setProfileName(summary.getProfileName());
        detail.setDescription(summary.getDescription());
        detail.setWarehouseLength(summary.getWarehouseLength());
        detail.setWarehouseWidth(summary.getWarehouseWidth());
        detail.setCreatedAt(summary.getCreatedAt());
        detail.setUpdatedAt(summary.getUpdatedAt());
        detail.setSharedProfile(summary.isSharedProfile());
        detail.setLayoutData(layoutJson);
        detail.setLocations(Collections.emptyList());
        return detail;
    }

    private String requireWarehouseCode() {
        String warehouseCode = defaultIfBlank(tenantContext.getWarehouseCode(), null);
        if (warehouseCode == null) {
            throw new IllegalStateException("Tenant warehouse context is not available");
        }
        return warehouseCode;
    }

    private String resolveRequestedHierarchyCustomerCode(String requestedCustomerCode) {
        String desired = defaultIfBlank(requestedCustomerCode, null);
        return desired == null ? null : resolveAuthorizedCustomer(desired);
    }

    private String resolveScopedHierarchyCustomerCode(String requestedCustomerCode) {
        String desired = defaultIfBlank(requestedCustomerCode, tenantContext.getCustomerCode());
        return desired == null ? null : resolveAuthorizedCustomer(desired);
    }

    private String normalizeHierarchyScope(String scope) {
        String normalized = defaultIfBlank(scope, null);
        if (normalized == null) {
            throw new IllegalArgumentException("Hierarchy scope is required");
        }
        normalized = normalized.trim().toUpperCase();
        if (!WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE.equals(normalized)
                && !WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE_CUSTOMER.equals(normalized)) {
            throw new IllegalArgumentException("Unsupported hierarchy scope: " + normalized);
        }
        return normalized;
    }

    private WarehouseOptimizeModels.WarehouseLocationHierarchyMapping copyHierarchyMapping(
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping) {
        if (mapping == null) {
            return null;
        }
        WarehouseOptimizeModels.WarehouseLocationHierarchyMapping copy = new WarehouseOptimizeModels.WarehouseLocationHierarchyMapping();
        copy.setZone(defaultIfBlank(mapping.getZone(), null));
        copy.setAisle(defaultIfBlank(mapping.getAisle(), null));
        copy.setBay(defaultIfBlank(mapping.getBay(), null));
        copy.setSlot(defaultIfBlank(mapping.getSlot(), null));
        copy.setLevel(defaultIfBlank(mapping.getLevel(), null));
        return copy;
    }

    private void validateHierarchyMapping(WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping) {
        if (mapping == null) {
            throw new IllegalArgumentException("Hierarchy mapping is required");
        }
        validateHierarchySource("Zone", mapping.getZone(), false);
        validateHierarchySource("Aisle", mapping.getAisle(), false);
        validateHierarchySource("Rack / Bay", mapping.getBay(), true);
        validateHierarchySource("Position / Slot", mapping.getSlot(), false);
        validateHierarchySource("Level", mapping.getLevel(), true);
    }

    private void validateHierarchySource(String label, String source, boolean required) {
        String normalized = defaultIfBlank(source, null);
        if (normalized == null) {
            if (required) {
                throw new IllegalArgumentException(label + " mapping is required");
            }
            return;
        }
        if (!ALLOWED_HIERARCHY_SOURCES.contains(normalized.trim().toUpperCase())) {
            throw new IllegalArgumentException("Unsupported " + label + " mapping source: " + normalized);
        }
    }

    private ResolvedHierarchyParts resolveDefaultLocationMasterParts(WarehouseOptimizeModels.OracleLocationMasterRow row) {
        ResolvedHierarchyParts resolved = new ResolvedHierarchyParts();
        resolved.zone = trimmed(row.getAreaCode());
        if (resolved.zone == null) {
            resolved.invalidReason = "LOC_AREA_COD is blank";
            return resolved;
        }
        resolved.bay = trimmed(row.getPositionCode());
        resolved.level = defaultIfBlank(trimmed(row.getLevelCode()), "1");
        resolved.groupKey = resolved.zone;
        resolved.valid = true;
        return resolved;
    }

    private ResolvedHierarchyParts resolveMappedLocationMasterParts(
            WarehouseOptimizeModels.OracleLocationMasterRow row,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping) {
        ResolvedHierarchyParts resolved = resolveMappedParts(
                mapping,
                trimmed(row.getAreaCode()),
                trimmed(row.getRackCode()),
                trimmed(row.getPositionCode()),
                trimmed(row.getLevelCode()));
        if (!resolved.valid) {
            return resolved;
        }
        resolved.groupKey = hierarchyGroupKey(resolved.zone, resolved.aisle);
        return resolved;
    }

    private ResolvedHierarchyParts resolveMappedStockParts(
            WarehouseOptimizeModels.OracleStockRow row,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping) {
        ResolvedHierarchyParts resolved = resolveMappedParts(
                mapping,
                trimmed(row.getAreaCode()),
                trimmed(row.getRackCode()),
                trimmed(row.getPositionCode()),
                trimmed(row.getLevelCode()));
        if (!resolved.valid) {
            return resolved;
        }
        resolved.groupKey = hierarchyGroupKey(resolved.zone, resolved.aisle);
        return resolved;
    }

    private ResolvedHierarchyParts resolveMappedParts(
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping,
            String areaCode,
            String rackCode,
            String positionCode,
            String levelCode) {
        ResolvedHierarchyParts resolved = new ResolvedHierarchyParts();
        resolved.zone = resolveHierarchySourceValue(mapping.getZone(), areaCode, rackCode, positionCode, levelCode);
        resolved.aisle = resolveHierarchySourceValue(mapping.getAisle(), areaCode, rackCode, positionCode, levelCode);
        resolved.bay = resolveHierarchySourceValue(mapping.getBay(), areaCode, rackCode, positionCode, levelCode);
        resolved.slot = resolveHierarchySourceValue(mapping.getSlot(), areaCode, rackCode, positionCode, levelCode);
        resolved.level = resolveHierarchySourceValue(mapping.getLevel(), areaCode, rackCode, positionCode, levelCode);

        // When Zone is not used, promote Aisle into the leading display/key slot.
        if (resolved.zone == null && resolved.aisle != null) {
            resolved.zone = resolved.aisle;
            resolved.aisle = null;
        }
        if (resolved.bay == null) {
            resolved.invalidReason = "the mapped Rack / Bay value is blank";
            return resolved;
        }
        if (resolved.level == null) {
            resolved.invalidReason = "the mapped Level value is blank";
            return resolved;
        }
        resolved.valid = true;
        return resolved;
    }

    private String resolveHierarchySourceValue(
            String source,
            String areaCode,
            String rackCode,
            String positionCode,
            String levelCode) {
        String normalized = defaultIfBlank(source, null);
        if (normalized == null) {
            return null;
        }
        switch (normalized.trim().toUpperCase()) {
            case "AREA_CODE":
                return areaCode;
            case "RACK_CODE":
                return rackCode;
            case "POSITION_CODE":
                return positionCode;
            case "LEVEL_CODE":
                return levelCode;
            default:
                return null;
        }
    }

    private String hierarchyGroupKey(String zone, String aisle) {
        return defaultIfBlank(zone, "") + "|" + defaultIfBlank(aisle, "");
    }

    private String areaDisplayLabel(String zone, String aisleCode) {
        if (defaultIfBlank(aisleCode, null) == null) {
            return defaultIfBlank(zone, "-");
        }
        return defaultIfBlank(zone, "-") + " / " + aisleCode;
    }

    private String canonicalLocation(String areaCode, String rackCode, String positionCode, String levelCode) {
        return defaultIfBlank(areaCode, "")
                + defaultIfBlank(rackCode, "")
                + defaultIfBlank(positionCode, "")
                + defaultIfBlank(levelCode, "");
    }

    private String canonicalLocation(String zone, String aisleCode, String bayCode, String slotCode, String levelCode) {
        return defaultIfBlank(zone, "")
                + defaultIfBlank(aisleCode, "")
                + defaultIfBlank(bayCode, "")
                + defaultIfBlank(slotCode, "")
                + defaultIfBlank(levelCode, "");
    }

    private BigDecimal medianPositive(List<BigDecimal> values, BigDecimal fallback) {
        List<BigDecimal> positiveValues = values == null ? new ArrayList<>() : values.stream()
                .filter(value -> value != null && value.compareTo(BigDecimal.ZERO) > 0)
                .sorted()
                .collect(Collectors.toList());
        if (positiveValues.isEmpty()) {
            return fallback;
        }

        int middle = positiveValues.size() / 2;
        if (positiveValues.size() % 2 == 1) {
            return roundLayoutNumber(positiveValues.get(middle));
        }
        return roundLayoutNumber(positiveValues.get(middle - 1)
                .add(positiveValues.get(middle))
                .divide(new BigDecimal("2"), 4, RoundingMode.HALF_UP));
    }

    private NormalizedMeasurement normalizeMeasurement(
            BigDecimal rawValue,
            BigDecimal minAccepted,
            BigDecimal maxAccepted) {
        if (rawValue == null || rawValue.compareTo(BigDecimal.ZERO) <= 0) {
            return new NormalizedMeasurement(null, false);
        }

        BigDecimal[] divisors = {
                BigDecimal.ONE,
                new BigDecimal("10"),
                new BigDecimal("100"),
                new BigDecimal("1000")
        };
        for (int index = 0; index < divisors.length; index++) {
            BigDecimal candidate = rawValue.divide(divisors[index], 4, RoundingMode.HALF_UP);
            if (candidate.compareTo(minAccepted) >= 0 && candidate.compareTo(maxAccepted) <= 0) {
                return new NormalizedMeasurement(roundLayoutNumber(candidate), index > 0);
            }
        }

        return new NormalizedMeasurement(null, false);
    }

    private BigDecimal stripSpan(int rackCount, BigDecimal bayDepth, BigDecimal aisleWidth) {
        return bayDepth.multiply(BigDecimal.valueOf(rackCount))
                .add(aisleWidth.multiply(BigDecimal.valueOf(Math.max(rackCount - 1, 0))));
    }

    private boolean shouldRotateArea(
            BigDecimal verticalWidth,
            BigDecimal verticalHeight,
            BigDecimal horizontalWidth,
            BigDecimal horizontalHeight) {
        BigDecimal verticalLongestSide = verticalWidth.max(verticalHeight);
        BigDecimal horizontalLongestSide = horizontalWidth.max(horizontalHeight);
        int longestCompare = verticalLongestSide.compareTo(horizontalLongestSide);
        if (longestCompare != 0) {
            return longestCompare > 0;
        }

        double verticalPenalty = aspectPenalty(verticalWidth, verticalHeight);
        double horizontalPenalty = aspectPenalty(horizontalWidth, horizontalHeight);
        if (Double.compare(verticalPenalty, horizontalPenalty) != 0) {
            return horizontalPenalty < verticalPenalty;
        }
        return false;
    }

    private PackedGrid buildPackedGrid(List<GeneratedAreaLayout> generatedAreas, int columnCount) {
        int safeColumns = Math.max(1, Math.min(columnCount, Math.max(1, generatedAreas.size())));
        int rowCount = (int) Math.ceil(generatedAreas.size() / (double) safeColumns);
        BigDecimal[] columnWidths = new BigDecimal[safeColumns];
        BigDecimal[] rowHeights = new BigDecimal[rowCount];
        for (int index = 0; index < safeColumns; index++) {
            columnWidths[index] = BigDecimal.ZERO;
        }
        for (int index = 0; index < rowCount; index++) {
            rowHeights[index] = BigDecimal.ZERO;
        }

        for (int index = 0; index < generatedAreas.size(); index++) {
            GeneratedAreaLayout generatedArea = generatedAreas.get(index);
            int row = index / safeColumns;
            int column = index % safeColumns;
            columnWidths[column] = columnWidths[column].max(zeroIfNull(generatedArea.aisle.getWidth()));
            rowHeights[row] = rowHeights[row].max(zeroIfNull(generatedArea.aisle.getHeight()));
        }

        BigDecimal totalWidth = OUTER_PADDING.multiply(new BigDecimal("2"));
        for (int index = 0; index < columnWidths.length; index++) {
            totalWidth = totalWidth.add(columnWidths[index]);
            if (index < columnWidths.length - 1) {
                totalWidth = totalWidth.add(AREA_GAP);
            }
        }

        BigDecimal totalHeight = OUTER_PADDING.multiply(new BigDecimal("2"));
        for (int index = 0; index < rowHeights.length; index++) {
            totalHeight = totalHeight.add(rowHeights[index]);
            if (index < rowHeights.length - 1) {
                totalHeight = totalHeight.add(AREA_GAP);
            }
        }

        PackedGrid grid = new PackedGrid();
        grid.columns = safeColumns;
        grid.rowCount = rowCount;
        grid.columnWidths = columnWidths;
        grid.rowHeights = rowHeights;
        grid.totalWidth = roundLayoutNumber(totalWidth);
        grid.totalHeight = roundLayoutNumber(totalHeight);
        grid.footprintArea = roundLayoutNumber(grid.totalWidth.multiply(grid.totalHeight));
        grid.aspectPenalty = aspectPenalty(grid.totalWidth, grid.totalHeight);
        grid.maxSide = grid.totalWidth.max(grid.totalHeight);
        return grid;
    }

    private int comparePackedGrids(PackedGrid left, PackedGrid right) {
        int areaCompare = left.footprintArea.compareTo(right.footprintArea);
        if (areaCompare != 0) {
            return areaCompare;
        }
        int penaltyCompare = Double.compare(left.aspectPenalty, right.aspectPenalty);
        if (penaltyCompare != 0) {
            return penaltyCompare;
        }
        return left.maxSide.compareTo(right.maxSide);
    }

    private double aspectPenalty(BigDecimal width, BigDecimal height) {
        double safeWidth = Math.max(0.0001d, zeroIfNull(width).doubleValue());
        double safeHeight = Math.max(0.0001d, zeroIfNull(height).doubleValue());
        return Math.abs(Math.log(safeWidth / safeHeight));
    }

    private MixedDetectionResult detectMixedLowerShelfLevels(
            String areaCode,
            List<String> rackCodes,
            List<GeneratedLocationSeed> areaRows,
            Map<String, Integer> levelIndexMap) {
        MixedDetectionResult result = MixedDetectionResult.none();
        if (levelIndexMap.size() < 3) {
            return result;
        }

        Map<Integer, List<BigDecimal>> areaLevelHeights = new LinkedHashMap<>();
        Map<String, Map<Integer, List<BigDecimal>>> rackLevelHeights = new LinkedHashMap<>();
        Map<Integer, Integer> areaLevelLocationCounts = new LinkedHashMap<>();
        Map<String, Map<Integer, Integer>> rackLevelLocationCounts = new LinkedHashMap<>();
        Map<String, Map<Integer, Set<String>>> rackLevelPositions = new LinkedHashMap<>();
        for (String rackCode : rackCodes) {
            rackLevelHeights.put(rackCode, new LinkedHashMap<>());
            rackLevelLocationCounts.put(rackCode, new LinkedHashMap<>());
            rackLevelPositions.put(rackCode, new LinkedHashMap<>());
        }

        for (GeneratedLocationSeed seed : areaRows) {
            Integer levelOrdinal = levelIndexMap.get(seed.levelCode);
            if (levelOrdinal == null) {
                continue;
            }
            if (seed.height != null && seed.height.compareTo(BigDecimal.ZERO) > 0) {
                areaLevelHeights.computeIfAbsent(levelOrdinal, key -> new ArrayList<>()).add(seed.height);
                rackLevelHeights
                        .computeIfAbsent(seed.rackCode, key -> new LinkedHashMap<>())
                        .computeIfAbsent(levelOrdinal, key -> new ArrayList<>())
                        .add(seed.height);
            }
            areaLevelLocationCounts.merge(levelOrdinal, 1, Integer::sum);
            rackLevelLocationCounts
                    .computeIfAbsent(seed.rackCode, key -> new LinkedHashMap<>())
                    .merge(levelOrdinal, 1, Integer::sum);
            if (seed.positionCode != null) {
                rackLevelPositions
                        .computeIfAbsent(seed.rackCode, key -> new LinkedHashMap<>())
                        .computeIfAbsent(levelOrdinal, key -> new LinkedHashSet<>())
                        .add(seed.positionCode);
            }
        }

        SplitCandidate bestCandidate = null;
        boolean possibleButUncertain = false;
        int levelCount = levelIndexMap.size();

        for (int split = 1; split < levelCount; split++) {
            boolean areaSupportsHeight = false;
            BigDecimal lowerMedian = BigDecimal.ZERO;
            BigDecimal upperMedian = BigDecimal.ZERO;
            BigDecimal lowerBoundaryMedian = BigDecimal.ZERO;
            BigDecimal upperBoundaryMedian = BigDecimal.ZERO;

            List<BigDecimal> lowerAreaValues = collectHeights(areaLevelHeights, 1, split);
            List<BigDecimal> upperAreaValues = collectHeights(areaLevelHeights, split + 1, levelCount);
            if (!lowerAreaValues.isEmpty() && !upperAreaValues.isEmpty()) {
                lowerMedian = medianPositive(lowerAreaValues, BigDecimal.ZERO);
                upperMedian = medianPositive(upperAreaValues, BigDecimal.ZERO);
                areaSupportsHeight = supportsMixedSplit(lowerMedian, upperMedian);
                if (!areaSupportsHeight && hintsMixedSplit(lowerMedian, upperMedian)) {
                    possibleButUncertain = true;
                }
                lowerBoundaryMedian = medianPositive(
                        areaLevelHeights.getOrDefault(split, Collections.emptyList()),
                        BigDecimal.ZERO);
                upperBoundaryMedian = medianPositive(
                        areaLevelHeights.getOrDefault(split + 1, Collections.emptyList()),
                        BigDecimal.ZERO);
            }

            int usableHeightRacks = 0;
            int supportingHeightRacks = 0;
            int usableLocationRacks = 0;
            int supportingLocationRacks = 0;
            int usableDensityRacks = 0;
            int supportingDensityRacks = 0;
            BigDecimal lowerBoundaryLocationMedian = BigDecimal.ZERO;
            BigDecimal upperBoundaryLocationMedian = BigDecimal.ZERO;
            BigDecimal lowerAverageLocationMedian = BigDecimal.ZERO;
            BigDecimal upperAverageLocationMedian = BigDecimal.ZERO;
            boolean areaSupportsLocationDensity = false;
            Integer lowerAreaBoundaryLocationCount = countLevelDensity(areaLevelLocationCounts, split);
            Integer upperAreaBoundaryLocationCount = countLevelDensity(areaLevelLocationCounts, split + 1);
            BigDecimal lowerAreaAverageLocationCount = averageLevelDensity(areaLevelLocationCounts, 1, split);
            BigDecimal upperAreaAverageLocationCount = averageLevelDensity(areaLevelLocationCounts, split + 1, levelCount);
            if (lowerAreaBoundaryLocationCount != null && upperAreaBoundaryLocationCount != null
                    && lowerAreaAverageLocationCount != null && upperAreaAverageLocationCount != null) {
                lowerBoundaryLocationMedian = BigDecimal.valueOf(lowerAreaBoundaryLocationCount);
                upperBoundaryLocationMedian = BigDecimal.valueOf(upperAreaBoundaryLocationCount);
                lowerAverageLocationMedian = lowerAreaAverageLocationCount;
                upperAverageLocationMedian = upperAreaAverageLocationCount;
                areaSupportsLocationDensity = supportsMixedDensityCounts(
                        lowerBoundaryLocationMedian,
                        upperBoundaryLocationMedian,
                        lowerAverageLocationMedian,
                        upperAverageLocationMedian);
                if (!areaSupportsLocationDensity && hintsMixedDensityCounts(
                        lowerBoundaryLocationMedian,
                        upperBoundaryLocationMedian,
                        lowerAverageLocationMedian,
                        upperAverageLocationMedian)) {
                    possibleButUncertain = true;
                }
            }

            List<BigDecimal> lowerBoundaryLocationCounts = new ArrayList<>();
            List<BigDecimal> upperBoundaryLocationCounts = new ArrayList<>();
            List<BigDecimal> lowerAverageLocationCounts = new ArrayList<>();
            List<BigDecimal> upperAverageLocationCounts = new ArrayList<>();
            List<BigDecimal> lowerBoundaryPositionCounts = new ArrayList<>();
            List<BigDecimal> upperBoundaryPositionCounts = new ArrayList<>();
            List<BigDecimal> lowerAveragePositionCounts = new ArrayList<>();
            List<BigDecimal> upperAveragePositionCounts = new ArrayList<>();
            for (Map<Integer, List<BigDecimal>> rackHeights : rackLevelHeights.values()) {
                List<BigDecimal> lowerRackValues = collectHeights(rackHeights, 1, split);
                List<BigDecimal> upperRackValues = collectHeights(rackHeights, split + 1, levelCount);
                if (lowerRackValues.isEmpty() || upperRackValues.isEmpty()) {
                    continue;
                }
                usableHeightRacks++;
                BigDecimal lowerRackMedian = medianPositive(lowerRackValues, BigDecimal.ZERO);
                BigDecimal upperRackMedian = medianPositive(upperRackValues, BigDecimal.ZERO);
                if (supportsMixedSplit(lowerRackMedian, upperRackMedian)) {
                    supportingHeightRacks++;
                } else if (hintsMixedSplit(lowerRackMedian, upperRackMedian)) {
                    possibleButUncertain = true;
                }
            }

            for (Map<Integer, Integer> rackLocationCounts : rackLevelLocationCounts.values()) {
                Integer lowerBoundaryCount = countLevelDensity(rackLocationCounts, split);
                Integer upperBoundaryCount = countLevelDensity(rackLocationCounts, split + 1);
                BigDecimal lowerAverageCount = averageLevelDensity(rackLocationCounts, 1, split);
                BigDecimal upperAverageCount = averageLevelDensity(rackLocationCounts, split + 1, levelCount);
                if (lowerBoundaryCount == null || upperBoundaryCount == null
                        || lowerAverageCount == null || upperAverageCount == null) {
                    continue;
                }

                usableLocationRacks++;
                BigDecimal lowerBoundary = BigDecimal.valueOf(lowerBoundaryCount);
                BigDecimal upperBoundary = BigDecimal.valueOf(upperBoundaryCount);
                lowerBoundaryLocationCounts.add(lowerBoundary);
                upperBoundaryLocationCounts.add(upperBoundary);
                lowerAverageLocationCounts.add(lowerAverageCount);
                upperAverageLocationCounts.add(upperAverageCount);

                if (supportsMixedDensityCounts(lowerBoundary, upperBoundary, lowerAverageCount, upperAverageCount)) {
                    supportingLocationRacks++;
                } else if (hintsMixedDensityCounts(lowerBoundary, upperBoundary, lowerAverageCount, upperAverageCount)) {
                    possibleButUncertain = true;
                }
            }

            for (Map<Integer, Set<String>> rackPositions : rackLevelPositions.values()) {
                Integer lowerBoundaryCount = distinctPositionCount(rackPositions, split);
                Integer upperBoundaryCount = distinctPositionCount(rackPositions, split + 1);
                BigDecimal lowerAverageCount = averagePositionCount(rackPositions, 1, split);
                BigDecimal upperAverageCount = averagePositionCount(rackPositions, split + 1, levelCount);
                if (lowerBoundaryCount == null || upperBoundaryCount == null
                        || lowerAverageCount == null || upperAverageCount == null) {
                    continue;
                }

                usableDensityRacks++;
                BigDecimal lowerBoundary = BigDecimal.valueOf(lowerBoundaryCount);
                BigDecimal upperBoundary = BigDecimal.valueOf(upperBoundaryCount);
                lowerBoundaryPositionCounts.add(lowerBoundary);
                upperBoundaryPositionCounts.add(upperBoundary);
                lowerAveragePositionCounts.add(lowerAverageCount);
                upperAveragePositionCounts.add(upperAverageCount);

                if (supportsMixedDensityCounts(lowerBoundary, upperBoundary, lowerAverageCount, upperAverageCount)) {
                    supportingDensityRacks++;
                } else if (hintsMixedDensityCounts(lowerBoundary, upperBoundary, lowerAverageCount, upperAverageCount)) {
                    possibleButUncertain = true;
                }
            }

            double heightSupportRatio = usableHeightRacks == 0 ? 0d : supportingHeightRacks / (double) usableHeightRacks;
            double locationSupportRatio = usableLocationRacks == 0 ? 0d : supportingLocationRacks / (double) usableLocationRacks;
            double densitySupportRatio = usableDensityRacks == 0 ? 0d : supportingDensityRacks / (double) usableDensityRacks;
            boolean areaSupportsDensity = false;
            BigDecimal lowerBoundaryPositionMedian = BigDecimal.ZERO;
            BigDecimal upperBoundaryPositionMedian = BigDecimal.ZERO;
            BigDecimal lowerAveragePositionMedian = BigDecimal.ZERO;
            BigDecimal upperAveragePositionMedian = BigDecimal.ZERO;
            if (!lowerBoundaryPositionCounts.isEmpty() && !upperBoundaryPositionCounts.isEmpty()
                    && !lowerAveragePositionCounts.isEmpty() && !upperAveragePositionCounts.isEmpty()) {
                lowerBoundaryPositionMedian = medianPositive(lowerBoundaryPositionCounts, BigDecimal.ZERO);
                upperBoundaryPositionMedian = medianPositive(upperBoundaryPositionCounts, BigDecimal.ZERO);
                lowerAveragePositionMedian = medianPositive(lowerAveragePositionCounts, BigDecimal.ZERO);
                upperAveragePositionMedian = medianPositive(upperAveragePositionCounts, BigDecimal.ZERO);
                areaSupportsDensity = supportsMixedDensityCounts(
                        lowerBoundaryPositionMedian,
                        upperBoundaryPositionMedian,
                        lowerAveragePositionMedian,
                        upperAveragePositionMedian);
                if (!areaSupportsDensity && hintsMixedDensityCounts(
                        lowerBoundaryPositionMedian,
                        upperBoundaryPositionMedian,
                        lowerAveragePositionMedian,
                        upperAveragePositionMedian)) {
                    possibleButUncertain = true;
                }
            }

            boolean splitSupported = (areaSupportsHeight && heightSupportRatio >= MIXED_RACK_SUPPORT_THRESHOLD)
                    || ((areaSupportsLocationDensity || areaSupportsDensity)
                    && Math.max(locationSupportRatio, densitySupportRatio) >= MIXED_RACK_SUPPORT_THRESHOLD);
            if (splitSupported) {
                SplitCandidate candidate = new SplitCandidate();
                candidate.lowerShelfLevels = split;
                candidate.locationBoundaryGap = lowerBoundaryLocationMedian.subtract(upperBoundaryLocationMedian);
                candidate.locationBoundaryRatioGap = upperBoundaryLocationMedian.compareTo(BigDecimal.ZERO) <= 0
                        ? 0d
                        : lowerBoundaryLocationMedian.divide(upperBoundaryLocationMedian, 6, RoundingMode.HALF_UP).doubleValue() - 1d;
                candidate.locationAverageGap = lowerAverageLocationMedian.subtract(upperAverageLocationMedian);
                candidate.boundaryGap = upperBoundaryMedian.subtract(lowerBoundaryMedian);
                candidate.boundaryRatioGap = upperBoundaryMedian.compareTo(BigDecimal.ZERO) <= 0
                        ? 0d
                        : 1d - lowerBoundaryMedian.divide(upperBoundaryMedian, 6, RoundingMode.HALF_UP).doubleValue();
                candidate.positionBoundaryGap = lowerBoundaryPositionMedian.subtract(upperBoundaryPositionMedian);
                candidate.positionBoundaryRatioGap = upperBoundaryPositionMedian.compareTo(BigDecimal.ZERO) <= 0
                        ? 0d
                        : lowerBoundaryPositionMedian.divide(upperBoundaryPositionMedian, 6, RoundingMode.HALF_UP).doubleValue() - 1d;
                candidate.positionAverageGap = lowerAveragePositionMedian.subtract(upperAveragePositionMedian);
                candidate.heightGap = upperMedian.subtract(lowerMedian);
                candidate.ratioGap = upperMedian.compareTo(BigDecimal.ZERO) <= 0
                        ? 0d
                        : 1d - lowerMedian.divide(upperMedian, 6, RoundingMode.HALF_UP).doubleValue();
                candidate.supportRatio = Math.max(heightSupportRatio, Math.max(locationSupportRatio, densitySupportRatio));
                if (bestCandidate == null || compareSplitCandidates(candidate, bestCandidate) > 0) {
                    bestCandidate = candidate;
                }
            } else if (areaSupportsHeight || areaSupportsLocationDensity || areaSupportsDensity
                    || heightSupportRatio > 0d || locationSupportRatio > 0d || densitySupportRatio > 0d) {
                possibleButUncertain = true;
            }
        }

        if (bestCandidate != null) {
            result.lowerShelfLevels = bestCandidate.lowerShelfLevels;
            return result;
        }

        if (possibleButUncertain) {
            result.uncertain = true;
            result.warnings.add("Area " + areaCode + " may contain mixed shelf-to-rack levels, but the signal was not clear enough to auto-apply lower shelf levels.");
        }
        return result;
    }

    private List<BigDecimal> collectHeights(
            Map<Integer, List<BigDecimal>> heightsByLevel,
            int levelFrom,
            int levelTo) {
        List<BigDecimal> values = new ArrayList<>();
        for (int level = levelFrom; level <= levelTo; level++) {
            values.addAll(heightsByLevel.getOrDefault(level, Collections.emptyList()));
        }
        return values;
    }

    private boolean supportsMixedSplit(BigDecimal lowerMedian, BigDecimal upperMedian) {
        if (lowerMedian == null || upperMedian == null || upperMedian.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal heightGap = upperMedian.subtract(lowerMedian);
        if (heightGap.compareTo(MIN_MIXED_HEIGHT_GAP) < 0) {
            return false;
        }
        BigDecimal ratio = lowerMedian.divide(upperMedian, 6, RoundingMode.HALF_UP);
        return ratio.compareTo(MAX_MIXED_LOWER_RATIO) <= 0;
    }

    private boolean hintsMixedSplit(BigDecimal lowerMedian, BigDecimal upperMedian) {
        if (lowerMedian == null || upperMedian == null || upperMedian.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal heightGap = upperMedian.subtract(lowerMedian);
        if (heightGap.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal ratio = lowerMedian.divide(upperMedian, 6, RoundingMode.HALF_UP);
        return heightGap.compareTo(MIXED_HEIGHT_HINT_GAP) >= 0 || ratio.compareTo(MIXED_HEIGHT_HINT_RATIO) <= 0;
    }

    private Integer distinctPositionCount(Map<Integer, Set<String>> positionsByLevel, int level) {
        Set<String> values = positionsByLevel.get(level);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.size();
    }

    private BigDecimal averagePositionCount(
            Map<Integer, Set<String>> positionsByLevel,
            int levelFrom,
            int levelTo) {
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (int level = levelFrom; level <= levelTo; level++) {
            Set<String> positions = positionsByLevel.get(level);
            if (positions == null || positions.isEmpty()) {
                continue;
            }
            total = total.add(BigDecimal.valueOf(positions.size()));
            count++;
        }
        if (count == 0) {
            return null;
        }
        return total.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
    }

    private Integer countLevelDensity(Map<Integer, Integer> countsByLevel, int level) {
        Integer count = countsByLevel.get(level);
        if (count == null || count <= 0) {
            return null;
        }
        return count;
    }

    private BigDecimal averageLevelDensity(
            Map<Integer, Integer> countsByLevel,
            int levelFrom,
            int levelTo) {
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (int level = levelFrom; level <= levelTo; level++) {
            Integer value = countsByLevel.get(level);
            if (value == null || value <= 0) {
                continue;
            }
            total = total.add(BigDecimal.valueOf(value));
            count++;
        }
        if (count == 0) {
            return null;
        }
        return total.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
    }

    private boolean supportsMixedDensityCounts(
            BigDecimal lowerBoundaryCount,
            BigDecimal upperBoundaryCount,
            BigDecimal lowerAverageCount,
            BigDecimal upperAverageCount) {
        if (lowerBoundaryCount == null || upperBoundaryCount == null
                || lowerAverageCount == null || upperAverageCount == null
                || upperBoundaryCount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal boundaryGap = lowerBoundaryCount.subtract(upperBoundaryCount);
        if (boundaryGap.compareTo(MIN_POSITION_BOUNDARY_GAP) < 0) {
            return false;
        }
        BigDecimal boundaryRatio = lowerBoundaryCount.divide(upperBoundaryCount, 6, RoundingMode.HALF_UP);
        if (boundaryRatio.compareTo(MIN_POSITION_BOUNDARY_RATIO) < 0) {
            return false;
        }
        return lowerAverageCount.subtract(upperAverageCount).compareTo(MIN_POSITION_AVERAGE_GAP) >= 0;
    }

    private boolean hintsMixedDensityCounts(
            BigDecimal lowerBoundaryCount,
            BigDecimal upperBoundaryCount,
            BigDecimal lowerAverageCount,
            BigDecimal upperAverageCount) {
        if (lowerBoundaryCount == null || upperBoundaryCount == null
                || lowerAverageCount == null || upperAverageCount == null
                || upperBoundaryCount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal boundaryGap = lowerBoundaryCount.subtract(upperBoundaryCount);
        BigDecimal boundaryRatio = lowerBoundaryCount.divide(upperBoundaryCount, 6, RoundingMode.HALF_UP);
        return boundaryGap.compareTo(BigDecimal.ZERO) > 0
                || boundaryRatio.compareTo(POSITION_HINT_RATIO) >= 0
                || lowerAverageCount.compareTo(upperAverageCount) > 0;
    }

    private int compareSplitCandidates(SplitCandidate left, SplitCandidate right) {
        int locationBoundaryGapCompare = left.locationBoundaryGap.compareTo(right.locationBoundaryGap);
        if (locationBoundaryGapCompare != 0) {
            return locationBoundaryGapCompare;
        }
        int locationBoundaryRatioCompare = Double.compare(left.locationBoundaryRatioGap, right.locationBoundaryRatioGap);
        if (locationBoundaryRatioCompare != 0) {
            return locationBoundaryRatioCompare;
        }
        int locationAverageGapCompare = left.locationAverageGap.compareTo(right.locationAverageGap);
        if (locationAverageGapCompare != 0) {
            return locationAverageGapCompare;
        }
        int positionBoundaryGapCompare = left.positionBoundaryGap.compareTo(right.positionBoundaryGap);
        if (positionBoundaryGapCompare != 0) {
            return positionBoundaryGapCompare;
        }
        int positionBoundaryRatioCompare = Double.compare(left.positionBoundaryRatioGap, right.positionBoundaryRatioGap);
        if (positionBoundaryRatioCompare != 0) {
            return positionBoundaryRatioCompare;
        }
        int positionAverageGapCompare = left.positionAverageGap.compareTo(right.positionAverageGap);
        if (positionAverageGapCompare != 0) {
            return positionAverageGapCompare;
        }
        int boundaryGapCompare = left.boundaryGap.compareTo(right.boundaryGap);
        if (boundaryGapCompare != 0) {
            return boundaryGapCompare;
        }
        int boundaryRatioCompare = Double.compare(left.boundaryRatioGap, right.boundaryRatioGap);
        if (boundaryRatioCompare != 0) {
            return boundaryRatioCompare;
        }
        int gapCompare = left.heightGap.compareTo(right.heightGap);
        if (gapCompare != 0) {
            return gapCompare;
        }
        int ratioCompare = Double.compare(left.ratioGap, right.ratioGap);
        if (ratioCompare != 0) {
            return ratioCompare;
        }
        int lowerShelfCompare = Integer.compare(left.lowerShelfLevels, right.lowerShelfLevels);
        if (lowerShelfCompare != 0) {
            return lowerShelfCompare;
        }
        return Double.compare(left.supportRatio, right.supportRatio);
    }

    private BigDecimal roundLayoutNumber(BigDecimal value) {
        return zeroIfNull(value).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    private String trimmed(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private int naturalCompare(String left, String right) {
        String normalizedLeft = left == null ? "" : left;
        String normalizedRight = right == null ? "" : right;
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < normalizedLeft.length() && rightIndex < normalizedRight.length()) {
            char leftChar = normalizedLeft.charAt(leftIndex);
            char rightChar = normalizedRight.charAt(rightIndex);

            if (Character.isDigit(leftChar) && Character.isDigit(rightChar)) {
                int nextLeft = leftIndex;
                int nextRight = rightIndex;
                while (nextLeft < normalizedLeft.length() && Character.isDigit(normalizedLeft.charAt(nextLeft))) {
                    nextLeft++;
                }
                while (nextRight < normalizedRight.length() && Character.isDigit(normalizedRight.charAt(nextRight))) {
                    nextRight++;
                }

                String leftDigits = normalizedLeft.substring(leftIndex, nextLeft);
                String rightDigits = normalizedRight.substring(rightIndex, nextRight);
                String normalizedLeftDigits = leftDigits.replaceFirst("^0+(?!$)", "");
                String normalizedRightDigits = rightDigits.replaceFirst("^0+(?!$)", "");
                if (normalizedLeftDigits.length() != normalizedRightDigits.length()) {
                    return Integer.compare(normalizedLeftDigits.length(), normalizedRightDigits.length());
                }

                int compare = normalizedLeftDigits.compareTo(normalizedRightDigits);
                if (compare != 0) {
                    return compare;
                }

                if (leftDigits.length() != rightDigits.length()) {
                    return Integer.compare(leftDigits.length(), rightDigits.length());
                }

                leftIndex = nextLeft;
                rightIndex = nextRight;
                continue;
            }

            int compare = Character.compare(Character.toUpperCase(leftChar), Character.toUpperCase(rightChar));
            if (compare != 0) {
                return compare;
            }
            leftIndex++;
            rightIndex++;
        }
        return Integer.compare(normalizedLeft.length(), normalizedRight.length());
    }

    private WarehouseOptimizeModels.StockSyncSnapshot buildSnapshot(
            Long profileId,
            List<WarehouseOptimizeModels.WarehouseLayoutLocation> viewerLocations,
            String customerCode,
            List<WarehouseOptimizeModels.OracleStockRow> stockRows,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping hierarchyMapping,
            LocalDateTime requestedCursor,
            boolean includeEmptyLayoutLocations) {
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> effectiveLocations =
                viewerLocations == null ? Collections.emptyList() : viewerLocations;
        Map<String, WarehouseOptimizeModels.WarehouseLayoutLocation> layoutMap = effectiveLocations.stream()
                .collect(Collectors.toMap(WarehouseOptimizeModels.WarehouseLayoutLocation::getLocation, location -> location, (left, right) -> left, LinkedHashMap::new));
        Map<String, WarehouseOptimizeModels.ProductRecord> productMap = repository.loadProductsBySku(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode);
        Map<String, WarehouseOptimizeModels.SlottingAssignment> assignmentMap = repository.loadAssignments(
                profileId,
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode).stream()
                .collect(Collectors.toMap(WarehouseOptimizeModels.SlottingAssignment::getLocation, assignment -> assignment, (left, right) -> left, LinkedHashMap::new));

        Map<String, WarehouseOptimizeModels.LiveLocationState> stateMap = new LinkedHashMap<>();
        List<String> unmatchedStockLocations = new ArrayList<>();
        LocalDateTime cursor = requestedCursor;

        List<ResolvedOracleStockRow> resolvedStockRows = resolveStockRows(stockRows, hierarchyMapping, unmatchedStockLocations);
        for (ResolvedOracleStockRow row : resolvedStockRows) {
            String locationCode = row.locationCode;
            WarehouseOptimizeModels.WarehouseLayoutLocation layoutLocation = layoutMap.get(locationCode);
            if (layoutLocation == null) {
                unmatchedStockLocations.add(locationCode);
                continue;
            }

            WarehouseOptimizeModels.LiveLocationState state = stateMap.computeIfAbsent(locationCode, key -> createBaseState(layoutLocation, customerCode));
            state.setPhysicalQty(zeroIfNull(state.getPhysicalQty()).add(zeroIfNull(row.physicalQty)));
            state.setAvailableQty(zeroIfNull(state.getAvailableQty()).add(zeroIfNull(row.availableQty)));
            if (state.getProductCode() == null || state.getProductCode().isEmpty()) {
                state.setProductCode(row.productCode);
            }
            LocalDateTime updatedAt = row.updatedAt;
            if (updatedAt != null && (state.getUpdatedAt() == null || updatedAt.isAfter(state.getUpdatedAt()))) {
                state.setUpdatedAt(updatedAt);
            }
            if (updatedAt != null && (cursor == null || updatedAt.isAfter(cursor))) {
                cursor = updatedAt;
            }
        }

        for (WarehouseOptimizeModels.LiveLocationState state : stateMap.values()) {
            WarehouseOptimizeModels.SlottingAssignment assignment = assignmentMap.get(state.getLocation());
            if (assignment != null) {
                state.setVelocityClass(assignment.getVelocityClass());
                state.setProductCategory(assignment.getProductCategory());
            }
            if ((state.getProductCategory() == null || state.getProductCategory().isEmpty()) && state.getProductCode() != null) {
                WarehouseOptimizeModels.ProductRecord product = productMap.get(state.getProductCode());
                if (product != null) {
                    state.setProductCategory(product.getCategory());
                }
            }
            state.setColorClass(resolveColorClass(state.getVelocityClass(), state.getProductCategory()));
        }

        List<String> unmatchedLayoutLocations = new ArrayList<>();
        if (includeEmptyLayoutLocations) {
            for (WarehouseOptimizeModels.WarehouseLayoutLocation layoutLocation : layoutMap.values()) {
                if (!stateMap.containsKey(layoutLocation.getLocation())) {
                    unmatchedLayoutLocations.add(layoutLocation.getLocation());
                    WarehouseOptimizeModels.LiveLocationState emptyState = createBaseState(layoutLocation, customerCode);
                    WarehouseOptimizeModels.SlottingAssignment assignment = assignmentMap.get(layoutLocation.getLocation());
                    if (assignment != null) {
                        emptyState.setVelocityClass(assignment.getVelocityClass());
                        emptyState.setProductCategory(assignment.getProductCategory());
                        emptyState.setColorClass(resolveColorClass(emptyState.getVelocityClass(), emptyState.getProductCategory()));
                    } else {
                        emptyState.setColorClass("empty");
                    }
                    stateMap.put(layoutLocation.getLocation(), emptyState);
                }
            }
        }

        WarehouseOptimizeModels.StockSyncSnapshot snapshot = new WarehouseOptimizeModels.StockSyncSnapshot();
        snapshot.setProfileId(profileId);
        snapshot.setCustomerCode(customerCode);
        snapshot.setSyncedAt(LocalDateTime.now());
        snapshot.setCursor(cursor == null ? LocalDateTime.now() : cursor);
        snapshot.setLocations(new ArrayList<>(stateMap.values()));
        snapshot.getLocations().sort(Comparator.comparing(WarehouseOptimizeModels.LiveLocationState::getLocation));
        snapshot.getDiagnostics().setUnmatchedLayoutLocations(unmatchedLayoutLocations);
        snapshot.getDiagnostics().setUnmatchedStockLocations(unmatchedStockLocations);
        return snapshot;
    }

    private List<ResolvedOracleStockRow> resolveStockRows(
            List<WarehouseOptimizeModels.OracleStockRow> stockRows,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping hierarchyMapping,
            List<String> invalidStockLocations) {
        if (stockRows == null || stockRows.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, ResolvedOracleStockRow> aggregated = new LinkedHashMap<>();
        for (WarehouseOptimizeModels.OracleStockRow stockRow : stockRows) {
            String locationCode;
            if (hierarchyMapping == null) {
                locationCode = canonicalLocation(
                        trimmed(stockRow.getAreaCode()),
                        trimmed(stockRow.getRackCode()),
                        trimmed(stockRow.getPositionCode()),
                        trimmed(stockRow.getLevelCode()));
            } else {
                ResolvedHierarchyParts resolved = resolveMappedStockParts(stockRow, hierarchyMapping);
                if (!resolved.valid) {
                    invalidStockLocations.add(defaultIfBlank(trimmed(stockRow.getLocationCode()), "(blank ST_LOC_COD)"));
                    continue;
                }
                locationCode = canonicalLocation(resolved.zone, resolved.aisle, resolved.bay, resolved.slot, resolved.level);
            }

            String aggregateKey = locationCode + "\u0000" + defaultIfBlank(trimmed(stockRow.getProductCode()), "");
            ResolvedOracleStockRow aggregate = aggregated.computeIfAbsent(aggregateKey, key -> {
                ResolvedOracleStockRow created = new ResolvedOracleStockRow();
                created.locationCode = locationCode;
                created.productCode = trimmed(stockRow.getProductCode());
                created.physicalQty = BigDecimal.ZERO;
                created.availableQty = BigDecimal.ZERO;
                return created;
            });
            aggregate.physicalQty = zeroIfNull(aggregate.physicalQty).add(zeroIfNull(stockRow.getPhysicalQty()));
            aggregate.availableQty = zeroIfNull(aggregate.availableQty).add(zeroIfNull(stockRow.getAvailableQty()));
            if (stockRow.getUpdatedAt() != null
                    && (aggregate.updatedAt == null || stockRow.getUpdatedAt().isAfter(aggregate.updatedAt))) {
                aggregate.updatedAt = stockRow.getUpdatedAt();
            }
        }
        return new ArrayList<>(aggregated.values());
    }

    private List<WarehouseOptimizeModels.WarehouseLayoutLocation> resolveViewerLocations(
            Long profileId,
            Object layoutDataOverride) {
        if (layoutDataOverride == null) {
            return getProfile(profileId).getLocations();
        }
        String layoutJson = repository.toJson(layoutDataOverride);
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> parsedLocations = repository.parseLocations(layoutJson);
        if (!parsedLocations.isEmpty()) {
            return parsedLocations;
        }
        return getProfile(profileId).getLocations();
    }

    private WarehouseOptimizeModels.LiveLocationState createBaseState(
            WarehouseOptimizeModels.WarehouseLayoutLocation layoutLocation,
            String customerCode) {
        WarehouseOptimizeModels.LiveLocationState state = new WarehouseOptimizeModels.LiveLocationState();
        state.setLocation(layoutLocation.getLocation());
        state.setZone(layoutLocation.getZone());
        state.setType(layoutLocation.getType());
        state.setLevel(layoutLocation.getLevel());
        state.setX(layoutLocation.getX());
        state.setY(layoutLocation.getY());
        state.setCustomerCode(customerCode);
        state.setPhysicalQty(BigDecimal.ZERO);
        state.setAvailableQty(BigDecimal.ZERO);
        state.setColorClass("empty");
        return state;
    }

    private String resolveAuthorizedCustomer(String requestedCustomerCode) {
        String desired = defaultIfBlank(requestedCustomerCode, tenantContext.getCustomerCode());
        boolean allowed = getAuthorizedCustomers().stream()
                .anyMatch(option -> tenantContext.getWarehouseCode().equals(option.getWarehouseCode())
                        && desired.equals(option.getCustomerCode()));
        if (!allowed) {
            throw new IllegalArgumentException("Customer filter is not authorized for this warehouse");
        }
        return desired;
    }

    private void ensureProfileAccessible(WarehouseOptimizeModels.WarehouseProfileDetail profile) {
        ensureProfileAccessible((WarehouseOptimizeModels.WarehouseProfileSummary) profile);
    }

    private void ensureProfileAccessible(WarehouseOptimizeModels.WarehouseProfileSummary profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Warehouse optimize profile not found");
        }
        boolean companyMatch = tenantContext.getCompanyCode().equals(profile.getCompanyCode());
        boolean warehouseMatch = tenantContext.getWarehouseCode().equals(profile.getWarehouseCode());
        boolean customerMatch = "*".equals(profile.getCustomerCode()) || tenantContext.getCustomerCode().equals(profile.getCustomerCode());
        if (!companyMatch || !warehouseMatch || !customerMatch) {
            throw new IllegalArgumentException("Warehouse optimize profile is outside the current tenant scope");
        }
    }

    private String resolveColorClass(String velocityClass, String category) {
        if ("A".equalsIgnoreCase(velocityClass)) return "velocity-a";
        if ("B".equalsIgnoreCase(velocityClass)) return "velocity-b";
        if ("C".equalsIgnoreCase(velocityClass)) return "velocity-c";
        if (category == null || category.trim().isEmpty()) return "empty";
        int bucket = Math.abs(category.trim().toUpperCase().hashCode()) % 6;
        return "category-" + bucket;
    }

    private List<WarehouseOptimizeModels.WorkingStatusSnapshotItem> buildWorkingStatusItems(
            String flow,
            Map<String, StatusDefinition> definitions,
            List<WarehouseOptimizeModels.OracleStatusAggregateRow> rows) {
        Map<String, WarehouseOptimizeModels.OracleStatusAggregateRow> rowMap = new LinkedHashMap<>();
        long totalOrders = 0;
        for (WarehouseOptimizeModels.OracleStatusAggregateRow row : rows) {
            if (row == null || defaultIfBlank(row.getStatusCode(), null) == null) {
                continue;
            }
            String statusCode = row.getStatusCode().trim();
            rowMap.put(statusCode, row);
            totalOrders += row.getOrderCount();
        }

        List<WarehouseOptimizeModels.WorkingStatusSnapshotItem> items = new ArrayList<>();
        for (Map.Entry<String, StatusDefinition> entry : definitions.entrySet()) {
            items.add(toWorkingStatusSnapshotItem(
                    flow,
                    entry.getKey(),
                    entry.getValue(),
                    rowMap.get(entry.getKey()),
                    totalOrders));
        }

        for (WarehouseOptimizeModels.OracleStatusAggregateRow row : rowMap.values()) {
            if (definitions.containsKey(row.getStatusCode())) {
                continue;
            }
            StatusDefinition fallbackDefinition = new StatusDefinition("Status " + row.getStatusCode(), "#94A3B8");
            items.add(toWorkingStatusSnapshotItem(flow, row.getStatusCode(), fallbackDefinition, row, totalOrders));
        }
        return items;
    }

    private WarehouseOptimizeModels.WorkingStatusSnapshotItem toWorkingStatusSnapshotItem(
            String flow,
            String statusCode,
            StatusDefinition definition,
            WarehouseOptimizeModels.OracleStatusAggregateRow row,
            long totalOrders) {
        WarehouseOptimizeModels.WorkingStatusSnapshotItem item = new WarehouseOptimizeModels.WorkingStatusSnapshotItem();
        item.setFlow(flow);
        item.setStatusCode(statusCode);
        item.setStatusLabel(definition.label);
        item.setColor(definition.color);
        item.setOrderCount(row == null ? 0 : row.getOrderCount());
        item.setTotalOrders(totalOrders);
        item.setCsQty(normalizeQuantity(row == null ? null : row.getCsQty()));
        item.setPcsQty(normalizeQuantity(row == null ? null : row.getPcsQty()));
        return item;
    }

    private static void registerStatusDefinition(
            Map<String, StatusDefinition> target,
            String statusCode,
            String label,
            String color) {
        target.put(statusCode, new StatusDefinition(label, color));
    }

    private List<WarehouseOptimizeModels.ProductRecord> randomProducts(int count) {
        String[] categories = {
                "Electronics", "Apparel", "Food & Beverage", "Home & Garden",
                "Automotive", "Health & Beauty", "Sports", "Office Supplies"
        };
        Random random = new Random(42L + count);
        List<WarehouseOptimizeModels.ProductRecord> products = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            WarehouseOptimizeModels.ProductRecord product = new WarehouseOptimizeModels.ProductRecord();
            product.setSku(String.format("SKU%06d", index + 1));
            product.setCategory(categories[random.nextInt(categories.length)]);
            product.setName(product.getCategory() + " Item " + product.getSku());
            product.setWeight(new BigDecimal(0.2 + (random.nextDouble() * 9)).setScale(2, RoundingMode.HALF_UP));
            product.setVolume(new BigDecimal(0.01 + (random.nextDouble() * 2)).setScale(4, RoundingMode.HALF_UP));
            product.setWidth(new BigDecimal(0.1 + (random.nextDouble() * 2)).setScale(2, RoundingMode.HALF_UP));
            product.setHeight(new BigDecimal(0.1 + (random.nextDouble() * 2)).setScale(2, RoundingMode.HALF_UP));
            product.setDepth(new BigDecimal(0.1 + (random.nextDouble() * 2)).setScale(2, RoundingMode.HALF_UP));
            BigDecimal demand = new BigDecimal(5 + (random.nextDouble() * 95)).setScale(2, RoundingMode.HALF_UP);
            product.setDemandFrequency(demand);
            if (demand.compareTo(new BigDecimal("70")) >= 0) {
                product.setVelocityClass("A");
            } else if (demand.compareTo(new BigDecimal("35")) >= 0) {
                product.setVelocityClass("B");
            } else {
                product.setVelocityClass("C");
            }
            product.setVariabilityClass(random.nextBoolean() ? "X" : "Z");
            product.setUom(random.nextBoolean() ? "CASE" : "EACH");
            product.setPicksPerDay(demand.divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP));
            product.setAvgQtyPerPick(new BigDecimal(1 + random.nextInt(5)).setScale(2, RoundingMode.HALF_UP));
            products.add(product);
        }
        return products;
    }

    private String stringValue(Map<String, Object> map, String key) {
        return map == null ? null : stringValue(map.get(key));
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private String defaultIfBlank(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private BigDecimal decimalValue(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal normalizeQuantity(BigDecimal value) {
        BigDecimal normalized = zeroIfNull(value);
        if (normalized.scale() <= 0) {
            return normalized.setScale(0, RoundingMode.HALF_UP);
        }
        return normalized.setScale(Math.min(normalized.scale(), 3), RoundingMode.HALF_UP);
    }

    private static class StatusDefinition {
        private final String label;
        private final String color;

        private StatusDefinition(String label, String color) {
            this.label = label;
            this.color = color;
        }
    }

    private static class GeneratedLocationSeed {
        private String locationCode;
        private String groupKey;
        private String areaCode;
        private String aisleCode;
        private String areaName;
        private String rackCode;
        private String positionCode;
        private String slotCode;
        private String levelCode;
        private String sourceRackCode;
        private String sourcePositionCode;
        private String sourceLevelCode;
        private String canonicalLocation;
        private BigDecimal length;
        private BigDecimal width;
        private BigDecimal height;
        private boolean dimensionNormalized;
        private boolean heightNormalized;
    }

    private static class ResolvedHierarchyParts {
        private boolean valid;
        private String invalidReason;
        private String zone;
        private String aisle;
        private String bay;
        private String slot;
        private String level;
        private String groupKey;
    }

    private static class ResolvedOracleStockRow {
        private String locationCode;
        private String productCode;
        private BigDecimal physicalQty;
        private BigDecimal availableQty;
        private LocalDateTime updatedAt;
    }

    private static class GeneratedAreaLayout {
        private WarehouseOptimizeModels.WarehouseAisle aisle;
        private int rackCount;
        private boolean rotated;
        private boolean mixedDetected;
        private boolean mixedUncertain;
        private List<String> warnings = new ArrayList<>();
    }

    private static class NormalizedMeasurement {
        private final BigDecimal value;
        private final boolean scaled;

        private NormalizedMeasurement(BigDecimal value, boolean scaled) {
            this.value = value;
            this.scaled = scaled;
        }
    }

    private static class MixedDetectionResult {
        private int lowerShelfLevels;
        private boolean uncertain;
        private List<String> warnings = new ArrayList<>();

        private static MixedDetectionResult none() {
            return new MixedDetectionResult();
        }
    }

    private static class SplitCandidate {
        private int lowerShelfLevels;
        private BigDecimal locationBoundaryGap;
        private double locationBoundaryRatioGap;
        private BigDecimal locationAverageGap;
        private BigDecimal positionBoundaryGap;
        private double positionBoundaryRatioGap;
        private BigDecimal positionAverageGap;
        private BigDecimal boundaryGap;
        private double boundaryRatioGap;
        private BigDecimal heightGap;
        private double ratioGap;
        private double supportRatio;
    }

    private static class PositionLayoutPlan {
        private int structuralPositionCount;
        private Map<String, PositionPlacement> placements = new HashMap<>();
    }

    private static class PositionPlacement {
        private int positionOrdinal;
        private BigDecimal axisOffset;
        private BigDecimal axisSpan;
    }

    private static class RackPackingPlan {
        private int rackRows;
        private int racksPerRow;
        private BigDecimal width;
        private BigDecimal height;
        private BigDecimal maxSide;
        private BigDecimal footprintArea;
        private double aspectPenalty;

        private int rackRow(int rackIndex) {
            return rackIndex / Math.max(racksPerRow, 1);
        }

        private int rackColumn(int rackIndex) {
            return rackIndex % Math.max(racksPerRow, 1);
        }
    }

    private static class PackedGrid {
        private int columns;
        private int rowCount;
        private BigDecimal[] columnWidths;
        private BigDecimal[] rowHeights;
        private BigDecimal totalWidth;
        private BigDecimal totalHeight;
        private BigDecimal footprintArea;
        private BigDecimal maxSide;
        private double aspectPenalty;
    }
}
