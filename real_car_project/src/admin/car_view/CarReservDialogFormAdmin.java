package admin.car_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import admin.car_evt.CarReservDialogEvt;

public class CarReservDialogFormAdmin extends JDialog {

	 	private JTextArea taRequest;

	    private JTextField tfCustomerDate;
	    private JTextField tfConfirmDate;

	    private JButton btnConfirm;
	    private JButton btnCancel;
	    private JButton btnComplete;
	    
	    private String reservCode;
	    
	    
	    public CarReservDialogFormAdmin(String reservCode) {
	    	
	    	this.reservCode = reservCode;
	    	
	    	
	    	
	        // =========================
	        // 전체 JDialog 설정
	        // =========================

	        setTitle("예약 상세정보");
	        setSize(620, 500);
	        setLayout(new BorderLayout(10, 0));
	        setBackground(Color.WHITE);

	        setLocationRelativeTo(null);
	        setModal(true);
	        


	        // =========================
	        // 왼쪽 패널 : 요청사항
	        // =========================

	        JPanel leftPanel = new JPanel(null);
	        leftPanel.setBackground(Color.WHITE);

	        // 요청사항 JTextArea

	        taRequest = new JTextArea("상세정비 내용");
	        taRequest.setBorder(BorderFactory.createLineBorder(Color.black,1));

	        taRequest.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
	        taRequest.setLineWrap(true);
	        taRequest.setWrapStyleWord(true);

	        taRequest.setBounds(20, 65, 280, 340);

	        leftPanel.add(taRequest);


	        // =========================
	        // 오른쪽 패널
	        // =========================

	        JPanel rightPanel = new JPanel(null);
	        rightPanel.setBackground(Color.WHITE);


	        // 고객 희망일 JLabel

	        JLabel lblCustomerDate = createLabel("고객 희망일");
	        lblCustomerDate.setBorder(BorderFactory.createLineBorder(Color.black,1));
	        lblCustomerDate.setBounds(15, 90, 100, 42);

	        rightPanel.add(lblCustomerDate);


	        // 고객 희망일 JTextField

	        tfCustomerDate = new JTextField();
	        tfCustomerDate.setBounds(130, 90, 130, 42);

	        rightPanel.add(tfCustomerDate);


	        // 정비 확정일 JLabel

	        JLabel lblConfirmDate = createLabel("정비확정일");
	        lblConfirmDate.setBorder(BorderFactory.createLineBorder(Color.black,1));
	        lblConfirmDate.setBounds(15, 165, 100, 42);

	        rightPanel.add(lblConfirmDate);


	        // 정비 확정일 JTextField

	        tfConfirmDate = new JTextField();
	        tfConfirmDate.setBounds(130, 165, 130, 42);

	        rightPanel.add(tfConfirmDate);


	        // 예약 확정 버튼

	        btnConfirm = createButton("예약확정");
	        btnConfirm.setBorder(BorderFactory.createLineBorder(Color.black,1));
	        btnConfirm.setBounds(15, 245, 100, 45);

	        rightPanel.add(btnConfirm);


	        // 예약 취소 버튼

	        btnCancel = createButton("예약취소");
	        btnCancel.setBorder(BorderFactory.createLineBorder(Color.black,1));
	        btnCancel.setBounds(130, 245, 100, 45);

	        rightPanel.add(btnCancel);


	        // 완료 버튼

	        btnComplete = createButton("완료");
	        btnComplete.setBorder(BorderFactory.createLineBorder(Color.black,1));
	        btnComplete.setBounds(85, 365, 120, 75);

	        rightPanel.add(btnComplete);


	        // =========================
	        // 전체 패널 배치
	        // =========================

	        leftPanel.setPreferredSize(new Dimension(310, 0));

	        add(leftPanel, BorderLayout.WEST);
	        add(rightPanel, BorderLayout.CENTER);
	        
	        //액션리스너 연결하기
	          
	         CarReservDialogEvt crde = new CarReservDialogEvt(this);
	         btnConfirm.addActionListener(crde);;
		     btnCancel.addActionListener(crde);;
		     btnComplete.addActionListener(crde);
	        
	        
			

	    }


	    // =========================
	    // JLabel 생성 메서드
	    // =========================

	    private JLabel createLabel(String text) {

	        JLabel label = new JLabel(text, SwingConstants.CENTER);

	        label.setOpaque(true);
	        label.setBackground(new Color(0, 140, 255));
	        label.setForeground(Color.WHITE);
	        label.setFont(new Font("맑은 고딕", Font.BOLD, 14));

	        return label;
	    }


	    // =========================
	    // JButton 생성 메서드
	    // =========================

	    private JButton createButton(String text) {

	        JButton button = new JButton(text);

	        button.setBackground(new Color(0, 140, 255));
	        button.setForeground(Color.WHITE);
	        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
	        button.setFocusPainted(false);

	        return button;
	    }


		public JTextArea getTaRequest() {
			return taRequest;
		}


		public void setTaRequest(JTextArea taRequest) {
			this.taRequest = taRequest;
		}


		public JTextField getTfCustomerDate() {
			return tfCustomerDate;
		}


		public void setTfCustomerDate(JTextField tfCustomerDate) {
			this.tfCustomerDate = tfCustomerDate;
		}


		public JTextField getTfConfirmDate() {
			return tfConfirmDate;
		}


		public void setTfConfirmDate(JTextField tfConfirmDate) {
			this.tfConfirmDate = tfConfirmDate;
		}


		public JButton getBtnConfirm() {
			return btnConfirm;
		}


		public void setBtnConfirm(JButton btnConfirm) {
			this.btnConfirm = btnConfirm;
		}


		public JButton getBtnCancel() {
			return btnCancel;
		}


		public void setBtnCancel(JButton btnCancel) {
			this.btnCancel = btnCancel;
		}


		public JButton getBtnComplete() {
			return btnComplete;
		}


		public void setBtnComplete(JButton btnComplete) {
			this.btnComplete = btnComplete;
		}


		public String getReservCode() {
			return reservCode;
		}


		public void setReservCode(String reservCode) {
			this.reservCode = reservCode;
		}


	    // =========================
	    // Getter
	    // =========================
	    
	    
	   
	

	
	
	

}
