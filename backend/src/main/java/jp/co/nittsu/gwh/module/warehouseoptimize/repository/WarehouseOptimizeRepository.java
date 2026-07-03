package jp.co.nittsu.gwh.module.warehouseoptimize.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.nittsu.gwh.config.OracleObjectNameResolver;
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
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class WarehouseOptimizeRepository {

    private static final String STOCK_SOURCE_CACHE_KEY = "oracle.stock";
    private static final String LOCATION_SOURCE_CACHE_KEY = "oracle.location";
    private static final String AREA_SOURCE_CACHE_KEY = "oracle.area";
    private static final String INBOUND_HEADER_SOURCE_CACHE_KEY = "oracle.inboundHeader";
    private static final String INBOUND_DETAIL_SOURCE_CACHE_KEY = "oracle.inboundDetail";
    private static final String OUTBOUND_HEADER_SOURCE_CACHE_KEY = "oracle.outboundHeader";
    private static final String OUTBOUND_DETAIL_SOURCE_CACHE_KEY = "oracle.outboundDetail";
    private static final String NO_SOURCE = "__NONE__";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final JdbcTemplate oracleJdbcTemplate;
    private final NamedParameterJdbcTemplate oracleNamedJdbcTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentMap<String, String> sourceCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> stockColumnPresenceCache = new ConcurrentHashMap<>();
    private final String[] oracleStockSourceCandidates;
    private final String[] oracleLocationSourceCandidates;
    private final String[] oracleAreaSourceCandidates;
    private final String[] oracleInboundHeaderSourceCandidates;
    private final String[] oracleInboundDetailSourceCandidates;
    private final String[] oracleOutboundHeaderSourceCandidates;
    private final String[] oracleOutboundDetailSourceCandidates;

    @PersistenceContext
    private EntityManager entityManager;

    public WarehouseOptimizeRepository(
            @Qualifier("warehouseOptimizeJdbcTemplate") JdbcTemplate jdbcTemplate,
            @Qualifier("warehouseOptimizeNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedJdbcTemplate,
            @Qualifier("dataSource") DataSource oracleDataSource,
            ObjectMapper objectMapper,
            OracleObjectNameResolver oracleObjectNameResolver) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.oracleJdbcTemplate = new JdbcTemplate(oracleDataSource);
        this.oracleJdbcTemplate.setFetchSize(1000);
        this.oracleNamedJdbcTemplate = new NamedParameterJdbcTemplate(this.oracleJdbcTemplate);
        this.objectMapper = objectMapper;
        this.oracleStockSourceCandidates = oracleObjectNameResolver.preferredCandidates("VGWH_TJ_ST", "GWH_TJ_ST");
        this.oracleLocationSourceCandidates = oracleObjectNameResolver.preferredCandidates("GWH_TM_LOC");
        this.oracleAreaSourceCandidates = oracleObjectNameResolver.preferredCandidates("GWH_TM_AREA");
        this.oracleInboundHeaderSourceCandidates = oracleObjectNameResolver.preferredCandidates("GWH_TJ_AV_H", "VGWH_TJ_AV_H");
        this.oracleInboundDetailSourceCandidates = oracleObjectNameResolver.preferredCandidates("GWH_TJ_AV_D", "VGWH_TJ_AV_D");
        this.oracleOutboundHeaderSourceCandidates = oracleObjectNameResolver.preferredCandidates("GWH_TJ_SP_H", "VGWH_TJ_SP_H");
        this.oracleOutboundDetailSourceCandidates = oracleObjectNameResolver.preferredCandidates("GWH_TJ_SP_D", "VGWH_TJ_SP_D");
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

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS gwh_location_hierarchy_settings (" +
                "id SERIAL PRIMARY KEY," +
                "op_cpny_cod VARCHAR(50) NOT NULL," +
                "op_whs_cod VARCHAR(50) NOT NULL," +
                "op_cust_cod VARCHAR(50)," +
                "mapping_data JSONB NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");

        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_products_tenant ON gwh_products(op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_profiles_scope ON gwh_warehouse_profiles(op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_assignments_scope ON gwh_slot_assignment_layers(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_orders_scope ON gwh_pick_order_layers(profile_id, op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_order_lines_order ON gwh_order_line_layers(order_id)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_results_order ON gwh_optimization_result_layers(order_id)");
        jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS idx_woz_hierarchy_scope ON gwh_location_hierarchy_settings(op_cpny_cod, op_whs_cod, op_cust_cod)");
        jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS uq_woz_hierarchy_scope ON gwh_location_hierarchy_settings(op_cpny_cod, op_whs_cod, COALESCE(op_cust_cod, ''))");
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

    public WarehouseOptimizeModels.WarehouseProfileSummary findProfileSummary(Long id) {
        String sql = "SELECT id, op_cpny_cod, op_whs_cod, op_cust_cod, op_pf_name, description, warehouse_length, warehouse_width, created_at, updated_at " +
                "FROM gwh_warehouse_profiles WHERE id = :id";
        List<WarehouseOptimizeModels.WarehouseProfileSummary> results = namedJdbcTemplate.query(
                sql, new MapSqlParameterSource("id", id), profileSummaryRowMapper());
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

    public WarehouseOptimizeModels.WarehouseLocationHierarchySetting findLocationHierarchySetting(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        String sql = "SELECT id, op_cpny_cod, op_whs_cod, op_cust_cod, mapping_data::text, created_at, updated_at " +
                "FROM gwh_location_hierarchy_settings " +
                "WHERE op_cpny_cod = :cpny AND op_whs_cod = :whs " +
                hierarchyScopeCondition(customerCode) +
                "LIMIT 1";
        List<WarehouseOptimizeModels.WarehouseLocationHierarchySetting> settings = namedJdbcTemplate.query(
                sql,
                hierarchyScopeParams(companyCode, warehouseCode, customerCode),
                (rs, rowNum) -> mapHierarchySetting(rs));
        return settings.isEmpty() ? null : settings.get(0);
    }

    public WarehouseOptimizeModels.WarehouseLocationHierarchySetting saveLocationHierarchySetting(
            String companyCode,
            String warehouseCode,
            String customerCode,
            WarehouseOptimizeModels.WarehouseLocationHierarchyMapping mapping) {
        String mappingJson = toJson(mapping);
        Long existingId = namedJdbcTemplate.query(
                "SELECT id FROM gwh_location_hierarchy_settings " +
                        "WHERE op_cpny_cod = :cpny AND op_whs_cod = :whs " +
                        hierarchyScopeCondition(customerCode) +
                        "LIMIT 1",
                hierarchyScopeParams(companyCode, warehouseCode, customerCode),
                rs -> rs.next() ? rs.getLong(1) : null);
        if (existingId != null) {
            namedJdbcTemplate.update(
                    "UPDATE gwh_location_hierarchy_settings " +
                            "SET mapping_data = CAST(:mapping AS JSONB), updated_at = NOW() " +
                            "WHERE id = :id",
                    new MapSqlParameterSource()
                            .addValue("id", existingId)
                            .addValue("mapping", mappingJson));
            return findLocationHierarchySetting(companyCode, warehouseCode, customerCode);
        }

        namedJdbcTemplate.update(
                "INSERT INTO gwh_location_hierarchy_settings " +
                        "(op_cpny_cod, op_whs_cod, op_cust_cod, mapping_data) " +
                        "VALUES (:cpny, :whs, :cust, CAST(:mapping AS JSONB))",
                new MapSqlParameterSource()
                        .addValue("cpny", companyCode)
                        .addValue("whs", warehouseCode)
                        .addValue("cust", customerCode)
                        .addValue("mapping", mappingJson));
        return findLocationHierarchySetting(companyCode, warehouseCode, customerCode);
    }

    public void deleteLocationHierarchySetting(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        namedJdbcTemplate.update(
                "DELETE FROM gwh_location_hierarchy_settings " +
                        "WHERE op_cpny_cod = :cpny AND op_whs_cod = :whs " +
                        hierarchyScopeCondition(customerCode),
                hierarchyScopeParams(companyCode, warehouseCode, customerCode));
    }

    public List<WarehouseOptimizeModels.OracleStockRow> loadOracleStockRows(String companyCode, String warehouseCode, String customerCode, LocalDateTime cursor) {
        RuntimeException lastException = null;
        for (String source : orderedCandidates(STOCK_SOURCE_CACHE_KEY, oracleStockSourceCandidates)) {
            try {
                List<WarehouseOptimizeModels.OracleStockRow> rows = loadOracleStockRowsFromSource(source, companyCode, warehouseCode, customerCode, cursor);
                sourceCache.put(STOCK_SOURCE_CACHE_KEY, source);
                return rows;
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex) && !isInvalidIdentifier(ex)) {
                    throw ex;
                }
                lastException = ex;
                invalidateCachedSource(STOCK_SOURCE_CACHE_KEY, source);
            }
        }
        if (lastException != null) {
            throw lastException;
        }
        return Collections.emptyList();
    }

    public List<WarehouseOptimizeModels.OracleLocationMasterRow> loadOracleLocationMasterRows(
            String companyCode,
            String warehouseCode) {
        RuntimeException lastException = null;
        for (String locationSource : orderedCandidates(LOCATION_SOURCE_CACHE_KEY, oracleLocationSourceCandidates)) {
            for (String areaSource : orderedOptionalCandidates(AREA_SOURCE_CACHE_KEY, oracleAreaSourceCandidates)) {
                try {
                    List<WarehouseOptimizeModels.OracleLocationMasterRow> rows =
                            loadOracleLocationMasterRowsFromSource(locationSource, areaSource, companyCode, warehouseCode);
                    sourceCache.put(LOCATION_SOURCE_CACHE_KEY, locationSource);
                    if (areaSource != null) {
                        sourceCache.put(AREA_SOURCE_CACHE_KEY, areaSource);
                    } else {
                        sourceCache.put(AREA_SOURCE_CACHE_KEY, NO_SOURCE);
                    }
                    return rows;
                } catch (RuntimeException ex) {
                    if (!isObjectNotFound(ex)) {
                        throw ex;
                    }
                    lastException = ex;
                    if (areaSource != null) {
                        invalidateCachedSource(AREA_SOURCE_CACHE_KEY, areaSource);
                    }
                }
            }
            invalidateCachedSource(LOCATION_SOURCE_CACHE_KEY, locationSource);
        }
        if (lastException != null) {
            throw lastException;
        }
        return Collections.emptyList();
    }

    public List<WarehouseOptimizeModels.OracleStatusAggregateRow> loadInboundStatusAggregateRows(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        RuntimeException lastException = null;
        for (String headerSource : orderedCandidates(INBOUND_HEADER_SOURCE_CACHE_KEY, oracleInboundHeaderSourceCandidates)) {
            for (String detailSource : orderedCandidates(INBOUND_DETAIL_SOURCE_CACHE_KEY, oracleInboundDetailSourceCandidates)) {
                try {
                    List<WarehouseOptimizeModels.OracleStatusAggregateRow> rows = loadStatusAggregateRowsFromSource(
                            headerSource,
                            detailSource,
                            companyCode,
                            warehouseCode,
                            customerCode,
                            "AVH_CPNY_COD",
                            "AVH_WHS_COD",
                            "AVH_CUST_COD",
                            "AVH_AV_NUM",
                            "AVH_AV_STS",
                            "AVD_CPNY_COD",
                            "AVD_WHS_COD",
                            "AVD_CUST_COD",
                            "AVD_AV_NUM",
                            "AVD_SCS_QTY",
                            "AVD_SPC_QTY");
                    sourceCache.put(INBOUND_HEADER_SOURCE_CACHE_KEY, headerSource);
                    sourceCache.put(INBOUND_DETAIL_SOURCE_CACHE_KEY, detailSource);
                    return rows;
                } catch (RuntimeException ex) {
                    if (!isObjectNotFound(ex)) {
                        throw ex;
                    }
                    lastException = ex;
                    invalidateCachedSource(INBOUND_DETAIL_SOURCE_CACHE_KEY, detailSource);
                }
            }
            invalidateCachedSource(INBOUND_HEADER_SOURCE_CACHE_KEY, headerSource);
        }
        if (lastException != null) {
            throw lastException;
        }
        return Collections.emptyList();
    }

    public List<WarehouseOptimizeModels.OracleStatusAggregateRow> loadOutboundStatusAggregateRows(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        RuntimeException lastException = null;
        for (String headerSource : orderedCandidates(OUTBOUND_HEADER_SOURCE_CACHE_KEY, oracleOutboundHeaderSourceCandidates)) {
            for (String detailSource : orderedCandidates(OUTBOUND_DETAIL_SOURCE_CACHE_KEY, oracleOutboundDetailSourceCandidates)) {
                try {
                    List<WarehouseOptimizeModels.OracleStatusAggregateRow> rows = loadStatusAggregateRowsFromSource(
                            headerSource,
                            detailSource,
                            companyCode,
                            warehouseCode,
                            customerCode,
                            "SPH_CPNY_COD",
                            "SPH_WHS_COD",
                            "SPH_CUST_COD",
                            "SPH_SP_NUM",
                            "SPH_SP_STS",
                            "SPD_CPNY_COD",
                            "SPD_WHS_COD",
                            "SPD_CUST_COD",
                            "SPD_SP_NUM",
                            "SPD_SCS_QTY",
                            "SPD_SPC_QTY");
                    sourceCache.put(OUTBOUND_HEADER_SOURCE_CACHE_KEY, headerSource);
                    sourceCache.put(OUTBOUND_DETAIL_SOURCE_CACHE_KEY, detailSource);
                    return rows;
                } catch (RuntimeException ex) {
                    if (!isObjectNotFound(ex)) {
                        throw ex;
                    }
                    lastException = ex;
                    invalidateCachedSource(OUTBOUND_DETAIL_SOURCE_CACHE_KEY, detailSource);
                }
            }
            invalidateCachedSource(OUTBOUND_HEADER_SOURCE_CACHE_KEY, headerSource);
        }
        if (lastException != null) {
            throw lastException;
        }
        return Collections.emptyList();
    }

    private List<WarehouseOptimizeModels.OracleStockRow> loadOracleStockRowsFromSource(
            String source,
            String companyCode,
            String warehouseCode,
            String customerCode,
            LocalDateTime cursor) {
        String locationExpr = stockTextColumnExpr(source, "ST_LOC_COD");
        String productExpr = "NVL(TRIM(TO_CHAR(ST_PROD_COD)), '')";

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(locationExpr).append(" AS ST_LOC_COD, ");
        sql.append("TRIM(TO_CHAR(ST_AREA_COD)) AS ST_AREA_COD, ");
        sql.append("TRIM(TO_CHAR(ST_RACK_COD)) AS ST_RACK_COD, ");
        sql.append("TRIM(TO_CHAR(ST_PSTN_COD)) AS ST_PSTN_COD, ");
        sql.append("TRIM(TO_CHAR(ST_LVL_COD)) AS ST_LVL_COD, ");
        sql.append(productExpr).append(" AS PRODUCT_CODE, ");
        sql.append("SUM(NVL(ST_PYST_QTY, 0)) AS PHYSICAL_QTY, ");
        sql.append("SUM(NVL(ST_AVST_QTY, 0)) AS AVAILABLE_QTY, ");
        sql.append("MAX(UPD_YMDHMS) AS UPDATED_AT ");
        sql.append("FROM ").append(source).append(" ");
        sql.append("WHERE ST_CPNY_COD = :cpny AND ST_WHS_COD = :whs AND ST_CUST_COD = :cust AND DEL_FLG = 0 ");
        if (cursor != null) {
            sql.append("AND UPD_YMDHMS > :cursor ");
        }
        sql.append("GROUP BY ");
        sql.append(locationExpr).append(", ");
        sql.append("TRIM(TO_CHAR(ST_AREA_COD)), ");
        sql.append("TRIM(TO_CHAR(ST_RACK_COD)), ");
        sql.append("TRIM(TO_CHAR(ST_PSTN_COD)), ");
        sql.append("TRIM(TO_CHAR(ST_LVL_COD)), ");
        sql.append(productExpr);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cpny", companyCode)
                .addValue("whs", warehouseCode)
                .addValue("cust", customerCode);
        if (cursor != null) {
            params.addValue("cursor", Timestamp.valueOf(cursor));
        }

        return oracleNamedJdbcTemplate.query(sql.toString(), params, rs -> {
            List<WarehouseOptimizeModels.OracleStockRow> mapped = new ArrayList<>();
            while (rs.next()) {
                WarehouseOptimizeModels.OracleStockRow item = new WarehouseOptimizeModels.OracleStockRow();
                item.setLocationCode(trimmedValue(rs.getObject("ST_LOC_COD")));
                item.setAreaCode(trimmedValue(rs.getObject("ST_AREA_COD")));
                item.setRackCode(trimmedValue(rs.getObject("ST_RACK_COD")));
                item.setPositionCode(trimmedValue(rs.getObject("ST_PSTN_COD")));
                item.setLevelCode(trimmedValue(rs.getObject("ST_LVL_COD")));
                item.setProductCode(trimmedValue(rs.getObject("PRODUCT_CODE")));
                item.setPhysicalQty(decimalValue(rs.getObject("PHYSICAL_QTY")));
                item.setAvailableQty(decimalValue(rs.getObject("AVAILABLE_QTY")));
                item.setUpdatedAt(toLocalDateTimeValue(rs.getObject("UPDATED_AT")));
                mapped.add(item);
            }
            return mapped;
        });
    }

    private List<WarehouseOptimizeModels.OracleLocationMasterRow> loadOracleLocationMasterRowsFromSource(
            String locationSource,
            String areaSource,
            String companyCode,
            String warehouseCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("l.LOC_COD AS LOC_COD, ");
        sql.append("l.LOC_AREA_COD AS LOC_AREA_COD, ");
        if (areaSource != null) {
            sql.append("a.AREA_NAM AS AREA_NAM, ");
        } else {
            sql.append("CAST(NULL AS VARCHAR2(255)) AS AREA_NAM, ");
        }
        sql.append("l.LOC_RACK_COD AS LOC_RACK_COD, ");
        sql.append("l.LOC_PSTN_COD AS LOC_PSTN_COD, ");
        sql.append("l.LOC_LVL_COD AS LOC_LVL_COD, ");
        sql.append("l.LOC_LEN AS LOC_LEN, ");
        sql.append("l.LOC_WID AS LOC_WID, ");
        sql.append("l.LOC_HIG AS LOC_HIG ");
        sql.append("FROM ").append(locationSource).append(" l ");
        if (areaSource != null) {
            sql.append("LEFT JOIN ").append(areaSource).append(" a ");
            sql.append("ON a.AREA_CPNY_COD = l.LOC_CPNY_COD ");
            sql.append("AND a.AREA_WHS_COD = l.LOC_WHS_COD ");
            sql.append("AND TRIM(TO_CHAR(a.AREA_COD)) = TRIM(TO_CHAR(l.LOC_AREA_COD)) ");
            sql.append("AND a.DEL_FLG = 0 ");
        }
        sql.append("WHERE l.LOC_CPNY_COD = :cpny ");
        sql.append("AND l.LOC_WHS_COD = :whs ");
        sql.append("AND l.DEL_FLG = 0 ");
        sql.append("ORDER BY l.LOC_AREA_COD, l.LOC_RACK_COD, l.LOC_PSTN_COD, l.LOC_LVL_COD, l.LOC_COD");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cpny", companyCode)
                .addValue("whs", warehouseCode);

        return oracleNamedJdbcTemplate.query(sql.toString(), params, rs -> {
            List<WarehouseOptimizeModels.OracleLocationMasterRow> mapped = new ArrayList<>();
            while (rs.next()) {
                WarehouseOptimizeModels.OracleLocationMasterRow item = new WarehouseOptimizeModels.OracleLocationMasterRow();
                item.setLocationCode(trimmedValue(rs.getObject("LOC_COD")));
                item.setAreaCode(trimmedValue(rs.getObject("LOC_AREA_COD")));
                item.setAreaName(trimmedValue(rs.getObject("AREA_NAM")));
                item.setRackCode(trimmedValue(rs.getObject("LOC_RACK_COD")));
                item.setPositionCode(trimmedValue(rs.getObject("LOC_PSTN_COD")));
                item.setLevelCode(trimmedValue(rs.getObject("LOC_LVL_COD")));
                item.setLength(decimalValue(rs.getObject("LOC_LEN")));
                item.setWidth(decimalValue(rs.getObject("LOC_WID")));
                item.setHeight(decimalValue(rs.getObject("LOC_HIG")));
                mapped.add(item);
            }
            return mapped;
        });
    }

    private List<WarehouseOptimizeModels.OracleStatusAggregateRow> loadStatusAggregateRowsFromSource(
            String headerSource,
            String detailSource,
            String companyCode,
            String warehouseCode,
            String customerCode,
            String headerCompanyColumn,
            String headerWarehouseColumn,
            String headerCustomerColumn,
            String headerOrderColumn,
            String headerStatusColumn,
            String detailCompanyColumn,
            String detailWarehouseColumn,
            String detailCustomerColumn,
            String detailOrderColumn,
            String detailCasesColumn,
            String detailPiecesColumn) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("TRIM(TO_CHAR(h.").append(headerStatusColumn).append(")) AS STATUS_CODE, ");
        sql.append("COUNT(DISTINCT h.").append(headerOrderColumn).append(") AS ORDER_COUNT, ");
        sql.append("NVL(SUM(NVL(d.").append(detailCasesColumn).append(", 0)), 0) AS CS_QTY, ");
        sql.append("NVL(SUM(NVL(d.").append(detailPiecesColumn).append(", 0)), 0) AS PCS_QTY ");
        sql.append("FROM ").append(headerSource).append(" h ");
        sql.append("LEFT JOIN ").append(detailSource).append(" d ");
        sql.append("ON d.").append(detailCompanyColumn).append(" = h.").append(headerCompanyColumn).append(" ");
        sql.append("AND d.").append(detailWarehouseColumn).append(" = h.").append(headerWarehouseColumn).append(" ");
        sql.append("AND d.").append(detailCustomerColumn).append(" = h.").append(headerCustomerColumn).append(" ");
        sql.append("AND d.").append(detailOrderColumn).append(" = h.").append(headerOrderColumn).append(" ");
        sql.append("AND d.DEL_FLG = 0 ");
        sql.append("WHERE h.").append(headerCompanyColumn).append(" = :cpny ");
        sql.append("AND h.").append(headerWarehouseColumn).append(" = :whs ");
        sql.append("AND h.").append(headerCustomerColumn).append(" = :cust ");
        sql.append("AND h.DEL_FLG = 0 ");
        sql.append("GROUP BY h.").append(headerStatusColumn).append(" ");
        sql.append("ORDER BY h.").append(headerStatusColumn);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cpny", companyCode)
                .addValue("whs", warehouseCode)
                .addValue("cust", customerCode);

        return oracleNamedJdbcTemplate.query(sql.toString(), params, rs -> {
            List<WarehouseOptimizeModels.OracleStatusAggregateRow> mapped = new ArrayList<>();
            while (rs.next()) {
                WarehouseOptimizeModels.OracleStatusAggregateRow item = new WarehouseOptimizeModels.OracleStatusAggregateRow();
                item.setStatusCode(trimmedValue(rs.getObject("STATUS_CODE")));
                item.setOrderCount(rs.getLong("ORDER_COUNT"));
                item.setCsQty(decimalValue(rs.getObject("CS_QTY")));
                item.setPcsQty(decimalValue(rs.getObject("PCS_QTY")));
                mapped.add(item);
            }
            return mapped;
        });
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
                        location.setFootprintWidth(decimalValue(locationNode, "footprintWidth"));
                        location.setFootprintDepth(decimalValue(locationNode, "footprintDepth"));
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

    private WarehouseOptimizeModels.WarehouseLocationHierarchySetting mapHierarchySetting(ResultSet rs) throws SQLException {
        WarehouseOptimizeModels.WarehouseLocationHierarchySetting setting = new WarehouseOptimizeModels.WarehouseLocationHierarchySetting();
        setting.setId(rs.getLong("id"));
        setting.setCompanyCode(rs.getString("op_cpny_cod"));
        setting.setWarehouseCode(rs.getString("op_whs_cod"));
        setting.setCustomerCode(rs.getString("op_cust_cod"));
        setting.setScope(rs.getString("op_cust_cod") == null
                ? WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE
                : WarehouseOptimizeModels.WarehouseLocationHierarchyScope.WAREHOUSE_CUSTOMER);
        setting.setMapping(parseHierarchyMapping(rs.getString("mapping_data")));
        setting.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
        setting.setUpdatedAt(toLocalDateTime(rs.getTimestamp("updated_at")));
        return setting;
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

    private String hierarchyScopeCondition(String customerCode) {
        return customerCode == null
                ? "AND op_cust_cod IS NULL "
                : "AND op_cust_cod = :cust ";
    }

    private MapSqlParameterSource hierarchyScopeParams(
            String companyCode,
            String warehouseCode,
            String customerCode) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cpny", companyCode)
                .addValue("whs", warehouseCode);
        if (customerCode != null) {
            params.addValue("cust", customerCode);
        }
        return params;
    }

    private WarehouseOptimizeModels.WarehouseLocationHierarchyMapping parseHierarchyMapping(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, WarehouseOptimizeModels.WarehouseLocationHierarchyMapping.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse warehouse location hierarchy mapping", ex);
        }
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

    private boolean isInvalidIdentifier(RuntimeException ex) {
        Throwable cursor = ex;
        while (cursor != null) {
            String message = cursor.getMessage();
            if (message != null && message.contains("ORA-00904")) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }

    private String stockTextColumnExpr(String source, String columnName) {
        return hasOracleColumn(source, columnName)
                ? "TRIM(TO_CHAR(" + columnName + "))"
                : "CAST(NULL AS VARCHAR2(255))";
    }

    private boolean hasOracleColumn(String source, String columnName) {
        String cacheKey = source + "::" + columnName.toUpperCase(Locale.ROOT);
        return stockColumnPresenceCache.computeIfAbsent(cacheKey, key -> loadOracleColumnPresence(source, columnName));
    }

    private boolean loadOracleColumnPresence(String source, String columnName) {
        return oracleJdbcTemplate.query("SELECT * FROM " + source + " WHERE 1 = 0", rs -> {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int index = 1; index <= metaData.getColumnCount(); index++) {
                if (columnName.equalsIgnoreCase(metaData.getColumnLabel(index))
                        || columnName.equalsIgnoreCase(metaData.getColumnName(index))) {
                    return true;
                }
            }
            return false;
        });
    }

    private String resolveOptionalSource(String cacheKey, String[] candidates) {
        String cached = sourceCache.get(cacheKey);
        if (NO_SOURCE.equals(cached)) {
            return null;
        }
        if (cached != null) {
            return cached;
        }

        for (String source : candidates) {
            try {
                Query query = entityManager.createNativeQuery("SELECT 1 FROM " + source + " WHERE ROWNUM = 1");
                query.getResultList();
                sourceCache.put(cacheKey, source);
                return source;
            } catch (RuntimeException ex) {
                if (!isObjectNotFound(ex)) {
                    throw ex;
                }
            }
        }
        sourceCache.put(cacheKey, NO_SOURCE);
        return null;
    }

    private String resolveRequiredSource(String cacheKey, String[] candidates) {
        String cached = sourceCache.get(cacheKey);
        if (cached != null && !NO_SOURCE.equals(cached)) {
            return cached;
        }

        RuntimeException lastException = null;
        for (String source : orderedCandidates(cacheKey, candidates)) {
            try {
                Query query = entityManager.createNativeQuery("SELECT 1 FROM " + source + " WHERE ROWNUM = 1");
                query.getResultList();
                sourceCache.put(cacheKey, source);
                return source;
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
        throw new IllegalStateException("No Oracle source could be resolved for " + cacheKey);
    }

    private List<String> orderedCandidates(String cacheKey, String[] candidates) {
        List<String> ordered = new ArrayList<>();
        String cached = sourceCache.get(cacheKey);
        if (cached != null && !cached.isEmpty() && !NO_SOURCE.equals(cached)) {
            ordered.add(cached);
        }
        Arrays.stream(candidates)
                .filter(candidate -> !ordered.contains(candidate))
                .forEach(ordered::add);
        return ordered;
    }

    private List<String> orderedOptionalCandidates(String cacheKey, String[] candidates) {
        if (NO_SOURCE.equals(sourceCache.get(cacheKey))) {
            return Collections.singletonList(null);
        }

        List<String> ordered = orderedCandidates(cacheKey, candidates);
        ordered.add(null);
        return ordered;
    }

    private void invalidateCachedSource(String cacheKey, String source) {
        if (source == null) {
            return;
        }
        sourceCache.remove(cacheKey, source);
        if (NO_SOURCE.equals(sourceCache.get(cacheKey))) {
            sourceCache.remove(cacheKey, NO_SOURCE);
        }
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

    private String trimmedValue(Object value) {
        return value == null ? null : value.toString().trim();
    }

    private BigDecimal decimalValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(String.valueOf(value));
    }
}
