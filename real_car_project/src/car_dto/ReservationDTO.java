package car_dto;

import java.util.Date;

public class ReservationDTO {

	private int vehicleId; // 차량등록 ID
	private String userId; // 유저 ID
	private String carNum; // 차량번호
	private Date preferredReservationDate; // 예약희망일
	private String reservationCode; // 예약 번호
	private String carModel; // 차량 종류
	private Date confirmedDate; // 예약확정일
	private String repairItem; // 선택된 정비 항목
	private String additionalRequest; // 요청사항
	private String reserveState; // 예약상태
	private int reserveStateNum; // 예약상태 번호 0 - 예약 대기, 1 - 예약 확정, 2 - 예약 취소, 3 - 예약취소 확정
	private String userName; // 사용자 이름

	public ReservationDTO() {
		super();
	}

	public ReservationDTO(String reservationCode, String userId, int vehicleId, String carNum, String carModel,
			Date preferredReservationDate, Date confirmedDate, String repairItem, String additionalRequest,
			String reserveState, int reserveStateNum, String userName) {
		super();
		this.reservationCode = reservationCode;
		this.userId = userId;
		this.vehicleId = vehicleId;
		this.carNum = carNum;
		this.carModel = carModel;
		this.preferredReservationDate = preferredReservationDate;
		this.confirmedDate = confirmedDate;
		this.repairItem = repairItem;
		this.additionalRequest = additionalRequest;
		this.reserveState = reserveState;
		this.reserveStateNum = reserveStateNum;
		this.userName = userName;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public Date getPreferredReservationDate() {
		return preferredReservationDate;
	}

	public void setPreferredReservationDate(Date preferredReservationDate) {
		this.preferredReservationDate = preferredReservationDate;
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public String getRepairItem() {
		return repairItem;
	}

	public void setRepairItem(String repairItem) {
		this.repairItem = repairItem;
	}

	public String getAdditionalRequest() {
		return additionalRequest;
	}

	public void setAdditionalRequest(String additionalRequest) {
		this.additionalRequest = additionalRequest;
	}

	public String getReserveState() {
		return reserveState;
	}

	public void setReserveState(String reserveState) {
		this.reserveState = reserveState;
	}

	public int getReserveStateNum() {
		return reserveStateNum;
	}

	public void setReserveStateNum(int reserveStateNum) {
		this.reserveStateNum = reserveStateNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "ReservationDTO [reservationCode=" + reservationCode + ", userId=" + userId + ", vehicleId="
				+ vehicleId + ", carNum=" + carNum + ", repairItem=" + repairItem + ", reserveState=" + reserveState
				+ "]";
	}

}
