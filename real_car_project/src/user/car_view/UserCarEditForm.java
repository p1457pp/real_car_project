package user.car_view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import car_dto.ManufacturerDTO;
import user.UserMainTap;
import user.car_evt.UserCarEditFormEvt;



public class UserCarEditForm extends JDialog {

	private JLabel jlblTitle, jlblPhoto, jlblCarNum, jlblCarModel, jlblCarYear, jlblFuel, jlblManufacturer, jlblMileage,
			jlblKm;
	private JLabel jlblGasoline, jlblDiesel, jlblLpg, jlblElectric;
	private JTextField jtfCarNum, jtfCarModel, jtfCarYear, jtfMileage;

	private ButtonGroup bgFuel;
	private JButton jbtnPhoto, jbtnRegister, jbtnCancel;
	private JRadioButton jrbGasoline, jrbDiesel, jrbLpg, jrbElectric;

	private JComboBox<ManufacturerDTO> jcbManufacturer; // 타입 변경
	private Font fontBold;

	// 생성자에 vehicleId 추가
	public UserCarEditForm(UserMainTap umt, int vehicleId) {
		setLayout(null);

		fontBold = new Font("맑은 고딕", Font.BOLD, 14);

		jlblTitle = new JLabel("차량 수정");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		jlblTitle.setBounds(30, 15, 200, 30);
		add(jlblTitle);

		jlblPhoto = new JLabel("이미지 없음", JLabel.CENTER);
		jlblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlblPhoto.setBounds(30, 60, 250, 230);
		add(jlblPhoto);

		jbtnPhoto = new JButton("사진변경");
		jbtnPhoto.setBounds(95, 305, 120, 30);
		add(jbtnPhoto);

		jlblCarNum = new JLabel("차량번호");
		jlblCarNum.setFont(fontBold);
		jlblCarNum.setBounds(320, 60, 80, 30);
		add(jlblCarNum);

		jtfCarNum = new JTextField();
		jtfCarNum.setBounds(420, 60, 250, 30);
		add(jtfCarNum);

		jlblCarModel = new JLabel("차종");
		jlblCarModel.setFont(fontBold);
		jlblCarModel.setBounds(320, 110, 80, 30);
		add(jlblCarModel);

		jtfCarModel = new JTextField();
		jtfCarModel.setBounds(420, 110, 250, 30);
		add(jtfCarModel);

		jlblManufacturer = new JLabel("제조사");
		jlblManufacturer.setFont(fontBold);
		jlblManufacturer.setBounds(320, 160, 80, 30);
		add(jlblManufacturer);

		// 콤보박스 초기화 설정 변경
		jcbManufacturer = new JComboBox<ManufacturerDTO>();
		jcbManufacturer.setBounds(420, 160, 150, 30);
		add(jcbManufacturer);

		jlblCarYear = new JLabel("연식");
		jlblCarYear.setFont(fontBold);
		jlblCarYear.setBounds(320, 210, 80, 30);
		add(jlblCarYear);

		jtfCarYear = new JTextField();
		jtfCarYear.setBounds(420, 210, 250, 30);
		add(jtfCarYear);

		jlblFuel = new JLabel("연료");
		jlblFuel.setFont(fontBold);
		jlblFuel.setBounds(320, 260, 80, 30);
		add(jlblFuel);

		jlblGasoline = new JLabel("휘발유");
		jlblDiesel = new JLabel("경유");
		jlblLpg = new JLabel("LPG");
		jlblElectric = new JLabel("전기");

		jrbGasoline = new JRadioButton();
		jrbDiesel = new JRadioButton();
		jrbLpg = new JRadioButton();
		jrbElectric = new JRadioButton();
		jrbGasoline.setSelected(true);

		bgFuel = new ButtonGroup();
		bgFuel.add(jrbGasoline);
		bgFuel.add(jrbDiesel);
		bgFuel.add(jrbLpg);
		bgFuel.add(jrbElectric);

		jrbGasoline.setBounds(420, 260, 20, 30);
		jlblGasoline.setBounds(442, 260, 45, 30);

		jrbDiesel.setBounds(495, 260, 20, 30);
		jlblDiesel.setBounds(517, 260, 35, 30);

		jrbLpg.setBounds(560, 260, 20, 30);
		jlblLpg.setBounds(582, 260, 35, 30);

		jrbElectric.setBounds(620, 260, 20, 30);
		jlblElectric.setBounds(642, 260, 35, 30);

		add(jlblGasoline);
		add(jlblDiesel);
		add(jlblLpg);
		add(jlblElectric);
		add(jrbGasoline);
		add(jrbDiesel);
		add(jrbLpg);
		add(jrbElectric);

		jlblMileage = new JLabel("주행거리");
		jlblMileage.setFont(fontBold);
		jlblMileage.setBounds(320, 310, 80, 30);
		add(jlblMileage);

		jtfMileage = new JTextField();
		jtfMileage.setBounds(420, 310, 210, 30);
		add(jtfMileage);

		jlblKm = new JLabel("km");
		jlblKm.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		jlblKm.setBounds(640, 310, 30, 30);
		add(jlblKm);

		jbtnRegister = new JButton("수정");
		jbtnRegister.setBounds(260, 390, 85, 32);
		add(jbtnRegister);

		jbtnCancel = new JButton("취소");
		jbtnCancel.setBounds(365, 390, 85, 32);
		add(jbtnCancel);

		// vehicleId를 함께 전달하도록 수정
		UserCarEditFormEvt evt = new UserCarEditFormEvt(this, vehicleId);

		jbtnPhoto.addActionListener(evt);
		jbtnRegister.addActionListener(evt);
		jbtnCancel.addActionListener(evt);

		setModal(true);
		setResizable(false);
		setSize(720, 485);
		setLocationRelativeTo(umt);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public JButton getJbtnPhoto() {
		return jbtnPhoto;
	}

	public JButton getJbtnRegister() {
		return jbtnRegister;
	}

	public JButton getJbtnCancel() {
		return jbtnCancel;
	}

	public JLabel getJlblPhoto() {
		return jlblPhoto;
	}

	public JTextField getJtfCarNum() {
		return jtfCarNum;
	}

	public JTextField getJtfCarModel() {
		return jtfCarModel;
	}

	public JTextField getJtfCarYear() {
		return jtfCarYear;
	}

	public JTextField getJtfMileage() {
		return jtfMileage;
	}

	public JComboBox<ManufacturerDTO> getJcbManufacturer() {
		return jcbManufacturer;
	} // 리턴 타입 변경

	public JRadioButton getJrbGasoline() {
		return jrbGasoline;
	}

	public JRadioButton getJrbDiesel() {
		return jrbDiesel;
	}

	public JRadioButton getJrbLpg() {
		return jrbLpg;
	}

	public JRadioButton getJrbElectric() {
		return jrbElectric;
	}
}
