package admin.car_view;

import java.awt.Font;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.AdminMainTab;
import admin.car_evt.AdminMaintenanceDetailFormEvt;
import car_util.UiStyle;

public class AdminMaintenanceDetailForm extends JDialog {

	private JTextField jtfCarNum, jtfMaintenanceDate;
	private JButton jbtnSearch, jbtnAddItem, jbtnDeleteItem, jbtnComplete, jbtnCancel;
	private JLabel jlblOwnerInfo, jlblTotal;
	private JComboBox<String> jcbCenter, jcbRepairItem, jcbStatus;
	private JSpinner jspQty;
	private JTable jtItems;
	private DefaultTableModel dtmItems;
	private JTextArea jtaMemo;

	public AdminMaintenanceDetailForm(AdminMainTab amt, int reportNo) {
		super(amt, reportNo > 0 ? "정비 상세 내역 및 완료 처리" : "신규 정비 내역 등록", true);
		setLayout(null);

		JLabel jlblTitle = new JLabel(reportNo > 0 ? "정비 상세 내역 (No. " + reportNo + ")" : "신규 정비 내역 등록");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(20, 15, 400, 25);
		add(jlblTitle);

		// 차량 조회
		add(createLabel("차량번호", 20, 55, 70));
		jtfCarNum = new JTextField();
		jtfCarNum.setBounds(95, 55, 130, 28);
		add(jtfCarNum);

		jbtnSearch = new JButton("차량조회");
		jbtnSearch.setBounds(235, 55, 90, 28);
		add(jbtnSearch);

		jlblOwnerInfo = new JLabel("차량번호를 입력 후 조회하세요");
		jlblOwnerInfo.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		jlblOwnerInfo.setBounds(335, 55, 340, 28);
		add(jlblOwnerInfo);

		// 정비일자 / 정비지점 / 진행상태
		add(createLabel("정비일자", 20, 95, 65));
		jtfMaintenanceDate = new JTextField(LocalDate.now().toString());
		jtfMaintenanceDate.setBounds(85, 95, 115, 28);
		add(jtfMaintenanceDate);

		add(createLabel("정비지점", 225, 95, 65));
		jcbCenter = new JComboBox<String>(new String[] { "강남점" });
		jcbCenter.setBounds(290, 95, 115, 28);
		add(jcbCenter);

		add(createLabel("진행상태", 430, 95, 65));
		jcbStatus = new JComboBox<String>(new String[] { "대기", "정비완료" });
		jcbStatus.setBounds(495, 95, 115, 28);
		if (reportNo == 0) {
			jcbStatus.setSelectedItem("대기");
		}
		add(jcbStatus);

		// 정비 항목 추가
		add(createLabel("정비항목", 20, 140, 70));
		jcbRepairItem = new JComboBox<String>();
		jcbRepairItem.setBounds(95, 140, 190, 28);
		add(jcbRepairItem);

		add(createLabel("수량", 300, 140, 40));
		jspQty = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
		jspQty.setBounds(340, 140, 55, 28);
		add(jspQty);

		jbtnAddItem = new JButton("항목추가");
		jbtnAddItem.setBounds(410, 140, 90, 28);
		add(jbtnAddItem);

		jbtnDeleteItem = new JButton("선택삭제");
		jbtnDeleteItem.setBounds(510, 140, 90, 28);
		add(jbtnDeleteItem);

		String[] cols = { "정비 항목", "부품단가", "수량", "공임비", "합계" };
		dtmItems = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1 || column == 2 || column == 3;
			}
		};
		jtItems = new JTable(dtmItems);
		jtItems.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		jtItems.setRowHeight(30);
		jtItems.getTableHeader().setReorderingAllowed(false);
		DefaultTableCellRenderer center = (DefaultTableCellRenderer) jtItems.getDefaultRenderer(Object.class);
		center.setHorizontalAlignment(JLabel.CENTER);

		JScrollPane jsp = new JScrollPane(jtItems);
		jsp.setBounds(20, 180, 640, 200);
		add(jsp);

		jlblTotal = new JLabel("총 청구 금액 : 0원");
		jlblTotal.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		jlblTotal.setBounds(400, 388, 260, 25);
		add(jlblTotal);

		JLabel lblMemo = createLabel("정비사 코멘트 / 특이사항", 20, 388, 200);
		add(lblMemo);

		jtaMemo = new JTextArea();
		jtaMemo.setLineWrap(true);
		jtaMemo.setWrapStyleWord(true);
		JScrollPane jspMemo = new JScrollPane(jtaMemo);
		jspMemo.setBounds(20, 418, 640, 100);
		add(jspMemo);

		jbtnComplete = new JButton("저장");
		jbtnComplete.setBounds(230, 540, 100, 30);
		add(jbtnComplete);

		jbtnCancel = new JButton("취소");
		jbtnCancel.setBounds(340, 540, 100, 30);
		add(jbtnCancel);

		AdminMaintenanceDetailFormEvt evt = new AdminMaintenanceDetailFormEvt(this, reportNo);
		jbtnSearch.addActionListener(evt);
		jbtnAddItem.addActionListener(evt);
		jbtnDeleteItem.addActionListener(evt);
		jbtnComplete.addActionListener(evt);
		jbtnCancel.addActionListener(evt);
		addWindowListener(evt);
		dtmItems.addTableModelListener(evt);

		evt.searchAllRepairItems();
		if (reportNo > 0) {
			evt.searchMaintenanceDetail(reportNo);
		}

		setSize(700, 640);
		setResizable(false);
		setLocationRelativeTo(amt);
		UiStyle.apply(this);

		setVisible(true);
	}

	private JLabel createLabel(String text, int x, int y, int w) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl.setBounds(x, y, w, 28);
		return lbl;
	}

	public JTextField getJtfCarNum() {
		return jtfCarNum;
	}

	public JTextField getJtfMaintenanceDate() {
		return jtfMaintenanceDate;
	}

	public JButton getJbtnSearch() {
		return jbtnSearch;
	}

	public JButton getJbtnAddItem() {
		return jbtnAddItem;
	}

	public JButton getJbtnDeleteItem() {
		return jbtnDeleteItem;
	}

	public JButton getJbtnComplete() {
		return jbtnComplete;
	}

	public JButton getJbtnCancel() {
		return jbtnCancel;
	}

	public JLabel getJlblOwnerInfo() {
		return jlblOwnerInfo;
	}

	public JLabel getJlblTotal() {
		return jlblTotal;
	}

	public JComboBox<String> getJcbCenter() {
		return jcbCenter;
	}

	public JComboBox<String> getJcbRepairItem() {
		return jcbRepairItem;
	}

	public JComboBox<String> getJcbStatus() {
		return jcbStatus;
	}

	public JSpinner getJspQty() {
		return jspQty;
	}

	public JTable getJtItems() {
		return jtItems;
	}

	public DefaultTableModel getDtmItems() {
		return dtmItems;
	}

	public JTextArea getJtaMemo() {
		return jtaMemo;
	}
}