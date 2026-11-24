package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import admin.car_service.AdminMaintenanceService;
import admin.car_view.AdminMaintenanceRevenueForm;
import car_dto.SettlementDTO;

public class AdminMaintenanceRevenueFormEvt implements ActionListener {

	private AdminMaintenanceRevenueForm amrf;
	private AdminMaintenanceService ams;

	public AdminMaintenanceRevenueFormEvt(AdminMaintenanceRevenueForm amrf) {
		this.amrf = amrf;
		this.ams = new AdminMaintenanceService();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == amrf.getJbtnSearch()) {
			searchAllRevenue();
		}
	}

	/** 선택한 년/월의 정비 건수와 정산 금액을 보여준다. */
	public void searchAllRevenue() {
		String year = (String) amrf.getJcbYear().getSelectedItem();
		String month = (String) amrf.getJcbMonth().getSelectedItem();

		SettlementDTO sDTO = ams.searchAllRevenue(year + "-" + month);

		StringBuilder sb = new StringBuilder();
		sb.append("===== ").append(year).append("년 ").append(month).append("월 정산 =====\n\n");
		sb.append(String.format("정비 건수 : %d건%n", sDTO.getCnt()));
		sb.append(String.format("정산 금액 : %,d원%n", sDTO.getTotal()));

		amrf.getJta().setText(sb.toString());
	}
}
