package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionLineDto;
import jp.co.nittsu.gwh.module.inbound.dto.ReceiveArrivalDto;
import jp.co.nittsu.gwh.module.inbound.dto.ScanItemRequest;
import jp.co.nittsu.gwh.module.inbound.dto.ScanItemResponse;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalInspectionRepository;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalInspectionRepository.TransactionPermitFlags;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jp.co.nittsu.gwh.config.GwhTransactionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for the real-time scan-receiving flow.
 *
 * Flow:
 *  1. User scans arrival number → loadArrival() → returns header + all AVD lines
 *  2. User scans item barcode  → processItemScan() → INSERT or UPDATE AVR per scan,
 *     update AVD line status to 300 (Receiving), update AVH if needed
 *
 * AVR accumulation rule (mirrors legacy apins020 per-scan semantics):
 *  - If AVR row (seq=1) does NOT exist yet → INSERT with resultTotalQty = scanned qty
 *  - If AVR row already exists             → UPDATE, add qty (accumulate)
 *  This allows the operator to scan the same product multiple times.
 *
 * Status codes:
 *   200 = Located / Ready for receiving
 *   300 = Receiving (in-progress)
 */
@Service
public class InboundReceiveService {

    private static final Logger log = LoggerFactory.getLogger(InboundReceiveService.class);
    private static final String STATUS_RECEIVING = "300";
    private static final String STATUS_INSPECTED  = "209";

    @Autowired
    private InboundOrderRepository orderRepo;

    @Autowired
    private ArrivalInspectionRepository inspRepo;

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private GwhTransactionProperties transProps;

    // ---------------------------------------------------------------------------
    // Load arrival (scan arrival number)
    // ---------------------------------------------------------------------------

    public ReceiveArrivalDto loadArrival(String arrivalNumber) {
        String cpny = tenantContext.getCompanyCode();
        String whs  = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        log.info("[loadArrival] START arrivalNumber={} cpny={} whs={} cust={}", arrivalNumber, cpny, whs, cust);

        InboundOrderDto header = orderRepo.findByArrivalNumber(cpny, whs, cust, arrivalNumber);
        if (header == null) {
            log.warn("[loadArrival] header not found for arrivalNumber={}", arrivalNumber);
            throw new IllegalArgumentException("Arrival not found: " + arrivalNumber);
        }
        log.info("[loadArrival] header found: status={} supplier={}", header.getArrivalStatus(), header.getSupplierCode());

        // --- CHECK #1: Arrival status gate (DB-driven: GWH_TM_PRCC.apinsReg) ---
        String arrivalStatus = header.getArrivalStatus();
        String[] prccStatuses = inspRepo.findProcessControlStatuses(cpny, whs, cust, "apinsReg");
        if (prccStatuses != null && prccStatuses[0] != null) {
            int currentSts = Integer.parseInt(arrivalStatus.trim());
            int requiredSts = Integer.parseInt(prccStatuses[0]);
            if (currentSts < requiredSts) {
                throw new ReceiveViolationException("ERR1027",
                    transProps.getErrorMessage("ERR1027")
                    + " Current: " + arrivalStatus + ", required >= " + prccStatuses[0]);
            }
            if (prccStatuses[2] != null) {
                int finishSts = Integer.parseInt(prccStatuses[2]);
                if (currentSts >= finishSts) {
                    throw new ReceiveViolationException("ERR1029",
                        "Arrival has already completed receiving. Status: " + arrivalStatus);
                }
            }
        } else {
            log.warn("[loadArrival] No PRCC record for apinsReg, cpny={} whs={} cust={}", cpny, whs, cust);
        }

        // --- CHECK #10: Transaction kind arrival usage (Legacy: GWH_TM_TRN.TRN_AVUS_FLG) ---
        String trnKind = header.getTransactionKind();
        List<String> validKinds = transProps.getReceiving().getValidArrivalTransactionKinds();
        if (validKinds != null && !validKinds.isEmpty()
                && trnKind != null && !trnKind.trim().isEmpty()
                && !validKinds.contains(trnKind.trim())) {
            throw new ReceiveViolationException("ERR3252",
                transProps.getErrorMessage("ERR3252") + " Transaction kind: " + trnKind);
        }

        return buildArrivalDto(cpny, whs, cust, arrivalNumber, header);
    }

    // ---------------------------------------------------------------------------
    // Process one item scan
    // ---------------------------------------------------------------------------

    @Transactional
    public ScanItemResponse processItemScan(ScanItemRequest request) {
        String cpny   = tenantContext.getCompanyCode();
        String whs    = tenantContext.getWarehouseCode();
        String cust   = tenantContext.getCustomerCode();
        String userId = tenantContext.getUserId();
        String avNum  = request.getArrivalNumber();

        // --- Find matching AVD line by product code ---
        log.info("[processItemScan] cpny={} whs={} cust={} avNum={} productCode={}", cpny, whs, cust, avNum, request.getProductCode());
        List<InspectionLineDto> lines = inspRepo.getInspectionLines(cpny, whs, cust, avNum);
        log.info("[processItemScan] lines returned: {}", lines.size());
        for (InspectionLineDto l : lines) {
            log.info("[processItemScan]   line#{} productCode=[{}]", l.getLineNumber(), l.getProductCode());
        }
        InspectionLineDto matched = null;
        for (InspectionLineDto line : lines) {
            if (request.getProductCode().trim().equalsIgnoreCase(
                    line.getProductCode() != null ? line.getProductCode().trim() : "")) {
                matched = line;
                break;
            }
        }
        if (matched == null) {
            throw new IllegalArgumentException(
                "Product [" + request.getProductCode() + "] is not on arrival [" + avNum + "]");
        }

        int lineNum = matched.getLineNumber();
        int addQty  = request.getQuantity();
        String lpn  = (request.getLpnNumber() != null && !request.getLpnNumber().trim().isEmpty())
                      ? request.getLpnNumber().trim() : null;
        String lpnType = request.getLpnType() != null ? request.getLpnType() : "PLT";

        // --- RESOLVE RECOMMENDED LOCATION (Legacy: GwhApins010BLC 3-tier priority) ---
        // Legacy always resolves location fresh per locsKnd, never copies from AVD.
        // Priority: stock-based (tier1/2) → product master → customer master
        String productCode = matched.getProductCode();
        boolean locationAutoResolved = false;
        String areaCode, rackCode, lvlCode, pstnCode;

        // Check if existing AVR already has a location (legacy: if AVR exists with location, keep it)
        boolean existingAvrHasLocation = false;
        if (inspRepo.avrExists(cpny, whs, cust, avNum, matched.getLineNumber())) {
            // Read location from existing AVR
            String[] existingLoc = inspRepo.getAvrLocation(cpny, whs, cust, avNum, matched.getLineNumber());
            if (existingLoc != null && existingLoc[0] != null && !existingLoc[0].trim().isEmpty()) {
                existingAvrHasLocation = true;
                areaCode = existingLoc[0];
                rackCode = existingLoc[1];
                pstnCode = existingLoc[2];
                lvlCode  = existingLoc[3];
            } else {
                existingAvrHasLocation = false;
                areaCode = null; rackCode = null; lvlCode = null; pstnCode = null;
            }
        } else {
            areaCode = null; rackCode = null; lvlCode = null; pstnCode = null;
        }

        // If no existing AVR location, resolve fresh (legacy: always resolves for new AVR)
        if (!existingAvrHasLocation) {
            log.info("[processItemScan] No existing AVR location, resolving fresh for product={}", productCode);
            String[] loc = inspRepo.resolveRecommendedLocation(cpny, whs, cust, productCode);
            log.info("[processItemScan] resolveRecommendedLocation returned: {}", loc != null ? (loc[0]+"/"+loc[1]+"/"+loc[2]+"/"+loc[3]) : "null");
            if (loc != null) {
                areaCode = loc[0];
                rackCode = loc[1];
                pstnCode = loc[2];
                lvlCode  = loc[3];
                locationAutoResolved = true;
            }
        } else {
            log.info("[processItemScan] Existing AVR has location: area={} rack={} lvl={} pstn={}", areaCode, rackCode, lvlCode, pstnCode);
        }
        log.info("[processItemScan] FINAL location: area={} rack={} lvl={} pstn={} autoResolved={}", areaCode, rackCode, lvlCode, pstnCode, locationAutoResolved);

        // Location validation only for user-provided locations (legacy doesn't validate auto-resolved)
        if (!locationAutoResolved && areaCode != null && !areaCode.trim().isEmpty()) {
            // --- CHECK #11: Area code byte length (Legacy: GWH_TM_WHS.WHS_ABYT) ---
            int maxAreaBytes = transProps.getReceiving().getAreaCodeByteLength();
            if (maxAreaBytes > 0 && areaCode.length() > maxAreaBytes) {
                throw new ReceiveViolationException("ERR0023",
                    transProps.getErrorMessage("ERR0023") + " Max: " + maxAreaBytes + ", Got: " + areaCode.length());
            }

            // --- CHECK #12: Location existence (Legacy: GWH_TM_LOC) ---
            if (!inspRepo.locationExists(cpny, whs, areaCode, rackCode, lvlCode, pstnCode)) {
                throw new ReceiveViolationException("ERR0004",
                    transProps.getErrorMessage("ERR0004"));
            }
        }

        // --- CHECK #7: SKU mixing at location (Legacy: GWH_TM_PROD_G.PRDG_ADIF_SKU_FLG) ---
        if (areaCode != null && !areaCode.trim().isEmpty() && !areaCode.trim().equals("---") && !areaCode.trim().equals("-")) {
            if (inspRepo.hasDifferentProductAtLocation(cpny, whs, cust, areaCode, rackCode, lvlCode, pstnCode, productCode)) {
                throw new ReceiveViolationException("ERR1346",
                    transProps.getErrorMessage("ERR1346"));
            }
        }

        // --- CHECK #8: Temperature control (Legacy: GWH_TM_PROD_G.PRDG_TEMP_CTR) ---
        if (areaCode != null && !areaCode.trim().isEmpty() && !areaCode.trim().equals("---") && !areaCode.trim().equals("-")) {
            String tempCtrl = inspRepo.getProductTempControl(cpny, whs, cust, productCode);
            if (tempCtrl != null && !tempCtrl.isEmpty()) {
                if (!inspRepo.locationMatchesTemperature(cpny, whs, areaCode, rackCode, lvlCode, pstnCode, tempCtrl)) {
                    throw new ReceiveViolationException("ERR1348",
                        transProps.getErrorMessage("ERR1348"));
                }
            }
        }

        // --- CHECK #4: HHT inspection mode (Legacy: GWH_TM_CODE KND=477) ---
        String operationFlag = transProps.getReceiving().getHhtInspectionMode().isEnabled() ? "N" : "Y";

        // --- Resolve AVSP_STS from PRCC (Legacy: PRCC_RSTS for receiving status) ---
        String avspSts = STATUS_RECEIVING; // default "300"
        String[] prccStatuses = inspRepo.findProcessControlStatuses(cpny, whs, cust, "apinsReg");
        if (prccStatuses != null && prccStatuses[0] != null) {
            avspSts = prccStatuses[0]; // PRCC_RSTS = receiving start status
        }

        // --- Resolve AVR_RWGT / AVR_RM3 from product master (Legacy: AVD_WGT * qty, AVD_M3 * qty) ---
        double[] wgtVol = inspRepo.getProductWeightVolume(cpny, whs, cust, productCode);
        Double lineWeightKg = wgtVol[0] * addQty;
        Double lineVolumeM3 = wgtVol[1] * addQty;

        // --- INSERT or ACCUMULATE AVR ---
        log.info("[processItemScan] STEP: checking avrExists");
        boolean exists = inspRepo.avrExists(cpny, whs, cust, avNum, lineNum);
        log.info("[processItemScan] avrExists={}", exists);
        if (exists) {
            log.info("[processItemScan] STEP: accumulateArrivalResult");
            inspRepo.accumulateArrivalResult(cpny, whs, cust, avNum, lineNum, addQty, lpn, userId);
            log.info("[processItemScan] STEP: accumulateArrivalResult DONE");
        } else {
            log.info("[processItemScan] STEP: insertArrivalResult");
            inspRepo.insertArrivalResult(
                    cpny, whs, cust, avNum, lineNum, 1,
                    matched.getProductCode(), matched.getOriginCode(), matched.getTrnKnd(), avspSts,
                    matched.getPiecesPerCase(), matched.getPiecesPerBulk(),
                    0, addQty, 0, addQty,
                    matched.getSubInventoryCode(),
                    matched.getPik1(), matched.getPik2(), matched.getPik3(), matched.getPik4(),
                    matched.getPik5(), matched.getPik6(), matched.getPik7(),
                    areaCode, rackCode, lvlCode, pstnCode,
                    matched.getDmgFlg(), operationFlag,
                    lineWeightKg, lineVolumeM3,
                    lpn, userId);
            log.info("[processItemScan] STEP: insertArrivalResult DONE");
        }

        // --- Upsert LPN header + detail if LPN provided ---
        if (lpn != null) {
            log.info("[processItemScan] STEP: upsertLpn lpn={}", lpn);
            // Read AVR_AS_NUM, AVR_ASLN_NUM, AVR_ASSQ_NUM from the AVR record
            Object[] avrNums = inspRepo.getAvrArrivalNumbers(cpny, whs, cust, avNum, lineNum);
            String avrAsNum = avrNums != null && avrNums[0] != null ? avrNums[0].toString() : avNum;
            int avrAslnNum = avrNums != null && avrNums[1] != null ? ((Number) avrNums[1]).intValue() : lineNum;
            int avrAssqNum = avrNums != null && avrNums[2] != null ? ((Number) avrNums[2]).intValue() : 1;
            inspRepo.upsertLpn(cpny, whs, cust, lpn, lpnType, avrAsNum,
                    avrAslnNum, avrAssqNum, matched.getProductCode(), matched.getLotNumber(),
                    addQty, matched.getSubInventoryCode(), userId);
            log.info("[processItemScan] STEP: upsertLpn DONE");
        }

        // --- INSERT Transaction Log (GWH_TJ_XT) ---
        // Matches legacy gwhCpxtBLC.gwhCpxt001Reg() per detail line
        log.info("[processItemScan] STEP: insertTransactionLog");
        try {
            String xtOpKnd = avspSts != null && avspSts.length() >= 2 ? avspSts.substring(0, 2) : "30";
            inspRepo.insertTransactionLog(cpny, whs, cust, avNum, lineNum, 1,
                    "AV", xtOpKnd, userId);
            log.info("[processItemScan] STEP: insertTransactionLog DONE");
        } catch (Exception ex) {
            // XT insert failure should not block receiving
            log.warn("[processItemScan] XT insert failed for {}/{}: {}", avNum, lineNum, ex.getMessage());
        }

        // --- Get updated line total ---
        log.info("[processItemScan] STEP: getLineReceivedQty");
        int lineReceived = inspRepo.getLineReceivedQty(cpny, whs, cust, avNum, lineNum);
        log.info("[processItemScan] lineReceived={}", lineReceived);
        int planTotal    = matched.getPlanTotalQty() != null ? matched.getPlanTotalQty() : 0;

        // --- CHECK #13: Weight/volume overflow (Legacy: GwhDataUtils precision) ---
        GwhTransactionProperties.ReceivingLimits limits = transProps.getReceiving().getLimits();
        if (limits.getMaxLineWeightKg() > 0 || limits.getMaxLineVolumeM3() > 0) {
            double lineWeight = wgtVol[0] * lineReceived;
            double lineVolume = wgtVol[1] * lineReceived;
            if (limits.getMaxLineWeightKg() > 0 && lineWeight > limits.getMaxLineWeightKg()) {
                throw new ReceiveViolationException("ERR1036",
                    transProps.getErrorMessage("ERR1036") + " Weight: " + lineWeight + " kg");
            }
            if (limits.getMaxLineVolumeM3() > 0 && lineVolume > limits.getMaxLineVolumeM3()) {
                throw new ReceiveViolationException("ERR1037",
                    transProps.getErrorMessage("ERR1037") + " Volume: " + lineVolume + " m3");
            }
        }

        // --- CHECK #14: CGID uniqueness ---
        // Placeholder: if AVD_CGID_NUM > 1, inspection is blocked for collective mode
        // This only applies to collective inspection (batch confirm), not per-scan
        // Skipped in per-scan mode

        // --- Load transaction permit flags ---
        TransactionPermitFlags permitFlags = inspRepo.getTransactionPermitFlags(cpny, whs, cust, avNum);
        boolean surplusAllowed  = permitFlags.isSurplusAllowed();
        boolean shortageAllowed = permitFlags.isShortageAllowed();

        // --- Over-receive check (SR-1) ---
        if (planTotal > 0 && lineReceived > planTotal && !surplusAllowed) {
            throw new ReceiveViolationException("ERR1033",
                "Over-receive is not permitted for this transaction kind. " +
                "Received: " + lineReceived + ", Planned: " + planTotal);
        }

        // --- Determine receiveStatus and warning ---
        String receiveStatus;
        String receiveWarning = null;
        if (planTotal == 0 || lineReceived == planTotal) {
            receiveStatus = "OK";
        } else if (lineReceived > planTotal) {
            receiveStatus = "OVER";
            receiveWarning = "Received (" + lineReceived + ") exceeds planned (" + planTotal + "). Surplus accepted.";
        } else {
            receiveStatus = "SHORT";
            receiveWarning = "Received (" + lineReceived + ") is below planned (" + planTotal + ").";
        }

        // --- Determine line status: 209 = exact, 205 = accepted discrepancy, 300 = in-progress ---
        String lineStatus;
        if (planTotal > 0 && lineReceived == planTotal) {
            lineStatus = STATUS_INSPECTED; // "209" exact match
        } else if (planTotal > 0 && lineReceived > planTotal) {
            lineStatus = "205"; // over-receive allowed
        } else {
            lineStatus = STATUS_RECEIVING; // "300" still in progress
        }

        // --- Update AVD line status ---
        log.info("[processItemScan] STEP: updateDetailStatus lineStatus={}", lineStatus);
        inspRepo.updateDetailStatus(cpny, whs, cust, avNum, lineNum, lineStatus, userId);
        log.info("[processItemScan] STEP: updateDetailStatus DONE");

        // --- Update AVR status to match ---
        log.info("[processItemScan] STEP: updateResultStatus");
        inspRepo.updateResultStatus(cpny, whs, cust, avNum, lineNum, lineStatus, userId);
        log.info("[processItemScan] STEP: updateResultStatus DONE");

        // --- Update AVH header: promote when ALL lines complete ---
        log.info("[processItemScan] STEP: allDetailLinesComplete check");
        String headerStatus;
        boolean allLinesComplete = inspRepo.allDetailLinesComplete(cpny, whs, cust, avNum);
        if (allLinesComplete) {
            List<InspectionLineDto> allLines = inspRepo.getInspectionLines(cpny, whs, cust, avNum);
            boolean anyShort = allLines.stream().anyMatch(l -> {
                int plan = l.getPlanTotalQty() != null ? l.getPlanTotalQty() : 0;
                int recv = l.getResultTotalQty() != null ? l.getResultTotalQty() : 0;
                return plan > 0 && recv < plan;
            });
            if (anyShort && !shortageAllowed) {
                throw new ReceiveViolationException("ERR1034",
                    "Short receive is not permitted. All lines must be fully received before closing.");
            }
            boolean anyDiscrepancy = allLines.stream().anyMatch(l -> {
                int plan = l.getPlanTotalQty() != null ? l.getPlanTotalQty() : 0;
                int recv = l.getResultTotalQty() != null ? l.getResultTotalQty() : 0;
                return plan > 0 && recv != plan;
            });
            headerStatus = anyDiscrepancy ? "205" : STATUS_INSPECTED;
        } else {
            headerStatus = STATUS_RECEIVING;
        }
        log.info("[processItemScan] STEP: updateHeaderStatus headerStatus={}", headerStatus);
        inspRepo.updateHeaderStatus(cpny, whs, cust, avNum, headerStatus, userId);
        log.info("[processItemScan] STEP: updateHeaderStatus DONE");

        // --- Compute arrival-wide totals ---
        log.info("[processItemScan] STEP: getTotalReceivedQty");
        int totalReceived = inspRepo.getTotalReceivedQty(cpny, whs, cust, avNum);
        int totalPlanned  = lines.stream().mapToInt(l -> l.getPlanTotalQty() != null ? l.getPlanTotalQty() : 0).sum();

        // Build response
        ScanItemResponse res = new ScanItemResponse();
        res.setArrivalNumber(avNum);
        res.setLineNumber(lineNum);
        res.setProductCode(matched.getProductCode());
        res.setProductName(matched.getProductName());
        res.setResultTotalQty(lineReceived);
        res.setPlanTotalQty(planTotal);
        res.setVariance(lineReceived - planTotal);
        res.setLpnNumber(lpn);
        res.setHeaderStatus(headerStatus);
        res.setReceiveStatus(receiveStatus);
        res.setReceiveWarning(receiveWarning);
        res.setTotalPlannedQty(totalPlanned);
        res.setTotalReceivedQty(totalReceived);

        log.info("[processItemScan] STEP: building response, about to reload arrival");
        // Reload full arrival data for frontend sync
        InboundOrderDto refreshedHeader = orderRepo.findByArrivalNumber(cpny, whs, cust, avNum);
        if (refreshedHeader != null) {
            res.setArrival(buildArrivalDto(cpny, whs, cust, avNum, refreshedHeader));
        }

        log.info("[processItemScan] STEP: ALL DONE, returning response");
        return res;
    }

    // ---------------------------------------------------------------------------
    // Build ReceiveArrivalDto from DB (reused by loadArrival and processItemScan)
    // ---------------------------------------------------------------------------

    private ReceiveArrivalDto buildArrivalDto(String cpny, String whs, String cust,
                                               String arrivalNumber, InboundOrderDto header) {
        List<InspectionLineDto> lines = inspRepo.getInspectionLines(cpny, whs, cust, arrivalNumber);

        ReceiveArrivalDto dto = new ReceiveArrivalDto();
        dto.setArrivalNumber(header.getArrivalNumber());
        dto.setArrivalStatus(header.getArrivalStatus());
        dto.setArrivalStatusLabel(mapStatusLabel(header.getArrivalStatus()));
        dto.setScheduledDate(header.getScheduledDate());
        dto.setArrivalDate(header.getActualDate());
        dto.setSupplierCode(header.getSupplierCode());
        dto.setSupplierName(header.getSupplierName() != null ? header.getSupplierName() : header.getSupplierCode());
        dto.setPoNumber(header.getPoNumber());
        dto.setRemarks(header.getRemarks());
        dto.setLines(lines);

        int planned  = lines.stream().mapToInt(l -> l.getPlanTotalQty()   != null ? l.getPlanTotalQty()   : 0).sum();
        int received = lines.stream().mapToInt(l -> l.getResultTotalQty() != null ? l.getResultTotalQty() : 0).sum();
        dto.setTotalPlannedQty(planned);
        dto.setTotalReceivedQty(received);
        return dto;
    }

    // ---------------------------------------------------------------------------

    private String mapStatusLabel(String status) {
        if (status == null) return "";
        switch (status.trim()) {
            case "100": return "Pre";
            case "200": return "Ready for Receiving";
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
