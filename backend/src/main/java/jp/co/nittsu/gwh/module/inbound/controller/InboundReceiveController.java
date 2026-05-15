package jp.co.nittsu.gwh.module.inbound.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.inbound.dto.ReceiveArrivalDto;
import jp.co.nittsu.gwh.module.inbound.dto.ScanItemRequest;
import jp.co.nittsu.gwh.module.inbound.dto.ScanItemResponse;
import jp.co.nittsu.gwh.module.inbound.service.InboundReceiveService;
import jp.co.nittsu.gwh.module.inbound.service.ReceiveViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller for the real-time scan-receiving flow.
 *
 * Base path: /api/v1/inbound/receive
 *
 * GET  /{arrivalNo}  — load arrival header + detail lines after scanning arrival number
 * POST /scan         — process one item scan; immediately persists AVR + updates AVD/AVH
 */
@RestController
@RequestMapping("/api/v1/inbound/receive")
public class InboundReceiveController {

    private static final Logger log = LoggerFactory.getLogger(InboundReceiveController.class);

    @Autowired
    private InboundReceiveService receiveService;

    /**
     * Load arrival header and all detail lines for a scanned arrival number.
     * Called once when the operator scans (or types) the arrival barcode.
     */
    @GetMapping("/{arrivalNo}")
    public ResponseEntity<ApiResponse<ReceiveArrivalDto>> loadArrival(
            @PathVariable String arrivalNo) {
        try {
            ReceiveArrivalDto dto = receiveService.loadArrival(arrivalNo);
            return ResponseEntity.ok(ApiResponse.success(dto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("RECEIVE_NOT_FOUND", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Failed to load arrival {}", arrivalNo, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("RECEIVE_LOAD_FAILED", "Failed to load arrival"));
        }
    }

    /**
     * Process a single item scan.
     * Each call immediately INSERTs or ACCUMULATEs one AVR result row and
     * updates AVD/AVH status to Receiving (300).
     */
    @PostMapping("/scan")
    public ResponseEntity<ApiResponse<ScanItemResponse>> scan(
            @Valid @RequestBody ScanItemRequest request) {
        try {
            ScanItemResponse response = receiveService.processItemScan(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("RECEIVE_SCAN_NOT_FOUND", ex.getMessage()));
        } catch (ReceiveViolationException ex) {
            log.warn("Receive violation [{}] for arrival {} product {}: {}",
                    ex.getErrorCode(), request.getArrivalNumber(), request.getProductCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
        } catch (Exception ex) {
            log.error("Scan failed for arrival {} product {}: {}",
                    request.getArrivalNumber(), request.getProductCode(), ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("RECEIVE_SCAN_FAILED", ex.getClass().getSimpleName() + ": " + ex.getMessage()));
        }
    }
}
