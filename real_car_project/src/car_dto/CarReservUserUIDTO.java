package car_dto;

import java.sql.Date;
import java.time.LocalDate;

public class CarReservUserUIDTO {
	
	private String reservDate,repairPart,add_request;

	public CarReservUserUIDTO() {
		
	}

	public CarReservUserUIDTO(String reservDate, String repairPart, String add_request) {
		
		this.reservDate = reservDate;
		this.repairPart = repairPart;
		this.add_request = add_request;
	}

	public String getReservDate() {
		return reservDate;
	}

	public void setReservDate(String reservDate) {
		this.reservDate = reservDate;
	}

	public String getRepairPart() {
		return repairPart;
	}

	public void setRepairPart(String repairPart) {
		this.repairPart = repairPart;
	}

	public String getAdd_request() {
		return add_request;
	}

	public void setAdd_request(String add_request) {
		this.add_request = add_request;
	} 
	
	
	
	
	
	

	
}
