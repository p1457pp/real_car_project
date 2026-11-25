package admin.car_evt;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import admin.car_service.AdminMaintenanceService;
import admin.car_view.AdminMaintenanceDetailForm;
import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import car_dto.RepairItemDTO;

public class AdminMaintenanceDetailFormEvt extends WindowAdapter implements ActionListener, TableModelListener {

	private AdminMaintenanceDetailForm amdf;
	private AdminMaintenanceService ams;
	private int reportNo;
	private List<RepairItemDTO> repairItems = new ArrayList<RepairItemDTO>();
	private boolean isUpdatingTable = false;
	private int totalAmount = 0;
	private final DecimalFormat priceFormat = new DecimalFormat("#,###");

	public AdminMaintenanceDetailFormEvt(AdminMaintenanceDetailForm amdf, int reportNo) {
		this.amdf = amdf;
		this.reportNo = reportNo;
		this.ams = new AdminMaintenanceService();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		amdf.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == amdf.getJbtnSearch()) {
			searchCarOwner();
		}
		if (e.getSource() == amdf.getJbtnAddItem()) {
			addRepairItemRow();
		}
		if (e.getSource() == amdf.getJbtnDeleteItem()) {
			removeSelectedItemRow();
		}
		if (e.getSource() == amdf.getJbtnComplete()) {
			completeMaintenance();
		}
		if (e.getSource() == amdf.getJbtnCancel()) {
			amdf.dispose();
		}
	}

	/** 표 셀(부품단가/수량/공임비)이 수정되면 행 합계를 다시 계산 */
	@Override
	public void tableChanged(TableModelEvent e) {
		if (isUpdatingTable || e.getType() != TableModelEvent.UPDATE) {
			return;
		}
		int row = e.getFirstRow();
		int col = e.getColumn();
		if (row < 0 || (col != 1 && col != 2 && col != 3)) {
			return;
		}
		updateRowCalculation(row);
	}

	/** 정비 항목 가져오기 */
	public void searchAllRepairItems() {
		repairItems = ams.searchAllRepairItems();
		amdf.getJcbRepairItem().removeAllItems();
		for (RepairItemDTO item : repairItems) {
			amdf.getJcbRepairItem().addItem(item.getRepairItem());
		}
	}

	/** 기존 정비 상세 내역 불러오기 */
	public void searchMaintenanceDetail(int reportNo) {
		MaintenanceDTO report = ams.searchMaintenanceDetail(reportNo);
		if (report != null) {
			amdf.getJtfCarNum().setText(report.getCarNum() != null ? report.getCarNum() : "");
			amdf.getJtfCarNum().setEditable(false);
			amdf.getJbtnSearch().setEnabled(false);

			amdf.getJlblOwnerInfo().setText((report.getUserName() != null ? report.getUserName() : "고객") + " ("
					+ (report.getCarModel() != null ? report.getCarModel() : "-") + ")");
			amdf.getJlblOwnerInfo().setForeground(new Color(22, 101, 52));

			if (report.getMaintDate() != null) {
				amdf.getJtfMaintenanceDate().setText(report.getMaintDate().replace(".", "-"));
			}
			if (report.getCenterName() != null) {
				amdf.getJcbCenter().setSelectedItem(report.getCenterName());
			}
			amdf.getJtaMemo().setText(report.getMechanicMemo() != null ? report.getMechanicMemo() : "");
		}

		isUpdatingTable = true;
		try {
			amdf.getDtmItems().setRowCount(0);
			for (MaintenanceDetailDTO d : ams.searchMaintenanceItems(reportNo)) {
				amdf.getDtmItems().addRow(new Object[] { d.getItemName(), priceFormat.format(d.getItemPrice()) + "원",
						d.getQty(), priceFormat.format(d.getLabCost()) + "원",
						priceFormat.format(d.getTotalPrice()) + "원" });
			}
		} finally {
			isUpdatingTable = false;
		}
		calcTotal();
	}

	/** 차량번호로 고객/차종 조회 */
	public void searchCarOwner() {
		String carNum = amdf.getJtfCarNum().getText().trim();
		if (carNum.isEmpty()) {
			JOptionPane.showMessageDialog(amdf, "차량번호를 입력해 주세요.");
			return;
		}
		String info = ams.searchCarOwnerInfo(carNum);
		if (info != null) {
			amdf.getJlblOwnerInfo().setText(info);
			amdf.getJlblOwnerInfo().setForeground(new Color(22, 101, 52));
		} else {
			amdf.getJlblOwnerInfo().setText("등록되지 않은 차량번호입니다.");
			amdf.getJlblOwnerInfo().setForeground(new Color(220, 38, 38));
		}
	}

	private void addRepairItemRow() {
		String selected = (String) amdf.getJcbRepairItem().getSelectedItem();
		if (selected == null || "선택 안함".equals(selected)) {
			JOptionPane.showMessageDialog(amdf, "추가할 정비 항목을 선택해 주세요.");
			return;
		}

		int unitPrice = 0;
		int labCost = 0;
		for (RepairItemDTO item : repairItems) {
			if (item.getRepairItem().equals(selected)) {
				unitPrice = item.getItemPrice();
				labCost = item.getLaborCost();
				break;
			}
		}
		int qty = (Integer) amdf.getJspQty().getValue();
		int rowTotal = unitPrice * qty + labCost;

		isUpdatingTable = true;
		try {
			amdf.getDtmItems().addRow(new Object[] { selected, priceFormat.format(unitPrice) + "원", qty,
					priceFormat.format(labCost) + "원", priceFormat.format(rowTotal) + "원" });
		} finally {
			isUpdatingTable = false;
		}
		calcTotal();
	}

	private void removeSelectedItemRow() {
		int row = amdf.getJtItems().getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(amdf, "삭제할 항목을 표에서 먼저 선택해 주세요.");
			return;
		}
		isUpdatingTable = true;
		try {
			amdf.getDtmItems().removeRow(row);
		} finally {
			isUpdatingTable = false;
		}
		calcTotal();
	}

	private void updateRowCalculation(int row) {
		DefaultTableModel dtm = amdf.getDtmItems();
		isUpdatingTable = true;
		try {
			int unitPrice = parseDigits(dtm.getValueAt(row, 1), 0);
			int qty = parseDigits(dtm.getValueAt(row, 2), 1);
			if (qty <= 0) {
				qty = 1;
			}
			int labCost = parseDigits(dtm.getValueAt(row, 3), 0);
			int rowTotal = unitPrice * qty + labCost;

			dtm.setValueAt(priceFormat.format(unitPrice) + "원", row, 1);
			dtm.setValueAt(qty, row, 2);
			dtm.setValueAt(priceFormat.format(labCost) + "원", row, 3);
			dtm.setValueAt(priceFormat.format(rowTotal) + "원", row, 4);
		} finally {
			isUpdatingTable = false;
		}
		calcTotal();
	}

	/** 총 금액 계산 */
	public void calcTotal() {
		int sum = 0;
		DefaultTableModel dtm = amdf.getDtmItems();
		for (int i = 0; i < dtm.getRowCount(); i++) {
			sum += parseDigits(dtm.getValueAt(i, 4), 0);
		}
		totalAmount = sum;
		amdf.getJlblTotal().setText("총 청구 금액 : " + priceFormat.format(totalAmount) + "원");
	}

	private int parseDigits(Object val, int defaultVal) {
		if (val == null) {
			return defaultVal;
		}
		String digits = val.toString().replaceAll("[^0-9]", "");
		if (digits.isEmpty()) {
			return defaultVal;
		}
		try {
			return Integer.parseInt(digits);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	/** 표 내용으로 정비 DTO를 만든다. */
	private MaintenanceDTO makeDTO() {
		if (amdf.getJtItems().isEditing()) {
			amdf.getJtItems().getCellEditor().stopCellEditing();
		}

		DefaultTableModel dtm = amdf.getDtmItems();
		List<MaintenanceDetailDTO> details = new ArrayList<MaintenanceDetailDTO>();
		for (int i = 0; i < dtm.getRowCount(); i++) {
			details.add(new MaintenanceDetailDTO(dtm.getValueAt(i, 0).toString(), parseDigits(dtm.getValueAt(i, 1), 0),
					parseDigits(dtm.getValueAt(i, 3), 0), parseDigits(dtm.getValueAt(i, 2), 1)));
		}

		MaintenanceDTO mDTO = new MaintenanceDTO();
		mDTO.setReportNo(reportNo);
		mDTO.setCarNum(amdf.getJtfCarNum().getText().trim());
		mDTO.setMaintDate(amdf.getJtfMaintenanceDate().getText().trim().replace(".", "-"));
		mDTO.setCenterName((String) amdf.getJcbCenter().getSelectedItem());
		mDTO.setMechanicMemo(amdf.getJtaMemo().getText().trim());
		mDTO.setMaintenanceInfo(details);
		mDTO.setTotalPrice(ams.calcTotalPrice(mDTO));
		return mDTO;
	}

	/** 정비 내역 수정 (저장) */
	public void modifyMaintenance() {
		save(false);
	}

	/** 정비완료로 수정 (저장) */
	public void completeMaintenance() {
		save(true);
	}

	private void save(boolean complete) {
		MaintenanceDTO mDTO = makeDTO();

		if (mDTO.getCarNum().isEmpty()) {
			JOptionPane.showMessageDialog(amdf, "차량번호를 입력해 주세요.");
			return;
		}
		if (mDTO.getMaintDate().isEmpty()) {
			JOptionPane.showMessageDialog(amdf, "정비일자를 YYYY-MM-DD 형식으로 입력해 주세요.");
			return;
		}
		if (reportNo <= 0 && ams.searchCarOwnerInfo(mDTO.getCarNum()) == null) {
			JOptionPane.showMessageDialog(amdf, "DB에 등록된 차량번호가 아닙니다. 차량 조회를 먼저 진행해 주세요.");
			return;
		}

		int cnt = complete ? ams.completeMaintenance(mDTO) : ams.modifyMaintenance(mDTO);
		if (cnt == 1) {
			JOptionPane.showMessageDialog(amdf, "정비완료 처리 및 저장이 완료되었습니다.");
			amdf.dispose();
		} else {
			JOptionPane.showMessageDialog(amdf, "저장 중 오류가 발생했습니다. 콘솔 로그를 확인해 주세요.");
		}
	}
}
