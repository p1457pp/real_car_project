package admin.car_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import car_dao.MaintDAO;
import car_dto.MaintDetailDTO;
import car_dto.MaintReportDTO;

public class AdminMaintenanceDetailForm extends JDialog {
    private static final long serialVersionUID = 1L;

    private AdminMaintenanceForm parentForm;
    private int reportNo;
    private MaintDAO maintDAO;
    private Map<String, int[]> itemPriceMap;

    private JTextField jtfCarNum;
    private JButton jbtnFindCar;
    private JLabel lblOwnerInfo;
    private JTextField jtfMaintDate;
    private JComboBox<String> jcbCenter;

    private JComboBox<String> jcbRepairItem;
    private JSpinner jspQty;
    private JTable jtItems;
    private DefaultTableModel dtmItems;

    private JLabel lblTotalAmount;
    private JTextArea jtaMechanicMemo;

    private int totalAmount = 0;
    private boolean isUpdatingTable = false;
    private final DecimalFormat priceFormat = new DecimalFormat("#,###");

    public AdminMaintenanceDetailForm(AdminMaintenanceForm parent, int reportNo) {
        super(parent, reportNo > 0 ? "정비 상세 내역 및 완료 처리" : "신규 정비 내역 등록", true);
        this.parentForm = parent;
        this.reportNo = reportNo;
        this.maintDAO = new MaintDAO();
        this.itemPriceMap = maintDAO.selectRepairItemPrices();

        setSize(730, 680);
        setLocationRelativeTo(parent);

        initComponents();
        if (reportNo > 0) {
            loadExistingData();
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 14));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(22, 24, 20, 24));

        JPanel topSection = new JPanel(new BorderLayout(0, 12));
        topSection.setOpaque(false);

        JLabel lblTitle = new JLabel(reportNo > 0 ? "정비 상세 내역 (No. " + reportNo + ")" : "신규 정비 내역 등록");
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 41, 59));
        topSection.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 250, 252));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 14, 12, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.15;
        formPanel.add(createLabel("차량번호"), gbc);

        JPanel carSearchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        carSearchBox.setOpaque(false);
        jtfCarNum = new JTextField(11);
        jtfCarNum.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtfCarNum.setPreferredSize(new Dimension(120, 30));

        jbtnFindCar = createRoundedButton("차량 조회", new Color(71, 85, 105), Color.WHITE, 92, 30);
        jbtnFindCar.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        jbtnFindCar.addActionListener(e -> searchCarOwner());

        lblOwnerInfo = new JLabel("차량번호를 입력 후 조회하세요");
        lblOwnerInfo.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        lblOwnerInfo.setForeground(new Color(100, 116, 139));

        carSearchBox.add(jtfCarNum);
        carSearchBox.add(jbtnFindCar);
        carSearchBox.add(lblOwnerInfo);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 0.85;
        formPanel.add(carSearchBox, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.15;
        formPanel.add(createLabel("정비일자"), gbc);

        jtfMaintDate = new JTextField(LocalDate.now().toString(), 10);
        jtfMaintDate.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtfMaintDate.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.35;
        formPanel.add(jtfMaintDate, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.15;
        formPanel.add(createLabel("정비지점"), gbc);

        jcbCenter = new JComboBox<>(new String[]{"강남점", "마포점", "서초점", "송파점", "영등포점", "강서점", "용산점"});
        jcbCenter.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jcbCenter.setBackground(Color.WHITE);
        jcbCenter.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.35;
        formPanel.add(jcbCenter, gbc);

        topSection.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(topSection, BorderLayout.NORTH);

        JPanel centerSection = new JPanel(new BorderLayout(0, 10));
        centerSection.setOpaque(false);

        JPanel itemControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        itemControlPanel.setBackground(new Color(241, 245, 249));
        itemControlPanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        JLabel lblItem = new JLabel("정비항목");
        lblItem.setFont(new Font("맑은 고딕", Font.BOLD, 13));

        jcbRepairItem = new JComboBox<>();
        jcbRepairItem.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jcbRepairItem.setPreferredSize(new Dimension(190, 30));
        jcbRepairItem.setBackground(Color.WHITE);
        for (String itemName : itemPriceMap.keySet()) {
            jcbRepairItem.addItem(itemName);
        }

        JLabel lblQty = new JLabel("수량");
        lblQty.setFont(new Font("맑은 고딕", Font.BOLD, 13));

        jspQty = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        jspQty.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jspQty.setPreferredSize(new Dimension(55, 30));

        JButton jbtnAddItem = createRoundedButton("항목 추가", new Color(37, 99, 235), Color.WHITE, 92, 30);
        jbtnAddItem.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        jbtnAddItem.addActionListener(e -> addRepairItemRow());

        JButton jbtnRemoveItem = createRoundedButton("선택 삭제", new Color(239, 68, 68), Color.WHITE, 92, 30);
        jbtnRemoveItem.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        jbtnRemoveItem.addActionListener(e -> removeSelectedItemRow());

        itemControlPanel.add(lblItem);
        itemControlPanel.add(jcbRepairItem);
        itemControlPanel.add(lblQty);
        itemControlPanel.add(jspQty);
        itemControlPanel.add(jbtnAddItem);
        itemControlPanel.add(jbtnRemoveItem);

        centerSection.add(itemControlPanel, BorderLayout.NORTH);

        String[] cols = {"정비 항목", "부품단가", "수량", "공임비", "합계"};
        dtmItems = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3;
            }
        };

        dtmItems.addTableModelListener(e -> {
            if (isUpdatingTable || e.getType() != TableModelEvent.UPDATE) {
                return;
            }
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row < 0 || (col != 1 && col != 2 && col != 3)) {
                return;
            }
            updateRowCalculation(row);
        });

        jtItems = new JTable(dtmItems);
        jtItems.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        jtItems.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtItems.setRowHeight(34);
        jtItems.setShowVerticalLines(false);
        jtItems.setGridColor(new Color(226, 232, 240));

        JTableHeader th = jtItems.getTableHeader();
        th.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        th.setBackground(new Color(248, 250, 252));
        th.setPreferredSize(new Dimension(0, 34));

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(SwingConstants.RIGHT);
        rightR.setBorder(new EmptyBorder(0, 0, 0, 10));

        jtItems.getColumnModel().getColumn(0).setPreferredWidth(220);
        jtItems.getColumnModel().getColumn(1).setPreferredWidth(110);
        jtItems.getColumnModel().getColumn(1).setCellRenderer(rightR);
        jtItems.getColumnModel().getColumn(2).setPreferredWidth(65);
        jtItems.getColumnModel().getColumn(2).setCellRenderer(centerR);
        jtItems.getColumnModel().getColumn(3).setPreferredWidth(110);
        jtItems.getColumnModel().getColumn(3).setCellRenderer(rightR);
        jtItems.getColumnModel().getColumn(4).setPreferredWidth(125);
        jtItems.getColumnModel().getColumn(4).setCellRenderer(rightR);

        JScrollPane tableScroll = new JScrollPane(jtItems);
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        centerSection.add(tableScroll, BorderLayout.CENTER);

        JPanel bottomCenterPanel = new JPanel(new BorderLayout(0, 10));
        bottomCenterPanel.setOpaque(false);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        totalPanel.setBackground(new Color(239, 246, 255));
        totalPanel.setBorder(BorderFactory.createLineBorder(new Color(191, 219, 254), 1));
        lblTotalAmount = new JLabel("총 청구 금액 : 0원");
        lblTotalAmount.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblTotalAmount.setForeground(new Color(29, 78, 216));
        totalPanel.add(lblTotalAmount);
        bottomCenterPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel memoPanel = new JPanel(new BorderLayout(0, 6));
        memoPanel.setOpaque(false);
        JLabel lblMemo = new JLabel("정비사 코멘트 / 특이사항");
        lblMemo.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblMemo.setForeground(new Color(71, 85, 105));

        jtaMechanicMemo = new JTextArea(4, 20);
        jtaMechanicMemo.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtaMechanicMemo.setLineWrap(true);
        jtaMechanicMemo.setWrapStyleWord(true);
        jtaMechanicMemo.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane memoScroll = new JScrollPane(jtaMechanicMemo);
        memoScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        memoPanel.add(lblMemo, BorderLayout.NORTH);
        memoPanel.add(memoScroll, BorderLayout.CENTER);
        bottomCenterPanel.add(memoPanel, BorderLayout.CENTER);

        centerSection.add(bottomCenterPanel, BorderLayout.SOUTH);
        mainPanel.add(centerSection, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);

        JButton jbtnComplete = createRoundedButton("정비완료", new Color(37, 99, 235), Color.WHITE, 120, 38);
        jbtnComplete.addActionListener(e -> saveAndCompleteMaintenance());

        JButton jbtnCancel = createRoundedButton("취소", new Color(100, 116, 139), Color.WHITE, 100, 38);
        jbtnCancel.addActionListener(e -> dispose());

        buttonPanel.add(jbtnComplete);
        buttonPanel.add(jbtnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void updateRowCalculation(int row) {
        isUpdatingTable = true;
        try {
            int unitPrice = parseDigits(dtmItems.getValueAt(row, 1), 0);
            int qty = parseDigits(dtmItems.getValueAt(row, 2), 1);
            if (qty <= 0) {
                qty = 1;
            }
            int labCost = parseDigits(dtmItems.getValueAt(row, 3), 0);
            int rowTotal = (unitPrice * qty) + labCost;

            dtmItems.setValueAt(priceFormat.format(unitPrice) + "원", row, 1);
            dtmItems.setValueAt(qty, row, 2);
            dtmItems.setValueAt(priceFormat.format(labCost) + "원", row, 3);
            dtmItems.setValueAt(priceFormat.format(rowTotal) + "원", row, 4);
            recalculateTotal();
        } finally {
            isUpdatingTable = false;
        }
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

    private void loadExistingData() {
        MaintReportDTO report = maintDAO.selectMaintReportByNo(reportNo);
        if (report != null) {
            jtfCarNum.setText(report.getCarNum() != null ? report.getCarNum() : "");
            jtfCarNum.setEditable(false);
            jbtnFindCar.setEnabled(false);

            String ownerText = (report.getUserName() != null ? report.getUserName() : "고객")
                    + " (" + (report.getCarModel() != null ? report.getCarModel() : "-") + ")";
            lblOwnerInfo.setText(ownerText);
            lblOwnerInfo.setForeground(new Color(22, 101, 52));

            if (report.getMaintDate() != null) {
                jtfMaintDate.setText(report.getMaintDate().replace(".", "-"));
            }
            if (report.getCenterName() != null) {
                jcbCenter.setSelectedItem(report.getCenterName());
            }
            jtaMechanicMemo.setText(report.getMechanicMemo() != null ? report.getMechanicMemo() : "");
        }

        isUpdatingTable = true;
        try {
            dtmItems.setRowCount(0);
            List<MaintDetailDTO> details = maintDAO.selectMaintDetailsByReportNo(reportNo);
            for (MaintDetailDTO d : details) {
                dtmItems.addRow(new Object[]{
                    d.getItemName(),
                    priceFormat.format(d.getItemPrice()) + "원",
                    d.getQty(),
                    priceFormat.format(d.getLabCost()) + "원",
                    priceFormat.format(d.getTotalPrice()) + "원"
                });
            }
        } finally {
            isUpdatingTable = false;
        }
        recalculateTotal();
    }

    private void searchCarOwner() {
        String carNum = jtfCarNum.getText().trim();
        if (carNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "차량번호를 입력해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String info = maintDAO.findCarOwnerInfo(carNum);
        if (info != null) {
            lblOwnerInfo.setText(info);
            lblOwnerInfo.setForeground(new Color(22, 101, 52));
        } else {
            lblOwnerInfo.setText("등록되지 않은 차량번호입니다.");
            lblOwnerInfo.setForeground(new Color(220, 38, 38));
        }
    }

    private void addRepairItemRow() {
        String selectedItem = (String) jcbRepairItem.getSelectedItem();
        if (selectedItem == null || "선택 안함".equals(selectedItem)) {
            JOptionPane.showMessageDialog(this, "추가할 정비 항목을 선택해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int[] prices = itemPriceMap.getOrDefault(selectedItem, new int[]{0, 0});
        int unitPrice = prices[0];
        int labCost = prices[1];
        int qty = (Integer) jspQty.getValue();
        int rowTotal = (unitPrice * qty) + labCost;

        isUpdatingTable = true;
        try {
            dtmItems.addRow(new Object[]{
                selectedItem,
                priceFormat.format(unitPrice) + "원",
                qty,
                priceFormat.format(labCost) + "원",
                priceFormat.format(rowTotal) + "원"
            });
        } finally {
            isUpdatingTable = false;
        }
        recalculateTotal();
    }

    private void removeSelectedItemRow() {
        int selectedRow = jtItems.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 항목을 표에서 먼저 선택해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        isUpdatingTable = true;
        try {
            dtmItems.removeRow(selectedRow);
        } finally {
            isUpdatingTable = false;
        }
        recalculateTotal();
    }

    private void recalculateTotal() {
        int sum = 0;
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            String rowTotalStr = dtmItems.getValueAt(i, 4).toString().replace(",", "").replace("원", "").trim();
            sum += Integer.parseInt(rowTotalStr);
        }
        totalAmount = sum;
        lblTotalAmount.setText("총 청구 금액 : " + priceFormat.format(totalAmount) + "원");
    }

    private void saveAndCompleteMaintenance() {
        if (jtItems.isEditing()) {
            jtItems.getCellEditor().stopCellEditing();
        }

        String carNum = jtfCarNum.getText().trim();
        String maintDate = jtfMaintDate.getText().trim().replace(".", "-");
        String centerName = (String) jcbCenter.getSelectedItem();
        String memo = jtaMechanicMemo.getText().trim();

        if (carNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "차량번호를 입력해 주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (maintDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "정비일자를 YYYY-MM-DD 형식으로 입력해 주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<MaintDetailDTO> detailList = new ArrayList<>();
        for (int i = 0; i < dtmItems.getRowCount(); i++) {
            String itemName = dtmItems.getValueAt(i, 0).toString();
            int unitPrice = parseDigits(dtmItems.getValueAt(i, 1), 0);
            int qty = parseDigits(dtmItems.getValueAt(i, 2), 1);
            int labCost = parseDigits(dtmItems.getValueAt(i, 3), 0);
            detailList.add(new MaintDetailDTO(itemName, unitPrice, labCost, qty));
        }

        boolean success;
        if (reportNo > 0) {
            success = maintDAO.updateAdminMaint(reportNo, carNum, maintDate, totalAmount, memo, detailList);
        } else {
            if (maintDAO.findCarOwnerInfo(carNum) == null) {
                JOptionPane.showMessageDialog(this, "DB에 등록된 차량번호가 아닙니다. 차량 조회를 먼저 진행해 주세요.", "차량 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            success = maintDAO.insertAdminMaint(carNum, maintDate, centerName, totalAmount, memo, detailList);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "정비완료 처리 및 DB 저장이 완료되었습니다.", "완료", JOptionPane.INFORMATION_MESSAGE);
            if (parentForm != null) {
                parentForm.loadMaintenanceData();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "DB 저장 중 오류가 발생했습니다. 콘솔 로그를 확인해 주세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lbl.setForeground(new Color(71, 85, 105));
        return lbl;
    }

    private JButton createRoundedButton(String text, Color bg, Color fg, int width, int height) {
        JButton btn = new JButton(text) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(width, height));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}