package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import admin.AdminMainTab;
import admin.car_service.AdminCarService;
import admin.car_view.AdminCarEditForm;
import admin.car_view.AdminCarListForm;
import car_dto.CarDTO;


public class AdminCarListFormEvt implements ActionListener {

	private AdminCarListForm aclf;
	private AdminMainTab amt;

	public static final int VEHICLE_ID = 0; // 테이블(DTM)에서 차량 ID가 위치한 열(column) 인덱스
	public AdminCarListFormEvt(AdminCarListForm aclf, AdminMainTab amt) {
		this.aclf = aclf;
		this.amt = amt;
		searchAllCar(); // JTable에 값 셋팅
	}// AdminCarListFormEvt

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == aclf.getJbtnModify()) {
			openEdit();
		} // end if

		if (ae.getSource() == aclf.getJbtnDeleteState()) {
			updateDeleteState();
		} // end if
	}// actionPerformed
	/**
	 * 모든 회원의 차량을 조회하여 JTable에 불러오는
	 */
	public void searchAllCar() {
		AdminCarService acs = new AdminCarService(); // acs연결
		List<CarDTO> listCar = acs.searchAllCar(); // DB에 있는 모든 차량을 list로 가져온다

		DefaultTableModel dtmCar = aclf.getDtmCar(); // aclf의 DTM 그릇정보를 가져온다
		dtmCar.setRowCount(0); // JTable안에 기존의 행을삭제 : 초기화

		// listCar에 저장된 carDTO의 현재정보를 DTM의 담는다
		for (CarDTO cDTO : listCar) { 
			dtmCar.addRow(
					new Object[] /* int와 string둘다 담기위한 object 배열 선언 */ { 
					cDTO.getVehicleId(), // 차량ID
					cDTO.getCarNum(), // 차량번호
					cDTO.getCarModel(), // 차종
					cDTO.getManufacturerName(), // 제조사
					cDTO.getCarYear(), // 연식
					cDTO.getUserId() // 회원ID
			});
		} // end for
	}// searchAllCar
	/**
	 * 선택한 차량을 삭제(is_deleted = 'Y')하고 JTable을 갱신하는 일
	 */
	public void updateDeleteState() {
		int row = aclf.getJtCar().getSelectedRow(); // 선택한 행이 몇번째행인지 알기위해 변수로 담는다

		if (row == -1) { // 선택된 행이 없을 때
			JOptionPane.showMessageDialog(aclf, "삭제할 차량을 선택해주세요.");
			return;
		} // end if

		// 유저가 선택한 DTM의 행번호가 들어간다 
		int vehicleId = Integer.parseInt(aclf.getDtmCar().getValueAt(row, VEHICLE_ID).toString());

		if (JOptionPane.showConfirmDialog(aclf, "선택한 차량을 삭제하시겠습니까?", "삭제 확인",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		} // end if

		AdminCarService acs = new AdminCarService(); // acs 연결
		if (acs.updateCarDeleteState(vehicleId)) { // 선택한 행의 차량정보를 acs에서 삭제한다
			JOptionPane.showMessageDialog(aclf, "삭제되었습니다.");
			searchAllCar(); // 삭제를 하고난뒤 다시 조회한다
		} else {
			JOptionPane.showMessageDialog(aclf, "삭제에 실패했습니다.");
		} // end if
	}// removeCar

	/**
	 * 선택한 차량의 수정 창을 여는 일
	 */
	public void openEdit() {
		int row = aclf.getJtCar().getSelectedRow(); // 유저가 선택한 행의 번호

		if (row == -1) { // 선택한 행이 없을 때
			JOptionPane.showMessageDialog(aclf, "수정할 차량을 선택해주세요.");
			return;
		} // end if

		// 유저가 선택한 DTM의 행번호가 들어간다 
		int vehicleId = Integer.parseInt(aclf.getDtmCar().getValueAt(row, VEHICLE_ID).toString());

		AdminCarEditForm acef = new AdminCarEditForm(amt, vehicleId);
		acef.setVisible(true); // modal 다이얼로그: 닫힐 때까지 대기
		searchAllCar(); // 수정 창이 닫히면 최신 데이터로 갱신
	}// openEdit

}// class
