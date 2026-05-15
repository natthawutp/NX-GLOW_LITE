package jp.co.nittsu.gwh.module.warehouseoptimize.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class WarehouseOptimizeRequests {

    private WarehouseOptimizeRequests() {}

    public static class SaveProfileRequest {
        private Long id;
        @NotBlank
        private String profileName;
        private String description;
        private BigDecimal warehouseLength;
        private BigDecimal warehouseWidth;
        @NotNull
        private Object layoutData;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getProfileName() { return profileName; }
        public void setProfileName(String profileName) { this.profileName = profileName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getWarehouseLength() { return warehouseLength; }
        public void setWarehouseLength(BigDecimal warehouseLength) { this.warehouseLength = warehouseLength; }
        public BigDecimal getWarehouseWidth() { return warehouseWidth; }
        public void setWarehouseWidth(BigDecimal warehouseWidth) { this.warehouseWidth = warehouseWidth; }
        public Object getLayoutData() { return layoutData; }
        public void setLayoutData(Object layoutData) { this.layoutData = layoutData; }
    }

    public static class GenerateProductsRequest {
        private Integer count = 100;
        private Map<String, Object> options;

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public Map<String, Object> getOptions() { return options; }
        public void setOptions(Map<String, Object> options) { this.options = options; }
    }

    public static class UploadProductsRequest {
        @NotEmpty
        private List<Map<String, Object>> products = new ArrayList<>();

        public List<Map<String, Object>> getProducts() { return products; }
        public void setProducts(List<Map<String, Object>> products) { this.products = products; }
    }

    public static class SlottingOptimizeRequest {
        @NotNull
        private Long profileId;
        private List<Map<String, Object>> locations = new ArrayList<>();
        private String algorithm = "combined";
        private Map<String, Object> options;

        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public List<Map<String, Object>> getLocations() { return locations; }
        public void setLocations(List<Map<String, Object>> locations) { this.locations = locations; }
        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
        public Map<String, Object> getOptions() { return options; }
        public void setOptions(Map<String, Object> options) { this.options = options; }
    }

    public static class GenerateOrdersRequest {
        @NotNull
        private Long profileId;
        private Integer count = 10;
        private Map<String, Object> options;

        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }
        public Map<String, Object> getOptions() { return options; }
        public void setOptions(Map<String, Object> options) { this.options = options; }
    }

    public static class UploadOrdersRequest {
        @NotNull
        private Long profileId;
        @NotEmpty
        private List<OrderUpload> orders = new ArrayList<>();

        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public List<OrderUpload> getOrders() { return orders; }
        public void setOrders(List<OrderUpload> orders) { this.orders = orders; }
    }

    public static class OrderUpload {
        @NotBlank
        private String orderNumber;
        private Integer priority = 5;
        private List<OrderUploadLine> lines = new ArrayList<>();

        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public List<OrderUploadLine> getLines() { return lines; }
        public void setLines(List<OrderUploadLine> lines) { this.lines = lines; }
    }

    public static class OrderUploadLine {
        private String sku;
        private String location;
        private Integer quantity = 1;

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class RouteOptimizeRequest {
        private WarehouseOptimizeModels.RouteNode startPoint;
        private List<WarehouseOptimizeModels.RouteNode> locations = new ArrayList<>();

        public WarehouseOptimizeModels.RouteNode getStartPoint() { return startPoint; }
        public void setStartPoint(WarehouseOptimizeModels.RouteNode startPoint) { this.startPoint = startPoint; }
        public List<WarehouseOptimizeModels.RouteNode> getLocations() { return locations; }
        public void setLocations(List<WarehouseOptimizeModels.RouteNode> locations) { this.locations = locations; }
    }

    public static class ViewerSyncRequest {
        private String customerCode;

        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    }

    public static class ViewerDeltaRequest {
        private String customerCode;
        private LocalDateTime cursor;

        public String getCustomerCode() { return customerCode; }
        public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
        public LocalDateTime getCursor() { return cursor; }
        public void setCursor(LocalDateTime cursor) { this.cursor = cursor; }
    }

    public static class LegacyProfileRequest {
        private Long id;
        private String op_cpny_cod;
        private String op_whs_cod;
        private String op_cust_cod;
        private String op_pf_name;
        private String name;
        private String description;
        private BigDecimal warehouseLength;
        private BigDecimal warehouseWidth;
        private Object layoutData;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getOp_cpny_cod() { return op_cpny_cod; }
        public void setOp_cpny_cod(String op_cpny_cod) { this.op_cpny_cod = op_cpny_cod; }
        public String getOp_whs_cod() { return op_whs_cod; }
        public void setOp_whs_cod(String op_whs_cod) { this.op_whs_cod = op_whs_cod; }
        public String getOp_cust_cod() { return op_cust_cod; }
        public void setOp_cust_cod(String op_cust_cod) { this.op_cust_cod = op_cust_cod; }
        public String getOp_pf_name() { return op_pf_name; }
        public void setOp_pf_name(String op_pf_name) { this.op_pf_name = op_pf_name; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getWarehouseLength() { return warehouseLength; }
        public void setWarehouseLength(BigDecimal warehouseLength) { this.warehouseLength = warehouseLength; }
        public BigDecimal getWarehouseWidth() { return warehouseWidth; }
        public void setWarehouseWidth(BigDecimal warehouseWidth) { this.warehouseWidth = warehouseWidth; }
        public Object getLayoutData() { return layoutData; }
        public void setLayoutData(Object layoutData) { this.layoutData = layoutData; }
    }
}
