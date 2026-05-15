package jp.co.nittsu.gwh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Binds gwh-trans-config.yml → gwh.transaction.*
 *
 * Provides typed access to receiving/inspection configuration,
 * status progression codes, and error code messages.
 *
 * Legacy sources: GWH_TM_PRCC, GWH_TM_TRN, GWH_TM_CUST, GWH_TM_WHS,
 *                 GWH_TM_CODE (KND=477, KND=042)
 */
@Configuration
@ConfigurationProperties(prefix = "gwh.transaction")
public class GwhTransactionProperties {

    private Receiving receiving = new Receiving();
    private StatusProgression statusProgression = new StatusProgression();
    private Map<String, String> errorCodes = new HashMap<>();

    // ----------------------------------------------------------------
    // Receiving / Inspection Configuration
    // ----------------------------------------------------------------
    public static class Receiving {
        private List<String> validArrivalTransactionKinds = new ArrayList<>();
        private int areaCodeByteLength = 3;
        private String locationSearchKind = "3";
        private DefaultArrivalArea defaultArrivalArea = new DefaultArrivalArea();
        private HhtInspectionMode hhtInspectionMode = new HhtInspectionMode();
        private Map<String, String> pikDateFormats = new HashMap<>();
        private ReceivingLimits limits = new ReceivingLimits();

        public List<String> getValidArrivalTransactionKinds() { return validArrivalTransactionKinds; }
        public void setValidArrivalTransactionKinds(List<String> v) { this.validArrivalTransactionKinds = v; }
        public int getAreaCodeByteLength() { return areaCodeByteLength; }
        public void setAreaCodeByteLength(int v) { this.areaCodeByteLength = v; }
        public String getLocationSearchKind() { return locationSearchKind; }
        public void setLocationSearchKind(String v) { this.locationSearchKind = v; }
        public DefaultArrivalArea getDefaultArrivalArea() { return defaultArrivalArea; }
        public void setDefaultArrivalArea(DefaultArrivalArea v) { this.defaultArrivalArea = v; }
        public HhtInspectionMode getHhtInspectionMode() { return hhtInspectionMode; }
        public void setHhtInspectionMode(HhtInspectionMode v) { this.hhtInspectionMode = v; }
        public Map<String, String> getPikDateFormats() { return pikDateFormats; }
        public void setPikDateFormats(Map<String, String> v) { this.pikDateFormats = v; }
        public ReceivingLimits getLimits() { return limits; }
        public void setLimits(ReceivingLimits v) { this.limits = v; }
    }

    public static class DefaultArrivalArea {
        private String areaCode = "";
        private String rackCode = "";
        private String positionCode = "";
        private String levelCode = "";

        public String getAreaCode() { return areaCode; }
        public void setAreaCode(String v) { this.areaCode = v; }
        public String getRackCode() { return rackCode; }
        public void setRackCode(String v) { this.rackCode = v; }
        public String getPositionCode() { return positionCode; }
        public void setPositionCode(String v) { this.positionCode = v; }
        public String getLevelCode() { return levelCode; }
        public void setLevelCode(String v) { this.levelCode = v; }
    }

    public static class HhtInspectionMode {
        private boolean enabled = false;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean v) { this.enabled = v; }
    }

    public static class ReceivingLimits {
        private double maxLineWeightKg = 99999.999;
        private double maxLineVolumeM3 = 9999.9999;
        private double maxTotalWeightKg = 999999.999;
        private double maxTotalVolumeM3 = 99999.9999;

        public double getMaxLineWeightKg() { return maxLineWeightKg; }
        public void setMaxLineWeightKg(double v) { this.maxLineWeightKg = v; }
        public double getMaxLineVolumeM3() { return maxLineVolumeM3; }
        public void setMaxLineVolumeM3(double v) { this.maxLineVolumeM3 = v; }
        public double getMaxTotalWeightKg() { return maxTotalWeightKg; }
        public void setMaxTotalWeightKg(double v) { this.maxTotalWeightKg = v; }
        public double getMaxTotalVolumeM3() { return maxTotalVolumeM3; }
        public void setMaxTotalVolumeM3(double v) { this.maxTotalVolumeM3 = v; }
    }

    // ----------------------------------------------------------------
    // Status Progression
    // ----------------------------------------------------------------
    public static class StatusProgression {
        private ArrivalStatus arrival = new ArrivalStatus();

        public ArrivalStatus getArrival() { return arrival; }
        public void setArrival(ArrivalStatus v) { this.arrival = v; }
    }

    public static class ArrivalStatus {
        private String ready = "200";
        private String receiving = "300";
        private String inspectedExact = "209";
        private String inspectedDiscrepancy = "205";

        public String getReady() { return ready; }
        public void setReady(String v) { this.ready = v; }
        public String getReceiving() { return receiving; }
        public void setReceiving(String v) { this.receiving = v; }
        public String getInspectedExact() { return inspectedExact; }
        public void setInspectedExact(String v) { this.inspectedExact = v; }
        public String getInspectedDiscrepancy() { return inspectedDiscrepancy; }
        public void setInspectedDiscrepancy(String v) { this.inspectedDiscrepancy = v; }
    }

    // ----------------------------------------------------------------
    // Root getters/setters
    // ----------------------------------------------------------------
    public Receiving getReceiving() { return receiving; }
    public void setReceiving(Receiving v) { this.receiving = v; }
    public StatusProgression getStatusProgression() { return statusProgression; }
    public void setStatusProgression(StatusProgression v) { this.statusProgression = v; }
    public Map<String, String> getErrorCodes() { return errorCodes; }
    public void setErrorCodes(Map<String, String> v) { this.errorCodes = v; }

    /** Get error message by code, with fallback to code itself */
    public String getErrorMessage(String code) {
        return errorCodes.getOrDefault(code, code);
    }
}
