package car_dto;

import java.sql.Date;
import java.util.Arrays;

public class CarDTO {
	private int vehicleId;
	private String carNum;
	private String carModel;
	private int carYear;
	private String fuelType;
	private int mileage;
	private byte[] carPhoto;
	private String carScrapped;
	private Date addDate;
	private int manufacturerId;
	private String manufacturerName;
	private String userId;
	private int isDeleted; // 1:정상, 0:삭제

	
	public CarDTO() {
	}

	public CarDTO(int vehicleId, String carNum, String carModel, int carYear, String fuelType, int mileage,
			byte[] carPhoto, String carScrapped, Date addDate, int manufacturerId, String manufacturerName,
			String userId, int isDeleted) {
		this.vehicleId = vehicleId;
		this.carNum = carNum;
		this.carModel = carModel;
		this.carYear = carYear;
		this.fuelType = fuelType;
		this.mileage = mileage;
		this.carPhoto = carPhoto;
		this.carScrapped = carScrapped;
		this.addDate = addDate;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.userId = userId;
		this.isDeleted = isDeleted;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public int getCarYear() {
		return carYear;
	}

	public void setCarYear(int carYear) {
		this.carYear = carYear;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public byte[] getCarPhoto() {
		return carPhoto;
	}

	public void setCarPhoto(byte[] carPhoto) {
		this.carPhoto = carPhoto;
	}

	public String getCarScrapped() {
		return carScrapped;
	}

	public void setCarScrapped(String carScrapped) {
		this.carScrapped = carScrapped;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public int getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "CarDTO [vehicleId=" + vehicleId + ", carNum=" + carNum + ", carModel=" + carModel + ", carYear="
				+ carYear + ", fuelType=" + fuelType + ", mileage=" + mileage + ", carPhoto="
				+ Arrays.toString(carPhoto) + ", carScrapped=" + carScrapped + ", addDate=" + addDate
				+ ", manufacturerId=" + manufacturerId + ", manufacturerName=" + manufacturerName + ", userId=" + userId
				+ ", deleteState=" + isDeleted + "]";
	}

}
