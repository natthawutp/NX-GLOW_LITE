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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class WarehouseOptimizeService {

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

    public WarehouseOptimizeModels.WarehouseProfileDetail saveProfile(WarehouseOptimizeRequests.SaveProfileRequest request) {
        String layoutJson = repository.toJson(request.getLayoutData());
        Long profileId = repository.saveProfile(tenantContext.getCompanyCode(), tenantContext.getWarehouseCode(), request, layoutJson);
        return getProfile(profileId);
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

    public WarehouseOptimizeModels.StockSyncSnapshot syncStock(Long profileId, String requestedCustomerCode) {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = getProfile(profileId);
        String customerCode = resolveAuthorizedCustomer(requestedCustomerCode);
        List<Map<String, Object>> rows = repository.loadOracleStockRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode,
                null);
        return buildSnapshot(profile, customerCode, rows, null, true);
    }

    public WarehouseOptimizeModels.StockDelta loadStockDelta(Long profileId, String requestedCustomerCode, LocalDateTime previousCursor) {
        WarehouseOptimizeModels.WarehouseProfileDetail profile = getProfile(profileId);
        String customerCode = resolveAuthorizedCustomer(requestedCustomerCode);
        List<Map<String, Object>> rows = repository.loadOracleStockRows(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode,
                previousCursor);
        WarehouseOptimizeModels.StockSyncSnapshot snapshot = buildSnapshot(profile, customerCode, rows, previousCursor, false);
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

    private WarehouseOptimizeModels.StockSyncSnapshot buildSnapshot(
            WarehouseOptimizeModels.WarehouseProfileDetail profile,
            String customerCode,
            List<Map<String, Object>> stockRows,
            LocalDateTime requestedCursor,
            boolean includeEmptyLayoutLocations) {
        Map<String, WarehouseOptimizeModels.WarehouseLayoutLocation> layoutMap = profile.getLocations().stream()
                .collect(Collectors.toMap(WarehouseOptimizeModels.WarehouseLayoutLocation::getLocation, location -> location, (left, right) -> left, LinkedHashMap::new));
        Map<String, WarehouseOptimizeModels.ProductRecord> productMap = repository.loadProductsBySku(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                customerCode);
        Map<String, WarehouseOptimizeModels.SlottingAssignment> assignmentMap = repository.loadAssignments(
                profile.getId(),
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
        snapshot.setProfileId(profile.getId());
        snapshot.setCustomerCode(customerCode);
        snapshot.setSyncedAt(LocalDateTime.now());
        snapshot.setCursor(cursor == null ? LocalDateTime.now() : cursor);
        snapshot.setLocations(new ArrayList<>(stateMap.values()));
        snapshot.getLocations().sort(Comparator.comparing(WarehouseOptimizeModels.LiveLocationState::getLocation));
        snapshot.getDiagnostics().setUnmatchedLayoutLocations(unmatchedLayoutLocations);
        snapshot.getDiagnostics().setUnmatchedStockLocations(unmatchedStockLocations);
        return snapshot;
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
}
