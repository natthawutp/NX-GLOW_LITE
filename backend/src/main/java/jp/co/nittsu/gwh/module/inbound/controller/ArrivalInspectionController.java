package jp.co.nittsu.gwh.module.inbound.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalInspectionListDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionCancelRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionLineDto;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionRegisterRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InspectionRegisterResponse;
import jp.co.nittsu.gwh.module.inbound.service.ArrivalInspectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for the Arrival Inspection feature.
 *
 * Migrated from legacy GWH_Git apins screens 010 / 020 / 040.
 * LPN is integrated: inspection results carry optional lpnNumber/lpnType
 * which are created or updated in VGWH_TJ_LPN during registration.
 *
 * Base path: /api/v1/inbound/inspection
 */
@RestController
@RequestMapping("/api/v1/inbound/inspection")
public class ArrivalInspectionController {

    private static final Logger log = LoggerFactory.getLogger(ArrivalInspectionController.class);

    @Autowired
    private ArrivalInspectionService inspectionService;

    /**
     * GET /api/v1/inbound/inspection
     * Search arrivals eligible for inspection (status 100/200/205/300).
     * Equivalent to legacy apins Screen 010 — gwhApins010Get.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ArrivalInspectionListDto>>> search(
            @RequestParam(required = false) String arrivalNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        List<ArrivalInspectionListDto> results =
                inspectionService.search(arrivalNo, status, dateFrom, dateTo);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * GET /api/v1/inbound/inspection/{arrivalNo}/lines
     * Retrieve detail lines for one arrival, pre-filled with any existing
     * inspection results and recommended storage location.
     * Equivalent to legacy apins Screen 020 — gwhApins020Get.
     */
    @GetMapping("/{arrivalNo}/lines")
    public ResponseEntity<ApiResponse<List<InspectionLineDto>>> getLines(
            @PathVariable String arrivalNo
    ) {
        List<InspectionLineDto> lines = inspectionService.getLines(arrivalNo);
        return ResponseEntity.ok(ApiResponse.success(lines));
    }

    /**
     * POST /api/v1/inbound/inspection/register
     * Register inspection results for one arrival.
     * Each line may carry an LPN number; the service creates/updates the LPN record.
     * Equivalent to legacy apins Screen 020 — gwhApins020Reg / gwhApins010Reg (collective).
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<InspectionRegisterResponse>> register(
            @Valid @RequestBody InspectionRegisterRequest request
    ) {
        try {
            InspectionRegisterResponse response = inspectionService.register(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("APINS_VALIDATION_ERROR", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Inspection registration failed for arrival {}", request.getArrivalNumber(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("APINS_REGISTER_FAILED", "Inspection registration failed"));
        }
    }

    /**
     * POST /api/v1/inbound/inspection/cancel
     * Cancel a completed inspection: logical-deletes AVR records, resets AVD/AVH status to 200.
     * Equivalent to legacy apins Screen 040 — gwhApins040Reg.
     */
    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @Valid @RequestBody InspectionCancelRequest request
    ) {
        try {
            inspectionService.cancel(request);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("APINS_CANCEL_VALIDATION", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Inspection cancel failed for arrival {}", request.getArrivalNumber(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("APINS_CANCEL_FAILED", "Inspection cancel failed"));
        }
    }
}
