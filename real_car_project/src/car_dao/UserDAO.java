package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // 1. 회원가입 메서드 (INSERT)
    public boolean registerUser(String id, String pw, String name, String email, String phone) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean isSuccess = false;

        String sql = "INSERT INTO USERS (USER_ID, PASSWORD, USER_NAME, EMAIL, PHONE, USER_GRADE, JOIN_DATE) "
                   + "VALUES (?, ?, ?, ?, ?, '일반', SYSDATE)";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            pstmt.setString(3, name);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.close(conn, pstmt);
        }

        return isSuccess;
    }

    // 2. 로그인 체크 메서드 (SELECT)
    public boolean loginUser(String id, String pw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isMatch = false;

        String sql = "SELECT * FROM USERS WHERE USER_ID = ? AND PASSWORD = ?";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                isMatch = true; // 아이디와 비밀번호가 일치하는 행이 존재함
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.close(conn, pstmt, rs);
        }

        return isMatch;
    }
}