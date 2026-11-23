package admin.car_view;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import admin.car_evt.AdminMemberManagementEvt;
import admin.car_service.AdminMemberManagementService;
import car_dto.AdminMemberManagementDTO;

public class AdminMemberManagementPanel extends JPanel {

	private DefaultTableModel dtm;
	private JTable jt;
	private JButton changeJbtn;
	private JButton deleteJbtn;
	
	 private JFrame owner;
	
	private boolean editMode; //수정모드 여부
	
	
	public AdminMemberManagementPanel(JFrame owner) {
		super(null);
	    this.owner = owner;
		
		

	    String[] columnName= {"회원아이디","회원명","이메일","전화번호","회원 등급","가입일"};
	    
	    
	    
	    dtm = new DefaultTableModel(columnName,0 ) {
	    	@Override
	    	public boolean isCellEditable(int row, int column) {
	    		return editMode && column >= 1 && column <=4;
	    	}
	    };
	    
	    jt = new JTable(dtm);
	    jt.getTableHeader().setReorderingAllowed(false); // 컬럼 순서 변경 방지
	    jt.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	    
	    JScrollPane jsp = new JScrollPane(jt);
	    jsp.setBounds(0, 0, 780, 250);
	    add(jsp);
	    
	    changeJbtn=new JButton("수정");
	    deleteJbtn=new JButton("삭제");
	    
	    styleButton(changeJbtn, new Color(0x2563EB));   // 파랑
	    styleButton(deleteJbtn, new Color(0xDC2626)); 
	    
	    changeJbtn.setBounds(550, 270, 90, 40);
	    deleteJbtn.setBounds(670, 270, 90, 40);
	   add(changeJbtn);
	   add(deleteJbtn);
		setMemberTable();
		
		AdminMemberManagementEvt ammEvt = new AdminMemberManagementEvt(this);
		deleteJbtn.addActionListener(ammEvt);
		changeJbtn.addActionListener(ammEvt);
	}
	

	
	
	public DefaultTableModel getDtm() {
		return dtm;
	}

	public JTable getJt() {
		return jt;
	}
	
	public void setMemberTable() {
		dtm.setRowCount(0);   // 새로고침 시 중복 방지

	    List<AdminMemberManagementDTO> list = new AdminMemberManagementService().searchAllMember();
	    for (AdminMemberManagementDTO m : list) {
	        dtm.addRow(new Object[] {
	            m.getUserId(), m.getName(), m.getEmail(),
	            m.getPhone(), m.getGrade(), m.getDate()
	        });
	    }
	}
	
	public boolean isEditMode() {
	    return editMode;
	}

	public void setEditMode(boolean editMode) {
	    this.editMode = editMode;
	}
	
	//버튼 디자인
	private void styleButton(JButton btn, Color bg) {
	    btn.setBackground(bg);
	    btn.setForeground(Color.WHITE);
	    btn.setFocusPainted(false);
	    btn.setBorderPainted(false);
	    btn.setOpaque(true);
	}
	
	
	
	public JButton getChangeJbtn() {
		return changeJbtn;
	}


	public void setChangeJbtn(JButton changeJbtn) {
		this.changeJbtn = changeJbtn;
	}


	public JButton getDeleteJbtn() {
		return deleteJbtn;
	}


	public void setDeleteJbtn(JButton deleteJbtn) {
		this.deleteJbtn = deleteJbtn;
	}


	
}
