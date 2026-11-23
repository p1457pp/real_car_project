package car_dto;

public class MaintDetailDTO {
    private int detailNo;
    private int reportNo;
    private String itemName;
    private int itemPrice;
    private int labCost;
    private int qty;
    private int totalPrice;

    public MaintDetailDTO() {}

    public MaintDetailDTO(String itemName, int itemPrice, int qty) {
        this(itemName, itemPrice, 0, qty);
    }

    public MaintDetailDTO(String itemName, int itemPrice, int labCost, int qty) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.labCost = labCost;
        this.qty = qty;
        this.totalPrice = (itemPrice + labCost) * qty;
    }

    public int getDetailNo() { return detailNo; }
    public void setDetailNo(int detailNo) { this.detailNo = detailNo; }

    public int getReportNo() { return reportNo; }
    public void setReportNo(int reportNo) { this.reportNo = reportNo; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getItemPrice() { return itemPrice; }
    public void setItemPrice(int itemPrice) { this.itemPrice = itemPrice; }

    public int getLabCost() { return labCost; }
    public void setLabCost(int labCost) { this.labCost = labCost; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
}