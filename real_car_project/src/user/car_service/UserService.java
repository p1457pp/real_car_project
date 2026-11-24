package user.car_service;

import java.sql.SQLException;

import car_dao.UserDAO;
import car_dto.UserDTO;

public class UserService {

	/**
	 * 로그인. DB가 꺼져 있으면(집) 입력값이 있으면 통과시켜 화면 전환을 확인할 수 있게 한다.
	 * 
	 * @param uDTO
	 * @return 로그인 성공한 아이디, 실패하면 빈 문자열
	 */
	public String userLogin(UserDTO uDTO) {
		String id = "";
		try {
			id = UserDAO.getInstance().login(uDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 회원 정보 조회. 
	 */
	public UserDTO searchByUserId(String userId) {
		UserDTO uDTO = null;
		try {
			uDTO = UserDAO.getInstance().selectByUserId(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uDTO;
	}

	/**
	 * 회원가입
	 * 
	 * @return 1: 성공, 0: 실패(중복 등)
	 */
	public int addUser(UserDTO uDTO) {
		int cnt = 0;
		try {
			cnt = UserDAO.getInstance().insertUser(uDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	/**
	 * 회원 정보 수정
	 * 
	 * @return 1: 성공, 0: 실패
	 */
	public int modifyUser(UserDTO uDTO) {
		int cnt = 0;
		try {
			cnt = UserDAO.getInstance().updateUser(uDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

}
