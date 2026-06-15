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
        return getProfile(profileId);
    }

    public WarehouseOptimizeModels.AutoGeneratedLayoutResponse autoGenerateLayout() {
        String companyCode = defaultIfBlank(tenantContext.getCompanyCode(), null);
        String warehouseCode = defaultIfBlank(tenantContext.getWarehouseCode(), null);
        if (companyCode == null || warehouseCode == null) {
            throw new IllegalStateException("Tenant warehouse context is not available for layout generation");
        }

        List<WarehouseOptimizeModels.OracleLocationMasterRow> rows =
                repository.loadOracleLocationMasterRows(companyCode, warehouseCode);
        return synthesizeLayout(rows);
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
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> viewerLocations =
                resolveViewerLocations(profileId, layoutDataOverride);
        List<Map<String, Object>> rows = repository.loadOracleStockRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode,
                null);
        return buildSnapshot(profileId, viewerLocations, customerCode, rows, null, true);
    }

    public WarehouseOptimizeModels.StockDelta loadStockDelta(
            Long profileId,
            String requestedCustomerCode,
            LocalDateTime previousCursor,
            Object layoutDataOverride) {
        getProfileSummary(profileId);
        String customerCode = resolveAuthorizedCustomer(requestedCustomerCode);
        List<WarehouseOptimizeModels.WarehouseLayoutLocation> viewerLocations =
                resolveViewerLocations(profileId, layoutDataOverride);
        List<Map<String, Object>> rows = repository.loadOracleStockRows(
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
            List<WarehouseOptimizeModels.OracleLocationMasterRow> sourceRows) {
        WarehouseOptimizeModels.AutoGeneratedLayoutResponse response = new WarehouseOptimizeModels.AutoGeneratedLayoutResponse();
        WarehouseOptimizeModels.AutoGeneratedLayoutSummary summary = response.getSummary();
        LinkedHashSet<String> warnings = new LinkedHashSet<>();

        if (sourceRows == null || sourceRows.isEmpty()) {
            WarehouseOptimizeModels.WarehouseLayout emptyLayout = new WarehouseOptimizeModels.WarehouseLayout();
            emptyLayout.setWarehouseWidth(roundLayoutNumber(OUTER_PADDING.multiply(new BigDecimal("2"))));
            emptyLayout.setWarehouseHeight(roundLayoutNumber(OUTER_PADDING.multiply(new BigDecimal("2"))));
            emptyLayout.setBoundaryPolygon(null);
            emptyLayout.setAisles(new ArrayList<>());
            response.setLayout(emptyLayout);
            warnings.add("No active GWH_TM_LOC rows were found for the current warehouse.");
            response.setWarnings(new ArrayList<>(warnings));
            return response;
        }

        Map<String, List<GeneratedLocationSeed>> areaMap = new LinkedHashMap<>();
        int inferredPositions = 0;
        int inferredLevels = 0;
        int skippedRows = 0;

        for (WarehouseOptimizeModels.OracleLocationMasterRow row : sourceRows) {
            String areaCode = trimmed(row.getAreaCode());
            if (areaCode == null) {
                skippedRows++;
                warnings.add("Skipped location " + defaultIfBlank(trimmed(row.getLocationCode()), "(blank LOC_COD)") + " because LOC_AREA_COD is blank.");
                continue;
            }

            GeneratedLocationSeed seed = new GeneratedLocationSeed();
            seed.locationCode = trimmed(row.getLocationCode());
            seed.areaCode = areaCode;
            seed.areaName = trimmed(row.getAreaName());
            seed.rackCode = defaultIfBlank(trimmed(row.getRackCode()), SYNTHETIC_RACK_KEY);
            seed.positionCode = trimmed(row.getPositionCode());
            seed.levelCode = defaultIfBlank(trimmed(row.getLevelCode()), "1");
            seed.length = row.getLength();
            seed.width = row.getWidth();
            seed.sourceRackCode = trimmed(row.getRackCode());
            seed.sourcePositionCode = trimmed(row.getPositionCode());
            seed.sourceLevelCode = trimmed(row.getLevelCode());

            if (seed.sourcePositionCode == null) {
                inferredPositions++;
            }
            if (seed.sourceLevelCode == null) {
                inferredLevels++;
            }

            areaMap.computeIfAbsent(areaCode, key -> new ArrayList<>()).add(seed);
        }

        List<String> orderedAreas = new ArrayList<>(areaMap.keySet());
        orderedAreas.sort(this::naturalCompare);

        Set<String> seenCanonicalLocations = new HashSet<>();
        List<GeneratedAreaLayout> generatedAreas = new ArrayList<>();
        int totalRacks = 0;
        int totalLocations = 0;

        for (int areaIndex = 0; areaIndex < orderedAreas.size(); areaIndex++) {
            String areaCode = orderedAreas.get(areaIndex);
            List<GeneratedLocationSeed> areaRows = areaMap.get(areaCode);

            List<GeneratedLocationSeed> missingPositionRows = areaRows.stream()
                    .filter(seed -> seed.positionCode == null)
                    .sorted(Comparator.comparing(
                            seed -> defaultIfBlank(seed.locationCode, ""),
                            this::naturalCompare))
                    .collect(Collectors.toList());
            for (int positionIndex = 0; positionIndex < missingPositionRows.size(); positionIndex++) {
                missingPositionRows.get(positionIndex).positionCode = String.format("AUTO_PSTN_%05d", positionIndex + 1);
            }

            List<GeneratedLocationSeed> deduplicatedRows = new ArrayList<>();
            for (GeneratedLocationSeed seed : areaRows) {
                String canonicalLocation = canonicalLocation(seed.areaCode, seed.rackCode, seed.positionCode, seed.levelCode);
                if (!seenCanonicalLocations.add(canonicalLocation)) {
                    skippedRows++;
                    warnings.add("Skipped duplicate canonical location " + canonicalLocation + " in area " + areaCode + ".");
                    continue;
                }
                seed.canonicalLocation = canonicalLocation;
                deduplicatedRows.add(seed);
            }

            if (deduplicatedRows.isEmpty()) {
                warnings.add("Area " + areaCode + " was ignored because no unique locations remained after normalization.");
                continue;
            }

            GeneratedAreaLayout generatedArea = buildGeneratedArea(areaCode, areaIndex + 1, deduplicatedRows);
            generatedAreas.add(generatedArea);
            totalRacks += generatedArea.rackCount;
            totalLocations += generatedArea.aisle.getLocations().size();
        }

        WarehouseOptimizeModels.WarehouseLayout layout = packGeneratedAreas(generatedAreas);
        response.setLayout(layout);

        summary.setAreas(generatedAreas.size());
        summary.setRacks(totalRacks);
        summary.setLocations(totalLocations);
        summary.setInferredPositions(inferredPositions);
        summary.setInferredLevels(inferredLevels);
        summary.setSkippedRows(skippedRows);

        if (generatedAreas.isEmpty()) {
            warnings.add("No layout areas could be generated from the current warehouse location master.");
        }

        response.setWarnings(new ArrayList<>(warnings));
        return response;
    }

    private GeneratedAreaLayout buildGeneratedArea(
            String areaCode,
            int areaOrdinal,
            List<GeneratedLocationSeed> areaRows) {
        List<String> rackCodes = areaRows.stream()
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
        int rackCount = Math.max(1, rackCodes.size());
        int positionCount = Math.max(1, positionCodes.size());
        boolean hasUsableRackStructure = areaRows.stream()
                .anyMatch(seed -> seed.sourceRackCode != null || seed.sourcePositionCode != null);

        WarehouseOptimizeModels.WarehouseAisle aisle = new WarehouseOptimizeModels.WarehouseAisle();
        aisle.setId(areaOrdinal);
        aisle.setX(BigDecimal.ZERO);
        aisle.setY(BigDecimal.ZERO);
        aisle.setWidth(roundLayoutNumber(bayDepth.multiply(BigDecimal.valueOf(rackCount))
                .add(aisleWidth.multiply(BigDecimal.valueOf(Math.max(rackCount - 1, 0))))));
        aisle.setHeight(roundLayoutNumber(bayWidth.multiply(BigDecimal.valueOf(positionCount))));
        aisle.setType(hasUsableRackStructure ? "HIGH_RACK" : "FLOOR");
        aisle.setDirection("VERTICAL");
        aisle.setLevels(Math.max(1, levelCodes.size()));
        aisle.setZone(areaCode);
        aisle.setBayDepth(roundLayoutNumber(bayDepth));
        aisle.setBayWidth(roundLayoutNumber(bayWidth));
        aisle.setAisleWidth(roundLayoutNumber(aisleWidth));
        aisle.setTunnelLevelFrom(1);
        aisle.setTunnelLevelTo(aisle.getLevels());
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
            int levelCompare = naturalCompare(left.levelCode, right.levelCode);
            if (levelCompare != 0) {
                return levelCompare;
            }
            return naturalCompare(defaultIfBlank(left.locationCode, ""), defaultIfBlank(right.locationCode, ""));
        });

        List<WarehouseOptimizeModels.WarehouseLayoutLocation> locations = new ArrayList<>();
        for (GeneratedLocationSeed seed : orderedRows) {
            int rackIndex = rackIndexMap.get(seed.rackCode);
            int positionOrdinal = positionIndexMap.get(seed.positionCode);
            int levelOrdinal = levelIndexMap.get(seed.levelCode);

            WarehouseOptimizeModels.WarehouseLayoutLocation location = new WarehouseOptimizeModels.WarehouseLayoutLocation();
            location.setLocation(seed.canonicalLocation);
            location.setZone(areaCode);
            location.setType(aisle.getType());
            location.setLevel(levelOrdinal);
            location.setAisle(areaOrdinal);
            location.setPosition(positionOrdinal);
            location.setSide(null);
            location.setX(roundLayoutNumber(BigDecimal.valueOf(rackIndex)
                    .multiply(bayDepth.add(aisleWidth))));
            location.setY(roundLayoutNumber(BigDecimal.valueOf(positionOrdinal - 1).multiply(bayWidth)));
            locations.add(location);
        }
        aisle.setLocations(locations);

        GeneratedAreaLayout generatedArea = new GeneratedAreaLayout();
        generatedArea.aisle = aisle;
        generatedArea.rackCount = rackCount;
        return generatedArea;
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
        int areasPerRow = (int) Math.ceil(Math.sqrt(areaCount));
        int rowCount = (int) Math.ceil(areaCount / (double) areasPerRow);
        int actualColumns = Math.min(areasPerRow, areaCount);

        BigDecimal[] columnWidths = new BigDecimal[actualColumns];
        BigDecimal[] rowHeights = new BigDecimal[rowCount];
        for (int index = 0; index < actualColumns; index++) {
            columnWidths[index] = BigDecimal.ZERO;
        }
        for (int index = 0; index < rowCount; index++) {
            rowHeights[index] = BigDecimal.ZERO;
        }

        for (int index = 0; index < generatedAreas.size(); index++) {
            GeneratedAreaLayout generatedArea = generatedAreas.get(index);
            int row = index / areasPerRow;
            int column = index % areasPerRow;
            if (column >= actualColumns) {
                continue;
            }
            columnWidths[column] = columnWidths[column].max(generatedArea.aisle.getWidth());
            rowHeights[row] = rowHeights[row].max(generatedArea.aisle.getHeight());
        }

        List<WarehouseOptimizeModels.WarehouseAisle> aisles = new ArrayList<>();
        for (int index = 0; index < generatedAreas.size(); index++) {
            GeneratedAreaLayout generatedArea = generatedAreas.get(index);
            WarehouseOptimizeModels.WarehouseAisle aisle = generatedArea.aisle;
            int row = index / areasPerRow;
            int column = index % areasPerRow;

            BigDecimal offsetX = OUTER_PADDING;
            for (int cursor = 0; cursor < column; cursor++) {
                offsetX = offsetX.add(columnWidths[cursor]).add(AREA_GAP);
            }

            BigDecimal offsetY = OUTER_PADDING;
            for (int cursor = 0; cursor < row; cursor++) {
                offsetY = offsetY.add(rowHeights[cursor]).add(AREA_GAP);
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

        BigDecimal warehouseWidth = OUTER_PADDING.multiply(new BigDecimal("2"));
        for (int index = 0; index < actualColumns; index++) {
            warehouseWidth = warehouseWidth.add(columnWidths[index]);
            if (index < actualColumns - 1) {
                warehouseWidth = warehouseWidth.add(AREA_GAP);
            }
        }

        BigDecimal warehouseHeight = OUTER_PADDING.multiply(new BigDecimal("2"));
        for (int index = 0; index < rowCount; index++) {
            warehouseHeight = warehouseHeight.add(rowHeights[index]);
            if (index < rowCount - 1) {
                warehouseHeight = warehouseHeight.add(AREA_GAP);
            }
        }

        layout.setWarehouseWidth(roundLayoutNumber(warehouseWidth));
        layout.setWarehouseHeight(roundLayoutNumber(warehouseHeight));
        layout.setAisles(aisles);
        return layout;
    }

    private String canonicalLocation(String areaCode, String rackCode, String positionCode, String levelCode) {
        return defaultIfBlank(areaCode, "")
                + defaultIfBlank(rackCode, "")
                + defaultIfBlank(positionCode, "")
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
            List<Map<String, Object>> stockRows,
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

        for (Map<String, Object> row : stockRows) {
            String locationCode = stringValue(row.get("location"));
            WarehouseOptimizeModels.WarehouseLayoutLocation layoutLocation = layoutMap.get(locationCode);
            if (layoutLocation == null) {
                unmatchedStockLocations.add(locationCode);
                continue;
            }

            WarehouseOptimizeModels.LiveLocationState state = stateMap.computeIfAbsent(locationCode, key -> createBaseState(layoutLocation, customerCode));
            state.setPhysicalQty(zeroIfNull(state.getPhysicalQty()).add(decimalValue(row.get("physicalQty"))));
            state.setAvailableQty(zeroIfNull(state.getAvailableQty()).add(decimalValue(row.get("availableQty"))));
            if (state.getProductCode() == null || state.getProductCode().isEmpty()) {
                state.setProductCode(stringValue(row.get("productCode")));
            }
            LocalDateTime updatedAt = (LocalDateTime) row.get("updatedAt");
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
        private String areaCode;
        private String areaName;
        private String rackCode;
        private String positionCode;
        private String levelCode;
        private String sourceRackCode;
        private String sourcePositionCode;
        private String sourceLevelCode;
        private String canonicalLocation;
        private BigDecimal length;
        private BigDecimal width;
    }

    private static class GeneratedAreaLayout {
        private WarehouseOptimizeModels.WarehouseAisle aisle;
        private int rackCount;
    }
}
