package user.car_view;

import javax.swing.JFrame;

public class CarReservForm extends JFrame {
	
	public CarReservForm()  
	{
		
		
		
		setBounds(30,30,800,650);
		setTitle("예약패널 만들기");
		
		
		add(new CarReservPanelUser());
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
