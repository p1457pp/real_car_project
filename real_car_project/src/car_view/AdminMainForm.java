package car_view;

import java.awt.Font; 

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class AdminMainForm extends JFrame {

    public AdminMainForm() {
        super("관리자 프로그램");

        String[] tabNames = { "회원관리", "예약관리", "차량관리", "정비이력", "월말정산" };
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        tabs.addTab(tabNames[0], new AdminMemberManagementPanel(this));
        tabs.addTab(tabNames[1], new JPanel());
        tabs.addTab(tabNames[2], new JPanel());
        tabs.addTab(tabNames[3], new JPanel());
        tabs.addTab(tabNames[4], new SettlementPanel(this));

        add(tabs);

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMainForm());
    }
}
