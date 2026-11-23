package car_evt;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;

import car_dto.SettlementDTO;
import car_service.SettlementService;
import car_view.SettlementPanel;

public class SettlementEvt implements ActionListener {
	private SettlementPanel sp;
	
	public SettlementEvt(SettlementPanel sp) {
		this.sp=sp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sp.getJbtnSearch()) {
			settle();
		}
	}
	
	private void settle() {
		String year = (String) sp.getJcbYear().getSelectedItem();
        String month = (String) sp.getJcbMonth().getSelectedItem();

        SettlementDTO sDTO = new SettlementService().searchMonthTotal(year + "-" + month);

        StringBuilder sb = new StringBuilder();
        sb.append("===== ").append(year).append("년 ").append(month).append("월 정산 =====\n\n");
        sb.append(String.format("정비 건수 : %d건%n", sDTO.getCnt()));
        sb.append(String.format("정산 금액 : %,d원%n", sDTO.getTotal()));

        sp.getJta().setText(sb.toString());
    }
	
}
