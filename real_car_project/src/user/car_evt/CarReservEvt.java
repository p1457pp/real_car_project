package user.car_evt;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import car_dto.CarReservFourColumnDTO;
import car_dto.CarReservUserUIDTO;
import user.car_service.ReservCarService;
import user.car_view.CarReservPanelUser;

public class CarReservEvt implements ActionListener,MouseListener {
	
	
	private CarReservPanelUser crp;
	private String userId="user001";
	


	public CarReservEvt(CarReservPanelUser crp) {
		
		this.crp = crp;
		crp.getLblUserName().setText(userId);
	}

	

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		//에약하기 버튼 인경우
		if(ae.getSource() == crp.getBtnReservation()) {
			addResrve();
		}
		
		//예약신청 화면을 누른경우
		if(ae.getSource()==crp.getJtbApply()) {
				showReservApply();
		}
		if(ae.getSource()==crp.getJtbCheck()) {
				showReservCheck();
		}
		
	}
	
	//예약확인 탭 누르면 보여야할 이벤트들
	private void showReservCheck() {
		// TODO Auto-generated method stub
		crp.getJtbApply().setBackground(Color.white);
		crp.getJtbCheck().setBackground(new Color(0, 140, 255));
		//에약체크 확인 화면(열려야댐)
		crp.getCl().show(crp.getJpCard(),"check");
		
		//하단 확인 탭을 누르면 기존 글자는 지우기
		crp.getTfReservationDate().setText("");
		crp.getCbRepairItem().setSelectedIndex(0);
		crp.getTaRequest().setText("");
		//예약확인 패널을 누르면 jtable에 예약한 데이터가 추가되야한다
		
		showReservDetail();
		
		
		
		
	}
	
	 public void tableButtonClicked(String actionType, int row) {
		 
		 DefaultTableModel dtm = crp.getDfm_check();
		 String rsCode = dtm.getValueAt(row,0).toString();
		 
		 
		 
		 
	 }
	
	
	//예약확인에서 예약정보하고 버튼 밑 승인 여부 및 취소 버튼도 여기서 함
	private void showReservDetail() {
		
		//테이블모델 가져오기
		DefaultTableModel dtmf = crp.getDfm_check();
		//처음에는 테이블 청소
		dtmf.setRowCount(0);
		ReservCarService rs = new ReservCarService();
		List<CarReservFourColumnDTO> crfDTO = null;
		//유저 아이디는 나중에 전역변수 참조해서 넣어야 한다
		try {
			crfDTO = rs.showDetailReservRecord(userId);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(crp, "조회실패");
			
			e.printStackTrace();
			return;
		}
		
		
		//에약번호,차량번호,차량종류,정비항목,정비허가,예약취소
		String[] row = new String[6];
		for(CarReservFourColumnDTO info:crfDTO) {
			row[0] = info.getReservation_code();
			row[1] = info.getCar_num();
			row[2] = info.getCar_model();
			row[3] = info.getRepair_item();
			row[4] = info.getReservation_permission();
			row[5] = "예약취소";
			dtmf.addRow(row);
			
		}
		
		
		
	}



	private void showReservApply() {
		// TODO Auto-generated method stub
		crp.getJtbApply().setBackground(new Color(0, 140, 255));
		crp.getJtbCheck().setBackground(Color.white);
		//예약신청화면 (기본)
		crp.getCl().show(crp.getJpCard(),"apply");
		
		DefaultTableModel dtm = crp.getDtm();
		dtm.setRowCount(0);
		
		ReservCarService rcs = new ReservCarService();
		
		drawReservApplyTable(rcs,dtm);
	}


	//ui에서 값들을 받아와서 서비스 로직으로 전달함
	public void addResrve() {
		
		DefaultTableModel dtm = crp.getDtm();
		dtm.setRowCount(0);
		
		//나중에 차량 번호하고 차종은 다른데서 받아온다
		System.out.println("버튼이 잘 눌리네여");
		//화면에서 입력한값 들 불러오기
		String reservedDate = crp.getTfReservationDate().getText().trim();
		//콤보박스에서 값꺼내기
		String repairPart =  (String)crp.getCbRepairItem().getSelectedItem();
		String additional_request = crp.getTaRequest().getText().trim();
		
		CarReservUserUIDTO cDTO = new CarReservUserUIDTO();
		cDTO.setReservDate(reservedDate);
		cDTO.setRepairPart(repairPart);
		cDTO.setAdd_request(additional_request);
		
		
		//서비스 로직에 값 전달
		ReservCarService rcs = new ReservCarService();
		boolean addResult=false;
		try {
			//여기서 유저 정보를 넘겨줘야 한다 
			addResult = rcs.addReserv(cDTO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("/"+reservedDate+"/"+"/"+repairPart+"/"+additional_request);
		
		
		if(!addResult) {
			JOptionPane.showMessageDialog(crp, "예약이 실패 했습니다");
			return;
		}
		JOptionPane.showMessageDialog(crp, "예약이 성공 했습니다");
		
		
		//jtable에 방금 내가 예약 성공한 내역을 뿌려줘야 한다.
		//유저의 정보는 임시로 여기서 사용 나중에 받아야한다 
		
		drawReservApplyTable(rcs,dtm);
		
		
		
		
	}
	
	public void drawReservApplyTable(ReservCarService rcs,DefaultTableModel dtm) {
		List<String[]> reservedRow = null;
		try {
			reservedRow  = rcs.showReservRecord(userId);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(String[] row : reservedRow) {
			System.out.println(row.toString());
			dtm.addRow(row);
		
		
		}
		
	}
	
	public void cancelReserv(MouseEvent e) {
		
		Point point =e.getPoint();
		
		JTable jtb = crp.getJt_check();
		
		//마우스 위치를 행과 열로 변환
		int row = jtb.rowAtPoint(point);
		int col = jtb.columnAtPoint(point);
		String rsCode = null;
		if(row >=0 && col == 5) {
			
			String reservStatus = jtb.getValueAt(row,4).toString();
			if("예약확정".equals(reservStatus)) {
				JOptionPane.showMessageDialog(jtb, "예약취소 불가 입니다");
				return;
			}
			
			 rsCode = jtb.getValueAt(row, 0).toString();
			 System.err.println(rsCode);
			 
			 int status = JOptionPane.showConfirmDialog(jtb, "정말로 예약을 삭제하실건가요?\n예약코드:"+ rsCode);
			 boolean resultDel = false;
			 
			 if(status !=  JOptionPane.YES_OPTION) {
				 return;
			 }
			 ReservCarService rs = new ReservCarService();
			 switch(status) {
			 case JOptionPane.YES_OPTION:
				 
				 //여기에 삭제하는 메서드 넣을것서비스로
				 //반환 값은 boolean으로 만약 취소 성공했다면 취소 했다고 알림죽
				 
				 try {
					 resultDel = rs.removeReserv(rsCode);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				 
				 
			 }
			 if(resultDel==true) {
				 showReservDetail();
				
						 
				 
				 
				 
				 JOptionPane.showMessageDialog(jtb, "삭제 성공!");
			 }else {
				 JOptionPane.showMessageDialog(jtb, "삭제 실패!"); 
			 }
			 
		}	 
			 
			
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == crp.getJt_check()) {
			cancelReserv(e);
		}
		
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
