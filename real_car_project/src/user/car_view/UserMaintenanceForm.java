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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import user.UserMainTab;
import user.car_evt.UserMaintenanceFormEvt;

public class UserMaintenanceForm extends JPanel {

	private JLabel jlblTitle;
	private JComboBox<String> jcbPeriod, jcbCar;
	private JButton jbtnSearch, jbtnDetail;

	private JTable jtMaintenance;
	private DefaultTableModel dtmMaintenance;
	private JScrollPane jsp;

	public UserMaintenanceForm(UserMainTab umt, JTabbedPane jtpUser, String userId) {
		setLayout(null);

		jlblTitle = new JLabel("정비이력");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		jcbPeriod = new JComboBox<String>(new String[] { "전체 기간", "최근 1개월", "최근 3개월", "최근 6개월" });
		jcbPeriod.setBounds(340, 10, 110, 25);
		add(jcbPeriod);

		jcbCar = new JComboBox<String>();
		jcbCar.setBounds(460, 10, 190, 25);
		add(jcbCar);

		jbtnSearch = new JButton("검색");
		jbtnSearch.setBounds(660, 10, 90, 25);
		add(jbtnSearch);

		String[] columns = { "번호", "정비일자", "차량번호", "차종", "정비내용", "총비용", "진행상태" };
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

		jbtnDetail = new JButton("상세내역");
		jbtnDetail.setBounds(340, 545, 100, 30);
		add(jbtnDetail);

		UserMaintenanceFormEvt evt = new UserMaintenanceFormEvt(this, umt, userId);
		jbtnSearch.addActionListener(evt);
		jbtnDetail.addActionListener(evt);
		jtMaintenance.addMouseListener(evt);

		evt.searchMyCars();
		evt.searchByUserId(userId);

		UiStyle.setStatusBadge(jtMaintenance, 6);
		UiStyle.apply(this);
	}

	public JComboBox<String> getJcbPeriod() {
		return jcbPeriod;
	}

	public JComboBox<String> getJcbCar() {
		return jcbCar;
	}

	public JButton getJbtnSearch() {
		return jbtnSearch;
	}

	public JButton getJbtnDetail() {
		return jbtnDetail;
	}

	public JTable getJtMaintenance() {
		return jtMaintenance;
	}

	public DefaultTableModel getDtmMaintenance() {
		return dtmMaintenance;
	}
}
