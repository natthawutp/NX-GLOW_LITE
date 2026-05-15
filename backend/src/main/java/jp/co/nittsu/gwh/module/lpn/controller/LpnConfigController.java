package jp.co.nittsu.gwh.module.lpn.controller;

import jp.co.nittsu.gwh.common.dto.ApiResponse;
import jp.co.nittsu.gwh.module.lpn.dto.LpnConfigDto;
import jp.co.nittsu.gwh.module.lpn.service.LpnConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/lpn/config")
public class LpnConfigController {

    private static final Logger log = LoggerFactory.getLogger(LpnConfigController.class);

    @Autowired
    private LpnConfigService lpnConfigService;

    @GetMapping
    public ResponseEntity<ApiResponse<LpnConfigDto>> getConfig() {
        LpnConfigDto config = lpnConfigService.getConfig();
        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<LpnConfigDto>> saveConfig(
            @RequestBody LpnConfigDto config, Principal principal) {
        try {
            String user = principal != null ? principal.getName() : "SYSTEM";
            LpnConfigDto saved = lpnConfigService.saveConfig(config, user);
            return ResponseEntity.ok(ApiResponse.success(saved));
        } catch (Exception ex) {
            log.error("LPN config save failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("LPN_CFG_FAILED", "Failed to save LPN configuration"));
        }
    }
}
