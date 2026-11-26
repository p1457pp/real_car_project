package car_test.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import car_dto.MaintenanceDetailDTO;

class UserMaintenanceTest {

	private MaintenanceDAO dao;

	@BeforeEach
	void setUp() {
		dao = MaintenanceDAO.getInstance();
	}

	@Test
	@DisplayName("1. 로그인 회원(user019) 보유 차량 목록 조회")
	void testSelectUserCars() {
		List<String> cars = dao.selectUserCars("user019");
		assertNotNull(cars);
		assertFalse(cars.isEmpty(), "user019 회원의 보유 차량이 1대 이상 조회되어야 함");
	}

	@Test
	@DisplayName("2. 사용자 차량 정비이력 기간 필터 조회 및 미래 일자 제외 검증")
	void testSelectUserMaintList() {
		String userId = "user019";
		String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

		// 전체 기간 조회
		List<MaintenanceDTO> allList = dao.selectUserMaintList(userId, "전체 기간", "전체 차량");
		assertNotNull(allList);
		assertFalse(allList.isEmpty(), "user019의 전체 정비이력이 1건 이상 조회되어야 함");

		// 최근 3개월 조회 시 오늘 이후 미래 일자가 제외되는지 검증
		List<MaintenanceDTO> recentList = dao.selectUserMaintList(userId, "최근 3개월", "전체 차량");
		assertNotNull(recentList);
		for (MaintenanceDTO dto : recentList) {
			assertTrue(dto.getMaintDate().compareTo(todayStr) <= 0,
					"기간 필터 조회 시 오늘 이후 날짜(" + dto.getMaintDate() + ")는 제외되어야 함");
		}
	}

	@Test
	@DisplayName("3. 사용자 정비 상세 명세서(부품단가·공임비·코멘트) 조회")
	void testSelectUserMaintDetailReceipt() {
		int reportNo = 21;

		MaintenanceDTO report = dao.selectMaintenanceDetail(reportNo);
		assertNotNull(report, "상세 명세서 기본 정보가 조회되어야 함");
		assertNotNull(report.getCenterName(), "정비지점명이 존재해야 함");

		List<MaintenanceDetailDTO> items = dao.selectMaintDetailsByReportNo(reportNo);
		assertNotNull(items);
		assertFalse(items.isEmpty(), "상세 명세서의 부품/공임 항목이 1건 이상 조회되어야 함");

		for (MaintenanceDetailDTO item : items) {
			assertTrue(item.getTotalPrice() >= 0, "항목별 합계 금액은 0원 이상이어야 함");
		}
	}

}