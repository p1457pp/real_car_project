package admin.car_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import car_dao.CarReservDAO;
import car_dao.DBConn;
import car_dto.CarReservDialogDTO;
import car_dto.CarReservTableAdminDTO;

public class ReservCarServiceAdmin {
	
	//서비스 계층
	
	//다이올로그에서 관리자가 예약 확정을 하면 db에 업데이트 하는 메서드
	public int cancelDialogReserv(String rscode) throws SQLException {
		Connection con = null;
		Properties prop =null;
		
		DBConn db = DBConn.getInstance();
		prop=db.getProp(null);
		int result = 0;
		try {
			con = db.getConn(prop);
			CarReservDAO crDAO = CarReservDAO.getInstance();
			result =crDAO.updateCancelReserv(con,rscode);
		
			
		} finally {
			db.DbClose(null, null, con);
		}
		
		return result;
		
	}
	
	//다이올로그에서 관리자가 예약 확정을 하면 db에 업데이트 하는 메서드
	public int confirmfinalDate(String confirmDate,String rsCode) throws SQLException {
		Connection con = null;
		Properties prop =null;
		
		DBConn db = DBConn.getInstance();
		prop=db.getProp(null);
		int result = 0;
		try {
			con = db.getConn(prop);
			CarReservDAO crDAO = CarReservDAO.getInstance();
			result =crDAO.updateFinalDate(con,confirmDate,rsCode);
			
			
		} finally {
			db.DbClose(null, null, con);
		}
		
		return result;
		
	}
	
	
	
	// 다이올로그에서 유저의 요청과 희망 날짜 정보를 받아온다
	public List<CarReservDialogDTO> showUserRequest(String rsCode) throws SQLException {
		Connection con = null;
		Properties prop = null;
		DBConn db = DBConn.getInstance();
		prop = db.getProp(null);
		
		List<CarReservDialogDTO> crdList = null;
		
		try {
			con = db.getConn(prop);
			CarReservDAO crDAO = CarReservDAO.getInstance();
			crdList = crDAO.selectReservRequest(con, rsCode);
			
			
			
			
			
			
		} finally {
			db.DbClose(null, null, con);
		}
		
		return crdList;
		
	}
	
	
	//아이디를 검색하면 검색한 유저의 예약정보만 보이고 검색한 게 없다면 전체를 보여줌 -관리자
	public List<CarReservTableAdminDTO> showReservToAdmin(String userId) throws SQLException {
		
		Connection con = null;
		Properties prop = null;
		
		List<CarReservTableAdminDTO> crList = null;
		DBConn db = DBConn.getInstance();
		prop= db.getProp(null);
		try {
			con = db.getConn(prop);
			CarReservDAO crDAO = CarReservDAO.getInstance();
			crList = crDAO.selectAllReservAdmin(con,userId);
			
		} finally {
			db.DbClose(null, null, con);
		}
		return crList; 
		
		
		
	}

}
