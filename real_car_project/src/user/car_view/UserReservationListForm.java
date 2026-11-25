package user.car_view;

import car_util.UiStyle;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import user.UserMainTab;
import user.car_evt.UserReservationListFormEvt;

public class UserReservationListForm extends JPanel {

	private JLabel jlblTitle;
	private JButton jbtnAddReservation;

	private JTable jtReservation;
	private DefaultTableModel dtmReservation;
	private JScrollPane jsp;

	public UserReservationListForm(UserMainTab umt, JTabbedPane jtpUser, String userId) {
		setLayout(null);

		jlblTitle = new JLabel("예약기록");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		jbtnAddReservation = new JButton("예약신청");
		jbtnAddReservation.setBounds(660, 10, 90, 25);
		add(jbtnAddReservation);

		String[] columns = { "예약번호", "차량번호", "예약일자", "정비항목", "예약상태", "예약취소" };
		dtmReservation = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtReservation = new JTable(dtmReservation);
		jtReservation.setRowHeight(40);
		jtReservation.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < 4; i++) {
			jtReservation.getColumnModel().getColumn(i).setCellRenderer(center);
		}
		jtReservation.getColumnModel().getColumn(4).setCellRenderer(new StatusButtonRenderer());
		jtReservation.getColumnModel().getColumn(5).setCellRenderer(new StatusButtonRenderer());

		jsp = new JScrollPane(jtReservation);
		jsp.setBounds(10, 50, 740, 450);
		add(jsp);

		UserReservationListFormEvt evt = new UserReservationListFormEvt(this, umt, jtpUser, userId);
		jbtnAddReservation.addActionListener(evt);
		jtReservation.addMouseListener(evt);

		evt.searchReservationsByUserId(userId);

		UiStyle.apply(this);
	}

	public JButton getJbtnAddReservation() {
		return jbtnAddReservation;
	}

	public JTable getJtReservation() {
		return jtReservation;
	}

	public DefaultTableModel getDtmReservation() {
		return dtmReservation;
	}
}
