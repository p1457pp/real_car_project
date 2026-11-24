package user.car_view;

import car_util.UiStyle;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import user.UserMainTab;
import user.car_evt.MyPageFormEvt;

public class MyPageForm extends JPanel {

	private JLabel jlblTitle, jlblWelcome, jlblGrade;
	private JTextField txtId, txtName, txtEmail, txtPhone;
	private JButton jbtnModify;

	public MyPageForm(UserMainTab umt, String userId) {
		setLayout(null);

		jlblTitle = new JLabel("회원정보");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		// 프로필 카드
		JPanel profileCard = new JPanel(null);
		profileCard.setBounds(10, 50, 740, 75);
		profileCard.setBackground(new Color(230, 240, 250));
		profileCard.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 240)));
		add(profileCard);

		JLabel lblImg = new JLabel("👤", SwingConstants.CENTER);
		lblImg.setFont(new Font("SansSerif", Font.PLAIN, 28));
		lblImg.setBounds(15, 15, 45, 45);
		profileCard.add(lblImg);

		jlblWelcome = new JLabel();
		jlblWelcome.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		jlblWelcome.setBounds(70, 15, 400, 22);
		profileCard.add(jlblWelcome);

		jlblGrade = new JLabel();
		jlblGrade.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		jlblGrade.setForeground(Color.DARK_GRAY);
		jlblGrade.setBounds(70, 40, 400, 20);
		profileCard.add(jlblGrade);

		int startY = 145;
		int height = 45;

		add(createRowLabel("아이디", 10, startY, 120, height));
		txtId = createField(130, startY, 620, height);
		add(txtId);

		add(createRowLabel("이름", 10, startY + height, 120, height));
		txtName = createField(130, startY + height, 620, height);
		add(txtName);

		add(createRowLabel("이메일", 10, startY + height * 2, 120, height));
		txtEmail = createField(130, startY + height * 2, 620, height);
		add(txtEmail);

		add(createRowLabel("연락처", 10, startY + height * 3, 120, height));
		txtPhone = createField(130, startY + height * 3, 620, height);
		add(txtPhone);

		jbtnModify = new JButton("정보수정");
		jbtnModify.setBounds(340, 545, 100, 30);
		add(jbtnModify);

		MyPageFormEvt evt = new MyPageFormEvt(this, umt, userId);
		jbtnModify.addActionListener(evt);
		evt.setUserInfo();

		UiStyle.apply(this);
	}

	private JLabel createRowLabel(String text, int x, int y, int width, int height) {
		JLabel lbl = new JLabel(text, SwingConstants.CENTER);
		lbl.setBounds(x, y, width, height);
		lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		lbl.setOpaque(true);
		lbl.setBackground(new Color(245, 245, 245));
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		return lbl;
	}

	private JTextField createField(int x, int y, int width, int height) {
		JTextField tf = new JTextField();
		tf.setBounds(x, y, width, height);
		tf.setEditable(false);
		tf.setBackground(Color.WHITE);
		tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		tf.setHorizontalAlignment(JTextField.CENTER);
		return tf;
	}

	public JLabel getJlblWelcome() {
		return jlblWelcome;
	}

	public JLabel getJlblGrade() {
		return jlblGrade;
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

	public JButton getJbtnModify() {
		return jbtnModify;
	}
}
