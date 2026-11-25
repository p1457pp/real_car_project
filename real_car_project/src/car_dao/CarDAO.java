package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import car_dto.CarDTO;
import car_dto.ManufacturerDTO;
import car_util.DBConnection;

/**
 * 자동차 정보 DAO
 *
 * 삭제 여부 컬럼 : is_deleted (NUMBER(1))
 *   1 = 사용 중(정상), 0 = 삭제됨(소프트 삭제)
 */
public class CarDAO {

	private static CarDAO cDAO;

	private CarDAO() {
	}

	public static CarDAO getInstance() {
		if (cDAO == null) {
			cDAO = new CarDAO();
		}
		return cDAO;
	}

	/**
	 * 새로운 자동차 정보 추가
	 *
	 * @param cDTO
	 * @throws SQLException
	 */
	public void insertCar(CarDTO cDTO) throws SQLException {
		Connection con = null; // DB연결관리, Transaction처리, 쿼리문 생성객체얻는일
		PreparedStatement pstmt = null; // 쿼리문 전처리하여 생성, 쿼리 실행
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결

		try {
			// 1.드라이버로딩
			// 2.커넥션얻기
			con = dbCon.getConn(dbCon.getProp(null));
			// 3.쿼리문생성객체얻기
			StringBuilder insertCar = new StringBuilder();
			insertCar
			.append("	insert into car_info (vehicle_id, car_num, car_model, car_year, fuel_type,	")
			.append("					mileage, car_photo, car_scrapped, add_date, manufacturer_id, user_id, is_deleted)	")
			.append("	values(seq_car_info.nextval, ?, ?, ?, ?, ?, ?, 'N', sysdate, ?, ?, 1)	");

			pstmt = con.prepareStatement(insertCar.toString());
			// 4.바인드변수 값 설정
			pstmt.setString(1, cDTO.getCarNum());
			pstmt.setString(2, cDTO.getCarModel());
			pstmt.setInt(3, cDTO.getCarYear());
			pstmt.setString(4, cDTO.getFuelType());
			pstmt.setInt(5, cDTO.getMileage());
			if (cDTO.getCarPhoto() != null) {
				pstmt.setBytes(6, cDTO.getCarPhoto());
			} else {
				pstmt.setNull(6, java.sql.Types.BLOB);
			}
			pstmt.setInt(7, cDTO.getManufacturerId());
			pstmt.setString(8, cDTO.getUserId());
			// 5.쿼리문 수행 후 결과 얻기
			pstmt.executeUpdate();
		} finally {
			// 6.연결 끊기
			dbCon.dbClose(null, pstmt, con);
		}
	}

	/**
	 * 기존 자동차 정보 수정
	 * 사진(car_photo)은 새 사진이 있을 때만 변경하고, 없으면 기존 사진을 유지한다.
	 *
	 * @param cDTO
	 * @return cnt
	 * @throws SQLException
	 */
	public int updateCar(CarDTO cDTO) throws SQLException {
		int cnt = 0;
		Connection con = null; // DB연결관리, Transaction처리, 쿼리문 생성객체얻는일
		PreparedStatement pstmt = null; // 쿼리문 전처리하여 생성, 쿼리 실행
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결

		boolean hasPhoto = cDTO.getCarPhoto() != null;

		try {
			// 1.드라이버로딩
			// 2.커넥션얻기
			con = dbCon.getConn(dbCon.getProp(null));
			// 3.쿼리문생성객체얻기
			StringBuilder updateCar = new StringBuilder();
			updateCar
			.append("	update	car_info	")
			.append("	set		car_num=?, car_model=?, car_year=?, fuel_type=?,	")
			.append("			mileage=?, manufacturer_id=?	");
			if (hasPhoto) {
				updateCar.append("	, car_photo=?	");
			}
			updateCar
			.append("	where	vehicle_id=?	")
			.append("	and		is_deleted = 1	");

			pstmt = con.prepareStatement(updateCar.toString());
			// 4.바인드변수 값 설정
			int idx = 1;
			pstmt.setString(idx++, cDTO.getCarNum());
			pstmt.setString(idx++, cDTO.getCarModel());
			pstmt.setInt(idx++, cDTO.getCarYear());
			pstmt.setString(idx++, cDTO.getFuelType());
			pstmt.setInt(idx++, cDTO.getMileage());
			pstmt.setInt(idx++, cDTO.getManufacturerId());
			if (hasPhoto) {
				pstmt.setBytes(idx++, cDTO.getCarPhoto());
			}
			pstmt.setInt(idx++, cDTO.getVehicleId());
			// 5.쿼리문 수행 후 결과 얻기
			cnt = pstmt.executeUpdate();
		} finally {
			// 6.연결 끊기
			dbCon.dbClose(null, pstmt, con);
		}
		return cnt; // 성공, 실패 변수 반환
	}

	/**
	 * 자동차 소프트 삭제 (is_deleted = 0)
	 * 리턴 값이 0이면 실패 1 이면 성공
	 *
	 * @param vehicleId
	 * @return cnt
	 * @throws SQLException
	 */
	public int deleteCar(int vehicleId) throws SQLException {
		int cnt = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결
		try {
			con = dbCon.getConn(dbCon.getProp(null));
			pstmt = con.prepareStatement(
					"update car_info set is_deleted = 0 where vehicle_id = ? and is_deleted = 1");
			pstmt.setInt(1, vehicleId);
			cnt = pstmt.executeUpdate();
		} finally {
			dbCon.dbClose(null, pstmt, con);
		}
		return cnt; // 성공, 실패 변수 반환
	}

	/**
	 * 모든 자동차 정보 가져오기 (삭제되지 않은 차량만)
	 *
	 * @return listCar
	 * @throws SQLException
	 */
	public List<CarDTO> selectAllCar() throws SQLException {
		List<CarDTO> listCar = new ArrayList<CarDTO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결

		try {
			//1.드라이버 로딩
			//2.커넥션얻기
			con = dbCon.getConn(dbCon.getProp(null));
			//3.쿼리문 생성객체 얻기
			StringBuilder selectCar = new StringBuilder();
			selectCar
			.append("	select	c.vehicle_id, c.car_num, c.car_model, c.car_year, c.fuel_type,	")
			.append("			c.mileage, c.car_scrapped, c.add_date, c.manufacturer_id, c.user_id,	")
			.append("			c.is_deleted, m.manufacturer	")
			.append("	from	car_info c, car_manufacturer m		")
			.append("	where	c.manufacturer_id = m.manufacturer_id		")
			.append("	and		c.is_deleted = 1	")
			.append("	order by c.vehicle_id		");

			pstmt = con.prepareStatement(selectCar.toString());
			//4.바인드 변수 설정
			//5.실행 후 결과 얻기
			rs = pstmt.executeQuery();

			CarDTO cDTO = null;
			while (rs.next()) {
				cDTO = new CarDTO();
				cDTO.setVehicleId(rs.getInt("vehicle_id"));
				cDTO.setCarNum(rs.getString("car_num"));
				cDTO.setCarModel(rs.getString("car_model"));
				cDTO.setCarYear(rs.getInt("car_year"));
				cDTO.setFuelType(rs.getString("fuel_type"));
				cDTO.setMileage(rs.getInt("mileage"));
				cDTO.setCarScrapped(rs.getString("car_scrapped"));
				cDTO.setAddDate(rs.getDate("add_date"));
				cDTO.setManufacturerId(rs.getInt("manufacturer_id"));
				cDTO.setManufacturerName(rs.getString("manufacturer"));
				cDTO.setUserId(rs.getString("user_id"));
				cDTO.setIsDeleted(rs.getInt("is_deleted"));
				listCar.add(cDTO);
			}

		} finally {
			//6.연결 끊기
			dbCon.dbClose(rs, pstmt, con);
		}
		return listCar; // 전체 유저의 차량정보 반환
	}//selectAllCar

	/**
	 * 특정 유저 자동차 정보 가져오기 (삭제되지 않은 차량만)
	 *
	 * @param userId
	 * @return listCar
	 * @throws SQLException
	 */
	public List<CarDTO> selectCarByUserId(String userId) throws SQLException {
		List<CarDTO> listCar = new ArrayList<CarDTO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결

		try {
			//1.드라이버 로딩
			//2.커넥션얻기
			con = dbCon.getConn(dbCon.getProp(null));
			//3.쿼리문 생성객체 얻기
			StringBuilder selectCar = new StringBuilder();
			selectCar
			.append("	select	vehicle_id, car_num, car_model, car_year, fuel_type,	")
			.append("			mileage, car_scrapped, car_photo, add_date, manufacturer_id, user_id, is_deleted	")
			.append("	from	car_info		")
			.append("	where	user_id = ?		")
			.append("	and		is_deleted = 1	")
			.append("	order by vehicle_id		");

			pstmt = con.prepareStatement(selectCar.toString());
			//4.바인드 변수 설정
			pstmt.setString(1, userId);
			//5.실행 후 결과 얻기
			rs = pstmt.executeQuery();

			CarDTO cDTO = null;
			while (rs.next()) {
				cDTO = new CarDTO();
				cDTO.setVehicleId(rs.getInt("vehicle_id"));
				cDTO.setCarPhoto(rs.getBytes("car_photo"));
				cDTO.setCarNum(rs.getString("car_num"));
				cDTO.setCarModel(rs.getString("car_model"));
				cDTO.setCarYear(rs.getInt("car_year"));
				cDTO.setFuelType(rs.getString("fuel_type"));
				cDTO.setMileage(rs.getInt("mileage"));
				cDTO.setCarScrapped(rs.getString("car_scrapped"));
				cDTO.setAddDate(rs.getDate("add_date"));
				cDTO.setManufacturerId(rs.getInt("manufacturer_id"));
				cDTO.setUserId(rs.getString("user_id"));
				cDTO.setIsDeleted(rs.getInt("is_deleted"));

				listCar.add(cDTO);
			}
		} finally {
			//6.연결 끊기
			dbCon.dbClose(rs, pstmt, con);
		}
		return listCar; // 해당 유저 모든 차량 list 반환
	}//selectCarByUserId

	/**
	 * 특정 자동차 정보 가져오기 (삭제되지 않은 차량만)
	 *
	 * @param vehicleId
	 * @return cDTO (없으면 null)
	 * @throws SQLException
	 */
	public CarDTO selectByVehicleId(int vehicleId) throws SQLException {
		CarDTO cDTO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결

		try {
			//1.드라이버 로딩
			//2.커넥션얻기
			con = dbCon.getConn(dbCon.getProp(null));
			//3.쿼리문 생성객체 얻기
			StringBuilder selectCar = new StringBuilder();
			selectCar
			.append("	select	vehicle_id, car_num, car_model, car_year, fuel_type,	")
			.append("			mileage, car_scrapped, car_photo, add_date, manufacturer_id, user_id, is_deleted	")
			.append("	from	car_info		")
			.append("	where	vehicle_id = ?	")
			.append("	and		is_deleted = 1	");

			pstmt = con.prepareStatement(selectCar.toString());
			//4.바인드 변수 설정
			pstmt.setInt(1, vehicleId);
			//5.실행 후 결과 얻기
			rs = pstmt.executeQuery();

			if (rs.next()) {
				cDTO = new CarDTO();
				cDTO.setVehicleId(rs.getInt("vehicle_id"));
				cDTO.setCarNum(rs.getString("car_num"));
				cDTO.setCarModel(rs.getString("car_model"));
				cDTO.setCarYear(rs.getInt("car_year"));
				cDTO.setFuelType(rs.getString("fuel_type"));
				cDTO.setMileage(rs.getInt("mileage"));
				cDTO.setCarScrapped(rs.getString("car_scrapped"));
				cDTO.setCarPhoto(rs.getBytes("car_photo"));
				cDTO.setAddDate(rs.getDate("add_date"));
				cDTO.setManufacturerId(rs.getInt("manufacturer_id"));
				cDTO.setUserId(rs.getString("user_id"));
				cDTO.setIsDeleted(rs.getInt("is_deleted"));
			}

		} finally {
			//6.연결 끊기
			dbCon.dbClose(rs, pstmt, con);
		}
		return cDTO; // 해당 vehicleID 차량 DTO 반환
	}//selectByVehicleId

	/**
	 * 모든 제조사 정보 가져오기
	 *
	 * @return listManufacturer
	 * @throws SQLException
	 */
	public List<ManufacturerDTO> selectAllManufacturers() throws SQLException {
		List<ManufacturerDTO> listManufacturer = new ArrayList<ManufacturerDTO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DBConnection dbCon = DBConnection.getInstance(); // DB 연결

		try {
			//1.드라이버 로딩
			//2.커넥션얻기
			con = dbCon.getConn(dbCon.getProp(null));
			//3.쿼리문 생성객체 얻기
			StringBuilder selectManufacturer = new StringBuilder();
			selectManufacturer
			.append("	select	manufacturer_id, manufacturer	")
			.append("	from	car_manufacturer	")
			.append("	order by manufacturer_id	");

			pstmt = con.prepareStatement(selectManufacturer.toString());
			//4.바인드 변수 설정 (? 없음)
			//5.실행 후 결과 얻기
			rs = pstmt.executeQuery();

			ManufacturerDTO mDTO = null;
			while (rs.next()) {
				mDTO = new ManufacturerDTO();
				mDTO.setManufacturerId(rs.getInt("manufacturer_id"));
				mDTO.setManufacturer(rs.getString("manufacturer"));

				listManufacturer.add(mDTO);
			}

		} finally {
			//6.연결 끊기
			dbCon.dbClose(rs, pstmt, con);
		}
		return listManufacturer; // 제조사 정보 리스트 반환
	}//selectAllManufacturers
}