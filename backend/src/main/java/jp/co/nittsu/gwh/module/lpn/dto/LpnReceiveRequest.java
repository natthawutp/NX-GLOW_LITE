package jp.co.nittsu.gwh.module.lpn.dto;

import java.util.List;

public class LpnReceiveRequest {

    private String arrivalNumber;
    private String lpnNumber;
    private String lpnRfNumber;
    private String lpnType;
    private List<ReceiveLine> items;

    public static class ReceiveLine {
        private String productCode;
        private String lotNumber;
        private Integer quantity;
        private String subInventoryCode;
        private Integer arrivalLineNumber;
        private Integer arrivalSeqNumber;
        private String pik1;
        private String pik2;
        private String pik3;
        private String pik4;
        private String pik5;
        private String pik6;
        private String pik7;

        // --- Getters & Setters ---
        public String getProductCode() { return productCode; }
        public void setProductCode(String productCode) { this.productCode = productCode; }
        public String getLotNumber() { return lotNumber; }
        public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getSubInventoryCode() { return subInventoryCode; }
        public void setSubInventoryCode(String subInventoryCode) { this.subInventoryCode = subInventoryCode; }
        public Integer getArrivalLineNumber() { return arrivalLineNumber; }
        public void setArrivalLineNumber(Integer arrivalLineNumber) { this.arrivalLineNumber = arrivalLineNumber; }
        public Integer getArrivalSeqNumber() { return arrivalSeqNumber; }
        public void setArrivalSeqNumber(Integer arrivalSeqNumber) { this.arrivalSeqNumber = arrivalSeqNumber; }
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
    }

    // --- Getters & Setters ---
    public String getArrivalNumber() { return arrivalNumber; }
    public void setArrivalNumber(String arrivalNumber) { this.arrivalNumber = arrivalNumber; }
    public String getLpnNumber() { return lpnNumber; }
    public void setLpnNumber(String lpnNumber) { this.lpnNumber = lpnNumber; }
    public String getLpnRfNumber() { return lpnRfNumber; }
    public void setLpnRfNumber(String lpnRfNumber) { this.lpnRfNumber = lpnRfNumber; }
    public String getLpnType() { return lpnType; }
    public void setLpnType(String lpnType) { this.lpnType = lpnType; }
    public List<ReceiveLine> getItems() { return items; }
    public void setItems(List<ReceiveLine> items) { this.items = items; }
}
