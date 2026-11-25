package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import car_dto.CarDTO;
import car_dto.ManufacturerDTO;
import car_util.ImageResizer;
import user.car_service.UserCarService;
import user.car_view.UserCarRegisterForm;



public class UserCarRegisterFormEvt implements ActionListener {

	private UserCarRegisterForm ucrf;
	private byte[] selectedPhoto; // 선택한 사진 (안 골랐으면 null)

	public UserCarRegisterFormEvt(UserCarRegisterForm ucrf) {
		this.ucrf = ucrf;
		searchAllManufacturer(); // 콤보박스에 제조사 설정
	}// UserCarRegisterFormEvt

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == ucrf.getJbtnPhoto()) {
			choosePhoto();
		} // end if

		if (ae.getSource() == ucrf.getJbtnRegister()) {
			addCar();
		} // end if
	}// actionPerformed
	/**
	 * 제조사를 모두 조회하여 JComboBox에 설정하는 일
	 */
	public void searchAllManufacturer() {
		UserCarService ucs = new UserCarService(); // ucs 연결
		// 제조사 정보 가져오기
		List<ManufacturerDTO> listManufacturer = ucs.searchAllManufacturer();

		// 현재 콤보박스이 제조사정보 모두 초기화
		ucrf.getJcbManufacturer().removeAllItems();
		// 다시등록
		for (ManufacturerDTO mDTO : listManufacturer) {
			ucrf.getJcbManufacturer().addItem(mDTO);
		} // end for
	}// searchAllManufacturer

	/**
	 * 사진 파일을 선택하여 byte[]로 저장하고 미리보기에 보여주는 일
	 */
	public void choosePhoto() {
		JFileChooser jfc = new JFileChooser(); // JFileChooser연결
		
		/*
		 * 사용자가 파일을 고르고 열기를 눌럿으면 - 0
		 * 사용자가 취소나 X를 눌렀으면 - 1
		 * 도중 에러가 발생하면 - -1 
		 * 의 값을 result에 담는다.
		 * */
		int result = jfc.showOpenDialog(ucrf);

		// 사용자가 JFileChooser를 열었으면
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile(); // 선택한 파일을 file 변수에 담기
			try {
				// 파일의 모든 바이트를 photo 바이트 배열에 저장
				selectedPhoto = Files.readAllBytes(file.toPath());
				ucrf.getJlblPhoto().setText(""); // 이미지없음을 없애고
				ucrf.getJlblPhoto().setIcon(ImageResizer.fit(selectedPhoto, ucrf.getJlblPhoto())); // 현재 사진정보로 수정
			} catch (IOException ioe) {
				// 예외가떨어지면 에러메세지 출력
				JOptionPane.showMessageDialog(ucrf, "사진을 읽을 수 없습니다.");
				ioe.printStackTrace();
			} // end catch
		} // end if
	}// choosePhoto

	/**
	 * 입력한 값을 검사하고 차량을 등록하는 일
	 */
	public void addCar() {
		// ucef의 JTF에 있는 정보를 가져오기
		JTextField jtfCarNum = ucrf.getJtfCarNum();
		JTextField jtfCarModel = ucrf.getJtfCarModel();
		JTextField jtfCarYear = ucrf.getJtfCarYear();
		JTextField jtfMileage = ucrf.getJtfMileage();

		// 가져온 JTF에 정보에서 해당 텍스트를 String으로 저장
		String carNum = jtfCarNum.getText().trim();
		String carModel = jtfCarModel.getText().trim();
		String carYear = jtfCarYear.getText().trim();
		String mileage = jtfMileage.getText().trim();

		// 유효성 검증
		
		// 차량번호 규칙과 맞지않는경우
		if (!UserCarService.isValidCarNum(carNum)) {
			JOptionPane.showMessageDialog(ucrf, "차량번호 형식이 올바르지 않습니다.\n숫자 2~3자리 + 한글 1자 + 숫자 4자리 (예: 12가3456)");
			jtfCarNum.requestFocus();
			return;
		} // end if

		// 차종이 비워있을 경우
		if (carModel.isEmpty()) {
			JOptionPane.showMessageDialog(ucrf, "차종은 필수 입력");
			jtfCarModel.requestFocus();
			return;
		} // end if

		// 연식이 비워있을 경우
		if (carYear.isEmpty()) {
			JOptionPane.showMessageDialog(ucrf, "연식은 필수 입력");
			jtfCarYear.requestFocus();
			return;
		} // end if

		// 주행거리가 비워있을경우
		if (mileage.isEmpty()) {
			JOptionPane.showMessageDialog(ucrf, "주행거리는 필수 입력");
			jtfMileage.requestFocus();
			return;
		} // end if

		// 연식과 주행거리 변수 초기화
		int intYear = 0;
		int intMileage = 0;
		
		try {
			intYear = Integer.parseInt(carYear);  // 수정한 차연식을 변수에 담기
			intMileage = Integer.parseInt(mileage); // 수정한 차연식을 변수에 담기
		} catch (NumberFormatException nfe) { // 숫자가 아닌경우
			JOptionPane.showMessageDialog(ucrf, "연식과 주행거리는 숫자로 입력해주세요.");
			return;
		} // end catch

		// 맞지않는 연도의 차일경우 ex) 1800년
		if (!UserCarService.isValidYear(intYear)) {
			JOptionPane.showMessageDialog(ucrf, "연식은 1900년부터 2027년까지 입력할 수 있습니다.");
			jtfCarYear.requestFocus();
			return;
		} // end if

		// 콤보박스에서 선택된 제조사정보를 mDTO에 저장
		ManufacturerDTO mDTO = (ManufacturerDTO) ucrf.getJcbManufacturer().getSelectedItem();

		CarDTO cDTO = new CarDTO();  // CarDTO의 그릇정보를 cDTO로 로드
		// String의 모든 문자를 cDTO에 그릇에 넣기
		cDTO.setCarNum(carNum);
		cDTO.setCarModel(carModel);
		cDTO.setCarYear(intYear);
		cDTO.setFuelType(getSelectedFuel());
		cDTO.setMileage(intMileage);
		cDTO.setCarPhoto(selectedPhoto);
		cDTO.setManufacturerId(mDTO.getManufacturerId());
		String targetUserId = (ucrf.getUserId() != null && !ucrf.getUserId().trim().isEmpty()) ? ucrf.getUserId().trim() : "";
		cDTO.setUserId(targetUserId);

		// UCS 연결
		UserCarService ucs = new UserCarService();

		if (ucs.addCarInfo(cDTO)) { // UCS의 차 추가 메소드 실행
			JOptionPane.showMessageDialog(ucrf, "차량이 등록되었습니다.");
			
			// 추가가 완료되면 취소버튼을 강제로 클릭시 : 
			// 입력칸 비우기 / 라디오·콤보 초기화 / 사진 초기화  
			ucrf.getJbtnCancel().doClick();
		} else { // 차량등록이 실패하면
			JOptionPane.showMessageDialog(ucrf, "등록에 실패했습니다.\n(번호판 중복 또는 입력값을 확인해주세요.)");
		} // end if
	}// addCar

	/**
	 * 선택된 라디오버튼의 연료 문자열을 반환하는 일
	 */
	public String getSelectedFuel() {
		String fuel = "휘발유"; // 기본 연로 셋팅 휘발유로

		// 각각의 라디오 버튼이 선택되면 변수값 바꾸기
		if (ucrf.getJrbDiesel().isSelected()) {
			fuel = "경유";
		} // end if
		if (ucrf.getJrbLpg().isSelected()) {
			fuel = "LPG";
		} // end if
		if (ucrf.getJrbElectric().isSelected()) {
			fuel = "전기";
		} // end if

		return fuel; // 바뀐 변수 리턴
	}// getSelectedFuel

}// class
