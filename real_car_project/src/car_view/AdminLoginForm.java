package car_view;


import java.awt.Color;  

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import car_evt.AdminLoginEvt;

public class AdminLoginForm extends JFrame {

	private JTextField jtfId;
	private JPasswordField jpfPass;
	private JButton jbtnLogin;

	public AdminLoginForm() {
		super("관리자 프로그램");

		jtfId = new JTextField();
		jpfPass = new JPasswordField();
		jbtnLogin = new JButton("로그인");
		
		styleButton(jbtnLogin, new Color(0x2563EB));
		
		JPanel panel = new JPanel(null);
		JLabel idLabel = new JLabel("관리자 아이디");
		idLabel.setBounds(180, 82, 110, 30);
		jtfId.setBounds(300, 80, 300, 30);

		JLabel pwLabel = new JLabel("관리자 비밀번호");
		pwLabel.setBounds(170, 122, 120, 30);
		jpfPass.setBounds(300, 120, 300, 30);

		jbtnLogin.setBounds(670, 270, 90, 40);

		panel.add(idLabel);
		panel.add(jtfId);
		panel.add(pwLabel);
		panel.add(jpfPass);
		panel.add(jbtnLogin);

		add(panel);

		setSize(800, 400);

		//프로그램이 완전히 종료되도록 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//화면 중앙에 창 띄우기
		setLocationRelativeTo(null);

		//창을 화면에 보이게
		setVisible(true);
		
		AdminLoginEvt ale = new AdminLoginEvt(this);
		jbtnLogin.addActionListener(ale);
	}

	public JTextField getJtfId() {
		return jtfId;
	}

	public JPasswordField getJpfPass() {
		return jpfPass;
	}

	public JButton getJbtnLogin() {
		return jbtnLogin;
	}
	
	private void styleButton(JButton btn, Color bg) {
	    btn.setBackground(bg);
	    btn.setForeground(Color.WHITE);
	    btn.setFocusPainted(false);
	    btn.setBorderPainted(false);
	    btn.setOpaque(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new AdminLoginForm();
		});
	}
}