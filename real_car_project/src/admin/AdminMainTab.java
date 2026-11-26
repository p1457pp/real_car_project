package admin;

import java.awt.Component;
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
import car_util.UiStyle;

public class AdminMainTab extends JFrame {

	private JTabbedPane jtpAdmin;
	private JButton jbtnLogout;
	private JButton jbtnRefresh;

	public AdminMainTab() {
		super("관리자 차량 관리");

		// 상단 : 로그아웃
		JPanel jpTop = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 2));
		jpTop.setOpaque(false);

		JLabel jlblAdmin = new JLabel("관리자");
		jlblAdmin.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		jpTop.add(jlblAdmin);

		jbtnRefresh = new JButton("새로고침");
		jbtnRefresh.setPreferredSize(new java.awt.Dimension(76, 20));
		jbtnRefresh.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		jbtnRefresh.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jpTop.add(jbtnRefresh);

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

		// 새로고침 버튼을 누르면 지금 보고 있는 탭을 DB에서 다시 조회
		jbtnRefresh.addActionListener(e -> refreshSelectedTab());

		// 탭을 누를 때도 새로고침
		jtpAdmin.addChangeListener(e -> refreshSelectedTab());

		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UiStyle.apply(this);

		setVisible(true);
	}

	/**
	 * 선택된 패널의 종류를 확인해서, 같은 종류의 패널을 새로 만들어 교체한다
	 * 모든화면은 수정 중이 아닐 때만 새로고침 (수정 중이면 입력 내용 보호)
	 * (패널 생성자에서 DB를 조회하므로 새로 만들면 최신 데이터가 보인다)
	 */
	public void refreshSelectedTab() {
		int idx = jtpAdmin.getSelectedIndex(); // 현재 선택된 탭의 번호
		Component c = jtpAdmin.getSelectedComponent(); // 현재 선택된 탭에 들어있는 패널 객체

		if (c instanceof AdminMemberForm) {
			if (!((AdminMemberForm) c).isEditMode()) {
				jtpAdmin.setComponentAt(idx, new AdminMemberForm(this));
			} // 회원화면
		} 
		
		else if (c instanceof AdminCarListForm) {
			jtpAdmin.setComponentAt(idx, new AdminCarListForm(this, jtpAdmin));
		} // 차량화면
		
		else if (c instanceof AdminReservationListForm) {
			jtpAdmin.setComponentAt(idx, new AdminReservationListForm());
		} // 예약화면
		
		else if (c instanceof AdminMaintenanceForm) {
			jtpAdmin.setComponentAt(idx, new AdminMaintenanceForm(this));
		} // 정비화면
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