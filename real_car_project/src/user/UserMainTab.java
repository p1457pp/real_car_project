package user;

import car_util.UiStyle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import admin.car_view.AdminCarListForm;
import admin.car_view.AdminMaintenanceForm;
import admin.car_view.AdminMemberForm;
import admin.car_view.AdminReservationListForm;
import user.car_view.MyPageForm;
import user.car_view.UserCarListForm;
import user.car_view.UserLoginForm;
import user.car_view.UserMaintenanceForm;
import user.car_view.UserReservationListForm;

public class UserMainTab extends JFrame {

	private JTabbedPane jtpUser;
	private JButton jbtnLogout;
	private JButton jbtnRefresh;
	private JLabel jlblUserName;
	private String userId;

	public UserMainTab(String userId) {
		super("사용자 차량 관리");
		this.userId = userId;

		// 상단 : 사용자 이름 / 로그아웃
		JPanel jpTop = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 2));
		jpTop.setOpaque(false);

		jlblUserName = new JLabel(userId + "님");
		jlblUserName.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		jpTop.add(jlblUserName);

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

		jtpUser = new JTabbedPane();
		jtpUser.setFont(new Font("맑은 고딕", Font.BOLD, 14));

		jtpUser.addTab("회원정보", new MyPageForm(this, userId));
		jtpUser.addTab("내 차량", new UserCarListForm(this, jtpUser, userId));
		jtpUser.addTab("예약관리", new UserReservationListForm(this, jtpUser, userId));
		jtpUser.addTab("정비이력", new UserMaintenanceForm(this, jtpUser, userId));

		// 한 줄 : 탭 오른쪽 빈 공간에 이름 + 로그아웃을 겹쳐 배치
		JPanel jpRoot = new JPanel(null) {
			@Override
			public void doLayout() {
				jtpUser.setBounds(0, 0, getWidth(), getHeight());
				jpTop.setBounds(getWidth() - 340, 0, 340, 26);
			}
		};
		jpRoot.add(jpTop);
		jpRoot.add(jtpUser);
		setContentPane(jpRoot);

		jbtnLogout.addActionListener(e -> logout());

		// 새로고침 버튼을 누르면 지금 보고 있는 탭을 DB에서 다시 조회
		jbtnRefresh.addActionListener(e -> refreshSelectedTab());

		// 탭을 누를 때도 새로고침
		jtpUser.addChangeListener(e -> refreshSelectedTab());

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
		int idx = jtpUser.getSelectedIndex();  // 현재 선택된 탭의 번호
		Component c = jtpUser.getSelectedComponent();  // 현재 선택된 탭에 들어있는 패널 객체

		if (c instanceof MyPageForm) {
			// 회원정보
			jtpUser.setComponentAt(idx, new MyPageForm(this, userId));
		} else if (c instanceof UserCarListForm) {
			// 내 차량
			jtpUser.setComponentAt(idx, new UserCarListForm(this, jtpUser, userId));
		} else if (c instanceof UserReservationListForm) {
			// 예약관리
			jtpUser.setComponentAt(idx, new UserReservationListForm(this, jtpUser, userId));
		} else if (c instanceof UserMaintenanceForm) {
			// 정비이력
			jtpUser.setComponentAt(idx, new UserMaintenanceForm(this, jtpUser, userId));
		}
	}


	/** 로그아웃 : 로그인 화면으로 돌아간다. */
	public void logout() {
		new UserLoginForm();
		dispose();
	}

	/** 마이페이지에서 조회한 사용자 이름을 상단에 표시한다. */
	public void setUserName(String name) {
		jlblUserName.setText(name + "님 (" + userId + ")");
	}

	public JTabbedPane getJtpUser() {
		return jtpUser;
	}

	public String getUserId() {
		return userId;
	}
}