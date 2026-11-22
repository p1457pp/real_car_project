package car_dto;

import java.time.LocalDate;

public class CarReservTableDTO {

	private String reservation_code, addtional_request, user_id,user_reserv_date, mechan_reserv_date, final_date;


	private int reservation_permission, vehicle_id;

	public CarReservTableDTO() {

	}

	public CarReservTableDTO(String reservation_code, String addtional_request, String user_id, String user_reserv_date,
			String mechan_reserv_date, String final_date, int reservation_permission, int vehicle_id) {
		super();
		this.reservation_code = reservation_code;
		this.addtional_request = addtional_request;
		this.user_id = user_id;
		this.user_reserv_date = user_reserv_date;
		this.mechan_reserv_date = mechan_reserv_date;
		this.final_date = final_date;
		this.reservation_permission = reservation_permission;
		this.vehicle_id = vehicle_id;
	}

	public String getReservation_code() {
		return reservation_code;
	}

	public void setReservation_code(String reservation_code) {
		this.reservation_code = reservation_code;
	}

	public String getAddtional_request() {
		return addtional_request;
	}

	public void setAddtional_request(String addtional_request) {
		this.addtional_request = addtional_request;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_reserv_date() {
		return user_reserv_date;
	}

	public void setUser_reserv_date(String user_reserv_date) {
		this.user_reserv_date = user_reserv_date;
	}

	public String getMechan_reserv_date() {
		return mechan_reserv_date;
	}

	public void setMechan_reserv_date(String mechan_reserv_date) {
		this.mechan_reserv_date = mechan_reserv_date;
	}

	public String getFinal_date() {
		return final_date;
	}

	public void setFinal_date(String final_date) {
		this.final_date = final_date;
	}

	public int getReservation_permission() {
		return reservation_permission;
	}

	public void setReservation_permission(int reservation_permission) {
		this.reservation_permission = reservation_permission;
	}

	public int getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(int vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	
	

}
