package user.car_view;

import car_util.UiStyle;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import car_dto.ManufacturerDTO;
import user.car_evt.UserCarRegisterFormEvt;

public class UserCarRegisterForm extends JPanel {
	private JLabel jlblTitle, jlblPhoto, jlblCarNum, jlblCarModel,
			jlblManufacturer, jlblCarYear, jlblFuel, jlblMileage, jlblKm;
    private JLabel jlblGasoline, jlblDiesel, jlblLpg, jlblElectric;
    private JTextField jtfCarNum, jtfCarModel, jtfCarYear, jtfMileage;
    
    private ButtonGroup bgFuel;
    private JButton jbtnPhoto, jbtnRegister, jbtnCancel;
    private JRadioButton jrbGasoline, jrbDiesel, jrbLpg, jrbElectric;

    private JComboBox<ManufacturerDTO> jcbManufacturer; // 타입 변경
    private Font fontBold;
    
    // userId를 외부에서 받을 수 있도록 생성자 오버로딩 또는 기본 설정
    private String userId;

    public UserCarRegisterForm(String userId) {
        this.userId = userId;
        initLayout();

		UiStyle.apply(this);
    }

    public UserCarRegisterForm() {
        initLayout();

		UiStyle.apply(this);
	}

	public String getUserId() {
		return userId;
    }

    private void initLayout() {
        setLayout(null);

        fontBold = new Font("맑은 고딕", Font.BOLD, 14);

        jlblTitle = new JLabel("차량 등록");
        jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        jlblTitle.setBounds(30, 45, 200, 30);
        add(jlblTitle);

        jlblPhoto = new JLabel("이미지 없음", JLabel.CENTER);
        jlblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jlblPhoto.setBounds(30, 105, 280, 260);
        add(jlblPhoto);

        jbtnPhoto = new JButton("사진선택");
        jbtnPhoto.setBounds(110, 380, 120, 30);
        add(jbtnPhoto);
		jlblCarNum = new JLabel("차량번호");
		jlblCarNum.setFont(fontBold);
		jlblCarNum.setBounds(350, 105, 80, 30);
		add(jlblCarNum);

        jtfCarNum = new JTextField();
        jtfCarNum.setBounds(450, 105, 250, 30);
        add(jtfCarNum);
		jlblCarModel = new JLabel("차종");
		jlblCarModel.setFont(fontBold);
		jlblCarModel.setBounds(350, 155, 80, 30);
		add(jlblCarModel);

        jtfCarModel = new JTextField();
        jtfCarModel.setBounds(450, 155, 250, 30);
        add(jtfCarModel);
		jlblManufacturer = new JLabel("제조사");
		jlblManufacturer.setFont(fontBold);
		jlblManufacturer.setBounds(350, 205, 80, 30);
		add(jlblManufacturer);

        // 콤보박스 초기화 설정 변경
        jcbManufacturer = new JComboBox<ManufacturerDTO>();
        jcbManufacturer.setBounds(450, 205, 150, 30);
        add(jcbManufacturer);
		jlblCarYear = new JLabel("연식");
		jlblCarYear.setFont(fontBold);
		jlblCarYear.setBounds(350, 255, 80, 30);
		add(jlblCarYear);

        jtfCarYear = new JTextField();
        jtfCarYear.setBounds(450, 255, 250, 30);
        add(jtfCarYear);
		jlblFuel = new JLabel("연료");
		jlblFuel.setFont(fontBold);
		jlblFuel.setBounds(350, 305, 80, 30);
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

        jrbGasoline.setBounds(450, 305, 20, 30);
        jlblGasoline.setBounds(472, 305, 45, 30);
        jrbDiesel.setBounds(525, 305, 20, 30);
        jlblDiesel.setBounds(547, 305, 35, 30);
        jrbLpg.setBounds(590, 305, 20, 30);
        jlblLpg.setBounds(612, 305, 35, 30);
        jrbElectric.setBounds(650, 305, 20, 30);
        jlblElectric.setBounds(672, 305, 35, 30);

        add(jrbGasoline); add(jlblGasoline);
        add(jrbDiesel);   add(jlblDiesel);
        add(jrbLpg);      add(jlblLpg);
        add(jrbElectric); add(jlblElectric);
		jlblMileage = new JLabel("주행거리");
		jlblMileage.setFont(fontBold);
		jlblMileage.setBounds(350, 355, 80, 30);
		add(jlblMileage);

        jtfMileage = new JTextField();
        jtfMileage.setBounds(450, 355, 210, 30);
        add(jtfMileage);
		jlblKm = new JLabel("km");
		jlblKm.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		jlblKm.setBounds(670, 355, 30, 30);
		add(jlblKm);

        jbtnRegister = new JButton("등록");
        jbtnRegister.setBounds(290, 530, 85, 32);
        add(jbtnRegister);

        jbtnCancel = new JButton("취소");
        jbtnCancel.setBounds(395, 530, 85, 32);
        add(jbtnCancel);
        
        // 이벤트 생성 시 userId 전달
        UserCarRegisterFormEvt evt = new UserCarRegisterFormEvt(this);
        
        jbtnPhoto.addActionListener(evt);
        jbtnRegister.addActionListener(evt);
        jbtnCancel.addActionListener(evt);
    }

	public JButton getJbtnRegister() { return jbtnRegister; }
	public JButton getJbtnCancel() { return jbtnCancel; }
	public JButton getJbtnPhoto() { return jbtnPhoto; }
	public JLabel getJlblPhoto() { return jlblPhoto; }
	public JTextField getJtfCarNum() { return jtfCarNum; }
	public JTextField getJtfCarModel() { return jtfCarModel; }
	public JTextField getJtfCarYear() { return jtfCarYear; }
	public JTextField getJtfMileage() { return jtfMileage; }
	public JComboBox<ManufacturerDTO> getJcbManufacturer() { return jcbManufacturer; } // 리턴 타입 변경
	public JRadioButton getJrbGasoline() { return jrbGasoline; }
	public JRadioButton getJrbDiesel() { return jrbDiesel; }
	public JRadioButton getJrbLpg() { return jrbLpg; }
	public JRadioButton getJrbElectric() { return jrbElectric; }
}
