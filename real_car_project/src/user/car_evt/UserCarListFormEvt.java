package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import car_dto.CarDTO;
import user.UserMainTap;
import user.car_service.UserCarService;
import user.car_view.UserCarEditForm;
import user.car_view.UserCarListForm;
import user.car_view.UserCarRegisterForm;



public class UserCarListFormEvt implements ActionListener {

	private UserCarListForm uclf;
	private UserMainTap umt;
	private JTabbedPane jtpUser;
	private String userId;

	// 테이블 모델의 컬럼 번호
	public static final int VEHICLE_ID = 0; // 화면에는 안 보이는 숨김 컬럼

	public UserCarListFormEvt(UserCarListForm uclf, UserMainTap umt, JTabbedPane jtpUser, String userId) {
		this.uclf = uclf;
		this.umt = umt;
		this.jtpUser = jtpUser;
		// this.userId = userId;
		this.userId = "user028"; // 로그인 연동 후 위 줄로 교체
		setPhotoColumn(); //기본 사진 설정
		loadMyCarInfo(); // JTable에 값 설정
	}// UserCarListFormEvt

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == uclf.getJbtnAddCar()) {
			moveCarRegister();
		} // end if

		if (ae.getSource() == uclf.getJbtnCarInfoEdit()) {
			openEdit();
		} // end if

		if (ae.getSource() == uclf.getJbtnCarDelete()) {
			removeCar();
		} // end if
	}// actionPerformed

	/**
	 * 내 차량을 조회하여 JTable에 설정하는 일 (삭제된 차량은 조회되지 않는다)
	 */
	public void loadMyCarInfo() {
		UserCarService ucs = new UserCarService(); // ucs 연결
		List<CarDTO> listCar = ucs.searchCarByUserId(userId); // 선택된 유저의 차 리스트 가져오기

	
		DefaultTableModel dtmCar = uclf.getDtmCarList();  // uclf의 DTM 그릇정보를 가져온다
		dtmCar.setRowCount(0); // JTable안에 기존의 행을삭제 : 초기화

		// listCar에 저장된 carDTO의 현재정보를 DTM의 담는다
		for (CarDTO cDTO : listCar) {
			dtmCar.addRow(new Object[] /* int와 string둘다 담기위한 object 배열 선언 */ { 
					cDTO.getVehicleId(), // 차량 고유 번호
					changeImage(cDTO.getCarPhoto()), // 사진
					cDTO.getCarNum(), // 차량번호
					cDTO.getCarModel(), // 차종
					cDTO.getCarYear(), // 연식
					cDTO.getCarScrapped() // 폐차
			});
		} // end for
	}// searchCar

	/**
	 * CarListForm의 JTable의 사진 컬럼에 이미지 기본 셋팅 값
	 */
	public void setPhotoColumn() { 
		// JTable의 글자 정렬이나 스타일을 지정
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public void setValue(Object value) { // Object로 Int든 String이던 받아온다
				if (value instanceof ImageIcon) { // 들어온 value가 이미지정보면
					setIcon((ImageIcon) value); // 사진이 있으면 있는 이미지로 저장
					setText(""); // 이미지없음 삭제
				} else {
					setIcon(null); // 사진이 없으면 빈칸
					setText(""); // 이미지정보 byte의 배열갑을 안보이게한다.
				} // end if
			}// setValue
		};

		// JTable 화면 기준 0번째 컬럼이 사진이기 떄문에 방금 설정한 renderer를 설정한다.
		uclf.getJtUserCarList().getColumnModel().getColumn(0).setCellRenderer(renderer);
	}// setPhotoColumn

	/**
	 * 사진 byte[]를 JTable에 보여줄 작은 이미지로 변환하는 일
	 * 
	 * @param photo
	 */
	public ImageIcon changeImage(byte[] photo) {
		if (photo == null) { // 사진이없으면 
			return null; // null를 반환
		} // end if

		// 사진이있으면 있는사진의 이미지값을 img에 저장
		Image img = new ImageIcon(photo).getImage().getScaledInstance(50, 36, Image.SCALE_SMOOTH);
		return new ImageIcon(img); // 새로운 이미지값을 가진 이미지 아이콘을 반환
	}// changeImage

	/**
	 * 차량 등록 화면으로 이동하는 일
	 */
	public void moveCarRegister() {
		UserCarRegisterForm ucrf = new UserCarRegisterForm(); // 해당 form 연결
		// 취소 버튼을 누르면 목록 화면으로 돌아온다.
		ucrf.getJbtnCancel().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveCarList();
			}
		});

		jtpUser.setComponentAt(1, ucrf); // 탭을 2번쨰로 설정 0 - 회원 2 - 차량 3 - 예약 4 - 정비
		jtpUser.revalidate(); // 바뀐 레이아웃 다시계산
		jtpUser.repaint(); // 계산된 레이아웃으로 다시 화면 설정
	}// moveCarRegister

	/**
	 * 차량 목록 화면으로 돌아오는 일
	 */
	public void moveCarList() {
		jtpUser.setComponentAt(1, uclf); // 탭을 2번쨰로 설정 0 - 회원 2 - 차량 3 - 예약 4 - 정비
		loadMyCarInfo(); // JTable을 최신 데이터로 갱신
		jtpUser.revalidate();  // 바뀐 레이아웃 다시계산
		jtpUser.repaint(); // 계산된 레이아웃으로 다시 화면 설정
	}// moveCarList

	/**
	 * 선택한 차량의 수정 창을 여는 일
	 */
	public void openEdit() {
		// 선택된 행의 번호 가져오기
		int row = uclf.getJtUserCarList().getSelectedRow();

		if (row == -1) { // 선택한 행이 없을 때
			JOptionPane.showMessageDialog(uclf, "수정할 차량을 선택해주세요.");
			return;
		} // end if

		// 선택된 행의 번호로 vehicleID 설정
		int vehicleId = Integer.parseInt(uclf.getDtmCarList().getValueAt(row, VEHICLE_ID).toString());

		UserCarEditForm ucef = new UserCarEditForm(umt, vehicleId); // form 연결
		ucef.setVisible(true); //form의 다이얼로그 띄우기

		loadMyCarInfo(); // 수정 창이 닫히면 최신 데이터로 갱신
	}// openEdit

	/**
	 * 선택한 차량을 삭제(delete_state = 'Y')하고 JTable을 갱신하는 일
	 */
	public void removeCar() {
		// 선택된 행의 번호 가져오기
		int row = uclf.getJtUserCarList().getSelectedRow();

		if (row == -1) { // 선택한 행이 없을 때
			JOptionPane.showMessageDialog(uclf, "삭제할 차량을 선택해주세요.");
			return;
		} // end if

		// 선택된 행의 번호로 vehicleID 설정
		int vehicleId = Integer.parseInt(uclf.getDtmCarList().getValueAt(row, VEHICLE_ID).toString());

		UserCarService ucs = new UserCarService(); // ucs 연결

		// 선택된 행으로 UCS의 removeCarInfo실행
		if (ucs.removeCarInfo(vehicleId)) {
			loadMyCarInfo(); // 'Y'가 된 차량은 조회되지 않는다.
		} else { // 예외 떨어졋을 경우
			JOptionPane.showMessageDialog(uclf, "삭제에 실패했습니다.");
		} // end if
	}// removeCar

}// class
