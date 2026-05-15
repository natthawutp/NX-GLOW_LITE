package jp.co.nittsu.gwh.module.inventory.service;

import jp.co.nittsu.gwh.module.inventory.dto.StockDto;
import jp.co.nittsu.gwh.module.inventory.dto.StockSearchRequest;
import jp.co.nittsu.gwh.module.inventory.dto.StockSearchResult;
import jp.co.nittsu.gwh.module.inventory.repository.StockRepository;
import jp.co.nittsu.gwh.module.inventory.repository.StockRepository.CustomerPrefs;
import jp.co.nittsu.gwh.module.inventory.repository.StockRepository.StockSearchResultData;
import jp.co.nittsu.gwh.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Stock inquiry business logic service.
 * Ports legacy GwhLpski020BLC logic to modern Spring Boot pattern.
 */
@Service
public class StockService {

    private static final Logger log = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TenantContext tenantContext;

    public StockSearchResult search(StockSearchRequest request) {
        String cpny = tenantContext.getCompanyCode();
        String whs = tenantContext.getWarehouseCode();
        String cust = tenantContext.getCustomerCode();

        // 1. Load customer preferences
        CustomerPrefs prefs = stockRepository.loadCustomerPrefs(cpny, whs, cust);
        boolean pieceMode = "Y".equals(prefs.getCustPcsmFlg());

        // 2. Validate maxDisplayResults
        Integer maxDisplay = request.getMaxDisplayResults();
        if (maxDisplay != null && maxDisplay == 0) {
            StockSearchResult errorResult = new StockSearchResult();
            errorResult.setRecords(Collections.<StockDto>emptyList());
            errorResult.setTotalRecords(0);
            errorResult.setPieceModeFlag(pieceMode);
            errorResult.setWarningCode("ERR0174");
            errorResult.setWarningMessage("Max display results cannot be zero");
            return errorResult;
        }

        // 3. Determine effective max rows
        int effectiveMaxRows = request.getSize();
        if (maxDisplay != null && maxDisplay > 0) {
            effectiveMaxRows = maxDisplay;
        }

        // 4. Execute main search
        StockSearchResultData searchData = stockRepository.search(
                cpny, whs, cust, request, pieceMode, effectiveMaxRows);

        // 5. Execute quantity totals
        BigDecimal ttlPycs = BigDecimal.ZERO;
        BigDecimal ttlPypc = BigDecimal.ZERO;
        BigDecimal ttlAvcs = BigDecimal.ZERO;
        BigDecimal ttlAvpc = BigDecimal.ZERO;
        BigDecimal ttlPyst = BigDecimal.ZERO;
        BigDecimal ttlAvst = BigDecimal.ZERO;

        if (!searchData.getRecords().isEmpty()) {
            try {
                List<StockDto> qtyRows = stockRepository.searchQtyTotals(
                        cpny, whs, cust, request, pieceMode);

                for (StockDto qty : qtyRows) {
                    if (qty.getPhysicalCasesQty() != null) {
                        ttlPycs = ttlPycs.add(qty.getPhysicalCasesQty());
                    }
                    if (qty.getPhysicalPiecesQty() != null) {
                        ttlPypc = ttlPypc.add(qty.getPhysicalPiecesQty());
                    }
                    if (qty.getAvailableCasesQty() != null) {
                        ttlAvcs = ttlAvcs.add(qty.getAvailableCasesQty());
                    }
                    if (qty.getAvailablePiecesQty() != null) {
                        ttlAvpc = ttlAvpc.add(qty.getAvailablePiecesQty());
                    }
                    if (qty.getPhysicalStockQty() != null) {
                        ttlPyst = ttlPyst.add(qty.getPhysicalStockQty());
                    }
                    if (qty.getAvailableStockQty() != null) {
                        ttlAvst = ttlAvst.add(qty.getAvailableStockQty());
                    }
                }
            } catch (Exception ex) {
                log.warn("Failed to calculate quantity totals: {}", ex.getMessage());
            }
        }

        // 6. Build result
        StockSearchResult result = new StockSearchResult();
        result.setRecords(searchData.getRecords());
        result.setTotalRecords(searchData.getTotalRecords());
        result.setPieceModeFlag(pieceMode);

        // Set totals based on piece mode
        if (pieceMode) {
            result.setTotalPhysicalPieces(ttlPypc);
            result.setTotalAvailablePieces(ttlAvpc);
        } else {
            result.setTotalPhysicalCases(ttlPycs);
            result.setTotalPhysicalPieces(ttlPypc);
            result.setTotalAvailableCases(ttlAvcs);
            result.setTotalAvailablePieces(ttlAvpc);
        }
        result.setTotalPhysicalStock(ttlPyst);
        result.setTotalAvailableStock(ttlAvst);

        // 7. Warning messages
        if (searchData.getRecords().isEmpty()) {
            result.setWarningCode("ERR0174");
            result.setWarningMessage("No data found");
        } else if (maxDisplay != null && searchData.getTotalRecords() > maxDisplay) {
            result.setWarningCode("INF1153");
            result.setWarningMessage("Found " + searchData.getTotalRecords()
                    + " records, displaying first " + maxDisplay);
        }

        return result;
    }
}
