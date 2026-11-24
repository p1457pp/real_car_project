package user.car_view;

import car_util.UiStyle;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import user.UserMainTab;
import user.car_evt.UserMaintenanceDetailFormEvt;

public class UserMaintenanceDetailForm extends JDialog {

	private JLabel jlblCarNum, jlblMaintenanceDate, jlblCarModel, jlblCenter, jlblStatus, jlblTotal;
	private JTextArea jtaComment;
	private JTable jtDetail;
	private DefaultTableModel dtmDetail;
	private JButton jbtnClose;

	public UserMaintenanceDetailForm(UserMainTab umt, int reportNo) {
		super(umt, "정비 상세 명세서 (No. " + reportNo + ")", true);
		setLayout(null);

		jlblMaintenanceDate = addInfo("정비일자", 20, 20);
		jlblCenter = addInfo("정비지점", 340, 20);
		jlblCarNum = addInfo("차량번호", 20, 55);
		jlblCarModel = addInfo("차종", 340, 55);
		jlblStatus = addInfo("진행상태", 20, 90);

		String[] columns = { "정비 항목", "부품단가", "수량", "공임비", "금액" };
		dtmDetail = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jtDetail = new JTable(dtmDetail);
		jtDetail.setRowHeight(30);
		jtDetail.getTableHeader().setReorderingAllowed(false);
		DefaultTableCellRenderer center = (DefaultTableCellRenderer) jtDetail.getDefaultRenderer(Object.class);
		center.setHorizontalAlignment(JLabel.CENTER);

		JScrollPane jsp = new JScrollPane(jtDetail);
		jsp.setBounds(20, 130, 620, 220);
		add(jsp);

		jlblTotal = new JLabel("총 청구 금액 : 0원");
		jlblTotal.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		jlblTotal.setBounds(380, 360, 260, 25);
		add(jlblTotal);

		JLabel lblComment = new JLabel("정비사 코멘트");
		lblComment.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblComment.setBounds(20, 360, 150, 25);
		add(lblComment);

		jtaComment = new JTextArea();
		jtaComment.setEditable(false);
		jtaComment.setLineWrap(true);
		jtaComment.setWrapStyleWord(true);
		JScrollPane jspMemo = new JScrollPane(jtaComment);
		jspMemo.setBounds(20, 390, 620, 90);
		add(jspMemo);

		jbtnClose = new JButton("닫기");
		jbtnClose.setBounds(290, 495, 100, 30);
		add(jbtnClose);

		UserMaintenanceDetailFormEvt evt = new UserMaintenanceDetailFormEvt(this, reportNo);
		jbtnClose.addActionListener(evt);
		addWindowListener(evt);
		evt.searchMaintenanceDetail(reportNo);

		setSize(680, 600);
		setResizable(false);
		setLocationRelativeTo(umt);
		UiStyle.apply(this);

		setVisible(true);
	}

	/** 라벨(제목)과 값 라벨을 만들어 값 라벨을 반환한다. */
	private JLabel addInfo(String key, int x, int y) {
		JLabel k = new JLabel(key);
		k.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		k.setBounds(x, y, 80, 25);
		add(k);

		JLabel v = new JLabel("-");
		v.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		v.setBounds(x + 80, y, 220, 25);
		add(v);
		return v;
	}

	public JLabel getJlblCarNum() {
		return jlblCarNum;
	}

	public JLabel getJlblMaintenanceDate() {
		return jlblMaintenanceDate;
	}

	public JLabel getJlblCarModel() {
		return jlblCarModel;
	}

	public JLabel getJlblCenter() {
		return jlblCenter;
	}

	public JLabel getJlblStatus() {
		return jlblStatus;
	}

	public JLabel getJlblTotal() {
		return jlblTotal;
	}

	public JTextArea getJtaComment() {
		return jtaComment;
	}

	public JTable getJtDetail() {
		return jtDetail;
	}

	public DefaultTableModel getDtmDetail() {
		return dtmDetail;
	}

	public JButton getJbtnClose() {
		return jbtnClose;
	}
}
