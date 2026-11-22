package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import admin.car_service.ReservCarServiceAdmin;
import admin.car_view.CarReservDialogFormAdmin;
import car_dto.CarReservDialogDTO;

public class CarReservDialogEvt implements ActionListener  {
	
	
	private CarReservDialogFormAdmin crdfa;
	
	private int isApprove;
	
	
	
	
	public CarReservDialogEvt(CarReservDialogFormAdmin crdfa) {
		
		this.crdfa = crdfa;
		//예약 상세정보를 가져와서 화면에서 보여줘야 한다
		showReq();
		
		
	}
	
	
	//생성자가 실행되자마자 화면에 해당 예약에 대한 정보를 표시 해야함
	public void showReq() {
		ReservCarServiceAdmin  rcsa = new ReservCarServiceAdmin();
		
		List<CarReservDialogDTO> source = null;
		try {
			source = rcsa.showUserRequest(crdfa.getReservCode());
			for(CarReservDialogDTO d: source) {
				crdfa.getTaRequest().setText(d.getAddtional_request());
				crdfa.getTfCustomerDate().setText(d.getUser_reserv_date());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}




	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		if(ae.getSource() == crdfa.getBtnConfirm()) {
			isApprove = 1;
			JOptionPane.showMessageDialog(crdfa, "확정하시겠습니까");
		}
		if(ae.getSource() == crdfa.getBtnCancel()) {
			isApprove = 2;
			JOptionPane.showMessageDialog(crdfa, "취소하시겠습니까");
		}
		
		if(ae.getSource() == crdfa.getBtnComplete()) {
			System.out.println("확인이다");
			
			reservDecision();
		}
		
		
	}
	
	
	public void reservDecision() {
		// 다이올로그에서 최종 결정한 날짜를 가져오고 확인을 누르면
		//예약을 확정 짓는다 
		//만약 취소 버튼을 눌렀다면 예약 취소로 상태 변경을 한다
		String confirmDate = crdfa.getTfConfirmDate().getText();
		ReservCarServiceAdmin rcsa = new ReservCarServiceAdmin();
		
		
		
		
		if(isApprove==2) {
			System.out.println("취소 쿼리 준비 완료");
			
			try {
				int result =  rcsa.cancelDialogReserv(crdfa.getReservCode());
				if(result==1) {
					JOptionPane.showMessageDialog(crdfa, "에약취소 완료");
					crdfa.dispose();
				}else {
					JOptionPane.showMessageDialog(crdfa, "예약취소 실패");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		else if(confirmDate==null|| "".equals(confirmDate)) {
			JOptionPane.showMessageDialog(crdfa, "정비일을 확정하세요");
		}
		
		//예약 날짜가 잡혀있고 예약확정 버튼을 누른경우에는  예약 상태를 업데이트한다
		else if(confirmDate != null && !confirmDate.isEmpty()&&isApprove==1) {
			
			
			
			System.out.println("예약확정 쿼리 준비 완료");
			try {
				int result =  rcsa.confirmfinalDate(confirmDate,crdfa.getReservCode());
				if(result==1) {
					JOptionPane.showMessageDialog(crdfa, "예약상태로 변경완료");
					crdfa.dispose();
				}else {
					JOptionPane.showMessageDialog(crdfa, "예약상태로 변경실패");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		
		
		
		
	}

}
