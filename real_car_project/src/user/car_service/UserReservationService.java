package user.car_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import car_dao.ReservationDAO;
import car_dto.RepairItemDTO;
import car_dto.ReservationDTO;
import car_util.DBConnection;

public class UserReservationService {
	
	
	//예약넣기 ,모든 테이블에 다 넣는다
	public boolean addReservation(ReservationDTO cDTO) throws SQLException {
		
		Connection con = null;
		Properties prop = null;		
		boolean flag = false;
		
		int total_cnt=0;
		
		DBConnection db = DBConnection.getInstance();
		prop = db.getProp(null);
		
		ReservationDAO rDAO = ReservationDAO.getInstance();
		
		//유저가 입력한 값(예약일, 요청사항, 정비항목)과 로그인한 유저/차량 정보가 cDTO 에 담겨 온다
		ReservationDTO crtDTO = cDTO;
		
		
		
		int seqNum = 0;
		String rsCode=null;
		try {
			con = db.getConn(prop);
			con.setAutoCommit(false);
			
			//seq객체에서 예약 번호 값 가져오기
			seqNum = rDAO.selectSeqReserv(con);
			//예약코드 생성
			
			
			rsCode = String.format("rs%03d", seqNum);
			
			crtDTO.setReservationCode(rsCode);
			
			//예약 테이블에 값 넣기
			int resultReserv = rDAO.insertReservData(con, crtDTO);
			if(resultReserv ==0) {
				System.err.println("예약테이블에 값이 들어가지 않았습니다");
			}
			
			//부품이름에 대한 코드를 먼저 얻기
			String repairItemCode = rDAO.selectRepairItemCode(con, cDTO);
			if(repairItemCode ==null) {
				System.err.println("부품코드 값을 가져오지 못했습니다");
			}
			
			//reservpart테이블에 값 넣기
			int resultReservPart = rDAO.insertReservParts(con, rsCode, repairItemCode);
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
			
				db.dbClose(null, null, con);
			}
		
			
		
	return flag;
	
		
	
		
		
	}
	
	//예약확인에서 보여줄 컬럼 
	public List<ReservationDTO> searchReservationsByUserId(String userId) throws SQLException {
		
		Connection con = null;
		Properties prop = null;
		DBConnection db = DBConnection.getInstance();
		
		List<ReservationDTO> crfDTO = null;
		
		
		
		prop = db.getProp(null);
		
		try {
			con = db.getConn(prop);
			ReservationDAO rDAO = ReservationDAO.getInstance();
			
			crfDTO = rDAO.selectReservDetailList(con,userId);
			
		} finally {
			db.dbClose(null, null, con);
		}
		
		return crfDTO;
		
		
	}
	
	//유저 예약하기 왼쪽 화면에 데이터 채우줘는 서비스 로직
	public List<String[]> searchReservedList(String userId) throws SQLException {
		
		Connection con = null;
		Properties prop = null;
		DBConnection db = DBConnection.getInstance();
		prop = db.getProp(null);
		
		List<String[]> reservedRow = null;
		
		try {
			con = db.getConn(prop);
			ReservationDAO rDAO = ReservationDAO.getInstance();
			
			reservedRow = rDAO.selectReservList(con, userId);
			
		} 
		
		finally {
			db.dbClose(null, null, con);
		}
		
		return reservedRow;
		
		
		
		
	}
	
	//삭제 트랜잭션 같이 연결된 다대다 테이블도 삭제 해야댐
	public boolean removeReservation(String rsCode) throws SQLException{
		
		Connection con = null;
		Properties prop = null;
		DBConnection db = DBConnection.getInstance();
		prop = db.getProp(null);
		
		boolean flag=false;
		
		int delReservParts = 0;
		int delReserv=0;
		int total=0;
		
		ReservationDAO rDAO = ReservationDAO.getInstance();
		
		try {
			con = db.getConn(prop);
			con.setAutoCommit(false);
			delReservParts = rDAO.deleteReservParts(con, rsCode);
			if(delReservParts==0) {
				System.out.println(" 예약파츠 데이터가 삭제되지 않았습니다");
				con.rollback();
				return false;
				
			}
			delReserv = rDAO.deleteReserv(con, rsCode);
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
			db.dbClose(null, null, con);
		}
		
		return flag;
		
		
		
		
	
	}
	
	/** 정비 항목 가져오기 */
	public List<RepairItemDTO> searchRepairItems() {
		List<RepairItemDTO> list = new java.util.ArrayList<RepairItemDTO>();
		try {
			list = ReservationDAO.getInstance().selectAllRepairItems();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
