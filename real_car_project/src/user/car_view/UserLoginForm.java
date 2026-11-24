package user.car_view;

import car_util.UiStyle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import user.car_evt.UserLoginFormEvt;

public class UserLoginForm extends JFrame {
	private JTextField txtId;
	private JPasswordField txtPw;
	private JButton btnLogin, btnRegister;

	public UserLoginForm() {
		setTitle("자동차 관리 - 로그인 페이지");
		setSize(450, 520);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		// 윗부분 (관리자 로그인과 같은 모양)
		add(UiStyle.createLoginHeader("자동차 수리 접수 서비스", "사용자 로그인", 450));

		JLabel lblId = UiStyle.createFieldLabel("아이디");
		lblId.setBounds(50, 175, 350, 20);
		add(lblId);

		txtId = new JTextField();
		UiStyle.styleField(txtId);
		txtId.setBounds(50, 198, 350, 40);
		add(txtId);

		JLabel lblPw = UiStyle.createFieldLabel("비밀번호");
		lblPw.setBounds(50, 252, 350, 20);
		add(lblPw);

		txtPw = new JPasswordField();
		UiStyle.styleField(txtPw);
		txtPw.setBounds(50, 275, 350, 40);
		add(txtPw);

		btnLogin = new JButton("로그인");
		btnLogin.setBounds(50, 340, 350, 44);
		add(btnLogin);

		btnRegister = new JButton("회원가입");
		btnRegister.setBounds(50, 396, 350, 36);
		add(btnRegister);

		UserLoginFormEvt evt = new UserLoginFormEvt(this);
		btnLogin.addActionListener(evt);
		btnRegister.addActionListener(evt);
		txtPw.addActionListener(evt);

		UiStyle.apply(this);

		// 회원가입은 보조 버튼 : 흰 바탕 + 파란 테두리
		UiStyle.styleOutlineButton(btnRegister);

		setVisible(true);
	}

	public JTextField getTxtId() {
		return txtId;
	}

	public JPasswordField getTxtPw() {
		return txtPw;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}
}
