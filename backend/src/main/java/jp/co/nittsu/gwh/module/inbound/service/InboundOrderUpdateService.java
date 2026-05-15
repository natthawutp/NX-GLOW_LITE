package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterResponse;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderUpdateRequest;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRegistrationRepository;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderValidationError;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderValidationException;
import jp.co.nittsu.gwh.security.TenantContext;
import jp.co.nittsu.gwh.security.WmsUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InboundOrderUpdateService {

    private static final Logger log = LoggerFactory.getLogger(InboundOrderUpdateService.class);
    private static final String PROGRAM_CODE = "MODERN_APINQ";

    @Autowired
    private InboundOrderRegistrationRepository registrationRepository;

    @Autowired
    private TenantContext tenantContext;

    /** Statuses that block modification (closed, completed, cancelled) */
    private static final java.util.Set<String> NON_EDITABLE_STATUSES = new java.util.HashSet<>(
            java.util.Arrays.asList("700", "809", "999"));

    /**
     * Check whether the given arrival status allows modification.
     */
    public static boolean isEditableStatus(String arrivalStatus) {
        return arrivalStatus != null && !NON_EDITABLE_STATUSES.contains(arrivalStatus.trim());
    }

    @Transactional
    public InboundOrderRegisterResponse update(InboundOrderUpdateRequest request) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();
        String userCode = resolveCurrentUserCode();

        log.info("[update] arrivalNo={} cpny={} whs={} cust={}", request.getArrivalNumber(), cpny, whs, cust);

        // 0. Status check — block modification for closed/completed/cancelled orders
        String currentStatus = registrationRepository.findCurrentStatus(cpny, whs, cust, request.getArrivalNumber());
        if (currentStatus == null) {
            throw new InboundOrderValidationException(
                Collections.singletonList(new InboundOrderValidationError(
                    "ERR1504", "Arrival order not found or has been deleted.", null)));
        }
        if (!isEditableStatus(currentStatus)) {
            throw new InboundOrderValidationException(
                Collections.singletonList(new InboundOrderValidationError(
                    "ERR1503", "Cannot modify arrival order in status " + currentStatus + ". Modification is not allowed for Closed, Completed, or Cancelled orders.", null)));
        }

        // 1. Optimistic lock check
        if (!registrationRepository.checkOptimisticLock(
                cpny, whs, cust, request.getArrivalNumber(), request.getUpdTimestamp())) {
            throw new InboundOrderValidationException(
                Collections.singletonList(new InboundOrderValidationError(
                    "ERR0285", "Record has been modified by another user. Please reload and try again.", null)));
        }

        // 2. Basic validation
        validateBasicFields(request);

        // 3. Update header
        registrationRepository.updateHeader(
                cpny, whs, cust, request.getArrivalNumber(),
                request.getHeader(), userCode, PROGRAM_CODE);

        // 4. Merge (upsert) new lines, then soft-delete any lines no longer in the set
        List<InboundOrderRegisterRequest.Line> lines = request.getLines();
        if (lines != null && !lines.isEmpty()) {
            registrationRepository.mergeDetailLines(
                    cpny, whs, cust, request.getArrivalNumber(),
                    lines, userCode, PROGRAM_CODE);

            int idx = 1;
            List<java.math.BigDecimal> keptLineNumbers = new ArrayList<>();
            for (InboundOrderRegisterRequest.Line line : lines) {
                keptLineNumbers.add(line.getArrivalLineNumber() != null
                    ? line.getArrivalLineNumber() : java.math.BigDecimal.valueOf(idx));
                idx++;
            }
            registrationRepository.softDeleteRemovedLines(
                    cpny, whs, cust, request.getArrivalNumber(),
                    keptLineNumbers, userCode, PROGRAM_CODE);
        } else {
            registrationRepository.softDeleteDetailLines(
                    cpny, whs, cust, request.getArrivalNumber(), userCode, PROGRAM_CODE);
        }

        // 5. Build response
        InboundOrderRegisterResponse response = new InboundOrderRegisterResponse();
        response.setArrivalNumber(request.getArrivalNumber());
        response.setSavedLines(lines != null ? lines.size() : 0);

        log.info("[update] completed arrivalNo={} savedLines={}", request.getArrivalNumber(), response.getSavedLines());
        return response;
    }

    private void validateBasicFields(InboundOrderUpdateRequest request) {
        List<InboundOrderValidationError> errors = new ArrayList<>();

        if (request.getHeader() == null) {
            errors.add(new InboundOrderValidationError("ERR0245", "Header data is required", null));
            throw new InboundOrderValidationException(errors);
        }

        if (request.getHeader().getScheduledDate() == null) {
            errors.add(new InboundOrderValidationError("ERR0245", "Scheduled date is required", null));
        }

        if (request.getHeader().getSupplierCode() == null || request.getHeader().getSupplierCode().trim().isEmpty()) {
            errors.add(new InboundOrderValidationError("ERR0245", "Supplier code is required", null));
        }

        if (request.getLines() == null || request.getLines().isEmpty()) {
            errors.add(new InboundOrderValidationError("ERR0245", "At least one detail line is required", null));
        } else {
            int lineIdx = 1;
            for (InboundOrderRegisterRequest.Line line : request.getLines()) {
                if (line.getProductCode() == null || line.getProductCode().trim().isEmpty()) {
                    errors.add(new InboundOrderValidationError("ERR0245", "Product code is required", lineIdx));
                }
                lineIdx++;
            }
        }

        if (!errors.isEmpty()) {
            throw new InboundOrderValidationException(errors);
        }
    }

    private String resolveCurrentUserCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return "SYSTEM";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof WmsUserPrincipal) {
            WmsUserPrincipal userPrincipal = (WmsUserPrincipal) principal;
            return userPrincipal.getEmail();
        }
        return "SYSTEM";
    }
}
