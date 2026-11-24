package admin.car_service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dao.AdminDAO;
import car_dto.AdminDTO;
import car_dto.UserDTO;

public class AdminService {

	/**
	 * 관리자 로그인. 
	 */
	public boolean adminLogin(AdminDTO aDTO) {
		boolean flag = false;
		try {
			String id = AdminDAO.getInstance().login(aDTO);
			flag = !id.isEmpty();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public List<UserDTO> searchAllMember() {
		List<UserDTO> list = new ArrayList<UserDTO>();
		try {
			list = AdminDAO.getInstance().selectAllUser();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<UserDTO> searchByUserId(String keyword) {
		List<UserDTO> list = new ArrayList<UserDTO>();
		try {
			list = AdminDAO.getInstance().selectByUserId(keyword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean removeUser(String userId) {
		boolean flag = false;
		try {
			flag = AdminDAO.getInstance().updateDeleteState(userId) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean modifyUser(UserDTO uDTO) {
		boolean flag = false;
		try {
			flag = AdminDAO.getInstance().updateUser(uDTO) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

}
