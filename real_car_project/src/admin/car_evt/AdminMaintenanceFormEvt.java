package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JOptionPane;

import admin.AdminMainTab;
import admin.car_service.AdminMaintenanceService;
import admin.car_view.AdminMaintenanceDetailForm;
import admin.car_view.AdminMaintenanceForm;
import car_dto.MaintenanceDTO;

public class AdminMaintenanceFormEvt extends MouseAdapter implements ActionListener {

	private AdminMaintenanceForm amf;
	private AdminMainTab amt;
	private AdminMaintenanceService ams;
	private final DecimalFormat priceFormat = new DecimalFormat("#,###");

	public AdminMaintenanceFormEvt(AdminMaintenanceForm amf, AdminMainTab amt) {
		this.amf = amf;
		this.amt = amt;
		this.ams = new AdminMaintenanceService();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == amf.getJbtnSearch() || e.getSource() == amf.getJtfKeyword()) {
			searchAllMaintenance();
		}
		if (e.getSource() == amf.getJbtnAdd()) {
			openDetail(0);
		}
		if (e.getSource() == amf.getJbtnInfoDetail()) {
			openSelectedDetail();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == amf.getJtMaintenance()) {
			openSelectedDetail();
		}
	}

	/** 모든 사용자 정비 내역 (기간/키워드 조건 적용) */
	public void searchAllMaintenance() {
		String start = amf.getJtfStart().getText().trim();
		String end = amf.getJtfEnd().getText().trim();
		String keyword = amf.getJtfKeyword().getText().trim();

		amf.getDtmMaintenance().setRowCount(0);
		List<MaintenanceDTO> list = ams.searchAllMaintenance(start, end, keyword);
		for (MaintenanceDTO dto : list) {
			String statusText = dto.getStatusName();
			if (statusText == null || statusText.isEmpty()) {
				statusText = dto.getStatus() == 2 ? "정비완료" : (dto.getStatus() == 1 ? "정비중" : "대기");
			}
			Object[] row = {
				dto.getReportNo(),
				dto.getMaintDate(),
				dto.getUserName() != null ? dto.getUserName() : "-",
				dto.getCarNum() != null ? dto.getCarNum() : "-",
				dto.getSummaryItem(),
				priceFormat.format(dto.getTotalPrice()) + "원",
				statusText
			};
			amf.getDtmMaintenance().addRow(row);
		}
	}

	private void openSelectedDetail() {
		int row = amf.getJtMaintenance().getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(amf, "상세내역을 볼 정비 내역을 선택해주세요.");
			return;
		}
		openDetail(Integer.parseInt(amf.getDtmMaintenance().getValueAt(row, 0).toString()));
	}

	/**
	 * 디테일 다이얼로그 열기
	 * 
	 * @param reportNo 0 이하면 신규 정비 등록
	 */
	public void openDetail(int reportNo) {
		new AdminMaintenanceDetailForm(amt, reportNo);
		// 다이얼로그가 닫히면 목록 갱신
		searchAllMaintenance();
	}
}
