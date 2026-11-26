package car_test.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import car_dto.RepairItemDTO;
import user.car_service.UserReservationService;

class UserReservationTest {
	@DisplayName("부품 목록 검색")
	@Test
	void test_searchRepairItems() {
		
		UserReservationService urs = new UserReservationService();
		List<RepairItemDTO>  result=urs.searchRepairItems();
		
		assertNotNull(result);
		
	}
	@DisplayName("유저예약 조회")
	@Test
	void test_searchReservationsByUserId() {
		
		UserReservationService urs = new UserReservationService();
		String id="user001";
		
		assertDoesNotThrow(()->urs.searchReservationsByUserId(id));
		
		
		
	}
	@DisplayName("관리자의 예약조회")
	@Test
	void test_searchReservedList() {
		
		UserReservationService urs = new UserReservationService();
		String id="user001";
		
		assertDoesNotThrow(()->urs.searchReservedList(id));
		
		
		
	}
	
}
