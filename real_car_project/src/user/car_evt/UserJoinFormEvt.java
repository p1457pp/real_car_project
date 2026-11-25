package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import car_dto.UserDTO;
import user.car_service.UserService;
import user.car_view.UserJoinForm;
import user.car_view.UserLoginForm;

public class UserJoinFormEvt extends WindowAdapter implements ActionListener {

	private UserJoinForm ujf;

	public UserJoinFormEvt(UserJoinForm ujf) {
		this.ujf = ujf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		joinProcess();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// 가입창을 닫으면 로그인 화면으로 복귀
		new UserLoginForm();
		ujf.dispose();
	}

	public void joinProcess() {
		String id = ujf.getTxtId().getText().trim();
		String pw = new String(ujf.getTxtPw().getPassword());
		String pwConf = new String(ujf.getTxtPwConfirm().getPassword());
		String name = ujf.getTxtName().getText().trim();
		String email = ujf.getTxtEmail().getText().trim();
		String phone = ujf.getTxtPhone().getText().trim();

		if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
			JOptionPane.showMessageDialog(ujf, "모든 항목을 입력해주세요.");
			return;
		}

		if (!pw.equals(pwConf)) {
			JOptionPane.showMessageDialog(ujf, "비밀번호가 일치하지 않습니다.");
			return;
		}

		int cnt = new UserService().addUser(new UserDTO(id, pw, name, email, phone));
		switch (cnt) {
		case 1:
			JOptionPane.showMessageDialog(ujf, "회원가입이 완료되었습니다!");
			break;
		default:
			JOptionPane.showMessageDialog(ujf, "회원가입 실패 (아이디 중복 등을 확인해주세요).");
			return;
		}

		new UserLoginForm();
		ujf.dispose();
	}
}
