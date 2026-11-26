package car_test.admin;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import admin.car_service.AdminCarService;
import car_dao.CarDAO;
import car_dto.CarDTO;

/**
 * 관리자 차량 테스트 (AdminCarService, CarDAO)<br><br>
 * 
 * 테스트 항목<br><br>
 * 
 * 모든 차량 검색<br>
 * 차량 한 대 검색<br>
 * 제조사 검색<br>
 * 차량 변경 - 입력값 유효성검증<br>
 * 차량 변경<br>
 * 차량 삭제<br><br>
 * 
 * 데이터가 실제 바뀌는 테스트(변경, 삭제)는 @Disabled 처리했습니다.<br>
 * 
 */
class AdminCarTest {

	@DisplayName("모든 차량 검색")
	@Test
	void testSelectAll() {
		CarDAO cDAO = CarDAO.getInstance();
		// select는 예외가 발생하지 않으면 성공.
		assertDoesNotThrow(() -> cDAO.selectAllCar());

		AdminCarService acs = new AdminCarService();
		assertNotNull(acs.searchAllCar());
	}// testSelectAll

	@DisplayName("차량 한 대 검색")
	@Test
	void testSelectOne() {
		AdminCarService acs = new AdminCarService();
		List<CarDTO> list = acs.searchAllCar();
		assertFalse(list.isEmpty()); // 차량이 1대 이상 있어야 한다

		int vehicleId = list.get(0).getVehicleId(); // 실제 있는 차량ID
		CarDAO cDAO = CarDAO.getInstance();
		assertDoesNotThrow(() -> cDAO.selectByVehicleId(vehicleId));
		assertNotNull(acs.searchCarByVehicleId(vehicleId));
	}// testSelectOne

	@DisplayName("제조사 검색")
	@Test
	void testSelectManufacturer() {
		CarDAO cDAO = CarDAO.getInstance();
		assertDoesNotThrow(() -> cDAO.selectAllManufacturers());

		AdminCarService acs = new AdminCarService();
		assertFalse(acs.searchAllManufacturer().isEmpty());
	}// testSelectManufacturer

	@DisplayName("차량 변경 - 입력값 유효성검증")
	@Test
	void testUpdateInvalid() {
		CarDTO cDTO = new CarDTO();
		cDTO.setVehicleId(1);
		cDTO.setCarNum("1234567"); // 잘못된 차량번호
		cDTO.setCarModel("쏘나타");
		cDTO.setCarYear(2020);
		cDTO.setFuelType("휘발유");
		cDTO.setMileage(10000);
		cDTO.setManufacturerId(1);

		AdminCarService acs = new AdminCarService();
		// 3 - 입력값 오류
		assertEquals(3, acs.modifyCar(cDTO));
	}// testUpdateInvalid

	@Disabled
	@DisplayName("차량 변경")
	@Test
	void testUpdate() {
		CarDTO cDTO = new CarDTO();
		cDTO.setVehicleId(1); // 실제 있는 차량ID
		cDTO.setCarNum("12가3456");
		cDTO.setCarModel("쏘나타");
		cDTO.setCarYear(2020);
		cDTO.setFuelType("휘발유");
		cDTO.setMileage(10000);
		cDTO.setManufacturerId(1);

		AdminCarService acs = new AdminCarService();
		// 1 - 변경 성공
		assertEquals(1, acs.modifyCar(cDTO));
	}// testUpdate

	@Disabled
	@DisplayName("차량 삭제")
	@Test
	void testDelete() {
		AdminCarService acs = new AdminCarService();
		// 실제 있는 차량ID (삭제 후에는 다시 실행하면 실패한다)
		assertTrue(acs.updateCarDeleteState(1));
	}// testDelete

}// AdminCarTest