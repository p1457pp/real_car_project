package car_dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *DB연결객체를 반환하는 일을 하는 클래스 
 */
public class DbConn {
	private static DbConn db;
	
	public DbConn() {
		
	}
	
	public static DbConn getInstance() {
		if(db==null) {
			db=new DbConn();
			}
		return db;
	}
	
	/**
	 * 입력된 경로에 존재하는 properites 파일을 Properites 객체로 생성하여
	 * 반환하는 일
	 * 파일의 경로가 null이나 ""라면 기본 파일의 경로로
	 * c:/dev/config/database.properties가 설정됨
	 * @param path properites 파일의 경로
	 * @return 객체
	 */
	public Properties getProp(String path) {
		Properties prop=null;
		if(path == null || "".equals(path)) {
			path="c:/dev/config/database_project.properties";
		}
		
		File readProp=new File(path);
		if(readProp.exists()) {
			BufferedReader br=null;
			try {
				br=new BufferedReader(new FileReader(readProp));
				prop=new Properties();
				prop.load(br);
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}//end catch
		}//end if
		
		
		return prop;
	}
	/**
	 * DB연동 작업 중 1.드라이버로딩, 2.로딩된 드라이버를 사용하여 Connection을
	 * 얻는일
	 * @return 연결된 connection
	 * @throws SQLException 
	 */
	public Connection getConn(Properties prop) throws SQLException {
		Connection con=null;
		if(prop != null) {
			String url=prop.getProperty("url");
			String id=prop.getProperty("id");
			String pass=prop.getProperty("pass");
			
			con=DriverManager.getConnection(url,id,pass);
		}
		
		return con;
	}
	
	public void dbClose(ResultSet rs,Statement stmt, Connection con) throws SQLException {
		
	try {
		if(rs != null) {rs.close();}
		if(stmt != null) {stmt.close();}
	}finally {
		if(con != null) {con.close();}
	}
}
	
	public static void main(String[] args) {
		DbConn db=DbConn.getInstance();
		Properties prop=db.getProp(null);
		try {
			Connection con=db.getConn(prop);
			System.out.println(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
