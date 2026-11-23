package admin.car_view;

import javax.swing.JFrame;

public class CarReservFormAdmin extends JFrame {
	
	public CarReservFormAdmin()  
	{
		
		
		
		setBounds(30,30,800,650);
		setTitle("관리자 예약 검색 폼");
		
		
		add(new CarReservPanelAdmin());
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
