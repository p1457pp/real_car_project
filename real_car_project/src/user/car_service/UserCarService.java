package user.car_service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dao.CarDAO;
import car_dto.CarDTO;
import car_dto.ManufacturerDTO;

public class UserCarService {

	// 번호판에 사용할 수 있는 한글
	private static final String ALLOWED_CHAR = "가나다라마거너더러머버서어저고노도로모보소오조구누두루무부수우주아바사자배허하호";
	public static final int MIN_YEAR = 1900;
	public static final int MAX_YEAR = 2027;

	/**
	 * 차량번호 형식 검사<br>
	 * 숫자 2~3개 + 허용 한글 1개 + 숫자 4개
	 * @param carNum
	 * @return true-올바른 형식, false-잘못된 형식
	 */
	public static boolean isValidCarNum(String carNum) {
		
		// 차량번호가 없을경우
		if (carNum == null) {
			return false;
		} // end if

		// 차량번호 글자수 체크
		int len = carNum.length(); 
		// 시작숫자 2 + 5개면 7글자, 시작숫자 3 + 5개면 8글자가 아니면
		if (len != 7 && len != 8) { 
			return false; // 실패 반환
		} // end if

		int charIdx = len - 5; // 시작숫자뒤 한글 글자 위치 ex) '가 나 다 라 마' 

		for (int i = 0; i < len; i++) {
			char c = carNum.charAt(i);
			
			// 선택된 한글 글자 하나가
			if (i == charIdx) {
				// 허용된 한글이 아니면
				if (ALLOWED_CHAR.indexOf(c) == -1) { 
					return false; // 실패 반환
				} // end if
			} else {
				// 숫자가 아니면
				if (c < '0' || c > '9') { 
					return false; // 실패 반환
				} // end if
			} // end if
		} // end for

		return true; // 다성공하면 성공 반환
	}// isValidCarNum

	/**
	 * 연식 범위 검사
	 * 연식은 1900~2027 사이여야한다
	 * @param year
	 * @return true, false
	 */
	public static boolean isValidYear(int year) {
		// true -1900~2027, false -범위 밖
		return year >= MIN_YEAR && year <= MAX_YEAR;
	}// isValidYear

	/**
	 * 입력값 검사 (등록, 수정에서 같이 사용)
	 * 
	 * @param cDTO
	 * @return true-모두 올바름, false-잘못된 값이 있음
	 */
	private boolean isValidCar(CarDTO cDTO) {
		
		// 차량번호 유효성 검증 통과?
		if (!isValidCarNum(cDTO.getCarNum())) {
			return false;
		} // end if

		// 차종 입력란이 비워있는지?
		if (cDTO.getCarModel() == null || cDTO.getCarModel().trim().isEmpty()) {
			return false; // 실패 반환
		} // end if

		// 연료타입 입력란이 비워있는지?
		if (cDTO.getFuelType() == null || cDTO.getFuelType().trim().isEmpty()) {
			return false; // 실패 반환
		} // end if

		// 제조사ID 입력란이 비워있는지?
		if (cDTO.getManufacturerId() <= 0) {
			return false; // 실패 반환
		} // end if

		// 해당되는 연식년도가 아닌지?
		if (!isValidYear(cDTO.getCarYear())) {
			return false; // 실패 반환
		} // end if

		// 주행거리가 허용하는 범위 안에 있는지?
		if (cDTO.getMileage() < 0 || cDTO.getMileage() > 9999999) {
			return false; // 실패 반환
		} // end if

		return true; // 다성공하면 true를 반환
	}// isValidCar

	/**
	 * 차량 등록
	 * 
	 * @param cDTO
	 * @return true-등록 성공, false-입력값 오류 또는 DB 문제(번호판 중복 등)
	 */
	public boolean addCarInfo(CarDTO cDTO) {
		boolean flag = false; // 기본값 false
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		// 입력값 유효성 검증 탈락 + 해당 차량 정보의 유저 아이디가 비워있을경우 
		if (!isValidCar(cDTO) || cDTO.getUserId() == null || cDTO.getUserId().trim().isEmpty()) {
			return false; // 실패 반환
		} // end if

		try { // 실패가 아닐시
			cDAO.insertCar(cDTO); // DAO의 차량등록 메소드 실행
			flag = true; // true로 변환
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return flag; // flag 반환
	}// addCarInfo

	/**
	 * 차량정보 수정<br>
	 * 반환되는 정수는 0-vehicle_id가 없어서 변경되지 않는 경우,
	 * 1-1건 변경된 경우, 2-DBMS에서 문제가 발생한 경우, 3-입력값 오류
	 * 
	 * @param cDTO
	 * @return 정수-0,1,2,3 중 하나 반환
	 */
	public int modifyCarInfo(CarDTO cDTO) {
		// 0 - 행이 선택되지 않는경우 1 - 변경완료 
		// 2 - 예외가떨어졋을경우 
		// 3 - 정보가 적합하지않는경우
		
		int cnt = 0; // 기본값 0을 설정
		CarDAO cDAO = CarDAO.getInstance();

		// 입력값이 올바르지 않으면 DB에 반영하지 않는다.
		if (cDTO.getVehicleId() <= 0 || !isValidCar(cDTO)) {
			return 3; // 3번반환
		} // end if

		try {
			cnt = cDAO.updateCar(cDTO); // 1 - 변경 성공
		} catch (SQLException e) { // 예외가 떨어진경우
			cnt = 2; // 2번 반환
			e.printStackTrace();
		} // end catch

		return cnt; // 번호 반환
	}// modifyCarInfo

	/**
	 * 차량 삭제 (소프트 삭제: delete_state = 'Y')
	 * 
	 * @param vehicleId
	 * @return true-삭제 성공, false-삭제 실패
	 */
	public boolean removeCarInfo(int vehicleId) {
		boolean flag = false; // 기본값 false
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			flag = cDAO.deleteCar(vehicleId) == 1; // 1건이 변경됬다? true 반환
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return flag; // flag 반환
	}// removeCarInfo

	/**
	 * 특정 유저의 차량 정보 가져오기
	 * 
	 * @param userId
	 * @return 삭제되지 않은 차량 목록
	 */
	public List<CarDTO> searchCarByUserId(String userId) {
		List<CarDTO> list = new ArrayList<CarDTO>(); // CarDTO의 그릇 리스트생성
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			// list의 선택된 유저의 모든 차량정보 리스드 가져와 담기
			list = cDAO.selectCarByUserId(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return list; // 리스트 반환
	}// searchCarByUserId

	/**
	 * 특정 차량 정보 가져오기 (사진 포함, 없으면 null)
	 * @param vehicleId
	 * @return 차량 정보
	 */
	public CarDTO searchCarByVehicleId(int vehicleId) {
		CarDTO cDTO = null; // 초기화
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			// 선택된 행의 차량고유번호의 자동차정보를 DTO로 가져오기
			cDTO = cDAO.selectByVehicleId(vehicleId);
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return cDTO; // DTO 반환
	}// searchCarByVehicleId

	/**
	 * 모든 제조사 정보 가져오기
	 * @return 제조사 목록
	 */
	public List<ManufacturerDTO> searchAllManufacturer() {
		// 제조사DTO의 그릇정보 list로 가져오기
		List<ManufacturerDTO> list = new ArrayList<ManufacturerDTO>();
		CarDAO cDAO = CarDAO.getInstance(); // DAO 연결

		try {
			// DAO에서 가져온 제조사정보를 list로 넘겨주기
			list = cDAO.selectAllManufacturers();
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return list; // 리스트 반환
	}// searchAllManufacturer

}// class