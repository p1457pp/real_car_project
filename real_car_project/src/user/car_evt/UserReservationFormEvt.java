package user.car_evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

import car_dto.CarDTO;
import car_dto.RepairItemDTO;
import car_dto.ReservationDTO;
import user.UserMainTab;
import user.car_service.UserCarService;
import user.car_service.UserReservationService;
import user.car_view.UserReservationForm;
import user.car_view.UserReservationListForm;

public class UserReservationFormEvt extends FocusAdapter implements ActionListener {

	private UserReservationForm urf;
	private UserMainTab umt;
	private JTabbedPane jtpUser;
	private String userId;
	private UserReservationService urs;

	public UserReservationFormEvt(UserReservationForm urf, UserMainTab umt, JTabbedPane jtpUser, String userId) {
		this.urf = urf;
		this.umt = umt;
		this.jtpUser = jtpUser;
		this.userId = userId;
		this.urs = new UserReservationService();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == urf.getJbtnRequest()) {
			addReservation();
		}
		if (e.getSource() == urf.getJbtnList()) {
			moveReservationList();
		}
		if (e.getSource() == urf.getJtfCarNum()) {
			findCarModel();
		
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == urf.getJtfCarNum()) {
			findCarModel();
		}
	}

	/** 정비 항목을 콤보박스에 채운다. */
	public void searchRepairItems() {
		urf.getJcbRepairItem().removeAllItems();
		List<RepairItemDTO> list = urs.searchRepairItems();
		for (RepairItemDTO item : list) {
			urf.getJcbRepairItem().addItem(item.getRepairItem());
		}
	}

	/** 나의 예약 현황 표를 채운다. (원본 CarReservEvt.drawReservApplyTable) */
	public void searchReservationsByUserId(String userId) {
		DefaultTableModel dtm = urf.getDtmReservation();
		dtm.setRowCount(0);
		List<String[]> reservedRow = null;
		try {
			reservedRow  = urs.searchReservedList(userId);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (reservedRow == null) {
			return;
		}
		for(String[] row : reservedRow) {
			dtm.addRow(row);
		}
	}

	/** 입력한 차량번호가 내 차량이면 차종을 채우고 해당 차량을 반환한다. */
	private CarDTO findCarModel() {
		String carNum = urf.getJtfCarNum().getText().trim();
		if (carNum.isEmpty()) {
			urf.getJtfCarModel().setText("");
			return null;
		}

		List<CarDTO> myCars = new UserCarService().searchCarByUserId(userId);
		for (CarDTO car : myCars) {
			if (carNum.equals(car.getCarNum())) {
				urf.getJtfCarModel().setText(car.getCarModel());
				return car;
			}
		}
		urf.getJtfCarModel().setText("");
		return null;
	}

	/** 예약 추가 */
	public void addReservation() {
		String carNum = urf.getJtfCarNum().getText().trim();
		String dateText = urf.getJtfReserveDate().getText().trim();
		String request = urf.getJtaRequest().getText().trim();
		String repairItem = (String) urf.getJcbRepairItem().getSelectedItem();

		if (carNum.isEmpty() || dateText.isEmpty() || repairItem == null) {
			JOptionPane.showMessageDialog(urf, "차량번호, 예약희망일, 정비항목을 입력해주세요.");
			return;
		}

		CarDTO car = findCarModel();
		if (car == null) {
			JOptionPane.showMessageDialog(urf, "내 차량으로 등록된 차량번호가 아닙니다.");
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		Date reserveDate = null;
		try {
			reserveDate = sdf.parse(dateText);
		} catch (ParseException pe) {
			JOptionPane.showMessageDialog(urf, "예약희망일은 yyyy-MM-dd 형식으로 입력해주세요.");
			return;
		}

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		if (reserveDate.before(today.getTime())) {
			JOptionPane.showMessageDialog(urf, "오늘 이후의 날짜를 입력해주세요.");
			return;
		}

		ReservationDTO rDTO = new ReservationDTO();
		rDTO.setUserId(userId);
		rDTO.setVehicleId(car == null ? 0 : car.getVehicleId());
		rDTO.setCarNum(carNum);
		rDTO.setPreferredReservationDate(reserveDate);
		rDTO.setRepairItem(repairItem);
		rDTO.setAdditionalRequest(request);

		boolean addResult = false;
		try {
			addResult = urs.addReservation(rDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!addResult) {
			JOptionPane.showMessageDialog(urf, "예약이 실패 했습니다");
			return;
		}
		JOptionPane.showMessageDialog(urf, "예약이 성공 했습니다");
		urf.getJtfReserveDate().setText("");
		urf.getJtaRequest().setText("");
		searchReservationsByUserId(userId);
	}

	/** 예약기록 화면으로 이동 (탭 2번 자리에 교체) */
	public void moveReservationList() {
		if (jtpUser != null && jtpUser.getTabCount() > 2) {
			jtpUser.setComponentAt(2, new UserReservationListForm(umt, jtpUser, userId));
			jtpUser.revalidate();
			jtpUser.repaint();
		}
	}
}
