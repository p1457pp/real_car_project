package admin.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import admin.car_service.AdminCarService;
import admin.car_view.AdminCarEditForm;
import car_dto.CarDTO;
import car_dto.ManufacturerDTO;
import user.car_service.UserCarService;



public class AdminCarEditFormEvt implements ActionListener {

	private AdminCarEditForm acef;
	private int vehicleId; // 기존 아이디 데이터값
	private byte[] photo; //  기존 사진 데이터값 

	public AdminCarEditFormEvt(AdminCarEditForm acef, int vehicleId) {
		this.acef = acef;
		this.vehicleId = vehicleId;
		searchAllManufacturer();
		loadCarInfo();
	}// AdminCarEditFormEvt

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == acef.getJbtnRegister()) {
			modifyCar();
		} // end if

		if (ae.getSource() == acef.getJbtnCancel()) {
			acef.dispose();
		} // end if
	}// actionPerformed

	/**
	 * 만약 DB에서 제조사를 업데이트 할떄 업데이트한 버전의 
	 * 제조사 아이템을 가져오기위해 만든 load 메소드
	 */
	public void searchAllManufacturer() {
		AdminCarService acs = new AdminCarService();

		acef.getJcbManufacturer().removeAllItems(); // 기존 제조사 아이템을지우고 
		for (ManufacturerDTO mDTO : acs.searchAllManufacturer()) { // 현재 제조사 아이템을 다가져와서
			acef.getJcbManufacturer().addItem(mDTO); // 콤보박스에 현재 제조사 아이템으로 로드
		} // end for
	}// searchAllManufacturer

	/**
	 * JDialog를 띄울떄 선택한 테이블행의
	 * 사용자 Id, 차량번호, 차종, 차연식, 주행거리의 정보를 
	 * JDialog의 JTF에 로드시키기위한 메소드
	 */
	public void loadCarInfo() {
		AdminCarService acs = new AdminCarService(); // acs 불러오기
		CarDTO cDTO = acs.searchCarByVehicleId(vehicleId); // carDTO에 있는 vehicleID를 instance변수에 저장

		if (cDTO == null) { //cDTO에 vehicleId값이 없다면 
			JOptionPane.showMessageDialog(acef, "차량 정보를 불러올 수 없습니다.");
			acef.dispose();
			return;
		} // end if

		// acef에 있는 UserId, CarNum, CarModel, CarYear, CarMileage들의 JTextField를 vehicleId의 차 정보로 로드
		acef.getJtfUserId().setText(cDTO.getUserId());
		acef.getJtfCarNum().setText(cDTO.getCarNum()); 
		acef.getJtfCarModel().setText(cDTO.getCarModel()); 
		acef.getJtfCarYear().setText(String.valueOf(cDTO.getCarYear()));
		acef.getJtfMileage().setText(String.valueOf(cDTO.getMileage()));

		photo = cDTO.getCarPhoto(); // vehicleId의 기존 사진 정보 가져오기
		if (photo != null) { // 사진이 등록 되어있을 경우
			acef.getJlblPhoto().setText(""); // 사진에 있는 이미지 없음을 ""공백으로 전환
			acef.getJlblPhoto().setIcon(new ImageIcon(photo)); // 안쪽 사진을 기존 사진으로 채운다
		} // end if

		// 선택된 라디오버튼 정보 가져오기
		String fuel = cDTO.getFuelType();
		if (fuel.equals("휘발유")) {
			acef.getJrbGasoline().setSelected(true);
		} // end if
		if (fuel.equals("경유")) {
			acef.getJrbDiesel().setSelected(true);
		} // end if
		if (fuel.equals("LPG")) {
			acef.getJrbLpg().setSelected(true);
		} // end if
		if (fuel.equals("전기")) {
			acef.getJrbElectric().setSelected(true);
		} // end if

		// 그후 선택된 라디오버튼 정보를 사용해서 Selected 만들어놓기
		for (int i = 0; i < acef.getJcbManufacturer().getItemCount(); i++) {
			ManufacturerDTO mDTO = acef.getJcbManufacturer().getItemAt(i);
			if (mDTO.getManufacturerId() == cDTO.getManufacturerId()) {
				acef.getJcbManufacturer().setSelectedIndex(i);
			} // end if
		} // end for
	}// loadCarInfo

	/**
	 * 수정된 정보를 CarDTO에 적용시키고 
	 * ACS의 modifyCar를 실행시켜 리턴 받은 코드의 값으로 
	 * 사용자에게 완료 혹은 에러 메세지를 띄워주기 위한 메소드
	 */
	public void modifyCar() { 
		// acef에 있는 JTF의 정보값을 새로운 JTextFiedl에 변수에 넣는다
		// 이유 : 유호성 검증시에 어떤 JTF의 값에서 오류가 발생했는지 알기위해
		JTextField jtfUserId = acef.getJtfUserId();
		JTextField jtfCarNum = acef.getJtfCarNum();
		JTextField jtfCarModel = acef.getJtfCarModel();
		JTextField jtfCarYear = acef.getJtfCarYear();
		JTextField jtfMileage = acef.getJtfMileage();

		// 새로운 JTF에 있는 정보값에서 getText()로 텍스트만 가져와 String으로 값을 변환한다.
		String userId = jtfUserId.getText().trim();
		String carNum = jtfCarNum.getText().trim();
		String carModel = jtfCarModel.getText().trim();
		String carYear = jtfCarYear.getText().trim();
		String mileage = jtfMileage.getText().trim();

		// 만약 CarNum이 UserCarService에 있는 VaildCarNum의 메소드의 유호성 검증에서 탈락한경우
		if (!UserCarService.isValidCarNum(carNum)) {
			JOptionPane.showMessageDialog(acef, "차량번호 형식이 올바르지 않습니다.\n숫자 2~3자리 + 한글 1자 + 숫자 4자리 (예: 12가3456)");
			jtfCarNum.requestFocus(); // 다시 jtfCarNum으로 커서를 옮긴다
			return;
		} // end if

		
		if (carModel.isEmpty()) { // 가져온 JTF에서 carModel이 존재하지 않을경우 
			JOptionPane.showMessageDialog(acef, "차종은 필수 입력");
			jtfCarModel.requestFocus(); // 다시 jtfCarModel으로 커서를 옮긴다
			return;
		} // end if

		// carYear, carMileage의 변수 초기화
		int intYear = 0;
		int intMileage = 0;
		
		try {
			// 초기화된 변수에 JTF에서 가져온 값을 넣기
			intYear = Integer.parseInt(carYear);
			intMileage = Integer.parseInt(mileage);
		} catch (NumberFormatException nfe) { 
			// 숫자가 아닐경우 나오는 메세지
			JOptionPane.showMessageDialog(acef, "연식과 주행거리는 숫자로 입력해주세요.");
			return;
		} // end catch

		if (!UserCarService.isValidYear(intYear)) {
			// UserCarService에서 유호성검증을 통과를 못한 year값일 경우 나오는 메세지
			JOptionPane.showMessageDialog(acef, "연식은 1900년부터 2027년까지 입력할 수 있습니다.");
			jtfCarYear.requestFocus(); // 다시 jtfCarYear으로 커서를 옮긴다
			return;
		} // end if

		// 사용자가 콤보박스에서 수정한 제조사 정보를 꺼내온다
		ManufacturerDTO mDTO = (ManufacturerDTO) acef.getJcbManufacturer().getSelectedItem();

		// cDTO에 수정된 정보를 적용한다.
		CarDTO cDTO = new CarDTO();
		cDTO.setVehicleId(vehicleId);
		cDTO.setUserId(userId);
		cDTO.setCarNum(carNum);
		cDTO.setCarModel(carModel);
		cDTO.setCarYear(intYear);
		cDTO.setFuelType(getSelectedFuel()); // 리턴받은 값을 가져온다
		cDTO.setMileage(intMileage);
		cDTO.setCarPhoto(photo);
		cDTO.setManufacturerId(mDTO.getManufacturerId()); // 수정된 콤보박스의 ID를 가져와 수정한다

		// acs를 호출하여
		AdminCarService acs = new AdminCarService(); 
		
		// 수정된 값으로 acs의 modifyCar 메소드를 실행후
		int resultCode = acs.modifyCar(cDTO);
		
		// 기본 메세지 설정
		String outMsg = "프로그램에서 문제가 발생하였습니다.";

		// 리턴 받은 코드가 0 - 차량이 존재하지않는다 1 - 차랑졍보가 변경, 3 - 입력값 이상로 변수 값 수정
		if (resultCode == 0) {
			outMsg = "수정할 차량이 존재하지 않습니다.";
		} // end if
		if (resultCode == 3) {
			outMsg = "입력값을 확인해주세요.";
		} // end if
		if (resultCode == 1) {
			outMsg = "차량 정보가 변경되었습니다.";
		} // end if

		// 수정된 메세지를 사용자한테 띄워준다
		JOptionPane.showMessageDialog(acef, outMsg);

		// 리턴받은 코드가 1이면 다시 acef화면으로 돌아간다.
		if (resultCode == 1) {
			acef.dispose();
		} // end if
	}// modifyCar

	
	/**
	 * 선택한 라디오의 버튼을 String으로 변경하여
	 * CarDTO에 넘겨주기위한 변환 메소드
	 * @return fuel
	 */
	public String getSelectedFuel() {
		// 연료타입의 기본값
		String fuel = "휘발유";
		
		// 만약 디젤,LPG,전기 라디오버튼이 선택된다면 그 선택된 라디오버튼의 이름으로 fuel의 값을 바꾼다
		if (acef.getJrbDiesel().isSelected()) {
			fuel = "경유";
		} // end if
		if (acef.getJrbLpg().isSelected()) {
			fuel = "LPG";
		} // end if
		if (acef.getJrbElectric().isSelected()) {
			fuel = "전기";
		} // end if

		// 바꾼값을 리턴
		return fuel;
	}// getSelectedFuel

}// class