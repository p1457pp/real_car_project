package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dto.AdminDTO;
import car_dto.UserDTO;
import car_util.DBConnection;

public class AdminDAO {

	private static AdminDAO aDAO;

	private AdminDAO() {
	}

	public static AdminDAO getInstance() {
		if (aDAO == null) {
			aDAO = new AdminDAO();
		}
		return aDAO;
	}

	/**
	 * 관리자 로그인
	 * 
	 * @param aDTO
	 * @return 일치하는 관리자 아이디, 없으면 빈 문자열
	 * @throws SQLException
	 */
	public String login(AdminDTO aDTO) throws SQLException {
		String id = "";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			StringBuilder selectAdmin = new StringBuilder();
			selectAdmin
			.append("	select admin_id	")
			.append("	from admin	")
			.append("	where admin_id=? and password=?	");

			pstmt = con.prepareStatement(selectAdmin.toString());
			pstmt.setString(1, aDTO.getId());
			pstmt.setString(2, aDTO.getPass());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getString("admin_id");
			}
		} finally {
			dbCon.dbClose(rs, pstmt, con);
		}
		return id;
	}

	/**
	 * 회원 아이디로 검색 (부분 일치). 빈 문자열이면 전체
	 * 
	 * @param keyword
	 * @return 회원 목록
	 * @throws SQLException
	 */
	public List<UserDTO> selectByUserId(String keyword) throws SQLException {
		List<UserDTO> list = new ArrayList<UserDTO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			StringBuilder selectUser = new StringBuilder();
			selectUser
			.append("	select user_id, user_name, email, phone, user_grade,	")
			.append("			to_char(join_date, 'yyyy-mm-dd') join_date	")
			.append("	from users	")
			.append("	where is_deleted=1 and user_id like '%'||?||'%'	")
			.append("	order by join_date desc, user_id	");

			pstmt = con.prepareStatement(selectUser.toString());
			pstmt.setString(1, keyword == null ? "" : keyword);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new UserDTO(rs.getString("user_id"), rs.getString("user_name"), rs.getString("email"),
						rs.getString("phone"), rs.getString("user_grade"), rs.getString("join_date")));
			}
		} finally {
			dbCon.dbClose(rs, pstmt, con);
		}
		return list;
	}

	/**
	 * 전체 회원 조회 (삭제되지 않은 회원)
	 * 
	 * @return 회원 목록
	 * @throws SQLException
	 */
	public List<UserDTO> selectAllUser() throws SQLException {
		return selectByUserId("");
	}

	/**
	 * 회원 삭제 (is_deleted 0 처리, 회원의 차량/예약도 소프트삭제. 정비 행은 보존)
	 * 
	 * @param userId
	 * @return cnt
	 * @throws SQLException
	 */
	public int updateDeleteState(String userId) throws SQLException {
		int cnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtCar = null;
		PreparedStatement pstmtReserv = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			con.setAutoCommit(false);

			// 1) 회원 소프트삭제
			StringBuilder updateState = new StringBuilder();
			updateState
			.append("	update users	")
			.append("	set is_deleted=0	")
			.append("	where user_id=?	");

			pstmt = con.prepareStatement(updateState.toString());
			pstmt.setString(1, userId);
			cnt = pstmt.executeUpdate();

			// 2) 회원의 차량도 소프트삭제 (예약/정비 이력은 행을 보존하고, 조회에서만 숨김)
			pstmtCar = con.prepareStatement("	update car_info set is_deleted=0 where user_id=?	");
			pstmtCar.setString(1, userId);
			pstmtCar.executeUpdate();

			// 3) 회원의 예약도 소프트삭제 (RESERV.is_deleted 0 = 삭제)
			pstmtReserv = con.prepareStatement("	update reserv set is_deleted=0 where user_id=?	");
			pstmtReserv.setString(1, userId);
			pstmtReserv.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ignore) {
				}
			}
			throw e;
		} finally {
			if (pstmtCar != null) {
				try {
					pstmtCar.close();
				} catch (SQLException ignore) {
				}
			}
			if (pstmtReserv != null) {
				try {
					pstmtReserv.close();
				} catch (SQLException ignore) {
				}
			}
			dbCon.dbClose(null, pstmt, con);
		}
		return cnt;
	}

	/**
	 * 회원 정보 수정 (이름, 이메일, 전화번호, 등급)
	 * 
	 * @param uDTO
	 * @return cnt
	 * @throws SQLException
	 */
	public int updateUser(UserDTO uDTO) throws SQLException {
		int cnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			StringBuilder updateUser = new StringBuilder();
			updateUser
			.append("	update users	")
			.append("	set user_name=?, email=?, phone=?, user_grade=?	")
			.append("	where user_id=?	");

			pstmt = con.prepareStatement(updateUser.toString());
			pstmt.setString(1, uDTO.getName());
			pstmt.setString(2, uDTO.getEmail());
			pstmt.setString(3, uDTO.getPhone());
			pstmt.setString(4, uDTO.getGrade());
			pstmt.setString(5, uDTO.getUserId());

			cnt = pstmt.executeUpdate();
		} finally {
			dbCon.dbClose(null, pstmt, con);
		}
		return cnt;
	}

}
