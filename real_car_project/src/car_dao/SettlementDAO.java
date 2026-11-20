package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import car_dto.SettlementDTO;

public class SettlementDAO {

	private static SettlementDAO sDAO;
	
	public SettlementDAO() {
		
	}
	
	public static SettlementDAO getInstance() {
		if(sDAO == null) {
			sDAO=new SettlementDAO();
		}
		return sDAO;
	}
	
	public SettlementDTO selectMonthTotal(String yearMonth) throws SQLException {
		SettlementDTO sDTO=new SettlementDTO(0, 0);
		
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		DbConn dc=DbConn.getInstance();
		Properties prop=dc.getProp(null);
		
		try {
			
			con=dc.getConn(prop);
			StringBuilder sql=new StringBuilder();
			sql
			.append(" select count(*) cnt, nvl(sum(total_price), 0) total ")
			.append(" from maint_reports ")
			.append(" where to_char(maint_date, 'yyyy-mm') = ?");
			
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, yearMonth);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				sDTO = new SettlementDTO(rs.getInt("cnt"), rs.getLong("total"));
			}
		}finally {
			dc.dbClose(rs, pstmt, con);
		}
		
		return sDTO;
		
	}
	
}
