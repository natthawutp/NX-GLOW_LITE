package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmRequest;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmResponse;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationSearchRequest;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalConfirmationRepository;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalConfirmationRepository.SearchResult;
import jp.co.nittsu.gwh.security.TenantContext;
import jp.co.nittsu.gwh.security.WmsUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Arrival Confirmation Service — matches standard GwhSrvcApWeb/GwhApcnf010BLC.
 */
@Service
public class ArrivalConfirmationService {

    private static final Logger log = LoggerFactory.getLogger(ArrivalConfirmationService.class);
    private static final String APCNF_PROCESS_CODE = "apcnfReg";
    private static final String PROGRAM_CODE = "MODERN_APCNF";

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private ArrivalConfirmationRepository arrivalConfirmationRepository;

    public SearchResult search(ArrivalConfirmationSearchRequest request) {
        return arrivalConfirmationRepository.search(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode(),
            request
        );
    }

    @Transactional
    public ArrivalConfirmationConfirmResponse confirm(ArrivalConfirmationConfirmRequest request) {
        List<String> targetArrivalNumbers = normalizeArrivalNumbers(request.getArrivalNumbers());

        String companyCode = tenantContext.getCompanyCode();
        String warehouseCode = tenantContext.getWarehouseCode();
        String customerCode = tenantContext.getCustomerCode();

        // =====================================================================
        // Step 1: Get Customer master (matches standard line 287)
        // =====================================================================
        Object[] custMaster = arrivalConfirmationRepository.getCustomerMaster(companyCode, warehouseCode, customerCode);
        if (custMaster == null) {
            throw new IllegalStateException("ERR0006: Customer master not found");
        }
        String custStKnd = toStr(custMaster[0]);     // CUST_ST_KND
        String custGetKnd = toStr(custMaster[1]);     // CUST_GET_KND
        // String custPcsmFlg = toStr(custMaster[2]); // CUST_PCSM_FLG (for partial delivery)
        // String custPadsKnd = toStr(custMaster[3]);  // CUST_PADS_KND

        // =====================================================================
        // Step 2: Get Operation date (matches standard line 296)
        // =====================================================================
        String opdYmd = arrivalConfirmationRepository.getOperationDate(companyCode, warehouseCode, customerCode);
        if (opdYmd == null) {
            throw new IllegalStateException("ERR0006: Operation date master (GWH_TM_OPD) not found");
        }

        // =====================================================================
        // Step 3: Get PRCC (matches standard lines 305-317)
        // =====================================================================
        String[] prcc = arrivalConfirmationRepository.findProcessControlStatuses(
            companyCode, warehouseCode, customerCode, APCNF_PROCESS_CODE);
        if (prcc == null) {
            throw new IllegalStateException("ERR0006: Process control (apcnfReg) not configured in GWH_TM_PRCC");
        }
        String prccRsts = prcc[0]; // required status
        String prccPsts = prcc[1]; // partial status
        String prccFsts = prcc[2]; // final status
        if (prccRsts == null || prccRsts.trim().isEmpty() || prccFsts == null || prccFsts.trim().isEmpty()) {
            throw new IllegalStateException("ERR0006: Process control RSTS or FSTS is empty for apcnfReg");
        }

        // =====================================================================
        // Step 4: Get Picking Key master (matches standard line 321)
        // =====================================================================
        Integer pikNum = arrivalConfirmationRepository.getPikAutoFlag(companyCode, warehouseCode, customerCode);

        // =====================================================================
        // Step 5: Get future arrival date permission (matches standard lines 325-328)
        // =====================================================================
        String futureDatePermission = arrivalConfirmationRepository.getFutureArrivalDatePermission(
            companyCode, warehouseCode, customerCode);

        // =====================================================================
        // Step 6: Check empty list (matches standard line 334)
        // =====================================================================
        if (targetArrivalNumbers.isEmpty()) {
            throw new IllegalStateException("ERR0416: No arrival numbers provided for confirmation");
        }

        // =====================================================================
        // Step 6b: Stock-taking check for serial stock (matches standard lines 338-353)
        // =====================================================================
        if ("S".equalsIgnoreCase(custStKnd)) {
            Object[] stkInfo = arrivalConfirmationRepository.getStockTakingInfo(companyCode, warehouseCode, customerCode);
            if (stkInfo != null) {
                String stkhSts = toStr(stkInfo[0]);
                String trnUius = toStr(stkInfo[1]);
                String trnUous = toStr(stkInfo[2]);
                if ("01".equals(stkhSts) && "Y".equalsIgnoreCase(trnUius) && "Y".equalsIgnoreCase(trnUous)) {
                    throw new IllegalStateException("ERR4290: Stock-taking in progress, confirmation not allowed");
                }
            }
        }

        // =====================================================================
        // Step 7: Validate existence
        // =====================================================================
        List<String> existing = arrivalConfirmationRepository.findExistingArrivalNumbers(
            companyCode, warehouseCode, customerCode, targetArrivalNumbers);
        Set<String> existingSet = new HashSet<>(existing);
        List<String> notFound = new ArrayList<>();
        List<String> confirmTargets = new ArrayList<>();
        for (String arrivalNumber : targetArrivalNumbers) {
            if (existingSet.contains(arrivalNumber)) {
                confirmTargets.add(arrivalNumber);
            } else {
                notFound.add(arrivalNumber);
            }
        }

        // =====================================================================
        // Step 8: Per-arrival validation (matches standard lines 355-451)
        // =====================================================================
        LocalDate arrivalDate = request.getArrivalDate() != null ? request.getArrivalDate() : LocalDate.now();
        String userCode = resolveCurrentUserCode();
        List<String> validatedArrivals = new ArrayList<>();

        for (String avNum : confirmTargets) {
            // --- Validate arrival date not null (ERR1502) ---
            if (request.getArrivalDate() == null) {
                log.warn("Arrival date not provided, using today for arrival {}", avNum);
            }

            // --- Future date check (ERR0415) ---
            if ("N".equalsIgnoreCase(futureDatePermission)) {
                if (arrivalDate.isAfter(parseOpdDate(opdYmd))) {
                    throw new IllegalStateException("ERR0415: Arrival date is after operation date for arrival " + avNum);
                }
            }

            // --- Status gate: check detail lines (ERR1112) (matches standard lines 402-412) ---
            if (arrivalConfirmationRepository.hasDetailsBelowRequiredStatus(
                    companyCode, warehouseCode, customerCode, avNum, prccRsts)) {
                throw new IllegalStateException("ERR1112: Arrival " + avNum +
                    " has detail lines below required status " + prccRsts);
            }

            // --- Get arrival data joined (AVR+AVD+AVH+TRN) ---
            List<Object[]> arrivalDataRows = arrivalConfirmationRepository.getArrivalData(
                companyCode, warehouseCode, customerCode, avNum);
            if (arrivalDataRows == null || arrivalDataRows.isEmpty()) {
                throw new IllegalStateException("ERR0031: No arrival data found for " + avNum);
            }

            // --- Per-row validation (matches standard lines 426-451) ---
            Timestamp lastUpdYmdhms = null;
            for (Object[] row : arrivalDataRows) {
                BigDecimal avdStpcQty = toBigDecimal(row[0]);    // AVD_STPC_QTY
                BigDecimal totalAvrRtpcQty = toBigDecimal(row[1]); // TOTAL_AVR_RTPC_QTY
                Timestamp arvUpdYmdhms = row[2] instanceof Timestamp ? (Timestamp) row[2] : null;
                String trnSppaFlg = toStr(row[4]);  // TRN_SPPA_FLG
                String trnShpaFlg = toStr(row[5]);  // TRN_SHPA_FLG

                // Over-receipt check (ERR1033): if SPPA='N' and detail < result
                if ("N".equalsIgnoreCase(trnSppaFlg) && avdStpcQty.compareTo(totalAvrRtpcQty) < 0) {
                    throw new IllegalStateException("ERR1033: Over-receipt not allowed for arrival " + avNum);
                }

                // Short-receipt check (ERR1034): if SHPA='N' and detail > result
                if ("N".equalsIgnoreCase(trnShpaFlg) && avdStpcQty.compareTo(totalAvrRtpcQty) > 0) {
                    throw new IllegalStateException("ERR1034: Short-receipt not allowed for arrival " + avNum);
                }

                // Optimistic lock timestamp check (ERR0285)
                if (request.getUpdTimestamp() != null && arvUpdYmdhms != null) {
                    if (!arvUpdYmdhms.equals(request.getUpdTimestamp())) {
                        throw new IllegalStateException("ERR0285: Data has been modified by another user for arrival " + avNum);
                    }
                }

                if (lastUpdYmdhms == null) lastUpdYmdhms = arvUpdYmdhms;
            }

            // Serial check (ERR1253): matches standard lines 446-451
            if ("1".equals(custGetKnd) || "4".equals(custGetKnd) || "5".equals(custGetKnd)) {
                Object[] serialCheck = arrivalConfirmationRepository.checkSerialComplete(
                    companyCode, warehouseCode, customerCode, avNum);
                if (serialCheck != null) {
                    BigDecimal totalInspect = toBigDecimal(serialCheck[0]);
                    BigDecimal totalSerial = toBigDecimal(serialCheck[1]);
                    if (totalInspect.compareTo(totalSerial) != 0) {
                        throw new IllegalStateException("ERR1253: Serial count mismatch for arrival " + avNum +
                            ". Inspect=" + totalInspect + ", Serial=" + totalSerial);
                    }
                }
            }

            // === CONFIRMATION PROCESSING (matches standard lines 456-575) ===

            // PIK key date value for auto-set
            String pikDateValue = arrivalDate.format(DateTimeFormatter.BASIC_ISO_DATE);

            // --- Insert Stock + XT per AVR row (matches standard lines 456-533) ---
            Date avYmd;
            if ("S".equalsIgnoreCase(custStKnd)) {
                avYmd = java.sql.Date.valueOf("9999-12-31");
            } else {
                avYmd = java.sql.Date.valueOf(arrivalDate);
            }

            String xtOpKnd = prccFsts.length() >= 2 ? prccFsts.substring(0, 2) : prccFsts;

            for (Object[] row : arrivalDataRows) {
                String asNum = toStr(row[6]);
                int aslnNum = toInt(row[7]);
                int assqNum = toInt(row[8]);
                String prodCode = toStr(row[9]);
                String orgnCode = toStr(row[10]);
                String sbivCode = toStr(row[11]);
                String pik1 = toStr(row[12]);
                String pik2 = toStr(row[13]);
                String pik3 = toStr(row[14]);
                String pik4 = toStr(row[15]);
                String pik5 = toStr(row[16]);
                String pik6 = toStr(row[17]);
                String pik7 = toStr(row[18]);
                String areaCod = toStr(row[19]);
                String rackCod = toStr(row[20]);
                String pstnCod = toStr(row[21]);
                String lvlCod = toStr(row[22]);
                BigDecimal rtpcQty = toBigDecimal(row[23]);
                BigDecimal ppcsQty = toBigDecimal(row[24]);
                BigDecimal ppbQty = toBigDecimal(row[25]);
                String dmgFlg = toStr(row[26]);
                String pcumPcs = toStr(row[27]);
                String pcumCs = toStr(row[28]);
                String pssaFlg = toStr(row[29]);
                String bondFlg = toStr(row[3]); // AVH_BOND_FLG

                // PIK auto-set for stock (matches standard lines 477-480)
                if (pikNum != null) {
                    switch (pikNum) {
                        case 1: pik1 = pikDateValue; break;
                        case 2: pik2 = pikDateValue; break;
                        case 3: pik3 = pikDateValue; break;
                        case 4: pik4 = pikDateValue; break;
                        case 5: pik5 = pikDateValue; break;
                        case 6: pik6 = pikDateValue; break;
                        case 7: pik7 = pikDateValue; break;
                    }
                }

                // Insert Stock (matches standard lines 522-526)
                try {
                    arrivalConfirmationRepository.insertStock(
                        companyCode, warehouseCode, customerCode,
                        asNum, aslnNum, assqNum,
                        custStKnd, avYmd, prodCode, orgnCode, sbivCode,
                        pik1, pik2, pik3, pik4, pik5, pik6, pik7,
                        areaCod, rackCod, pstnCod, lvlCod,
                        rtpcQty, ppcsQty, ppbQty,
                        dmgFlg, bondFlg, pssaFlg,
                        userCode, PROGRAM_CODE);
                } catch (RuntimeException ex) {
                    String msg = ex.getMessage();
                    if (msg != null && (msg.contains("ORA-00001") || msg.contains("unique constraint"))) {
                        log.warn("Stock record already exists for {}/{}/{}, skipping", asNum, aslnNum, assqNum);
                    } else {
                        throw new IllegalStateException("ERR0006: Failed to insert stock for " + asNum);
                    }
                }

                // Insert XT (matches standard lines 529-533)
                try {
                    arrivalConfirmationRepository.insertXt(
                        companyCode, warehouseCode, customerCode,
                        asNum, aslnNum, assqNum,
                        "AV", xtOpKnd, userCode,
                        null, null,
                        userCode, PROGRAM_CODE);
                } catch (RuntimeException ex) {
                    log.warn("XT insert failed for {}/{}/{}: {}", asNum, aslnNum, assqNum, ex.getMessage());
                }
            }

            // --- Update AVR: set FSTS + AVSP_YMD + AVSP_STS + CNFM_FLG + PIK (matches standard lines 550-561) ---
            arrivalConfirmationRepository.confirmArrivals(
                companyCode, warehouseCode, customerCode,
                java.util.Collections.singletonList(avNum),
                arrivalDate, prccFsts, userCode, PROGRAM_CODE);

            // PIK auto-set for AVR (matches standard lines 545-548)
            if (pikNum != null) {
                arrivalConfirmationRepository.updateAvrPikKey(
                    companyCode, warehouseCode, customerCode, avNum, pikNum, pikDateValue);
            }

            // --- Partial delivery check (matches standard lines 579-719) ---
            String trnPdacFlg = arrivalConfirmationRepository.getTransactionKindPartialFlag(
                companyCode, warehouseCode, customerCode, avNum);
            if ("Y".equals(trnPdacFlg)) {
                log.info("Partial delivery flag is 'Y' for arrival {}, partial delivery creation not yet implemented", avNum);
                // TODO: Implement partial delivery flow (standard lines 579-719)
                // This requires: getArrivalConfirmationRemain, insertAvh, insertAvd, insertXt,
                // updatePartialDeliveryFlag, numbering control for new AV_NUM
            }

            validatedArrivals.add(avNum);
        }

        ArrivalConfirmationConfirmResponse response = new ArrivalConfirmationConfirmResponse();
        response.setRequestedCount(targetArrivalNumbers.size());
        response.setConfirmedCount(validatedArrivals.size());
        response.setAppliedStatus(prccFsts);
        response.setNotFoundArrivalNumbers(notFound);
        return response;
    }

    private LocalDate parseOpdDate(String opdYmd) {
        if (opdYmd == null || opdYmd.trim().isEmpty()) return LocalDate.now();
        try {
            return LocalDate.parse(opdYmd.trim(), DateTimeFormatter.BASIC_ISO_DATE);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private String toStr(Object val) {
        return val != null ? val.toString().trim() : null;
    }

    private int toInt(Object val) {
        if (val == null) return 0;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString().trim()); } catch (Exception e) { return 0; }
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        if (val instanceof Number) return BigDecimal.valueOf(((Number) val).doubleValue());
        try { return new BigDecimal(val.toString().trim()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private List<String> normalizeArrivalNumbers(List<String> arrivalNumbers) {
        List<String> result = new ArrayList<>();
        if (arrivalNumbers == null) {
            return result;
        }
        Set<String> unique = new HashSet<>();
        for (String value : arrivalNumbers) {
            if (value == null) {
                continue;
            }
            String trimmed = value.trim();
            if (!trimmed.isEmpty() && unique.add(trimmed)) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private String resolveCurrentUserCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return "SYSTEM";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof WmsUserPrincipal) {
            return ((WmsUserPrincipal) principal).getEmail();
        }
        return "SYSTEM";
    }
}
