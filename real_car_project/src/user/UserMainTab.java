package user;

import car_util.UiStyle;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import user.car_view.MyPageForm;
import user.car_view.UserCarListForm;
import user.car_view.UserLoginForm;
import user.car_view.UserMaintenanceForm;
import user.car_view.UserReservationListForm;

public class UserMainTab extends JFrame {

	private JTabbedPane jtpUser;
	private JButton jbtnLogout;
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

		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UiStyle.apply(this);

		setVisible(true);
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
