package admin.car_view;

import car_util.UiStyle;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.AdminMainTab;
import admin.car_evt.AdminMaintenanceFormEvt;

public class AdminMaintenanceForm extends JPanel {

	private JLabel jlblTitle;
	private JTextField jtfStart, jtfEnd, jtfKeyword;
	private JButton jbtnSearch, jbtnAdd, jbtnInfoDetail;

	private JTable jtMaintenance;
	private DefaultTableModel dtmMaintenance;
	private JScrollPane jsp;

	public AdminMaintenanceForm(AdminMainTab amt) {
		setLayout(null);

		jlblTitle = new JLabel("정비이력");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		// 조회 기간 (yyyy.MM.dd ~ yyyy.MM.dd)
		jtfStart = new JTextField("2026.01.01");
		jtfStart.setBounds(120, 10, 85, 25);
		add(jtfStart);

		JLabel lblWave = new JLabel("~", JLabel.CENTER);
		lblWave.setBounds(205, 10, 20, 25);
		add(lblWave);

		jtfEnd = new JTextField("2027.12.31");
		jtfEnd.setBounds(225, 10, 85, 25);
		add(jtfEnd);

		jtfKeyword = new JTextField();
		jtfKeyword.setToolTipText("차량번호 또는 고객명");
		jtfKeyword.setBounds(425, 10, 225, 25);
		add(jtfKeyword);

		JLabel lblKeyword = new JLabel("차량번호/고객명", JLabel.RIGHT);
		lblKeyword.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
		lblKeyword.setBounds(320, 10, 100, 25);
		add(lblKeyword);

		jbtnSearch = new JButton("검색");
		jbtnSearch.setBounds(660, 10, 90, 25);
		add(jbtnSearch);

		String[] columns = { "번호", "정비일자", "고객명", "차량번호", "정비내용", "총청구액", "진행상태" };
		dtmMaintenance = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jtMaintenance = new JTable(dtmMaintenance);
		jtMaintenance.setRowHeight(40);
		jtMaintenance.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer center = (DefaultTableCellRenderer) jtMaintenance.getDefaultRenderer(Object.class);
		center.setHorizontalAlignment(JLabel.CENTER);

		jsp = new JScrollPane(jtMaintenance);
		jsp.setBounds(10, 50, 740, 450);
		add(jsp);

		jbtnAdd = new JButton("정비추가");
		jbtnAdd.setBounds(280, 545, 100, 30);
		add(jbtnAdd);

		jbtnInfoDetail = new JButton("상세내역");
		jbtnInfoDetail.setBounds(390, 545, 100, 30);
		add(jbtnInfoDetail);

		AdminMaintenanceFormEvt evt = new AdminMaintenanceFormEvt(this, amt);
		jbtnSearch.addActionListener(evt);
		jtfKeyword.addActionListener(evt);
		jbtnAdd.addActionListener(evt);
		jbtnInfoDetail.addActionListener(evt);
		jtMaintenance.addMouseListener(evt);

		evt.searchAllMaintenance();

		UiStyle.setStatusBadge(jtMaintenance, 6);
		UiStyle.apply(this);
	}

	public JTextField getJtfStart() {
		return jtfStart;
	}

	public JTextField getJtfEnd() {
		return jtfEnd;
	}

	public JTextField getJtfKeyword() {
		return jtfKeyword;
	}

	public JButton getJbtnSearch() {
		return jbtnSearch;
	}

	public JButton getJbtnAdd() {
		return jbtnAdd;
	}

	public JButton getJbtnInfoDetail() {
		return jbtnInfoDetail;
	}

	public JTable getJtMaintenance() {
		return jtMaintenance;
	}

	public DefaultTableModel getDtmMaintenance() {
		return dtmMaintenance;
	}
}
