package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import admin.car_service.AdminService;
import admin.car_view.AdminMemberForm;
import car_dto.UserDTO;

public class AdminMemberFormEvt implements ActionListener {

	private AdminMemberForm amf;
	private List<String[]> original = new ArrayList<String[]>();

	public AdminMemberFormEvt(AdminMemberForm amf) {
		this.amf = amf;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == amf.getJbtnDelete()) {
			updateDeleteState();
		}

		if (ae.getSource() == amf.getJbtnChange()) {
			if (amf.isEditMode()) {
				finishEdit();
			} else {
				startEdit();
			}
		}
	}

	/** 전체 회원을 표에 보여준다. */
	public void searchMember() {
		amf.setMemberTable(new AdminService().searchAllMember());
	}

	public void updateDeleteState() {
		int row = amf.getJt().getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(amf, "삭제할 회원을 선택해주세요.");
			return;
		}

		String userId = (String) amf.getDtm().getValueAt(row, 0);

		int choice = JOptionPane.showConfirmDialog(amf, userId + " 회원을 삭제하시겠습니까?\n(회원 정보와 차량은 삭제 처리만 되고, 예약/정비 기록은 DB에 보존됩니다.)", "삭제 확인",
				JOptionPane.YES_NO_OPTION);
		if (choice != JOptionPane.YES_OPTION) {
			return;
		}

		if (new AdminService().removeUser(userId)) {
			JOptionPane.showMessageDialog(amf, "삭제되었습니다.");
			searchMember();
		} else {
			JOptionPane.showMessageDialog(amf, "삭제에 실패했습니다.");
		}
	}

	public void startEdit() {
		DefaultTableModel dtm = amf.getDtm();

		if (dtm.getRowCount() == 0) {
			JOptionPane.showMessageDialog(amf, "수정할 회원이 없습니다.");
			return;
		}

		original.clear();
		for (int i = 0; i < dtm.getRowCount(); i++) {
			String[] rowData = new String[5];
			for (int j = 0; j < 5; j++) {
				rowData[j] = toStr(dtm.getValueAt(i, j));
			}
			original.add(rowData);
		}

		amf.setEditMode(true);
		amf.getJbtnChange().setText("수정완료");
		amf.getJbtnDelete().setEnabled(false);
		JOptionPane.showMessageDialog(amf, "수정할 셀을 더블클릭해서 입력한 뒤 Enter를 누르고,\n끝나면 [수정완료] 버튼을 눌러주세요.");
	}

	public void finishEdit() {
		JTable jt = amf.getJt();
		DefaultTableModel dtm = amf.getDtm();

		if (jt.isEditing()) {
			jt.getCellEditor().stopCellEditing();
		}

		List<UserDTO> changed = new ArrayList<UserDTO>();
		for (int i = 0; i < dtm.getRowCount(); i++) {
			String[] now = new String[5];
			for (int j = 0; j < 5; j++) {
				now[j] = toStr(dtm.getValueAt(i, j));
			}
			for (int j = 1; j < 5; j++) {
				if (!now[j].equals(original.get(i)[j])) {
					if (now[1].isEmpty() || now[2].isEmpty() || now[3].isEmpty() || now[4].isEmpty()) {
						JOptionPane.showMessageDialog(amf, (i + 1) + "번째 행에 빈 값이 있습니다.");
						return;
					}
					changed.add(new UserDTO(now[0], now[1], now[2], now[3], now[4], null));
					break;
				}
			}
		}

		if (changed.isEmpty()) {
			JOptionPane.showMessageDialog(amf, "변경된 내용이 없습니다.");
			endEditMode();
			return;
		}

		int choice = JOptionPane.showConfirmDialog(amf, changed.size() + "회원 정보를 수정하시겠습니까?", "수정 확인",
				JOptionPane.YES_NO_OPTION);
		if (choice != JOptionPane.YES_OPTION) {
			return;
		}

		AdminService as = new AdminService();
		int success = 0;
		for (UserDTO uDTO : changed) {
			if (as.modifyUser(uDTO)) {
				success++;
			}
		}
		if (success == changed.size()) {
			JOptionPane.showMessageDialog(amf, "수정되었습니다.");
		} else {
			JOptionPane.showMessageDialog(amf, changed.size() + "건 중 " + success + "건만 수정되었습니다.");
		}

		endEditMode();
		searchMember();
	}

	private String toStr(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	private void endEditMode() {
		amf.setEditMode(false);
		amf.getJbtnChange().setText("수정");
		amf.getJbtnDelete().setEnabled(true);
	}
}
