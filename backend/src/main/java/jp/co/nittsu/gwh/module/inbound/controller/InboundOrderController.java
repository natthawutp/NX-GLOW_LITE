package jp.co.nittsu.gwh.module.inbound.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDetailDto;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderDto;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterRequest;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderRegisterResponse;
import jp.co.nittsu.gwh.module.inbound.dto.InboundOrderUpdateRequest;
import jp.co.nittsu.gwh.module.inbound.repository.InboundOrderRepository.InboundOrderSearchResult;
import jp.co.nittsu.gwh.module.inbound.service.InboundOrderRegistrationService;
import jp.co.nittsu.gwh.module.inbound.service.InboundOrderService;
import jp.co.nittsu.gwh.module.inbound.service.InboundOrderUpdateService;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderValidationError;
import jp.co.nittsu.gwh.module.inbound.validation.InboundOrderValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/inbound")
public class InboundOrderController {

        private static final Logger log = LoggerFactory.getLogger(InboundOrderController.class);

    @Autowired
    private InboundOrderService inboundOrderService;

        @Autowired
        private InboundOrderRegistrationService inboundOrderRegistrationService;

        @Autowired
        private InboundOrderUpdateService inboundOrderUpdateService;

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<InboundOrderDto>>> getInboundOrders(
            @RequestParam(required = false) String arrivalNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 200));

        InboundOrderSearchResult result = inboundOrderService.search(
                arrivalNo,
                status,
                supplier,
                dateFrom,
                dateTo,
                safePage,
                safeSize
        );

        return ResponseEntity.ok(ApiResponse.success(
                result.getRecords(),
                result.getTotalRecords(),
                safePage,
                safeSize
        ));
    }

        @PostMapping("/orders")
        public ResponseEntity<ApiResponse<InboundOrderRegisterResponse>> registerInboundOrder(
                        @Valid @RequestBody InboundOrderRegisterRequest request
        ) {
                try {
                        InboundOrderRegisterResponse response = inboundOrderRegistrationService.register(request);
                        return ResponseEntity.ok(ApiResponse.success(response));
                } catch (InboundOrderValidationException ex) {
                        ApiResponse<InboundOrderRegisterResponse> response = new ApiResponse<>();
                        response.setStatus("ERROR");
                        List<ApiResponse.ApiMessage> messages = new ArrayList<>();
                        for (InboundOrderValidationError error : ex.getErrors()) {
                                String messageText = error.getMessage();
                                if (error.getRowIndex() != null) {
                                        messageText = "[Row " + error.getRowIndex() + "] " + messageText;
                                }
                                messages.add(new ApiResponse.ApiMessage(error.getCode(), messageText));
                        }
                        response.setMessages(messages);
                        return ResponseEntity.badRequest().body(response);
                } catch (Exception ex) {
                        log.error("Inbound order registration failed", ex);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(ApiResponse.error("APPRE_REGISTER_FAILED", "Failed to register inbound order"));
                }
        }

    @GetMapping("/orders/{arrivalNo}")
    public ResponseEntity<ApiResponse<InboundOrderDetailDto>> getInboundOrderDetail(
            @PathVariable String arrivalNo) {
        InboundOrderDetailDto detail = inboundOrderService.getDetail(arrivalNo);
        if (detail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("NOT_FOUND", "Arrival order not found: " + arrivalNo));
        }
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @PutMapping("/orders/{arrivalNo}")
    public ResponseEntity<ApiResponse<InboundOrderRegisterResponse>> updateInboundOrder(
            @PathVariable String arrivalNo,
            @Valid @RequestBody InboundOrderUpdateRequest request) {
        try {
            request.setArrivalNumber(arrivalNo);
            InboundOrderRegisterResponse response = inboundOrderUpdateService.update(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (InboundOrderValidationException ex) {
            ApiResponse<InboundOrderRegisterResponse> response = new ApiResponse<>();
            response.setStatus("ERROR");
            List<ApiResponse.ApiMessage> messages = new ArrayList<>();
            for (InboundOrderValidationError error : ex.getErrors()) {
                String messageText = error.getMessage();
                if (error.getRowIndex() != null) {
                    messageText = "[Row " + error.getRowIndex() + "] " + messageText;
                }
                messages.add(new ApiResponse.ApiMessage(error.getCode(), messageText));
            }
            response.setMessages(messages);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            log.error("Inbound order update failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("APINQ_UPDATE_FAILED", "Failed to update inbound order"));
        }
    }
}
