package jp.co.nittsu.gwh.module.lpn.repository;

import jp.co.nittsu.gwh.module.lpn.dto.LpnConfigDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class LpnConfigRepository {

    private static final String[] CFG_SOURCES = {
        "SGWH0001.GWH_TM_LPN_CFG",
        "GWH_TM_LPN_CFG"
    };

    private static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter HMS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    @PersistenceContext
    private EntityManager em;

    public LpnConfigDto getConfig(String cpny, String whs, String cust) {
        for (String src : CFG_SOURCES) {
            try {
                String sql = "SELECT LCFG_LPN_USE_FLG, LCFG_AUTO_GEN_FLG, LCFG_BARCD_FMT, " +
                    "LCFG_BARCD_PFX, LCFG_PRNT_TPL, LCFG_HIR_FLG, LCFG_MIX_FLG, LCFG_OBND_LPN_FLG " +
                    "FROM " + src +
                    " WHERE LCFG_CPNY_COD = :cpny AND LCFG_WHS_COD = :whs AND LCFG_CUST_COD = :cust AND DEL_FLG = '0'";
                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", cpny);
                q.setParameter("whs", whs);
                q.setParameter("cust", cust);

                @SuppressWarnings("unchecked")
                List<Object[]> rows = q.getResultList();
                if (rows.isEmpty()) {
                    return defaultConfig(cpny, whs, cust);
                }
                Object[] row = rows.get(0);
                LpnConfigDto dto = new LpnConfigDto();
                dto.setCompanyCode(cpny);
                dto.setWarehouseCode(whs);
                dto.setCustomerCode(cust);
                dto.setLpnEnabled("Y".equals(str(row[0])));
                dto.setAutoGenerate("Y".equals(str(row[1])));
                dto.setBarcodeFormat(str(row[2]));
                dto.setBarcodePrefix(str(row[3]));
                dto.setPrintTemplate(str(row[4]));
                dto.setHierarchyEnabled("Y".equals(str(row[5])));
                dto.setMixedSkuAllowed("Y".equals(str(row[6])));
                dto.setOutboundLpnEnabled("Y".equals(str(row[7])));
                return dto;
            } catch (Exception ignored) {}
        }
        return defaultConfig(cpny, whs, cust);
    }

    public void saveConfig(LpnConfigDto config, String user) {
        String now = LocalDate.now().format(YMD_FORMATTER);
        String time = LocalTime.now().format(HMS_FORMATTER);

        for (String src : CFG_SOURCES) {
            try {
                String sql = "MERGE INTO " + src + " t " +
                    "USING (SELECT :cpny AS CPNY, :whs AS WHS, :cust AS CUST FROM DUAL) s " +
                    "ON (t.LCFG_CPNY_COD = s.CPNY AND t.LCFG_WHS_COD = s.WHS AND t.LCFG_CUST_COD = s.CUST) " +
                    "WHEN MATCHED THEN UPDATE SET " +
                    "LCFG_LPN_USE_FLG = :useFlg, LCFG_AUTO_GEN_FLG = :autoFlg, " +
                    "LCFG_BARCD_FMT = :bFmt, LCFG_BARCD_PFX = :bPfx, LCFG_PRNT_TPL = :pTpl, " +
                    "LCFG_HIR_FLG = :hirFlg, LCFG_MIX_FLG = :mixFlg, LCFG_OBND_LPN_FLG = :obFlg, " +
                    "UPD_YMD = :updYmd, UPD_TIM = :updTim, UPD_TMID = :updTmid, UPD_USER = :updUser, " +
                    "UPD_PGM = 'LPN_CFG_SAVE', UPD_TM_ZONE = :updTmZone, UPD_YMDHMS = SYSTIMESTAMP, UPD_L_YMDHMS = SYSTIMESTAMP " +
                    "WHEN NOT MATCHED THEN INSERT (" +
                    "LCFG_CPNY_COD, LCFG_WHS_COD, LCFG_CUST_COD, " +
                    "LCFG_LPN_USE_FLG, LCFG_AUTO_GEN_FLG, LCFG_BARCD_FMT, LCFG_BARCD_PFX, " +
                    "LCFG_PRNT_TPL, LCFG_HIR_FLG, LCFG_MIX_FLG, LCFG_OBND_LPN_FLG, " +
                    "DEL_FLG, CRT_YMD, CRT_TIM, CRT_TMID, CRT_USER, CRT_PGM, CRT_TM_ZONE, CRT_YMDHMS, CRT_L_YMDHMS, " +
                    "UPD_YMD, UPD_TIM, UPD_TMID, UPD_USER, UPD_PGM, UPD_TM_ZONE, UPD_YMDHMS, UPD_L_YMDHMS" +
                    ") VALUES (" +
                    ":cpny2, :whs2, :cust2, " +
                    ":useFlg2, :autoFlg2, :bFmt2, :bPfx2, " +
                    ":pTpl2, :hirFlg2, :mixFlg2, :obFlg2, " +
                    "'0', :crtYmd, :crtTim, :crtTmid, :crtUser, 'LPN_CFG_SAVE', :crtTmZone, SYSTIMESTAMP, SYSTIMESTAMP, " +
                    ":updYmd2, :updTim2, :updTmid2, :updUser2, 'LPN_CFG_SAVE', :updTmZone2, SYSTIMESTAMP, SYSTIMESTAMP)";

                Query q = em.createNativeQuery(sql);
                q.setParameter("cpny", config.getCompanyCode());
                q.setParameter("whs", config.getWarehouseCode());
                q.setParameter("cust", config.getCustomerCode());
                q.setParameter("useFlg", config.isLpnEnabled() ? "Y" : "N");
                q.setParameter("autoFlg", config.isAutoGenerate() ? "Y" : "N");
                q.setParameter("bFmt", config.getBarcodeFormat());
                q.setParameter("bPfx", config.getBarcodePrefix());
                q.setParameter("pTpl", config.getPrintTemplate());
                q.setParameter("hirFlg", config.isHierarchyEnabled() ? "Y" : "N");
                q.setParameter("mixFlg", config.isMixedSkuAllowed() ? "Y" : "N");
                q.setParameter("obFlg", config.isOutboundLpnEnabled() ? "Y" : "N");
                q.setParameter("updYmd", now);
                q.setParameter("updTim", time);
                q.setParameter("updTmid", "GWH-WMS");
                q.setParameter("updUser", user);
                q.setParameter("updTmZone", "+0900");
                q.setParameter("cpny2", config.getCompanyCode());
                q.setParameter("whs2", config.getWarehouseCode());
                q.setParameter("cust2", config.getCustomerCode());
                q.setParameter("useFlg2", config.isLpnEnabled() ? "Y" : "N");
                q.setParameter("autoFlg2", config.isAutoGenerate() ? "Y" : "N");
                q.setParameter("bFmt2", config.getBarcodeFormat());
                q.setParameter("bPfx2", config.getBarcodePrefix());
                q.setParameter("pTpl2", config.getPrintTemplate());
                q.setParameter("hirFlg2", config.isHierarchyEnabled() ? "Y" : "N");
                q.setParameter("mixFlg2", config.isMixedSkuAllowed() ? "Y" : "N");
                q.setParameter("obFlg2", config.isOutboundLpnEnabled() ? "Y" : "N");
                q.setParameter("crtYmd", now);
                q.setParameter("crtTim", time);
                q.setParameter("crtTmid", "GWH-WMS");
                q.setParameter("crtUser", user);
                q.setParameter("crtTmZone", "+0900");
                q.setParameter("updYmd2", now);
                q.setParameter("updTim2", time);
                q.setParameter("updTmid2", "GWH-WMS");
                q.setParameter("updUser2", user);
                q.setParameter("updTmZone2", "+0900");
                q.executeUpdate();
                return;
            } catch (Exception ignored) {}
        }
    }

    private LpnConfigDto defaultConfig(String cpny, String whs, String cust) {
        LpnConfigDto dto = new LpnConfigDto();
        dto.setCompanyCode(cpny);
        dto.setWarehouseCode(whs);
        dto.setCustomerCode(cust);
        dto.setLpnEnabled(false);
        dto.setAutoGenerate(true);
        dto.setBarcodeFormat("CUSTOM");
        dto.setMixedSkuAllowed(true);
        dto.setHierarchyEnabled(false);
        dto.setOutboundLpnEnabled(false);
        return dto;
    }

    private String str(Object o) {
        return o != null ? o.toString().trim() : null;
    }
}
