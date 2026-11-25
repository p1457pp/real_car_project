package admin.car_view;

import car_util.UiStyle;

import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.AdminMainTab;
import admin.car_evt.AdminMemberFormEvt;
import car_dto.UserDTO;

public class AdminMemberForm extends JPanel {

	private JLabel jlblTitle;
	private JButton jbtnChange, jbtnDelete;

	private DefaultTableModel dtm;
	private JTable jt;
	private JScrollPane jsp;

	private boolean editMode;

	public AdminMemberForm(AdminMainTab amt) {
		setLayout(null);

		jlblTitle = new JLabel("회원관리");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		String[] columnName = { "회원아이디", "회원명", "이메일", "전화번호", "회원 등급", "가입일" };

		dtm = new DefaultTableModel(columnName, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return editMode && column >= 1 && column <= 4;
			}
		};

		jt = new JTable(dtm);
		jt.setRowHeight(40);
		jt.getTableHeader().setReorderingAllowed(false);
		jt.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		DefaultTableCellRenderer center = (DefaultTableCellRenderer) jt.getDefaultRenderer(Object.class);
		center.setHorizontalAlignment(JLabel.CENTER);

		jsp = new JScrollPane(jt);
		jsp.setBounds(10, 50, 740, 450);
		add(jsp);

		jbtnChange = new JButton("수정");
		jbtnChange.setBounds(280, 545, 100, 30);
		add(jbtnChange);

		jbtnDelete = new JButton("삭제");
		jbtnDelete.setBounds(390, 545, 100, 30);
		add(jbtnDelete);

		AdminMemberFormEvt evt = new AdminMemberFormEvt(this);
		jbtnChange.addActionListener(evt);
		jbtnDelete.addActionListener(evt);

		evt.searchMember();

		UiStyle.apply(this);
	}

	/** 표를 목록으로 다시 채운다. */
	public void setMemberTable(List<UserDTO> list) {
		dtm.setRowCount(0);
		for (UserDTO m : list) {
			dtm.addRow(new Object[] { m.getUserId(), m.getName(), m.getEmail(), m.getPhone(), m.getGrade(),
					m.getDate() });
		}
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public JTable getJt() {
		return jt;
	}

	public JButton getJbtnChange() {
		return jbtnChange;
	}

	public JButton getJbtnDelete() {
		return jbtnDelete;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
}
