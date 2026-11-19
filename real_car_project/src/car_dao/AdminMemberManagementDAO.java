package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import car_dto.AdminMemberManagementDTO;

public class AdminMemberManagementDAO {

	private static AdminMemberManagementDAO ammDAO;
	
	private AdminMemberManagementDAO() {
		
	}
	
	
	public static AdminMemberManagementDAO getInstance() {
		if(ammDAO == null) {
			ammDAO=new AdminMemberManagementDAO();
		}
		return ammDAO;
	}
	
	public List<AdminMemberManagementDTO> selectAllMember() throws SQLException {
		
		 List<AdminMemberManagementDTO> list = new ArrayList<AdminMemberManagementDTO>();
		
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		DbConn dc=DbConn.getInstance();
		Properties prop=dc.getProp(null);
		
		try {
			con=dc.getConn(prop);
			StringBuilder sql = new StringBuilder();
	        sql.append(" select user_id, user_name, email, phone, user_grade, ")
	           .append("        to_char(join_date, 'yyyy-mm-dd') join_date ")
	           .append(" from users ")
	           .append(" order by join_date desc ");
			
			
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(new AdminMemberManagementDTO(
						rs.getString("user_id"),
						rs.getString("user_name"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getString("user_grade"),
						rs.getString("join_date")));
			}
		}finally {
			dc.dbClose(rs, pstmt, con);
		}
		
		return list;
		
	}
	public int deleteUser(String userId) throws SQLException {
	    int cnt = 0;

	    Connection con = null;
	    PreparedStatement pstmt = null;

	    DbConn dc = DbConn.getInstance();
	    Properties prop = dc.getProp(null);

	    try {
	        con = dc.getConn(prop);

	        String sql = "delete from users where user_id=?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, userId);

	        cnt = pstmt.executeUpdate();
	    } finally {
	        dc.dbClose(null, pstmt, con);
	    }
	    return cnt;
	}
	
	public int updateUser(AdminMemberManagementDTO ammDTO) throws SQLException {
		int cnt=0;
		
		Connection con = null;
	    PreparedStatement pstmt = null;

	    DbConn dc = DbConn.getInstance();
	    Properties prop = dc.getProp(null);

		try {
			con = dc.getConn(prop);
			StringBuilder sql = new StringBuilder();
	        sql.append(" update users ")
	        .append(" set user_name=?, email=?, phone=?, user_grade=?")
	        .append(" where user_id=? ");
	        
	        pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, ammDTO.getName());
			pstmt.setString(2, ammDTO.getEmail());
			pstmt.setString(3, ammDTO.getPhone());
			pstmt.setString(4, ammDTO.getGrade());
			pstmt.setString(5, ammDTO.getUserId());
			
			 cnt = pstmt.executeUpdate();
		}finally {
			dc.dbClose(null, pstmt, con);
		}
	    return cnt;
		
	}
}

