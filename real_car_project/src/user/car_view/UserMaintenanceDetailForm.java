package user.car_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import car_dao.MaintDAO;
import car_dto.MaintDetailDTO;
import car_dto.MaintReportDTO;

public class UserMaintenanceDetailForm extends JDialog {
    private static final long serialVersionUID = 1L;

    private int reportNo;
    private MaintDAO maintDAO;

    private JLabel lblDateVal;
    private JLabel lblCenterVal;
    private JLabel lblCarNumVal;
    private JLabel lblCarModelVal;
    private JLabel lblStatusVal;
    private JLabel lblTotalPriceVal;
    private JTextArea jtaMemo;
    private DefaultTableModel dtmDetail;

    private final DecimalFormat priceFormat = new DecimalFormat("#,###");

    public UserMaintenanceDetailForm(JFrame parent, int reportNo) {
        super(parent, "정비 상세 명세서", true);
        this.reportNo = reportNo;
        this.maintDAO = new MaintDAO();

        setSize(680, 600);
        setLocationRelativeTo(parent);

        initComponents();
        loadDetailData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 14));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(22, 24, 20, 24));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 12));
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("정비 상세 명세서 (No. " + reportNo + ")");
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 41, 59));
        headerPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel infoGrid = new JPanel(new GridLayout(3, 4, 8, 8));
        infoGrid.setBackground(new Color(248, 250, 252));
        infoGrid.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 14, 12, 14)));

        lblDateVal = createValueLabel("-");
        lblCenterVal = createValueLabel("-");
        lblCarNumVal = createValueLabel("-");
        lblCarModelVal = createValueLabel("-");
        lblStatusVal = createValueLabel("-");

        infoGrid.add(createKeyLabel("정비일자"));
        infoGrid.add(lblDateVal);
        infoGrid.add(createKeyLabel("정비지점"));
        infoGrid.add(lblCenterVal);
        infoGrid.add(createKeyLabel("차량번호"));
        infoGrid.add(lblCarNumVal);
        infoGrid.add(createKeyLabel("차종"));
        infoGrid.add(lblCarModelVal);
        infoGrid.add(createKeyLabel("진행상태"));
        infoGrid.add(lblStatusVal);
        infoGrid.add(new JLabel(""));
        infoGrid.add(new JLabel(""));

        headerPanel.add(infoGrid, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setOpaque(false);

        String[] cols = {"정비 항목", "부품단가", "수량", "공임비", "금액"};
        dtmDetail = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable jtDetail = new JTable(dtmDetail);
        jtDetail.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtDetail.setRowHeight(34);
        jtDetail.setShowVerticalLines(false);
        jtDetail.setGridColor(new Color(226, 232, 240));

        JTableHeader th = jtDetail.getTableHeader();
        th.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        th.setBackground(new Color(241, 245, 249));
        th.setPreferredSize(new Dimension(0, 34));

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(SwingConstants.RIGHT);
        rightR.setBorder(new EmptyBorder(0, 0, 0, 10));

        jtDetail.getColumnModel().getColumn(0).setPreferredWidth(200);
        jtDetail.getColumnModel().getColumn(1).setPreferredWidth(105);
        jtDetail.getColumnModel().getColumn(1).setCellRenderer(rightR);
        jtDetail.getColumnModel().getColumn(2).setPreferredWidth(105);
        jtDetail.getColumnModel().getColumn(2).setCellRenderer(rightR);
        jtDetail.getColumnModel().getColumn(3).setPreferredWidth(60);
        jtDetail.getColumnModel().getColumn(3).setCellRenderer(centerR);
        jtDetail.getColumnModel().getColumn(4).setPreferredWidth(120);
        jtDetail.getColumnModel().getColumn(4).setCellRenderer(rightR);

        JScrollPane tableScroll = new JScrollPane(jtDetail);
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel memoAndTotalPanel = new JPanel(new BorderLayout(0, 10));
        memoAndTotalPanel.setOpaque(false);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        totalPanel.setBackground(new Color(239, 246, 255));
        totalPanel.setBorder(BorderFactory.createLineBorder(new Color(191, 219, 254), 1));
        lblTotalPriceVal = new JLabel("총 청구 금액 : 0원");
        lblTotalPriceVal.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        lblTotalPriceVal.setForeground(new Color(29, 78, 216));
        totalPanel.add(lblTotalPriceVal);
        memoAndTotalPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel memoBox = new JPanel(new BorderLayout(0, 6));
        memoBox.setOpaque(false);
        JLabel lblMemo = new JLabel("정비사 코멘트");
        lblMemo.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblMemo.setForeground(new Color(71, 85, 105));

        jtaMemo = new JTextArea(4, 20);
        jtaMemo.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtaMemo.setLineWrap(true);
        jtaMemo.setWrapStyleWord(true);
        jtaMemo.setEditable(false);
        jtaMemo.setBackground(new Color(248, 250, 252));
        jtaMemo.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane memoScroll = new JScrollPane(jtaMemo);
        memoScroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        memoBox.add(lblMemo, BorderLayout.NORTH);
        memoBox.add(memoScroll, BorderLayout.CENTER);
        memoAndTotalPanel.add(memoBox, BorderLayout.CENTER);

        centerPanel.add(memoAndTotalPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setOpaque(false);
        JButton jbtnClose = createRoundedButton("닫기", new Color(71, 85, 105), Color.WHITE, 110, 36);
        jbtnClose.addActionListener(e -> dispose());
        bottomPanel.add(jbtnClose);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void loadDetailData() {
        MaintReportDTO report = maintDAO.selectMaintReportByNo(reportNo);
        if (report != null) {
            lblDateVal.setText(report.getMaintDate() != null ? report.getMaintDate() : "-");
            lblCenterVal.setText(report.getCenterName() != null ? report.getCenterName() : "-");
            lblCarNumVal.setText(report.getCarNum() != null ? report.getCarNum() : "-");
            lblCarModelVal.setText(report.getCarModel() != null ? report.getCarModel() : "-");
            String statusStr = (report.getStatus() >= 1) ? "정비완료" : "대기";
            lblStatusVal.setText(statusStr);
            lblTotalPriceVal.setText("총 청구 금액 : " + priceFormat.format(report.getTotalPrice()) + "원");
            jtaMemo.setText(report.getMechanicMemo() != null ? report.getMechanicMemo() : "");
        }

        dtmDetail.setRowCount(0);
        List<MaintDetailDTO> details = maintDAO.selectMaintDetailsByReportNo(reportNo);
        for (MaintDetailDTO d : details) {
            dtmDetail.addRow(new Object[]{
                d.getItemName(),
                priceFormat.format(d.getItemPrice()) + "원",
                d.getQty(),
                priceFormat.format(d.getLabCost()) + "원",
                priceFormat.format(d.getTotalPrice()) + "원"
            });
        }
    }

    private JLabel createKeyLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 116, 139));
        return lbl;
    }

    private JLabel createValueLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        lbl.setForeground(new Color(30, 41, 59));
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