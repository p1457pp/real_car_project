package car_dto;

import java.util.Date;

public class RepairItemDTO {

	private String repairCode; // 정비 코드
	private String repairItem; // 정비 부품
	private int itemPrice; // 부품 단가
	private int laborCost; // 공임
	private Date additionalDate; // 추가일

	public RepairItemDTO() {
		super();
	}

	public RepairItemDTO(String repairCode, String repairItem, int itemPrice, int laborCost, Date additionalDate) {
		super();
		this.repairCode = repairCode;
		this.repairItem = repairItem;
		this.itemPrice = itemPrice;
		this.laborCost = laborCost;
		this.additionalDate = additionalDate;
	}

	public String getRepairCode() {
		return repairCode;
	}

	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}

	public String getRepairItem() {
		return repairItem;
	}

	public void setRepairItem(String repairItem) {
		this.repairItem = repairItem;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}

	public int getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(int laborCost) {
		this.laborCost = laborCost;
	}

	public Date getAdditionalDate() {
		return additionalDate;
	}

	public void setAdditionalDate(Date additionalDate) {
		this.additionalDate = additionalDate;
	}

	@Override
	public String toString() {
		return "RepairItemDTO [repairCode=" + repairCode + ", repairItem=" + repairItem + ", itemPrice=" + itemPrice
				+ ", laborCost=" + laborCost + "]";
	}

}
