package jp.co.nittsu.gwh.module.inbound.repository;

import jp.co.nittsu.gwh.module.inbound.dto.ArrivalInspectionListDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionLineDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for the arrival inspection feature.
 *
 * All queries target arrival statuses that are eligible for inspection:
 *   100 (Created), 200 (Located), 300 (Receiving).
 * After a successful inspection the header status moves to 205 (Inspected).
 */
@Repository
public class ArrivalInspectionRepository {

    private static final Logger log = LoggerFactory.getLogger(ArrivalInspectionRepository.class);

    /** Status codes that can be selected for inspection */
    private static final String INSPECTABLE_STATUS_IN =
            "('100', '200', '205', '300')";

    @PersistenceContext
    private EntityManager em;

    // ---------------------------------------------------------------------------
    // Inspection list (Screen 010 equivalent)
    // ---------------------------------------------------------------------------

    public List<ArrivalInspectionListDto> searchInspectable(
            String companyCode, String warehouseCode, String customerCode,
            String arrivalNo, String status,
            LocalDate dateFrom, LocalDate dateTo) {

        StringBuilder sql = new StringBuilder();
        sql.append(
            "SELECT h.AVH_AV_NUM, h.AVH_AV_STS, NVL(h.AVH_SPL_NAM1, h.AVH_SPL_COD), h.AVH_SPL_COD, " +
            "h.AVH_PO_NUM, h.AVH_TRN_KND, h.AVH_SCDL_YMD, h.AVH_ARV_YMD, h.AVH_RMKS, h.UPD_YMDHMS, " +
            "(SELECT COUNT(*) FROM GWH.GWH_TJ_AV_D d " +
            "  WHERE d.AVD_CPNY_COD = h.AVH_CPNY_COD AND d.AVD_WHS_COD = h.AVH_WHS_COD " +
            "  AND d.AVD_CUST_COD = h.AVH_CUST_COD AND d.AVD_AV_NUM = h.AVH_AV_NUM AND d.DEL_FLG = '0') AS TOTAL_LINES, " +
            "(SELECT COUNT(*) FROM GWH.GWH_TJ_AV_R r " +
            "  WHERE r.AVR_CPNY_COD = h.AVH_CPNY_COD AND r.AVR_WHS_COD = h.AVH_WHS_COD " +
            "  AND r.AVR_CUST_COD = h.AVH_CUST_COD AND r.AVR_AS_NUM = h.AVH_AV_NUM AND r.DEL_FLG = '0') AS INSP_LINES " +
            "FROM GWH.GWH_TJ_AV_H h " +
            "WHERE h.AVH_CPNY_COD = :cpny AND h.AVH_WHS_COD = :whs AND h.AVH_CUST_COD = :cust " +
            "  AND h.DEL_FLG = '0' AND h.AVH_AV_STS IN " + INSPECTABLE_STATUS_IN
        );

        if (arrivalNo != null && !arrivalNo.trim().isEmpty()) {
            sql.append(" AND UPPER(h.AVH_AV_NUM) LIKE :avNum");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND h.AVH_AV_STS = :status");
        }
        if (dateFrom != null) {
            sql.append(" AND TRUNC(h.AVH_SCDL_YMD) >= :dateFrom");
        }
        if (dateTo != null) {
            sql.append(" AND TRUNC(h.AVH_SCDL_YMD) <= :dateTo");
        }
        sql.append(" ORDER BY h.AVH_SCDL_YMD DESC, h.AVH_AV_NUM DESC");

        Query q = em.createNativeQuery(sql.toString());
        q.setParameter("cpny", companyCode);
        q.setParameter("whs", warehouseCode);
        q.setParameter("cust", customerCode);
        if (arrivalNo != null && !arrivalNo.trim().isEmpty()) {
            q.setParameter("avNum", "%" + arrivalNo.trim().toUpperCase() + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            q.setParameter("status", status.trim());
        }
        if (dateFrom != null) q.setParameter("dateFrom", Date.valueOf(dateFrom));
        if (dateTo != null)   q.setParameter("dateTo",   Date.valueOf(dateTo));
        q.setMaxResults(200);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        List<ArrivalInspectionListDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            ArrivalInspectionListDto dto = new ArrivalInspectionListDto();
            dto.setArrivalNumber(str(row[0]));
            dto.setArrivalStatus(str(row[1]));
            dto.setSupplierName(str(row[2]));
            dto.setSupplierCode(str(row[3]));
            dto.setPoNumber(str(row[4]));
            dto.setTransactionKind(str(row[5]));
            dto.setScheduledDate(toDate(row[6]));
            dto.setArrivalDate(toDate(row[7]));
            dto.setRemarks(str(row[8]));
            dto.setUpdTimestamp(str(row[9]));
            dto.setTotalLines(toInt(row[10]));
            dto.setInspectedLines(toInt(row[11]));
            result.add(dto);
        }
        return result;
    }

    // ---------------------------------------------------------------------------
    // Detail lines for a single arrival (Screen 020 equivalent)
    // ---------------------------------------------------------------------------

    public List<InspectionLineDto> getInspectionLines(
            String companyCode, String warehouseCode, String customerCode, String arrivalNumber) {

        String sql =
            "SELECT d.AVD_AVLN_NUM, d.AVD_AV_STS, d.AVD_PROD_COD, d.AVD_PROD_NAM, " +
            "  d.AVD_SBIV_COD, NULL AS PO_NUM, NULL AS LOT_NUM, " +
            "  d.AVD_SCS_QTY, d.AVD_SPC_QTY, d.AVD_SBL_QTY, " +
            "  d.AVD_PPCS_QTY, d.AVD_PPB_QTY, " +
            "  NULL AS AVD_AREA_COD, NULL AS AVD_RACK_COD, NULL AS AVD_LVL_COD, NULL AS AVD_PSTN_COD, " +
            "  d.AVD_CGID_NUM, d.AVD_WGT, d.AVD_M3, d.UPD_YMDHMS, " +
            "  r.AVR_RCS_QTY, r.AVR_RPC_QTY, r.AVR_RBL_QTY, r.AVR_RTPC_QTY, " +
            "  r.AVR_AREA_COD, r.AVR_RACK_COD, r.AVR_LVL_COD, r.AVR_PSTN_COD, r.AVR_COI1, " +
            "  d.AVD_ORGN_COD, h.AVH_TRN_KND, " +
            "  d.AVD_PIK1, d.AVD_PIK2, d.AVD_PIK3, d.AVD_PIK4, d.AVD_PIK5, d.AVD_PIK6, d.AVD_PIK7, " +
            "  d.AVD_DMG_FLG " +
            "FROM GWH.GWH_TJ_AV_D d " +
            "INNER JOIN GWH.GWH_TJ_AV_H h " +
            "  ON h.AVH_CPNY_COD = d.AVD_CPNY_COD AND h.AVH_WHS_COD = d.AVD_WHS_COD " +
            "  AND h.AVH_CUST_COD = d.AVD_CUST_COD AND h.AVH_AV_NUM = d.AVD_AV_NUM AND h.DEL_FLG = '0' " +
            "LEFT JOIN GWH.GWH_TJ_AV_R r " +
            "  ON r.AVR_CPNY_COD = d.AVD_CPNY_COD AND r.AVR_WHS_COD = d.AVD_WHS_COD " +
            "  AND r.AVR_CUST_COD = d.AVD_CUST_COD AND r.AVR_AS_NUM = d.AVD_AV_NUM " +
            "  AND r.AVR_ASLN_NUM = d.AVD_AVLN_NUM AND r.AVR_ASSQ_NUM = 1 AND r.DEL_FLG = '0' " +
            "WHERE d.AVD_CPNY_COD = :cpny AND d.AVD_WHS_COD = :whs AND d.AVD_CUST_COD = :cust " +
            "  AND d.AVD_AV_NUM = :avNum AND d.DEL_FLG = '0' " +
            "ORDER BY d.AVD_AVLN_NUM";

        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", companyCode);
        q.setParameter("whs", warehouseCode);
        q.setParameter("cust", customerCode);
        q.setParameter("avNum", arrivalNumber);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        List<InspectionLineDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            InspectionLineDto dto = new InspectionLineDto();
            dto.setLineNumber(toInt(row[0]));
            dto.setLineStatus(str(row[1]));
            dto.setProductCode(str(row[2]));
            dto.setProductName(str(row[3]));
            dto.setSubInventoryCode(str(row[4]));
            dto.setPoNumber(str(row[5]));
            dto.setLotNumber(str(row[6]));
            dto.setPlanCsQty(toInt(row[7]));
            dto.setPlanPcQty(toInt(row[8]));
            dto.setPlanBlQty(toInt(row[9]));
            dto.setPiecesPerCase(toInt(row[10]));
            dto.setPiecesPerBulk(toInt(row[11]));
            // Recommended location from AVD
            dto.setAreaCode(str(row[12]));
            dto.setRackCode(str(row[13]));
            dto.setLevelCode(str(row[14]));
            dto.setPositionCode(str(row[15]));
            int cgid = toInt(row[16]);
            dto.setMultiCargo(cgid > 1);
            dto.setLineWeight(toBigDecimal(row[17]));
            dto.setLineVolume(toBigDecimal(row[18]));
            dto.setUpdTimestamp(str(row[19]));
            // Existing inspection result (if any)
            if (row[20] != null) {
                dto.setResultCsQty(toInt(row[20]));
                dto.setResultPcQty(toInt(row[21]));
                dto.setResultBlQty(toInt(row[22]));
                dto.setResultTotalQty(toInt(row[23]));
                // Override location with the inspected location
                if (row[24] != null) dto.setAreaCode(str(row[24]));
                if (row[25] != null) dto.setRackCode(str(row[25]));
                if (row[26] != null) dto.setLevelCode(str(row[26]));
                if (row[27] != null) dto.setPositionCode(str(row[27]));
                dto.setLpnNumber(str(row[28]));
            }
            // Legacy columns for AVR insert (indices 29-38)
            dto.setOriginCode(str(row[29]));
            dto.setTrnKnd(str(row[30]));
            dto.setPik1(str(row[31]));
            dto.setPik2(str(row[32]));
            dto.setPik3(str(row[33]));
            dto.setPik4(str(row[34]));
            dto.setPik5(str(row[35]));
            dto.setPik6(str(row[36]));
            dto.setPik7(str(row[37]));
            dto.setDmgFlg(str(row[38]));
            // Compute plan total pieces
            int ppc = dto.getPiecesPerCase() != null ? dto.getPiecesPerCase() : 1;
            int ppb = dto.getPiecesPerBulk() != null ? dto.getPiecesPerBulk() : 1;
            int planTotal = (ppc * nvl(dto.getPlanCsQty())) + nvl(dto.getPlanPcQty()) + (ppb * nvl(dto.getPlanBlQty()));
            dto.setPlanTotalQty(planTotal);
            result.add(dto);
        }
        return result;
    }

    // ---------------------------------------------------------------------------
    // Write operations โ€” performed inside service @Transactional
    // ---------------------------------------------------------------------------

    /**
     * Check if an AVR record already exists for a given line (seq=1).
     * Used by the scan-item flow to decide INSERT vs UPDATE.
     */
    public boolean avrExists(String cpny, String whs, String cust, String avNum, int lineNum) {
        String sql =
            "SELECT COUNT(*) FROM GWH.GWH_TJ_AV_R " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND AVR_ASSQ_NUM = 1 AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("lineNum", lineNum);
        @SuppressWarnings("unchecked")
        List<Object> avrRows = q.getResultList();
        return !avrRows.isEmpty() && toBigDecimal(avrRows.get(0)).intValue() > 0;
    }

    /**
     * Get location from existing AVR record.
     * Returns [area, rack, pstn, lvl] or null if not found.
     */
    public String[] getAvrLocation(String cpny, String whs, String cust, String avNum, int lineNum) {
        String sql =
            "SELECT AVR_AREA_COD, AVR_RACK_COD, AVR_PSTN_COD, AVR_LVL_COD " +
            "FROM GWH.GWH_TJ_AV_R " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND AVR_ASSQ_NUM = 1 AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("lineNum", lineNum);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        if (!rows.isEmpty() && rows.get(0) != null) {
            Object[] row = rows.get(0);
            return new String[]{str(row[0]), str(row[1]), str(row[2]), str(row[3])};
        }
        return null;
    }

    /**
     * Get arrival number references from existing AVR record.
     * Returns [AVR_AS_NUM, AVR_ASLN_NUM, AVR_ASSQ_NUM] or null if not found.
     */
    public Object[] getAvrArrivalNumbers(String cpny, String whs, String cust, String avNum, int lineNum) {
        String sql =
            "SELECT AVR_AS_NUM, AVR_ASLN_NUM, AVR_ASSQ_NUM " +
            "FROM GWH.GWH_TJ_AV_R " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND DEL_FLG = '0'" +
            "  AND ROWNUM = 1";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("lineNum", lineNum);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        if (!rows.isEmpty() && rows.get(0) != null) {
            return rows.get(0);
        }
        return null;
    }

    /**
     * Accumulate quantity onto an existing AVR record (scan-item real-time flow).
     * Optionally assigns/updates the LPN.
     */
    public void accumulateArrivalResult(
            String cpny, String whs, String cust, String avNum, int lineNum,
            int addQty, String lpnNumber, String userId) {

        // When lpnNumber is null, omit the AVR_COI1 assignment entirely.
        // Passing null via Hibernate native query causes ORA-00932 (VARBINARY vs NCHAR)
        // because Hibernate cannot infer the column type for a null parameter.
        String lpnClause = (lpnNumber != null) ? "AVR_COI1 = :lpn, " : "";
        String sql =
            "UPDATE GWH.GWH_TJ_AV_R " +
            "SET AVR_RTPC_QTY = NVL(AVR_RTPC_QTY, 0) + :addQty, " +
            "    AVR_RPC_QTY  = NVL(AVR_RPC_QTY, 0)  + :addQty, " +
            "    " + lpnClause +
            "    UPD_YMD = TO_CHAR(SYSDATE,'YYYYMMDD'), UPD_TIM = TO_CHAR(SYSDATE,'HH24MISS'), " +
            "    UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND AVR_ASSQ_NUM = 1 AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("lineNum", lineNum);
        q.setParameter("addQty", addQty);
        if (lpnNumber != null) q.setParameter("lpn", lpnNumber);
        q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /**
     * Get running received total for a single AVD line (seq=1).
     */
    public int getLineReceivedQty(String cpny, String whs, String cust, String avNum, int lineNum) {
        String sql =
            "SELECT NVL(SUM(AVR_RTPC_QTY), 0) FROM GWH.GWH_TJ_AV_R " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("lineNum", lineNum);
        @SuppressWarnings("unchecked")
        List<Object> lineRows = q.getResultList();
        return !lineRows.isEmpty() ? toBigDecimal(lineRows.get(0)).intValue() : 0;
    }

    /**
     * Sum all AVR result quantities for the arrival (for header total).
     */
    public int getTotalReceivedQty(String cpny, String whs, String cust, String avNum) {
        String sql =
            "SELECT NVL(SUM(AVR_RTPC_QTY), 0) FROM GWH.GWH_TJ_AV_R " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum);
        @SuppressWarnings("unchecked")
        List<Object> totalRows = q.getResultList();
        return !totalRows.isEmpty() ? toBigDecimal(totalRows.get(0)).intValue() : 0;
    }

    /** Delete existing AVR records for a line (used before re-inserting on NEW mode) */
    public int deleteArrivalResults(String cpny, String whs, String cust, String avNum, int lineNum) {
        String sql = "DELETE FROM GWH.GWH_TJ_AV_R " +
                     "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
                     "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", avNum);
        q.setParameter("lineNum", lineNum);
        return q.executeUpdate();
    }

    /**
     * Insert one AVR record โ€” matches legacy GwhAbins010DAO.insertGwhTjAvR().
     * All columns aligned with DATABASE_COMPREHENSIVE.md GWH_TJ_AV_R (123 cols).
     */
    public void insertArrivalResult(
            String cpny, String whs, String cust, String avNum, int lineNum, int seq,
            String productCode, String originCode, String trnKnd, String avspSts,
            Integer ppcsQty, Integer ppbQty, Integer csSqty, Integer pcQty, Integer blQty, Integer totalQty,
            String sbivCode,
            String pik1, String pik2, String pik3, String pik4, String pik5, String pik6, String pik7,
            String areaCode, String rackCode, String levelCode, String positionCode,
            String dmgFlg, String opFlg,
            Double weightKg, Double volumeM3,
            String lpnNumber, String userId) {

        String sql =
            "INSERT INTO GWH.GWH_TJ_AV_R " +
            "(AVR_CPNY_COD, AVR_WHS_COD, AVR_CUST_COD, AVR_AS_NUM, AVR_ASLN_NUM, AVR_ASSQ_NUM, " +
            " AVR_AS_KND, AVR_TRN_KND, AVR_CRT_YMD, AVR_AVSP_YMD, AVR_AVSP_STS, " +
            " AVR_PROD_COD, AVR_ORGN_COD, AVR_SBIV_COD, " +
            " AVR_PPCS_QTY, AVR_PPB_QTY, AVR_RCS_QTY, AVR_RBL_QTY, AVR_RPC_QTY, AVR_RTPC_QTY, " +
            " AVR_PIK1, AVR_PIK2, AVR_PIK3, AVR_PIK4, AVR_PIK5, AVR_PIK6, AVR_PIK7, " +
            " AVR_AV_NUM, AVR_AVLN_NUM, AVR_AVSQ_NUM, " +
            " AVR_AREA_COD, AVR_RACK_COD, AVR_LVL_COD, AVR_PSTN_COD, AVR_COI1, " +
            " AVR_RWGT, AVR_RM3, " +
            " AVR_DMG_FLG, AVR_OP_FLG, AVR_CNFM_FLG, DEL_FLG, " +
            " CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
            " UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS) " +
            "VALUES (:cpny, :whs, :cust, :avNum, :lineNum, :seq, " +
            " 'AV', :trnKnd, TRUNC(SYSDATE), TRUNC(SYSDATE), :avspSts," +
            " :prd, :orgn, :sbiv, " +
            " :ppcs, :ppb, :cs, NVL(:bl, 0), :pc, :total, " +
            " :pik1, :pik2, :pik3, :pik4, :pik5, :pik6, :pik7, " +
            " :avNum, :lineNum, :seq, " +
            " :area, :rack, :lvl, :pstn, :lpn, " +
            " :rwgt, :rm3, " +
            " :dmg, :opFlg, 'N', '0', " +
            " TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-MODERN', :userId, 'RECEIVE', 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
            " TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-MODERN', :userId, 'RECEIVE', 'UTC', SYSTIMESTAMP, SYSTIMESTAMP)";

        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", avNum);
        q.setParameter("lineNum", lineNum);
        q.setParameter("seq", seq);
        q.setParameter("trnKnd", trnKnd != null ? trnKnd : "001");
        q.setParameter("avspSts", avspSts != null ? avspSts : "300");
        q.setParameter("prd", productCode);
        q.setParameter("orgn", originCode);
        q.setParameter("sbiv", sbivCode != null ? sbivCode : "-");
        q.setParameter("ppcs", ppcsQty != null ? ppcsQty : 1);
        q.setParameter("ppb", ppbQty != null ? ppbQty : 1);
        q.setParameter("cs", csSqty);
        q.setParameter("pc", pcQty);
        q.setParameter("bl", blQty);
        q.setParameter("total", totalQty);
        q.setParameter("pik1", pik1);
        q.setParameter("pik2", pik2);
        q.setParameter("pik3", pik3);
        q.setParameter("pik4", pik4);
        q.setParameter("pik5", pik5);
        q.setParameter("pik6", pik6);
        q.setParameter("pik7", pik7);
        q.setParameter("area", areaCode);
        q.setParameter("rack", rackCode);
        q.setParameter("lvl", levelCode);
        q.setParameter("pstn", positionCode);
        q.setParameter("lpn", lpnNumber);
        q.setParameter("rwgt", weightKg != null ? weightKg : 0.0);
        q.setParameter("rm3", volumeM3 != null ? volumeM3 : 0.0);
        q.setParameter("dmg", dmgFlg != null ? dmgFlg : "N");
        q.setParameter("opFlg", opFlg != null ? opFlg : "N");
        q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /** Update AVD line status + set AVD_INSP_STS='Y' (matches legacy GwhApins010DAO.updateAvd) */
    public void updateDetailStatus(String cpny, String whs, String cust, String avNum, int lineNum,
                                   String newStatus, String userId) {
        String sql =
            "UPDATE GWH.GWH_TJ_AV_D " +
            "SET AVD_AV_STS = :sts, AVD_INSP_STS = 'Y', " +
            "UPD_YMD = TO_CHAR(SYSDATE,'YYYYMMDD'), UPD_TIM = TO_CHAR(SYSDATE,'HH24MISS'), UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "  AND AVD_AV_NUM = :avNum AND AVD_AVLN_NUM = :lineNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("lineNum", lineNum);
        q.setParameter("sts", newStatus); q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /** Update AVR result record status for a single line (seq=1). */
    public void updateResultStatus(String cpny, String whs, String cust, String avNum, int lineNum,
                                   String newStatus, String userId) {
        String sql =
            "UPDATE GWH.GWH_TJ_AV_R " +
            "SET AVR_AVSP_STS = :sts, UPD_YMD = TO_CHAR(SYSDATE,'YYYYMMDD'), UPD_TIM = TO_CHAR(SYSDATE,'HH24MISS'), UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND AVR_ASSQ_NUM = 1 AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", avNum);
        q.setParameter("lineNum", lineNum);
        q.setParameter("sts", newStatus);
        q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /** Returns true if every active AVD line for the arrival has the specified status. */
    public boolean allDetailLinesHaveStatus(String cpny, String whs, String cust,
                                            String avNum, String status) {
        String sql =
            "SELECT COUNT(*) FROM GWH.GWH_TJ_AV_D " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "  AND AVD_AV_NUM = :avNum AND DEL_FLG = '0' AND AVD_AV_STS <> :sts";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", avNum);
        q.setParameter("sts", status);
        @SuppressWarnings("unchecked")
        List<Object> statusRows = q.getResultList();
        int count1 = !statusRows.isEmpty() ? toBigDecimal(statusRows.get(0)).intValue() : 0;
        return count1 == 0;
    }

    /**
     * Returns true if every active AVD line for the arrival has status 209 (Inspected)
     * OR 205 (Accepted discrepancy) โ€” i.e., all lines are "done" regardless of discrepancy.
     */
    public boolean allDetailLinesComplete(String cpny, String whs, String cust, String avNum) {
        String sql =
            "SELECT COUNT(*) FROM GWH.GWH_TJ_AV_D " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "  AND AVD_AV_NUM = :avNum AND DEL_FLG = '0' " +
            "  AND AVD_AV_STS NOT IN ('209', '205')";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("avNum", avNum);
        @SuppressWarnings("unchecked")
        List<Object> completeRows = q.getResultList();
        int count2 = !completeRows.isEmpty() ? toBigDecimal(completeRows.get(0)).intValue() : 0;
        return count2 == 0;
    }

    /** Update AVH header status */
    public void updateHeaderStatus(String cpny, String whs, String cust, String avNum,
                                   String newStatus, String userId) {
        String sql =
            "UPDATE GWH.GWH_TJ_AV_H " +
            "SET AVH_AV_STS = :sts, UPD_YMD = TO_CHAR(SYSDATE,'YYYYMMDD'), UPD_TIM = TO_CHAR(SYSDATE,'HH24MISS'), UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVH_CPNY_COD = :cpny AND AVH_WHS_COD = :whs AND AVH_CUST_COD = :cust " +
            "  AND AVH_AV_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("sts", newStatus); q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /** Cancel: logical delete of all AVR records for an arrival */
    public void cancelArrivalResults(String cpny, String whs, String cust, String avNum, String userId) {
        String sql =
            "UPDATE GWH.GWH_TJ_AV_R " +
            "SET DEL_FLG = '1', UPD_YMD = TO_CHAR(SYSDATE,'YYYYMMDD'), UPD_TIM = TO_CHAR(SYSDATE,'HH24MISS'), UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "  AND AVR_AS_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /** Reset all AVD lines for the arrival back to pre-inspection status */
    public void cancelDetailStatuses(String cpny, String whs, String cust, String avNum,
                                     String revertStatus, String userId) {
        String sql =
            "UPDATE GWH.GWH_TJ_AV_D " +
            "SET AVD_AV_STS = :sts, UPD_YMD = TO_CHAR(SYSDATE,'YYYYMMDD'), UPD_TIM = TO_CHAR(SYSDATE,'HH24MISS'), UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
            "WHERE AVD_CPNY_COD = :cpny AND AVD_WHS_COD = :whs AND AVD_CUST_COD = :cust " +
            "  AND AVD_AV_NUM = :avNum AND DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
        q.setParameter("avNum", avNum); q.setParameter("sts", revertStatus); q.setParameter("userId", userId);
        q.executeUpdate();
    }

    /** Create or update an LPN header + detail record linked to this arrival */
    public void upsertLpn(String cpny, String whs, String cust,
                          String lpnNumber, String lpnType, String avNum,
                          int lineNum, int seqNum,
                          String productCode, String lotNumber, int qty,
                          String subInvCode, String userId) {
        // Skip LPN insert if arrival line/seq numbers are invalid (0)
        if (lineNum == 0 || seqNum == 0) {
            log.warn("[upsertLpn] Skipped โ€” lineNum={} seqNum={} are invalid for lpn={}", lineNum, seqNum, lpnNumber);
            return;
        }
        // --- LPN Header upsert ---
        String checkSql =
            "SELECT COUNT(*) FROM GWH.GWH_TJ_LPN " +
            "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
            "  AND LPN_NUM = :lpn AND DEL_FLG = '0'";
        Query checkQ = em.createNativeQuery(checkSql);
        checkQ.setParameter("cpny", cpny); checkQ.setParameter("whs", whs);
        checkQ.setParameter("cust", cust); checkQ.setParameter("lpn", lpnNumber);
        @SuppressWarnings("unchecked")
        List<Object> lpnRows = checkQ.getResultList();
        long count = !lpnRows.isEmpty() ? toBigDecimal(lpnRows.get(0)).longValue() : 0;

        if (count == 0) {
            String insertSql =
                "INSERT INTO GWH.GWH_TJ_LPN " +
                "(LPN_CPNY_COD, LPN_WHS_COD, LPN_CUST_COD, LPN_NUM, LPN_TYPE, LPN_STS, " +
                " LPN_AV_NUM, LPN_TTL_QTY, LPN_RCV_YMD, DEL_FLG, CRT_USER, CRT_YMDHMS, UPD_USER, UPD_YMDHMS) " +
                "VALUES (:cpny, :whs, :cust, :lpn, :type, '200', " +
                " :avNum, 0, TO_CHAR(SYSDATE,'YYYYMMDD'), '0', :userId, SYSTIMESTAMP, :userId, SYSTIMESTAMP)";
            Query iQ = em.createNativeQuery(insertSql);
            iQ.setParameter("cpny", cpny); iQ.setParameter("whs", whs); iQ.setParameter("cust", cust);
            iQ.setParameter("lpn", lpnNumber); iQ.setParameter("type", lpnType);
            iQ.setParameter("avNum", avNum);
            iQ.setParameter("userId", userId);
            iQ.executeUpdate();
        } else {
            String updateSql =
                "UPDATE GWH.GWH_TJ_LPN " +
                "SET LPN_STS = '200', LPN_AV_NUM = :avNum, UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP " +
                "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
                "  AND LPN_NUM = :lpn AND DEL_FLG = '0'";
            Query uQ = em.createNativeQuery(updateSql);
            uQ.setParameter("cpny", cpny); uQ.setParameter("whs", whs); uQ.setParameter("cust", cust);
            uQ.setParameter("lpn", lpnNumber); uQ.setParameter("avNum", avNum);
            uQ.setParameter("userId", userId);
            uQ.executeUpdate();
        }

        // --- LPN Detail upsert (AV_NUM, AVLN_NUM, AVSQ_NUM from AVR) ---
        String checkDSql =
            "SELECT COUNT(*) FROM GWH.GWH_TJ_LPN_D " +
            "WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
            "  AND LPND_LPN_NUM = :lpn AND LPND_AV_NUM = :avNum AND LPND_AVLN_NUM = :avLn " +
            "  AND LPND_AVSQ_NUM = :avSq AND DEL_FLG = '0'";
        Query checkDQ = em.createNativeQuery(checkDSql);
        checkDQ.setParameter("cpny", cpny); checkDQ.setParameter("whs", whs);
        checkDQ.setParameter("cust", cust); checkDQ.setParameter("lpn", lpnNumber);
        checkDQ.setParameter("avNum", avNum); checkDQ.setParameter("avLn", lineNum);
        checkDQ.setParameter("avSq", seqNum);
        @SuppressWarnings("unchecked")
        List<Object> detailRows = checkDQ.getResultList();
        long detailCount = !detailRows.isEmpty() ? toBigDecimal(detailRows.get(0)).longValue() : 0;

        if (detailCount == 0) {
            // Determine next line number within this LPN
            String maxLnSql =
                "SELECT NVL(MAX(LPND_LN_NUM), 0) + 1 FROM GWH.GWH_TJ_LPN_D " +
                "WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
                "  AND LPND_LPN_NUM = :lpn AND DEL_FLG = '0'";
            Query maxLnQ = em.createNativeQuery(maxLnSql);
            maxLnQ.setParameter("cpny", cpny); maxLnQ.setParameter("whs", whs);
            maxLnQ.setParameter("cust", cust); maxLnQ.setParameter("lpn", lpnNumber);
            int nextLn = toBigDecimal(maxLnQ.getSingleResult()).intValue();

            String insertDSql =
                "INSERT INTO GWH.GWH_TJ_LPN_D " +
                "(LPND_CPNY_COD, LPND_WHS_COD, LPND_CUST_COD, LPND_LPN_NUM, LPND_LN_NUM, " +
                " LPND_PROD_COD, LPND_LOT_NUM, LPND_QTY, LPND_ALC_QTY, LPND_AVL_QTY, " +
                " LPND_SBIV_COD, LPND_AV_NUM, LPND_AVLN_NUM, LPND_AVSQ_NUM, " +
                " DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                " UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS) " +
                "VALUES (:cpny, :whs, :cust, :lpn, :ln, " +
                " :prod, :lot, :qty, 0, :qty, " +
                " :sbiv, :avNum, :avLn, :avSq, " +
                " '0', TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-WMS', :userId, 'RECEIVE', '+0900', SYSTIMESTAMP, SYSTIMESTAMP, " +
                " TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-WMS', :userId, 'RECEIVE', '+0900', SYSTIMESTAMP, SYSTIMESTAMP)";
            Query iDQ = em.createNativeQuery(insertDSql);
            iDQ.setParameter("cpny", cpny); iDQ.setParameter("whs", whs); iDQ.setParameter("cust", cust);
            iDQ.setParameter("lpn", lpnNumber); iDQ.setParameter("ln", nextLn);
            iDQ.setParameter("prod", productCode); iDQ.setParameter("lot", lotNumber);
            iDQ.setParameter("qty", qty);
            iDQ.setParameter("sbiv", subInvCode != null ? subInvCode : "-");
            iDQ.setParameter("avNum", avNum); iDQ.setParameter("avLn", lineNum); iDQ.setParameter("avSq", seqNum);
            iDQ.setParameter("userId", userId);
            iDQ.executeUpdate();
        } else {
            String updateDSql =
                "UPDATE GWH.GWH_TJ_LPN_D " +
                "SET LPND_QTY = LPND_QTY + :qty, LPND_AVL_QTY = LPND_AVL_QTY + :qty, " +
                "    UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP " +
                "WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
                "  AND LPND_LPN_NUM = :lpn AND LPND_AV_NUM = :avNum AND LPND_AVLN_NUM = :avLn " +
                "  AND LPND_AVSQ_NUM = :avSq AND DEL_FLG = '0'";
            Query uDQ = em.createNativeQuery(updateDSql);
            uDQ.setParameter("cpny", cpny); uDQ.setParameter("whs", whs); uDQ.setParameter("cust", cust);
            uDQ.setParameter("lpn", lpnNumber); uDQ.setParameter("avNum", avNum);
            uDQ.setParameter("avLn", lineNum); uDQ.setParameter("avSq", seqNum);
            uDQ.setParameter("qty", qty); uDQ.setParameter("userId", userId);
            uDQ.executeUpdate();
        }

        // --- Update LPN_TTL_QTY = COUNT of detail lines, LPN_LOC_COD = Area-Rack-Position-Level ---
        String cntSql =
            "UPDATE GWH.GWH_TJ_LPN " +
            "SET LPN_TTL_QTY = (SELECT NVL(SUM(LPND_QTY), 0) FROM GWH.GWH_TJ_LPN_D " +
            "                   WHERE LPND_CPNY_COD = :cpny AND LPND_WHS_COD = :whs AND LPND_CUST_COD = :cust " +
            "                     AND LPND_LPN_NUM = :lpn AND DEL_FLG = '0'), " +
            "    LPN_LOC_COD = (SELECT TRIM(AVR_AREA_COD) || '-' || AVR_RACK_COD || '-' || AVR_PSTN_COD || '-' || AVR_LVL_COD " +
            "                   FROM GWH.GWH_TJ_AV_R " +
            "                   WHERE AVR_CPNY_COD = :cpny AND AVR_WHS_COD = :whs AND AVR_CUST_COD = :cust " +
            "                     AND AVR_AS_NUM = :avNum AND AVR_ASLN_NUM = :lineNum AND DEL_FLG = '0' " +
            "                     AND AVR_AREA_COD IS NOT NULL AND ROWNUM = 1), " +
            "    UPD_USER = :userId, UPD_YMDHMS = SYSTIMESTAMP " +
            "WHERE LPN_CPNY_COD = :cpny AND LPN_WHS_COD = :whs AND LPN_CUST_COD = :cust " +
            "  AND LPN_NUM = :lpn AND DEL_FLG = '0'";
        Query cntQ = em.createNativeQuery(cntSql);
        cntQ.setParameter("cpny", cpny); cntQ.setParameter("whs", whs); cntQ.setParameter("cust", cust);
        cntQ.setParameter("lpn", lpnNumber); cntQ.setParameter("userId", userId);
        cntQ.setParameter("avNum", avNum); cntQ.setParameter("lineNum", lineNum);
        cntQ.executeUpdate();
    }

    // ---------------------------------------------------------------------------
    // Transaction permit flags
    // ---------------------------------------------------------------------------

    public static class TransactionPermitFlags {
        private final boolean surplusAllowed;
        private final boolean shortageAllowed;

        public TransactionPermitFlags(boolean surplusAllowed, boolean shortageAllowed) {
            this.surplusAllowed = surplusAllowed;
            this.shortageAllowed = shortageAllowed;
        }

        public boolean isSurplusAllowed()  { return surplusAllowed; }
        public boolean isShortageAllowed() { return shortageAllowed; }
    }

    public TransactionPermitFlags getTransactionPermitFlags(
            String cpny, String whs, String cust, String avNum) {
        // Legacy pattern: GWH_TM_TRN uses wildcard '*' partition with priority
        String sql =
            "SELECT t.TRN_SPPA_FLG, t.TRN_SHPA_FLG " +
            "FROM GWH.GWH_TJ_AV_H h " +
            "LEFT JOIN (" +
            "  SELECT TRN_SPPA_FLG, TRN_SHPA_FLG, TRN_KND, TRN_CPNY_COD, TRN_WHS_COD, TRN_CUST_COD," +
            "    ROW_NUMBER() OVER (PARTITION BY TRN_KND" +
            "      ORDER BY LENGTH(TRN_CPNY_COD || TRN_WHS_COD || TRN_CUST_COD) DESC) PRIORITY_SEQ" +
            "  FROM GWH.GWH_TM_TRN" +
            "  WHERE (TRN_CPNY_COD = :cpny OR TRN_CPNY_COD = '*')" +
            "    AND (TRN_WHS_COD = :whs OR TRN_WHS_COD = '*')" +
            "    AND (TRN_CUST_COD = :cust OR TRN_CUST_COD = '*')" +
            "    AND DEL_FLG = '0'" +
            ") t ON t.TRN_KND = h.AVH_TRN_KND AND t.PRIORITY_SEQ = 1 " +
            "WHERE h.AVH_CPNY_COD = :cpny AND h.AVH_WHS_COD = :whs " +
            "  AND h.AVH_CUST_COD = :cust AND h.AVH_AV_NUM = :avNum " +
            "  AND h.DEL_FLG = '0'";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);
            q.setParameter("avNum", avNum);
            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            if (!rows.isEmpty()) {
                Object[] row = rows.get(0);
                boolean surplus  = "Y".equals(row[0] != null ? row[0].toString().trim() : "");
                boolean shortage = "Y".equals(row[1] != null ? row[1].toString().trim() : "");
                return new TransactionPermitFlags(surplus, shortage);
            }
            return new TransactionPermitFlags(false, false);
        } catch (Exception e) {
            log.warn("[getTransactionPermitFlags] Could not load flags for avNum={}: {}", avNum, e.getMessage());
            return new TransactionPermitFlags(false, false);
        }
    }

    // ---------------------------------------------------------------------------
    // Validation checks
    // ---------------------------------------------------------------------------

    /** CHECK #12: Validate location exists in location master */
    public boolean locationExists(String cpny, String whs, String areaCod, String rackCod, String lvlCod, String pstnCod) {
        String sql =
            "SELECT COUNT(*) FROM GWH.GWH_TM_LOC " +
            "WHERE LOC_CPNY_COD = :cpny AND LOC_WHS_COD = :whs " +
            "  AND TRIM(LOC_AREA_COD) = TRIM(:area) AND LOC_RACK_COD = :rack " +
            "  AND LOC_LVL_COD = :lvl AND LOC_PSTN_COD = :pstn AND DEL_FLG = '0'";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny); q.setParameter("whs", whs);
            q.setParameter("area", areaCod); q.setParameter("rack", rackCod);
            q.setParameter("lvl", lvlCod); q.setParameter("pstn", pstnCod);
            @SuppressWarnings("unchecked")
            List<Object> rows = q.getResultList();
            return !rows.isEmpty() && ((Number) rows.get(0)).intValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /** CHECK #7: Check if different SKU (different product) exists at the target location */
    public boolean hasDifferentProductAtLocation(String cpny, String whs, String cust,
            String areaCod, String rackCod, String lvlCod, String pstnCod, String productCode) {
        String sql =
            "SELECT COUNT(*) FROM GWH.GWH_TJ_ST " +
            "WHERE ST_CPNY_COD = :cpny AND ST_WHS_COD = :whs AND ST_CUST_COD = :cust " +
            "  AND TRIM(ST_AREA_COD) = TRIM(:area) AND ST_RACK_COD = :rack " +
            "  AND ST_LVL_COD = :lvl AND ST_PSTN_COD = :pstn " +
            "  AND ST_PROD_COD <> :prod AND DEL_FLG = '0' AND ST_PYST_QTY > 0";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
            q.setParameter("area", areaCod); q.setParameter("rack", rackCod);
            q.setParameter("lvl", lvlCod); q.setParameter("pstn", pstnCod);
            q.setParameter("prod", productCode);
            @SuppressWarnings("unchecked")
            List<Object> rows = q.getResultList();
            return !rows.isEmpty() && ((Number) rows.get(0)).intValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /** CHECK #8: Get product group temperature control code */
    public String getProductTempControl(String cpny, String whs, String cust, String productCode) {
        String sql =
            "SELECT g.PRDG_TEMP_CTR FROM GWH.GWH_TM_PROD p " +
            "JOIN GWH.GWH_TM_PROD_G g ON g.PRDG_CPNY_COD = p.PROD_CPNY_COD " +
            "  AND g.PRDG_WHS_COD = p.PROD_WHS_COD AND g.PRDG_CUST_COD = p.PROD_CUST_COD " +
            "  AND g.PRDG_COD = p.PROD_PRDG_COD AND g.DEL_FLG = '0' " +
            "WHERE p.PROD_CPNY_COD = :cpny AND p.PROD_WHS_COD = :whs " +
            "  AND p.PROD_CUST_COD = :cust AND p.PROD_COD = :prod AND p.DEL_FLG = '0'";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny); q.setParameter("whs", whs);
            q.setParameter("cust", cust); q.setParameter("prod", productCode);
            @SuppressWarnings("unchecked")
            List<Object> rows = q.getResultList();
            if (!rows.isEmpty() && rows.get(0) != null) {
                return rows.get(0).toString().trim();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /** CHECK #8: Verify location temperature matches product requirement */
    public boolean locationMatchesTemperature(String cpny, String whs,
            String areaCod, String rackCod, String lvlCod, String pstnCod, String tempCtrl) {
        String sql =
            "SELECT COUNT(*) FROM GWH.GWH_TM_LOC " +
            "WHERE LOC_CPNY_COD = :cpny AND LOC_WHS_COD = :whs " +
            "  AND TRIM(LOC_AREA_COD) = TRIM(:area) AND LOC_RACK_COD = :rack " +
            "  AND LOC_LVL_COD = :lvl AND LOC_PSTN_COD = :pstn " +
            "  AND LOC_TEMP_CTR = :temp AND DEL_FLG = '0'";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny); q.setParameter("whs", whs);
            q.setParameter("area", areaCod); q.setParameter("rack", rackCod);
            q.setParameter("lvl", lvlCod); q.setParameter("pstn", pstnCod);
            q.setParameter("temp", tempCtrl);
            @SuppressWarnings("unchecked")
            List<Object> rows = q.getResultList();
            return !rows.isEmpty() && ((Number) rows.get(0)).intValue() > 0;
        } catch (Exception e) {
            return true; // fail-open: if query fails, don't block
        }
    }

    /** CHECK #13: Get product weight and volume per piece */
    public double[] getProductWeightVolume(String cpny, String whs, String cust, String productCode) {
        String sql =
            "SELECT NVL(PROD_PC_WGT, 0), NVL(PROD_PC_M3, 0) FROM GWH.GWH_TM_PROD " +
            "WHERE PROD_CPNY_COD = :cpny AND PROD_WHS_COD = :whs " +
            "  AND PROD_CUST_COD = :cust AND PROD_COD = :prod AND DEL_FLG = '0'";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny); q.setParameter("whs", whs);
            q.setParameter("cust", cust); q.setParameter("prod", productCode);
            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            if (!rows.isEmpty()) {
                Object[] row = rows.get(0);
                double wgt = row[0] instanceof Number ? ((Number) row[0]).doubleValue() : 0;
                double vol = row[1] instanceof Number ? ((Number) row[1]).doubleValue() : 0;
                return new double[]{wgt, vol};
            }
            return new double[]{0, 0};
        } catch (Exception e) {
            return new double[]{0, 0};
        }
    }

    // ---------------------------------------------------------------------------
    // Process control status lookup (GWH_TM_PRCC)
    // ---------------------------------------------------------------------------

    /**
     * Query GWH_TM_PRCC for process control statuses.
     * Returns [PRCC_RSTS, PRCC_PSTS, PRCC_FSTS] or null if not found.
     * Legacy ref: gwhCbprcBLC.getProcessStatus()
     */
    public String[] findProcessControlStatuses(String cpny, String whs, String cust, String processCode) {
        // Legacy pattern: GwhMasterQueryUtils.getPartitionStatementTmPrcc()
        // PRCC records may use '*' wildcard for CPNY/WHS/CUST.
        // ROW_NUMBER partitioned by PRCC_PRCS_COD, ordered by specificity DESC
        // (most specific match = longest concatenation of codes wins).
        String sql =
            "SELECT PRCC_RSTS, PRCC_PSTS, PRCC_FSTS FROM (" +
            "  SELECT PRCC_RSTS, PRCC_PSTS, PRCC_FSTS," +
            "    ROW_NUMBER() OVER (PARTITION BY PRCC_PRCS_COD" +
            "      ORDER BY LENGTH(PRCC_CPNY_COD || PRCC_WHS_COD || PRCC_CUST_COD) DESC) PRIORITY_SEQ" +
            "  FROM GWH.GWH_TM_PRCC" +
            "  WHERE (PRCC_CPNY_COD = :cpny OR PRCC_CPNY_COD = '*')" +
            "    AND (PRCC_WHS_COD = :whs OR PRCC_WHS_COD = '*')" +
            "    AND (PRCC_CUST_COD = :cust OR PRCC_CUST_COD = '*')" +
            "    AND PRCC_PRCS_COD = :prcsCode AND DEL_FLG = '0'" +
            ") WHERE PRIORITY_SEQ = 1";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("cpny", cpny);
            q.setParameter("whs", whs);
            q.setParameter("cust", cust);
            q.setParameter("prcsCode", processCode);
            @SuppressWarnings("unchecked")
            List<Object[]> rows = q.getResultList();
            if (!rows.isEmpty()) {
                Object[] row = rows.get(0);
                return new String[]{
                    row[0] != null ? row[0].toString().trim() : null,
                    row[1] != null ? row[1].toString().trim() : null,
                    row[2] != null ? row[2].toString().trim() : null
                };
            }
            log.warn("[findProcessControlStatuses] Not found for processCode={}", processCode);
            return null;
        } catch (Exception e) {
            log.warn("[findProcessControlStatuses] Error for processCode={}: {}", processCode, e.getMessage());
            return null;
        }
    }

    // ---------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------

    private String str(Object v) { return v == null ? null : v.toString().trim(); }

    private int toInt(Object v) {
        if (v == null) return 0;
        if (v instanceof BigDecimal) return ((BigDecimal) v).intValue();
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return 0; }
    }

    private int nvl(Integer v) { return v == null ? 0 : v; }

    private BigDecimal toBigDecimal(Object v) {
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal) return (BigDecimal) v;
        if (v instanceof Number) return BigDecimal.valueOf(((Number) v).doubleValue());
        try { return new BigDecimal(v.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    /** Check if an area code is a real location value (not a placeholder like "---", "-", null, blank) */
    private boolean isValidAreaCode(String area) {
        if (area == null) return false;
        String trimmed = area.trim();
        return !trimmed.isEmpty() && !trimmed.matches("^-+$");
    }

    private LocalDate toDate(Object v) {
        if (v == null) return null;
        if (v instanceof Date) return ((Date) v).toLocalDate();
        if (v instanceof java.sql.Timestamp) return ((java.sql.Timestamp) v).toLocalDateTime().toLocalDate();
        return null;
    }

    // ---------------------------------------------------------------------------
    // Recommended Location Resolution (Legacy: GwhApins010BLC / GwhApins010DAO)
    // ---------------------------------------------------------------------------

    /**
     * Get CUST_LOCS_KND from customer master.
     * "1" = final shelving, "2" = max stock, "3"/"0" = product/customer default.
     */
    @SuppressWarnings("unchecked")
    public String getLocationSearchKind(String cpny, String whs, String cust) {
        String sql =
            "SELECT M1.CUST_LOCS_KND FROM GWH.GWH_TM_CUST M1 " +
            "WHERE M1.CUST_CPNY_COD = :cpny AND M1.CUST_WHS_COD = :whs " +
            "  AND M1.CUST_COD = :cust AND M1.DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        List<Object> rows = q.getResultList();
        if (!rows.isEmpty() && rows.get(0) != null) {
            return rows.get(0).toString().trim();
        }
        return "0";
    }

    /**
     * Final shelving location โ€” where this product was last received (most recent stock record).
     * Legacy: GwhApins010DAO.getFinalShelvingLoc()
     * Returns [area, rack, pstn, lvl] or null if not found.
     */
    public String[] getFinalShelvingLocation(String cpny, String whs, String cust, String productCode) {
        String sql =
            "SELECT T1.ST_AREA_COD, T1.ST_RACK_COD, T1.ST_PSTN_COD, T1.ST_LVL_COD " +
            "FROM GWH.GWH_TJ_ST T1 " +
            "WHERE T1.ST_CPNY_COD = :cpny AND T1.ST_WHS_COD = :whs " +
            "  AND T1.ST_CUST_COD = :cust AND T1.ST_PROD_COD = :prd " +
            "  AND T1.DEL_FLG = '0' " +
            "  AND T1.CRT_YMDHMS = (" +
            "    SELECT MAX(TJ1.CRT_YMDHMS) FROM GWH.GWH_TJ_ST TJ1 " +
            "    LEFT JOIN GWH.GWH_TJ_AV_R TJ2 " +
            "      ON TJ2.AVR_CPNY_COD = TJ1.ST_CPNY_COD " +
            "      AND TJ2.AVR_WHS_COD = TJ1.ST_WHS_COD " +
            "      AND TJ2.AVR_CUST_COD = TJ1.ST_CUST_COD " +
            "      AND TJ2.AVR_AV_NUM = TJ1.ST_AV_NUM " +
            "      AND TJ2.AVR_PROD_COD = TJ1.ST_PROD_COD " +
            "    WHERE TJ1.ST_CPNY_COD = :cpny AND TJ1.ST_WHS_COD = :whs " +
            "      AND TJ1.ST_CUST_COD = :cust AND TJ1.ST_PROD_COD = :prd " +
            "      AND TJ1.DEL_FLG = '0' AND TJ2.AVR_AS_KND = 'AV'" +
            "  ) " +
            "GROUP BY T1.ST_AREA_COD, T1.ST_RACK_COD, T1.ST_PSTN_COD, T1.ST_LVL_COD";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("prd", productCode);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.setMaxResults(1).getResultList();
        if (!rows.isEmpty() && rows.get(0) != null && rows.get(0)[0] != null) {
            Object[] row = rows.get(0);
            return new String[]{str(row[0]), str(row[1]), str(row[2]), str(row[3])};
        }
        return null;
    }

    /**
     * Maximum stock location โ€” location with highest physical stock for this product.
     * Legacy: GwhApins010DAO.getMaximumStockLoc()
     * Returns [area, rack, pstn, lvl] or null if not found.
     */
    public String[] getMaximumStockLocation(String cpny, String whs, String cust, String productCode) {
        String sql =
            "SELECT TJ1.ST_AREA_COD, TJ1.ST_RACK_COD, TJ1.ST_PSTN_COD, TJ1.ST_LVL_COD " +
            "FROM (" +
            "  SELECT SUM(T1.ST_PYST_QTY) AS TOTAL_QTY, " +
            "    T1.ST_AREA_COD, T1.ST_RACK_COD, T1.ST_PSTN_COD, T1.ST_LVL_COD " +
            "  FROM GWH.GWH_TJ_ST T1 " +
            "  WHERE T1.ST_CPNY_COD = :cpny AND T1.ST_WHS_COD = :whs " +
            "    AND T1.ST_CUST_COD = :cust AND T1.ST_PROD_COD = :prd " +
            "    AND T1.DEL_FLG = '0' " +
            "  GROUP BY T1.ST_AREA_COD, T1.ST_RACK_COD, T1.ST_PSTN_COD, T1.ST_LVL_COD " +
            "  ORDER BY TOTAL_QTY DESC, T1.ST_AREA_COD, T1.ST_RACK_COD, T1.ST_PSTN_COD, T1.ST_LVL_COD" +
            ") TJ1 " +
            "WHERE ROWNUM = 1 AND TJ1.TOTAL_QTY > 0";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("prd", productCode);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        if (!rows.isEmpty() && rows.get(0) != null && rows.get(0)[0] != null) {
            Object[] row = rows.get(0);
            return new String[]{str(row[0]), str(row[1]), str(row[2]), str(row[3])};
        }
        return null;
    }

    /**
     * Get product master default receiving location (PROD_RCAR_COD, PROD_RCRA_COD, etc.)
     * Returns [area, rack, pstn, lvl] or null if not set.
     */
    public String[] getProductDefaultLocation(String cpny, String whs, String cust, String productCode) {
        String sql =
            "SELECT P.PROD_RCAR_COD, P.PROD_RCRA_COD, P.PROD_RCPS_COD, P.PROD_RCLV_COD " +
            "FROM GWH.GWH_TM_PROD P " +
            "WHERE P.PROD_CPNY_COD = :cpny AND P.PROD_WHS_COD = :whs " +
            "  AND P.PROD_CUST_COD = :cust AND P.PROD_COD = :prd AND P.DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        q.setParameter("prd", productCode);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        if (!rows.isEmpty() && rows.get(0) != null) {
            String area = str(rows.get(0)[0]);
            if (area != null && !area.trim().isEmpty()) {
                Object[] row = rows.get(0);
                return new String[]{area, str(row[1]), str(row[2]), str(row[3])};
            }
        }
        return null;
    }

    /**
     * Get customer master default arrival location (CUST_AV_AREA, CUST_AV_RACK, etc.)
     * Returns [area, rack, pstn, lvl] or null if not set.
     */
    public String[] getCustomerDefaultLocation(String cpny, String whs, String cust) {
        String sql =
            "SELECT C.CUST_AV_AREA, C.CUST_AV_RACK, C.CUST_AV_PSTN, C.CUST_AV_LVL " +
            "FROM GWH.GWH_TM_CUST C " +
            "WHERE C.CUST_CPNY_COD = :cpny AND C.CUST_WHS_COD = :whs " +
            "  AND C.CUST_COD = :cust AND C.DEL_FLG = '0'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("cpny", cpny);
        q.setParameter("whs", whs);
        q.setParameter("cust", cust);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        if (!rows.isEmpty() && rows.get(0) != null) {
            String area = str(rows.get(0)[0]);
            if (area != null && !area.trim().isEmpty()) {
                Object[] row = rows.get(0);
                return new String[]{area, str(row[1]), str(row[2]), str(row[3])};
            }
        }
        return null;
    }

    // ---------------------------------------------------------------------------
    // Transaction log (GWH_TJ_XT)
    // ---------------------------------------------------------------------------

    /**
     * Insert one transaction log record into GWH_TJ_XT.
     * Matches legacy gwhCpxtBLC.gwhCpxt001Reg() โ€” records arrival inspection events.
     *
     * @param opKnd Operation kind: first 2 chars of PRCC_FSTS (e.g. "20" for status 200)
     */
    public void insertTransactionLog(String cpny, String whs, String cust,
                                     String asNum, int aslnNum, int assqNum,
                                     String asKnd, String opKnd,
                                     String userId) {
        String sql =
            "INSERT INTO GWH.GWH_TJ_XT (" +
            "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
            "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS, " +
            "XT_CPNY_COD, XT_WHS_COD, XT_CUST_COD, XT_AS_NUM, XT_ASLN_NUM, XT_ASSQ_NUM, " +
            "XT_AS_KND, XT_OP_KND, XT_OPRT_USER, XT_EVNT_RMKS, " +
            "XT_STR_YMD, XT_STR_TIM, XT_END_YMD, XT_END_TIM" +
            ") VALUES (" +
            "'0', TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-MODERN', :userId, 'RECEIVE', 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
            "TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS'), 'GWH-MODERN', :userId, 'RECEIVE', 'UTC', SYSTIMESTAMP, SYSTIMESTAMP, " +
            ":cpny, :whs, :cust, :asNum, :aslnNum, :assqNum, " +
            ":asKnd, :opKnd, :userId, NULL, " +
            "NULL, NULL, TO_CHAR(SYSDATE,'YYYYMMDD'), TO_CHAR(SYSDATE,'HH24MISS')" +
            ")";
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter("userId", userId != null ? userId : "SYSTEM");
            q.setParameter("cpny", cpny); q.setParameter("whs", whs); q.setParameter("cust", cust);
            q.setParameter("asNum", asNum); q.setParameter("aslnNum", aslnNum); q.setParameter("assqNum", assqNum);
            q.setParameter("asKnd", asKnd != null ? asKnd : "AV");
            q.setParameter("opKnd", opKnd);
            q.executeUpdate();
        } catch (RuntimeException ex) {
            // Duplicate XT โ€” log and skip (same as legacy)
            String msg = ex.getMessage();
            if (msg != null && (msg.contains("ORA-00001") || msg.contains("unique constraint"))) {
                log.warn("[insertTransactionLog] Duplicate XT for {}/{}/{}, skipping", asNum, aslnNum, assqNum);
            } else {
                throw ex;
            }
        }
    }

    /**
     * Resolve recommended location using the legacy 3-tier priority system.
     * <pre>
     * Priority (controlled by CUST_LOCS_KND):
     *   "1" โ’ Final shelving (last received location) โ’ Product default โ’ Customer default
     *   "2" โ’ Maximum stock location                  โ’ Product default โ’ Customer default
     *   "3"/"0" โ’                                       Product default โ’ Customer default
     * </pre>
     * Returns [area, rack, pstn, lvl] or null if no location resolved.
     */
    public String[] resolveRecommendedLocation(String cpny, String whs, String cust, String productCode) {
        String locsKnd = getLocationSearchKind(cpny, whs, cust);
        log.info("[resolveRecommendedLocation] cpny={} whs={} cust={} product={} locsKnd={}", cpny, whs, cust, productCode, locsKnd);

        String[] loc = null;

        // Tier 1: Stock-based lookup (only for locsKnd "1" or "2")
        if ("1".equals(locsKnd)) {
            loc = getFinalShelvingLocation(cpny, whs, cust, productCode);
            log.info("[resolveRecommendedLocation] Tier1 getFinalShelvingLocation result={}", loc != null ? (loc[0]+"/"+loc[1]+"/"+loc[2]+"/"+loc[3]) : "null");
        } else if ("2".equals(locsKnd)) {
            loc = getMaximumStockLocation(cpny, whs, cust, productCode);
            log.info("[resolveRecommendedLocation] Tier1 getMaximumStockLocation result={}", loc != null ? (loc[0]+"/"+loc[1]+"/"+loc[2]+"/"+loc[3]) : "null");
        }

        // Tier 2: Product master default
        if (loc == null) {
            loc = getProductDefaultLocation(cpny, whs, cust, productCode);
            log.info("[resolveRecommendedLocation] Tier2 getProductDefaultLocation result={}", loc != null ? (loc[0]+"/"+loc[1]+"/"+loc[2]+"/"+loc[3]) : "null");
        }

        // Tier 3: Customer master default
        if (loc == null) {
            loc = getCustomerDefaultLocation(cpny, whs, cust);
            log.info("[resolveRecommendedLocation] Tier3 getCustomerDefaultLocation result={}", loc != null ? (loc[0]+"/"+loc[1]+"/"+loc[2]+"/"+loc[3]) : "null");
        }

        log.info("[resolveRecommendedLocation] FINAL result={}", loc != null ? (loc[0]+"/"+loc[1]+"/"+loc[2]+"/"+loc[3]) : "null");
        return loc;
    }
}
