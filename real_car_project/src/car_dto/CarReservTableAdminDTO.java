package car_dto;





public class CarReservTableAdminDTO {
	
	private String reservation_code,user_id,repair_item,user_reserv_date,final_date,reserv_state;

	public CarReservTableAdminDTO() {
		
	}

	public CarReservTableAdminDTO(String reservation_code, String user_id, String repair_item, String user_reserv_date,
			String final_date, String reserv_state) {
		
		this.reservation_code = reservation_code;
		this.user_id = user_id;
		this.repair_item = repair_item;
		this.user_reserv_date = user_reserv_date;
		this.final_date = final_date;
		this.reserv_state = reserv_state;
	}

	public String getReservation_code() {
		return reservation_code;
	}

	public void setReservation_code(String reservation_code) {
		this.reservation_code = reservation_code;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRepair_item() {
		return repair_item;
	}

	public void setRepair_item(String repair_item) {
		this.repair_item = repair_item;
	}

	public String getUser_reserv_date() {
		return user_reserv_date;
	}

	public void setUser_reserv_date(String user_reserv_date) {
		this.user_reserv_date = user_reserv_date;
	}

	public String getFinal_date() {
		return final_date;
	}

	public void setFinal_date(String final_date) {
		this.final_date = final_date;
	}

	public String getReserv_state() {
		return reserv_state;
	}

	public void setReserv_state(String reserv_state) {
		this.reserv_state = reserv_state;
	}
	
	
	
	
	

}
