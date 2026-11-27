package car_test.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import car_dao.UserDAO;
import car_dto.UserDTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserMemberTest {

	UserDAO dao = UserDAO.getInstance();
	String testId = "test_ydh_99";

	@Order(1)
	@DisplayName("1. 회원가입 및 회원 정보 수정 테스트")
	@Test
	void testInsertAndUpdate() throws Exception {
		UserDTO newUser = new UserDTO(testId, "11223344", "테스트유저", "test99@test.com", "01012345678");
		int insertCnt = dao.insertUser(newUser);
		assertEquals(1, insertCnt);

		UserDTO editUser = new UserDTO(testId, "11223344", "수정유저", "edit99@test.com", "01099998888");
		int updateCnt = dao.updateUser(editUser);
		assertEquals(1, updateCnt);
	}

	@Order(2)
	@DisplayName("2. 로그인 및 회원 정보 조회 테스트")
	@Test
	void testLoginAndSelect() throws Exception {
		// 정상 로그인 확인
		String loginId = dao.login(new UserDTO(testId, "11223344", null, null, null));
		assertEquals(testId, loginId);

		// 실패 로그인 확인
		String failId = dao.login(new UserDTO(testId, "wrong_pw", null, null, null));
		assertEquals("", failId);

		// 회원 정보 단건 조회 확인
		UserDTO user = dao.selectByUserId(testId);
		assertNotNull(user);
		assertEquals(testId, user.getUserId());
	}
}