package admin.car_view;

import car_util.UiStyle;

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
import javax.swing.SwingConstants;
import admin.AdminMainTab;
import admin.car_evt.AdminCarEditFormEvt;
import car_dto.ManufacturerDTO;


/**
 * 회원 차량 정보 수정 다이얼로그 (관리자, JDialog 상속)
 * AdminCarListForm 의 [수정] 버튼 -> AdminCarListFormEvt.openEdit() 에서 열린다.
 */
public class AdminCarEditForm extends JDialog {

	private JLabel jlblUserIdTitle, jlblTitle, jlblPhoto, jlblCarNum, jlblCarModel,
			jlblManufacturer, jlblCarYear, jlblFuel, jlblMileage, jlblKm;
	private JLabel jlblGasoline, jlblDiesel, jlblLpg, jlblElectric;
	private JTextField jtfUserId, jtfCarNum, jtfCarModel, jtfCarYear, jtfMileage;

	private ButtonGroup bgFuel;
	private JButton jbtnRegister, jbtnCancel;
	private JRadioButton jrbGasoline, jrbDiesel, jrbLpg, jrbElectric;

	private JComboBox<ManufacturerDTO> jcbManufacturer;
	private Font fontBold;
	public AdminCarEditForm(AdminMainTab amt, int vehicleId) {
		setLayout(null);

		fontBold = new Font("맑은 고딕", Font.BOLD, 14);

		// 제목
		jlblTitle = new JLabel("회원 차량 정보 수정");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		jlblTitle.setBounds(30, 15, 250, 30);
		add(jlblTitle);

		// 사진 영역 (관리자는 조회만)
		jlblPhoto = new JLabel("이미지 없음", SwingConstants.CENTER);
		jlblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlblPhoto.setBounds(30, 60, 250, 230);
		add(jlblPhoto);

		// 회원ID 
		jlblUserIdTitle = new JLabel("회원ID");
		jlblUserIdTitle.setFont(fontBold);
		jlblUserIdTitle.setBounds(320, 60, 80, 30);
		add(jlblUserIdTitle);

		jtfUserId = new JTextField();
		jtfUserId.setBounds(420, 60, 250, 30);
		add(jtfUserId);

		// 차량번호
		jlblCarNum = new JLabel("차량번호");
		jlblCarNum.setFont(fontBold);
		jlblCarNum.setBounds(320, 110, 80, 30);
		add(jlblCarNum);

		jtfCarNum = new JTextField();
		jtfCarNum.setBounds(420, 110, 250, 30);
		add(jtfCarNum);

		// 차종
		jlblCarModel = new JLabel("차종");
		jlblCarModel.setFont(fontBold);
		jlblCarModel.setBounds(320, 160, 80, 30);
		add(jlblCarModel);

		jtfCarModel = new JTextField();
		jtfCarModel.setBounds(420, 160, 250, 30);
		add(jtfCarModel);

		// 제조사
		jlblManufacturer = new JLabel("제조사");
		jlblManufacturer.setFont(fontBold);
		jlblManufacturer.setBounds(320, 210, 80, 30);
		add(jlblManufacturer);

		jcbManufacturer = new JComboBox<ManufacturerDTO>();
		jcbManufacturer.setBounds(420, 210, 150, 30);
		add(jcbManufacturer);

		// 연식
		jlblCarYear = new JLabel("연식");
		jlblCarYear.setFont(fontBold);
		jlblCarYear.setBounds(320, 260, 80, 30);
		add(jlblCarYear);

		jtfCarYear = new JTextField();
		jtfCarYear.setBounds(420, 260, 250, 30);
		add(jtfCarYear);

		// 연료 (라디오 버튼 + 글자 라벨)
		jlblFuel = new JLabel("연료");
		jlblFuel.setFont(fontBold);
		jlblFuel.setBounds(320, 310, 80, 30);
		add(jlblFuel);

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

		jlblGasoline = new JLabel("휘발유");
		jlblDiesel = new JLabel("경유");
		jlblLpg = new JLabel("LPG");
		jlblElectric = new JLabel("전기");

		jrbGasoline.setBounds(420, 310, 20, 30);
		jlblGasoline.setBounds(442, 310, 45, 30);

		jrbDiesel.setBounds(495, 310, 20, 30);
		jlblDiesel.setBounds(517, 310, 35, 30);

		jrbLpg.setBounds(560, 310, 20, 30);
		jlblLpg.setBounds(582, 310, 35, 30);

		jrbElectric.setBounds(620, 310, 20, 30);
		jlblElectric.setBounds(642, 310, 35, 30);

		add(jrbGasoline);
		add(jlblGasoline);
		add(jrbDiesel);
		add(jlblDiesel);
		add(jrbLpg);
		add(jlblLpg);
		add(jrbElectric);
		add(jlblElectric);

		// 주행거리
		jlblMileage = new JLabel("주행거리");
		jlblMileage.setFont(fontBold);
		jlblMileage.setBounds(320, 360, 80, 30);
		add(jlblMileage);

		jtfMileage = new JTextField();
		jtfMileage.setBounds(420, 360, 210, 30);
		add(jtfMileage);

		jlblKm = new JLabel("km");
		jlblKm.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		jlblKm.setBounds(640, 360, 30, 30);
		add(jlblKm);

		// 하단 버튼
		jbtnRegister = new JButton("수정");
		jbtnRegister.setBounds(260, 430, 85, 32);
		add(jbtnRegister);

		jbtnCancel = new JButton("취소");
		jbtnCancel.setBounds(365, 430, 85, 32);
		add(jbtnCancel);

		// 액션 이벤트 추가 (vehicleId를 함께 전달)
		AdminCarEditFormEvt evt = new AdminCarEditFormEvt(this, vehicleId);

		jbtnRegister.addActionListener(evt);
		jbtnCancel.addActionListener(evt);

		setSize(720, 525);
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(amt);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		UiStyle.apply(this);
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
	
	public JTextField getJtfUserId() {
		return jtfUserId;
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
	}

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
