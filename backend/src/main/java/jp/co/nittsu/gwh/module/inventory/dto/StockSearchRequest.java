package jp.co.nittsu.gwh.module.inventory.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Search criteria for Stock Inquiry.
 * Maps legacy GwhLpski020HdrDTO fields to modern camelCase conventions.
 */
public class StockSearchRequest {

    // Inventory identifiers
    private String arrivalNumber;
    private String arrivalLineNumber;
    private String arrivalSeqNumber;

    // Date range
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;

    // Product
    private String productCode;
    private boolean forwardSearch;

    // Origin
    private String originCode;

    // PIK keys 1-7
    private String pik1;
    private String pik2;
    private String pik3;
    private String pik4;
    private String pik5;
    private String pik6;
    private String pik7;

    // Location parts
    private String areaCode;
    private String rackCode;
    private String positionCode;
    private String levelCode;

    // Sub-inventory
    private String subInventoryCode;

    // Flags
    private String damageFlag;
    private String lockFlag;
    private String bondFlag;
    private String passaFlag;

    // Quantity filters: "1"=all, "2"=non-zero, "3"=zero
    private String physicalQtyFilter;
    private String availableQtyFilter;

    // Temp location filter
    private boolean tempLocationOnly;

    // DROP aggregation flags: "01"=distinct, "02"=aggregate
    private String subDrop;
    private String areaDrop;
    private String rackDrop;
    private String posDrop;
    private String levDrop;
    private String pik1Drop;
    private String pik2Drop;
    private String pik3Drop;
    private String pik4Drop;
    private String pik5Drop;
    private String dmgDrop;

    // Pagination
    private int page;
    private int size = 20;
    private Integer maxDisplayResults;

    // Sort
    private String sortField;
    private String sortOrder;

    // --- Getters & Setters ---

    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getArrivalLineNumber() { return arrivalLineNumber; }
    public void setArrivalLineNumber(String arrivalLineNumber) { this.arrivalLineNumber = arrivalLineNumber; }
    public String getArrivalSeqNumber() { return arrivalSeqNumber; }
    public void setArrivalSeqNumber(String arrivalSeqNumber) { this.arrivalSeqNumber = arrivalSeqNumber; }
    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }
    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public boolean isForwardSearch() { return forwardSearch; }
    public void setForwardSearch(boolean forwardSearch) { this.forwardSearch = forwardSearch; }
    public String getOriginCode() { return originCode; }
    public void setOriginCode(String originCode) { this.originCode = originCode; }
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
    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }
    public String getRackCode() { return rackCode; }
    public void setRackCode(String rackCode) { this.rackCode = rackCode; }
    public String getPositionCode() { return positionCode; }
    public void setPositionCode(String positionCode) { this.positionCode = positionCode; }
    public String getLevelCode() { return levelCode; }
    public void setLevelCode(String levelCode) { this.levelCode = levelCode; }
    public String getSubInventoryCode() { return subInventoryCode; }
    public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
    public String getDamageFlag() { return damageFlag; }
    public void setDamageFlag(String damageFlag) { this.damageFlag = damageFlag; }
    public String getLockFlag() { return lockFlag; }
    public void setLockFlag(String lockFlag) { this.lockFlag = lockFlag; }
    public String getBondFlag() { return bondFlag; }
    public void setBondFlag(String bondFlag) { this.bondFlag = bondFlag; }
    public String getPassaFlag() { return passaFlag; }
    public void setPassaFlag(String passaFlag) { this.passaFlag = passaFlag; }
    public String getPhysicalQtyFilter() { return physicalQtyFilter; }
    public void setPhysicalQtyFilter(String physicalQtyFilter) { this.physicalQtyFilter = physicalQtyFilter; }
    public String getAvailableQtyFilter() { return availableQtyFilter; }
    public void setAvailableQtyFilter(String availableQtyFilter) { this.availableQtyFilter = availableQtyFilter; }
    public boolean isTempLocationOnly() { return tempLocationOnly; }
    public void setTempLocationOnly(boolean tempLocationOnly) { this.tempLocationOnly = tempLocationOnly; }
    public String getSubDrop() { return subDrop; }
    public void setSubDrop(String subDrop) { this.subDrop = subDrop; }
    public String getAreaDrop() { return areaDrop; }
    public void setAreaDrop(String areaDrop) { this.areaDrop = areaDrop; }
    public String getRackDrop() { return rackDrop; }
    public void setRackDrop(String rackDrop) { this.rackDrop = rackDrop; }
    public String getPosDrop() { return posDrop; }
    public void setPosDrop(String posDrop) { this.posDrop = posDrop; }
    public String getLevDrop() { return levDrop; }
    public void setLevDrop(String levDrop) { this.levDrop = levDrop; }
    public String getPik1Drop() { return pik1Drop; }
    public void setPik1Drop(String pik1Drop) { this.pik1Drop = pik1Drop; }
    public String getPik2Drop() { return pik2Drop; }
    public void setPik2Drop(String pik2Drop) { this.pik2Drop = pik2Drop; }
    public String getPik3Drop() { return pik3Drop; }
    public void setPik3Drop(String pik3Drop) { this.pik3Drop = pik3Drop; }
    public String getPik4Drop() { return pik4Drop; }
    public void setPik4Drop(String pik4Drop) { this.pik4Drop = pik4Drop; }
    public String getPik5Drop() { return pik5Drop; }
    public void setPik5Drop(String pik5Drop) { this.pik5Drop = pik5Drop; }
    public String getDmgDrop() { return dmgDrop; }
    public void setDmgDrop(String dmgDrop) { this.dmgDrop = dmgDrop; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public Integer getMaxDisplayResults() { return maxDisplayResults; }
    public void setMaxDisplayResults(Integer maxDisplayResults) { this.maxDisplayResults = maxDisplayResults; }
    public String getSortField() { return sortField; }
    public void setSortField(String sortField) { this.sortField = sortField; }
    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
}
