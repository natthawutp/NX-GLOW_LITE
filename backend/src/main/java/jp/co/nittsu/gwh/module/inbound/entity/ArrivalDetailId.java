package jp.co.nittsu.gwh.module.inbound.entity;

import java.io.Serializable;
import java.util.Objects;

public class ArrivalDetailId implements Serializable {
    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private String arrivalNumber;
    private Integer lineNumber;

    public ArrivalDetailId() {}

    public ArrivalDetailId(String companyCode, String warehouseCode, String customerCode,
                           String arrivalNumber, Integer lineNumber) {
        this.companyCode = companyCode;
        this.warehouseCode = warehouseCode;
        this.customerCode = customerCode;
        this.arrivalNumber = arrivalNumber;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrivalDetailId)) return false;
        ArrivalDetailId that = (ArrivalDetailId) o;
        return Objects.equals(companyCode, that.companyCode) &&
               Objects.equals(warehouseCode, that.warehouseCode) &&
               Objects.equals(customerCode, that.customerCode) &&
               Objects.equals(arrivalNumber, that.arrivalNumber) &&
               Objects.equals(lineNumber, that.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCode, warehouseCode, customerCode, arrivalNumber, lineNumber);
    }
}
