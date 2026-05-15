package jp.co.nittsu.gwh.module.inbound.entity;

import java.io.Serializable;
import java.util.Objects;

public class ArrivalResultId implements Serializable {
    private String companyCode;
    private String warehouseCode;
    private String customerCode;
    private String arrivalNumber;
    private Integer lineNumber;
    private Integer sequenceNumber;

    public ArrivalResultId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrivalResultId)) return false;
        ArrivalResultId that = (ArrivalResultId) o;
        return Objects.equals(companyCode, that.companyCode) &&
               Objects.equals(warehouseCode, that.warehouseCode) &&
               Objects.equals(customerCode, that.customerCode) &&
               Objects.equals(arrivalNumber, that.arrivalNumber) &&
               Objects.equals(lineNumber, that.lineNumber) &&
               Objects.equals(sequenceNumber, that.sequenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCode, warehouseCode, customerCode, arrivalNumber, lineNumber, sequenceNumber);
    }
}
