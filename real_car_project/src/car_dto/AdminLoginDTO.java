package car_dto;

public class AdminLoginDTO {
	private String id,pass;
	
	public AdminLoginDTO() {
		super();
	}

	public AdminLoginDTO(String id, String pass) {
		super();
		this.id = id;
		this.pass = pass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
	
}
