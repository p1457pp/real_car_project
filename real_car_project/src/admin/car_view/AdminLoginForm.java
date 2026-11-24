/*<<<<<<< HEAD
package admin.car_view;


import java.awt.Color;   

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import admin.car_evt.AdminLoginEvt;

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
=======*/
package admin.car_view;

import car_util.UiStyle;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import admin.car_evt.AdminLoginFormEvt;

public class AdminLoginForm extends JFrame {

	private JTextField jtfId;
	private JPasswordField jpfPass;
	private JButton jbtnLogin;

	public AdminLoginForm() {
		super("관리자 프로그램");
		setSize(450, 520);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		// 윗부분 (사용자 로그인과 같은 모양)
		add(UiStyle.createLoginHeader("자동차 수리 접수 서비스", "관리자 로그인", 450));

		JLabel idLabel = UiStyle.createFieldLabel("관리자 아이디");
		idLabel.setBounds(50, 175, 350, 20);
		add(idLabel);

		jtfId = new JTextField();
		UiStyle.styleField(jtfId);
		jtfId.setBounds(50, 198, 350, 40);
		add(jtfId);

		JLabel pwLabel = UiStyle.createFieldLabel("관리자 비밀번호");
		pwLabel.setBounds(50, 252, 350, 20);
		add(pwLabel);

		jpfPass = new JPasswordField();
		UiStyle.styleField(jpfPass);
		jpfPass.setBounds(50, 275, 350, 40);
		add(jpfPass);

		jbtnLogin = new JButton("로그인");
		jbtnLogin.setBounds(50, 340, 350, 44);
		add(jbtnLogin);

		JLabel lblNotice = new JLabel("관리자 전용 페이지입니다.", SwingConstants.CENTER);
		lblNotice.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lblNotice.setForeground(UiStyle.DISABLED);
		lblNotice.setBounds(50, 402, 350, 20);
		add(lblNotice);

		UiStyle.apply(this);

		setVisible(true);

		AdminLoginFormEvt ale = new AdminLoginFormEvt(this);
		jbtnLogin.addActionListener(ale);
		jpfPass.addActionListener(ale);
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

}
