package car_dto;




public class CarReservFourColumnDTO {
	
	private String  reservation_code,car_num,car_model,repair_item,reservation_permission;

	public CarReservFourColumnDTO() {
	
	}

	public CarReservFourColumnDTO(String reservation_code, String car_num, String car_model, String repair_item,
			String reservation_permission) {
		
		this.reservation_code = reservation_code;
		this.car_num = car_num;
		this.car_model = car_model;
		this.repair_item = repair_item;
		this.reservation_permission = reservation_permission;
	}

	public String getReservation_code() {
		return reservation_code;
	}

	public void setReservation_code(String reservation_code) {
		this.reservation_code = reservation_code;
	}

	public String getCar_num() {
		return car_num;
	}

	public void setCar_num(String car_num) {
		this.car_num = car_num;
	}

	public String getCar_model() {
		return car_model;
	}

	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}

	public String getRepair_item() {
		return repair_item;
	}

	public void setRepair_item(String repair_item) {
		this.repair_item = repair_item;
	}

	public String getReservation_permission() {
		return reservation_permission;
	}

	public void setReservation_permission(String reservation_permission) {
		this.reservation_permission = reservation_permission;
	}
	
	
	
	
	

}
