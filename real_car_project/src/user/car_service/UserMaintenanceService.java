package user.car_service;

import java.util.List;

import car_dao.MaintenanceDAO;
import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import car_dto.RepairItemDTO;

public class UserMaintenanceService {

	/** 나의 정비 디테일 (정비 보고서 1건) */
	public MaintenanceDTO searchMaintenanceDetail(int reportNo) {
		return MaintenanceDAO.getInstance().selectMaintenanceDetail(reportNo);
	}

	/** 나의 정비 상세 항목 (품목별) */
	public List<MaintenanceDetailDTO> searchMaintenanceItems(int reportNo) {
		return MaintenanceDAO.getInstance().selectMaintDetailsByReportNo(reportNo);
	}

	/**
	 * 나의 정비 내역
	 * 
	 * @param userId 회원 아이디
	 * @param period 전체 기간 / 최근 1개월 / 최근 3개월 / 최근 6개월
	 * @param carNum 차량번호, "전체 차량"이면 전체
	 */
	public List<MaintenanceDTO> searchByUserId(String userId, String period, String carNum) {
		return MaintenanceDAO.getInstance().selectUserMaintList(userId, period, carNum);
	}

	/** 나의 차량 정비내역 */
	public List<MaintenanceDTO> searchByVehicleId(int vehicleId) {
		return MaintenanceDAO.getInstance().selectByVehicleId(vehicleId);
	}

	/** 나의 보유 차량 목록 ("차량번호(차종)" 형태) */
	public List<String> searchMyCars(String userId) {
		return MaintenanceDAO.getInstance().selectUserCars(userId);
	}

	/** 정비 항목 */
	public List<RepairItemDTO> searchAllRepairItems() {
		return MaintenanceDAO.getInstance().selectAllRepairItems();
	}

	/** 총 금액 계산 : 항목별 (부품단가 x 수량 + 공임비) 의 합 */
	public int calcTotal(MaintenanceDTO mDTO) {
		return calcTotal(mDTO.getMaintenanceInfo());
	}

	public int calcTotal(List<MaintenanceDetailDTO> details) {
		int sum = 0;
		if (details != null) {
			for (MaintenanceDetailDTO d : details) {
				sum += d.getTotalPrice();
			}
		}
		return sum;
	}

}
