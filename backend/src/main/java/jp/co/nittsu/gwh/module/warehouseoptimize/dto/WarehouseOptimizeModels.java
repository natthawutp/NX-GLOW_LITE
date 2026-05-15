package jp.co.nittsu.gwh.module.warehouseoptimize.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class WarehouseOptimizeModels {

    private WarehouseOptimizeModels() {}

    public static class CustomerOption {
        private String warehouseCode;
        private String customerCode;
        private String customerName;

        public String getWarehouseCode() { return warehouseCode; }
        public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
    }

    public static class WarehouseProfileSummary {
        private Long id;
        private String companyCode;
        private String warehouseCode;
        private String customerCode;
        private String profileName;
        private String description;
        private BigDecimal warehouseLength;
        private BigDecimal warehouseWidth;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean sharedProfile;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCompanyCode() { return companyCode; }
        public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
        public String getWarehouseCode() { return warehouseCode; }
        public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public String getProfileName() { return profileName; }
        public void setProfileName(String profileName) { this.profileName = profileName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getWarehouseLength() { return warehouseLength; }
        public void setWarehouseLength(BigDecimal warehouseLength) { this.warehouseLength = warehouseLength; }
        public BigDecimal getWarehouseWidth() { return warehouseWidth; }
        public void setWarehouseWidth(BigDecimal warehouseWidth) { this.warehouseWidth = warehouseWidth; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public boolean isSharedProfile() { return sharedProfile; }
        public void setSharedProfile(boolean sharedProfile) { this.sharedProfile = sharedProfile; }
    }

    public static class WarehouseLayoutLocation {
        private String location;
        private String zone;
        private String type;
        private Integer level;
        private Integer aisle;
        private Integer position;
        private String side;
        private BigDecimal x;
        private BigDecimal y;
        private String slottedClass;
        private String slottedSku;

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Integer getLevel() { return level; }
        public void setLevel(Integer level) { this.level = level; }
        public Integer getAisle() { return aisle; }
        public void setAisle(Integer aisle) { this.aisle = aisle; }
        public Integer getPosition() { return position; }
        public void setPosition(Integer position) { this.position = position; }
        public String getSide() { return side; }
        public void setSide(String side) { this.side = side; }
        public BigDecimal getX() { return x; }
        public void setX(BigDecimal x) { this.x = x; }
        public BigDecimal getY() { return y; }
        public void setY(BigDecimal y) { this.y = y; }
        public String getSlottedClass() { return slottedClass; }
        public void setSlottedClass(String slottedClass) { this.slottedClass = slottedClass; }
        public String getSlottedSku() { return slottedSku; }
        public void setSlottedSku(String slottedSku) { this.slottedSku = slottedSku; }
    }

    public static class WarehouseProfileDetail extends WarehouseProfileSummary {
        private String layoutData;
        private List<WarehouseLayoutLocation> locations = new ArrayList<>();

        public String getLayoutData() { return layoutData; }
        public void setLayoutData(String layoutData) { this.layoutData = layoutData; }
        public List<WarehouseLayoutLocation> getLocations() { return locations; }
        public void setLocations(List<WarehouseLayoutLocation> locations) { this.locations = locations; }
    }

    public static class ProductRecord {
        private Long id;
        private String sku;
        private String name;
        private String category;
        private BigDecimal weight;
        private BigDecimal volume;
        private BigDecimal width;
        private BigDecimal height;
        private BigDecimal depth;
        private BigDecimal demandFrequency;
        private String velocityClass;
        private String variabilityClass;
        private String uom;
        private BigDecimal picksPerDay;
        private BigDecimal avgQtyPerPick;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public BigDecimal getWeight() { return weight; }
        public void setWeight(BigDecimal weight) { this.weight = weight; }
        public BigDecimal getVolume() { return volume; }
        public void setVolume(BigDecimal volume) { this.volume = volume; }
        public BigDecimal getWidth() { return width; }
        public void setWidth(BigDecimal width) { this.width = width; }
        public BigDecimal getHeight() { return height; }
        public void setHeight(BigDecimal height) { this.height = height; }
        public BigDecimal getDepth() { return depth; }
        public void setDepth(BigDecimal depth) { this.depth = depth; }
        public BigDecimal getDemandFrequency() { return demandFrequency; }
        public void setDemandFrequency(BigDecimal demandFrequency) { this.demandFrequency = demandFrequency; }
        public String getVelocityClass() { return velocityClass; }
        public void setVelocityClass(String velocityClass) { this.velocityClass = velocityClass; }
        public String getVariabilityClass() { return variabilityClass; }
        public void setVariabilityClass(String variabilityClass) { this.variabilityClass = variabilityClass; }
        public String getUom() { return uom; }
        public void setUom(String uom) { this.uom = uom; }
        public BigDecimal getPicksPerDay() { return picksPerDay; }
        public void setPicksPerDay(BigDecimal picksPerDay) { this.picksPerDay = picksPerDay; }
        public BigDecimal getAvgQtyPerPick() { return avgQtyPerPick; }
        public void setAvgQtyPerPick(BigDecimal avgQtyPerPick) { this.avgQtyPerPick = avgQtyPerPick; }
    }

    public static class SlottingAssignment {
        private Long id;
        private Long profileId;
        private String customerCode;
        private String location;
        private String zone;
        private String productSku;
        private String productName;
        private String productCategory;
        private String velocityClass;
        private BigDecimal demandFrequency;
        private BigDecimal assignmentScore;
        private String assignmentReason;
        private LocalDateTime assignedAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getProductCategory() { return productCategory; }
        public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
        public String getVelocityClass() { return velocityClass; }
        public void setVelocityClass(String velocityClass) { this.velocityClass = velocityClass; }
        public BigDecimal getDemandFrequency() { return demandFrequency; }
        public void setDemandFrequency(BigDecimal demandFrequency) { this.demandFrequency = demandFrequency; }
        public BigDecimal getAssignmentScore() { return assignmentScore; }
        public void setAssignmentScore(BigDecimal assignmentScore) { this.assignmentScore = assignmentScore; }
        public String getAssignmentReason() { return assignmentReason; }
        public void setAssignmentReason(String assignmentReason) { this.assignmentReason = assignmentReason; }
        public LocalDateTime getAssignedAt() { return assignedAt; }
        public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    }

    public static class OrderLine {
        private Long id;
        private String sku;
        private String productName;
        private String location;
        private Integer quantity;
        private Integer pickSequence;
        private Integer originalSequence;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Integer getPickSequence() { return pickSequence; }
        public void setPickSequence(Integer pickSequence) { this.pickSequence = pickSequence; }
        public Integer getOriginalSequence() { return originalSequence; }
        public void setOriginalSequence(Integer originalSequence) { this.originalSequence = originalSequence; }
    }

    public static class PickOrder {
        private Long id;
        private Long profileId;
        private String customerCode;
        private String orderNumber;
        private Integer priority;
        private String status;
        private Integer linesCount;
        private Integer totalQuantity;
        private LocalDateTime createdAt;
        private LocalDateTime optimizedAt;
        private List<OrderLine> lines = new ArrayList<>();

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Integer getLinesCount() { return linesCount; }
        public void setLinesCount(Integer linesCount) { this.linesCount = linesCount; }
        public Integer getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getOptimizedAt() { return optimizedAt; }
        public void setOptimizedAt(LocalDateTime optimizedAt) { this.optimizedAt = optimizedAt; }
        public List<OrderLine> getLines() { return lines; }
        public void setLines(List<OrderLine> lines) { this.lines = lines; }
    }

    public static class RouteNode {
        private String name;
        private String sku;
        private BigDecimal x;
        private BigDecimal y;
        private Integer level;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public BigDecimal getX() { return x; }
        public void setX(BigDecimal x) { this.x = x; }
        public BigDecimal getY() { return y; }
        public void setY(BigDecimal y) { this.y = y; }
        public Integer getLevel() { return level; }
        public void setLevel(Integer level) { this.level = level; }
    }

    public static class RouteResult {
        private String method;
        private List<String> route = new ArrayList<>();
        private BigDecimal distance;
        private BigDecimal percent;

        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public List<String> getRoute() { return route; }
        public void setRoute(List<String> route) { this.route = route; }
        public BigDecimal getDistance() { return distance; }
        public void setDistance(BigDecimal distance) { this.distance = distance; }
        public BigDecimal getPercent() { return percent; }
        public void setPercent(BigDecimal percent) { this.percent = percent; }
    }

    public static class RouteOptimizationResult {
        private Long orderId;
        private RouteNode startPoint;
        private RouteResult original;
        private List<RouteResult> optimized = new ArrayList<>();
        private RouteResult best;
        private List<RouteResult> comparison = new ArrayList<>();

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public RouteNode getStartPoint() { return startPoint; }
        public void setStartPoint(RouteNode startPoint) { this.startPoint = startPoint; }
        public RouteResult getOriginal() { return original; }
        public void setOriginal(RouteResult original) { this.original = original; }
        public List<RouteResult> getOptimized() { return optimized; }
        public void setOptimized(List<RouteResult> optimized) { this.optimized = optimized; }
        public RouteResult getBest() { return best; }
        public void setBest(RouteResult best) { this.best = best; }
        public List<RouteResult> getComparison() { return comparison; }
        public void setComparison(List<RouteResult> comparison) { this.comparison = comparison; }
    }

    public static class SlottingAnalytics {
        private List<VelocityDistribution> velocityDistribution = new ArrayList<>();
        private List<ZoneDistribution> zoneDistribution = new ArrayList<>();

        public List<VelocityDistribution> getVelocityDistribution() { return velocityDistribution; }
        public void setVelocityDistribution(List<VelocityDistribution> velocityDistribution) { this.velocityDistribution = velocityDistribution; }
        public List<ZoneDistribution> getZoneDistribution() { return zoneDistribution; }
        public void setZoneDistribution(List<ZoneDistribution> zoneDistribution) { this.zoneDistribution = zoneDistribution; }
    }

    public static class PickingAnalytics {
        private List<AlgorithmComparison> algorithmComparison = new ArrayList<>();
        private List<BestAlgorithmUse> bestAlgorithmUsage = new ArrayList<>();
        private long totalOrders;
        private long optimizedOrders;

        public List<AlgorithmComparison> getAlgorithmComparison() { return algorithmComparison; }
        public void setAlgorithmComparison(List<AlgorithmComparison> algorithmComparison) { this.algorithmComparison = algorithmComparison; }
        public List<BestAlgorithmUse> getBestAlgorithmUsage() { return bestAlgorithmUsage; }
        public void setBestAlgorithmUsage(List<BestAlgorithmUse> bestAlgorithmUsage) { this.bestAlgorithmUsage = bestAlgorithmUsage; }
        public long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
        public long getOptimizedOrders() { return optimizedOrders; }
        public void setOptimizedOrders(long optimizedOrders) { this.optimizedOrders = optimizedOrders; }
    }

    public static class WarehouseOptimizeAnalytics {
        private SlottingAnalytics slotting = new SlottingAnalytics();
        private PickingAnalytics picking = new PickingAnalytics();

        public SlottingAnalytics getSlotting() { return slotting; }
        public void setSlotting(SlottingAnalytics slotting) { this.slotting = slotting; }
        public PickingAnalytics getPicking() { return picking; }
        public void setPicking(PickingAnalytics picking) { this.picking = picking; }
    }

    public static class VelocityDistribution {
        private String velocityClass;
        private long count;
        private BigDecimal avgScore;

        public String getVelocityClass() { return velocityClass; }
        public void setVelocityClass(String velocityClass) { this.velocityClass = velocityClass; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
        public BigDecimal getAvgScore() { return avgScore; }
        public void setAvgScore(BigDecimal avgScore) { this.avgScore = avgScore; }
    }

    public static class ZoneDistribution {
        private String zone;
        private long count;

        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }

    public static class AlgorithmComparison {
        private String algorithm;
        private long orderCount;
        private BigDecimal avgOriginalDistance;
        private BigDecimal avgOptimizedDistance;
        private BigDecimal avgImprovement;

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
        public long getOrderCount() { return orderCount; }
        public void setOrderCount(long orderCount) { this.orderCount = orderCount; }
        public BigDecimal getAvgOriginalDistance() { return avgOriginalDistance; }
        public void setAvgOriginalDistance(BigDecimal avgOriginalDistance) { this.avgOriginalDistance = avgOriginalDistance; }
        public BigDecimal getAvgOptimizedDistance() { return avgOptimizedDistance; }
        public void setAvgOptimizedDistance(BigDecimal avgOptimizedDistance) { this.avgOptimizedDistance = avgOptimizedDistance; }
        public BigDecimal getAvgImprovement() { return avgImprovement; }
        public void setAvgImprovement(BigDecimal avgImprovement) { this.avgImprovement = avgImprovement; }
    }

    public static class BestAlgorithmUse {
        private String algorithm;
        private long timesBest;

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
        public long getTimesBest() { return timesBest; }
        public void setTimesBest(long timesBest) { this.timesBest = timesBest; }
    }

    public static class ViewerDiagnostics {
        private List<String> unmatchedLayoutLocations = new ArrayList<>();
        private List<String> unmatchedStockLocations = new ArrayList<>();

        public List<String> getUnmatchedLayoutLocations() { return unmatchedLayoutLocations; }
        public void setUnmatchedLayoutLocations(List<String> unmatchedLayoutLocations) { this.unmatchedLayoutLocations = unmatchedLayoutLocations; }
        public List<String> getUnmatchedStockLocations() { return unmatchedStockLocations; }
        public void setUnmatchedStockLocations(List<String> unmatchedStockLocations) { this.unmatchedStockLocations = unmatchedStockLocations; }
    }

    public static class LiveLocationState {
        private String location;
        private String zone;
        private String type;
        private Integer level;
        private BigDecimal x;
        private BigDecimal y;
        private String customerCode;
        private String productCode;
        private String productCategory;
        private String velocityClass;
        private String colorClass;
        private BigDecimal physicalQty;
        private BigDecimal availableQty;
        private boolean blink;
        private LocalDateTime updatedAt;

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getZone() { return zone; }
        public void setZone(String zone) { this.zone = zone; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Integer getLevel() { return level; }
        public void setLevel(Integer level) { this.level = level; }
        public BigDecimal getX() { return x; }
        public void setX(BigDecimal x) { this.x = x; }
        public BigDecimal getY() { return y; }
        public void setY(BigDecimal y) { this.y = y; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        public String getProductCategory() { return productCategory; }
        public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
        public String getVelocityClass() { return velocityClass; }
        public void setVelocityClass(String velocityClass) { this.velocityClass = velocityClass; }
        public String getColorClass() { return colorClass; }
        public void setColorClass(String colorClass) { this.colorClass = colorClass; }
        public BigDecimal getPhysicalQty() { return physicalQty; }
        public void setPhysicalQty(BigDecimal physicalQty) { this.physicalQty = physicalQty; }
        public BigDecimal getAvailableQty() { return availableQty; }
        public void setAvailableQty(BigDecimal availableQty) { this.availableQty = availableQty; }
        public boolean isBlink() { return blink; }
        public void setBlink(boolean blink) { this.blink = blink; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }

    public static class StockSyncSnapshot {
        private Long profileId;
        private String customerCode;
        private LocalDateTime syncedAt;
        private LocalDateTime cursor;
        private List<LiveLocationState> locations = new ArrayList<>();
        private ViewerDiagnostics diagnostics = new ViewerDiagnostics();

        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public LocalDateTime getSyncedAt() { return syncedAt; }
        public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }
        public LocalDateTime getCursor() { return cursor; }
        public void setCursor(LocalDateTime cursor) { this.cursor = cursor; }
        public List<LiveLocationState> getLocations() { return locations; }
        public void setLocations(List<LiveLocationState> locations) { this.locations = locations; }
        public ViewerDiagnostics getDiagnostics() { return diagnostics; }
        public void setDiagnostics(ViewerDiagnostics diagnostics) { this.diagnostics = diagnostics; }
    }

    public static class StockDelta {
        private Long profileId;
        private String customerCode;
        private LocalDateTime previousCursor;
        private LocalDateTime cursor;
        private List<LiveLocationState> changedLocations = new ArrayList<>();
        private ViewerDiagnostics diagnostics = new ViewerDiagnostics();

        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public LocalDateTime getPreviousCursor() { return previousCursor; }
        public void setPreviousCursor(LocalDateTime previousCursor) { this.previousCursor = previousCursor; }
        public LocalDateTime getCursor() { return cursor; }
        public void setCursor(LocalDateTime cursor) { this.cursor = cursor; }
        public List<LiveLocationState> getChangedLocations() { return changedLocations; }
        public void setChangedLocations(List<LiveLocationState> changedLocations) { this.changedLocations = changedLocations; }
        public ViewerDiagnostics getDiagnostics() { return diagnostics; }
        public void setDiagnostics(ViewerDiagnostics diagnostics) { this.diagnostics = diagnostics; }
    }
}
