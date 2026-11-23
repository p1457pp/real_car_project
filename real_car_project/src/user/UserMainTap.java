package user;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import user.car_view.CarReservPanelUser;
import user.car_view.UserCarListForm;
import user.car_view.UserMaintenanceForm;

public class UserMainTap extends JFrame {

	private JTabbedPane jtpUser;
	private String userId;

	public UserMainTap() {
		this("user028");
	}

	public UserMainTap(String userId) {
		super("사용자 차량 관리");
		this.userId = (userId != null && !userId.trim().isEmpty()) ? userId : "user028";

		jtpUser = new JTabbedPane();
		jtpUser.setFont(new Font("맑은 고딕", Font.BOLD, 14));

		jtpUser.addTab("회원정보", new JPanel());
		jtpUser.addTab("내 차량", new UserCarListForm(this, jtpUser, this.userId));
		jtpUser.addTab("예약관리", new CarReservPanelUser());
		jtpUser.addTab("정비이력", new UserMaintenanceForm(this.userId).getContentPane());

		add(jtpUser);

		setSize(800, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public JTabbedPane getJtpUser() {
		return jtpUser;
	}

	public String getUserId() {
		return userId;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new UserMainTap("user028"));
	}
}
