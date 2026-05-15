package jp.co.nittsu.gwh.module.lpn.entity;

import java.io.Serializable;
import java.util.Objects;

public class LpnContentId implements Serializable {

    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private String lpnNumber;
    private Integer lineNumber;

    public LpnContentId() {}

    public LpnContentId(String companyCode, String warehouseCode, String customerCode,
                         String lpnNumber, Integer lineNumber) {
        this.companyCode = companyCode;
        this.warehouseCode = warehouseCode;
        this.customerCode = customerCode;
        this.lpnNumber = lpnNumber;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LpnContentId that = (LpnContentId) o;
        return Objects.equals(companyCode, that.companyCode) &&
               Objects.equals(warehouseCode, that.warehouseCode) &&
               Objects.equals(customerCode, that.customerCode) &&
               Objects.equals(lpnNumber, that.lpnNumber) &&
               Objects.equals(lineNumber, that.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCode, warehouseCode, customerCode, lpnNumber, lineNumber);
    }
}
