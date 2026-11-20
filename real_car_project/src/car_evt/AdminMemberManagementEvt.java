package car_evt;

import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import car_dto.AdminMemberManagementDTO;
import car_view.AdminMemberManagementPanel;
import car_service.AdminMemberManagementService;

import javax.swing.JOptionPane;

public class AdminMemberManagementEvt implements ActionListener {

	private car_view.AdminMemberManagementPanel ammf;

	private List<String[]> original = new ArrayList<String[]>();
	
	public AdminMemberManagementEvt(car_view.AdminMemberManagementPanel ammf) {
		this.ammf=ammf;

	}


	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == ammf.getDeleteJbtn()) {
			deleteUser();
		}//end if
		
		if(ae.getSource() == ammf.getChangeJbtn()) {
			if (ammf.isEditMode()) {
				finishEdit(); //수정완료 클릭
			} else {
				startEdit(); //수정 상태 클릭
			}
		}
	}

	
	

	public void deleteUser() {
		// 1. 선택된 행 확인
		int row = ammf.getJt().getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(ammf, "삭제할 회원을 선택해주세요.");
			return;
		}

		// 2. 회원 아이디 얻기
		String userId = (String) ammf.getDtm().getValueAt(row, 0);

		// 3. 삭제 확인
		int choice = JOptionPane.showConfirmDialog(ammf,
				userId + " 회원을 삭제하시겠습니까?", "삭제 확인",
				JOptionPane.YES_NO_OPTION);
		if (choice != JOptionPane.YES_OPTION) {
			return;
		}

		// 4. 삭제 처리
		if (new AdminMemberManagementService().deleteUser(userId)) {
			JOptionPane.showMessageDialog(ammf, "삭제되었습니다.");
			ammf.setMemberTable();   // 표 새로고침
		} else {
			JOptionPane.showMessageDialog(ammf, "삭제에 실패했습니다.");
		}
		
	
	}
	
	public void startEdit() {
		DefaultTableModel dtm= ammf.getDtm();
		
		if(dtm.getRowCount() == 0) {
			JOptionPane.showMessageDialog(ammf, "수정할 회원이 없습니다.");
			return;
		}
		
		original.clear();
		for(int i=0; i < dtm.getRowCount(); i++) {
			String[] rowData = new String[5];
			for(int j=0; j<5; j++) {
				rowData[j] = toStr(dtm.getValueAt(i, j));
			}
			original.add(rowData);
		}
		
		 ammf.setEditMode(true);
		 ammf.getChangeJbtn().setText("수정완료");
		 ammf.getDeleteJbtn().setEnabled(false);   // 편집 중 삭제 방지
		 JOptionPane.showMessageDialog(ammf, "수정할 셀을 더블클릭해서 입력한 뒤 Enter를 누르고,\n끝나면 [수정완료] 버튼을 눌러주세요.");
	}
	
	public void finishEdit() {
		JTable jt= ammf.getJt();
		DefaultTableModel dtm= ammf.getDtm();
		
		//입력중인 셀이 있으면 확정
		if(jt.isEditing()) {
			jt.getCellEditor().stopCellEditing();
		}
		
		List<AdminMemberManagementDTO> changed = new ArrayList<AdminMemberManagementDTO>();
		for (int i=0; i < dtm.getRowCount(); i++) {
			String[] now= new String[5];
			for(int j=0; j<5; j++) {
				now[j] = toStr(dtm.getValueAt(i, j));
			}
			for(int j=1; j<5; j++) {
				if (!now[j].equals(original.get(i)[j])) {
					if (now[1].isEmpty() || now[2]	.isEmpty() ||  now[3].isEmpty() || now[4].isEmpty()) {
						JOptionPane.showMessageDialog(ammf, (i+1)+"번째 행에 빈 값이 있습니다.");
						return;
					}
					changed.add(new AdminMemberManagementDTO(now[0], now[1], now[2], now[3], now[4], null));
	                break;
				}
			}
		}
		
		// 2. 변경 없음
	    if (changed.isEmpty()) {
	        JOptionPane.showMessageDialog(ammf, "변경된 내용이 없습니다.");
	        endEditMode();
	        return;
	    }
	    
	    //수정 확인 팝업
	    int choice = JOptionPane.showConfirmDialog(ammf, changed.size() + "회원 정보를 수정하시겠습니까?",
	    		"수정 확인", JOptionPane.YES_NO_OPTION);
	    if (choice != JOptionPane.YES_OPTION) {
	    	return;
	    }
	    
	    //DB 저장
	    AdminMemberManagementService sev= new AdminMemberManagementService();
	    int success = 0;
	    for(AdminMemberManagementDTO amDTO : changed) {
	    	if (sev.updateUser(amDTO)) {
	    		success++;	    		
	    	}
	    }
	    if (success == changed.size()) {
	    	JOptionPane.showMessageDialog(ammf, "수정되었습니다.");
	    }else {
	    	JOptionPane.showMessageDialog(ammf, changed.size() + "건 중" + success + "건만 수정되었습니다.");
	    }
	    
	    endEditMode();
	    ammf.setMemberTable();
	}
	
	private String toStr(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}
	
	private void endEditMode() {
		ammf.setEditMode(false);
		ammf.getChangeJbtn().setText("수정");
		ammf.getDeleteJbtn().setEnabled(true);
	}
}

