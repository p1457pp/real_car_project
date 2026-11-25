package admin.car_view;

import car_util.UiStyle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import admin.AdminMainTab;
import admin.car_evt.AdminMaintenanceRevenueFormEvt;

public class AdminMaintenanceRevenueForm extends JPanel {

	private JButton jbtnSearch;
	private JTextArea jta;
	private JComboBox<String> jcbYear, jcbMonth;

	public AdminMaintenanceRevenueForm(AdminMainTab amt) {
		super(null);

		jcbYear = new JComboBox<String>(new String[] { "2025", "2026", "2027" });
		jcbMonth = new JComboBox<String>(new String[] {
				"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
		jbtnSearch = new JButton("조회");

		JLabel jlblTitle = new JLabel("월말정산");
		jlblTitle.setFont(new java.awt.Font("맑은 고딕", java.awt.Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		JLabel lblYear = new JLabel("년");
		JLabel lblMonth = new JLabel("월");

		jcbYear.setBounds(400, 10, 80, 25);
		lblYear.setBounds(485, 10, 20, 25);
		jcbMonth.setBounds(510, 10, 60, 25);
		lblMonth.setBounds(575, 10, 20, 25);
		jbtnSearch.setBounds(660, 10, 90, 25);

		jta = new JTextArea();
		jta.setEditable(false);
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setBounds(10, 50, 740, 450);

		add(jcbYear);
		add(lblYear);
		add(jcbMonth);
		add(lblMonth);
		add(jbtnSearch);
		add(jsp);

		jbtnSearch.addActionListener(new AdminMaintenanceRevenueFormEvt(this));

		UiStyle.apply(this);
	}

	public JButton getJbtnSearch() {
		return jbtnSearch;
	}

	public void setJbtnSearch(JButton jbtnSearch) {
		this.jbtnSearch = jbtnSearch;
	}

	public JTextArea getJta() {
		return jta;
	}

	public void setJta(JTextArea jta) {
		this.jta = jta;
	}

	public JComboBox<String> getJcbYear() {
		return jcbYear;
	}

	public JComboBox<String> getJcbMonth() {
		return jcbMonth;
	}
}
