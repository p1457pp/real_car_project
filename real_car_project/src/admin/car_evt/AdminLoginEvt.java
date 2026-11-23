package admin.car_evt;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import admin.car_service.AdminLoginService;
import admin.car_view.AdminLoginForm;
import admin.car_view.AdminMainForm;
import admin.car_view.AdminMemberManagementPanel;
import car_dto.AdminLoginDTO;


public class AdminLoginEvt extends WindowAdapter implements ActionListener {
	private AdminLoginForm alf;
	
	public AdminLoginEvt(AdminLoginForm alf) {
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
		
		//폼에서 아이디 얻기
				String id=alf.getJtfId().getText().trim();
				String pass=new String( alf.getJpfPass().getPassword()).trim();
				
				//아이디에 값이 있다면 커서를 비밀번호로 이동
				if( id.isEmpty() ) {
					return;
				}//end if
				
				alf.getJpfPass().requestFocus();
				
				//비밀번호에 값이 있다면 로그인 업무를 처리
				if( pass.isEmpty() ) {
					return;
				}//end if
			AdminLoginService als=new AdminLoginService();	
			
			AdminLoginDTO alDTO=new AdminLoginDTO(id,pass);
			if (!als.longinProcess(alDTO)) {
				JOptionPane.showMessageDialog(alf, "아이디 혹은 비밀번호를 다시 확인해주세요.");
				return;
			}
		
		new AdminMainForm();
		alf.dispose();
	}
	
}
