package admin.car_view;

import car_util.UiStyle;

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

import admin.car_evt.AdminReservationDetailFormEvt;

public class AdminReservationDetailForm extends JDialog {

	private JTextArea jtaDetail;
	private JTextField jtfWishDate;
	private JTextField jtfConfirmDate;

	private JButton jbtnConfirm;
	private JButton jbtnCancel;
	private JButton jbtnComplete;

	private String reservCode;

	public AdminReservationDetailForm(String reservCode) {
		this.reservCode = reservCode;

		setTitle("예약 상세정보");
		setSize(620, 500);
		setLayout(new BorderLayout(10, 0));
		setBackground(Color.WHITE);

		setLocationRelativeTo(null);
		setModal(true);

		JPanel leftPanel = new JPanel(null);
		leftPanel.setBackground(Color.WHITE);

		jtaDetail = new JTextArea("상세정비 내용");
		jtaDetail.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		jtaDetail.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		jtaDetail.setLineWrap(true);
		jtaDetail.setWrapStyleWord(true);
		jtaDetail.setBounds(20, 65, 280, 340);
		leftPanel.add(jtaDetail);

		JPanel rightPanel = new JPanel(null);
		rightPanel.setBackground(Color.WHITE);

		JLabel lblCustomerDate = createLabel("고객 희망일");
		lblCustomerDate.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		lblCustomerDate.setBounds(15, 90, 100, 42);
		rightPanel.add(lblCustomerDate);

		jtfWishDate = new JTextField();
		jtfWishDate.setBounds(130, 90, 130, 42);
		rightPanel.add(jtfWishDate);

		JLabel lblConfirmDate = createLabel("정비확정일");
		lblConfirmDate.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		lblConfirmDate.setBounds(15, 165, 100, 42);
		rightPanel.add(lblConfirmDate);

		jtfConfirmDate = new JTextField();
		jtfConfirmDate.setBounds(130, 165, 130, 42);
		rightPanel.add(jtfConfirmDate);

		jbtnConfirm = createButton("예약확정");
		jbtnConfirm.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		jbtnConfirm.setBounds(15, 245, 100, 45);
		rightPanel.add(jbtnConfirm);

		jbtnCancel = createButton("예약취소");
		jbtnCancel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		jbtnCancel.setBounds(130, 245, 100, 45);
		rightPanel.add(jbtnCancel);

		jbtnComplete = createButton("완료");
		jbtnComplete.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		jbtnComplete.setBounds(85, 365, 120, 75);
		rightPanel.add(jbtnComplete);

		leftPanel.setPreferredSize(new Dimension(310, 0));

		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.CENTER);

		AdminReservationDetailFormEvt crde = new AdminReservationDetailFormEvt(this);
		jbtnConfirm.addActionListener(crde);
		jbtnCancel.addActionListener(crde);
		jbtnComplete.addActionListener(crde);
		addWindowListener(crde);

		UiStyle.apply(this);
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBackground(new Color(0, 140, 255));
		label.setForeground(Color.WHITE);
		label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		return label;
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(new Color(0, 140, 255));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		button.setFocusPainted(false);
		return button;
	}

	public JTextArea getJtaDetail() {
		return jtaDetail;
	}

	public void setJtaDetail(JTextArea jtaDetail) {
		this.jtaDetail = jtaDetail;
	}

	public JTextField getJtfWishDate() {
		return jtfWishDate;
	}

	public void setJtfWishDate(JTextField jtfWishDate) {
		this.jtfWishDate = jtfWishDate;
	}

	public JTextField getJtfConfirmDate() {
		return jtfConfirmDate;
	}

	public void setJtfConfirmDate(JTextField jtfConfirmDate) {
		this.jtfConfirmDate = jtfConfirmDate;
	}

	public JButton getJbtnConfirm() {
		return jbtnConfirm;
	}

	public void setJbtnConfirm(JButton jbtnConfirm) {
		this.jbtnConfirm = jbtnConfirm;
	}

	public JButton getJbtnCancel() {
		return jbtnCancel;
	}

	public void setJbtnCancel(JButton jbtnCancel) {
		this.jbtnCancel = jbtnCancel;
	}

	public JButton getJbtnComplete() {
		return jbtnComplete;
	}

	public void setJbtnComplete(JButton jbtnComplete) {
		this.jbtnComplete = jbtnComplete;
	}

	public String getReservCode() {
		return reservCode;
	}

	public void setReservCode(String reservCode) {
		this.reservCode = reservCode;
	}
}
