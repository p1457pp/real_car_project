package car_dto;




public class CarReservDialogDTO {
	
	private String addtional_request,user_reserv_date;

	public CarReservDialogDTO() {
		
	}

	public CarReservDialogDTO(String addtional_request, String user_reserv_date) {
		
		this.addtional_request = addtional_request;
		this.user_reserv_date = user_reserv_date;
	}

	public String getAddtional_request() {
		return addtional_request;
	}

	public void setAddtional_request(String addtional_request) {
		this.addtional_request = addtional_request;
	}

	public String getUser_reserv_date() {
		return user_reserv_date;
	}

	public void setUser_reserv_date(String user_reserv_date) {
		this.user_reserv_date = user_reserv_date;
	}
	
	
	
	
	

}
