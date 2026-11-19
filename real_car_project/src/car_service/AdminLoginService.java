package car_service;

import java.sql.SQLException;

import car_dao.AdminLoginDAO;
import car_dto.AdminLoginDTO;

public class AdminLoginService {

	public boolean longinProcess(AdminLoginDTO alDTO) {
		boolean flag=false;
		
		AdminLoginDAO alDAO=AdminLoginDAO.getInstance();
		
		String id;
		try {
			id = alDAO.login(alDTO);
			flag=!id.isEmpty();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
}
