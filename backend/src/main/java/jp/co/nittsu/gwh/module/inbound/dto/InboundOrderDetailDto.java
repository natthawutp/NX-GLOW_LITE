package jp.co.nittsu.gwh.module.inbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Full arrival order detail DTO: header + detail lines.
 * Used by GET /inbound/orders/{arrivalNo} for both detail view and edit screen data loading.
 */
public class InboundOrderDetailDto {

    @JsonProperty("AVH_AV_NUM")
    private String arrivalNumber;

    @JsonProperty("AVH_AV_STS")
    private String arrivalStatus;

    @JsonProperty("AVH_AV_STS_LBL")
    private String arrivalStatusLabel;

    @JsonProperty("AVH_TRN_KND")
    private String transactionKind;

    @JsonProperty("AVH_TRN_KND_LBL")
    private String transactionKindLabel;

    @JsonProperty("AVH_SPL_COD")
    private String supplierCode;

    @JsonProperty("AVH_SPL_NAM1")
    private String supplierName;

    @JsonProperty("AVH_PO_NUM")
    private String poNumber;

    @JsonProperty("AVH_RF_NUM")
    private String referenceNumber;

    @JsonProperty("AVH_SCDL_YMD")
    private LocalDate scheduledDate;

    @JsonProperty("AVH_ARV_YMD")
    private LocalDate actualDate;

    @JsonProperty("AVH_WGT")
    private BigDecimal weight;

    @JsonProperty("AVH_M3")
    private BigDecimal volumeM3;

    @JsonProperty("AVH_RMKS")
    private String remarks;

    @JsonProperty("UPD_YMDHMS")
    private String updTimestamp;

    @JsonProperty("editable")
    private boolean editable;

    @JsonProperty("lines")
    private List<Line> lines = new ArrayList<>();

    @JsonProperty("arrivalResults")
    private List<ArrivalResult> arrivalResults = new ArrayList<>();

    @JsonProperty("lpns")
    private List<LpnInfo> lpns = new ArrayList<>();

    // --- Getters & Setters ---

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getArrivalStatus() { return arrivalStatus; }
    public void setArrivalStatus(String arrivalStatus) { this.arrivalStatus = arrivalStatus; }
    public String getArrivalStatusLabel() { return arrivalStatusLabel; }
    public void setArrivalStatusLabel(String arrivalStatusLabel) { this.arrivalStatusLabel = arrivalStatusLabel; }
    public String getTransactionKind() { return transactionKind; }
    public void setTransactionKind(String transactionKind) { this.transactionKind = transactionKind; }
    public String getTransactionKindLabel() { return transactionKindLabel; }
    public void setTransactionKindLabel(String transactionKindLabel) { this.transactionKindLabel = transactionKindLabel; }
    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public LocalDate getActualDate() { return actualDate; }
    public void setActualDate(LocalDate actualDate) { this.actualDate = actualDate; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public BigDecimal getVolumeM3() { return volumeM3; }
    public void setVolumeM3(BigDecimal volumeM3) { this.volumeM3 = volumeM3; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getUpdTimestamp() { return updTimestamp; }
    public void setUpdTimestamp(String updTimestamp) { this.updTimestamp = updTimestamp; }
    public boolean isEditable() { return editable; }
    public void setEditable(boolean editable) { this.editable = editable; }
    public List<Line> getLines() { return lines; }
    public void setLines(List<Line> lines) { this.lines = lines; }
    public List<ArrivalResult> getArrivalResults() { return arrivalResults; }
    public void setArrivalResults(List<ArrivalResult> arrivalResults) { this.arrivalResults = arrivalResults; }
    public List<LpnInfo> getLpns() { return lpns; }
    public void setLpns(List<LpnInfo> lpns) { this.lpns = lpns; }

    /**
     * Detail line within an arrival order.
     */
    public static class Line {

        @JsonProperty("AVD_AVLN_NUM")
        private BigDecimal lineNumber;

        @JsonProperty("AVD_AV_STS")
        private String lineStatus;

        @JsonProperty("AVD_AV_STS_LBL")
        private String lineStatusLabel;

        @JsonProperty("AVD_INSP_STS")
        private String inspectionStatus;

        @JsonProperty("AVD_PROD_COD")
        private String productCode;

        @JsonProperty("AVD_PROD_NAM")
        private String productName;

        @JsonProperty("AVD_ORGN_COD")
        private String originCode;

        @JsonProperty("AVD_SPPR_COD")
        private String shipperCode;

        @JsonProperty("AVD_SPPR_NAM")
        private String shipperName;

        @JsonProperty("AVD_PPCS_QTY")
        private BigDecimal plannedPieceQuantity;

        @JsonProperty("AVD_SCS_QTY")
        private BigDecimal plannedCaseQuantity;

        @JsonProperty("AVD_STPC_QTY")
        private BigDecimal totalPieceQuantity;

        @JsonProperty("AVD_WGT")
        private BigDecimal weight;

        @JsonProperty("AVD_M3")
        private BigDecimal volumeM3;

        @JsonProperty("AVD_SBIV_COD")
        private String subInventoryCode;

        @JsonProperty("AVD_PIK1")
        private String pik1;

        @JsonProperty("AVD_PIK2")
        private String pik2;

        @JsonProperty("AVD_PIK3")
        private String pik3;

        @JsonProperty("AVD_PIK4")
        private String pik4;

        @JsonProperty("AVD_PIK5")
        private String pik5;

        @JsonProperty("AVD_PIK6")
        private String pik6;

        @JsonProperty("AVD_PIK7")
        private String pik7;

        @JsonProperty("AVD_RMKS")
        private String remarks;

        // --- Getters & Setters ---

        public BigDecimal getLineNumber() { return lineNumber; }
        public void setLineNumber(BigDecimal lineNumber) { this.lineNumber = lineNumber; }
        public String getLineStatus() { return lineStatus; }
        public void setLineStatus(String lineStatus) { this.lineStatus = lineStatus; }
        public String getLineStatusLabel() { return lineStatusLabel; }
        public void setLineStatusLabel(String lineStatusLabel) { this.lineStatusLabel = lineStatusLabel; }
        public String getInspectionStatus() { return inspectionStatus; }
        public void setInspectionStatus(String inspectionStatus) { this.inspectionStatus = inspectionStatus; }
        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getOriginCode() { return originCode; }
        public void setOriginCode(String originCode) { this.originCode = originCode; }
        public String getShipperCode() { return shipperCode; }
        public void setShipperCode(String shipperCode) { this.shipperCode = shipperCode; }
        public String getShipperName() { return shipperName; }
        public void setShipperName(String shipperName) { this.shipperName = shipperName; }
        public BigDecimal getPlannedPieceQuantity() { return plannedPieceQuantity; }
        public void setPlannedPieceQuantity(BigDecimal plannedPieceQuantity) { this.plannedPieceQuantity = plannedPieceQuantity; }
        public BigDecimal getPlannedCaseQuantity() { return plannedCaseQuantity; }
        public void setPlannedCaseQuantity(BigDecimal plannedCaseQuantity) { this.plannedCaseQuantity = plannedCaseQuantity; }
        public BigDecimal getTotalPieceQuantity() { return totalPieceQuantity; }
        public void setTotalPieceQuantity(BigDecimal totalPieceQuantity) { this.totalPieceQuantity = totalPieceQuantity; }
        public BigDecimal getWeight() { return weight; }
        public void setWeight(BigDecimal weight) { this.weight = weight; }
        public BigDecimal getVolumeM3() { return volumeM3; }
        public void setVolumeM3(BigDecimal volumeM3) { this.volumeM3 = volumeM3; }
        public String getSubInventoryCode() { return subInventoryCode; }
        public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
        public String getPik1() { return pik1; }
        public void setPik1(String pik1) { this.pik1 = pik1; }
        public String getPik2() { return pik2; }
        public void setPik2(String pik2) { this.pik2 = pik2; }
        public String getPik3() { return pik3; }
        public void setPik3(String pik3) { this.pik3 = pik3; }
        public String getPik4() { return pik4; }
        public void setPik4(String pik4) { this.pik4 = pik4; }
        public String getPik5() { return pik5; }
        public void setPik5(String pik5) { this.pik5 = pik5; }
        public String getPik6() { return pik6; }
        public void setPik6(String pik6) { this.pik6 = pik6; }
        public String getPik7() { return pik7; }
        public void setPik7(String pik7) { this.pik7 = pik7; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    /**
     * Arrival result (inspection/receiving) record from GWH_TJ_AV_R.
     */
    public static class ArrivalResult {

        @JsonProperty("AVR_AVLN_NUM")
        private BigDecimal lineNumber;

        @JsonProperty("AVR_AVSQ_NUM")
        private BigDecimal sequenceNumber;

        @JsonProperty("AVR_AVSP_STS")
        private String resultStatus;

        @JsonProperty("AVR_AVSP_STS_LBL")
        private String resultStatusLabel;

        @JsonProperty("AVR_PROD_COD")
        private String productCode;

        @JsonProperty("AVR_RCS_QTY")
        private BigDecimal resultCaseQty;

        @JsonProperty("AVR_RPC_QTY")
        private BigDecimal resultPieceQty;

        @JsonProperty("AVR_RTPC_QTY")
        private BigDecimal resultTotalPieceQty;

        @JsonProperty("AVR_AREA_COD")
        private String areaCode;

        @JsonProperty("AVR_RACK_COD")
        private String rackCode;

        @JsonProperty("AVR_PSTN_COD")
        private String positionCode;

        @JsonProperty("AVR_LVL_COD")
        private String levelCode;

        @JsonProperty("AVR_LOCATION")
        private String location;

        @JsonProperty("AVR_DMG_FLG")
        private String damageFlag;

        @JsonProperty("AVR_RMKS")
        private String remarks;

        // --- Getters & Setters ---

        public BigDecimal getLineNumber() { return lineNumber; }
        public void setLineNumber(BigDecimal lineNumber) { this.lineNumber = lineNumber; }
        public BigDecimal getSequenceNumber() { return sequenceNumber; }
        public void setSequenceNumber(BigDecimal sequenceNumber) { this.sequenceNumber = sequenceNumber; }
        public String getResultStatus() { return resultStatus; }
        public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
        public String getResultStatusLabel() { return resultStatusLabel; }
        public void setResultStatusLabel(String resultStatusLabel) { this.resultStatusLabel = resultStatusLabel; }
        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        public BigDecimal getResultCaseQty() { return resultCaseQty; }
        public void setResultCaseQty(BigDecimal resultCaseQty) { this.resultCaseQty = resultCaseQty; }
        public BigDecimal getResultPieceQty() { return resultPieceQty; }
        public void setResultPieceQty(BigDecimal resultPieceQty) { this.resultPieceQty = resultPieceQty; }
        public BigDecimal getResultTotalPieceQty() { return resultTotalPieceQty; }
        public void setResultTotalPieceQty(BigDecimal resultTotalPieceQty) { this.resultTotalPieceQty = resultTotalPieceQty; }
        public String getAreaCode() { return areaCode; }
        public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
        public String getRackCode() { return rackCode; }
        public void setRackCode(String rackCode) { this.rackCode = rackCode; }
        public String getPositionCode() { return positionCode; }
        public void setPositionCode(String positionCode) { this.positionCode = positionCode; }
        public String getLevelCode() { return levelCode; }
        public void setLevelCode(String levelCode) { this.levelCode = levelCode; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getDamageFlag() { return damageFlag; }
        public void setDamageFlag(String damageFlag) { this.damageFlag = damageFlag; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    /**
     * LPN (License Plate Number) linked to this arrival order.
     */
    public static class LpnInfo {

        @JsonProperty("LPN_NUM")
        private String lpnNumber;

        @JsonProperty("LPN_TYPE")
        private String lpnType;

        @JsonProperty("LPN_STS")
        private String lpnStatus;

        @JsonProperty("LPN_LOC_COD")
        private String locationCode;

        @JsonProperty("LPN_TTL_QTY")
        private BigDecimal totalQty;

        @JsonProperty("LPN_TTL_WGT")
        private BigDecimal totalWeight;

        @JsonProperty("LPN_TTL_VOL")
        private BigDecimal totalVolume;

        @JsonProperty("LPN_RCV_YMD")
        private String receiveDate;

        @JsonProperty("LPN_RMK")
        private String remarks;

        // --- Getters & Setters ---

        public String getLpnNumber() { return lpnNumber; }
        public void setLpnNumber(String lpnNumber) { this.lpnNumber = lpnNumber; }
        public String getLpnType() { return lpnType; }
        public void setLpnType(String lpnType) { this.lpnType = lpnType; }
        public String getLpnStatus() { return lpnStatus; }
        public void setLpnStatus(String lpnStatus) { this.lpnStatus = lpnStatus; }
        public String getLocationCode() { return locationCode; }
        public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
        public BigDecimal getTotalQty() { return totalQty; }
        public void setTotalQty(BigDecimal totalQty) { this.totalQty = totalQty; }
        public BigDecimal getTotalWeight() { return totalWeight; }
        public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }
        public BigDecimal getTotalVolume() { return totalVolume; }
        public void setTotalVolume(BigDecimal totalVolume) { this.totalVolume = totalVolume; }
        public String getReceiveDate() { return receiveDate; }
        public void setReceiveDate(String receiveDate) { this.receiveDate = receiveDate; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }
}
