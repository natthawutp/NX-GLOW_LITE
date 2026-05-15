package jp.co.nittsu.gwh.module.inbound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InboundOrderRegisterRequest {

    @NotNull
    @Valid
    @JsonProperty("header")
    private Header header;

    @Valid
    @JsonProperty("lines")
    private List<Line> lines = new ArrayList<>();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public static class Header {
        @JsonProperty("AVH_SCDL_YMD")
        private LocalDate scheduledDate;
        @JsonProperty("AVH_TRN_KND")
        private String transactionKind;
        @JsonProperty("AVH_AV_STS")
        private String arrivalStatus;
        @JsonProperty("AVH_PO_NUM")
        private String poNumber;
        @JsonProperty("AVH_RF_NUM")
        private String referenceNumber;
        @JsonProperty("AVH_RMKS")
        private String remarks;
        @JsonProperty("AVH_SPL_COD")
        private String supplierCode;
        @JsonProperty("AVH_SPL_NAM1")
        private String supplierName;
        @JsonProperty("AVH_WGT")
        private BigDecimal weight;
        @JsonProperty("AVH_M3")
        private BigDecimal volumeM3;

        @JsonProperty("AVH_CLI")
        private List<String> cli = new ArrayList<>();
        @JsonProperty("AVH_CFG")
        private List<String> cfg = new ArrayList<>();
        @JsonProperty("AVH_CNI")
        private List<BigDecimal> cni = new ArrayList<>();

        public LocalDate getScheduledDate() {
            return scheduledDate;
        }

        public void setScheduledDate(LocalDate scheduledDate) {
            this.scheduledDate = scheduledDate;
        }

        public String getTransactionKind() {
            return transactionKind;
        }

        public void setTransactionKind(String transactionKind) {
            this.transactionKind = transactionKind;
        }

        public String getArrivalStatus() {
            return arrivalStatus;
        }

        public void setArrivalStatus(String arrivalStatus) {
            this.arrivalStatus = arrivalStatus;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
        }

        public String getReferenceNumber() {
            return referenceNumber;
        }

        public void setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getSupplierCode() {
            return supplierCode;
        }

        public void setSupplierCode(String supplierCode) {
            this.supplierCode = supplierCode;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public BigDecimal getVolumeM3() {
            return volumeM3;
        }

        public void setVolumeM3(BigDecimal volumeM3) {
            this.volumeM3 = volumeM3;
        }

        public List<String> getCli() {
            return cli;
        }

        public void setCli(List<String> cli) {
            this.cli = cli;
        }

        public List<String> getCfg() {
            return cfg;
        }

        public void setCfg(List<String> cfg) {
            this.cfg = cfg;
        }

        public List<BigDecimal> getCni() {
            return cni;
        }

        public void setCni(List<BigDecimal> cni) {
            this.cni = cni;
        }
    }

    public static class Line {
        @JsonProperty("AVD_AVLN_NUM")
        private BigDecimal arrivalLineNumber;
        @JsonProperty("AVD_AV_STS")
        private String arrivalStatus;
        @JsonProperty("AVD_INSP_STS")
        private String inspectStatus;
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

        @JsonProperty("AVD_CLI")
        private List<String> cli = new ArrayList<>();
        @JsonProperty("AVD_CFG")
        private List<String> cfg = new ArrayList<>();
        @JsonProperty("AVD_CNI")
        private List<BigDecimal> cni = new ArrayList<>();

        public BigDecimal getArrivalLineNumber() {
            return arrivalLineNumber;
        }

        public void setArrivalLineNumber(BigDecimal arrivalLineNumber) {
            this.arrivalLineNumber = arrivalLineNumber;
        }

        public String getArrivalStatus() {
            return arrivalStatus;
        }

        public void setArrivalStatus(String arrivalStatus) {
            this.arrivalStatus = arrivalStatus;
        }

        public String getInspectStatus() {
            return inspectStatus;
        }

        public void setInspectStatus(String inspectStatus) {
            this.inspectStatus = inspectStatus;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getOriginCode() {
            return originCode;
        }

        public void setOriginCode(String originCode) {
            this.originCode = originCode;
        }

        public String getShipperCode() {
            return shipperCode;
        }

        public void setShipperCode(String shipperCode) {
            this.shipperCode = shipperCode;
        }

        public String getShipperName() {
            return shipperName;
        }

        public void setShipperName(String shipperName) {
            this.shipperName = shipperName;
        }

        public BigDecimal getPlannedPieceQuantity() {
            return plannedPieceQuantity;
        }

        public void setPlannedPieceQuantity(BigDecimal plannedPieceQuantity) {
            this.plannedPieceQuantity = plannedPieceQuantity;
        }

        public BigDecimal getPlannedCaseQuantity() {
            return plannedCaseQuantity;
        }

        public void setPlannedCaseQuantity(BigDecimal plannedCaseQuantity) {
            this.plannedCaseQuantity = plannedCaseQuantity;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public BigDecimal getVolumeM3() {
            return volumeM3;
        }

        public void setVolumeM3(BigDecimal volumeM3) {
            this.volumeM3 = volumeM3;
        }

        public String getSubInventoryCode() {
            return subInventoryCode;
        }

        public void setSubInventoryCode(String subInventoryCode) {
            this.subInventoryCode = subInventoryCode;
        }

        public String getPik1() {
            return pik1;
        }

        public void setPik1(String pik1) {
            this.pik1 = pik1;
        }

        public String getPik2() {
            return pik2;
        }

        public void setPik2(String pik2) {
            this.pik2 = pik2;
        }

        public String getPik3() {
            return pik3;
        }

        public void setPik3(String pik3) {
            this.pik3 = pik3;
        }

        public String getPik4() {
            return pik4;
        }

        public void setPik4(String pik4) {
            this.pik4 = pik4;
        }

        public String getPik5() {
            return pik5;
        }

        public void setPik5(String pik5) {
            this.pik5 = pik5;
        }

        public String getPik6() {
            return pik6;
        }

        public void setPik6(String pik6) {
            this.pik6 = pik6;
        }

        public String getPik7() {
            return pik7;
        }

        public void setPik7(String pik7) {
            this.pik7 = pik7;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public List<String> getCli() {
            return cli;
        }

        public void setCli(List<String> cli) {
            this.cli = cli;
        }

        public List<String> getCfg() {
            return cfg;
        }

        public void setCfg(List<String> cfg) {
            this.cfg = cfg;
        }

        public List<BigDecimal> getCni() {
            return cni;
        }

        public void setCni(List<BigDecimal> cni) {
            this.cni = cni;
        }
    }
}