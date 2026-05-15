package jp.co.nittsu.gwh.module.inventory.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.inventory.dto.StockSearchRequest;
import jp.co.nittsu.gwh.module.inventory.dto.StockSearchResult;
import jp.co.nittsu.gwh.module.inventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Stock inquiry REST controller.
 * Endpoint: GET /api/v1/inventory/stocks
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stocks")
    public ResponseEntity<ApiResponse<StockSearchResult>> getStocks(
            @ModelAttribute StockSearchRequest request) {

        // Safety bounds
        int safePage = Math.max(request.getPage(), 0);
        int safeSize = Math.max(1, Math.min(request.getSize(), 200));
        request.setPage(safePage);
        request.setSize(safeSize);

        StockSearchResult result = stockService.search(request);

        ApiResponse<StockSearchResult> response = new ApiResponse<>();
        response.setStatus("SUCCESS");
        response.setData(result);
        response.setTotalRecords(result.getTotalRecords());
        response.setPage(safePage);
        response.setSize(safeSize);

        // Add warning message if present
        if (result.getWarningCode() != null) {
            if ("ERR0174".equals(result.getWarningCode())) {
                response.setStatus("WARNING");
            }
            response.addMessage(result.getWarningCode(), result.getWarningMessage());
        }

        return ResponseEntity.ok(response);
    }
}
