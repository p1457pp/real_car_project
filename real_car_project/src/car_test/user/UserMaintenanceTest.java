package car_test.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import car_dao.MaintenanceDAO;
import car_dto.MaintenanceDTO;

class UserMaintenanceTest {

	private MaintenanceDAO dao;

	@BeforeEach
	void setUp() {
		dao = MaintenanceDAO.getInstance();
	}

	@Test
	@DisplayName("1. 미등록 회원 조회 시 빈 목록 반환 검증")
	void testSelectInvalidUser() {
		assertTrue(dao.selectUserCars("non_existent_user_999").isEmpty());
		assertTrue(dao.selectUserMaintList("non_existent_user_999", "전체 기간", "전체 차량").isEmpty());
	}

	@Test
	@DisplayName("2. 사용자 정비이력 기간 필터 조회 및 미래 일자 제외 검증")
	void testSelectUserMaintListAndDetail() {
		String userId = "user019";
		String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

		assertNotNull(dao.selectUserCars(userId));
		assertNotNull(dao.selectUserMaintList(userId, "전체 기간", "전체 차량"));

		List<MaintenanceDTO> recentList = dao.selectUserMaintList(userId, "최근 3개월", "전체 차량");
		assertNotNull(recentList);
		for (MaintenanceDTO dto : recentList) {
			assertTrue(dto.getMaintDate().compareTo(todayStr) <= 0);
		}

		// 조회된 이력이 있으면 첫 번째 건의 상세 명세서까지 동적 검증
		if (!recentList.isEmpty()) {
			int reportNo = recentList.get(0).getReportNo();
			assertNotNull(dao.selectMaintenanceDetail(reportNo));
			assertNotNull(dao.selectMaintDetailsByReportNo(reportNo));
		}
	}

}