package jp.co.nittsu.gwh.module.lpn.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.lpn.dto.*;
import jp.co.nittsu.gwh.module.lpn.repository.LpnRepository.LpnSearchResult;
import jp.co.nittsu.gwh.module.lpn.service.LpnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lpn")
public class LpnController {

    private static final Logger log = LoggerFactory.getLogger(LpnController.class);

    @Autowired
    private LpnService lpnService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LpnDto>>> searchLpns(
            @RequestParam(required = false) String lpnNumber,
            @RequestParam(required = false) String lpnRfNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String lpnType,
            @RequestParam(required = false) String locationCode,
            @RequestParam(required = false) String arrivalNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 200));

        LpnSearchResult result = lpnService.search(
            lpnNumber, lpnRfNumber, status, lpnType, locationCode, arrivalNumber,
            safePage, safeSize
        );

        return ResponseEntity.ok(ApiResponse.success(
            result.getRecords(),
            result.getTotalRecords(),
            safePage,
            safeSize
        ));
    }

    @GetMapping("/validate-barcode")
    public ResponseEntity<ApiResponse<Boolean>> validateBarcode(@RequestParam String barcode) {
        boolean exists = lpnService.isBarcodeAlreadyUsed(barcode);
        if (exists) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("LPN_BARCODE_DUPLICATE", "LPN Number already scanned."));
        }
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @GetMapping("/{lpnNumber}")
    public ResponseEntity<ApiResponse<LpnDto>> getLpnDetail(@PathVariable String lpnNumber) {
        LpnDto lpn = lpnService.getDetail(lpnNumber);
        if (lpn == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("LPN_NOT_FOUND", "LPN '" + lpnNumber + "' not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(lpn));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LpnDto>> createLpn(
            @RequestBody LpnCreateRequest request, Principal principal) {
        try {
            String user = principal != null ? principal.getName() : "SYSTEM";
            LpnDto result = lpnService.create(request, user);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("LPN_VALIDATION", ex.getMessage()));
        } catch (Exception ex) {
            log.error("LPN creation failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LPN_CREATE_FAILED", "Failed to create LPN"));
        }
    }

    @PostMapping("/receive")
    public ResponseEntity<ApiResponse<LpnDto>> receiveLpn(
            @RequestBody LpnReceiveRequest request, Principal principal) {
        try {
            String user = principal != null ? principal.getName() : "SYSTEM";
            LpnDto result = lpnService.receive(request, user);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("LPN_VALIDATION", ex.getMessage()));
        } catch (Exception ex) {
            log.error("LPN receive failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LPN_RECEIVE_FAILED", "Failed to receive LPN"));
        }
    }

    @PostMapping("/putaway")
    public ResponseEntity<ApiResponse<LpnDto>> putawayLpn(
            @RequestBody LpnPutawayRequest request, Principal principal) {
        try {
            String user = principal != null ? principal.getName() : "SYSTEM";
            LpnDto result = lpnService.putaway(request, user);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("LPN_VALIDATION", ex.getMessage()));
        } catch (Exception ex) {
            log.error("LPN putaway failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LPN_PUTAWAY_FAILED", "Failed to putaway LPN"));
        }
    }

    @DeleteMapping("/{lpnNumber}")
    public ResponseEntity<ApiResponse<Void>> removeLpn(
            @PathVariable String lpnNumber, Principal principal) {
        try {
            String user = principal != null ? principal.getName() : "SYSTEM";
            lpnService.removeLpn(lpnNumber, user);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("LPN_REMOVE_BLOCKED", ex.getMessage()));
        } catch (Exception ex) {
            log.error("LPN remove failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LPN_REMOVE_FAILED", "Failed to remove LPN"));
        }
    }

    @PutMapping("/{lpnNumber}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable String lpnNumber,
            @RequestParam String status,
            Principal principal) {
        try {
            String user = principal != null ? principal.getName() : "SYSTEM";
            lpnService.updateStatus(lpnNumber, status, user);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception ex) {
            log.error("LPN status update failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LPN_STATUS_FAILED", "Failed to update LPN status"));
        }
    }
}
