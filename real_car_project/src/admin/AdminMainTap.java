package admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import admin.car_view.AdminCarListForm;



public class AdminMainTap extends JFrame {

	private JTabbedPane jtpAdmin;
	private JButton jbtnLogout;
	private JLabel jlblAdminName;

	public AdminMainTap() {
		jtpAdmin = new JTabbedPane();
		jbtnLogout = new JButton("로그아웃");
		jlblAdminName = new JLabel("관리자 " + "님");

		jtpAdmin.addTab("회원", new JPanel());
		AdminCarListForm aclf = new AdminCarListForm(this, jtpAdmin);
		jtpAdmin.addTab("차량", aclf);
		jtpAdmin.addTab("예약", new JPanel());
		jtpAdmin.addTab("정비", new JPanel());

		jbtnLogout.setBounds(650, 2, 90, 22);
		add(jbtnLogout);

		jlblAdminName.setBounds(580, 2, 90, 22);
		add(jlblAdminName);

		add(jtpAdmin);

		setResizable(false);
		setBounds(100, 100, 780, 700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
