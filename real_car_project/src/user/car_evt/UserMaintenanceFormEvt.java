package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JOptionPane;

import car_dto.MaintenanceDTO;
import user.UserMainTab;
import user.car_service.UserMaintenanceService;
import user.car_view.UserMaintenanceDetailForm;
import user.car_view.UserMaintenanceForm;

public class UserMaintenanceFormEvt extends MouseAdapter implements ActionListener {

	private UserMaintenanceForm umf;
	private UserMainTab umt;
	private String userId;
	private UserMaintenanceService ums;
	private final DecimalFormat priceFormat = new DecimalFormat("#,###");

	public UserMaintenanceFormEvt(UserMaintenanceForm umf, UserMainTab umt, String userId) {
		this.umf = umf;
		this.umt = umt;
		this.userId = userId;
		this.ums = new UserMaintenanceService();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == umf.getJbtnSearch()) {
			searchByUserId(userId);
		}
		if (e.getSource() == umf.getJbtnDetail()) {
			openDetail();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == umf.getJtMaintenance()) {
			openDetail();
		}
	}

	/** 내 차량 콤보박스 채우기 */
	public void searchMyCars() {
		umf.getJcbCar().removeAllItems();
		umf.getJcbCar().addItem("전체 차량");
		for (String car : ums.searchMyCars(userId)) {
			umf.getJcbCar().addItem(car);
		}
	}

	/** 나의 정비 내역 (선택한 기간, 차량 조건 적용) */
	public void searchByUserId(String userId) {
		String period = (String) umf.getJcbPeriod().getSelectedItem();
		String car = (String) umf.getJcbCar().getSelectedItem();

		String carNum = "전체 차량";
		if (car != null && !"전체 차량".equals(car)) {
			int idx = car.indexOf("(");
			carNum = (idx > 0) ? car.substring(0, idx) : car;
		}

		umf.getDtmMaintenance().setRowCount(0);
		List<MaintenanceDTO> list = ums.searchByUserId(userId, period, carNum);
		for (MaintenanceDTO dto : list) {
			String statusText = dto.getStatusName();
			if (statusText == null || statusText.isEmpty()) {
				statusText = dto.getStatus() == 2 ? "정비완료" : (dto.getStatus() == 1 ? "정비중" : "대기");
			}
			Object[] row = {
				dto.getReportNo(),
				dto.getMaintDate(),
				dto.getCarNum(),
				dto.getCarModel(),
				dto.getSummaryItem(),
				priceFormat.format(dto.getTotalPrice()) + "원",
				statusText
			};
			umf.getDtmMaintenance().addRow(row);
		}
	}

	/** 정비 상세 명세서 다이얼로그 열기 */
	public void openDetail() {
		int row = umf.getJtMaintenance().getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(umf, "상세내역을 볼 정비 내역을 선택해주세요.");
			return;
		}

		int reportNo = Integer.parseInt(umf.getDtmMaintenance().getValueAt(row, 0).toString());
		new UserMaintenanceDetailForm(umt, reportNo);
	}
}
