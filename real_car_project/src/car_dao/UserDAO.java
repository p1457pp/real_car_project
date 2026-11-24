package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import car_dto.UserDTO;
import car_util.DBConnection;

public class UserDAO {

	private static UserDAO uDAO;

	private UserDAO() {
	}

	public static UserDAO getInstance() {
		if (uDAO == null) {
			uDAO = new UserDAO();
		}
		return uDAO;
	}

	/**
	 * 새로운 유저 정보 추가 (회원가입)
	 * 
	 * @param uDTO
	 * @return cnt
	 * @throws SQLException
	 */
	public int insertUser(UserDTO uDTO) throws SQLException {
		int cnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			StringBuilder insertUser = new StringBuilder();
			insertUser
			.append("	insert into users (user_id, password, user_name, email, phone, user_grade, role, join_date, is_deleted)	")
			.append("	values(?, ?, ?, ?, ?, '일반', 'USER', sysdate, 1)	");

			pstmt = con.prepareStatement(insertUser.toString());
			pstmt.setString(1, uDTO.getUserId());
			pstmt.setString(2, uDTO.getPassword());
			pstmt.setString(3, uDTO.getName());
			pstmt.setString(4, uDTO.getEmail());
			pstmt.setString(5, uDTO.getPhone());

			cnt = pstmt.executeUpdate();
		} finally {
			dbCon.dbClose(null, pstmt, con);
		}
		return cnt;
	}

	/**
	 * 유저 정보 수정 (이름, 이메일, 전화번호)
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
			.append("	set user_name=?, email=?, phone=?	")
			.append("	where user_id=?	");

			pstmt = con.prepareStatement(updateUser.toString());
			pstmt.setString(1, uDTO.getName());
			pstmt.setString(2, uDTO.getEmail());
			pstmt.setString(3, uDTO.getPhone());
			pstmt.setString(4, uDTO.getUserId());

			cnt = pstmt.executeUpdate();
		} finally {
			dbCon.dbClose(null, pstmt, con);
		}
		return cnt;
	}

	/**
	 * 아이디, 비밀번호가 일치하는 유저의 아이디 조회 (로그인)
	 * 
	 * @param uDTO
	 * @return 일치하는 유저 아이디, 없으면 빈 문자열
	 * @throws SQLException
	 */
	public String login(UserDTO uDTO) throws SQLException {
		String id = "";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DBConnection dbCon = DBConnection.getInstance();

		try {
			con = dbCon.getConn(dbCon.getProp(null));
			StringBuilder selectLogin = new StringBuilder();
			selectLogin
			.append("	select user_id	")
			.append("	from users	")
			.append("	where user_id=? and password=? and is_deleted=1	");

			pstmt = con.prepareStatement(selectLogin.toString());
			pstmt.setString(1, uDTO.getUserId());
			pstmt.setString(2, uDTO.getPassword());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				id = rs.getString("user_id");
			}
		} finally {
			dbCon.dbClose(rs, pstmt, con);
		}
		return id;
	}

	/**
	 * 특정 유저 정보 조회
	 * 
	 * @param userId
	 * @return UserDTO, 없으면 null
	 * @throws SQLException
	 */
	public UserDTO selectByUserId(String userId) throws SQLException {
		UserDTO uDTO = null;

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
			.append("	where user_id=? and is_deleted=1	");

			pstmt = con.prepareStatement(selectUser.toString());
			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				uDTO = new UserDTO(rs.getString("user_id"), rs.getString("user_name"), rs.getString("email"),
						rs.getString("phone"), rs.getString("user_grade"), rs.getString("join_date"));
			}
		} finally {
			dbCon.dbClose(rs, pstmt, con);
		}
		return uDTO;
	}

}
