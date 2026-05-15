package jp.co.nittsu.gwh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Binds gwh-config.yml → gwh.master.*
 *
 * Comprehensive configuration covering all GWH master data:
 *   - PIK (Picking Key 1-7) slot definitions + defaults + barcode
 *   - Customer master defaults, UI, custom labels
 *   - Product master defaults, UI, custom labels
 *   - Transaction kind defaults, standard kinds, UI
 *   - Warehouse master defaults
 *   - Location master defaults, UI
 *   - Code master standard kinds
 *   - Numbering master standard codes
 *   - Reason master standard codes
 *   - Status master defaults, business codes, status progressions
 *   - Environment parameters
 *   - Organization defaults
 *   - Serial master defaults
 *
 * Source: GWH_Git/GWH/GwhCommon/GwhCommon-api/.../common/cm/
 */
@Configuration
@ConfigurationProperties(prefix = "gwh.master")
public class GwhMasterProperties {

    private Pik pik = new Pik();
    private Customer customer = new Customer();
    private Transaction transaction = new Transaction();
    private Product product = new Product();
    private Warehouse warehouse = new Warehouse();
    private Location location = new Location();
    private Code code = new Code();
    private Numbering numbering = new Numbering();
    private Reason reason = new Reason();
    private Status status = new Status();
    private Environment environment = new Environment();
    private Organization organization = new Organization();
    private Serial serial = new Serial();

    // ================================================================
    // PIK (Picking Key)
    // Legacy: cm/cmpik — GwhCmpikDTO (38 fields)
    // Table:  GWH_TM_PIK
    // ================================================================
    public static class Pik {
        private List<PikSlot> slots = new ArrayList<>();
        private PikDefaults defaults = new PikDefaults();
        private PikBarcode barcode = new PikBarcode();

        public List<PikSlot> getSlots() { return slots; }
        public void setSlots(List<PikSlot> slots) { this.slots = slots; }
        public PikDefaults getDefaults() { return defaults; }
        public void setDefaults(PikDefaults v) { this.defaults = v; }
        public PikBarcode getBarcode() { return barcode; }
        public void setBarcode(PikBarcode v) { this.barcode = v; }
    }

    public static class PikSlot {
        private int slot;
        private String columnSuffix;
        private String label;
        private String labelJa;
        private boolean enabled;
        private boolean required;
        private String dataType;
        private int maxLength;
        private boolean allocationKey;
        private String description;

        public int getSlot() { return slot; }
        public void setSlot(int slot) { this.slot = slot; }
        public String getColumnSuffix() { return columnSuffix; }
        public void setColumnSuffix(String v) { this.columnSuffix = v; }
        public String getLabel() { return label; }
        public void setLabel(String v) { this.label = v; }
        public String getLabelJa() { return labelJa; }
        public void setLabelJa(String v) { this.labelJa = v; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean v) { this.enabled = v; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean v) { this.required = v; }
        public String getDataType() { return dataType; }
        public void setDataType(String v) { this.dataType = v; }
        public int getMaxLength() { return maxLength; }
        public void setMaxLength(int v) { this.maxLength = v; }
        public boolean isAllocationKey() { return allocationKey; }
        public void setAllocationKey(boolean v) { this.allocationKey = v; }
        public String getDescription() { return description; }
        public void setDescription(String v) { this.description = v; }
    }

    public static class PikDefaults {
        private String useFlag = "1";
        private String typeKind = "01";
        private String singleGroupDigit = "";
        private String inputFormKind = "01";
        private String allocationPartKind = "01";
        private String inputListKind = "01";
        private String displayShippingFlag = "0";
        private String autoFlag = "0";
        private String duplicateDetailFlag = "0";
        private String availabilitySortKind = "01";
        private String pickingSortKind = "01";
        private String packageSortKind = "01";
        private String locationSortKind = "01";
        private String locationCodeSortKind = "01";
        private String releaseSortKind = "01";
        private String format = "";

        public String getUseFlag() { return useFlag; }
        public void setUseFlag(String v) { this.useFlag = v; }
        public String getTypeKind() { return typeKind; }
        public void setTypeKind(String v) { this.typeKind = v; }
        public String getSingleGroupDigit() { return singleGroupDigit; }
        public void setSingleGroupDigit(String v) { this.singleGroupDigit = v; }
        public String getInputFormKind() { return inputFormKind; }
        public void setInputFormKind(String v) { this.inputFormKind = v; }
        public String getAllocationPartKind() { return allocationPartKind; }
        public void setAllocationPartKind(String v) { this.allocationPartKind = v; }
        public String getInputListKind() { return inputListKind; }
        public void setInputListKind(String v) { this.inputListKind = v; }
        public String getDisplayShippingFlag() { return displayShippingFlag; }
        public void setDisplayShippingFlag(String v) { this.displayShippingFlag = v; }
        public String getAutoFlag() { return autoFlag; }
        public void setAutoFlag(String v) { this.autoFlag = v; }
        public String getDuplicateDetailFlag() { return duplicateDetailFlag; }
        public void setDuplicateDetailFlag(String v) { this.duplicateDetailFlag = v; }
        public String getAvailabilitySortKind() { return availabilitySortKind; }
        public void setAvailabilitySortKind(String v) { this.availabilitySortKind = v; }
        public String getPickingSortKind() { return pickingSortKind; }
        public void setPickingSortKind(String v) { this.pickingSortKind = v; }
        public String getPackageSortKind() { return packageSortKind; }
        public void setPackageSortKind(String v) { this.packageSortKind = v; }
        public String getLocationSortKind() { return locationSortKind; }
        public void setLocationSortKind(String v) { this.locationSortKind = v; }
        public String getLocationCodeSortKind() { return locationCodeSortKind; }
        public void setLocationCodeSortKind(String v) { this.locationCodeSortKind = v; }
        public String getReleaseSortKind() { return releaseSortKind; }
        public void setReleaseSortKind(String v) { this.releaseSortKind = v; }
        public String getFormat() { return format; }
        public void setFormat(String v) { this.format = v; }
    }

    public static class PikBarcode {
        private String barcodeKind = "";
        private int startPosition = 0;
        private int fieldLength = 0;
        private String separator = "";
        private String positionValue = "";

        public String getBarcodeKind() { return barcodeKind; }
        public void setBarcodeKind(String v) { this.barcodeKind = v; }
        public int getStartPosition() { return startPosition; }
        public void setStartPosition(int v) { this.startPosition = v; }
        public int getFieldLength() { return fieldLength; }
        public void setFieldLength(int v) { this.fieldLength = v; }
        public String getSeparator() { return separator; }
        public void setSeparator(String v) { this.separator = v; }
        public String getPositionValue() { return positionValue; }
        public void setPositionValue(String v) { this.positionValue = v; }
    }

    // ================================================================
    // Customer Master
    // Legacy: cm/cmcus — GwhCmcusDTO (223 fields)
    // Table:  GWH_TM_CUST
    // ================================================================
    public static class Customer {
        private CustomerDefaults defaults = new CustomerDefaults();
        private CustomerUi ui = new CustomerUi();

        public CustomerDefaults getDefaults() { return defaults; }
        public void setDefaults(CustomerDefaults v) { this.defaults = v; }
        public CustomerUi getUi() { return ui; }
        public void setUi(CustomerUi v) { this.ui = v; }
    }

    public static class CustomerDefaults {
        private String stockControlKind = "";
        private String displayKind = "01";
        private String crossDockEnabled = "0";
        private String leadTimeDlFlag = "0";
        private int historyRetentionDays = 365;
        private int cumulativeRetentionDays = 730;
        private String productAddressDisplayKind = "01";
        private String cargoIdDisplayKind = "01";
        private String abbreviationPrintKind = "01";
        private String barcodePrintKind = "01";
        private String locationSortKind = "01";
        private String pieceCountGroupFlag = "0";
        private String resultSortDisplayKind = "01";
        private String productSortDisplayKind = "01";
        private String arrivalArea = "";
        private String arrivalRack = "";
        private String arrivalPosition = "";
        private String arrivalLevel = "";
        private String allocationBackArea = "";
        private String allocationBackRack = "";
        private String allocationBackPosition = "";
        private String allocationBackLevel = "";
        private String kittingProcessArea = "";
        private String kittingProcessRack = "";
        private String kittingProcessPosition = "";
        private String kittingProcessLevel = "";
        private String unreplenishKind = "01";
        private String unreplenishFlag = "0";
        private String unreplenishArea = "";
        private String unreplenishRack = "";
        private String unreplenishPosition = "";
        private String unreplenishLevel = "";
        private String arrivalInspectionQtyPermit = "0";
        private String pickingQtyPermit = "0";
        private String inventoryQtyPermit = "0";
        private String receivingInspectionQtyPermit = "0";
        private String shippingInspectionQtyPermit = "0";
        private String arrivalInvoicePermit = "0";
        private String arrivalRefPermit = "0";
        private String arrivalPoPermit = "0";
        private String shippingInvoicePermit = "0";
        private String shippingRefPermit = "0";
        private String shippingPoPermit = "0";
        private String serialRequestQtyPermit = "0";
        private String loadQuantityPermit = "0";
        private String locationChangeQtyPermit = "0";
        private String displayArrivalTemplate = "01";
        private String displayUnplanInTemplate = "01";
        private String displayShippingTemplate = "01";
        private String displayUnplanOutTemplate = "01";
        private String displayStockAdjustTemplate = "01";
        private String displayRelocationTemplate = "01";
        private String displayPartitionTemplate = "01";
        private String displayKittingAsmTemplate = "01";
        private String displayKittingSplitTemplate = "01";
        private String displayBondArrTemplate = "01";
        private String displayBondShipTemplate = "01";
        private String shipperCode = "";
        private String shipperCountryCode = "";
        private String shipperCityCode = "";
        private String shipperStateCode = "";
        private String allocateGroupingEnabled = "0";
        private String consolidatePartialAlloc = "0";
        private String allocatePriorityKind = "01";
        private String allocatePriorityPikKind = "01";
        private String getKind = "01";
        private String subInventoryEnabled = "0";
        private String operationDisplayFlag = "0";
        private String allocationPermitFlag = "0";
        private String swapFlag = "0";
        private String batchGroupFlag = "0";
        private String autoRegisterProductFlag = "0";
        private String pieceManagementEnabled = "0";
        private String splitManagementEnabled = "0";
        private String deliveryManagementEnabled = "0";
        private String cargoConfirmArrival = "0";
        private String cargoConfirmShipping = "0";
        private String deliveryInsuranceKind = "01";
        private int deliveryInsuranceAmount = 0;
        private String hotmanCode = "";
        private String deliveryGenerateFlag = "0";
        private String kpiEnabled = "0";
        private String kpiJudgmentKind = "01";
        private String displayBarcodeKind = "01";
        private String barcodeKind = "";
        private int barcodeStartPosition = 0;
        private int barcodeFieldLength = 0;
        private String barcodeSeparator = "";
        private String barcodePositionValue = "";
        private String screenSortArrival = "01";
        private String screenSortPicking = "01";
        private String screenSortShipping = "01";
        private String screenSortStock = "01";
        private String screenSortLocation = "01";
        private String screenSortSerial = "01";
        private String stockLedgerKey1 = "";
        private String stockLedgerKey2 = "";
        private String stockLedgerKey3 = "";
        private String stockLedgerKey4 = "";
        private String stockLedgerKey5 = "";
        private String stockLedgerKey6 = "";
        private String stockLedgerKey7 = "";
        private String stockLedgerKey8 = "";
        private String allocStockLedgerKey1 = "";
        private String allocStockLedgerKey2 = "";
        private String allocStockLedgerKey3 = "";
        private String allocStockLedgerKey4 = "";
        private String allocStockLedgerKey5 = "";
        private String allocStockLedgerKey6 = "";
        private String allocStockLedgerKey7 = "";
        private String allocStockLedgerKey8 = "";
        private String palletControlKind = "01";
        private String rotationPrintKind = "01";
        private String pieceCertificateFlag = "0";
        private String yymmlockFlag = "0";
        private String autoCarryFlag = "0";
        private String cargoIdOutputFlag = "0";
        private String subInventoryOverrideFlag = "0";
        private String serialLookupFlag = "0";
        private String palletAutoCarryFlag = "0";
        private String appendPartialConfirmFlag = "0";
        private String damageReportFlag = "0";
        private String arrivalItemInputKind = "01";
        private String arrivalStockInputKind = "01";

        public String getStockControlKind() { return stockControlKind; }
        public void setStockControlKind(String v) { this.stockControlKind = v; }
        public String getDisplayKind() { return displayKind; }
        public void setDisplayKind(String v) { this.displayKind = v; }
        public String getCrossDockEnabled() { return crossDockEnabled; }
        public void setCrossDockEnabled(String v) { this.crossDockEnabled = v; }
        public String getLeadTimeDlFlag() { return leadTimeDlFlag; }
        public void setLeadTimeDlFlag(String v) { this.leadTimeDlFlag = v; }
        public int getHistoryRetentionDays() { return historyRetentionDays; }
        public void setHistoryRetentionDays(int v) { this.historyRetentionDays = v; }
        public int getCumulativeRetentionDays() { return cumulativeRetentionDays; }
        public void setCumulativeRetentionDays(int v) { this.cumulativeRetentionDays = v; }
        public String getProductAddressDisplayKind() { return productAddressDisplayKind; }
        public void setProductAddressDisplayKind(String v) { this.productAddressDisplayKind = v; }
        public String getCargoIdDisplayKind() { return cargoIdDisplayKind; }
        public void setCargoIdDisplayKind(String v) { this.cargoIdDisplayKind = v; }
        public String getAbbreviationPrintKind() { return abbreviationPrintKind; }
        public void setAbbreviationPrintKind(String v) { this.abbreviationPrintKind = v; }
        public String getBarcodePrintKind() { return barcodePrintKind; }
        public void setBarcodePrintKind(String v) { this.barcodePrintKind = v; }
        public String getLocationSortKind() { return locationSortKind; }
        public void setLocationSortKind(String v) { this.locationSortKind = v; }
        public String getPieceCountGroupFlag() { return pieceCountGroupFlag; }
        public void setPieceCountGroupFlag(String v) { this.pieceCountGroupFlag = v; }
        public String getResultSortDisplayKind() { return resultSortDisplayKind; }
        public void setResultSortDisplayKind(String v) { this.resultSortDisplayKind = v; }
        public String getProductSortDisplayKind() { return productSortDisplayKind; }
        public void setProductSortDisplayKind(String v) { this.productSortDisplayKind = v; }
        public String getArrivalArea() { return arrivalArea; }
        public void setArrivalArea(String v) { this.arrivalArea = v; }
        public String getArrivalRack() { return arrivalRack; }
        public void setArrivalRack(String v) { this.arrivalRack = v; }
        public String getArrivalPosition() { return arrivalPosition; }
        public void setArrivalPosition(String v) { this.arrivalPosition = v; }
        public String getArrivalLevel() { return arrivalLevel; }
        public void setArrivalLevel(String v) { this.arrivalLevel = v; }
        public String getAllocationBackArea() { return allocationBackArea; }
        public void setAllocationBackArea(String v) { this.allocationBackArea = v; }
        public String getAllocationBackRack() { return allocationBackRack; }
        public void setAllocationBackRack(String v) { this.allocationBackRack = v; }
        public String getAllocationBackPosition() { return allocationBackPosition; }
        public void setAllocationBackPosition(String v) { this.allocationBackPosition = v; }
        public String getAllocationBackLevel() { return allocationBackLevel; }
        public void setAllocationBackLevel(String v) { this.allocationBackLevel = v; }
        public String getKittingProcessArea() { return kittingProcessArea; }
        public void setKittingProcessArea(String v) { this.kittingProcessArea = v; }
        public String getKittingProcessRack() { return kittingProcessRack; }
        public void setKittingProcessRack(String v) { this.kittingProcessRack = v; }
        public String getKittingProcessPosition() { return kittingProcessPosition; }
        public void setKittingProcessPosition(String v) { this.kittingProcessPosition = v; }
        public String getKittingProcessLevel() { return kittingProcessLevel; }
        public void setKittingProcessLevel(String v) { this.kittingProcessLevel = v; }
        public String getUnreplenishKind() { return unreplenishKind; }
        public void setUnreplenishKind(String v) { this.unreplenishKind = v; }
        public String getUnreplenishFlag() { return unreplenishFlag; }
        public void setUnreplenishFlag(String v) { this.unreplenishFlag = v; }
        public String getUnreplenishArea() { return unreplenishArea; }
        public void setUnreplenishArea(String v) { this.unreplenishArea = v; }
        public String getUnreplenishRack() { return unreplenishRack; }
        public void setUnreplenishRack(String v) { this.unreplenishRack = v; }
        public String getUnreplenishPosition() { return unreplenishPosition; }
        public void setUnreplenishPosition(String v) { this.unreplenishPosition = v; }
        public String getUnreplenishLevel() { return unreplenishLevel; }
        public void setUnreplenishLevel(String v) { this.unreplenishLevel = v; }
        public String getArrivalInspectionQtyPermit() { return arrivalInspectionQtyPermit; }
        public void setArrivalInspectionQtyPermit(String v) { this.arrivalInspectionQtyPermit = v; }
        public String getPickingQtyPermit() { return pickingQtyPermit; }
        public void setPickingQtyPermit(String v) { this.pickingQtyPermit = v; }
        public String getInventoryQtyPermit() { return inventoryQtyPermit; }
        public void setInventoryQtyPermit(String v) { this.inventoryQtyPermit = v; }
        public String getReceivingInspectionQtyPermit() { return receivingInspectionQtyPermit; }
        public void setReceivingInspectionQtyPermit(String v) { this.receivingInspectionQtyPermit = v; }
        public String getShippingInspectionQtyPermit() { return shippingInspectionQtyPermit; }
        public void setShippingInspectionQtyPermit(String v) { this.shippingInspectionQtyPermit = v; }
        public String getArrivalInvoicePermit() { return arrivalInvoicePermit; }
        public void setArrivalInvoicePermit(String v) { this.arrivalInvoicePermit = v; }
        public String getArrivalRefPermit() { return arrivalRefPermit; }
        public void setArrivalRefPermit(String v) { this.arrivalRefPermit = v; }
        public String getArrivalPoPermit() { return arrivalPoPermit; }
        public void setArrivalPoPermit(String v) { this.arrivalPoPermit = v; }
        public String getShippingInvoicePermit() { return shippingInvoicePermit; }
        public void setShippingInvoicePermit(String v) { this.shippingInvoicePermit = v; }
        public String getShippingRefPermit() { return shippingRefPermit; }
        public void setShippingRefPermit(String v) { this.shippingRefPermit = v; }
        public String getShippingPoPermit() { return shippingPoPermit; }
        public void setShippingPoPermit(String v) { this.shippingPoPermit = v; }
        public String getSerialRequestQtyPermit() { return serialRequestQtyPermit; }
        public void setSerialRequestQtyPermit(String v) { this.serialRequestQtyPermit = v; }
        public String getLoadQuantityPermit() { return loadQuantityPermit; }
        public void setLoadQuantityPermit(String v) { this.loadQuantityPermit = v; }
        public String getLocationChangeQtyPermit() { return locationChangeQtyPermit; }
        public void setLocationChangeQtyPermit(String v) { this.locationChangeQtyPermit = v; }
        public String getDisplayArrivalTemplate() { return displayArrivalTemplate; }
        public void setDisplayArrivalTemplate(String v) { this.displayArrivalTemplate = v; }
        public String getDisplayUnplanInTemplate() { return displayUnplanInTemplate; }
        public void setDisplayUnplanInTemplate(String v) { this.displayUnplanInTemplate = v; }
        public String getDisplayShippingTemplate() { return displayShippingTemplate; }
        public void setDisplayShippingTemplate(String v) { this.displayShippingTemplate = v; }
        public String getDisplayUnplanOutTemplate() { return displayUnplanOutTemplate; }
        public void setDisplayUnplanOutTemplate(String v) { this.displayUnplanOutTemplate = v; }
        public String getDisplayStockAdjustTemplate() { return displayStockAdjustTemplate; }
        public void setDisplayStockAdjustTemplate(String v) { this.displayStockAdjustTemplate = v; }
        public String getDisplayRelocationTemplate() { return displayRelocationTemplate; }
        public void setDisplayRelocationTemplate(String v) { this.displayRelocationTemplate = v; }
        public String getDisplayPartitionTemplate() { return displayPartitionTemplate; }
        public void setDisplayPartitionTemplate(String v) { this.displayPartitionTemplate = v; }
        public String getDisplayKittingAsmTemplate() { return displayKittingAsmTemplate; }
        public void setDisplayKittingAsmTemplate(String v) { this.displayKittingAsmTemplate = v; }
        public String getDisplayKittingSplitTemplate() { return displayKittingSplitTemplate; }
        public void setDisplayKittingSplitTemplate(String v) { this.displayKittingSplitTemplate = v; }
        public String getDisplayBondArrTemplate() { return displayBondArrTemplate; }
        public void setDisplayBondArrTemplate(String v) { this.displayBondArrTemplate = v; }
        public String getDisplayBondShipTemplate() { return displayBondShipTemplate; }
        public void setDisplayBondShipTemplate(String v) { this.displayBondShipTemplate = v; }
        public String getShipperCode() { return shipperCode; }
        public void setShipperCode(String v) { this.shipperCode = v; }
        public String getShipperCountryCode() { return shipperCountryCode; }
        public void setShipperCountryCode(String v) { this.shipperCountryCode = v; }
        public String getShipperCityCode() { return shipperCityCode; }
        public void setShipperCityCode(String v) { this.shipperCityCode = v; }
        public String getShipperStateCode() { return shipperStateCode; }
        public void setShipperStateCode(String v) { this.shipperStateCode = v; }
        public String getAllocateGroupingEnabled() { return allocateGroupingEnabled; }
        public void setAllocateGroupingEnabled(String v) { this.allocateGroupingEnabled = v; }
        public String getConsolidatePartialAlloc() { return consolidatePartialAlloc; }
        public void setConsolidatePartialAlloc(String v) { this.consolidatePartialAlloc = v; }
        public String getAllocatePriorityKind() { return allocatePriorityKind; }
        public void setAllocatePriorityKind(String v) { this.allocatePriorityKind = v; }
        public String getAllocatePriorityPikKind() { return allocatePriorityPikKind; }
        public void setAllocatePriorityPikKind(String v) { this.allocatePriorityPikKind = v; }
        public String getGetKind() { return getKind; }
        public void setGetKind(String v) { this.getKind = v; }
        public String getSubInventoryEnabled() { return subInventoryEnabled; }
        public void setSubInventoryEnabled(String v) { this.subInventoryEnabled = v; }
        public String getOperationDisplayFlag() { return operationDisplayFlag; }
        public void setOperationDisplayFlag(String v) { this.operationDisplayFlag = v; }
        public String getAllocationPermitFlag() { return allocationPermitFlag; }
        public void setAllocationPermitFlag(String v) { this.allocationPermitFlag = v; }
        public String getSwapFlag() { return swapFlag; }
        public void setSwapFlag(String v) { this.swapFlag = v; }
        public String getBatchGroupFlag() { return batchGroupFlag; }
        public void setBatchGroupFlag(String v) { this.batchGroupFlag = v; }
        public String getAutoRegisterProductFlag() { return autoRegisterProductFlag; }
        public void setAutoRegisterProductFlag(String v) { this.autoRegisterProductFlag = v; }
        public String getPieceManagementEnabled() { return pieceManagementEnabled; }
        public void setPieceManagementEnabled(String v) { this.pieceManagementEnabled = v; }
        public String getSplitManagementEnabled() { return splitManagementEnabled; }
        public void setSplitManagementEnabled(String v) { this.splitManagementEnabled = v; }
        public String getDeliveryManagementEnabled() { return deliveryManagementEnabled; }
        public void setDeliveryManagementEnabled(String v) { this.deliveryManagementEnabled = v; }
        public String getCargoConfirmArrival() { return cargoConfirmArrival; }
        public void setCargoConfirmArrival(String v) { this.cargoConfirmArrival = v; }
        public String getCargoConfirmShipping() { return cargoConfirmShipping; }
        public void setCargoConfirmShipping(String v) { this.cargoConfirmShipping = v; }
        public String getDeliveryInsuranceKind() { return deliveryInsuranceKind; }
        public void setDeliveryInsuranceKind(String v) { this.deliveryInsuranceKind = v; }
        public int getDeliveryInsuranceAmount() { return deliveryInsuranceAmount; }
        public void setDeliveryInsuranceAmount(int v) { this.deliveryInsuranceAmount = v; }
        public String getHotmanCode() { return hotmanCode; }
        public void setHotmanCode(String v) { this.hotmanCode = v; }
        public String getDeliveryGenerateFlag() { return deliveryGenerateFlag; }
        public void setDeliveryGenerateFlag(String v) { this.deliveryGenerateFlag = v; }
        public String getKpiEnabled() { return kpiEnabled; }
        public void setKpiEnabled(String v) { this.kpiEnabled = v; }
        public String getKpiJudgmentKind() { return kpiJudgmentKind; }
        public void setKpiJudgmentKind(String v) { this.kpiJudgmentKind = v; }
        public String getDisplayBarcodeKind() { return displayBarcodeKind; }
        public void setDisplayBarcodeKind(String v) { this.displayBarcodeKind = v; }
        public String getBarcodeKind() { return barcodeKind; }
        public void setBarcodeKind(String v) { this.barcodeKind = v; }
        public int getBarcodeStartPosition() { return barcodeStartPosition; }
        public void setBarcodeStartPosition(int v) { this.barcodeStartPosition = v; }
        public int getBarcodeFieldLength() { return barcodeFieldLength; }
        public void setBarcodeFieldLength(int v) { this.barcodeFieldLength = v; }
        public String getBarcodeSeparator() { return barcodeSeparator; }
        public void setBarcodeSeparator(String v) { this.barcodeSeparator = v; }
        public String getBarcodePositionValue() { return barcodePositionValue; }
        public void setBarcodePositionValue(String v) { this.barcodePositionValue = v; }
        public String getScreenSortArrival() { return screenSortArrival; }
        public void setScreenSortArrival(String v) { this.screenSortArrival = v; }
        public String getScreenSortPicking() { return screenSortPicking; }
        public void setScreenSortPicking(String v) { this.screenSortPicking = v; }
        public String getScreenSortShipping() { return screenSortShipping; }
        public void setScreenSortShipping(String v) { this.screenSortShipping = v; }
        public String getScreenSortStock() { return screenSortStock; }
        public void setScreenSortStock(String v) { this.screenSortStock = v; }
        public String getScreenSortLocation() { return screenSortLocation; }
        public void setScreenSortLocation(String v) { this.screenSortLocation = v; }
        public String getScreenSortSerial() { return screenSortSerial; }
        public void setScreenSortSerial(String v) { this.screenSortSerial = v; }
        public String getStockLedgerKey1() { return stockLedgerKey1; }
        public void setStockLedgerKey1(String v) { this.stockLedgerKey1 = v; }
        public String getStockLedgerKey2() { return stockLedgerKey2; }
        public void setStockLedgerKey2(String v) { this.stockLedgerKey2 = v; }
        public String getStockLedgerKey3() { return stockLedgerKey3; }
        public void setStockLedgerKey3(String v) { this.stockLedgerKey3 = v; }
        public String getStockLedgerKey4() { return stockLedgerKey4; }
        public void setStockLedgerKey4(String v) { this.stockLedgerKey4 = v; }
        public String getStockLedgerKey5() { return stockLedgerKey5; }
        public void setStockLedgerKey5(String v) { this.stockLedgerKey5 = v; }
        public String getStockLedgerKey6() { return stockLedgerKey6; }
        public void setStockLedgerKey6(String v) { this.stockLedgerKey6 = v; }
        public String getStockLedgerKey7() { return stockLedgerKey7; }
        public void setStockLedgerKey7(String v) { this.stockLedgerKey7 = v; }
        public String getStockLedgerKey8() { return stockLedgerKey8; }
        public void setStockLedgerKey8(String v) { this.stockLedgerKey8 = v; }
        public String getAllocStockLedgerKey1() { return allocStockLedgerKey1; }
        public void setAllocStockLedgerKey1(String v) { this.allocStockLedgerKey1 = v; }
        public String getAllocStockLedgerKey2() { return allocStockLedgerKey2; }
        public void setAllocStockLedgerKey2(String v) { this.allocStockLedgerKey2 = v; }
        public String getAllocStockLedgerKey3() { return allocStockLedgerKey3; }
        public void setAllocStockLedgerKey3(String v) { this.allocStockLedgerKey3 = v; }
        public String getAllocStockLedgerKey4() { return allocStockLedgerKey4; }
        public void setAllocStockLedgerKey4(String v) { this.allocStockLedgerKey4 = v; }
        public String getAllocStockLedgerKey5() { return allocStockLedgerKey5; }
        public void setAllocStockLedgerKey5(String v) { this.allocStockLedgerKey5 = v; }
        public String getAllocStockLedgerKey6() { return allocStockLedgerKey6; }
        public void setAllocStockLedgerKey6(String v) { this.allocStockLedgerKey6 = v; }
        public String getAllocStockLedgerKey7() { return allocStockLedgerKey7; }
        public void setAllocStockLedgerKey7(String v) { this.allocStockLedgerKey7 = v; }
        public String getAllocStockLedgerKey8() { return allocStockLedgerKey8; }
        public void setAllocStockLedgerKey8(String v) { this.allocStockLedgerKey8 = v; }
        public String getPalletControlKind() { return palletControlKind; }
        public void setPalletControlKind(String v) { this.palletControlKind = v; }
        public String getRotationPrintKind() { return rotationPrintKind; }
        public void setRotationPrintKind(String v) { this.rotationPrintKind = v; }
        public String getPieceCertificateFlag() { return pieceCertificateFlag; }
        public void setPieceCertificateFlag(String v) { this.pieceCertificateFlag = v; }
        public String getYymmlockFlag() { return yymmlockFlag; }
        public void setYymmlockFlag(String v) { this.yymmlockFlag = v; }
        public String getAutoCarryFlag() { return autoCarryFlag; }
        public void setAutoCarryFlag(String v) { this.autoCarryFlag = v; }
        public String getCargoIdOutputFlag() { return cargoIdOutputFlag; }
        public void setCargoIdOutputFlag(String v) { this.cargoIdOutputFlag = v; }
        public String getSubInventoryOverrideFlag() { return subInventoryOverrideFlag; }
        public void setSubInventoryOverrideFlag(String v) { this.subInventoryOverrideFlag = v; }
        public String getSerialLookupFlag() { return serialLookupFlag; }
        public void setSerialLookupFlag(String v) { this.serialLookupFlag = v; }
        public String getPalletAutoCarryFlag() { return palletAutoCarryFlag; }
        public void setPalletAutoCarryFlag(String v) { this.palletAutoCarryFlag = v; }
        public String getAppendPartialConfirmFlag() { return appendPartialConfirmFlag; }
        public void setAppendPartialConfirmFlag(String v) { this.appendPartialConfirmFlag = v; }
        public String getDamageReportFlag() { return damageReportFlag; }
        public void setDamageReportFlag(String v) { this.damageReportFlag = v; }
        public String getArrivalItemInputKind() { return arrivalItemInputKind; }
        public void setArrivalItemInputKind(String v) { this.arrivalItemInputKind = v; }
        public String getArrivalStockInputKind() { return arrivalStockInputKind; }
        public void setArrivalStockInputKind(String v) { this.arrivalStockInputKind = v; }
    }

    public static class CustomerUi {
        private boolean showShipperSection = true;
        private boolean showWeekendSettings = true;
        private boolean showBillingSection = true;
        private boolean showCustomItemsSection = true;
        private boolean showCustomFlagsSection = true;
        private boolean showDeliverySection = true;
        private boolean showBarcodeScanSection = true;
        private boolean showLocationDefaultsSection = true;
        private boolean showSortingKeysSection = true;
        private boolean showStockLedgerKeysSection = true;
        private Map<String, String> customItemLabels = new HashMap<>();
        private Map<String, String> customFlagLabels = new HashMap<>();
        private Map<String, String> customNumericLabels = new HashMap<>();

        public boolean isShowShipperSection() { return showShipperSection; }
        public void setShowShipperSection(boolean v) { this.showShipperSection = v; }
        public boolean isShowWeekendSettings() { return showWeekendSettings; }
        public void setShowWeekendSettings(boolean v) { this.showWeekendSettings = v; }
        public boolean isShowBillingSection() { return showBillingSection; }
        public void setShowBillingSection(boolean v) { this.showBillingSection = v; }
        public boolean isShowCustomItemsSection() { return showCustomItemsSection; }
        public void setShowCustomItemsSection(boolean v) { this.showCustomItemsSection = v; }
        public boolean isShowCustomFlagsSection() { return showCustomFlagsSection; }
        public void setShowCustomFlagsSection(boolean v) { this.showCustomFlagsSection = v; }
        public boolean isShowDeliverySection() { return showDeliverySection; }
        public void setShowDeliverySection(boolean v) { this.showDeliverySection = v; }
        public boolean isShowBarcodeScanSection() { return showBarcodeScanSection; }
        public void setShowBarcodeScanSection(boolean v) { this.showBarcodeScanSection = v; }
        public boolean isShowLocationDefaultsSection() { return showLocationDefaultsSection; }
        public void setShowLocationDefaultsSection(boolean v) { this.showLocationDefaultsSection = v; }
        public boolean isShowSortingKeysSection() { return showSortingKeysSection; }
        public void setShowSortingKeysSection(boolean v) { this.showSortingKeysSection = v; }
        public boolean isShowStockLedgerKeysSection() { return showStockLedgerKeysSection; }
        public void setShowStockLedgerKeysSection(boolean v) { this.showStockLedgerKeysSection = v; }
        public Map<String, String> getCustomItemLabels() { return customItemLabels; }
        public void setCustomItemLabels(Map<String, String> v) { this.customItemLabels = v; }
        public Map<String, String> getCustomFlagLabels() { return customFlagLabels; }
        public void setCustomFlagLabels(Map<String, String> v) { this.customFlagLabels = v; }
        public Map<String, String> getCustomNumericLabels() { return customNumericLabels; }
        public void setCustomNumericLabels(Map<String, String> v) { this.customNumericLabels = v; }
    }

    // ================================================================
    // Transaction Kind
    // Legacy: cm/cmtrn — GwhCmtrnDTO (81 fields)
    // Table:  GWH_TM_TRN
    // ================================================================
    public static class Transaction {
        private TransactionDefaults defaults = new TransactionDefaults();
        private Map<String, String> standardKinds = new HashMap<>();
        private TransactionUi ui = new TransactionUi();

        public TransactionDefaults getDefaults() { return defaults; }
        public void setDefaults(TransactionDefaults v) { this.defaults = v; }
        public Map<String, String> getStandardKinds() { return standardKinds; }
        public void setStandardKinds(Map<String, String> v) { this.standardKinds = v; }
        public TransactionUi getUi() { return ui; }
        public void setUi(TransactionUi v) { this.ui = v; }
    }

    public static class TransactionDefaults {
        private String arrivalChargeCalc = "0";
        private String shippingChargeCalc = "0";
        private String storageChargeCalc = "0";
        private String disposalChargeCalc = "0";
        private String reasonCodeRequired = "0";
        private String pickKeyDiscrepancyPermit = "0";
        private String surplusPermitAtArrival = "0";
        private String shortagePermitAtArrival = "0";
        private String arrivalUsage = "0";
        private String unplanInUsage = "0";
        private String shippingUsage = "0";
        private String unplanOutUsage = "0";
        private String stockAdjustUsage = "0";
        private String relocationUsage = "0";
        private String partitionUsage = "0";
        private String kittingAssemblyUsage = "0";
        private String kittingSplitUsage = "0";
        private String bondArrivalUsage = "0";
        private String bondShippingUsage = "0";
        private String kpiShippingFrequencyTarget = "0";
        private String bondControlFlag = "0";
        private String partialDataAutoCreate = "0";
        private String displayKind = "";

        public String getArrivalChargeCalc() { return arrivalChargeCalc; }
        public void setArrivalChargeCalc(String v) { this.arrivalChargeCalc = v; }
        public String getShippingChargeCalc() { return shippingChargeCalc; }
        public void setShippingChargeCalc(String v) { this.shippingChargeCalc = v; }
        public String getStorageChargeCalc() { return storageChargeCalc; }
        public void setStorageChargeCalc(String v) { this.storageChargeCalc = v; }
        public String getDisposalChargeCalc() { return disposalChargeCalc; }
        public void setDisposalChargeCalc(String v) { this.disposalChargeCalc = v; }
        public String getReasonCodeRequired() { return reasonCodeRequired; }
        public void setReasonCodeRequired(String v) { this.reasonCodeRequired = v; }
        public String getPickKeyDiscrepancyPermit() { return pickKeyDiscrepancyPermit; }
        public void setPickKeyDiscrepancyPermit(String v) { this.pickKeyDiscrepancyPermit = v; }
        public String getSurplusPermitAtArrival() { return surplusPermitAtArrival; }
        public void setSurplusPermitAtArrival(String v) { this.surplusPermitAtArrival = v; }
        public String getShortagePermitAtArrival() { return shortagePermitAtArrival; }
        public void setShortagePermitAtArrival(String v) { this.shortagePermitAtArrival = v; }
        public String getArrivalUsage() { return arrivalUsage; }
        public void setArrivalUsage(String v) { this.arrivalUsage = v; }
        public String getUnplanInUsage() { return unplanInUsage; }
        public void setUnplanInUsage(String v) { this.unplanInUsage = v; }
        public String getShippingUsage() { return shippingUsage; }
        public void setShippingUsage(String v) { this.shippingUsage = v; }
        public String getUnplanOutUsage() { return unplanOutUsage; }
        public void setUnplanOutUsage(String v) { this.unplanOutUsage = v; }
        public String getStockAdjustUsage() { return stockAdjustUsage; }
        public void setStockAdjustUsage(String v) { this.stockAdjustUsage = v; }
        public String getRelocationUsage() { return relocationUsage; }
        public void setRelocationUsage(String v) { this.relocationUsage = v; }
        public String getPartitionUsage() { return partitionUsage; }
        public void setPartitionUsage(String v) { this.partitionUsage = v; }
        public String getKittingAssemblyUsage() { return kittingAssemblyUsage; }
        public void setKittingAssemblyUsage(String v) { this.kittingAssemblyUsage = v; }
        public String getKittingSplitUsage() { return kittingSplitUsage; }
        public void setKittingSplitUsage(String v) { this.kittingSplitUsage = v; }
        public String getBondArrivalUsage() { return bondArrivalUsage; }
        public void setBondArrivalUsage(String v) { this.bondArrivalUsage = v; }
        public String getBondShippingUsage() { return bondShippingUsage; }
        public void setBondShippingUsage(String v) { this.bondShippingUsage = v; }
        public String getKpiShippingFrequencyTarget() { return kpiShippingFrequencyTarget; }
        public void setKpiShippingFrequencyTarget(String v) { this.kpiShippingFrequencyTarget = v; }
        public String getBondControlFlag() { return bondControlFlag; }
        public void setBondControlFlag(String v) { this.bondControlFlag = v; }
        public String getPartialDataAutoCreate() { return partialDataAutoCreate; }
        public void setPartialDataAutoCreate(String v) { this.partialDataAutoCreate = v; }
        public String getDisplayKind() { return displayKind; }
        public void setDisplayKind(String v) { this.displayKind = v; }
    }

    public static class TransactionUi {
        private boolean showChargeSection = true;
        private boolean showModuleUsageSection = true;
        private boolean showCustomItemsSection = true;
        private boolean showCustomFlagsSection = true;
        private Map<String, String> customItemLabels = new HashMap<>();
        private Map<String, String> customFlagLabels = new HashMap<>();
        private Map<String, String> customNumericLabels = new HashMap<>();

        public boolean isShowChargeSection() { return showChargeSection; }
        public void setShowChargeSection(boolean v) { this.showChargeSection = v; }
        public boolean isShowModuleUsageSection() { return showModuleUsageSection; }
        public void setShowModuleUsageSection(boolean v) { this.showModuleUsageSection = v; }
        public boolean isShowCustomItemsSection() { return showCustomItemsSection; }
        public void setShowCustomItemsSection(boolean v) { this.showCustomItemsSection = v; }
        public boolean isShowCustomFlagsSection() { return showCustomFlagsSection; }
        public void setShowCustomFlagsSection(boolean v) { this.showCustomFlagsSection = v; }
        public Map<String, String> getCustomItemLabels() { return customItemLabels; }
        public void setCustomItemLabels(Map<String, String> v) { this.customItemLabels = v; }
        public Map<String, String> getCustomFlagLabels() { return customFlagLabels; }
        public void setCustomFlagLabels(Map<String, String> v) { this.customFlagLabels = v; }
        public Map<String, String> getCustomNumericLabels() { return customNumericLabels; }
        public void setCustomNumericLabels(Map<String, String> v) { this.customNumericLabels = v; }
    }

    // ================================================================
    // Product Master
    // Legacy: cm/cmprd — GwhCmprdDTO (201 fields)
    // Table:  GWH_TM_PROD
    // ================================================================
    public static class Product {
        private ProductDefaults defaults = new ProductDefaults();
        private ProductUi ui = new ProductUi();

        public ProductDefaults getDefaults() { return defaults; }
        public void setDefaults(ProductDefaults v) { this.defaults = v; }
        public ProductUi getUi() { return ui; }
        public void setUi(ProductUi v) { this.ui = v; }
    }

    public static class ProductDefaults {
        private int piecesPerCase = 1;
        private String allocationMethodKind = "F";
        private String stockControlFlag = "0";
        private String storageKind = "01";
        private String storageFrequency = "";
        private String autoSerialFlag = "0";
        private int stockWeightQty = 0;
        private int stockWeightPcQty = 0;
        private String hierarchy1 = "";
        private String hierarchy2 = "";
        private String hierarchy3 = "";
        private double caseWeight = 0;
        private double caseVolume = 0;
        private double caseLength = 0;
        private double caseWidth = 0;
        private double caseHeight = 0;
        private double ballWeight = 0;
        private double ballVolume = 0;
        private double ballLength = 0;
        private double ballWidth = 0;
        private double ballHeight = 0;
        private double pieceWeight = 0;
        private double pieceVolume = 0;
        private double pieceLength = 0;
        private double pieceWidth = 0;
        private double pieceHeight = 0;
        private double grossWeight = 0;
        private double netWeight = 0;
        private String uomPiece = "PCS";
        private String uomBall = "BL";
        private String uomCase = "CS";
        private String uomHeight = "CM";
        private String uomWeight = "KG";
        private String uomVolume = "M3";
        private String replenishCaseArea = "";
        private String replenishCaseRack = "";
        private String replenishCasePosition = "";
        private String replenishCaseLevel = "";
        private String replenishCaseFixed = "0";
        private String replenishPieceArea = "";
        private String replenishPieceRack = "";
        private String replenishPiecePosition = "";
        private String replenishPieceLevel = "";
        private String replenishPieceFixed = "0";
        private int replenishMaxQty = 0;
        private int replenishSeedQty = 0;
        private double depositAmount = 0;
        private String depositCurrency = "";
        private double purchaseAmount = 0;
        private String purchaseCurrency = "";
        private double salesAmount = 0;
        private String salesCurrency = "";
        private String priceCurrency = "";
        private int roundingDigit = 0;
        private String roundingKind = "01";
        private String bondControlKind = "00";
        private String hsCode = "";
        private String customerStatus = "";
        private String expiryKind = "01";
        private int expiryDays = 0;
        private int shippingDueDays = 0;
        private String shippingDueKind = "01";
        private int stockDueDays = 0;
        private String stockDueKind = "01";
        private int arrivalDueDays = 0;
        private String arrivalDueKind = "01";
        private int shippingRefDays = 0;
        private int shippingRetDays = 0;
        private String bestBeforePrintKind = "01";
        private int bestBeforePrintDays = 0;
        private String kittingProduct = "0";
        private String kittingSourceFlag = "0";
        private String psaaKind = "00";
        private int psaaDays = 0;
        private String crossDockKind = "00";
        private String chargeClassGroup = "";
        private String serialLotGetFlag = "0";
        private String serialAllocFlag = "0";
        private String caseMarkType = "";
        private String receiptBatchNum = "";
        private int productPerBallNum = 0;
        private int productMaxQty = 0;
        private int productMinQty = 0;
        private String frequencyType = "01";
        private String arrivalItemKind = "01";
        private String arrivalEndBagKind = "01";
        private String autoRegisterFromInterface = "0";
        private String calcArrivalFeeUnit = "";
        private int calcArrivalFeeRound = 0;
        private double priceArrivalFee = 0;
        private double specialPriceArrivalFee = 0;
        private String calcShippingFeeUnit = "";
        private int calcShippingFeeRound = 0;
        private double priceShippingFee = 0;
        private double specialPriceShippingFee = 0;
        private String calcStorageFeeUnit = "";
        private int calcStorageFeeRound = 0;
        private double priceStorageFee = 0;
        private double specialPriceStorageFee = 0;
        private String calcKittingFeeUnit = "";
        private int calcKittingFeeRound = 0;
        private double priceKittingFee = 0;
        private double specialPriceKittingFee = 0;
        private String calcSubFeeUnit = "";
        private int calcSubFeeRound = 0;
        private double priceSubFee = 0;
        private double specialPriceSubFee = 0;
        private String calcWarehouseFeeUnit = "";
        private int calcWarehouseFeeRound = 0;
        private double priceWarehouseFee = 0;
        private double specialPriceWarehouseFee = 0;
        private String screenSortSerial = "01";
        private String screenSortArrival = "01";
        private String screenSortPicking = "01";
        private String screenSortShipping = "01";
        private String screenSortStock = "01";
        private String screenSortLocation = "01";
        private String customSortSerial = "01";
        private String customSortArrival = "01";
        private String customSortPicking = "01";
        private String customSortShipping = "01";
        private String customSortStock = "01";
        private String customSortLocation = "01";

        public int getPiecesPerCase() { return piecesPerCase; }
        public void setPiecesPerCase(int v) { this.piecesPerCase = v; }
        public String getAllocationMethodKind() { return allocationMethodKind; }
        public void setAllocationMethodKind(String v) { this.allocationMethodKind = v; }
        public String getStockControlFlag() { return stockControlFlag; }
        public void setStockControlFlag(String v) { this.stockControlFlag = v; }
        public String getStorageKind() { return storageKind; }
        public void setStorageKind(String v) { this.storageKind = v; }
        public String getStorageFrequency() { return storageFrequency; }
        public void setStorageFrequency(String v) { this.storageFrequency = v; }
        public String getAutoSerialFlag() { return autoSerialFlag; }
        public void setAutoSerialFlag(String v) { this.autoSerialFlag = v; }
        public int getStockWeightQty() { return stockWeightQty; }
        public void setStockWeightQty(int v) { this.stockWeightQty = v; }
        public int getStockWeightPcQty() { return stockWeightPcQty; }
        public void setStockWeightPcQty(int v) { this.stockWeightPcQty = v; }
        public String getHierarchy1() { return hierarchy1; }
        public void setHierarchy1(String v) { this.hierarchy1 = v; }
        public String getHierarchy2() { return hierarchy2; }
        public void setHierarchy2(String v) { this.hierarchy2 = v; }
        public String getHierarchy3() { return hierarchy3; }
        public void setHierarchy3(String v) { this.hierarchy3 = v; }
        public double getCaseWeight() { return caseWeight; }
        public void setCaseWeight(double v) { this.caseWeight = v; }
        public double getCaseVolume() { return caseVolume; }
        public void setCaseVolume(double v) { this.caseVolume = v; }
        public double getCaseLength() { return caseLength; }
        public void setCaseLength(double v) { this.caseLength = v; }
        public double getCaseWidth() { return caseWidth; }
        public void setCaseWidth(double v) { this.caseWidth = v; }
        public double getCaseHeight() { return caseHeight; }
        public void setCaseHeight(double v) { this.caseHeight = v; }
        public double getBallWeight() { return ballWeight; }
        public void setBallWeight(double v) { this.ballWeight = v; }
        public double getBallVolume() { return ballVolume; }
        public void setBallVolume(double v) { this.ballVolume = v; }
        public double getBallLength() { return ballLength; }
        public void setBallLength(double v) { this.ballLength = v; }
        public double getBallWidth() { return ballWidth; }
        public void setBallWidth(double v) { this.ballWidth = v; }
        public double getBallHeight() { return ballHeight; }
        public void setBallHeight(double v) { this.ballHeight = v; }
        public double getPieceWeight() { return pieceWeight; }
        public void setPieceWeight(double v) { this.pieceWeight = v; }
        public double getPieceVolume() { return pieceVolume; }
        public void setPieceVolume(double v) { this.pieceVolume = v; }
        public double getPieceLength() { return pieceLength; }
        public void setPieceLength(double v) { this.pieceLength = v; }
        public double getPieceWidth() { return pieceWidth; }
        public void setPieceWidth(double v) { this.pieceWidth = v; }
        public double getPieceHeight() { return pieceHeight; }
        public void setPieceHeight(double v) { this.pieceHeight = v; }
        public double getGrossWeight() { return grossWeight; }
        public void setGrossWeight(double v) { this.grossWeight = v; }
        public double getNetWeight() { return netWeight; }
        public void setNetWeight(double v) { this.netWeight = v; }
        public String getUomPiece() { return uomPiece; }
        public void setUomPiece(String v) { this.uomPiece = v; }
        public String getUomBall() { return uomBall; }
        public void setUomBall(String v) { this.uomBall = v; }
        public String getUomCase() { return uomCase; }
        public void setUomCase(String v) { this.uomCase = v; }
        public String getUomHeight() { return uomHeight; }
        public void setUomHeight(String v) { this.uomHeight = v; }
        public String getUomWeight() { return uomWeight; }
        public void setUomWeight(String v) { this.uomWeight = v; }
        public String getUomVolume() { return uomVolume; }
        public void setUomVolume(String v) { this.uomVolume = v; }
        public String getReplenishCaseArea() { return replenishCaseArea; }
        public void setReplenishCaseArea(String v) { this.replenishCaseArea = v; }
        public String getReplenishCaseRack() { return replenishCaseRack; }
        public void setReplenishCaseRack(String v) { this.replenishCaseRack = v; }
        public String getReplenishCasePosition() { return replenishCasePosition; }
        public void setReplenishCasePosition(String v) { this.replenishCasePosition = v; }
        public String getReplenishCaseLevel() { return replenishCaseLevel; }
        public void setReplenishCaseLevel(String v) { this.replenishCaseLevel = v; }
        public String getReplenishCaseFixed() { return replenishCaseFixed; }
        public void setReplenishCaseFixed(String v) { this.replenishCaseFixed = v; }
        public String getReplenishPieceArea() { return replenishPieceArea; }
        public void setReplenishPieceArea(String v) { this.replenishPieceArea = v; }
        public String getReplenishPieceRack() { return replenishPieceRack; }
        public void setReplenishPieceRack(String v) { this.replenishPieceRack = v; }
        public String getReplenishPiecePosition() { return replenishPiecePosition; }
        public void setReplenishPiecePosition(String v) { this.replenishPiecePosition = v; }
        public String getReplenishPieceLevel() { return replenishPieceLevel; }
        public void setReplenishPieceLevel(String v) { this.replenishPieceLevel = v; }
        public String getReplenishPieceFixed() { return replenishPieceFixed; }
        public void setReplenishPieceFixed(String v) { this.replenishPieceFixed = v; }
        public int getReplenishMaxQty() { return replenishMaxQty; }
        public void setReplenishMaxQty(int v) { this.replenishMaxQty = v; }
        public int getReplenishSeedQty() { return replenishSeedQty; }
        public void setReplenishSeedQty(int v) { this.replenishSeedQty = v; }
        public double getDepositAmount() { return depositAmount; }
        public void setDepositAmount(double v) { this.depositAmount = v; }
        public String getDepositCurrency() { return depositCurrency; }
        public void setDepositCurrency(String v) { this.depositCurrency = v; }
        public double getPurchaseAmount() { return purchaseAmount; }
        public void setPurchaseAmount(double v) { this.purchaseAmount = v; }
        public String getPurchaseCurrency() { return purchaseCurrency; }
        public void setPurchaseCurrency(String v) { this.purchaseCurrency = v; }
        public double getSalesAmount() { return salesAmount; }
        public void setSalesAmount(double v) { this.salesAmount = v; }
        public String getSalesCurrency() { return salesCurrency; }
        public void setSalesCurrency(String v) { this.salesCurrency = v; }
        public String getPriceCurrency() { return priceCurrency; }
        public void setPriceCurrency(String v) { this.priceCurrency = v; }
        public int getRoundingDigit() { return roundingDigit; }
        public void setRoundingDigit(int v) { this.roundingDigit = v; }
        public String getRoundingKind() { return roundingKind; }
        public void setRoundingKind(String v) { this.roundingKind = v; }
        public String getBondControlKind() { return bondControlKind; }
        public void setBondControlKind(String v) { this.bondControlKind = v; }
        public String getHsCode() { return hsCode; }
        public void setHsCode(String v) { this.hsCode = v; }
        public String getCustomerStatus() { return customerStatus; }
        public void setCustomerStatus(String v) { this.customerStatus = v; }
        public String getExpiryKind() { return expiryKind; }
        public void setExpiryKind(String v) { this.expiryKind = v; }
        public int getExpiryDays() { return expiryDays; }
        public void setExpiryDays(int v) { this.expiryDays = v; }
        public int getShippingDueDays() { return shippingDueDays; }
        public void setShippingDueDays(int v) { this.shippingDueDays = v; }
        public String getShippingDueKind() { return shippingDueKind; }
        public void setShippingDueKind(String v) { this.shippingDueKind = v; }
        public int getStockDueDays() { return stockDueDays; }
        public void setStockDueDays(int v) { this.stockDueDays = v; }
        public String getStockDueKind() { return stockDueKind; }
        public void setStockDueKind(String v) { this.stockDueKind = v; }
        public int getArrivalDueDays() { return arrivalDueDays; }
        public void setArrivalDueDays(int v) { this.arrivalDueDays = v; }
        public String getArrivalDueKind() { return arrivalDueKind; }
        public void setArrivalDueKind(String v) { this.arrivalDueKind = v; }
        public int getShippingRefDays() { return shippingRefDays; }
        public void setShippingRefDays(int v) { this.shippingRefDays = v; }
        public int getShippingRetDays() { return shippingRetDays; }
        public void setShippingRetDays(int v) { this.shippingRetDays = v; }
        public String getBestBeforePrintKind() { return bestBeforePrintKind; }
        public void setBestBeforePrintKind(String v) { this.bestBeforePrintKind = v; }
        public int getBestBeforePrintDays() { return bestBeforePrintDays; }
        public void setBestBeforePrintDays(int v) { this.bestBeforePrintDays = v; }
        public String getKittingProduct() { return kittingProduct; }
        public void setKittingProduct(String v) { this.kittingProduct = v; }
        public String getKittingSourceFlag() { return kittingSourceFlag; }
        public void setKittingSourceFlag(String v) { this.kittingSourceFlag = v; }
        public String getPsaaKind() { return psaaKind; }
        public void setPsaaKind(String v) { this.psaaKind = v; }
        public int getPsaaDays() { return psaaDays; }
        public void setPsaaDays(int v) { this.psaaDays = v; }
        public String getCrossDockKind() { return crossDockKind; }
        public void setCrossDockKind(String v) { this.crossDockKind = v; }
        public String getChargeClassGroup() { return chargeClassGroup; }
        public void setChargeClassGroup(String v) { this.chargeClassGroup = v; }
        public String getSerialLotGetFlag() { return serialLotGetFlag; }
        public void setSerialLotGetFlag(String v) { this.serialLotGetFlag = v; }
        public String getSerialAllocFlag() { return serialAllocFlag; }
        public void setSerialAllocFlag(String v) { this.serialAllocFlag = v; }
        public String getCaseMarkType() { return caseMarkType; }
        public void setCaseMarkType(String v) { this.caseMarkType = v; }
        public String getReceiptBatchNum() { return receiptBatchNum; }
        public void setReceiptBatchNum(String v) { this.receiptBatchNum = v; }
        public int getProductPerBallNum() { return productPerBallNum; }
        public void setProductPerBallNum(int v) { this.productPerBallNum = v; }
        public int getProductMaxQty() { return productMaxQty; }
        public void setProductMaxQty(int v) { this.productMaxQty = v; }
        public int getProductMinQty() { return productMinQty; }
        public void setProductMinQty(int v) { this.productMinQty = v; }
        public String getFrequencyType() { return frequencyType; }
        public void setFrequencyType(String v) { this.frequencyType = v; }
        public String getArrivalItemKind() { return arrivalItemKind; }
        public void setArrivalItemKind(String v) { this.arrivalItemKind = v; }
        public String getArrivalEndBagKind() { return arrivalEndBagKind; }
        public void setArrivalEndBagKind(String v) { this.arrivalEndBagKind = v; }
        public String getAutoRegisterFromInterface() { return autoRegisterFromInterface; }
        public void setAutoRegisterFromInterface(String v) { this.autoRegisterFromInterface = v; }
        public String getCalcArrivalFeeUnit() { return calcArrivalFeeUnit; }
        public void setCalcArrivalFeeUnit(String v) { this.calcArrivalFeeUnit = v; }
        public int getCalcArrivalFeeRound() { return calcArrivalFeeRound; }
        public void setCalcArrivalFeeRound(int v) { this.calcArrivalFeeRound = v; }
        public double getPriceArrivalFee() { return priceArrivalFee; }
        public void setPriceArrivalFee(double v) { this.priceArrivalFee = v; }
        public double getSpecialPriceArrivalFee() { return specialPriceArrivalFee; }
        public void setSpecialPriceArrivalFee(double v) { this.specialPriceArrivalFee = v; }
        public String getCalcShippingFeeUnit() { return calcShippingFeeUnit; }
        public void setCalcShippingFeeUnit(String v) { this.calcShippingFeeUnit = v; }
        public int getCalcShippingFeeRound() { return calcShippingFeeRound; }
        public void setCalcShippingFeeRound(int v) { this.calcShippingFeeRound = v; }
        public double getPriceShippingFee() { return priceShippingFee; }
        public void setPriceShippingFee(double v) { this.priceShippingFee = v; }
        public double getSpecialPriceShippingFee() { return specialPriceShippingFee; }
        public void setSpecialPriceShippingFee(double v) { this.specialPriceShippingFee = v; }
        public String getCalcStorageFeeUnit() { return calcStorageFeeUnit; }
        public void setCalcStorageFeeUnit(String v) { this.calcStorageFeeUnit = v; }
        public int getCalcStorageFeeRound() { return calcStorageFeeRound; }
        public void setCalcStorageFeeRound(int v) { this.calcStorageFeeRound = v; }
        public double getPriceStorageFee() { return priceStorageFee; }
        public void setPriceStorageFee(double v) { this.priceStorageFee = v; }
        public double getSpecialPriceStorageFee() { return specialPriceStorageFee; }
        public void setSpecialPriceStorageFee(double v) { this.specialPriceStorageFee = v; }
        public String getCalcKittingFeeUnit() { return calcKittingFeeUnit; }
        public void setCalcKittingFeeUnit(String v) { this.calcKittingFeeUnit = v; }
        public int getCalcKittingFeeRound() { return calcKittingFeeRound; }
        public void setCalcKittingFeeRound(int v) { this.calcKittingFeeRound = v; }
        public double getPriceKittingFee() { return priceKittingFee; }
        public void setPriceKittingFee(double v) { this.priceKittingFee = v; }
        public double getSpecialPriceKittingFee() { return specialPriceKittingFee; }
        public void setSpecialPriceKittingFee(double v) { this.specialPriceKittingFee = v; }
        public String getCalcSubFeeUnit() { return calcSubFeeUnit; }
        public void setCalcSubFeeUnit(String v) { this.calcSubFeeUnit = v; }
        public int getCalcSubFeeRound() { return calcSubFeeRound; }
        public void setCalcSubFeeRound(int v) { this.calcSubFeeRound = v; }
        public double getPriceSubFee() { return priceSubFee; }
        public void setPriceSubFee(double v) { this.priceSubFee = v; }
        public double getSpecialPriceSubFee() { return specialPriceSubFee; }
        public void setSpecialPriceSubFee(double v) { this.specialPriceSubFee = v; }
        public String getCalcWarehouseFeeUnit() { return calcWarehouseFeeUnit; }
        public void setCalcWarehouseFeeUnit(String v) { this.calcWarehouseFeeUnit = v; }
        public int getCalcWarehouseFeeRound() { return calcWarehouseFeeRound; }
        public void setCalcWarehouseFeeRound(int v) { this.calcWarehouseFeeRound = v; }
        public double getPriceWarehouseFee() { return priceWarehouseFee; }
        public void setPriceWarehouseFee(double v) { this.priceWarehouseFee = v; }
        public double getSpecialPriceWarehouseFee() { return specialPriceWarehouseFee; }
        public void setSpecialPriceWarehouseFee(double v) { this.specialPriceWarehouseFee = v; }
        public String getScreenSortSerial() { return screenSortSerial; }
        public void setScreenSortSerial(String v) { this.screenSortSerial = v; }
        public String getScreenSortArrival() { return screenSortArrival; }
        public void setScreenSortArrival(String v) { this.screenSortArrival = v; }
        public String getScreenSortPicking() { return screenSortPicking; }
        public void setScreenSortPicking(String v) { this.screenSortPicking = v; }
        public String getScreenSortShipping() { return screenSortShipping; }
        public void setScreenSortShipping(String v) { this.screenSortShipping = v; }
        public String getScreenSortStock() { return screenSortStock; }
        public void setScreenSortStock(String v) { this.screenSortStock = v; }
        public String getScreenSortLocation() { return screenSortLocation; }
        public void setScreenSortLocation(String v) { this.screenSortLocation = v; }
        public String getCustomSortSerial() { return customSortSerial; }
        public void setCustomSortSerial(String v) { this.customSortSerial = v; }
        public String getCustomSortArrival() { return customSortArrival; }
        public void setCustomSortArrival(String v) { this.customSortArrival = v; }
        public String getCustomSortPicking() { return customSortPicking; }
        public void setCustomSortPicking(String v) { this.customSortPicking = v; }
        public String getCustomSortShipping() { return customSortShipping; }
        public void setCustomSortShipping(String v) { this.customSortShipping = v; }
        public String getCustomSortStock() { return customSortStock; }
        public void setCustomSortStock(String v) { this.customSortStock = v; }
        public String getCustomSortLocation() { return customSortLocation; }
        public void setCustomSortLocation(String v) { this.customSortLocation = v; }
    }

    public static class ProductUi {
        private boolean showPricingSection = true;
        private boolean showDimensionsSection = true;
        private boolean showExpirySection = true;
        private boolean showBondingSection = false;
        private boolean showReplenishSection = true;
        private boolean showFeeCalcSection = false;
        private boolean showSortingKeysSection = true;
        private boolean showCustomItemsSection = true;
        private boolean showCustomFlagsSection = true;
        private Map<String, String> customItemLabels = new HashMap<>();
        private Map<String, String> customFlagLabels = new HashMap<>();
        private Map<String, String> customNumericLabels = new HashMap<>();

        public boolean isShowPricingSection() { return showPricingSection; }
        public void setShowPricingSection(boolean v) { this.showPricingSection = v; }
        public boolean isShowDimensionsSection() { return showDimensionsSection; }
        public void setShowDimensionsSection(boolean v) { this.showDimensionsSection = v; }
        public boolean isShowExpirySection() { return showExpirySection; }
        public void setShowExpirySection(boolean v) { this.showExpirySection = v; }
        public boolean isShowBondingSection() { return showBondingSection; }
        public void setShowBondingSection(boolean v) { this.showBondingSection = v; }
        public boolean isShowReplenishSection() { return showReplenishSection; }
        public void setShowReplenishSection(boolean v) { this.showReplenishSection = v; }
        public boolean isShowFeeCalcSection() { return showFeeCalcSection; }
        public void setShowFeeCalcSection(boolean v) { this.showFeeCalcSection = v; }
        public boolean isShowSortingKeysSection() { return showSortingKeysSection; }
        public void setShowSortingKeysSection(boolean v) { this.showSortingKeysSection = v; }
        public boolean isShowCustomItemsSection() { return showCustomItemsSection; }
        public void setShowCustomItemsSection(boolean v) { this.showCustomItemsSection = v; }
        public boolean isShowCustomFlagsSection() { return showCustomFlagsSection; }
        public void setShowCustomFlagsSection(boolean v) { this.showCustomFlagsSection = v; }
        public Map<String, String> getCustomItemLabels() { return customItemLabels; }
        public void setCustomItemLabels(Map<String, String> v) { this.customItemLabels = v; }
        public Map<String, String> getCustomFlagLabels() { return customFlagLabels; }
        public void setCustomFlagLabels(Map<String, String> v) { this.customFlagLabels = v; }
        public Map<String, String> getCustomNumericLabels() { return customNumericLabels; }
        public void setCustomNumericLabels(Map<String, String> v) { this.customNumericLabels = v; }
    }

    // ================================================================
    // Warehouse, Location, Code, Numbering, Reason, Status,
    // Environment, Organization, Serial
    // ================================================================

    public static class Warehouse {
        private WarehouseDefaults defaults = new WarehouseDefaults();
        private LocationStructure locationStructure = new LocationStructure();

        public WarehouseDefaults getDefaults() { return defaults; }
        public void setDefaults(WarehouseDefaults v) { this.defaults = v; }
        public LocationStructure getLocationStructure() { return locationStructure; }
        public void setLocationStructure(LocationStructure v) { this.locationStructure = v; }
    }

    public static class WarehouseDefaults {
        private String countryCode = "";
        private String cityCode = "";
        private String stateCode = "";
        private String storeCode = "";
        private String abbreviationType = "";
        private String reportBatchPattern1 = "";
        private String reportBatchPattern2 = "";
        private String reportBatchPattern3 = "";
        private String reportBatchPattern4 = "";

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String v) { this.countryCode = v; }
        public String getCityCode() { return cityCode; }
        public void setCityCode(String v) { this.cityCode = v; }
        public String getStateCode() { return stateCode; }
        public void setStateCode(String v) { this.stateCode = v; }
        public String getStoreCode() { return storeCode; }
        public void setStoreCode(String v) { this.storeCode = v; }
        public String getAbbreviationType() { return abbreviationType; }
        public void setAbbreviationType(String v) { this.abbreviationType = v; }
        public String getReportBatchPattern1() { return reportBatchPattern1; }
        public void setReportBatchPattern1(String v) { this.reportBatchPattern1 = v; }
        public String getReportBatchPattern2() { return reportBatchPattern2; }
        public void setReportBatchPattern2(String v) { this.reportBatchPattern2 = v; }
        public String getReportBatchPattern3() { return reportBatchPattern3; }
        public void setReportBatchPattern3(String v) { this.reportBatchPattern3 = v; }
        public String getReportBatchPattern4() { return reportBatchPattern4; }
        public void setReportBatchPattern4(String v) { this.reportBatchPattern4 = v; }
    }

    public static class LocationStructure {
        private String area = "";
        private String rack = "";
        private String position = "";
        private String level = "";

        public String getArea() { return area; }
        public void setArea(String v) { this.area = v; }
        public String getRack() { return rack; }
        public void setRack(String v) { this.rack = v; }
        public String getPosition() { return position; }
        public void setPosition(String v) { this.position = v; }
        public String getLevel() { return level; }
        public void setLevel(String v) { this.level = v; }
    }

    public static class Location {
        private LocationDefaults defaults = new LocationDefaults();
        private LocationUi ui = new LocationUi();

        public LocationDefaults getDefaults() { return defaults; }
        public void setDefaults(LocationDefaults v) { this.defaults = v; }
        public LocationUi getUi() { return ui; }
        public void setUi(LocationUi v) { this.ui = v; }
    }

    public static class LocationDefaults {
        private String zoneCode = "";
        private String composeKind = "01";
        private String noAllocateFlag = "0";
        private String noReplenishFlag = "0";
        private String noProductFlag = "0";
        private String noPickFlag = "0";
        private int allocatePriority = 0;
        private int pickLineNumber = 0;
        private double locationLength = 0;
        private double locationWidth = 0;
        private double locationHeight = 0;
        private String temperatureControl = "";
        private String assignedProductCode = "";
        private String subInventoryLocation = "";

        public String getZoneCode() { return zoneCode; }
        public void setZoneCode(String v) { this.zoneCode = v; }
        public String getComposeKind() { return composeKind; }
        public void setComposeKind(String v) { this.composeKind = v; }
        public String getNoAllocateFlag() { return noAllocateFlag; }
        public void setNoAllocateFlag(String v) { this.noAllocateFlag = v; }
        public String getNoReplenishFlag() { return noReplenishFlag; }
        public void setNoReplenishFlag(String v) { this.noReplenishFlag = v; }
        public String getNoProductFlag() { return noProductFlag; }
        public void setNoProductFlag(String v) { this.noProductFlag = v; }
        public String getNoPickFlag() { return noPickFlag; }
        public void setNoPickFlag(String v) { this.noPickFlag = v; }
        public int getAllocatePriority() { return allocatePriority; }
        public void setAllocatePriority(int v) { this.allocatePriority = v; }
        public int getPickLineNumber() { return pickLineNumber; }
        public void setPickLineNumber(int v) { this.pickLineNumber = v; }
        public double getLocationLength() { return locationLength; }
        public void setLocationLength(double v) { this.locationLength = v; }
        public double getLocationWidth() { return locationWidth; }
        public void setLocationWidth(double v) { this.locationWidth = v; }
        public double getLocationHeight() { return locationHeight; }
        public void setLocationHeight(double v) { this.locationHeight = v; }
        public String getTemperatureControl() { return temperatureControl; }
        public void setTemperatureControl(String v) { this.temperatureControl = v; }
        public String getAssignedProductCode() { return assignedProductCode; }
        public void setAssignedProductCode(String v) { this.assignedProductCode = v; }
        public String getSubInventoryLocation() { return subInventoryLocation; }
        public void setSubInventoryLocation(String v) { this.subInventoryLocation = v; }
    }

    public static class LocationUi {
        private boolean showDimensionsSection = true;
        private boolean showCustomItemsSection = true;
        private boolean showCustomFlagsSection = true;
        private Map<String, String> customItemLabels = new HashMap<>();
        private Map<String, String> customFlagLabels = new HashMap<>();
        private Map<String, String> customNumericLabels = new HashMap<>();

        public boolean isShowDimensionsSection() { return showDimensionsSection; }
        public void setShowDimensionsSection(boolean v) { this.showDimensionsSection = v; }
        public boolean isShowCustomItemsSection() { return showCustomItemsSection; }
        public void setShowCustomItemsSection(boolean v) { this.showCustomItemsSection = v; }
        public boolean isShowCustomFlagsSection() { return showCustomFlagsSection; }
        public void setShowCustomFlagsSection(boolean v) { this.showCustomFlagsSection = v; }
        public Map<String, String> getCustomItemLabels() { return customItemLabels; }
        public void setCustomItemLabels(Map<String, String> v) { this.customItemLabels = v; }
        public Map<String, String> getCustomFlagLabels() { return customFlagLabels; }
        public void setCustomFlagLabels(Map<String, String> v) { this.customFlagLabels = v; }
        public Map<String, String> getCustomNumericLabels() { return customNumericLabels; }
        public void setCustomNumericLabels(Map<String, String> v) { this.customNumericLabels = v; }
    }

    public static class Code {
        private CodeDefaults defaults = new CodeDefaults();
        private Map<String, String> standardKinds = new HashMap<>();

        public CodeDefaults getDefaults() { return defaults; }
        public void setDefaults(CodeDefaults v) { this.defaults = v; }
        public Map<String, String> getStandardKinds() { return standardKinds; }
        public void setStandardKinds(Map<String, String> v) { this.standardKinds = v; }
    }

    public static class CodeDefaults {
        private String standardFlag = "0";

        public String getStandardFlag() { return standardFlag; }
        public void setStandardFlag(String v) { this.standardFlag = v; }
    }

    public static class Numbering {
        private NumberingDefaults defaults = new NumberingDefaults();
        private Map<String, String> standardCodes = new HashMap<>();

        public NumberingDefaults getDefaults() { return defaults; }
        public void setDefaults(NumberingDefaults v) { this.defaults = v; }
        public Map<String, String> getStandardCodes() { return standardCodes; }
        public void setStandardCodes(Map<String, String> v) { this.standardCodes = v; }
    }

    public static class NumberingDefaults {
        private String numberType = "SEQ";
        private String noLoopFlag = "0";

        public String getNumberType() { return numberType; }
        public void setNumberType(String v) { this.numberType = v; }
        public String getNoLoopFlag() { return noLoopFlag; }
        public void setNoLoopFlag(String v) { this.noLoopFlag = v; }
    }

    public static class Reason {
        private Map<String, String> standardCodes = new HashMap<>();

        public Map<String, String> getStandardCodes() { return standardCodes; }
        public void setStandardCodes(Map<String, String> v) { this.standardCodes = v; }
    }

    public static class Status {
        private StatusDefaults defaults = new StatusDefaults();
        private Map<String, String> businessCodes = new HashMap<>();
        private Map<String, String> arrivalStatuses = new HashMap<>();
        private Map<String, String> shippingStatuses = new HashMap<>();
        private Map<String, String> stockStatuses = new HashMap<>();

        public StatusDefaults getDefaults() { return defaults; }
        public void setDefaults(StatusDefaults v) { this.defaults = v; }
        public Map<String, String> getBusinessCodes() { return businessCodes; }
        public void setBusinessCodes(Map<String, String> v) { this.businessCodes = v; }
        public Map<String, String> getArrivalStatuses() { return arrivalStatuses; }
        public void setArrivalStatuses(Map<String, String> v) { this.arrivalStatuses = v; }
        public Map<String, String> getShippingStatuses() { return shippingStatuses; }
        public void setShippingStatuses(Map<String, String> v) { this.shippingStatuses = v; }
        public Map<String, String> getStockStatuses() { return stockStatuses; }
        public void setStockStatuses(Map<String, String> v) { this.stockStatuses = v; }
    }

    public static class StatusDefaults {
        private String workStatusKind = "01";
        private String confirmTimingFlag = "0";

        public String getWorkStatusKind() { return workStatusKind; }
        public void setWorkStatusKind(String v) { this.workStatusKind = v; }
        public String getConfirmTimingFlag() { return confirmTimingFlag; }
        public void setConfirmTimingFlag(String v) { this.confirmTimingFlag = v; }
    }

    public static class Environment {
        private Map<String, Object> parameters = new HashMap<>();

        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> v) { this.parameters = v; }
    }

    public static class Organization {
        private OrganizationDefaults defaults = new OrganizationDefaults();

        public OrganizationDefaults getDefaults() { return defaults; }
        public void setDefaults(OrganizationDefaults v) { this.defaults = v; }
    }

    public static class OrganizationDefaults {
        private String countryCode = "JP";
        private String costCenterJpn = "";
        private String costCenterUsa = "";
        private String costCenterEu = "";

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String v) { this.countryCode = v; }
        public String getCostCenterJpn() { return costCenterJpn; }
        public void setCostCenterJpn(String v) { this.costCenterJpn = v; }
        public String getCostCenterUsa() { return costCenterUsa; }
        public void setCostCenterUsa(String v) { this.costCenterUsa = v; }
        public String getCostCenterEu() { return costCenterEu; }
        public void setCostCenterEu(String v) { this.costCenterEu = v; }
    }

    public static class Serial {
        private SerialDefaults defaults = new SerialDefaults();
        private SerialTracking tracking = new SerialTracking();

        public SerialDefaults getDefaults() { return defaults; }
        public void setDefaults(SerialDefaults v) { this.defaults = v; }
        public SerialTracking getTracking() { return tracking; }
        public void setTracking(SerialTracking v) { this.tracking = v; }
    }

    public static class SerialDefaults {
        private String assignmentKind = "01";

        public String getAssignmentKind() { return assignmentKind; }
        public void setAssignmentKind(String v) { this.assignmentKind = v; }
    }

    public static class SerialTracking {
        private boolean enabled = false;
        private boolean requireUniquePerProduct = true;
        private boolean allowReuse = false;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean v) { this.enabled = v; }
        public boolean isRequireUniquePerProduct() { return requireUniquePerProduct; }
        public void setRequireUniquePerProduct(boolean v) { this.requireUniquePerProduct = v; }
        public boolean isAllowReuse() { return allowReuse; }
        public void setAllowReuse(boolean v) { this.allowReuse = v; }
    }

    // ================================================================
    // Root getters/setters
    // ================================================================
    public Pik getPik() { return pik; }
    public void setPik(Pik v) { this.pik = v; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer v) { this.customer = v; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction v) { this.transaction = v; }
    public Product getProduct() { return product; }
    public void setProduct(Product v) { this.product = v; }
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse v) { this.warehouse = v; }
    public Location getLocation() { return location; }
    public void setLocation(Location v) { this.location = v; }
    public Code getCode() { return code; }
    public void setCode(Code v) { this.code = v; }
    public Numbering getNumbering() { return numbering; }
    public void setNumbering(Numbering v) { this.numbering = v; }
    public Reason getReason() { return reason; }
    public void setReason(Reason v) { this.reason = v; }
    public Status getStatus() { return status; }
    public void setStatus(Status v) { this.status = v; }
    public Environment getEnvironment() { return environment; }
    public void setEnvironment(Environment v) { this.environment = v; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization v) { this.organization = v; }
    public Serial getSerial() { return serial; }
    public void setSerial(Serial v) { this.serial = v; }
}
