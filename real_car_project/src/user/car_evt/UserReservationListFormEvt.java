package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import car_dto.ReservationDTO;
import user.UserMainTab;
import user.car_service.UserReservationService;
import user.car_view.UserReservationForm;
import user.car_view.UserReservationListForm;

public class UserReservationListFormEvt extends MouseAdapter implements ActionListener {

	private UserReservationListForm urlf;
	private UserMainTab umt;
	private JTabbedPane jtpUser;
	private String userId;
	private UserReservationService urs;
	private int vehicleId;

	public UserReservationListFormEvt(UserReservationListForm urlf, UserMainTab umt, JTabbedPane jtpUser,
			String userId) {
		this.urlf = urlf;
		this.umt = umt;
		this.jtpUser = jtpUser;
		this.userId = userId;
		
		this.urs = new UserReservationService();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == urlf.getJbtnAddReservation()) {
			moveReservation();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == urlf.getJtReservation()) {
			changeStatus(e);
		}
	}

	//예약확인에서 예약정보하고 버튼 밑 승인 여부 및 취소 버튼도 여기서 함
	public void searchReservationsByUserId(String userId) {
		
		//테이블모델 가져오기
		DefaultTableModel dtmf = urlf.getDtmReservation();
		//처음에는 테이블 청소
		dtmf.setRowCount(0);
		List<ReservationDTO> crfDTO = null;
		try {
			crfDTO = urs.searchReservationsByUserId(userId);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(urlf, "조회실패");
			
			e.printStackTrace();
			return;
		}
		
		//에약번호,차량번호,차량종류,정비항목,정비허가,예약취소
		String[] row = new String[6];
		for(ReservationDTO info:crfDTO) {
			row[0] = info.getReservationCode();
			row[1] = info.getCarNum();
			row[2] = info.getCarModel();
			row[3] = info.getRepairItem();
			row[4] = info.getReserveState();
			row[5] = "예약취소";
			dtmf.addRow(row);
			
		}
	}

	/** 예약신청 화면으로 이동 (탭 2번 자리에 교체) */
	public void moveReservation() {
		if (jtpUser != null && jtpUser.getTabCount() > 2) {
			jtpUser.setComponentAt(2, new UserReservationForm(umt, jtpUser, userId));
			jtpUser.revalidate();
			jtpUser.repaint();
		}
	}

	public void changeStatus(MouseEvent e) {
		
		Point point =e.getPoint();
		
		JTable jtb = urlf.getJtReservation();
		
		//마우스 위치를 행과 열로 변환
		int row = jtb.rowAtPoint(point);
		int col = jtb.columnAtPoint(point);
		String rsCode = null;
		if(row >=0 && col == 5) {
			
			String reservStatus = jtb.getValueAt(row,4).toString();
			if("예약확정".equals(reservStatus)) {
				JOptionPane.showMessageDialog(jtb, "예약취소 불가 입니다");
				return;
			}
			
			 rsCode = jtb.getValueAt(row, 0).toString();
			 
			 int status = JOptionPane.showConfirmDialog(jtb, "정말로 예약을 삭제하실건가요?\n예약코드:"+ rsCode);
			 boolean resultDel = false;
			 
			 if(status !=  JOptionPane.YES_OPTION) {
				 return;
			 }
			 
			 //삭제 트랜잭션 (예약부품 삭제 + 예약 소프트삭제)
			 try {
				 resultDel = urs.removeReservation(rsCode);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			 
			 if(resultDel==true) {
				 searchReservationsByUserId(userId);
				 JOptionPane.showMessageDialog(jtb, "삭제 성공!");
			 }else {
				 JOptionPane.showMessageDialog(jtb, "삭제 실패!"); 
			 }
			 
		}	 
			 
			
	}
}
