package user.car_view;

import car_util.UiStyle;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import user.car_evt.UserJoinFormEvt;

public class UserJoinForm extends JFrame {
	private JTextField txtId, txtName, txtEmail, txtPhone;
	private JPasswordField txtPw, txtPwConfirm;
	private JButton btnSubmit;

	public UserJoinForm() {
		setTitle("자동차 관리 - 회원 가입");
		setSize(450, 630);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(50, 25, 335, 535);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.setLayout(null);
		add(panel);

		JLabel lblTitle = new JLabel("회원 가입", SwingConstants.CENTER);
		lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTitle.setBounds(50, 15, 235, 30);
		panel.add(lblTitle);

		JLabel lblId = new JLabel("아이디");
		lblId.setBounds(50, 50, 235, 20);
		panel.add(lblId);

		txtId = new JTextField();
		txtId.setBounds(50, 70, 235, 25);
		panel.add(txtId);

		JLabel lblPw = new JLabel("비밀번호");
		lblPw.setBounds(50, 100, 235, 20);
		panel.add(lblPw);

		txtPw = new JPasswordField();
		txtPw.setBounds(50, 120, 235, 25);
		panel.add(txtPw);

		JLabel lblPwConf = new JLabel("비밀번호 확인");
		lblPwConf.setBounds(50, 150, 235, 20);
		panel.add(lblPwConf);

		txtPwConfirm = new JPasswordField();
		txtPwConfirm.setBounds(50, 170, 235, 25);
		panel.add(txtPwConfirm);

		JLabel lblName = new JLabel("이름");
		lblName.setBounds(50, 200, 235, 20);
		panel.add(lblName);

		txtName = new JTextField();
		txtName.setBounds(50, 220, 235, 25);
		panel.add(txtName);

		JLabel lblEmail = new JLabel("이메일");
		lblEmail.setBounds(50, 250, 235, 20);
		panel.add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setBounds(50, 270, 235, 25);
		panel.add(txtEmail);

		JLabel lblPhone = new JLabel("전화번호");
		lblPhone.setBounds(50, 300, 235, 20);
		panel.add(lblPhone);

		txtPhone = new JTextField();
		txtPhone.setBounds(50, 320, 235, 25);
		panel.add(txtPhone);

		btnSubmit = new JButton("가입하기");
		btnSubmit.setBackground(new Color(0, 122, 255));
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBounds(50, 365, 235, 40);
		panel.add(btnSubmit);

		UserJoinFormEvt evt = new UserJoinFormEvt(this);
		btnSubmit.addActionListener(evt);
		addWindowListener(evt);

		UiStyle.apply(this);

		setVisible(true);
	}

	public JTextField getTxtId() {
		return txtId;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public JTextField getTxtPhone() {
		return txtPhone;
	}

	public JPasswordField getTxtPw() {
		return txtPw;
	}

	public JPasswordField getTxtPwConfirm() {
		return txtPwConfirm;
	}

	public JButton getBtnSubmit() {
		return btnSubmit;
	}
}
