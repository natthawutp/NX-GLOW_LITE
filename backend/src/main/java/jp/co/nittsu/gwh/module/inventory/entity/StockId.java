package jp.co.nittsu.gwh.module.inventory.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for Stock entity.
 */
public class StockId implements Serializable {

    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private String productCode;
    private String locationCode;
    private String lotNumber;

    public StockId() {}

    public StockId(String companyCode, String warehouseCode, String customerCode,
                   String productCode, String locationCode, String lotNumber) {
        this.companyCode = companyCode;
        this.warehouseCode = warehouseCode;
        this.customerCode = customerCode;
        this.productCode = productCode;
        this.locationCode = locationCode;
        this.lotNumber = lotNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockId stockId = (StockId) o;
        return Objects.equals(companyCode, stockId.companyCode) &&
               Objects.equals(warehouseCode, stockId.warehouseCode) &&
               Objects.equals(customerCode, stockId.customerCode) &&
               Objects.equals(productCode, stockId.productCode) &&
               Objects.equals(locationCode, stockId.locationCode) &&
               Objects.equals(lotNumber, stockId.lotNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCode, warehouseCode, customerCode, productCode, locationCode, lotNumber);
    }
}
