package jp.co.nittsu.gwh.module.lpn.entity;

import java.io.Serializable;
import java.util.Objects;

public class LpnConfigId implements Serializable {

    private String companyCode;
    private String warehouseCode;
    private String customerCode;

    public LpnConfigId() {}

    public LpnConfigId(String companyCode, String warehouseCode, String customerCode) {
        this.companyCode = companyCode;
        this.warehouseCode = warehouseCode;
        this.customerCode = customerCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LpnConfigId that = (LpnConfigId) o;
        return Objects.equals(companyCode, that.companyCode) &&
               Objects.equals(warehouseCode, that.warehouseCode) &&
               Objects.equals(customerCode, that.customerCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyCode, warehouseCode, customerCode);
    }
}
