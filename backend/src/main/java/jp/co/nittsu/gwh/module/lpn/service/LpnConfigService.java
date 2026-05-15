package jp.co.nittsu.gwh.module.lpn.service;

import jp.co.nittsu.gwh.module.lpn.dto.LpnConfigDto;
import jp.co.nittsu.gwh.module.lpn.repository.LpnConfigRepository;
import jp.co.nittsu.gwh.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LpnConfigService {

    @Autowired
    private LpnConfigRepository lpnConfigRepository;

    @Autowired
    private TenantContext tenantContext;

    public LpnConfigDto getConfig() {
        return lpnConfigRepository.getConfig(
            tenantContext.getCompanyCode(),
            tenantContext.getWarehouseCode(),
            tenantContext.getCustomerCode()
        );
    }

    public boolean isLpnEnabled() {
        LpnConfigDto config = getConfig();
        return config != null && config.isLpnEnabled();
    }

    @Transactional
    public LpnConfigDto saveConfig(LpnConfigDto config, String user) {
        config.setCompanyCode(tenantContext.getCompanyCode());
        config.setWarehouseCode(tenantContext.getWarehouseCode());
        config.setCustomerCode(tenantContext.getCustomerCode());
        lpnConfigRepository.saveConfig(config, user);
        return getConfig();
    }
}
