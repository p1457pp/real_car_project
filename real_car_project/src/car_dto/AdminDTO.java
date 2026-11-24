package car_dto;

public class AdminDTO {

	private String id; // 관리자 아이디
	private String pass; // 관리자 비밀번호

	public AdminDTO() {
		super();
	}

	public AdminDTO(String id, String pass) {
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

	@Override
	public String toString() {
		return "AdminDTO [id=" + id + "]";
	}

}
