package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import car_dto.CarDTO;
import car_dto.ManufacturerDTO;
import user.car_service.UserCarService;
import user.car_view.UserCarEditForm;



public class UserCarEditFormEvt implements ActionListener {

	private UserCarEditForm ucef;
	private int vehicleId;
	private byte[] photo; // 기존 사진 (안 바꾸면 그대로 update에 넘김)

	public UserCarEditFormEvt(UserCarEditForm ucef, int vehicleId) {
		this.ucef = ucef;
		this.vehicleId = vehicleId;
		searchAllManufacturer(); // 콤보박스에 제조사 설정
		searchCarByVehicleId(vehicleId); // 선택한 차량 정보를 입력칸에 설정
	}// UserCarEditFormEvt

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == ucef.getJbtnPhoto()) {
			choosePhoto();
		} // end if

		if (ae.getSource() == ucef.getJbtnRegister()) {
			modifyCar();
		} // end if

		if (ae.getSource() == ucef.getJbtnCancel()) {
			ucef.dispose();
		} // end if
	}// actionPerformed

	/**
	 * 제조사를 모두 조회하여 JComboBox에 설정하는 메소드
	 */
	public void searchAllManufacturer() {
		UserCarService ucs = new UserCarService(); // usc 연결

		ucef.getJcbManufacturer().removeAllItems(); // 현재 콤보박스에 모든 아이템 삭제 : 초기화
		// DAO -> ACS로 가져온 제조사 현재 정보 가져오기
		for (ManufacturerDTO mDTO : ucs.searchAllManufacturer()) {
			ucef.getJcbManufacturer().addItem(mDTO); // 가져온 정보를 추가
		} // end for
	}// searchAllManufacturer
	/**
	 * 선택한 차량 정보를 조회하여 입력칸에 설정하는 메소드
	 */
	public void searchCarByVehicleId(int vehicleId) {
		UserCarService ucs = new UserCarService(); // ucs 연결
		CarDTO cDTO = ucs.searchCarByVehicleId(vehicleId); // ucs의 현재 유저아이디의 차 정보 가져오기

		if (cDTO == null) { // cDTO에 값이 없거나 불러오는걸 실패하면
			JOptionPane.showMessageDialog(ucef, "차량 정보를 불러올 수 없습니다.");
			ucef.dispose(); // 다이얼로그 창닫기
			return;
		} // end if

		// EditForm의 JTF를 선택된 유저의 정보로 설정
		ucef.getJtfCarNum().setText(cDTO.getCarNum());
		ucef.getJtfCarModel().setText(cDTO.getCarModel());
		ucef.getJtfCarYear().setText(String.valueOf(cDTO.getCarYear()));
		ucef.getJtfMileage().setText(String.valueOf(cDTO.getMileage()));

		// 사진
		photo = cDTO.getCarPhoto(); // 사진정보를 변수에 담기
		if (photo != null) { // 사진이있으면
			ucef.getJlblPhoto().setText(""); // 이미지없음 글자를 지우고
			ucef.getJlblPhoto().setIcon(new ImageIcon(photo)); // 현재 이미지로 설정
		} // end if

		// 유저가 이전에 선택한 제조사타입을 그대로 설정
		for (int i = 0; i < ucef.getJcbManufacturer().getItemCount(); i++) {
			ManufacturerDTO mDTO = ucef.getJcbManufacturer().getItemAt(i);
			if (mDTO.getManufacturerId() == cDTO.getManufacturerId()) {
				ucef.getJcbManufacturer().setSelectedIndex(i);
			} // end if
		} // end for
		
		// 유저가 이전에 선택한 연료타입을 그대로 설정
		String fuel = cDTO.getFuelType();
		if ("휘발유".equals(fuel)) {
			ucef.getJrbGasoline().setSelected(true);
		} // end if
		if ("경유".equals(fuel)) {
			ucef.getJrbDiesel().setSelected(true);
		} // end if
		if ("LPG".equals(fuel)) {
			ucef.getJrbLpg().setSelected(true);
		} // end if
		if ("전기".equals(fuel)) {
			ucef.getJrbElectric().setSelected(true);
		} // end if

	}// searchCar

	/**
	 * 사진 파일을 선택하여 byte[]로 저장하고 미리보기에 보여주는 일
	 */
	public void choosePhoto() {
		JFileChooser jfc = new JFileChooser(); // JFileChooser 로드

		/*
		 * 사용자가 파일을 고르고 열기를 눌럿으면 - 0
		 * 사용자가 취소나 X를 눌렀으면 - 1
		 * 도중 에러가 발생하면 - -1 
		 * 의 값을 result에 담는다.
		 * */
		int result = jfc.showOpenDialog(ucef); 

		// 사용자가 JFileChooser를 열었으면
		if (result == JFileChooser.APPROVE_OPTION) { 
			File file = jfc.getSelectedFile(); // 선택한 파일을 file 변수에 담기
			try {
				// 파일의 모든 바이트를 photo 바이트 배열에 저장
				photo = Files.readAllBytes(file.toPath());
				ucef.getJlblPhoto().setText(""); // 이미지없음을 없애고
				ucef.getJlblPhoto().setIcon(new ImageIcon(photo)); // 현재 사진정보로 수정
			} catch (IOException ioe) {
				// 예외가떨어지면 에러메세지 출력
				JOptionPane.showMessageDialog(ucef, "사진을 읽을 수 없습니다.");
				ioe.printStackTrace();
			} // end catch
		} // end if
	}// choosePhoto

	/**
	 * 입력한 값을 검사하고 차량 정보를 변경하는 일
	 */
	public void modifyCar() {
		// ucef의 JTF에 있는 정보를 가져오기
		JTextField jtfCarNum = ucef.getJtfCarNum();
		JTextField jtfCarModel = ucef.getJtfCarModel();
		JTextField jtfCarYear = ucef.getJtfCarYear();
		JTextField jtfMileage = ucef.getJtfMileage();

		// 가져온 JTF에 정보에서 해당 텍스트를 String으로 저장
		String carNum = jtfCarNum.getText().trim();
		String carModel = jtfCarModel.getText().trim();
		String carYear = jtfCarYear.getText().trim();
		String mileage = jtfMileage.getText().trim();

		// 유효성 검증
		
		// 차량번호 규칙과 맞지않는경우
		if (!UserCarService.isValidCarNum(carNum)) {
			JOptionPane.showMessageDialog(ucef, "차량번호 형식이 올바르지 않습니다.\n숫자 2~3자리 + 한글 1자 + 숫자 4자리 (예: 12가3456)");
			jtfCarNum.requestFocus();
			return;
		} // end if

		// 차종이 비워있을 경우
		if (carModel.isEmpty()) {
			JOptionPane.showMessageDialog(ucef, "차종은 필수 입력");
			jtfCarModel.requestFocus();
			return;
		} // end if

		// 연식이 비워있을 경우
		if (carYear.isEmpty()) {
			JOptionPane.showMessageDialog(ucef, "연식은 필수 입력");
			jtfCarYear.requestFocus();
			return;
		} // end if

		// 주행거리가 비워있을경우
		if (mileage.isEmpty()) {
			JOptionPane.showMessageDialog(ucef, "주행거리는 필수 입력");
			jtfMileage.requestFocus();
			return;
		} // end if

		// 연식과 주행거리 변수 초기화
		int intYear = 0;
		int intMileage = 0;
		
		try {
			intYear = Integer.parseInt(carYear); // 수정한 차연식을 변수에 담기
			intMileage = Integer.parseInt(mileage); // 수정한 차연식을 변수에 담기
		} catch (NumberFormatException nfe) { // 숫자가 아닌경우
			JOptionPane.showMessageDialog(ucef, "연식과 주행거리는 숫자로 입력해주세요.");
			return;
		} // end catch

		// 맞지않는 연도의 차일경우 ex) 1800년
		if (!UserCarService.isValidYear(intYear)) {
			JOptionPane.showMessageDialog(ucef, "연식은 1900년부터 2027년까지 입력할 수 있습니다.");
			jtfCarYear.requestFocus();
			return;
		} // end if

		// 콤보박스에서 선택된 제조사정보를 mDTO에 저장
		ManufacturerDTO mDTO = (ManufacturerDTO) ucef.getJcbManufacturer().getSelectedItem();

		CarDTO cDTO = new CarDTO(); // CarDTO의 그릇정보를 cDTO로 로드
		// String의 모든 문자를 cDTO에 그릇에 넣기
		cDTO.setVehicleId(vehicleId);
		cDTO.setCarNum(carNum);
		cDTO.setCarModel(carModel);
		cDTO.setCarYear(intYear);
		cDTO.setFuelType(getSelectedFuel());
		cDTO.setMileage(intMileage);
		cDTO.setCarPhoto(photo);
		cDTO.setManufacturerId(mDTO.getManufacturerId());

		// UCS 연결
		UserCarService ucs = new UserCarService();

		// 변경작업 수행 : 
		// 0 - 행이 선택되지 않는경우 1 - 변경완료 2 - 예외가떨어졋을경우 3 - 정보가 적합하지않는경우
		int resultCode = ucs.modifyCarInfo(cDTO);
		// 메세지 기본 셋팅 2번 메세지로
		String outMsg = "프로그램에서 문제가 발생하였습니다.";

		if (resultCode == 0) {
			outMsg = "수정할 차량이 존재하지 않습니다.";
		} // end if

		if (resultCode == 3) {
			outMsg = "입력값을 확인해주세요.";
		} // end if

		if (resultCode == 1) {
			outMsg = "차량 정보가 변경되었습니다.";
		} // end if

		// 메세지 띄우기
		JOptionPane.showMessageDialog(ucef, outMsg);

		if (resultCode == 1) {
			ucef.dispose(); // 성공하면 창을 닫는다.
		} // end if
	}// modifyCar

	/**
	 * 선택된 라디오버튼의 연료 문자열을 반환하는 일
	 */
	public String getSelectedFuel() {
		String fuel = "휘발유"; // 기본 연로 셋팅 휘발유로

		// 각각의 라디오 버튼이 선택되면 변수값 바꾸기
		if (ucef.getJrbDiesel().isSelected()) {
			fuel = "경유";
		} // end if
		if (ucef.getJrbLpg().isSelected()) {
			fuel = "LPG";
		} // end if
		if (ucef.getJrbElectric().isSelected()) {
			fuel = "전기";
		} // end if

		return fuel; // 바뀐 변수 리턴
	}// getSelectedFuel

}// class
