package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import car_dto.UserDTO;
import user.UserMainTab;
import user.car_service.UserService;
import user.car_view.MyPageForm;

public class MyPageFormEvt implements ActionListener {

	private MyPageForm mpf;
	private UserMainTab umt;
	private String userId;
	private boolean editMode;

	public MyPageFormEvt(MyPageForm mpf, UserMainTab umt, String userId) {
		this.mpf = mpf;
		this.umt = umt;
		this.userId = userId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mpf.getJbtnModify()) {
			if (editMode) {
				modifyUser();
			} else {
				setEditMode(true);
			}
		}
	}

	/** 회원 정보를 조회해서 화면에 채운다. */
	public void setUserInfo() {
		UserDTO uDTO = new UserService().searchByUserId(userId);
		if (uDTO == null) {
			JOptionPane.showMessageDialog(mpf, "회원 정보를 불러오지 못했습니다.");
			return;
		}

		mpf.getJlblWelcome().setText(uDTO.getName() + "님, 환영합니다.");
		mpf.getJlblGrade().setText(uDTO.getGrade() + " 회원 (가입일 " + uDTO.getDate() + ")");
		mpf.getTxtId().setText(uDTO.getUserId());
		mpf.getTxtName().setText(uDTO.getName());
		mpf.getTxtEmail().setText(uDTO.getEmail());
		mpf.getTxtPhone().setText(uDTO.getPhone());

		if (umt != null) {
			umt.setUserName(uDTO.getName());
		}
	}

	private void setEditMode(boolean edit) {
		editMode = edit;
		mpf.getTxtName().setEditable(edit);
		mpf.getTxtEmail().setEditable(edit);
		mpf.getTxtPhone().setEditable(edit);
		mpf.getJbtnModify().setText(edit ? "수정완료" : "정보수정");
	}

	public void modifyUser() {
		String name = mpf.getTxtName().getText().trim();
		String email = mpf.getTxtEmail().getText().trim();
		String phone = mpf.getTxtPhone().getText().trim();

		if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
			JOptionPane.showMessageDialog(mpf, "이름, 이메일, 연락처를 모두 입력해주세요.");
			return;
		}

		int cnt = new UserService().modifyUser(new UserDTO(userId, null, name, email, phone));
		if (cnt == 1) {
			JOptionPane.showMessageDialog(mpf, "회원 정보가 수정되었습니다.");
			setUserInfo();
		} else {
			JOptionPane.showMessageDialog(mpf, "수정에 실패했습니다.");
			return;
		}
		setEditMode(false);
	}
}
