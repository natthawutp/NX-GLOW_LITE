package jp.co.nittsu.gwh.common.utils.data;

import jp.co.nittsu.gwh.common.service.WmsNumberingService;
import jp.co.nittsu.gwh.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Modern WMS equivalent of the legacy GwhNumberingDataUtils.
 * Provides the same getControlNumber accessor, delegating to the raw JDBC WmsNumberingService.
 */
@Component
public class GwhNumberingDataUtils {

    @Autowired
    private WmsNumberingService wmsNumberingService;

    @Autowired
    private TenantContext tenantContext;

    /**
     * Modern equivalent of legacy:
     * gwhNumberingDataUtils.getControlNumber(gwhLoginUserDTO, "AV_NUM")
     * 
     * In the modern system, we get the tenant context from TenantContext 
     * rather than passing a GwhLoginUserDTO around manually.
     *
     * @param numCode The sequence code (e.g. "AV_NUM")
     * @return The next formatted control number
     */
    public String getControlNumber(String numCode) {
        return getControlNumber(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode(),
            numCode
        );
    }

    /**
     * Overloaded version if explicit tenant context is needed.
     */
    public String getControlNumber(String companyCode, String warehouseCode, String customerCode, String numCode) {
        try {
            return wmsNumberingService.generateNumber(numCode, companyCode, warehouseCode, customerCode);
        } catch (Exception e) {
            // Legacy interface typically catches exceptions and returns null
            return null;
        }
    }
}
