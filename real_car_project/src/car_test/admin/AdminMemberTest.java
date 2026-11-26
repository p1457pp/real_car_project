package car_test.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import admin.car_service.AdminMemberManagementService;   // 실제 패키지에 맞게 수정
import car_dao.AdminMemberManagementDAO;
import car_dto.AdminMemberManagementDTO;
import car_util.DBConnection;

/**
 * 회원관리 DAO / Service 테스트 (실제 DB의 기존 회원 user001 사용)
 *
 * 기준 데이터
 *   회원아이디 user001 / 회원명 홍길동 / 이메일 hong@test.com
 *   전화번호 010-1111-2222 / 회원 등급 일반 / 가입일 2026-11-11
 *
 * 안전장치
 *   - 각 테스트 직전에 user001의 현재 값을 백업하고, 테스트 직후(@AfterEach)에 원래 값으로 복구한다.
 *   - 테스트가 실패해도 @AfterEach는 실행되므로 데이터가 바뀐 채로 남지 않는다.
 *   - user001의 실제 삭제 테스트는 @Disabled 상태이다 (아래 주석 참고).
 */
class AdminMemberTest {

	// 기준 데이터
	private static final String ID = "user001";
	private static final String NAME = "홍길동";
	private static final String EMAIL = "hong@test.com";
	private static final String PHONE = "010-1111-2222";
	private static final String GRADE = "일반";
	private static final String JOIN_DATE = "2026-11-11";

	private static final String NO_USER = "no_such_user_x";   // 존재하지 않는 아이디

	private AdminMemberManagementDAO dao;
	private AdminMemberManagementService service;
	private Snapshot backup;   // 테스트 전 user001의 DB 상태

	// ===================== 준비 / 복구 =====================

	@BeforeEach
	void setUp() throws SQLException {
		dao = AdminMemberManagementDAO.getInstance();
		service = new AdminMemberManagementService();

		backup = readSnapshot(ID);
		// user001이 DB에 없으면 테스트를 실패시키지 않고 건너뜀
		assumeTrue(backup != null, ID + " 회원이 DB에 없어 테스트를 건너뜁니다.");
	}

	@AfterEach
	void tearDown() throws SQLException {
		restore(backup);   // 수정/삭제된 값을 원래대로 복구
	}

	// ===================== DAO =====================

	@Test
	@DisplayName("DAO: 전체 조회 - user001의 값이 기준 데이터와 같다")
	void selectAllMember_user001() throws SQLException {
		AdminMemberManagementDTO found = findById(dao.selectAllMember(), ID);

		assertNotNull(found, ID + " 회원이 조회되어야 합니다.");
		assertEquals(NAME, found.getName());
		assertEquals(EMAIL, found.getEmail());
		assertEquals(PHONE, found.getPhone());
		assertEquals(GRADE, found.getGrade());
		assertEquals(JOIN_DATE, found.getDate());
	}

	@Test
	@DisplayName("DAO: 수정 - 1건 수정되고 값이 DB에 반영된다")
	void updateUser_success() throws SQLException {
		AdminMemberManagementDTO dto =
				new AdminMemberManagementDTO(ID, "김수정", "kim@test.com", "010-9999-8888", "우수", null);

		assertEquals(1, dao.updateUser(dto));

		AdminMemberManagementDTO found = findById(dao.selectAllMember(), ID);
		assertNotNull(found);
		assertEquals("김수정", found.getName());
		assertEquals("kim@test.com", found.getEmail());
		assertEquals("010-9999-8888", found.getPhone());
		assertEquals("우수", found.getGrade());
		assertEquals(JOIN_DATE, found.getDate(), "가입일은 수정 대상이 아니므로 그대로여야 합니다.");
	}

	@Test
	@DisplayName("DAO: 수정 - 존재하지 않는 아이디는 0건")
	void updateUser_notExist() throws SQLException {
		AdminMemberManagementDTO dto =
				new AdminMemberManagementDTO(NO_USER, "이름", "a@a.com", "010-0000-0000", "일반", null);

		assertEquals(0, dao.updateUser(dto));
	}

	@Test
	@DisplayName("DAO: 삭제 - 존재하지 않는 아이디는 0건")
	void deleteUser_notExist() throws SQLException {
		assertEquals(0, dao.deleteUser(NO_USER));
	}

	/**
	 * user001을 실제로 삭제하는 테스트. 기본은 비활성화(@Disabled) 상태이다.
	 *
	 * 이유: 예약/차량 등 다른 테이블이 user001을 참조하면 FK 오류(ORA-02292)로 삭제 자체가 안 되고,
	 *       삭제가 되더라도 @AfterEach의 복구는 users 행만 다시 넣을 뿐 연결된 자식 데이터는 되살리지 못한다.
	 * 실행하려면 @Disabled를 지우고, 개인 테스트 DB에서만 돌릴 것.
	 */
	@Disabled("실제 회원을 삭제하므로 기본은 비활성화")
	@Test
	@DisplayName("DAO: 삭제 - user001이 삭제되고 목록에서 사라진다")
	void deleteUser_success() throws SQLException {
		assertEquals(1, dao.deleteUser(ID));
		assertNull(findById(dao.selectAllMember(), ID));
		// 복구는 @AfterEach에서 수행
	}

	// ===================== Service =====================

	@Test
	@DisplayName("Service: 전체 조회 - user001이 목록에 포함된다")
	void searchAllMember_user001() {
		List<AdminMemberManagementDTO> list = service.searchAllMember();

		assertNotNull(list);
		AdminMemberManagementDTO found = findById(list, ID);
		assertNotNull(found);
		assertEquals(NAME, found.getName());
	}

	@Test
	@DisplayName("Service: 수정 - 성공하면 true")
	void serviceUpdateUser_success() {
		AdminMemberManagementDTO dto =
				new AdminMemberManagementDTO(ID, "서비스수정", "svc@test.com", "010-2222-3333", "VIP", null);

		assertTrue(service.updateUser(dto));

		AdminMemberManagementDTO found = findById(service.searchAllMember(), ID);
		assertNotNull(found);
		assertEquals("서비스수정", found.getName());
		assertEquals("svc@test.com", found.getEmail());
		assertEquals("VIP", found.getGrade());
	}

	@Test
	@DisplayName("Service: 수정 - 없는 회원이면 false")
	void serviceUpdateUser_notExist() {
		AdminMemberManagementDTO dto =
				new AdminMemberManagementDTO(NO_USER, "이름", "a@a.com", "010-0000-0000", "일반", null);

		assertFalse(service.updateUser(dto));
	}

	@Test
	@DisplayName("Service: 삭제 - 없는 회원이면 false")
	void serviceDeleteUser_notExist() {
		assertFalse(service.deleteUser(NO_USER));
	}

	// ===================== 백업 / 복구 도우미 =====================

	/** 테스트 전 user001의 DB 상태 */
	private static class Snapshot {
		String id, password, name, email, phone, grade, joinDate;
		int isDeleted;
	}

	private Snapshot readSnapshot(String userId) throws SQLException {
		DBConnection dc = DBConnection.getInstance();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection(dc);
			pstmt = con.prepareStatement(
					"select user_id, password, user_name, email, phone, user_grade, "
					+ "       to_char(join_date, 'yyyy-mm-dd') join_date, is_deleted "
					+ "from users where user_id = ?");
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			Snapshot s = new Snapshot();
			s.id = rs.getString("user_id");
			s.password = rs.getString("password");
			s.name = rs.getString("user_name");
			s.email = rs.getString("email");
			s.phone = rs.getString("phone");
			s.grade = rs.getString("user_grade");
			s.joinDate = rs.getString("join_date");
			s.isDeleted = rs.getInt("is_deleted");
			return s;
		} finally {
			dc.dbClose(rs, pstmt, con);
		}
	}

	/** 백업 값으로 되돌린다. 행이 없으면(삭제 테스트 후) 다시 INSERT한다. */
	private void restore(Snapshot s) throws SQLException {
		if (s == null) {
			return;
		}
		DBConnection dc = DBConnection.getInstance();
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection(dc);

			pstmt = con.prepareStatement(
					"update users set user_name=?, email=?, phone=?, user_grade=? where user_id=?");
			pstmt.setString(1, s.name);
			pstmt.setString(2, s.email);
			pstmt.setString(3, s.phone);
			pstmt.setString(4, s.grade);
			pstmt.setString(5, s.id);
			int cnt = pstmt.executeUpdate();
			pstmt.close();

			if (cnt == 0) {   // 삭제되어 있다면 원래 값으로 다시 삽입 (가입일은 날짜까지만 복원됨)
				pstmt = con.prepareStatement(
						"insert into users (user_id, password, user_name, email, phone, user_grade, join_date, is_deleted) "
						+ "values (?, ?, ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'), ?)");
				pstmt.setString(1, s.id);
				pstmt.setString(2, s.password);
				pstmt.setString(3, s.name);
				pstmt.setString(4, s.email);
				pstmt.setString(5, s.phone);
				pstmt.setString(6, s.grade);
				pstmt.setString(7, s.joinDate);
				pstmt.setInt(8, s.isDeleted);
				pstmt.executeUpdate();
			}
		} finally {
			dc.dbClose(null, pstmt, con);
		}
	}

	private Connection getConnection(DBConnection dc) throws SQLException {
		Connection con = dc.getConn(dc.getProp(null));
		if (con == null) {
			throw new SQLException("DB 연결 실패: 설정 파일(c:/dev/config/database_project.properties)을 확인하세요.");
		}
		return con;
	}

	private AdminMemberManagementDTO findById(List<AdminMemberManagementDTO> list, String userId) {
		for (AdminMemberManagementDTO dto : list) {
			if (userId.equals(dto.getUserId())) {
				return dto;
			}
		}
		return null;
	}
}
