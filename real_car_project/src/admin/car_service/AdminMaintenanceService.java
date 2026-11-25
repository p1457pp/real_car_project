package admin.car_service;

import java.util.List;

import car_dao.MaintenanceDAO;
import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import car_dto.RepairItemDTO;
import car_dto.SettlementDTO;

public class AdminMaintenanceService {

	/**
	 * 정비 정보 수정. reportNo 가 0 이하이면 신규 등록, 아니면 수정
	 * 
	 * @return 1: 성공, 0: 실패
	 */
	public int modifyMaintenance(MaintenanceDTO mDTO) {
		MaintenanceDAO mDAO = MaintenanceDAO.getInstance();
		boolean flag;
		if (mDTO.getReportNo() > 0) {
			flag = mDAO.updateMaintenance(mDTO.getReportNo(), mDTO.getCarNum(), mDTO.getMaintDate(),
					mDTO.getTotalPrice(), mDTO.getStatus(), mDTO.getMechanicMemo(), mDTO.getMaintenanceInfo());
		} else {
			flag = mDAO.insertMaintenance(mDTO.getCarNum(), mDTO.getMaintDate(), mDTO.getCenterName(),
					mDTO.getTotalPrice(), mDTO.getStatus(), mDTO.getMechanicMemo(), mDTO.getMaintenanceInfo());
		}
		return flag ? 1 : 0;
	}

	/** 정비완료 처리 (DAO 에서 상태를 정비완료(1)로 저장) */
	public int completeMaintenance(MaintenanceDTO mDTO) {
		mDTO.setStatus(1);
		return modifyMaintenance(mDTO);
	}

	/** 특정 차량 정비내역 */
	public List<MaintenanceDTO> searchByVehicleId(int vehicleId) {
		return MaintenanceDAO.getInstance().selectByVehicleId(vehicleId);
	}

	/**
	 * 모든 사용자 정비 내역
	 * 
	 * @param startDate yyyy.MM.dd
	 * @param endDate   yyyy.MM.dd
	 * @param keyword   차량번호 또는 고객명 (비우면 전체)
	 */
	public List<MaintenanceDTO> searchAllMaintenance(String startDate, String endDate, String keyword) {
		return MaintenanceDAO.getInstance().selectAllMaintenance(startDate, endDate, keyword);
	}

	/** 정비 항목 */
	public List<RepairItemDTO> searchAllRepairItems() {
		return MaintenanceDAO.getInstance().selectAllRepairItems();
	}

	/** 정비 상세 정보 (보고서 1건) */
	public MaintenanceDTO searchMaintenanceDetail(int reportNo) {
		return MaintenanceDAO.getInstance().selectMaintenanceDetail(reportNo);
	}

	/** 정비 상세 항목 (품목별) */
	public List<MaintenanceDetailDTO> searchMaintenanceItems(int reportNo) {
		return MaintenanceDAO.getInstance().selectMaintDetailsByReportNo(reportNo);
	}

	/** 차량번호로 고객/차종 조회. 없으면 null */
	public String searchCarOwnerInfo(String carNum) {
		return MaintenanceDAO.getInstance().findCarOwnerInfo(carNum);
	}

	/** 총 금액 계산 : 항목별 (부품단가 x 수량 + 공임비) 의 합 */
	public int calcTotalPrice(MaintenanceDTO mDTO) {
		int sum = 0;
		if (mDTO.getMaintenanceInfo() != null) {
			for (MaintenanceDetailDTO d : mDTO.getMaintenanceInfo()) {
				int qty = d.getQty() <= 0 ? 1 : d.getQty();
				sum += d.getItemPrice() * qty + d.getLabCost();
			}
		}
		return sum;
	}

	/** 월말 정산 (yyyy-MM) */
	public SettlementDTO searchAllRevenue(String yearMonth) {
		return MaintenanceDAO.getInstance().selectAllRevenue(yearMonth);
	}

}