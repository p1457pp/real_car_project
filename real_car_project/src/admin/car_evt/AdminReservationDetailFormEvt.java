package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import admin.car_service.AdminReservationService;
import admin.car_view.AdminReservationDetailForm;
import car_dto.CarDTO;
import car_dto.MaintenanceDTO;
import car_dto.ReservationDTO;

public class AdminReservationDetailFormEvt extends WindowAdapter implements ActionListener {

	private AdminReservationDetailForm ardf;
	private AdminReservationService ars;
	private ReservationDTO rDTO;
	private int isApprove; // 1: 확정, 2: 취소

	public AdminReservationDetailFormEvt(AdminReservationDetailForm ardf) {
		this.ardf = ardf;
		this.ars = new AdminReservationService();
		showDetail();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		ardf.dispose();
	}

	//생성자가 실행되자마자 화면에 해당 예약에 대한 정보를 표시 해야함
	public void showDetail() {
		try {
			rDTO = ars.searchReservationDetail(ardf.getReservCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rDTO == null) {
			ardf.getJtaDetail().setText("예약 정보를 불러오지 못했습니다.");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(rDTO.getAdditionalRequest() == null ? "" : rDTO.getAdditionalRequest());
		sb.append(searchCarInfoByVehicleId(rDTO.getVehicleId()));
		sb.append(searchMaintenanceInfoByVehicleId(rDTO.getVehicleId()));

		ardf.getJtaDetail().setText(sb.toString());
		ardf.getJtaDetail().setEditable(false);
		ardf.getJtaDetail().setCaretPosition(0);

		ardf.getJtfWishDate().setText(
				rDTO.getPreferredReservationDate() == null ? "" : rDTO.getPreferredReservationDate().toString());
		ardf.getJtfWishDate().setEditable(false);
		if (rDTO.getConfirmedDate() != null) {
			ardf.getJtfConfirmDate().setText(rDTO.getConfirmedDate().toString());
		}
	}

	/** 특정 차량 정보 (상세 영역에 덧붙일 문자열) */
	public String searchCarInfoByVehicleId(int vehicleId) {
		CarDTO car = ars.searchCarInfoByVehicleId(vehicleId);
		if (car == null) {
			return "\n[차량] " + (rDTO.getCarNum() == null ? "-" : rDTO.getCarNum()) + " "
					+ (rDTO.getCarModel() == null ? "" : rDTO.getCarModel());
		}
		return "\n[차량] " + car.getCarNum() + " " + car.getCarModel() + " (" + car.getCarYear() + "년식)";
	}

	/** 특정 차량 정비정보 (상세 영역에 덧붙일 문자열) */
	public String searchMaintenanceInfoByVehicleId(int vehicleId) {
		List<MaintenanceDTO> list = ars.searchMaintenanceInfoByVehicleId(vehicleId);
		StringBuilder sb = new StringBuilder("\n[정비이력] " + list.size() + "건");
		for (MaintenanceDTO m : list) {
			sb.append("\n  - ").append(m.getMaintDate()).append(" ").append(m.getSummaryItem()).append(" (")
					.append(m.getStatusName()).append(")");
		}
		return sb.toString();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == ardf.getJbtnConfirm()) {
			isApprove = 1;
			JOptionPane.showMessageDialog(ardf, "확정할 정비일을 입력하고 [완료]를 눌러주세요.");
		}
		if (ae.getSource() == ardf.getJbtnCancel()) {
			isApprove = 2;
			JOptionPane.showMessageDialog(ardf, "[완료]를 누르면 예약이 취소됩니다.");
		}
		if (ae.getSource() == ardf.getJbtnComplete()) {
			if (isApprove == 2) {
				changeStatus("cancel");
			} else if (isApprove == 1) {
				changeStatus("confirm");
			} else {
				JOptionPane.showMessageDialog(ardf, "예약확정 또는 예약취소를 먼저 선택하세요.");
			}
		}
	}

	public void changeStatus(String type) {
		// 다이올로그에서 최종 결정한 날짜를 가져오고 확인을 누르면 예약을 확정 짓는다
		// 만약 취소 버튼을 눌렀다면 예약 취소로 상태 변경을 한다
		String confirmDate = ardf.getJtfConfirmDate().getText();
		if ("cancel".equals(type)) {
			try {
				int result = ars.cancelReservation(ardf.getReservCode());
				if (result == 1) {
					JOptionPane.showMessageDialog(ardf, "에약취소 완료");
					ardf.dispose();
				} else {
					JOptionPane.showMessageDialog(ardf, "예약취소 실패");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (confirmDate == null || "".equals(confirmDate)) {
			JOptionPane.showMessageDialog(ardf, "정비일을 확정하세요");
		}
		//예약 날짜가 잡혀있고 예약확정 버튼을 누른경우에는  예약 상태를 업데이트한다
		else if (confirmDate != null && !confirmDate.isEmpty() && "confirm".equals(type)) {
			try {
				int result = ars.changeStatus(confirmDate, ardf.getReservCode());
				if (result == 1) {
					JOptionPane.showMessageDialog(ardf, "예약상태로 변경완료");
					ardf.dispose();
				} else {
					JOptionPane.showMessageDialog(ardf, "예약상태로 변경실패");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
