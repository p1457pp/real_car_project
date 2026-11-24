package car_util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * DB연결객체를 반환하는 일을 하는 클래스.
 */
public class DBConnection {

    private static DBConnection db;

    private DBConnection() {
    }//DBConnection

    // 멀티스레드 환경을 고려하여 synchronized 추가
    public static synchronized DBConnection getInstance() {
        if (db == null) {
            db = new DBConnection();
        }//end if
        return db;
    }//getInstance

    /**
     * 입력된 경로에 존재하는 properties 파일을 Properties 객체로 생성하여 반환하는 일<br>
     * @param path properties 파일의 경로
     * @return 객체
     */
    public Properties getProp(String path) {
        Properties prop = null;
        if (path == null || "".equals(path)) {
            path = "c:/dev/config/database.properties";
        }//end if

        File readProp = new File(path);
        if (readProp.exists()) {
            // Try-with-resources를 사용하여 예외 발생 여부와 상관없이 BufferedReader 자동 close
            try (BufferedReader br = Files.newBufferedReader(readProp.toPath())) {
                prop = new Properties();
                prop.load(br);
            } catch (IOException e) {
                e.printStackTrace();
            }//end catch
        }//end if

        return prop;
    }//getProp

    /**
     * DB연동 작업 중 1.드라이버로딩, 2.로딩된 드라이버를 사용하여 Connection을 얻는 일
     * @param prop 설정값이 담긴 Properties 객체
     * @return 연결된 connection
     * @throws SQLException
     */
    public Connection getConn(Properties prop) throws SQLException {
        // properties 파일을 찾지 못한 경우 : null을 반환하면 DAO에서 NullPointerException이 나므로 SQLException으로 알린다
        if (prop == null) {
            throw new SQLException("DB 설정 파일(database.properties)을 찾을 수 없습니다. 경로를 확인하세요.");
        }//end if

        Connection con = null;
        if (prop != null) {
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String id = prop.getProperty("id");
            String pass = prop.getProperty("pass");

            try {
                // 1. 드라이버 로딩 (properties에 driver 설정이 있는 경우)
                if (driver != null && !"".equals(driver)) {
                    Class.forName(driver);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // 2. Connection 얻기
            con = DriverManager.getConnection(url, id, pass);
        }//end if

        return con;
    }// getConn

    // 정적 호출 호환용 (기존 MaintenanceDAO 등에서 호출하던 형태 지원)
    public static Connection getConnection() throws SQLException {
        DBConnection instance = getInstance();
        return instance.getConn(instance.getProp(null));
    }

    /**
     * DB작업에 사용된 객체들을 안전하게 종료하는 일
     * @param rs
     * @param stmt
     * @param con
     * @throws SQLException
     */
    public void dbClose(ResultSet rs, Statement stmt, Connection con)
            throws SQLException {

        try {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } finally {
            try {
                if (stmt != null) {
                    try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
                }
            } finally {
                if (con != null) {
                    con.close();
                }
            }
        }
    }//dbClose

    // 기존 close(rs, stmt, con) 호환용
    public static void close(ResultSet rs, Statement stmt, Connection con) {
        try {
            getInstance().dbClose(rs, stmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}//class
