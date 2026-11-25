package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.List;

import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import user.car_service.UserMaintenanceService;
import user.car_view.UserMaintenanceDetailForm;

public class UserMaintenanceDetailFormEvt extends WindowAdapter implements ActionListener {

	private UserMaintenanceDetailForm umdf;
	private UserMaintenanceService ums;
	private final DecimalFormat priceFormat = new DecimalFormat("#,###");

	public UserMaintenanceDetailFormEvt(UserMaintenanceDetailForm umdf, int reportNo) {
		this.umdf = umdf;
		this.ums = new UserMaintenanceService();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		umdf.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == umdf.getJbtnClose()) {
			umdf.dispose();
		}
	}

	/** 정비 상세 내역 조회 */
	public void searchMaintenanceDetail(int reportNo) {
		MaintenanceDTO mDTO = ums.searchMaintenanceDetail(reportNo);
		if (mDTO != null) {
			umdf.getJlblMaintenanceDate().setText(nvl(mDTO.getMaintDate()));
			umdf.getJlblCenter().setText(nvl(mDTO.getCenterName()));
			umdf.getJlblCarNum().setText(nvl(mDTO.getCarNum()));
			umdf.getJlblCarModel().setText(nvl(mDTO.getCarModel()));
			umdf.getJlblStatus().setText(mDTO.getStatus() >= 1 ? "정비완료" : "대기");
			umdf.getJtaComment().setText(mDTO.getMechanicMemo() == null ? "" : mDTO.getMechanicMemo());
		}

		umdf.getDtmDetail().setRowCount(0);
		List<MaintenanceDetailDTO> details = ums.searchMaintenanceItems(reportNo);
		for (MaintenanceDetailDTO d : details) {
			umdf.getDtmDetail().addRow(new Object[] { d.getItemName(), priceFormat.format(d.getItemPrice()) + "원",
					d.getQty(), priceFormat.format(d.getLabCost()) + "원",
					priceFormat.format(d.getTotalPrice()) + "원" });
		}

		if (mDTO != null) {
			mDTO.setMaintenanceInfo(details);
		}
		calcTotal(mDTO);
	}

	/** 총 청구 금액 : 원본처럼 보고서에 저장된 총액을 표시한다. */
	public void calcTotal(MaintenanceDTO mDTO) {
		int total = (mDTO == null) ? 0 : mDTO.getTotalPrice();
		umdf.getJlblTotal().setText("총 청구 금액 : " + priceFormat.format(total) + "원");
	}

	private String nvl(String s) {
		return s == null ? "-" : s;
	}
}
