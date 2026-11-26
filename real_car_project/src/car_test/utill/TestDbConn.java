package car_test.utill;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import car_util.DBConnection;

/**
 * DB 연결 테스트 (car_util.DBConnection)<br><br>
 * 
 * 테스트 항목<br><br>
 * 
 * DB 연결 - 설정 파일 읽기<br>
 * DB 연결<br><br>
 * 
 * c:/dev/config/database.properties 파일과 DB 서버가 있어야 통과한다.
 */
class TestDbConn {

	@DisplayName("DB 연결 - 설정 파일 읽기")
	@Test
	void testProperties() {
		DBConnection dc = DBConnection.getInstance();
		Properties prop = dc.getProp(null);
		assertNotNull(prop, "Properties 객체가 생성되지 않았습니다.");
	}// testProperties

	@DisplayName("DB 연결")
	@Test
	void testConnection() throws SQLException {
		DBConnection dc = DBConnection.getInstance();
		Properties prop = dc.getProp(null);

		Connection con = dc.getConn(prop);
		assertNotNull(con, "Connection을 얻지 못했습니다.");

		dc.dbClose(null, null, con); // 테스트 후 연결 닫기
	}// testConnection

}// class