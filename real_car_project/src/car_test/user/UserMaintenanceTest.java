package car_test.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
	private static final String TEST_USER_ID = "user019";

	@BeforeEach
	void setUp() {
		dao = MaintenanceDAO.getInstance();
	}

	@Test
	@DisplayName("1. 보유 차량 목록 조회 (정상 회원 / 미등록 회원)")
	void testSelectUserCars() {
		assertNotNull(dao.selectUserCars(TEST_USER_ID));
		assertTrue(dao.selectUserCars("non_existent_user_999").isEmpty());
	}

	@Test
	@DisplayName("2. 기간별 필터(전체·1개월·3개월·6개월) SQL 동작 및 미래 일자 제외 검증")
	void testSelectUserMaintListByPeriod() {
		String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		String[] periods = { "전체 기간", "최근 1개월", "최근 3개월", "최근 6개월" };

		for (String period : periods) {
			List<MaintenanceDTO> list = dao.selectUserMaintList(TEST_USER_ID, period, "전체 차량");
			assertNotNull(list, period + " 조회 결과가 null이 아니어야 함");

			if (!"전체 기간".equals(period)) {
				for (MaintenanceDTO dto : list) {
					assertTrue(dto.getMaintDate().compareTo(todayStr) <= 0,
							period + " 조회 시 오늘 이후 날짜는 제외되어야 함");
				}
			}
		}
	}

	@Test
	@DisplayName("3. 특정 차량번호 선택 시 해당 차량 정비 이력만 필터링되는지 검증")
	void testSelectUserMaintListByCarNum() {
		List<String> cars = dao.selectUserCars(TEST_USER_ID);
		if (!cars.isEmpty()) {
			// "24라4567(모델명)" 형태에서 순수 차량번호만 추출
			String targetCarNum = cars.get(0).split("\\(")[0];
			List<MaintenanceDTO> filteredList = dao.selectUserMaintList(TEST_USER_ID, "전체 기간", targetCarNum);

			assertNotNull(filteredList);
			for (MaintenanceDTO dto : filteredList) {
				assertEquals(targetCarNum, dto.getCarNum(), "선택한 차량번호와 일치하는 이력만 조회되어야 함");
			}
		}
	}

	@Test
	@DisplayName("4. 정비 상세 명세서 단건·품목 조회 및 없는 번호 예외 검증")
	void testSelectUserMaintDetailReceipt() {
		// 존재하지 않는 리포트 번호 조회 시 안전하게 null / 빈 리스트 반환
		assertNull(dao.selectMaintenanceDetail(-1));
		assertTrue(dao.selectMaintDetailsByReportNo(-1).isEmpty());

		// 조회된 이력이 있으면 첫 번째 건의 상세 명세서까지 동적 검증
		List<MaintenanceDTO> allList = dao.selectUserMaintList(TEST_USER_ID, "전체 기간", "전체 차량");
		if (!allList.isEmpty()) {
			int reportNo = allList.get(0).getReportNo();
			assertNotNull(dao.selectMaintenanceDetail(reportNo));
			assertNotNull(dao.selectMaintDetailsByReportNo(reportNo));
		}
	}

}