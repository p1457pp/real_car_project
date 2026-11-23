package admin;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import admin.car_view.AdminCarListForm;
import admin.car_view.AdminMaintenanceForm;
import admin.car_view.AdminMemberManagementPanel;
import admin.car_view.CarReservPanelAdmin;
import admin.car_view.SettlementPanel;

public class AdminMainTap extends JFrame {

	private JTabbedPane jtpAdmin;

	public AdminMainTap() {
		super("관리자 차량 관리");

		jtpAdmin = new JTabbedPane();
		jtpAdmin.setFont(new Font("맑은 고딕", Font.BOLD, 14));

		jtpAdmin.addTab("회원관리", new AdminMemberManagementPanel(this));
		jtpAdmin.addTab("예약관리", new CarReservPanelAdmin());
		jtpAdmin.addTab("차량관리", new AdminCarListForm(this, jtpAdmin));
		jtpAdmin.addTab("정비이력", new AdminMaintenanceForm().getContentPane());
		jtpAdmin.addTab("월말정산", new SettlementPanel(this));

		add(jtpAdmin);

		setSize(820, 660);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public JTabbedPane getJtpAdmin() {
		return jtpAdmin;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new AdminMainTap());
	}
}
