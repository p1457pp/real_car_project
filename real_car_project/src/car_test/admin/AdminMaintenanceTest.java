package car_test.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import admin.car_service.AdminMaintenanceService;
import admin.car_service.SettlementService;
import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import car_dto.RepairItemDTO;
import car_dto.SettlementDTO;

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
		List<MaintenanceDetailDTO> items = new ArrayList<>();

		MaintenanceDetailDTO d1 = new MaintenanceDetailDTO();
		d1.setItemName("엔진오일");
		d1.setItemPrice(150000);
		d1.setQty(1);
		d1.setLabCost(50000); // 200,000원

		MaintenanceDetailDTO d2 = new MaintenanceDetailDTO();
		d2.setItemName("미션오일");
		d2.setItemPrice(70000);
		d2.setQty(0); // 수량 0 이하 입력 시 기본값 1 적용 검증
		d2.setLabCost(30000); // 100,000원

		MaintenanceDetailDTO d3 = new MaintenanceDetailDTO();
		d3.setItemName("차동기어오일");
		d3.setItemPrice(40000);
		d3.setQty(1);
		d3.setLabCost(90000); // 130,000원

		items.add(d1);
		items.add(d2);
		items.add(d3);

		MaintenanceDTO mDTO = new MaintenanceDTO();
		mDTO.setMaintenanceInfo(items);

		int total = maintService.calcTotalPrice(mDTO);
		assertEquals(430000, total, "항목별 부품비와 공임비 합계가 430,000원이어야 함");
	}

	@Test
	@DisplayName("2. 차량번호로 고객명 및 차종 조회 (정상/미등록 케이스)")
	void testSearchCarOwnerInfo() {
		String validInfo = maintService.searchCarOwnerInfo("24라4567");
		assertNotNull(validInfo, "등록된 차량번호(24라4567)는 고객/차종 정보가 조회되어야 함");
		assertTrue(validInfo.contains("서예린"), "차주명(서예린)이 포함되어야 함");

		String invalidInfo = maintService.searchCarOwnerInfo("99가9999");
		assertNull(invalidInfo, "존재하지 않는 차량번호는 null을 반환해야 함");
	}

	@Test
	@DisplayName("3. 표준 정비 항목 목록 조회 (REPAIR_ITEM)")
	void testSearchAllRepairItems() {
		List<RepairItemDTO> items = maintService.searchAllRepairItems();
		assertNotNull(items);
		assertFalse(items.isEmpty(), "표준 정비 항목이 1건 이상 등록되어 있어야 함");
	}

	@Test
	@DisplayName("4. 관리자 전체 정비이력 검색 및 상세 명세서 조회")
	void testSearchAllMaintenanceAndDetail() {
		// 전체 목록 조회
		List<MaintenanceDTO> allList = maintService.searchAllMaintenance("", "", "");
		assertNotNull(allList);
		assertFalse(allList.isEmpty(), "전체 정비이력 목록이 1건 이상 조회되어야 함");

		// 특정 번호(21번) 리포트 및 상세 품목 조회
		int targetReportNo = 21;
		MaintenanceDTO detail = maintService.searchMaintenanceDetail(targetReportNo);
		assertNotNull(detail, "21번 정비 리포트가 존재해야 함");
		assertEquals("24라4567", detail.getCarNum());

		List<MaintenanceDetailDTO> detailItems = maintService.searchMaintenanceItems(targetReportNo);
		assertNotNull(detailItems);
		assertFalse(detailItems.isEmpty(), "21번 리포트의 상세 정비 품목이 1건 이상 있어야 함");
	}

	@Test
	@DisplayName("5. 정비 수정 및 진행상태 전환(대기 0 <-> 정비완료 1) 검증")
	void testModifyAndCompleteMaintenance() {
		int targetReportNo = 21;

		// 시연 데이터 보존을 위해 기존 상태 백업
		MaintenanceDTO origin = maintService.searchMaintenanceDetail(targetReportNo);
		List<MaintenanceDetailDTO> originItems = maintService.searchMaintenanceItems(targetReportNo);
		assertNotNull(origin, "테스트 대상 21번 정비 내역이 DB에 있어야 함");

		String formattedDate = origin.getMaintDate().replace(".", "-");

		try {
			// 1) 대기(0) 상태로 수정 저장 테스트
			MaintenanceDTO waitDto = new MaintenanceDTO();
			waitDto.setReportNo(targetReportNo);
			waitDto.setCarNum(origin.getCarNum());
			waitDto.setMaintDate(formattedDate);
			waitDto.setTotalPrice(origin.getTotalPrice());
			waitDto.setStatus(0);
			waitDto.setMechanicMemo(origin.getMechanicMemo());
			waitDto.setMaintenanceInfo(originItems);

			int waitResult = maintService.modifyMaintenance(waitDto);
			assertEquals(1, waitResult, "대기(0) 상태 수정이 성공(1)해야 함");
			assertEquals(0, maintService.searchMaintenanceDetail(targetReportNo).getStatus());

			// 2) 정비완료(1) 전환 메서드 테스트
			int completeResult = maintService.completeMaintenance(waitDto);
			assertEquals(1, completeResult, "정비완료(1) 전환이 성공(1)해야 함");
			assertEquals(1, maintService.searchMaintenanceDetail(targetReportNo).getStatus());

			// 3) 미등록 차량번호로 신규 등록 시 실패(0) 반환 검증
			MaintenanceDTO invalidInsert = new MaintenanceDTO();
			invalidInsert.setReportNo(0);
			invalidInsert.setCarNum("99가9999");
			invalidInsert.setMaintDate("2026-11-26");
			invalidInsert.setCenterName("강남점");
			invalidInsert.setTotalPrice(10000);
			invalidInsert.setStatus(0);
			invalidInsert.setMaintenanceInfo(originItems);

			assertEquals(0, maintService.modifyMaintenance(invalidInsert), "미등록 차량은 등록 실패(0)해야 함");
		} finally {
			// 테스트 종료 후 원래 데이터로 원상복구
			origin.setMaintDate(formattedDate);
			origin.setMaintenanceInfo(originItems);
			maintService.modifyMaintenance(origin);
		}
	}

	@Test
	@DisplayName("6. 월말 정산 매출 집계 조회 (2026-11)")
	void testMonthlyRevenue() {
		SettlementDTO revenueFromMaint = maintService.searchAllRevenue("2026-11");
		assertNotNull(revenueFromMaint, "MaintenanceDAO 기준 월별 정산 결과가 null이 아니어야 함");

		SettlementDTO revenueFromSettlement = settlementService.searchMonthTotal("2026-11");
		assertNotNull(revenueFromSettlement, "SettlementService 기준 월별 정산 결과가 null이 아니어야 함");
	}

}