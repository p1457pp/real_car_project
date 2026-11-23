package admin.car_service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dao.AdminMemberManagementDAO;
import car_dto.AdminMemberManagementDTO;

public class AdminMemberManagementService {
	public List<AdminMemberManagementDTO> searchAllMember() {
		List<AdminMemberManagementDTO> list= new ArrayList<AdminMemberManagementDTO>();
		
		try {
			list=AdminMemberManagementDAO.getInstance().selectAllMember();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	public boolean deleteUser(String userId) {
	    boolean flag = false;
	    try {
	        flag = AdminMemberManagementDAO.getInstance().deleteUser(userId) == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return flag;
	}
	
	public boolean updateUser(AdminMemberManagementDTO ammDTO) {
		boolean flag = false;
	    try {
	        flag = AdminMemberManagementDAO.getInstance().updateUser(ammDTO) == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return flag;
	}
}
