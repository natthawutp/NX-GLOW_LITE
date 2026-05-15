package jp.co.nittsu.gwh.module.inbound.service;

import jp.co.nittsu.gwh.common.utils.data.GwhNumberingDataUtils;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterResponse;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRegistrationRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import jp.co.nittsu.gwh.security.WmsUserPrincipal;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderLegacyRuleValidator;
import java.util.Collections;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderValidationError;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InboundOrderRegistrationService {

    private static final String PROGRAM_CODE = "MODERN_APPRE";
    private static final String INBOUND_REG_PROCESS_CODE = "appreReg";
    private static final String ARRIVAL_NUM_CODE = "AV_NUM";

    @Autowired
    private InboundOrderRegistrationRepository registrationRepository;

    @Autowired
    private GwhNumberingDataUtils gwhNumberingDataUtils;

    @Autowired
    private TenantContext tenantContext;

    @Autowired
    private InboundOrderLegacyRuleValidator legacyRuleValidator;

    @Transactional
    public InboundOrderRegisterResponse register(InboundOrderRegisterRequest request) {
        String companyCode = tenantContext.getCompanyCode();
        String warehouseCode = tenantContext.getWarehouseCode();
        String customerCode = tenantContext.getCustomerCode();
        String userCode = resolveCurrentUserCode();
        String finalStatus = normalizeStatus(
            registrationRepository.getProcessFinalStatus(
                companyCode,
                warehouseCode,
                customerCode,
                INBOUND_REG_PROCESS_CODE
            )
        );

        legacyRuleValidator.validateOrThrow(
            request,
            companyCode,
            warehouseCode,
            customerCode,
            registrationRepository
        );

        String arrivalNumber = resolveUniqueArrivalNumber(companyCode, warehouseCode, customerCode);
        registrationRepository.insertHeader(
                companyCode,
                warehouseCode,
                customerCode,
                arrivalNumber,
            withFinalStatus(request, finalStatus),
                userCode,
                PROGRAM_CODE
        );
        registrationRepository.insertLines(
                companyCode,
                warehouseCode,
                customerCode,
                arrivalNumber,
                request.getLines(),
                userCode,
                PROGRAM_CODE
        );

            int milestoneCaptured = registrationRepository.insertMilestones(
                companyCode,
                warehouseCode,
                customerCode,
                arrivalNumber,
                finalStatus,
                request.getLines(),
                userCode,
                PROGRAM_CODE
            );

            int productivityCaptured = registrationRepository.insertWarehouseProductivity(
                companyCode,
                warehouseCode,
                customerCode,
                arrivalNumber,
                finalStatus,
                request.getLines().size(),
                userCode,
                PROGRAM_CODE
            );

        InboundOrderRegisterResponse response = new InboundOrderRegisterResponse();
        response.setArrivalNumber(arrivalNumber);
            response.setArrivalStatus(finalStatus);
        response.setSavedLines(request.getLines().size());
            response.setMilestoneCaptured(milestoneCaptured);
            response.setProductivityCaptured(productivityCaptured);
        return response;
    }

            private InboundOrderRegisterRequest.Header withFinalStatus(InboundOrderRegisterRequest request, String finalStatus) {
            InboundOrderRegisterRequest.Header header = request.getHeader();
            header.setArrivalStatus(finalStatus);
            return header;
            }

    /**
     * Generates a unique arrival number, auto-advancing past any numbers that
     * already exist in GWH_TJ_AV_H (handles sequence wrap-around gracefully,
     * mirroring legacy GwhCmnumBLC behaviour).
     */
    private String resolveUniqueArrivalNumber(String companyCode, String warehouseCode, String customerCode) {
        for (int attempt = 0; attempt < 10; attempt++) {
            String candidate = generateArrivalNumber(companyCode, warehouseCode, customerCode);
            if (!registrationRepository.existsArrivalNumber(companyCode, warehouseCode, customerCode, candidate)) {
                return candidate;
            }
        }
        throw new InboundOrderValidationException(Collections.singletonList(
            new InboundOrderValidationError("ERR1013",
                "Could not generate a unique arrival number after 10 attempts. " +
                "The sequence may be exhausted. Please contact the system administrator.", null)
        ));
    }

    private String generateArrivalNumber(String companyCode, String warehouseCode, String customerCode) {
        // Mirrored legacy GwhAppre010BLC: gwhNumberingDataUtils.getControlNumber
        return gwhNumberingDataUtils.getControlNumber(companyCode, warehouseCode, customerCode, ARRIVAL_NUM_CODE);
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

    private String normalizeStatus(String status) {
        return status == null || status.trim().isEmpty() ? "100" : status.trim();
    }

}