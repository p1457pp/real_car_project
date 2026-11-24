package car_dao;

import java.sql.Statement;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConn {
	
	private static DBConn db;
	
	private DBConn() {
		
	}
	
	public static DBConn getInstance() {
		if(db== null) {
			
			db = new DBConn();
		}
		return db;
	}
	
	public Connection getConn(Properties prop) throws SQLException {
		
		Connection con = null;
		if(prop != null) {
			String url = prop.getProperty("url");
			String id = prop.getProperty("id");
			String pass = prop.getProperty("pass");
			con = DriverManager.getConnection(url,id,pass);
		}
		
		
		return con;
		
		
		
	}
	
	public Properties getProp(String path) {
		Properties prop = null;
		if(path==null || "".equals(path)) {
			path = "c:/dev/config/car_prj.properties";
		}
		File readProp = new File(path);
		
		if(readProp.exists()) {
			
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(readProp));
			prop = new Properties();
			prop.load(br);
			
		}catch(IOException io) {
			io.printStackTrace();
		}
				
		}
		return prop;
			
	}
	
	public void DbClose(ResultSet rs,Statement stmt,Connection con)throws SQLException {
		
		try {
			if(rs!=null) {
				rs.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
			
		}finally {
			if(con != null) {
				con.close();
			}
			
		}
		
	}
	
	

}
