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

    // 3. 마이페이지 내 정보 조회 메서드 (SELECT)
    // 반환 순서: [0]USER_ID, [1]PASSWORD, [2]USER_NAME, [3]EMAIL, [4]PHONE, [5]USER_GRADE
    public String[] getUserInfo(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String[] userInfo = null;

        String sql = "SELECT USER_ID, PASSWORD, USER_NAME, EMAIL, PHONE, USER_GRADE "
                   + "FROM USERS WHERE USER_ID = ?";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                userInfo = new String[] {
                    rs.getString("USER_ID"),
                    rs.getString("PASSWORD"),
                    rs.getString("USER_NAME"),
                    rs.getString("EMAIL"),
                    rs.getString("PHONE"),
                    rs.getString("USER_GRADE")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.close(conn, pstmt, rs);
        }

        return userInfo;
    }

    // 4. 마이페이지 회원 정보 수정 메서드 (UPDATE)
    public boolean updateUser(String id, String pw, String name, String email, String phone) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean isSuccess = false;

        String sql = "UPDATE USERS "
                   + "SET PASSWORD = ?, USER_NAME = ?, EMAIL = ?, PHONE = ? "
                   + "WHERE USER_ID = ?";

        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pw);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, id);

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
}