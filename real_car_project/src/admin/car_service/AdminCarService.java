package admin.car_service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dao.CarDAO;
import car_dto.CarDTO;
import car_dto.ManufacturerDTO;
import user.car_service.UserCarService;



public class AdminCarService {

	/**
	 * 모든 회원의 차량 정보 가져오기
	 * @return list
	 */
	public List<CarDTO> searchAllCar() {
		// list의 변수설정을 자동차DTO로 설정
		List<CarDTO> list = new ArrayList<CarDTO>(); 
		CarDAO cDAO = CarDAO.getInstance(); // DAO를 연결한다

		try {
			list = cDAO.selectAllCar(); // DAO에서 DB에서 꺼내온값을 list에 담는다
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return list; // 해당 리스트를 반환한다.
	}// searchAllCar

	/**
	 * 특정 차량 정보 가져오기 (없으면 null)
	 * @param vehicleId
	 * @return cDTO
	 */
	public CarDTO searchCarByVehicleId(int vehicleId) {
		CarDTO cDTO = null; // cDTO을 값을 0으로 설정 
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			// cDAO에서 선택된 차량고유번호의 DB행 값을 cDTO로 가져온다
			cDTO = cDAO.selectByVehicleId(vehicleId); 
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return cDTO; // cDTO를 리턴한다.
	}// searchCarByVehicleId

	/**
	 * 모든 제조사 정보 가져오기
	 */
	public List<ManufacturerDTO> searchAllManufacturer() {
		// list의 변수설정을 제조사DTO로 설정
		List<ManufacturerDTO> list = new ArrayList<ManufacturerDTO>();
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			list = cDAO.selectAllManufacturers(); // list에 모든 제조사정보 넣기
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return list; // 리스트 반환
	}// searchAllManufacturer

	/**
	 * 차량정보 수정<br>
	 * 
	 * @param cDTO
	 * @return 0-vehicle_id가 없는 경우, 1- 해당행의 변경성공, 2-DBMS 예외터짐, 3-입력값 오류 일떄
	 */
	public int modifyCar(CarDTO cDTO) {
		int cnt = 0; // 기본 0번으로 셋팅
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		/*
		 * 차량 정보 유효성 검사
		 * 1. 차량고유번호가 0행 이하인지
		 * 2. 차량번호 형식에 부합하는지
		 * 3. 유호한 연식에 적합한지
		 * 4. 차종이름이 없거나 비었거나
		 * 5. 제조사 선택되지 않았거나
		 * 6. 주행거리가 허용하는 범위를 넘어섯거나
		 */
		if (cDTO.getVehicleId() <= 0 || !UserCarService.isValidCarNum(cDTO.getCarNum())
				|| !UserCarService.isValidYear(cDTO.getCarYear()) || cDTO.getCarModel() == null
				|| cDTO.getCarModel().trim().isEmpty() || cDTO.getManufacturerId() <= 0 || cDTO.getMileage() < 0
				|| cDTO.getMileage() > 9999999) {
			return 3; // 하나라도 false가 나오면 "3 - 실패" 를 리턴
		} // end if

		try {
			cnt = cDAO.updateCar(cDTO); // 3번이 아니면 DAO의 업데이트 쿼리 실행
		} catch (SQLException e) {
			cnt = 2; // 업데이트도중 예외가 떨어지면 2번으로 변환
			e.printStackTrace();
		} // end catch

		return cnt; // 정수값 리턴
	}// modifyCar
	/**
	 * 차량 삭제 (소프트 삭제: delete_state = 'Y')
	 * 
	 * @param vehicleId
	 * @return true-삭제 성공, false-삭제 실패
	 */
	public boolean updateCarDeleteState(int vehicleId) {
		boolean flag = false; // 기본 false로 설정
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			// DAO에서 나온 리턴 변수가 1이면 성공 성공하면 true 0이면 실패 실패하면 false
			flag = cDAO.deleteCar(vehicleId) == 1; 
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return flag; // 성공했는지 실패했는지 값을 리턴
	}// removeCar

}// class
