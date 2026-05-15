package jp.co.nittsu.gwh.module.lpn.entity;

import java.io.Serializable;
import java.util.Objects;

public class LpnId implements Serializable {

    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private String lpnNumber;

    public LpnId() {}

    public LpnId(String companyCode, String warehouseCode, String customerCode, String lpnNumber) {
        this.companyCode = companyCode;
        this.warehouseCode = warehouseCode;
        this.customerCode = customerCode;
        this.lpnNumber = lpnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LpnId lpnId = (LpnId) o;
        return Objects.equals(companyCode, lpnId.companyCode) &&
               Objects.equals(warehouseCode, lpnId.warehouseCode) &&
               Objects.equals(customerCode, lpnId.customerCode) &&
               Objects.equals(lpnNumber, lpnId.lpnNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCode, warehouseCode, customerCode, lpnNumber);
    }
}
