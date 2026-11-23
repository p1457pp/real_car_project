 package admin.car_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import car_dao.MaintDAO;
import car_dto.MaintReportDTO;

public class AdminMaintenanceForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private MaintDAO maintDAO;

    private JTextField jtfStartDate;
    private JTextField jtfEndDate;
    private JTextField jtfKeyword;
    private JTable jtMaintenance;
    private DefaultTableModel dtmMaintenance;

    private final DecimalFormat priceFormat = new DecimalFormat("#,###");

    public AdminMaintenanceForm() {
        this("admin");
    }

    public AdminMaintenanceForm(String adminId) {
        this.maintDAO = new MaintDAO();

        setTitle("관리자 - 차량 정비 이력 관리");
        setSize(1060, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadMaintenanceData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel topPanel = new JPanel(new BorderLayout(0, 14));
        topPanel.setOpaque(false);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel lblTitle = new JLabel("차량 정비 이력 관리");
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 41, 59));
        titleRow.add(lblTitle, BorderLayout.WEST);

        JButton jbtnAdd = createRoundedButton("+ 정비 내역 추가", new Color(16, 185, 129), Color.WHITE, 145, 36);
        jbtnAdd.addActionListener(e -> new AdminMaintenanceDetailForm(this, 0).setVisible(true));
        titleRow.add(jbtnAdd, BorderLayout.EAST);
        topPanel.add(titleRow, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(4, 10, 4, 10)));

        JLabel lblDate = new JLabel("정비기간");
        lblDate.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblDate.setForeground(new Color(71, 85, 105));

        jtfStartDate = new JTextField("2026.01.01", 9);
        jtfStartDate.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtfStartDate.setHorizontalAlignment(SwingConstants.CENTER);
        jtfStartDate.setPreferredSize(new Dimension(105, 32));

        JLabel lblTilde = new JLabel("~");
        lblTilde.setFont(new Font("맑은 고딕", Font.BOLD, 13));

        jtfEndDate = new JTextField("2027.12.31", 9);
        jtfEndDate.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtfEndDate.setHorizontalAlignment(SwingConstants.CENTER);
        jtfEndDate.setPreferredSize(new Dimension(105, 32));

        JLabel lblKeyword = new JLabel("차량번호 / 고객명");
        lblKeyword.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblKeyword.setForeground(new Color(71, 85, 105));
        lblKeyword.setBorder(new EmptyBorder(0, 10, 0, 0));

        jtfKeyword = new JTextField(14);
        jtfKeyword.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtfKeyword.setPreferredSize(new Dimension(160, 32));
        jtfKeyword.addActionListener(e -> loadMaintenanceData());

        JButton jbtnSearch = createRoundedButton("검색", new Color(37, 99, 235), Color.WHITE, 80, 32);
        jbtnSearch.addActionListener(e -> loadMaintenanceData());

        filterPanel.add(lblDate);
        filterPanel.add(jtfStartDate);
        filterPanel.add(lblTilde);
        filterPanel.add(jtfEndDate);
        filterPanel.add(lblKeyword);
        filterPanel.add(jtfKeyword);
        filterPanel.add(jbtnSearch);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"번호", "정비일자", "고객명", "차량번호", "정비내용", "총청구액", "진행상태", "관리"};
        dtmMaintenance = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };	
        
        jtMaintenance = new JTable(dtmMaintenance);
        jtMaintenance.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jtMaintenance.setRowHeight(42);
        jtMaintenance.setShowVerticalLines(false);
        jtMaintenance.setGridColor(new Color(226, 232, 240));
        jtMaintenance.setSelectionBackground(new Color(239, 246, 255));
        jtMaintenance.setSelectionForeground(new Color(30, 41, 59));

        JTableHeader header = jtMaintenance.getTableHeader();
        header.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(71, 85, 105));
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        rightRenderer.setBorder(new EmptyBorder(0, 0, 0, 12));

        jtMaintenance.getColumnModel().getColumn(0).setPreferredWidth(55);
        jtMaintenance.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jtMaintenance.getColumnModel().getColumn(1).setPreferredWidth(95);
        jtMaintenance.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        jtMaintenance.getColumnModel().getColumn(2).setPreferredWidth(95);
        jtMaintenance.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        jtMaintenance.getColumnModel().getColumn(3).setPreferredWidth(110);
        jtMaintenance.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        jtMaintenance.getColumnModel().getColumn(4).setPreferredWidth(235);
        jtMaintenance.getColumnModel().getColumn(5).setPreferredWidth(115);
        jtMaintenance.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        jtMaintenance.getColumnModel().getColumn(6).setPreferredWidth(95);
        jtMaintenance.getColumnModel().getColumn(6).setCellRenderer(new StatusBadgeRenderer());
        jtMaintenance.getColumnModel().getColumn(7).setPreferredWidth(105);
        jtMaintenance.getColumnModel().getColumn(7).setCellRenderer(new DetailButtonRenderer());
        jtMaintenance.getColumnModel().getColumn(7).setCellEditor(new DetailButtonEditor());

        JScrollPane scrollPane = new JScrollPane(jtMaintenance);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    public void loadMaintenanceData() {
        dtmMaintenance.setRowCount(0);
        String startDate = jtfStartDate.getText().trim();
        String endDate = jtfEndDate.getText().trim();
        String keyword = jtfKeyword.getText().trim();

        List<MaintReportDTO> list = maintDAO.selectAdminMaintList(startDate, endDate, keyword);
        for (MaintReportDTO dto : list) {
            String statusText = dto.getStatusName();
            if (statusText == null || statusText.isEmpty()) {
                statusText = dto.getStatus() == 2 ? "정비완료" : (dto.getStatus() == 1 ? "정비중" : "대기");
            }
            Object[] row = {
                dto.getReportNo(),
                dto.getMaintDate(),
                dto.getUserName() != null ? dto.getUserName() : "-",
                dto.getCarNum() != null ? dto.getCarNum() : "-",
                dto.getSummaryItem(),
                priceFormat.format(dto.getTotalPrice()) + "원",
                statusText,
                "상세보기"
            };
            dtmMaintenance.addRow(row);
        }
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

    private class StatusBadgeRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            String status = value != null ? value.toString() : "대기";
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

            JLabel badge = new JLabel(status, SwingConstants.CENTER) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setPreferredSize(new Dimension(68, 24));
            badge.setFont(new Font("맑은 고딕", Font.BOLD, 12));

            if ("정비완료".equals(status)) {
                badge.setBackground(new Color(220, 252, 231));
                badge.setForeground(new Color(22, 101, 52));
            } else if ("정비중".equals(status)) {
                badge.setBackground(new Color(219, 234, 254));
                badge.setForeground(new Color(30, 64, 175));
            } else {
                badge.setBackground(new Color(254, 243, 199));
                badge.setForeground(new Color(146, 64, 14));
            }
            panel.add(badge);
            return panel;
        }
    }

    private class DetailButtonRenderer extends JPanel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private final JButton btn;

        public DetailButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 6));
            btn = createRoundedButton("상세보기", new Color(241, 245, 249), new Color(37, 99, 235), 84, 28);
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            add(btn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }	

    private class DetailButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        private final JPanel panel;
        private final JButton btn;
        private int currentReportNo;

        public DetailButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
            panel.setBackground(Color.WHITE);
            btn = createRoundedButton("상세보기", new Color(219, 234, 254), new Color(29, 78, 216), 84, 28);
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            btn.addActionListener(e -> {
                fireEditingStopped();
                new AdminMaintenanceDetailForm(AdminMaintenanceForm.this, currentReportNo).setVisible(true);
            });
            panel.add(btn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentReportNo = Integer.parseInt(table.getValueAt(row, 0).toString());
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "상세보기";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMaintenanceForm().setVisible(true));
    }
}