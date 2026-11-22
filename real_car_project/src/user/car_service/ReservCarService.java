package user.car_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import car_dao.CarReservDAO;
import car_dao.DBConn;
import car_dto.CarReservFourColumnDTO;
import car_dto.CarReservTableDTO;
import car_dto.CarReservUserUIDTO;
import car_dto.ReservPartsDTO;

public class ReservCarService {
	
	
	//예약넣기 ,모든 테이블에 다 넣는다
	public boolean addReserv(CarReservUserUIDTO cDTO) throws SQLException {
		
		Connection con = null;
		Properties prop = null;		
		boolean flag = false;
		
		int total_cnt=0;
		
		DBConn db = DBConn.getInstance();
		prop = db.getProp(null);
		
		CarReservDAO crDAO = CarReservDAO.getInstance();
		
		//유저 dto에서 받은 값을 다시 테이블 dto에 넣어준다
		CarReservTableDTO crtDTO = new CarReservTableDTO();
		crtDTO.setUser_reserv_date(cDTO.getReservDate());
		crtDTO.setAddtional_request(cDTO.getAdd_request());
		//여기서 로그인한 유저와 차량 정보를 추가로 넣어야함*************
		crtDTO.setUser_id("user001");
		crtDTO.setVehicle_id(41);
		
		
		
		
		int seqNum = 0;
		String rsCode=null;
		try {
			con = db.getConn(prop);
			con.setAutoCommit(false);
			
			//seq객체에서 예약 번호 값 가져오기
			seqNum = crDAO.selectSeqReserv(con);
			//예약코드 생성
			
			
			rsCode = String.format("rs%03d", seqNum);
			
			crtDTO.setReservation_code(rsCode);
			
			//예약 테이블에 값 넣기
			int resultReserv = crDAO.insertReservData(con, crtDTO);
			if(resultReserv ==0) {
				System.err.println("예약테이블에 값이 들어가지 않았습니다");
			}
			
			//부품이름에 대한 코드를 먼저 얻기
			String repairItemCode = crDAO.selectRepairItemCode(con, cDTO);
			if(repairItemCode ==null) {
				System.err.println("부품코드 값을 가져오지 못했습니다");
			}
			
			//reservpart테이블에 들어갈 dto에 값 넣기
			ReservPartsDTO rpDTO = new ReservPartsDTO();
			rpDTO.setRepair_code(repairItemCode);
			rpDTO.setReservation_code(rsCode);
			
			int resultReservPart = crDAO.insertReservParts(con, rpDTO);
			if(resultReservPart == 0) {
				System.err.println("예약푸품 테이블에 값이 잘 들어가지 않았습니다.");
			}
			total_cnt = resultReserv + resultReservPart;
			
			if(total_cnt==2) {
				con.commit();
				flag=true;
			} else {
				con.rollback();
			}
			

		} catch(SQLException e) {
			if(con!=null) {
				try {
					con.rollback();
				}catch(SQLException se) {
					se.printStackTrace();
				}
			}
			
			
		}
		
		finally {
			
				db.DbClose(null, null, con);
			}
		
			
		
	return flag;
	
		
	
		
		
	}
	
	//예약확인에서 보여줄 컬럼 
	public List<CarReservFourColumnDTO> showDetailReservRecord(String userId) throws SQLException {
		
		Connection con = null;
		Properties prop = null;
		DBConn db = DBConn.getInstance();
		
		List<CarReservFourColumnDTO> crfDTO = null;
		
		
		
		prop = db.getProp(null);
		
		try {
			con = db.getConn(prop);
			CarReservDAO crDAO = CarReservDAO.getInstance();
			
			crfDTO = crDAO.selectReservDetailList(con,userId);
			
		} finally {
			db.DbClose(null, null, con);
		}
		
		return crfDTO;
		
		
	}
	
	//유저 예약하기 왼쪽 화면에 데이터 채우줘는 서비스 로직
	public List<String[]> showReservRecord(String userId) throws SQLException {
		
		Connection con = null;
		Properties prop = null;
		DBConn db = DBConn.getInstance();
		prop = db.getProp(null);
		
		List<String[]> reservedRow = null;
		
		try {
			con = db.getConn(prop);
			CarReservDAO crDAO = CarReservDAO.getInstance();
			
			reservedRow = crDAO.selectReservList(con, userId);
			
		} 
		
		finally {
			db.DbClose(null, null, con);
		}
		
		return reservedRow;
		
		
		
		
	}
	
	//삭제 트랜잭션 같이 연결된 다대다 테이블도 삭제 해야댐
	public boolean removeReserv(String rsCode) throws SQLException{
		
		Connection con = null;
		Properties prop = null;
		DBConn db = DBConn.getInstance();
		prop = db.getProp(null);
		
		boolean flag=false;
		
		int delReservParts = 0;
		int delReserv=0;
		int total=0;
		
		CarReservDAO crDAO = CarReservDAO.getInstance();
		
		try {
			con = db.getConn(prop);
			con.setAutoCommit(false);
			delReservParts = crDAO.deleteReservParts(con, rsCode);
			if(delReservParts==0) {
				System.out.println(" 예약파츠 데이터가 삭제되지 않았습니다");
				con.rollback();
				return false;
				
			}
			delReserv = crDAO.deleteReserv(con, rsCode);
			if(delReserv==0) {
				System.out.println("예약내역이 삭제되지 않았습니다");
				con.rollback();
				return false;
			}
			
			total = delReservParts+delReserv;
			
			if(total==2) {
				con.commit();
				flag =true;
			} else {
				con.rollback();
			}
			
			
			
			
			
			
		}
		
		catch(SQLException e) {
			
			if(con!=null) {
				
				try {
					con.rollback();
				} catch(SQLException se) {
					se.printStackTrace();
				}
					
				}
			}
		finally {
			db.DbClose(null, null, con);
		}
		
		return flag;
		
		
		
		
	
	}
	
	
	
	
}
	
	
	
		
		
	


