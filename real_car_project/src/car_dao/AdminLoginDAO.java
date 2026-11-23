package car_dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import car_dto.AdminLoginDTO;

public class AdminLoginDAO {
	private static AdminLoginDAO alDAO;

	private AdminLoginDAO() {

	}

	public static AdminLoginDAO getInstance() {
		if(alDAO == null ) {
			alDAO=new AdminLoginDAO();
		}
		return alDAO;
	}

	public String login(AdminLoginDTO alDTO) throws SQLException {

		String id="";

		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		DbConn dc=DbConn.getInstance();
		Properties prop=dc.getProp(null);

		try {
			//1. 드라이버로딩
			//2. 커넥션얻기
			con=dc.getConn(prop);
			//3. 쿼리문 생성객체 얻기
			StringBuilder adminId=new StringBuilder();
			adminId
			.append("	select	admin_id 		")
			.append("	from	admin		")
			.append("	where	admin_id=? and password=?	");

			

			//4. 바인드변수에 값 설정
			pstmt.setString(1, alDTO.getId() );
			pstmt.setString(2, alDTO.getPass() );

			//5. 쿼리문 수행 후 결과 얻기
			rs=pstmt.executeQuery();

			if(rs.next()) {
				id=rs.getString("admin_id");
			}//end if

		}finally {
			//6. 연결끊기
			dc.dbClose(rs, pstmt, con);
		}//end finally
		return id;		
	}//selectLogin

}


