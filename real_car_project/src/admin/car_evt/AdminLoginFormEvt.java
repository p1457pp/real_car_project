package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import admin.AdminMainTab;
import admin.car_service.AdminService;
import admin.car_view.AdminLoginForm;
import car_dto.AdminDTO;

public class AdminLoginFormEvt extends WindowAdapter implements ActionListener {
	private AdminLoginForm alf;

	public AdminLoginFormEvt(AdminLoginForm alf) {
		this.alf = alf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		loginProcess();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		alf.dispose();
	}

	public void loginProcess() {
		String id = alf.getJtfId().getText().trim();
		String pass = new String(alf.getJpfPass().getPassword()).trim();

		if (id.isEmpty()) {
			return;
		}

		alf.getJpfPass().requestFocus();

		if (pass.isEmpty()) {
			return;
		}

		AdminService as = new AdminService();
		AdminDTO aDTO = new AdminDTO(id, pass);
		if (!as.adminLogin(aDTO)) {
			JOptionPane.showMessageDialog(alf, "아이디 혹은 비밀번호를 다시 확인해주세요.");
			return;
		}

		new AdminMainTab();
		alf.dispose();
	}
}
