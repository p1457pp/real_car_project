package admin.car_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import car_dao.ReservationDAO;
import car_dto.CarDTO;
import car_dto.MaintenanceDTO;
import car_dto.ReservationDTO;
import car_util.DBConnection;

public class AdminReservationService {
	
	//서비스 계층
	
	//다이올로그에서 관리자가 예약 확정을 하면 db에 업데이트 하는 메서드
	public int cancelReservation(String rscode) throws SQLException {
		Connection con = null;
		Properties prop =null;
		
		DBConnection db = DBConnection.getInstance();
		prop=db.getProp(null);
		int result = 0;
		try {
			con = db.getConn(prop);
			ReservationDAO rDAO = ReservationDAO.getInstance();
			result =rDAO.updateCancelReserv(con,rscode);
		
			
		} finally {
			db.dbClose(null, null, con);
		}
		
		return result;
		
	}
	
	//다이올로그에서 관리자가 예약 확정을 하면 db에 업데이트 하는 메서드
	public int changeStatus(String confirmDate,String rsCode) throws SQLException {
		Connection con = null;
		Properties prop =null;
		
		DBConnection db = DBConnection.getInstance();
		prop=db.getProp(null);
		int result = 0;
		try {
			con = db.getConn(prop);
			ReservationDAO rDAO = ReservationDAO.getInstance();
			result =rDAO.updateFinalDate(con,confirmDate,rsCode);
			
			
		} finally {
			db.dbClose(null, null, con);
		}
		
		return result;
		
	}
	
	
	
	// 다이올로그에서 유저의 요청과 희망 날짜 정보를 받아온다
	public ReservationDTO searchReservationDetail(String rsCode) throws SQLException {
		Connection con = null;
		Properties prop = null;
		DBConnection db = DBConnection.getInstance();
		prop = db.getProp(null);
		
		List<ReservationDTO> crdList = null;
		
		try {
			con = db.getConn(prop);
			ReservationDAO rDAO = ReservationDAO.getInstance();
			crdList = rDAO.selectReservRequest(con, rsCode);
			
			
			
			
			
			
		} finally {
			db.dbClose(null, null, con);
		}
		
		return (crdList == null || crdList.isEmpty()) ? null : crdList.get(0);
		
	}
	
	
	//아이디를 검색하면 검색한 유저의 예약정보만 보이고 검색한 게 없다면 전체를 보여줌 -관리자
	public List<ReservationDTO> searchReservationsByUserId(String userId) throws SQLException {
		
		Connection con = null;
		Properties prop = null;
		
		List<ReservationDTO> crList = null;
		DBConnection db = DBConnection.getInstance();
		prop= db.getProp(null);
		try {
			con = db.getConn(prop);
			ReservationDAO rDAO = ReservationDAO.getInstance();
			crList = rDAO.selectAllReservAdmin(con,userId);
			
		} finally {
			db.dbClose(null, null, con);
		}
		return crList; 
		
		
		
	}
	
	//전체 예약 조회 (아이디 검색 없이)
	public List<ReservationDTO> searchAllReservations() throws SQLException {
		return searchReservationsByUserId(null);
	}
	
	//특정 차량 정보
	public CarDTO searchCarInfoByVehicleId(int vehicleId) {
		CarDTO cDTO = null;
		try {
			cDTO = ReservationDAO.getInstance().selectCarInfoByVehicleId(vehicleId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cDTO;
	}
	
	//특정 차량 정비정보
	public List<MaintenanceDTO> searchMaintenanceInfoByVehicleId(int vehicleId) {
		return ReservationDAO.getInstance().selectMaintenanceInfoByVehicleId(vehicleId);
	}
	
}
