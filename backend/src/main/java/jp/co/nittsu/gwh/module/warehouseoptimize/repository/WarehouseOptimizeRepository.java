package jp.co.nittsu.gwh.module.warehouseoptimize.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeModels;
import jp.co.nittsu.gwh.module.warehouseoptimize.dto.WarehouseOptimizeRequests;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WarehouseOptimizeRepository {

    private static final String[] ORACLE_STOCK_SOURCE_CANDIDATES = {
            "SGWH0001.GWH_TJ_ST",
            "GWH_TJ_ST",
            "SGWH0001.VGWH_TJ_ST",
            "VGWH_TJ_ST"
    };

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public WarehouseOptimizeRepository(
            @Qualifier("warehouseOptimizeJdbcTemplate") JdbcTemplate jdbcTemplate,
            @Qualifier("warehouseOptimizeNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedJdbcTemplate,
            ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initializeSchema() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_products (" +
                "id SERIAL PRIMARY KEY," +
                "sku VARCHAR(50) UNIQUE NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "category VARCHAR(100)," +
                "weight DECIMAL(10,2) DEFAULT 0," +
                "volume DECIMAL(10,4) DEFAULT 0," +
                "width DECIMAL(10,2) DEFAULT 0," +
                "height DECIMAL(10,2) DEFAULT 0," +
                "depth DECIMAL(10,2) DEFAULT 0," +
                "demand_frequency DECIMAL(10,2) DEFAULT 0," +
                "velocity_class VARCHAR(1) DEFAULT 'C'," +
                "variability_class VARCHAR(1) DEFAULT 'Z'," +
                "uom VARCHAR(20) DEFAULT 'EACH'," +
                "picks_per_day DECIMAL(10,2) DEFAULT 0," +
                "avg_qty_per_pick DECIMAL(10,2) DEFAULT 1," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        jdbcTemplate.execute("ALTER TABLE gwh_products ADD COLUMN IF NOT EXISTS op_cpny_cod VARCHAR(50)");
        jdbcTemplate.execute("ALTER TABLE gwh_products ADD COLUMN IF NOT EXISTS op_whs_cod VARCHAR(50)");
        jdbcTemplate.execute("ALTER TABLE gwh_products ADD COLUMN IF NOT EXISTS op_cust_cod VARCHAR(50)");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_warehouse_profiles (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "description TEXT," +
                "warehouse_length DECIMAL(10,2)," +
                "warehouse_width DECIMAL(10,2)," +
                "layout_data JSONB," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        jdbcTemplate.execute("ALTER TABLE gwh_warehouse_profiles ADD COLUMN IF NOT EXISTS op_cpny_cod VARCHAR(50)");
        jdbcTemplate.execute("ALTER TABLE gwh_warehouse_profiles ADD COLUMN IF NOT EXISTS op_whs_cod VARCHAR(50)");
        jdbcTemplate.execute("ALTER TABLE gwh_warehouse_profiles ADD COLUMN IF NOT EXISTS op_cust_cod VARCHAR(50)");
        jdbcTemplate.execute("ALTER TABLE gwh_warehouse_profiles ADD COLUMN IF NOT EXISTS op_pf_name VARCHAR(100)");
        jdbcTemplate.execute("DO $$ BEGIN " +
                "IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_gwh_warehouse_profiles_hierarchy') THEN " +
                "ALTER TABLE gwh_warehouse_profiles " +
                "ADD CONSTRAINT uq_gwh_warehouse_profiles_hierarchy UNIQUE (op_cpny_cod, op_whs_cod, op_cust_cod, op_pf_name);" +
                "END IF; END $$;");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_slot_assignment_layers (" +
                "id SERIAL PRIMARY KEY," +
                "profile_id INTEGER REFERENCES gwh_warehouse_profiles(id) ON DELETE CASCADE," +
                "op_cpny_cod VARCHAR(50) NOT NULL," +
                "op_whs_cod VARCHAR(50) NOT NULL," +
                "op_cust_cod VARCHAR(50) NOT NULL," +
                "location VARCHAR(100) NOT NULL," +
                "zone VARCHAR(50)," +
                "product_sku VARCHAR(50) NOT NULL," +
                "product_name VARCHAR(255)," +
                "product_category VARCHAR(100)," +
                "velocity_class VARCHAR(1)," +
                "demand_frequency DECIMAL(10,2) DEFAULT 0," +
                "assignment_score DECIMAL(10,4)," +
                "assignment_reason TEXT," +
                "assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod, location)" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_pick_order_layers (" +
                "id SERIAL PRIMARY KEY," +
                "profile_id INTEGER REFERENCES gwh_warehouse_profiles(id) ON DELETE CASCADE," +
                "op_cpny_cod VARCHAR(50) NOT NULL," +
                "op_whs_cod VARCHAR(50) NOT NULL," +
                "op_cust_cod VARCHAR(50) NOT NULL," +
                "order_number VARCHAR(50) NOT NULL," +
                "priority INTEGER DEFAULT 5," +
                "status VARCHAR(20) DEFAULT 'PENDING'," +
                "lines_count INTEGER DEFAULT 0," +
                "total_quantity INTEGER DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "optimized_at TIMESTAMP," +
                "UNIQUE(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod, order_number)" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_order_line_layers (" +
                "id SERIAL PRIMARY KEY," +
                "order_id INTEGER REFERENCES gwh_pick_order_layers(id) ON DELETE CASCADE," +
                "sku VARCHAR(50) NOT NULL," +
                "product_name VARCHAR(255)," +
                "location VARCHAR(100)," +
                "quantity INTEGER DEFAULT 1," +
                "pick_sequence INTEGER," +
                "original_sequence INTEGER," +
                "picked BOOLEAN DEFAULT FALSE" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_optimization_result_layers (" +
                "id SERIAL PRIMARY KEY," +
                "order_id INTEGER REFERENCES gwh_pick_order_layers(id) ON DELETE CASCADE," +
                "algorithm VARCHAR(50) NOT NULL," +
                "original_distance DECIMAL(10,2)," +
                "optimized_distance DECIMAL(10,2)," +
                "distance_saved DECIMAL(10,2)," +
                "percent_improvement DECIMAL(5,2)," +
                "route_data JSONB," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");

        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_products_tenant ON gwh_products(op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_profiles_scope ON gwh_warehouse_profiles(op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_assignments_scope ON gwh_slot_assignment_layers(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_orders_scope ON gwh_pick_order_layers(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_order_lines_order ON gwh_order_line_layers(order_id)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_results_order ON gwh_optimization_result_layers(order_id)");
    }

    public List<WarehouseOptimizeModels.WarehouseProfileSummary> findProfiles(
            String companyCode, String warehouseCode, String currentCustomerCode,
            String profileName, String queryText) {
        String sql = "SELECT id, op_cpny_cod, op_whs_cod, op_cust_cod, op_pf_name, description, warehouse_length, warehouse_width, created_at, updated_at " +
                "FROM gwh_warehouse_profiles " +
                "WHERE op_cpny_cod = :cpny AND op_whs_cod = :whs " +
                "AND (op_cust_cod = '*' OR op_cust_cod = :cust) " +
                "AND (:profileName = '' OR op_pf_name ILIKE :profileLike) " +
                "AND (:queryText = '' OR op_pf_name ILIKE :queryLike OR COALESCE(description, '') ILIKE :queryLike) " +
                "ORDER BY updated_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cpny", companyCode)
                .addValue("whs", warehouseCode)
                .addValue("cust", currentCustomerCode)
                .addValue("profileName", profileName == null ? "" : profileName.trim())
                .addValue("profileLike", "%" + (profileName == null ? "" : profileName.trim()) + "%")
                .addValue("queryText", queryText == null ? "" : queryText.trim())
                .addValue("queryLike", "%" + (queryText == null ? "" : queryText.trim()) + "%");
        return namedJdbcTemplate.query(sql, params, profileSummaryRowMapper());
    }

    public WarehouseOptimizeModels.WarehouseProfileDetail findProfileDetail(Long id) {
        String sql = "SELECT id, op_cpny_cod, op_whs_cod, op_cust_cod, op_pf_name, description, warehouse_length, warehouse_width, layout_data::text, created_at, updated_at " +
                "FROM gwh_warehouse_profiles WHERE id = :id";
        List<WarehouseOptimizeModels.WarehouseProfileDetail> results = namedJdbcTemplate.query(
                sql, new MapSqlParameterSource("id", id), (rs, rowNum) -> mapProfileDetail(rs));
        return results.isEmpty() ? null : results.get(0);
    }

    public Long saveProfile(String companyCode, String warehouseCode, WarehouseOptimizeRequests.SaveProfileRequest request, String layoutJson) {
        String sharedCustomerCode = "*";
        if (request.getId() != null) {
            namedJdbcTemplate.update(
                    "UPDATE gwh_warehouse_profiles SET op_cpny_cod = :cpny, op_whs_cod = :whs, op_cust_cod = :cust, op_pf_name = :pf, name = :pf, " +
                            "description = :description, warehouse_length = :length, warehouse_width = :width, layout_data = CAST(:layoutData AS JSONB), updated_at = NOW() " +
                            "WHERE id = :id",
                    new MapSqlParameterSource()
                            .addValue("id", request.getId())
                            .addValue("cpny", companyCode)
                            .addValue("whs", warehouseCode)
                            .addValue("cust", sharedCustomerCode)
                            .addValue("pf", request.getProfileName())
                            .addValue("description", request.getDescription())
                            .addValue("length", request.getWarehouseLength())
                            .addValue("width", request.getWarehouseWidth())
                            .addValue("layoutData", layoutJson));
            return request.getId();
        }

        Long existingId = namedJdbcTemplate.query(
                "SELECT id FROM gwh_warehouse_profiles WHERE op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust AND op_pf_name = :pf LIMIT 1",
                new MapSqlParameterSource()
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", sharedCustomerCode)
                        .addValue("pf", request.getProfileName()),
                rs -> rs.next() ? rs.getLong(1) : null);

        if (existingId != null) {
            request.setId(existingId);
            return saveProfile(companyCode, warehouseCode, request, layoutJson);
        }

        return namedJdbcTemplate.queryForObject(
                "INSERT INTO gwh_warehouse_profiles " +
                        "(op_cpny_cod, op_whs_cod, op_cust_cod, op_pf_name, name, description, warehouse_length, warehouse_width, layout_data) " +
                        "VALUES (:cpny, :whs, :cust, :pf, :pf, :description, :length, :width, CAST(:layoutData AS JSONB)) RETURNING id",
                new MapSqlParameterSource()
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", sharedCustomerCode)
                        .addValue("pf", request.getProfileName())
                        .addValue("description", request.getDescription())
                        .addValue("length", request.getWarehouseLength())
                        .addValue("width", request.getWarehouseWidth())
                        .addValue("layoutData", layoutJson),
                Long.class);
    }

    public List<WarehouseOptimizeModels.ProductRecord> loadProducts(String companyCode, String warehouseCode, String customerCode) {
        String sql = "SELECT id, sku, name, category, weight, volume, width, height, depth, demand_frequency, velocity_class, variability_class, uom, picks_per_day, avg_qty_per_pick " +
                "FROM gwh_products " +
                "WHERE op_cpny_cod = :cpny " +
                "AND op_whs_cod = :whs " +
                "AND op_cust_cod = :cust " +
                "ORDER BY velocity_class, demand_frequency DESC, sku";
        return namedJdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                productRowMapper());
    }

    public void upsertProducts(String companyCode, String warehouseCode, String customerCode, List<WarehouseOptimizeModels.ProductRecord> products) {
        String sql = "INSERT INTO gwh_products " +
                "(sku, name, category, weight, volume, width, height, depth, demand_frequency, velocity_class, variability_class, uom, picks_per_day, avg_qty_per_pick, op_cpny_cod, op_whs_cod, op_cust_cod, updated_at) " +
                "VALUES (:sku, :name, :category, :weight, :volume, :width, :height, :depth, :demandFrequency, :velocityClass, :variabilityClass, :uom, :picksPerDay, :avgQtyPerPick, :cpny, :whs, :cust, NOW()) " +
                "ON CONFLICT (sku) DO UPDATE SET " +
                "name = EXCLUDED.name, category = EXCLUDED.category, weight = EXCLUDED.weight, volume = EXCLUDED.volume, width = EXCLUDED.width, height = EXCLUDED.height, depth = EXCLUDED.depth, " +
                "demand_frequency = EXCLUDED.demand_frequency, velocity_class = EXCLUDED.velocity_class, variability_class = EXCLUDED.variability_class, uom = EXCLUDED.uom, " +
                "picks_per_day = EXCLUDED.picks_per_day, avg_qty_per_pick = EXCLUDED.avg_qty_per_pick, op_cpny_cod = EXCLUDED.op_cpny_cod, op_whs_cod = EXCLUDED.op_whs_cod, op_cust_cod = EXCLUDED.op_cust_cod, updated_at = NOW()";
        for (WarehouseOptimizeModels.ProductRecord product : products) {
            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("sku", product.getSku())
                    .addValue("name", product.getName())
                    .addValue("category", product.getCategory())
                    .addValue("weight", product.getWeight())
                    .addValue("volume", product.getVolume())
                    .addValue("width", product.getWidth())
                    .addValue("height", product.getHeight())
                    .addValue("depth", product.getDepth())
                    .addValue("demandFrequency", product.getDemandFrequency())
                    .addValue("velocityClass", product.getVelocityClass())
                    .addValue("variabilityClass", product.getVariabilityClass())
                    .addValue("uom", product.getUom())
                    .addValue("picksPerDay", product.getPicksPerDay())
                    .addValue("avgQtyPerPick", product.getAvgQtyPerPick())
                    .addValue("cpny", companyCode)
                    .addValue("whs", warehouseCode)
                    .addValue("cust", customerCode));
        }
    }

    public void replaceAssignments(
            Long profileId, String companyCode, String warehouseCode, String customerCode,
            List<WarehouseOptimizeModels.SlottingAssignment> assignments) {
        namedJdbcTemplate.update(
                "DELETE FROM gwh_slot_assignment_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode));

        String sql = "INSERT INTO gwh_slot_assignment_layers " +
                "(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod, location, zone, product_sku, product_name, product_category, velocity_class, demand_frequency, assignment_score, assignment_reason) " +
                "VALUES (:profileId, :cpny, :whs, :cust, :location, :zone, :sku, :name, :category, :velocityClass, :demandFrequency, :score, :reason)";
        for (WarehouseOptimizeModels.SlottingAssignment assignment : assignments) {
            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("profileId", profileId)
                    .addValue("cpny", companyCode)
                    .addValue("whs", warehouseCode)
                    .addValue("cust", customerCode)
                    .addValue("location", assignment.getLocation())
                    .addValue("zone", assignment.getZone())
                    .addValue("sku", assignment.getProductSku())
                    .addValue("name", assignment.getProductName())
                    .addValue("category", assignment.getProductCategory())
                    .addValue("velocityClass", assignment.getVelocityClass())
                    .addValue("demandFrequency", assignment.getDemandFrequency())
                    .addValue("score", assignment.getAssignmentScore())
                    .addValue("reason", assignment.getAssignmentReason()));
        }
    }

    public List<WarehouseOptimizeModels.SlottingAssignment> loadAssignments(
            Long profileId, String companyCode, String warehouseCode, String customerCode) {
        String sql = "SELECT id, profile_id, op_cust_cod, location, zone, product_sku, product_name, product_category, velocity_class, demand_frequency, assignment_score, assignment_reason, assigned_at " +
                "FROM gwh_slot_assignment_layers " +
                "WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust " +
                "ORDER BY velocity_class, demand_frequency DESC, location";
        return namedJdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                assignmentRowMapper());
    }

    public void clearAssignments(Long profileId, String companyCode, String warehouseCode, String customerCode) {
        namedJdbcTemplate.update(
                "DELETE FROM gwh_slot_assignment_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode));
    }

    public void replaceOrders(
            Long profileId, String companyCode, String warehouseCode, String customerCode,
            List<WarehouseOptimizeModels.PickOrder> orders) {
        namedJdbcTemplate.update(
                "DELETE FROM gwh_pick_order_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode));

        String orderSql = "INSERT INTO gwh_pick_order_layers " +
                "(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod, order_number, priority, status, lines_count, total_quantity, optimized_at) " +
                "VALUES (:profileId, :cpny, :whs, :cust, :orderNumber, :priority, :status, :linesCount, :totalQuantity, :optimizedAt) RETURNING id";
        String lineSql = "INSERT INTO gwh_order_line_layers (order_id, sku, product_name, location, quantity, pick_sequence, original_sequence) " +
                "VALUES (:orderId, :sku, :productName, :location, :quantity, :pickSequence, :originalSequence)";

        for (WarehouseOptimizeModels.PickOrder order : orders) {
            Long orderId = namedJdbcTemplate.queryForObject(orderSql, new MapSqlParameterSource()
                    .addValue("profileId", profileId)
                    .addValue("cpny", companyCode)
                    .addValue("whs", warehouseCode)
                    .addValue("cust", customerCode)
                    .addValue("orderNumber", order.getOrderNumber())
                    .addValue("priority", order.getPriority())
                    .addValue("status", order.getStatus())
                    .addValue("linesCount", order.getLinesCount())
                    .addValue("totalQuantity", order.getTotalQuantity())
                    .addValue("optimizedAt", order.getOptimizedAt() == null ? null : Timestamp.valueOf(order.getOptimizedAt())), Long.class);
            order.setId(orderId);
            for (WarehouseOptimizeModels.OrderLine line : order.getLines()) {
                namedJdbcTemplate.update(lineSql, new MapSqlParameterSource()
                        .addValue("orderId", orderId)
                        .addValue("sku", line.getSku())
                        .addValue("productName", line.getProductName())
                        .addValue("location", line.getLocation())
                        .addValue("quantity", line.getQuantity())
                        .addValue("pickSequence", line.getPickSequence())
                        .addValue("originalSequence", line.getOriginalSequence()));
            }
        }
    }

    public List<WarehouseOptimizeModels.PickOrder> loadOrders(
            Long profileId, String companyCode, String warehouseCode, String customerCode) {
        String sql = "SELECT id, profile_id, op_cust_cod, order_number, priority, status, lines_count, total_quantity, created_at, optimized_at " +
                "FROM gwh_pick_order_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust ORDER BY created_at DESC, id DESC";
        return namedJdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                pickOrderRowMapper());
    }

    public WarehouseOptimizeModels.PickOrder loadOrderDetail(Long orderId, String companyCode, String warehouseCode, String customerCode) {
        String sql = "SELECT id, profile_id, op_cust_cod, order_number, priority, status, lines_count, total_quantity, created_at, optimized_at " +
                "FROM gwh_pick_order_layers WHERE id = :orderId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust";
        List<WarehouseOptimizeModels.PickOrder> orders = namedJdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("orderId", orderId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                pickOrderRowMapper());
        if (orders.isEmpty()) {
            return null;
        }
        WarehouseOptimizeModels.PickOrder order = orders.get(0);
        order.setLines(loadOrderLines(orderId));
        return order;
    }

    public List<WarehouseOptimizeModels.OrderLine> loadOrderLines(Long orderId) {
        return namedJdbcTemplate.query(
                "SELECT id, sku, product_name, location, quantity, pick_sequence, original_sequence FROM gwh_order_line_layers WHERE order_id = :orderId ORDER BY original_sequence, id",
                new MapSqlParameterSource("orderId", orderId),
                orderLineRowMapper());
    }

    public void saveOptimizationResults(Long orderId, WarehouseOptimizeModels.RouteOptimizationResult result) {
        namedJdbcTemplate.update("DELETE FROM gwh_optimization_result_layers WHERE order_id = :orderId",
                new MapSqlParameterSource("orderId", orderId));

        String sql = "INSERT INTO gwh_optimization_result_layers " +
                "(order_id, algorithm, original_distance, optimized_distance, distance_saved, percent_improvement, route_data) " +
                "VALUES (:orderId, :algorithm, :originalDistance, :optimizedDistance, :distanceSaved, :percentImprovement, CAST(:routeData AS JSONB))";

        for (WarehouseOptimizeModels.RouteResult optimized : result.getOptimized()) {
            BigDecimal originalDistance = result.getOriginal() == null ? BigDecimal.ZERO : result.getOriginal().getDistance();
            BigDecimal optimizedDistance = optimized.getDistance() == null ? BigDecimal.ZERO : optimized.getDistance();
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("route", optimized.getRoute());
            payload.put("method", optimized.getMethod());
            String routeJson = toJson(payload);
            namedJdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("orderId", orderId)
                    .addValue("algorithm", optimized.getMethod())
                    .addValue("originalDistance", originalDistance)
                    .addValue("optimizedDistance", optimizedDistance)
                    .addValue("distanceSaved", originalDistance.subtract(optimizedDistance))
                    .addValue("percentImprovement", optimized.getPercent())
                    .addValue("routeData", routeJson));
        }

        if (result.getBest() != null) {
            namedJdbcTemplate.update(
                    "UPDATE gwh_pick_order_layers SET status = 'OPTIMIZED', optimized_at = NOW() WHERE id = :orderId",
                    new MapSqlParameterSource("orderId", orderId));
            int sequence = 1;
            for (String location : result.getBest().getRoute()) {
                namedJdbcTemplate.update(
                        "UPDATE gwh_order_line_layers SET pick_sequence = :pickSequence WHERE order_id = :orderId AND location = :location",
                        new MapSqlParameterSource()
                                .addValue("pickSequence", sequence++)
                                .addValue("orderId", orderId)
                                .addValue("location", location));
            }
        }
    }

    public List<Map<String, Object>> loadStoredOptimizationResults(Long orderId) {
        return namedJdbcTemplate.queryForList(
                "SELECT algorithm, original_distance, optimized_distance, distance_saved, percent_improvement, route_data::text AS route_data, created_at " +
                        "FROM gwh_optimization_result_layers WHERE order_id = :orderId ORDER BY created_at DESC",
                new MapSqlParameterSource("orderId", orderId));
    }

    public WarehouseOptimizeModels.WarehouseOptimizeAnalytics loadAnalytics(
            Long profileId, String companyCode, String warehouseCode, String customerCode) {
        WarehouseOptimizeModels.WarehouseOptimizeAnalytics analytics = new WarehouseOptimizeModels.WarehouseOptimizeAnalytics();

        List<WarehouseOptimizeModels.VelocityDistribution> velocity = namedJdbcTemplate.query(
                "SELECT velocity_class, COUNT(*) AS count, AVG(assignment_score) AS avg_score " +
                        "FROM gwh_slot_assignment_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust " +
                        "GROUP BY velocity_class ORDER BY velocity_class",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                (rs, rowNum) -> {
                    WarehouseOptimizeModels.VelocityDistribution distribution = new WarehouseOptimizeModels.VelocityDistribution();
                    distribution.setVelocityClass(rs.getString("velocity_class"));
                    distribution.setCount(rs.getLong("count"));
                    distribution.setAvgScore(rs.getBigDecimal("avg_score"));
                    return distribution;
                });
        analytics.getSlotting().setVelocityDistribution(velocity);

        List<WarehouseOptimizeModels.ZoneDistribution> zones = namedJdbcTemplate.query(
                "SELECT COALESCE(zone, 'UNASSIGNED') AS zone, COUNT(*) AS count " +
                        "FROM gwh_slot_assignment_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust " +
                        "GROUP BY COALESCE(zone, 'UNASSIGNED') ORDER BY zone",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                (rs, rowNum) -> {
                    WarehouseOptimizeModels.ZoneDistribution distribution = new WarehouseOptimizeModels.ZoneDistribution();
                    distribution.setZone(rs.getString("zone"));
                    distribution.setCount(rs.getLong("count"));
                    return distribution;
                });
        analytics.getSlotting().setZoneDistribution(zones);

        List<WarehouseOptimizeModels.AlgorithmComparison> algorithmComparison = namedJdbcTemplate.query(
                "SELECT algorithm, COUNT(*) AS order_count, AVG(original_distance) AS avg_original_distance, AVG(optimized_distance) AS avg_optimized_distance, AVG(percent_improvement) AS avg_improvement " +
                        "FROM gwh_optimization_result_layers r " +
                        "JOIN gwh_pick_order_layers o ON o.id = r.order_id " +
                        "WHERE o.profile_id = :profileId AND o.op_cpny_cod = :cpny AND o.op_whs_cod = :whs AND o.op_cust_cod = :cust " +
                        "GROUP BY algorithm ORDER BY avg_improvement DESC NULLS LAST",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                (rs, rowNum) -> {
                    WarehouseOptimizeModels.AlgorithmComparison comparison = new WarehouseOptimizeModels.AlgorithmComparison();
                    comparison.setAlgorithm(rs.getString("algorithm"));
                    comparison.setOrderCount(rs.getLong("order_count"));
                    comparison.setAvgOriginalDistance(rs.getBigDecimal("avg_original_distance"));
                    comparison.setAvgOptimizedDistance(rs.getBigDecimal("avg_optimized_distance"));
                    comparison.setAvgImprovement(rs.getBigDecimal("avg_improvement"));
                    return comparison;
                });
        analytics.getPicking().setAlgorithmComparison(algorithmComparison);

        analytics.getPicking().setTotalOrders(namedJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM gwh_pick_order_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                Long.class));
        analytics.getPicking().setOptimizedOrders(namedJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM gwh_pick_order_layers WHERE profile_id = :profileId AND op_cpny_cod = :cpny AND op_whs_cod = :whs AND op_cust_cod = :cust AND status = 'OPTIMIZED'",
                new MapSqlParameterSource()
                        .addValue("profileId", profileId)
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode),
                Long.class));
        return analytics;
    }

    public Map<String, WarehouseOptimizeModels.ProductRecord> loadProductsBySku(
            String companyCode, String warehouseCode, String customerCode) {
        Map<String, WarehouseOptimizeModels.ProductRecord> map = new HashMap<>();
        for (WarehouseOptimizeModels.ProductRecord product : loadProducts(companyCode, warehouseCode, customerCode)) {
            map.put(product.getSku(), product);
        }
        return map;
    }

    public List<Map<String, Object>> loadOracleStockRows(String companyCode, String warehouseCode, String customerCode, LocalDateTime cursor) {
        RuntimeException lastException = null;
        for (String source : ORACLE_STOCK_SOURCE_CANDIDATES) {
            try {
                return loadOracleStockRowsFromSource(source, companyCode, warehouseCode, customerCode, cursor);
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
                lastException = ex;
            }
        }
        if (lastException != null) {
            throw lastException;
        }
        return Collections.emptyList();
    }

    private List<Map<String, Object>> loadOracleStockRowsFromSource(
            String source,
            String companyCode,
            String warehouseCode,
            String customerCode,
            LocalDateTime cursor) {
        String locationExpr =
                "NVL(TRIM(TO_CHAR(ST_AREA_COD)), '') || " +
                "NVL(TRIM(TO_CHAR(ST_RACK_COD)), '') || " +
                "NVL(TRIM(TO_CHAR(ST_PSTN_COD)), '') || " +
                "NVL(TRIM(TO_CHAR(ST_LVL_COD)), '')";
        String productExpr = "NVL(TRIM(TO_CHAR(ST_PROD_COD)), '')";

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(locationExpr).append(" AS LOCATION, ");
        sql.append(productExpr).append(" AS PRODUCT_CODE, ");
        sql.append("SUM(NVL(ST_PYST_QTY, 0)) AS PHYSICAL_QTY, ");
        sql.append("SUM(NVL(ST_AVST_QTY, 0)) AS AVAILABLE_QTY, ");
        sql.append("MAX(UPD_YMDHMS) AS UPDATED_AT ");
        sql.append("FROM ").append(source).append(" ");
        sql.append("WHERE ST_CPNY_COD = :cpny AND ST_WHS_COD = :whs AND ST_CUST_COD = :cust AND DEL_FLG = 0 ");
        if (cursor != null) {
            sql.append("AND UPD_YMDHMS > :cursor ");
        }
        sql.append("GROUP BY ").append(locationExpr).append(", ").append(productExpr);

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("cpny", companyCode);
        query.setParameter("whs", warehouseCode);
        query.setParameter("cust", customerCode);
        if (cursor != null) {
            query.setParameter("cursor", Timestamp.valueOf(cursor));
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        List<Map<String, Object>> mapped = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> item = new HashMap<>();
            item.put("location", row[0] == null ? "" : row[0].toString().trim());
            item.put("productCode", row[1] == null ? "" : row[1].toString().trim());
            item.put("physicalQty", row[2] instanceof BigDecimal ? row[2] : new BigDecimal(String.valueOf(row[2] == null ? "0" : row[2])));
            item.put("availableQty", row[3] instanceof BigDecimal ? row[3] : new BigDecimal(String.valueOf(row[3] == null ? "0" : row[3])));
            item.put("updatedAt", toLocalDateTimeValue(row[4]));
            mapped.add(item);
        }
        return mapped;
    }

    public String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize warehouse optimize payload", ex);
        }
    }

    public List<WarehouseOptimizeModels.WarehouseLayoutLocation> parseLocations(String layoutJson) {
        if (layoutJson == null || layoutJson.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            JsonNode root = objectMapper.readTree(layoutJson);
            JsonNode aisles = root.path("aisles");
            List<WarehouseOptimizeModels.WarehouseLayoutLocation> locations = new ArrayList<>();
            if (aisles.isArray()) {
                for (JsonNode aisle : aisles) {
                    JsonNode locs = aisle.path("locations");
                    if (!locs.isArray()) {
                        continue;
                    }
                    for (JsonNode locationNode : locs) {
                        WarehouseOptimizeModels.WarehouseLayoutLocation location = new WarehouseOptimizeModels.WarehouseLayoutLocation();
                        location.setLocation(text(locationNode, "location"));
                        location.setZone(text(locationNode, "zone"));
                        location.setType(text(locationNode, "type"));
                        location.setLevel(intValue(locationNode, "level"));
                        location.setAisle(intValue(locationNode, "aisle"));
                        location.setPosition(intValue(locationNode, "position"));
                        location.setSide(text(locationNode, "side"));
                        location.setX(decimalValue(locationNode, "x"));
                        location.setY(decimalValue(locationNode, "y"));
                        location.setSlottedClass(text(locationNode, "slottedClass"));
                        location.setSlottedSku(text(locationNode, "slottedSku"));
                        locations.add(location);
                    }
                }
            }
            return locations;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse layout JSON", ex);
        }
    }

    private WarehouseOptimizeModels.WarehouseProfileDetail mapProfileDetail(ResultSet rs) throws SQLException {
        WarehouseOptimizeModels.WarehouseProfileDetail detail = new WarehouseOptimizeModels.WarehouseProfileDetail();
        detail.setId(rs.getLong("id"));
        detail.setCompanyCode(rs.getString("op_cpny_cod"));
        detail.setWarehouseCode(rs.getString("op_whs_cod"));
        detail.setCustomerCode(rs.getString("op_cust_cod"));
        detail.setProfileName(rs.getString("op_pf_name"));
        detail.setDescription(rs.getString("description"));
        detail.setWarehouseLength(rs.getBigDecimal("warehouse_length"));
        detail.setWarehouseWidth(rs.getBigDecimal("warehouse_width"));
        detail.setLayoutData(rs.getString("layout_data"));
        detail.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
        detail.setUpdatedAt(toLocalDateTime(rs.getTimestamp("updated_at")));
        detail.setSharedProfile("*".equals(rs.getString("op_cust_cod")));
        detail.setLocations(parseLocations(detail.getLayoutData()));
        return detail;
    }

    private RowMapper<WarehouseOptimizeModels.WarehouseProfileSummary> profileSummaryRowMapper() {
        return (rs, rowNum) -> {
            WarehouseOptimizeModels.WarehouseProfileSummary summary = new WarehouseOptimizeModels.WarehouseProfileSummary();
            summary.setId(rs.getLong("id"));
            summary.setCompanyCode(rs.getString("op_cpny_cod"));
            summary.setWarehouseCode(rs.getString("op_whs_cod"));
            summary.setCustomerCode(rs.getString("op_cust_cod"));
            summary.setProfileName(rs.getString("op_pf_name"));
            summary.setDescription(rs.getString("description"));
            summary.setWarehouseLength(rs.getBigDecimal("warehouse_length"));
            summary.setWarehouseWidth(rs.getBigDecimal("warehouse_width"));
            summary.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
            summary.setUpdatedAt(toLocalDateTime(rs.getTimestamp("updated_at")));
            summary.setSharedProfile("*".equals(rs.getString("op_cust_cod")));
            return summary;
        };
    }

    private RowMapper<WarehouseOptimizeModels.ProductRecord> productRowMapper() {
        return (rs, rowNum) -> {
            WarehouseOptimizeModels.ProductRecord product = new WarehouseOptimizeModels.ProductRecord();
            product.setId(rs.getLong("id"));
            product.setSku(rs.getString("sku"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setWeight(rs.getBigDecimal("weight"));
            product.setVolume(rs.getBigDecimal("volume"));
            product.setWidth(rs.getBigDecimal("width"));
            product.setHeight(rs.getBigDecimal("height"));
            product.setDepth(rs.getBigDecimal("depth"));
            product.setDemandFrequency(rs.getBigDecimal("demand_frequency"));
            product.setVelocityClass(rs.getString("velocity_class"));
            product.setVariabilityClass(rs.getString("variability_class"));
            product.setUom(rs.getString("uom"));
            product.setPicksPerDay(rs.getBigDecimal("picks_per_day"));
            product.setAvgQtyPerPick(rs.getBigDecimal("avg_qty_per_pick"));
            return product;
        };
    }

    private RowMapper<WarehouseOptimizeModels.SlottingAssignment> assignmentRowMapper() {
        return (rs, rowNum) -> {
            WarehouseOptimizeModels.SlottingAssignment assignment = new WarehouseOptimizeModels.SlottingAssignment();
            assignment.setId(rs.getLong("id"));
            assignment.setProfileId(rs.getLong("profile_id"));
            assignment.setCustomerCode(rs.getString("op_cust_cod"));
            assignment.setLocation(rs.getString("location"));
            assignment.setZone(rs.getString("zone"));
            assignment.setProductSku(rs.getString("product_sku"));
            assignment.setProductName(rs.getString("product_name"));
            assignment.setProductCategory(rs.getString("product_category"));
            assignment.setVelocityClass(rs.getString("velocity_class"));
            assignment.setDemandFrequency(rs.getBigDecimal("demand_frequency"));
            assignment.setAssignmentScore(rs.getBigDecimal("assignment_score"));
            assignment.setAssignmentReason(rs.getString("assignment_reason"));
            assignment.setAssignedAt(toLocalDateTime(rs.getTimestamp("assigned_at")));
            return assignment;
        };
    }

    private RowMapper<WarehouseOptimizeModels.PickOrder> pickOrderRowMapper() {
        return (rs, rowNum) -> {
            WarehouseOptimizeModels.PickOrder order = new WarehouseOptimizeModels.PickOrder();
            order.setId(rs.getLong("id"));
            order.setProfileId(rs.getLong("profile_id"));
            order.setCustomerCode(rs.getString("op_cust_cod"));
            order.setOrderNumber(rs.getString("order_number"));
            order.setPriority(rs.getInt("priority"));
            order.setStatus(rs.getString("status"));
            order.setLinesCount(rs.getInt("lines_count"));
            order.setTotalQuantity(rs.getInt("total_quantity"));
            order.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
            order.setOptimizedAt(toLocalDateTime(rs.getTimestamp("optimized_at")));
            return order;
        };
    }

    private RowMapper<WarehouseOptimizeModels.OrderLine> orderLineRowMapper() {
        return (rs, rowNum) -> {
            WarehouseOptimizeModels.OrderLine line = new WarehouseOptimizeModels.OrderLine();
            line.setId(rs.getLong("id"));
            line.setSku(rs.getString("sku"));
            line.setProductName(rs.getString("product_name"));
            line.setLocation(rs.getString("location"));
            line.setQuantity(rs.getInt("quantity"));
            line.setPickSequence((Integer) rs.getObject("pick_sequence"));
            line.setOriginalSequence((Integer) rs.getObject("original_sequence"));
            return line;
        };
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private LocalDateTime toLocalDateTimeValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }
        if (value instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) value).getTime()).toLocalDateTime();
        }
        if (value instanceof java.time.LocalDateTime) {
            return (LocalDateTime) value;
        }
        return Timestamp.valueOf(String.valueOf(value)).toLocalDateTime();
    }

    private boolean isObjectNotFound(RuntimeException ex) {
        Throwable cursor = ex;
        while (cursor != null) {
            String message = cursor.getMessage();
            if (message != null && message.contains("ORA-00942")) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }

    private String text(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }

    private Integer intValue(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        return value.isMissingNode() || value.isNull() ? null : value.asInt();
    }

    private BigDecimal decimalValue(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        if (value.isMissingNode() || value.isNull()) {
            return null;
        }
        return value.decimalValue();
    }
}
