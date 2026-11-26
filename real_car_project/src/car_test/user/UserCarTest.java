package car_test.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import car_dao.CarDAO;
import car_dto.CarDTO;
import user.car_service.UserCarService;

/**
 * 사용자 차량 테스트 (UserCarService, CarDAO)<br><br>
 *
 * 테스트 항목<br><br>
 *
 * 회원 차량 검색<br>
 * 차량 한 대 검색<br>
 * 제조사 검색<br>
 * 차량 등록 - 입력값 유효성검증<br>
 * 차량 변경 - 입력값 유효성검증<br>
 * 차량 등록<br>
 * 차량 변경<br>
 * 차량 삭제<br><br>
 *
 * 데이터가 실제 바뀌는 테스트(등록, 변경, 삭제)는 @Disabled 처리했습니다.<br>
 *
 */
class UserCarTest {

	@DisplayName("회원 차량 검색")
	@Test
	void testSelectByUserId() throws Exception {
		CarDAO cDAO = CarDAO.getInstance();
		List<CarDTO> allList = cDAO.selectAllCar();
		assertFalse(allList.isEmpty()); // 차량이 1대 이상 있어야 한다

		String userId = allList.get(0).getUserId(); // 실제 차량을 가진 회원 아이디
		// select는 예외가 발생하지 않으면 성공.
		assertDoesNotThrow(() -> cDAO.selectCarByUserId(userId));

		UserCarService ucs = new UserCarService();
		assertFalse(ucs.searchCarByUserId(userId).isEmpty());
	}// testSelectByUserId

	@DisplayName("차량 한 대 검색")
	@Test
	void testSelectOne() throws Exception {
		CarDAO cDAO = CarDAO.getInstance();
		List<CarDTO> allList = cDAO.selectAllCar();
		assertFalse(allList.isEmpty()); // 차량이 1대 이상 있어야 한다

		int vehicleId = allList.get(0).getVehicleId(); // 실제 있는 차량ID
		assertDoesNotThrow(() -> cDAO.selectByVehicleId(vehicleId));

		UserCarService ucs = new UserCarService();
		assertNotNull(ucs.searchCarByVehicleId(vehicleId));
	}// testSelectOne

	@DisplayName("제조사 검색")
	@Test
	void testSelectManufacturer() {
		CarDAO cDAO = CarDAO.getInstance();
		assertDoesNotThrow(() -> cDAO.selectAllManufacturers());

		UserCarService ucs = new UserCarService();
		assertFalse(ucs.searchAllManufacturer().isEmpty());
	}// testSelectManufacturer

	@DisplayName("차량 등록 - 입력값 유효성검증")
	@Test
	void testInsertInvalid() {
		CarDTO cDTO = new CarDTO();
		cDTO.setUserId("test");
		cDTO.setCarNum("1234567"); // 잘못된 차량번호
		cDTO.setCarModel("아반떼");
		cDTO.setCarYear(2020);
		cDTO.setFuelType("휘발유");
		cDTO.setMileage(10000);
		cDTO.setManufacturerId(1);

		UserCarService ucs = new UserCarService();
		// false - 입력값 오류 (DB까지 가지 않는다)
		assertFalse(ucs.addCarInfo(cDTO));
	}// testInsertInvalid

	@DisplayName("차량 변경 - 입력값 유효성검증")
	@Test
	void testUpdateInvalid() {
		CarDTO cDTO = new CarDTO();
		cDTO.setVehicleId(1);
		cDTO.setCarNum("1234567"); // 잘못된 차량번호
		cDTO.setCarModel("아반떼");
		cDTO.setCarYear(2020);
		cDTO.setFuelType("휘발유");
		cDTO.setMileage(10000);
		cDTO.setManufacturerId(1);

		UserCarService ucs = new UserCarService();
		// 3 - 입력값 오류
		assertEquals(3, ucs.modifyCarInfo(cDTO));
	}// testUpdateInvalid

	@Disabled
	@DisplayName("차량 등록")
	@Test
	void testInsert() {
		CarDTO cDTO = new CarDTO();
		cDTO.setUserId("test"); // 실제 있는 회원 아이디
		cDTO.setCarNum("12가3456"); // 다른 차량과 겹치지 않는 번호
		cDTO.setCarModel("아반떼");
		cDTO.setCarYear(2020);
		cDTO.setFuelType("휘발유");
		cDTO.setMileage(10000);
		cDTO.setManufacturerId(1);

		UserCarService ucs = new UserCarService();
		// true - 등록 성공
		assertTrue(ucs.addCarInfo(cDTO));
	}// testInsert

	@Disabled
	@DisplayName("차량 변경")
	@Test
	void testUpdate() {
		CarDTO cDTO = new CarDTO();
		cDTO.setVehicleId(1); // 실제 있는 차량ID
		cDTO.setCarNum("12가3456");
		cDTO.setCarModel("아반떼");
		cDTO.setCarYear(2020);
		cDTO.setFuelType("휘발유");
		cDTO.setMileage(10000);
		cDTO.setManufacturerId(1);

		UserCarService ucs = new UserCarService();
		// 1 - 변경 성공
		assertEquals(1, ucs.modifyCarInfo(cDTO));
	}// testUpdate

	@Disabled
	@DisplayName("차량 삭제")
	@Test
	void testDelete() {
		UserCarService ucs = new UserCarService();
		// 실제 있는 차량ID (삭제 후에는 다시 실행하면 실패한다)
		assertTrue(ucs.removeCarInfo(1));
	}// testDelete

}// UserCarTest