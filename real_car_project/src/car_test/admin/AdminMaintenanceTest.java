package car_test.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import admin.car_service.AdminMaintenanceService;
import admin.car_service.SettlementService;
import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import car_dto.RepairItemDTO;

class AdminMaintenanceTest {

	private AdminMaintenanceService maintService;
	private SettlementService settlementService;

	@BeforeEach
	void setUp() {
		maintService = new AdminMaintenanceService();
		settlementService = new SettlementService();
	}

	@Test
	@DisplayName("1. 총 청구 금액 자동 계산 검증 ((부품단가 x 수량) + 공임비)")
	void testCalcTotalPrice() {
		MaintenanceDetailDTO d1 = new MaintenanceDetailDTO();
		d1.setItemPrice(150000);
		d1.setQty(1);
		d1.setLabCost(50000); // 200,000원

		MaintenanceDetailDTO d2 = new MaintenanceDetailDTO();
		d2.setItemPrice(70000);
		d2.setQty(0); // 수량 0 이하 입력 시 기본값 1 적용 검증
		d2.setLabCost(30000); // 100,000원

		MaintenanceDTO mDTO = new MaintenanceDTO();
		mDTO.setMaintenanceInfo(List.of(d1, d2));

		assertEquals(300000, maintService.calcTotalPrice(mDTO));
	}

	@Test
	@DisplayName("2. 미등록 차량번호 조회 시 null 반환 검증")
	void testSearchInvalidCarOwnerInfo() {
		assertNull(maintService.searchCarOwnerInfo("99가9999"));
	}

	@Test
	@DisplayName("3. 표준 정비 항목 및 전체 정비이력·상세 조회 (읽기 전용)")
	void testSelectMaintenanceAndDetails() {
		List<RepairItemDTO> repairItems = maintService.searchAllRepairItems();
		assertNotNull(repairItems);

		List<MaintenanceDTO> allList = maintService.searchAllMaintenance("", "", "");
		assertNotNull(allList);

		// 고정 번호 없이 조회된 첫 번째 내역이 있으면 차주 및 상세 항목까지 동적 검증
		if (!allList.isEmpty()) {
			MaintenanceDTO first = allList.get(0);
			if (first.getCarNum() != null) {
				assertNotNull(maintService.searchCarOwnerInfo(first.getCarNum()));
			}
			assertNotNull(maintService.searchMaintenanceDetail(first.getReportNo()));
			assertNotNull(maintService.searchMaintenanceItems(first.getReportNo()));
		}
	}

	@Test
	@DisplayName("4. 월말 정산 매출 집계 조회 정상 동작 확인")
	void testMonthlyRevenue() {
		assertNotNull(maintService.searchAllRevenue("2026-11"));
		assertNotNull(settlementService.searchMonthTotal("2026-11"));
	}

}