package car_dto;





public class ReservPartsDTO {
	
	private String reservation_code,repair_code;

	public ReservPartsDTO() {
		
	}

	public ReservPartsDTO(String reservation_code, String repair_code) {
		
		this.reservation_code = reservation_code;
		this.repair_code = repair_code;
	}

	public String getReservation_code() {
		return reservation_code;
	}

	public void setReservation_code(String reservation_code) {
		this.reservation_code = reservation_code;
	}

	public String getRepair_code() {
		return repair_code;
	}

	public void setRepair_code(String repair_code) {
		this.repair_code = repair_code;
	}
	
	
	
	
	
	
	
	

}
