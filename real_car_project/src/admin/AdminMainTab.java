package admin;

import car_util.UiStyle;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import admin.car_view.AdminCarListForm;
import admin.car_view.AdminLoginForm;
import admin.car_view.AdminMaintenanceForm;
import admin.car_view.AdminMaintenanceRevenueForm;
import admin.car_view.AdminMemberForm;
import admin.car_view.AdminReservationListForm;

public class AdminMainTab extends JFrame {

	private JTabbedPane jtpAdmin;
	private JButton jbtnLogout;

	public AdminMainTab() {
		super("관리자 차량 관리");

		// 상단 : 로그아웃
		JPanel jpTop = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 2));
		jpTop.setOpaque(false);

		JLabel jlblAdmin = new JLabel("관리자");
		jlblAdmin.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		jpTop.add(jlblAdmin);

		jbtnLogout = new JButton("로그아웃");
		jbtnLogout.setPreferredSize(new java.awt.Dimension(76, 20));
		jbtnLogout.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		jbtnLogout.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jpTop.add(jbtnLogout);

		jtpAdmin = new JTabbedPane();
		jtpAdmin.setFont(new Font("맑은 고딕", Font.BOLD, 14));

		jtpAdmin.addTab("회원관리", new AdminMemberForm(this));
		jtpAdmin.addTab("예약관리", new AdminReservationListForm());
		jtpAdmin.addTab("차량관리", new AdminCarListForm(this, jtpAdmin));
		jtpAdmin.addTab("정비이력", new AdminMaintenanceForm(this));
		jtpAdmin.addTab("월말정산", new AdminMaintenanceRevenueForm(this));

		// 한 줄 : 탭 오른쪽 빈 공간에 이름 + 로그아웃을 겹쳐 배치
		JPanel jpRoot = new JPanel(null) {
			@Override
			public void doLayout() {
				jtpAdmin.setBounds(0, 0, getWidth(), getHeight());
				jpTop.setBounds(getWidth() - 340, 0, 340, 26);
			}
		};
		jpRoot.add(jpTop);
		jpRoot.add(jtpAdmin);
		setContentPane(jpRoot);

		jbtnLogout.addActionListener(e -> logout());

		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UiStyle.apply(this);

		setVisible(true);
	}

	/** 로그아웃 : 관리자 로그인 화면으로 돌아간다. */
	public void logout() {
		new AdminLoginForm();
		dispose();
	}

	public JTabbedPane getJtpAdmin() {
		return jtpAdmin;
	}
}
