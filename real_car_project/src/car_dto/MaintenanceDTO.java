package car_dto;

public class MaintenanceDTO {
    private int reportNo;
    private String maintDate;
    private String centerName;
    private int totalPrice;
    private int status;
    private String statusName;
    private String mechanicMemo;
    private String registDate;
    private String reservationCode;

    private String carNum;
    private String carModel;
    private String userId;
    private String userName;
    private String summaryItem;
    private java.util.List<MaintenanceDetailDTO> maintenanceInfo; // 정비 상세 항목

    public int getReportNo() { return reportNo; }
    public void setReportNo(int reportNo) { this.reportNo = reportNo; }

    public String getMaintDate() { return maintDate; }
    public void setMaintDate(String maintDate) { this.maintDate = maintDate; }

    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }

    public String getMechanicMemo() { return mechanicMemo; }
    public void setMechanicMemo(String mechanicMemo) { this.mechanicMemo = mechanicMemo; }

    public String getRegistDate() { return registDate; }
    public void setRegistDate(String registDate) { this.registDate = registDate; }

    public String getReservationCode() { return reservationCode; }
    public void setReservationCode(String reservationCode) { this.reservationCode = reservationCode; }

    public String getCarNum() { return carNum; }
    public void setCarNum(String carNum) { this.carNum = carNum; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getSummaryItem() { return summaryItem; }
    public void setSummaryItem(String summaryItem) { this.summaryItem = summaryItem; }

    public java.util.List<MaintenanceDetailDTO> getMaintenanceInfo() { return maintenanceInfo; }
    public void setMaintenanceInfo(java.util.List<MaintenanceDetailDTO> maintenanceInfo) { this.maintenanceInfo = maintenanceInfo; }
}
