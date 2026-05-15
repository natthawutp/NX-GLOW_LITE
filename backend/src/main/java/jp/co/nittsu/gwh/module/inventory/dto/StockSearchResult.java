package jp.co.nittsu.gwh.module.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Wrapper for stock inquiry results including records, totals, and warnings.
 * Mirrors legacy GwhLpski020 response structure.
 */
public class StockSearchResult {

    private List<StockDto> records;
    private long totalRecords;

    // Quantity totals (aggregated across all matching groups)
    private BigDecimal totalPhysicalCases;
    private BigDecimal totalPhysicalPieces;
    private BigDecimal totalAvailableCases;
    private BigDecimal totalAvailablePieces;
    private BigDecimal totalPhysicalStock;
    private BigDecimal totalAvailableStock;

    // Customer preference: CUST_PCSM_FLG == "Y" means piece-only mode
    private boolean pieceModeFlag;

    // Warning/info messages from legacy business logic
    private String warningCode;
    private String warningMessage;

    // --- Getters & Setters ---

    public List<StockDto> getRecords() { return records; }
    public void setRecords(List<StockDto> records) { this.records = records; }
    public long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }
    public BigDecimal getTotalPhysicalCases() { return totalPhysicalCases; }
    public void setTotalPhysicalCases(BigDecimal totalPhysicalCases) { this.totalPhysicalCases = totalPhysicalCases; }
    public BigDecimal getTotalPhysicalPieces() { return totalPhysicalPieces; }
    public void setTotalPhysicalPieces(BigDecimal totalPhysicalPieces) { this.totalPhysicalPieces = totalPhysicalPieces; }
    public BigDecimal getTotalAvailableCases() { return totalAvailableCases; }
    public void setTotalAvailableCases(BigDecimal totalAvailableCases) { this.totalAvailableCases = totalAvailableCases; }
    public BigDecimal getTotalAvailablePieces() { return totalAvailablePieces; }
    public void setTotalAvailablePieces(BigDecimal totalAvailablePieces) { this.totalAvailablePieces = totalAvailablePieces; }
    public BigDecimal getTotalPhysicalStock() { return totalPhysicalStock; }
    public void setTotalPhysicalStock(BigDecimal totalPhysicalStock) { this.totalPhysicalStock = totalPhysicalStock; }
    public BigDecimal getTotalAvailableStock() { return totalAvailableStock; }
    public void setTotalAvailableStock(BigDecimal totalAvailableStock) { this.totalAvailableStock = totalAvailableStock; }
    public boolean isPieceModeFlag() { return pieceModeFlag; }
    public void setPieceModeFlag(boolean pieceModeFlag) { this.pieceModeFlag = pieceModeFlag; }
    public String getWarningCode() { return warningCode; }
    public void setWarningCode(String warningCode) { this.warningCode = warningCode; }
    public String getWarningMessage() { return warningMessage; }
    public void setWarningMessage(String warningMessage) { this.warningMessage = warningMessage; }
}
