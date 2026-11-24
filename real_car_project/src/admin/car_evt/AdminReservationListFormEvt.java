package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import admin.car_service.AdminReservationService;
import admin.car_view.AdminReservationDetailForm;
import admin.car_view.AdminReservationListForm;
import car_dto.ReservationDTO;

public class AdminReservationListFormEvt extends MouseAdapter implements ActionListener {

	private AdminReservationListForm arlf;
	private AdminReservationService ars;

	public AdminReservationListFormEvt(AdminReservationListForm arlf) {
		this.arlf = arlf;
		this.ars = new AdminReservationService();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == arlf.getJbtnSearch() || ae.getSource() == arlf.getJtfId()) {
			searchReservationsByUserId();
		}
		if (ae.getSource() == arlf.getJbtnApprove()) {
			openDetail(selectedCode());
		}
		if (ae.getSource() == arlf.getJbtnCancel()) {
			cancelReservation();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == arlf.getJtReservation()) {
			JTable jt = arlf.getJtReservation();
			int row = jt.rowAtPoint(e.getPoint());
			int col = jt.columnAtPoint(e.getPoint());
			// 상세내용(4번 컬럼) 클릭 시 상세 다이얼로그
			if (row >= 0 && col == 4) {
				openDetail(jt.getValueAt(row, 0).toString());
			}
		}
	}

	//맨처음 관리자가 들어오면 전체 데이터를 보여줍니다
	public void searchAllReservations() {
		showReservToAdmin(null);
	}

	//아이디를 검색하면 검색한 유저의 예약정보만 보여준다
	public void searchReservationsByUserId() {
		if("".equals(arlf.getJtfId().getText())) {
			JOptionPane.showMessageDialog(arlf, "아이디를 검색해주세여");
			return;
		}
		String id = arlf.getJtfId().getText().trim();
		showReservToAdmin(id);
	}

	//테이블의 예약정보를 가져온다
	private void showReservToAdmin(String id) {
		DefaultTableModel dtm = arlf.getDtmReservation();
		//화면 초기화 하기
		dtm.setRowCount(0);
		String[] reservRow = new String[7];
		try {
			//조회는 아이디 검색 안하면 다 아이디 있으면 회원을 조회한다
			List<ReservationDTO> UserData = ars.searchReservationsByUserId(id);
			for(ReservationDTO row: UserData) {
				reservRow[0] = row.getReservationCode();
				reservRow[1] = row.getUserId();
				reservRow[2] = row.getRepairItem();
				reservRow[3] = row.getPreferredReservationDate().toString();
				reservRow[4] = "보기";
				reservRow[5] = row.getConfirmedDate() == null ? "미정" : row.getConfirmedDate().toString();
				reservRow[6] = row.getReserveState();
				dtm.addRow(reservRow);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String selectedCode() {
		int row = arlf.getJtReservation().getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(arlf, "예약을 선택해주세요.");
			return null;
		}
		return arlf.getJtReservation().getValueAt(row, 0).toString();
	}

	/** 디테일 다이얼로그 열기 */
	public void openDetail(String reservationCode) {
		if (reservationCode == null) {
			return;
		}
		AdminReservationDetailForm ardf = new AdminReservationDetailForm(reservationCode);
		ardf.setLocationRelativeTo(arlf);
		ardf.setVisible(true);

		// 다이얼로그가 닫히면 목록 갱신
		String id = arlf.getJtfId().getText().trim();
		showReservToAdmin(id.isEmpty() ? null : id);
	}

	/** 선택한 예약을 바로 취소 */
	public void cancelReservation() {
		String code = selectedCode();
		if (code == null) {
			return;
		}

		int choice = JOptionPane.showConfirmDialog(arlf, code + " 예약을 취소하시겠습니까?", "예약취소",
				JOptionPane.YES_NO_OPTION);
		if (choice != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			if (ars.cancelReservation(code) == 1) {
				JOptionPane.showMessageDialog(arlf, "에약취소 완료");
				showReservToAdmin(null);
			} else {
				JOptionPane.showMessageDialog(arlf, "예약취소 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
