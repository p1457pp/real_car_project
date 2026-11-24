package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import car_dto.CarDTO;
import car_dto.MaintenanceDTO;
import car_dto.RepairItemDTO;
import car_dto.ReservationDTO;
import car_util.DBConnection;

public class ReservationDAO {

	private static ReservationDAO rDAO;

	private DBConnection db = DBConnection.getInstance();

	private ReservationDAO() {

	}

	public static ReservationDAO getInstance() {
		if (rDAO == null) {
			rDAO = new ReservationDAO();
		}
		return rDAO;
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
	public  List<ReservationDTO>  selectReservRequest (Connection con,String rsCode) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<ReservationDTO> crdList = new ArrayList<ReservationDTO>();
		
		try {
			StringBuilder selectedLine = new StringBuilder();
			selectedLine
			.append(" select ADDTIONAL_REQUEST,USER_RESERV_DATE,VEHICLE_ID,FINAL_DATE	")
			.append(" from RESERV	")
			.append(" where RESERVATION_CODE=?")
			.append(" and is_deleted=1    ");
			
			pstmt = con.prepareStatement(selectedLine.toString());
			
			pstmt.setString(1, rsCode);
			
			rs = pstmt.executeQuery();
			ReservationDTO crdDTO = new ReservationDTO();
			
			if (rs.next()) {
				crdDTO.setAdditionalRequest(rs.getString("ADDTIONAL_REQUEST"));
				crdDTO.setPreferredReservationDate(rs.getDate("USER_RESERV_DATE"));
				crdDTO.setVehicleId(rs.getInt("VEHICLE_ID"));
				crdDTO.setConfirmedDate(rs.getDate("FINAL_DATE"));
				crdList.add(crdDTO);
				
				
			}
			
			
		} finally {
			db.dbClose(rs, pstmt, null);
		}
		return crdList;
		
		
	}
	
	//관리자가 맨처음 모든 유저의 테이블을 조회하는 메서드-관리자용
	public List<ReservationDTO> selectAllReservAdmin(Connection con,String userId)throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<ReservationDTO> crList = new ArrayList<ReservationDTO>();

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
			
			ReservationDTO crDTO = null;
			while (rs.next()) {
				crDTO = new ReservationDTO();
				crDTO.setReservationCode(rs.getString("RESERVATION_CODE"));
				crDTO.setUserId(rs.getString("USER_ID"));
				crDTO.setRepairItem(rs.getString("REPAIR_ITEM"));
				crDTO.setPreferredReservationDate(rs.getDate("USER_RESERV_DATE"));
				// FINAL_DATE 가 null 이면 확정일 없음("미정" 표시는 Evt 에서 한다)
				if(rs.getDate("FINAL_DATE")!=null) {
					crDTO.setConfirmedDate(rs.getDate("FINAL_DATE"));
				}
				
				crDTO.setReserveState(rs.getString("RESERV_STATE"));
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
	public String selectRepairItemCode(Connection con, ReservationDTO cDTO) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String repairItemCode = null;

		try {

			StringBuilder selectedRepairCode = new StringBuilder();
			selectedRepairCode.append(" select REPAIR_CODE       ")
			.append(" from REPAIR_ITEM   ")
					.append(" where REPAIR_ITEM=?    ");

			pstmt = con.prepareStatement(selectedRepairCode.toString());

			pstmt.setString(1, cDTO.getRepairItem());

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
	public List<ReservationDTO> selectReservDetailList(Connection con,String userId) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<ReservationDTO> crfList = new ArrayList<ReservationDTO>(); 
		
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
			
			ReservationDTO crfDTO = null;
			while(rs.next()) {
				crfDTO = new ReservationDTO();
				
			
				crfDTO.setReservationCode(rs.getString("RESERVATION_CODE"));
				crfDTO.setCarNum(rs.getString("CAR_NUM"));
				crfDTO.setCarModel(rs.getString("USER_RESERV_DATE"));
				crfDTO.setRepairItem(rs.getString("REPAIR_ITEM"));
				crfDTO.setReserveState(rs.getString("RESERV_STATE"));
				
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
	 * @param reservationCode 예약코드
	 * @param repairCode 정비항목 코드
	 * @return
	 * @throws SQLException
	 */
	public int insertReservParts(Connection con, String reservationCode, String repairCode) throws SQLException {

		PreparedStatement pstmt = null;
		int result = 0;
		try {
			StringBuilder insertLine = new StringBuilder();
			insertLine.append(" insert into RESERV_PARTS(RESERVATION_CODE, REPAIR_CODE) values(?,?)");

			pstmt = con.prepareStatement(insertLine.toString());
			pstmt.setString(1, reservationCode);
			pstmt.setString(2, repairCode);

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

	public int insertReservData(Connection con, ReservationDTO crtDTO) throws SQLException {

		PreparedStatement pstmt = null;
		int result = 0;

		try {

			StringBuilder insertLine = new StringBuilder();
			insertLine.append("  insert into RESERV(RESERVATION_CODE, USER_RESERV_DATE, ADDTIONAL_REQUEST,"
					+ "VEHICLE_ID, USER_ID, RESERVATION_PERMISSION)  ").append("  values(?,?,?,?,?,0)  ");

			pstmt = con.prepareStatement(insertLine.toString());
			pstmt.setString(1, crtDTO.getReservationCode());
			pstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(crtDTO.getPreferredReservationDate()));
			pstmt.setString(3, crtDTO.getAdditionalRequest());

			// 이건 내부에서 넣어야함 나중에 로그인시 유저하고 챠량 정보 넣기
			pstmt.setInt(4, crtDTO.getVehicleId());
			pstmt.setString(5, crtDTO.getUserId());

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

	/**
	 * 정비 항목 전체 조회
	 * 
	 * @return 정비 항목 목록
	 * @throws SQLException
	 */
	public List<RepairItemDTO> selectAllRepairItems() throws SQLException {
		List<RepairItemDTO> list = new ArrayList<RepairItemDTO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			StringBuilder sql = new StringBuilder();
			sql
			.append("	select repair_code, repair_item, unit_price, lab_cost, addtional_date	")
			.append("	from repair_item	")
			.append("	order by repair_code	");

			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new RepairItemDTO(rs.getString("repair_code"), rs.getString("repair_item"),
						rs.getInt("unit_price"), rs.getInt("lab_cost"), rs.getDate("addtional_date")));
			}
		} finally {
			dbCon.dbClose(rs, pstmt, con);
		}
		return list;
	}

	/**
	 * 특정 차량 정보
	 * 
	 * @param vehicleId
	 * @return CarDTO
	 * @throws SQLException
	 */
	public CarDTO selectCarInfoByVehicleId(int vehicleId) throws SQLException {
		return CarDAO.getInstance().selectByVehicleId(vehicleId);
	}

	/**
	 * 특정 차량 정비정보
	 * 
	 * @param vehicleId
	 * @return 정비 목록
	 */
	public List<MaintenanceDTO> selectMaintenanceInfoByVehicleId(int vehicleId) {
		return MaintenanceDAO.getInstance().selectByVehicleId(vehicleId);
	}

}
