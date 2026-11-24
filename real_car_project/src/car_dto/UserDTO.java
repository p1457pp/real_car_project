package car_dto;

public class UserDTO {

	private String userId; // 유저 아이디
	private String password; // 유저 비밀번호
	private String name; // 유저 이름
	private String email; // 유저 이메일
	private String phone; // 유저 전화번호
	private String grade; // 회원 등급
	private String date; // 가입일

	public UserDTO() {
		super();
	}

	// 회원가입, 로그인, 정보수정용
	public UserDTO(String userId, String password, String name, String email, String phone) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	// 회원 조회용 (관리자 회원관리)
	public UserDTO(String userId, String name, String email, String phone, String grade, String date) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.grade = grade;
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", name=" + name + ", email=" + email + ", phone=" + phone + ", grade="
				+ grade + ", date=" + date + "]";
	}

}
