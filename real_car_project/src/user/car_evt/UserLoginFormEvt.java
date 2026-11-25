package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import car_dto.UserDTO;
import user.UserMainTab;
import user.car_service.UserService;
import user.car_view.UserJoinForm;
import user.car_view.UserLoginForm;

public class UserLoginFormEvt implements ActionListener {

	private UserLoginForm ulf;

	public UserLoginFormEvt(UserLoginForm ulf) {
		this.ulf = ulf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ulf.getBtnRegister()) {
			new UserJoinForm();
			ulf.dispose();
			return;
		}

		// 로그인 버튼 또는 비밀번호 입력창에서 Enter
		loginProcess();
	}

	public void loginProcess() {
		String id = ulf.getTxtId().getText().trim();
		String pw = new String(ulf.getTxtPw().getPassword());

		if (id.isEmpty() || pw.isEmpty()) {
			JOptionPane.showMessageDialog(ulf, "아이디와 비밀번호를 입력해주세요.");
			return;
		}

		String loginId = new UserService().userLogin(new UserDTO(id, pw, null, null, null));
		if (loginId == null || loginId.isEmpty()) {
			JOptionPane.showMessageDialog(ulf, "아이디 또는 비밀번호가 일치하지 않습니다.");
			return;
		}

		JOptionPane.showMessageDialog(ulf, "로그인 성공!");

		new UserMainTab(loginId);
		ulf.dispose();
	}
}
