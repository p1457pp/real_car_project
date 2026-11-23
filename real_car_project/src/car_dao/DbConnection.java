package car_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
    // 오라클 접속 정보 (팀원들의 DB 접속 주소, 아이디, 비밀번호에 맞게 수정하세요)
    private static final String URL = "jdbc:oracle:thin:@192.168.0.201:1521:orcl";
    private static final String USER = "c##carproject"; // 본인 DB 계정명
    private static final String PASS = "112233";      // 본인 DB 비밀번호

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 자원 닫기 유틸 메서드
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }
}