package user;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import user.car_view.UserCarListForm;


public class UserMainTap extends JFrame {
	
	private JTabbedPane jtpUser;
	private JButton jbtnLogout;
	private JLabel jblUserName;
	
    public UserMainTap (String userId) {
        jtpUser = new JTabbedPane();
        jbtnLogout = new JButton("로그아웃");
        jblUserName = new JLabel("홍길동 " + "님");
        
        
        jtpUser.addTab("회원", new JPanel());
        UserCarListForm uclf = new UserCarListForm(this, jtpUser, userId);
        jtpUser.addTab("차량", uclf);
        jtpUser.addTab("예약", new JPanel());
        jtpUser.addTab("정비", new JPanel());
        
        jbtnLogout.setBounds(650, 2, 90, 22);
        add(jbtnLogout);
        
        jblUserName.setBounds(580, 2, 90, 22);
        add(jblUserName);
        
        
        add(jtpUser);                       

        setResizable(false);
        setBounds(100, 100, 780, 700);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    
}
