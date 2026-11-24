package car_dto;

public class ManufacturerDTO {
	private int manufacturerId;
	private String manufacturer;

	public ManufacturerDTO() {
	}

	public ManufacturerDTO(int manufacturerId, String manufacturer) {
		this.manufacturerId = manufacturerId;
		this.manufacturer = manufacturer;
	}

	public int getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(int manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Override
	public String toString() {
		return manufacturer;
	}
}
