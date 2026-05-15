package jp.co.nittsu.gwh.module.inbound.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

/**
 * Request body for a single item scan on the receiving screen.
 * Each scan triggers an immediate persist of one AVR result row.
 */
public class ScanItemRequest {

    @NotBlank
    private String arrivalNumber;

    /** Product code or barcode scanned — matched against AVD_PRD_COD */
    @NotBlank
    private String productCode;

    /** Quantity for this scan event (default 1) */
    @Min(1)
    private int quantity = 1;

    /** Optional LPN to link the received item to */
    private String lpnNumber;

    /** LPN type (PLT/CSE/TOT/OTH) — used only when lpnNumber is provided */
    private String lpnType = "PLT";

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String v) { this.arrivalNumber = v; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String v) { this.productCode = v; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int v) { this.quantity = v; }
    public String getLpnNumber() { return lpnNumber; }
    public void setLpnNumber(String v) { this.lpnNumber = v; }
    public String getLpnType() { return lpnType; }
    public void setLpnType(String v) { this.lpnType = v; }
}
