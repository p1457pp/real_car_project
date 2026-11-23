package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dto.CarReservDialogDTO;
import car_dto.CarReservFourColumnDTO;
import car_dto.CarReservTableAdminDTO;
import car_dto.CarReservTableDTO;
import car_dto.CarReservUserUIDTO;
import car_dto.ReservPartsDTO;
import car_util.DbConn;

public class CarReservDAO {

	private static CarReservDAO crDAO;

	private DbConn db = DbConn.getInstance();

	private CarReservDAO() {

	}

	public static CarReservDAO getInstance() {
		if (crDAO == null) {
			crDAO = new CarReservDAO();
		}
		return crDAO;
	}
	
	//다이올로그에서 유저 예약을 취소하면 예약상태를 변경하는 코드이다
		public int updateCancelReserv(Connection con,String rsCode) throws SQLException {
			// TODO Auto-generated method stub
			PreparedStatement pstmt = null;
			int result =0;
			
			try {
				
				StringBuilder updateLine = new StringBuilder();
				
				updateLine
				.append("	update reserv set RESERVATION_PERMISSION=2		")
				.append("	where RESERVATION_CODE=? and is_deleted=1		");
				
				
				
				
				
				pstmt = con.prepareStatement(updateLine.toString());
				
				pstmt.setString(1,rsCode );
				
				
				result = pstmt.executeUpdate();
				
				
			}finally {
				db.dbClose(null, pstmt, null);
			}
			
			
			
			
			return result;
		}
	
	//업데이트 하면 선택한 유저의 예약상태가 변경되어야 한다 업데이트한다-관리자
	public int updateFinalDate(Connection con,String confirmDate,String rsCode) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null;
		int result =0;
		
		try {
			
			StringBuilder updateLine = new StringBuilder();
			
			updateLine
			.append("	update reserv set MECHAN_RESERV_DATE=TO_DATE(?, 'YYYY-MM-DD'), FINAL_DATE=TO_DATE(?, 'YYYY-MM-DD') ,RESERVATION_PERMISSION=1		")
			.append("	where RESERVATION_CODE=? and is_deleted=1		");
			
			
			
			
			
			pstmt = con.prepareStatement(updateLine.toString());
			
			pstmt.setString(1,confirmDate );
			pstmt.setString(2,confirmDate );
			pstmt.setString(3,rsCode );
			
			result = pstmt.executeUpdate();
			
			
		}finally {
			db.dbClose(null, pstmt, null);
		}
		
		
		
		
		return result;
	}
	
	//상세보기를 누르면 해당 행에 대한 상세보기가 나와야하는 코드이다 -관리자용
	public  List<CarReservDialogDTO>  selectReservRequest (Connection con,String rsCode) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<CarReservDialogDTO> crdList = new ArrayList<CarReservDialogDTO>();
		
		try {
			StringBuilder selectedLine = new StringBuilder();
			selectedLine
			.append(" select ADDTIONAL_REQUEST,USER_RESERV_DATE	")
			.append(" from RESERV	")
			.append(" where RESERVATION_CODE=?")
			.append(" and is_deleted=1    ");
			
			pstmt = con.prepareStatement(selectedLine.toString());
			
			pstmt.setString(1, rsCode);
			
			rs = pstmt.executeQuery();
			CarReservDialogDTO crdDTO = new CarReservDialogDTO();
			
			if (rs.next()) {
				crdDTO.setAddtional_request(rs.getString("ADDTIONAL_REQUEST"));
				crdDTO.setUser_reserv_date(rs.getDate("USER_RESERV_DATE").toString());
				crdList.add(crdDTO);
				
				
			}
			
			
		} finally {
			db.dbClose(rs, pstmt, null);
		}
		return crdList;
		
		
	}
	
	//관리자가 맨처음 모든 유저의 테이블을 조회하는 메서드-관리자용
	public List<CarReservTableAdminDTO> selectAllReservAdmin(Connection con,String userId)throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<CarReservTableAdminDTO> crList = new ArrayList<CarReservTableAdminDTO>();

		try {
			StringBuilder selectedLine = new StringBuilder();
			
				
				selectedLine
				.append("  select r.RESERVATION_CODE,r.USER_ID,ri.REPAIR_ITEM,r.USER_RESERV_DATE,r.FINAL_DATE,rs.RESERV_STATE   ")
				.append("   from reserv r  ")
				.append("   join RESERV_PARTS rp on r.RESERVATION_CODE = rp.RESERVATION_CODE  ")
				.append("   join RESERV_STATE rs on r.RESERVATION_PERMISSION = rs.RESERV_CODE  ")
				.append("   join REPAIR_ITEM ri on rp.REPAIR_CODE = ri.REPAIR_CODE  ")
				.append("where r.is_deleted =1 ")
				;
				
				boolean flag = userId!=null && !userId.isEmpty();
				
				if(flag) {
					selectedLine.append(" and r.user_id=? ");
					
				}
				
				pstmt = con.prepareStatement(selectedLine.toString());
				
				if(flag) {
					pstmt.setString(1, userId);
				}
			

			

			

			rs = pstmt.executeQuery();
			
			CarReservTableAdminDTO crDTO = null;
			while (rs.next()) {
				crDTO = new CarReservTableAdminDTO();
				crDTO.setReservation_code(rs.getString("RESERVATION_CODE"));
				crDTO.setUser_id(rs.getString("USER_ID"));
				crDTO.setRepair_item(rs.getString("REPAIR_ITEM"));
				crDTO.setUser_reserv_date(rs.getDate("USER_RESERV_DATE").toString());
				if(rs.getDate("FINAL_DATE")==null) {
					crDTO.setFinal_date("미정");
				}
				else {
					crDTO.setFinal_date(rs.getDate("FINAL_DATE").toString());
				}
				
				crDTO.setReserv_state(rs.getString("RESERV_STATE"));
				crList.add(crDTO);
				
			}
		} finally {
			db.dbClose(rs, pstmt, null);
		}

		return crList;
		
	}
	
	

	// 에약테이블 시퀸스 구하기-유저
	public int selectSeqReserv(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int seqReservNum = 0;

		try {

			StringBuilder selectedSeqNum = new StringBuilder();
			selectedSeqNum.append(" select SEQ_RESERV.nextval as seq_reserv_num     ").append("  from dual   ");

			pstmt = con.prepareStatement(selectedSeqNum.toString());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				seqReservNum = rs.getInt("seq_reserv_num");
			}
		} finally {
			db.dbClose(rs, pstmt, null);
		}

		return seqReservNum;

	}
	
	//리페어 아이템코드 가져오는 기능
	public String selectRepairItemCode(Connection con, CarReservUserUIDTO cDTO) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String repairItemCode = null;

		try {

			StringBuilder selectedRepairCode = new StringBuilder();
			selectedRepairCode.append(" select REPAIR_CODE       ")
			.append(" from REPAIR_ITEM   ")
					.append(" where REPAIR_ITEM=?    ");

			pstmt = con.prepareStatement(selectedRepairCode.toString());

			pstmt.setString(1, cDTO.getRepairPart());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				repairItemCode = rs.getString("REPAIR_CODE");
			}
		} finally {
			db.dbClose(rs, pstmt, null);
		}

		return repairItemCode;

	}
	//예약확인 누르면 유저의 예약데이터를 전달한다-유저용
	public List<CarReservFourColumnDTO> selectReservDetailList(Connection con,String userId) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<CarReservFourColumnDTO> crfList = new ArrayList<CarReservFourColumnDTO>(); 
		
		try {
			StringBuilder selectedLine = new StringBuilder();
			selectedLine
			.append(" select r.RESERVATION_CODE,ci.CAR_NUM,to_char(r.USER_RESERV_DATE,'YYYY-MM-DD') USER_RESERV_DATE ,ri.REPAIR_ITEM,rs.RESERV_STATE  ")
			.append("  from reserv r  ")
			.append("  join CAR_INFO ci on r.VEHICLE_ID = ci.VEHICLE_ID  ")
			.append("  join RESERV_PARTS rp on r.RESERVATION_CODE = rp.RESERVATION_CODE  ")
			.append("  join REPAIR_ITEM ri on rp.REPAIR_CODE=ri.REPAIR_CODE  ")
			.append("  join USERS u on r.USER_ID=u.USER_ID  ")
			.append("  join RESERV_STATE rs on r.RESERVATION_PERMISSION = rs.RESERV_CODE ")
			.append("where u.USER_ID=?")
			.append(" and r.is_deleted=1 ");
			pstmt = con.prepareStatement(selectedLine.toString());
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			CarReservFourColumnDTO crfDTO = null;
			while(rs.next()) {
				crfDTO = new CarReservFourColumnDTO();
				
			
				crfDTO.setReservation_code(rs.getString("RESERVATION_CODE"));
				crfDTO.setCar_num(rs.getString("CAR_NUM"));
				crfDTO.setCar_model(rs.getString("USER_RESERV_DATE"));
				crfDTO.setRepair_item(rs.getString("REPAIR_ITEM"));
				crfDTO.setReservation_permission(rs.getString("RESERV_STATE"));
				
				crfList.add(crfDTO);
				
				
				
			}
			
		} finally {
			db.dbClose(rs, pstmt, null);
		}
		return crfList;
		
	}
	
	//유저가 예약한 내역을 보여줘야함-유저용
	public List<String[]> selectReservList(Connection con, String userId) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<String[]> crList = new ArrayList<String[]>();

		try {
			StringBuilder selectedLine = new StringBuilder();
			selectedLine.append(" select ci.CAR_NUM,r.USER_RESERV_DATE,ci.CAR_MODEL,ri.REPAIR_ITEM  ")
					.append(" from reserv r ").append(" join CAR_INFO ci on r.VEHICLE_ID = ci.VEHICLE_ID  ")
					.append(" join RESERV_PARTS rp on r.RESERVATION_CODE = rp.RESERVATION_CODE  ")
					.append(" join REPAIR_ITEM ri on rp.REPAIR_CODE=ri.REPAIR_CODE  ")
					.append(" join USERS u on r.USER_ID=u.USER_ID  ")
					.append(" where u.USER_ID=?  ")
					.append(" and r.is_deleted=1  ");

			pstmt = con.prepareStatement(selectedLine.toString());

			// 유저 정보는 나중에 로그인 성공후 해당 로직에서 받아올것
			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();

			String[] reservRecord = null;

			while (rs.next()) {

				reservRecord = new String[4];
				reservRecord[0] = rs.getString("CAR_NUM");
				reservRecord[1] = rs.getDate("USER_RESERV_DATE").toString();
				reservRecord[2] = rs.getString("CAR_MODEL");
				reservRecord[3] = rs.getString("REPAIR_ITEM");

				crList.add(reservRecord);

			}

		} finally {
			db.dbClose(rs, pstmt, null);
		}
		return crList;

	}

	/**
	 * 중간테이블인 예약부품테이블에 데이터를 넣는 dao이다
	 * 
	 * @param con
	 * @param rpDTO 예약부품테이블에 대한 dto
	 * @return
	 * @throws SQLException
	 */
	public int insertReservParts(Connection con, ReservPartsDTO rpDTO) throws SQLException {

		PreparedStatement pstmt = null;
		int result = 0;
		try {
			StringBuilder insertLine = new StringBuilder();
			insertLine.append(" insert into RESERV_PARTS(RESERVATION_CODE, REPAIR_CODE) values(?,?)");

			pstmt = con.prepareStatement(insertLine.toString());
			pstmt.setString(1, rpDTO.getReservation_code());
			pstmt.setString(2, rpDTO.getRepair_code());

			result = pstmt.executeUpdate();

		} finally {

			db.dbClose(null, pstmt, null);
		}

		return result;

	}

	/**
	 * 예약 DAO
	 * 
	 * @param cDTO ui에 있는 회원 정보를 가져온다 , 유저 아이디와 차량 정보는 로그인후 내부 클래스에서 가져온다
	 * 
	 * @throws SQLException
	 */

	public int insertReservData(Connection con, CarReservTableDTO crtDTO) throws SQLException {

		PreparedStatement pstmt = null;
		int result = 0;

		try {

			StringBuilder insertLine = new StringBuilder();
			insertLine.append("  insert into RESERV(RESERVATION_CODE, USER_RESERV_DATE, ADDTIONAL_REQUEST,"
					+ "VEHICLE_ID, USER_ID)  ").append("  values(?,?,?,?,?)  ");

			pstmt = con.prepareStatement(insertLine.toString());
			pstmt.setString(1, crtDTO.getReservation_code());
			pstmt.setString(2, crtDTO.getUser_reserv_date());
			pstmt.setString(3, crtDTO.getAddtional_request());

			// 이건 내부에서 넣어야함 나중에 로그인시 유저하고 챠량 정보 넣기
			pstmt.setInt(4, crtDTO.getVehicle_id());
			pstmt.setString(5, crtDTO.getUser_id());

			result = pstmt.executeUpdate();

		} finally {
			db.dbClose(null, pstmt, null);
		}

		return result;
	}
	
	// 테이블에서 삭제 버튼을 누르면 자식 테이블 먼저 삭제 예약부품
	public int deleteReservParts(Connection con,String rsCode) throws SQLException {
		
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			StringBuilder deleteLine = new StringBuilder();
			deleteLine
			.append(" delete from RESERV_PARTS   ")
			.append(" where RESERVATION_CODE=?   ");
			
			pstmt = con.prepareStatement(deleteLine.toString());
			pstmt.setString(1, rsCode);
			result = pstmt.executeUpdate();
			
			
		} finally {
			db.dbClose(null, pstmt, null);
		}
		
		return result;
		
		
	}
	
	public int deleteReserv(Connection con,String rsCode) throws SQLException {
		
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			//실제 값을 지우지는 않고 상태만 변경
			StringBuilder updateLine = new StringBuilder();
			updateLine
			.append(" update reserv set is_deleted=0   ")
			.append(" where RESERVATION_CODE=?   ")
			.append(" and is_deleted=1   ");
			
			pstmt = con.prepareStatement(updateLine.toString());
			pstmt.setString(1, rsCode);
			result = pstmt.executeUpdate();
			
		} finally {
			db.dbClose(null, pstmt, null);
		}
		
		return result;
		
		
	}

	

//	//전체 예약 데이터 조회하기 테스트용
//	public List<CarReservDTO> selectAllReserv() throws SQLException {
//		
//		List<CarReservDTO> listCarReserv = new ArrayList<CarReservDTO>();
//		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		DBConn dc = DBConn.getInstance();
//		Properties prop = dc.getProp(null);
//		
//		try {
//			con = dc.getConn(prop);
//			StringBuilder selectedLine = new StringBuilder();
//			selectedLine
//			.append("select reservation_code ")
//			.append("from RESERV ")
//			.append("where USER_ID=? ");
//			
//			
//			
//			pstmt = con.prepareStatement(selectedLine.toString());
//			
//			pstmt.setString(1, "user001");
//			
//			//특정 인물 조회하기
//			
//			rs = pstmt.executeQuery();
//			CarReservDTO reservDTO = null;
//			while(rs.next()) {
//				reservDTO = new CarReservDTO();
//				
//				//예약 코드 조회하기
//				reservDTO.(rs.getString("RESERVATION_CODE"));
//				listCarReserv.add(reservDTO);
//				
//			}
//	
//		} finally {
//			dc.DbClose(rs, pstmt, con);
//		}
//		
//		return listCarReserv; 
//		
//		
//		
//	}

}
