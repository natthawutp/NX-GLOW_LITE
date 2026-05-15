package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.module.inbound.dto.ArrivalInspectionListDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionCancelRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionLineDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionRegisterRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionRegisterRequest.InspectionLineRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionRegisterResponse;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalInspectionRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Business logic for arrival inspection.
 *
 * Status codes used (migrated from legacy apins):
 *   200 = Located / Ready for inspection (PRCC_RSTS equivalent)
 *   205 = Inspected          (PRCC_PSTS equivalent)
 *   209 = Confirmed          (PRCC_FSTS equivalent)
 *
 * LPN status after inspection: 200 (Received).
 */
@Service
public class ArrivalInspectionService {

    private static final Logger log = LoggerFactory.getLogger(ArrivalInspectionService.class);

    /** Header status set on the arrival after all lines are inspected */
    private static final String STATUS_INSPECTED = "205";
    /** Line status set per detail after successful inspection */
    private static final String STATUS_LINE_INSPECTED = "205";
    /** Status to revert lines to on cancel */
    private static final String STATUS_REVERT = "200";

    @Autowired
    private ArrivalInspectionRepository repo;

    @Autowired
    private TenantContext tenantContext;

    // ---------------------------------------------------------------------------
    // Search
    // ---------------------------------------------------------------------------

    public List<ArrivalInspectionListDto> search(
            String arrivalNo, String status, LocalDate dateFrom, LocalDate dateTo) {

        List<ArrivalInspectionListDto> rows = repo.searchInspectable(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                arrivalNo, status, dateFrom, dateTo);

        for (ArrivalInspectionListDto row : rows) {
            row.setArrivalStatusLabel(mapStatusLabel(row.getArrivalStatus()));
        }
        return rows;
    }

    // ---------------------------------------------------------------------------
    // Get detail lines
    // ---------------------------------------------------------------------------

    public List<InspectionLineDto> getLines(String arrivalNumber) {
        return repo.getInspectionLines(
                tenantContext.getCompanyCode(),
                tenantContext.getWarehouseCode(),
                tenantContext.getCustomerCode(),
                arrivalNumber);
    }

    // ---------------------------------------------------------------------------
    // Register inspection
    // ---------------------------------------------------------------------------

    @Transactional
    public InspectionRegisterResponse register(InspectionRegisterRequest request) {
        String cpny = tenantContext.getCompanyCode();
        String whs  = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();
        String userId = tenantContext.getUserId();
        String avNum = request.getArrivalNumber();
        boolean isNew = !"CHANGE".equalsIgnoreCase(request.getProcessMode()) &&
                        !"LOCATION".equalsIgnoreCase(request.getProcessMode());

        // Load AVD lines once to resolve product codes
        List<InspectionLineDto> avdLines = repo.getInspectionLines(cpny, whs, cust, avNum);
        java.util.Map<Integer, InspectionLineDto> avdByLine = new java.util.HashMap<>();
        for (InspectionLineDto dl : avdLines) { avdByLine.put(dl.getLineNumber(), dl); }

        Set<String> lpnsUsed = new LinkedHashSet<>();
        int registeredCount = 0;

        for (InspectionLineRequest line : request.getLines()) {
            int lineNum = line.getLineNumber();
            InspectionLineDto avd = avdByLine.get(lineNum);
            String productCode = (avd != null) ? avd.getProductCode() : null;

            // Resolve actual quantities — default 0 if null
            int cs    = nvl(line.getResultCsQty());
            int pc    = nvl(line.getResultPcQty());
            int bl    = nvl(line.getResultBlQty());
            int total = cs + pc + bl;  // simplified; real PPC/PPB calculation belongs in controller validation

            // Determine LPN: line-level overrides collective header LPN
            String lpn = (line.getLpnNumber() != null && !line.getLpnNumber().trim().isEmpty())
                    ? line.getLpnNumber().trim()
                    : (request.getCollectiveLpnNumber() != null && !request.getCollectiveLpnNumber().trim().isEmpty()
                            ? request.getCollectiveLpnNumber().trim()
                            : null);

            String lpnType = (line.getLpnType() != null && !line.getLpnType().trim().isEmpty())
                    ? line.getLpnType().trim()
                    : (request.getCollectiveLpnType() != null && !request.getCollectiveLpnType().trim().isEmpty()
                            ? request.getCollectiveLpnType().trim()
                            : "PLT");

            // On NEW mode: delete existing AVR records before inserting
            if (isNew) {
                repo.deleteArrivalResults(cpny, whs, cust, avNum, lineNum);
            }

            // Resolve location: user-provided → legacy 3-tier resolution
            String areaCode = line.getAreaCode();
            String rackCode = line.getRackCode();
            String lvlCode  = line.getLevelCode();
            String pstnCode = line.getPositionCode();
            if (areaCode == null || areaCode.trim().isEmpty()) {
                String[] loc = repo.resolveRecommendedLocation(cpny, whs, cust, productCode);
                if (loc != null) {
                    areaCode = loc[0];
                    rackCode = loc[1];
                    pstnCode = loc[2];
                    lvlCode  = loc[3];
                }
            }

            // Insert AVR record (seq = 1 for single-cargo lines)
            String orgnCode = (avd != null) ? avd.getOriginCode() : null;
            String trnKnd = (avd != null) ? avd.getTrnKnd() : null;
            Integer ppcsQty = (avd != null) ? avd.getPiecesPerCase() : null;
            Integer ppbQty = (avd != null) ? avd.getPiecesPerBulk() : null;
            String sbivCode = (avd != null) ? avd.getSubInventoryCode() : null;
            String pik1 = (avd != null) ? avd.getPik1() : null;
            String pik2 = (avd != null) ? avd.getPik2() : null;
            String pik3 = (avd != null) ? avd.getPik3() : null;
            String pik4 = (avd != null) ? avd.getPik4() : null;
            String pik5 = (avd != null) ? avd.getPik5() : null;
            String pik6 = (avd != null) ? avd.getPik6() : null;
            String pik7 = (avd != null) ? avd.getPik7() : null;
            String dmgFlg = (avd != null) ? avd.getDmgFlg() : "N";
            double[] wgtVol = repo.getProductWeightVolume(cpny, whs, cust, productCode);
            Double rwgt = wgtVol[0] * total;
            Double rm3  = wgtVol[1] * total;
            repo.insertArrivalResult(
                    cpny, whs, cust, avNum, lineNum, 1,
                    productCode, orgnCode, trnKnd, null,
                    ppcsQty, ppbQty, cs, pc, bl, total,
                    sbivCode,
                    pik1, pik2, pik3, pik4, pik5, pik6, pik7,
                    areaCode, rackCode, lvlCode, pstnCode,
                    dmgFlg, "N",
                    rwgt, rm3,
                    lpn, userId);

            // Update AVD line status
            repo.updateDetailStatus(cpny, whs, cust, avNum, lineNum, STATUS_LINE_INSPECTED, userId);

            // Upsert LPN if provided (header + detail with AVR references)
            if (lpn != null) {
                String lotNumber = (avd != null) ? avd.getLotNumber() : null;
                // Read AVR_AS_NUM, AVR_ASLN_NUM, AVR_ASSQ_NUM from the AVR record
                Object[] avrNums = repo.getAvrArrivalNumbers(cpny, whs, cust, avNum, lineNum);
                String avrAsNum = avrNums != null && avrNums[0] != null ? avrNums[0].toString() : avNum;
                int avrAslnNum = avrNums != null && avrNums[1] != null ? ((Number) avrNums[1]).intValue() : lineNum;
                int avrAssqNum = avrNums != null && avrNums[2] != null ? ((Number) avrNums[2]).intValue() : 1;
                repo.upsertLpn(cpny, whs, cust, lpn, lpnType, avrAsNum,
                        avrAslnNum, avrAssqNum, productCode, lotNumber, total, sbivCode, userId);
                lpnsUsed.add(lpn);
            }

            registeredCount++;
        }

        // Update AVH header status to Inspected
        repo.updateHeaderStatus(cpny, whs, cust, avNum, STATUS_INSPECTED, userId);

        return new InspectionRegisterResponse(avNum, STATUS_INSPECTED, registeredCount,
                new ArrayList<>(lpnsUsed));
    }

    // ---------------------------------------------------------------------------
    // Cancel inspection
    // ---------------------------------------------------------------------------

    @Transactional
    public void cancel(InspectionCancelRequest request) {
        String cpny   = tenantContext.getCompanyCode();
        String whs    = tenantContext.getWarehouseCode();
        String cust   = tenantContext.getCustomerCode();
        String userId = tenantContext.getUserId();
        String avNum  = request.getArrivalNumber();

        repo.cancelArrivalResults(cpny, whs, cust, avNum, userId);
        repo.cancelDetailStatuses(cpny, whs, cust, avNum, STATUS_REVERT, userId);
        repo.updateHeaderStatus(cpny, whs, cust, avNum, STATUS_REVERT, userId);
    }

    // ---------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------

    private int nvl(Integer v) { return v == null ? 0 : v; }

    private String mapStatusLabel(String status) {
        if (status == null) return "";
        switch (status.trim()) {
            case "100": return "Pre";
            case "200": return "Ready for Inspection";
            case "205": return "Inspecting";
            case "209": return "Inspected";
            case "300": return "Receiving";
            case "305": return "Locating";
            case "306": return "Located";
            case "500": return "Putaway";
            case "609": return "Stored";
            case "809": return "Confirmed";
            default:    return status;
        }
    }
}
