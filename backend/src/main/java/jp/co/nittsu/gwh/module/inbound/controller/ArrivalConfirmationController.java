package jp.co.nittsu.gwh.module.inbound.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmRequest;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationConfirmResponse;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationDto;
import jp.co.nittsu.gwh.module.inbound.dto.ArrivalConfirmationSearchRequest;
import jp.co.nittsu.gwh.module.inbound.repository.ArrivalConfirmationRepository.SearchResult;
import jp.co.nittsu.gwh.module.inbound.service.ArrivalConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/inbound/apcnf")
public class ArrivalConfirmationController {

    @Autowired
    private ArrivalConfirmationService arrivalConfirmationService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<java.util.List<ArrivalConfirmationDto>>> search(
        @RequestBody(required = false) ArrivalConfirmationSearchRequest request
    ) {
        ArrivalConfirmationSearchRequest effectiveRequest = request != null ? request : new ArrivalConfirmationSearchRequest();
        SearchResult result = arrivalConfirmationService.search(effectiveRequest);
        return ResponseEntity.ok(ApiResponse.success(
            result.getRecords(),
            result.getTotalRecords(),
            Math.max(effectiveRequest.getPage(), 0),
            Math.max(1, Math.min(effectiveRequest.getSize(), 200))
        ));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<ArrivalConfirmationConfirmResponse>> confirm(
        @Valid @RequestBody ArrivalConfirmationConfirmRequest request
    ) {
        try {
            ArrivalConfirmationConfirmResponse response = arrivalConfirmationService.confirm(request);
            ApiResponse<ArrivalConfirmationConfirmResponse> apiResponse = ApiResponse.success(response);
            if (!response.getNotFoundArrivalNumbers().isEmpty()) {
                apiResponse.setStatus("WARNING");
                apiResponse.addMessage("APCNF_PARTIAL", "Some arrival numbers were not found and were skipped.");
                apiResponse.getData().setNotFoundArrivalNumbers(new ArrayList<>(response.getNotFoundArrivalNumbers()));
            }
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("APCNF_CONFIRM_INVALID", ex.getMessage()));
        }
    }
}
