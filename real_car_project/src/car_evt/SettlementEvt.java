package car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
	}
}
