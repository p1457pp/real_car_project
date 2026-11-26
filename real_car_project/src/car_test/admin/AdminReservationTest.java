package car_test.admin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import admin.car_service.AdminReservationService;
import car_dto.CarDTO;
import car_dto.ReservationDTO;

class AdminReservationTest {
	@DisplayName("모든 예약 조회")
	@Test
	void test_searchAllReservations() {
		
		AdminReservationService ars = new AdminReservationService();
		List<ReservationDTO> result = null;
		try {
			result = ars.searchAllReservations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(result);
		
		
		
		
	}
	@DisplayName("차량 정보찾기 ")
	@Test
	void test_searchCarInfoByVehicleId() {
		
		AdminReservationService ars = new AdminReservationService();
		
		int v_id = 64;
		CarDTO result = ars.searchCarInfoByVehicleId(v_id);
		
		assertDoesNotThrow(()->ars.searchCarInfoByVehicleId(v_id));
		
		assertNotNull(result);
		
	}
	@DisplayName("예약리스트 조회")
	@Test
	void test_searchReservedList() {
		
		AdminReservationService ars = new AdminReservationService();
		
		String rsCode = "rs003";
		ReservationDTO result=assertDoesNotThrow(()->ars.searchReservationDetail(rsCode));;
		
		
		assertNotNull(result);
		
		
	}
}
