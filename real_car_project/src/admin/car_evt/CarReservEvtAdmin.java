package admin.car_evt;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import admin.car_service.ReservCarServiceAdmin;
import admin.car_view.CarReservDialogFormAdmin;
import admin.car_view.CarReservPanelAdmin;
import car_dto.CarReservTableAdminDTO;

public class CarReservEvtAdmin implements ActionListener,MouseListener {
	
	
	private CarReservPanelAdmin crpa;
	
	
	
	

	public CarReservEvtAdmin(CarReservPanelAdmin crpa) {
		
		this.crpa = crpa;
		showReservToAdmin(null);
	}




	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource() ==  crpa.getBtnSearch()) {
			searchReservedUser();
		}
		
	}
	
	public void searchReservedUser() {
		
		if("".equals(crpa.getTfId().getText())) {
			JOptionPane.showMessageDialog(crpa, "아이디를 검색해주세여");
			return;
		}
		String id = crpa.getTfId().getText().trim();
		//아이디를 가져왔으니 아이디로 검색해서 유저 정보를 가져와야한다.
		
		System.out.println("유저 아이디는 " + id );
		
		showReservToAdmin(id);
		
		
		
		
	}
	
	//맨처음 관리자가 들어오면 전체 데이터를 보여줍니다
	//테이블의 예약정보를 가져온다
	public void showReservToAdmin(String id) {
		ReservCarServiceAdmin rcsa = new ReservCarServiceAdmin();
		DefaultTableModel dtm = crpa.getDtm();
		
		//화면 초기화 하기
		dtm.setRowCount(0);
		
		String[] reservRow = new String[7];
		try {
			//조회는 아이디 검색 안하면 다 아이디 있으면 회원을 조회한다
			List<CarReservTableAdminDTO> UserData = rcsa.showReservToAdmin(id);
			
			for(CarReservTableAdminDTO row: UserData) {
				reservRow[0] = row.getReservation_code();
				reservRow[1] = row.getUser_id();
				reservRow[2] = row.getRepair_item();
				reservRow[3] = row.getUser_reserv_date();
				reservRow[4] = "보기";
				reservRow[5] = row.getFinal_date();
				reservRow[6] = row.getReserv_state();
				
				dtm.addRow(reservRow);
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void showDetailRequest(MouseEvent e) {
		
		Point point  = e.getPoint();
		JTable jtb = crpa.getReservationTable();
		
		int row  = jtb.rowAtPoint(point);
		int col = jtb.columnAtPoint(point);
		//누른칸의 해당 예약 아이디를 가져와서 상세내용을 조회 해야한다
		//넘겨 줘야할거는
		String rsCode = null;
		
		if(row>=0&& col==4) {
			rsCode = jtb.getValueAt(row, 0).toString();
			CarReservDialogFormAdmin dialog = new CarReservDialogFormAdmin(rsCode);
			// 관리자 패널 중앙에 배치
			dialog.setModal(true);
			dialog.setLocationRelativeTo(crpa);
		
			dialog.setVisible(true);
			
			//화면 다시 갱신
			String id = crpa.getId()!=null?crpa.getId().trim():null;
			
			showReservToAdmin(id);
			
		}
		
		
		
		
		
			
		
		
	}




	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == crpa.getReservationTable()) {
			showDetailRequest(e);
		}
	}




	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
