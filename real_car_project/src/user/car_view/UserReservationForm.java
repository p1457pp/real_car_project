package user.car_view;

import car_util.UiStyle;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import user.UserMainTab;
import user.car_evt.UserReservationFormEvt;

public class UserReservationForm extends JPanel {

	private JLabel jlblTitle;
	private JTextField jtfCarNum, jtfReserveDate, jtfCarModel;
	private JTextArea jtaRequest;
	private JTable jtReservation;
	private DefaultTableModel dtmReservation;
	private JButton jbtnRequest, jbtnList;
	private JComboBox<String> jcbRepairItem;

	public UserReservationForm(UserMainTab umt, JTabbedPane jtpUser, String userId) {
		setLayout(null);

		jlblTitle = new JLabel("예약신청");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		jbtnList = new JButton("예약기록");
		jbtnList.setBounds(660, 10, 90, 25);
		add(jbtnList);

		// 왼쪽 : 나의 예약 현황 표
		String[] columns = { "차량번호", "예약일자", "차종", "정비항목" };
		dtmReservation = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jtReservation = new JTable(dtmReservation);
		jtReservation.setRowHeight(40);
		DefaultTableCellRenderer center = (DefaultTableCellRenderer) jtReservation.getDefaultRenderer(Object.class);
		center.setHorizontalAlignment(JLabel.CENTER);

		JScrollPane jsp = new JScrollPane(jtReservation);
		jsp.setBounds(10, 50, 420, 450);
		add(jsp);

		// 오른쪽 : 예약 입력
		int x = 450, lw = 90, fx = x + lw, fw = 210;

		add(createLabel("차량번호", x, 50, lw));
		jtfCarNum = new JTextField();
		jtfCarNum.setBounds(fx, 50, fw, 30);
		add(jtfCarNum);

		add(createLabel("차종", x, 90, lw));
		jtfCarModel = new JTextField();
		jtfCarModel.setBounds(fx, 90, fw, 30);
		jtfCarModel.setEditable(false);
		add(jtfCarModel);

		add(createLabel("예약희망일", x, 130, lw));
		jtfReserveDate = new JTextField();
		jtfReserveDate.setBounds(fx, 130, fw, 30);
		add(jtfReserveDate);

		JLabel lblFormat = new JLabel("예) 2027-01-15");
		lblFormat.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
		lblFormat.setBounds(fx, 160, fw, 18);
		add(lblFormat);

		add(createLabel("정비항목", x, 185, lw));
		jcbRepairItem = new JComboBox<String>();
		jcbRepairItem.setBounds(fx, 185, fw, 30);
		add(jcbRepairItem);

		add(createLabel("요청사항", x, 225, lw));
		jtaRequest = new JTextArea();
		jtaRequest.setLineWrap(true);
		jtaRequest.setWrapStyleWord(true);
		jtaRequest.setBorder(new LineBorder(java.awt.Color.GRAY));
		JScrollPane jspReq = new JScrollPane(jtaRequest);
		jspReq.setBounds(x, 255, lw + fw, 245);
		add(jspReq);

		jbtnRequest = new JButton("예약하기");
		jbtnRequest.setBounds(340, 545, 100, 30);
		add(jbtnRequest);

		UserReservationFormEvt evt = new UserReservationFormEvt(this, umt, jtpUser, userId);
		jbtnRequest.addActionListener(evt);
		jbtnList.addActionListener(evt);
		jtfCarNum.addActionListener(evt);
		jtfCarNum.addFocusListener(evt);

		evt.searchRepairItems();
		evt.searchReservationsByUserId(userId);

		UiStyle.apply(this);
	}

	private JLabel createLabel(String text, int x, int y, int w) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl.setBounds(x, y, w, 30);
		return lbl;
	}

	public JTextField getJtfCarNum() {
		return jtfCarNum;
	}

	public JTextField getJtfReserveDate() {
		return jtfReserveDate;
	}

	public JTextField getJtfCarModel() {
		return jtfCarModel;
	}

	public JTextArea getJtaRequest() {
		return jtaRequest;
	}

	public JTable getJtReservation() {
		return jtReservation;
	}

	public DefaultTableModel getDtmReservation() {
		return dtmReservation;
	}

	public JButton getJbtnRequest() {
		return jbtnRequest;
	}

	public JButton getJbtnList() {
		return jbtnList;
	}

	public JComboBox<String> getJcbRepairItem() {
		return jcbRepairItem;
	}
}
