package admin.car_view;

import car_util.UiStyle;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.car_evt.AdminReservationListFormEvt;

public class AdminReservationListForm extends JPanel {

    private JLabel jlblName;
    private JTextField jtfId;
    private JButton jbtnSearch;

    private JTable jtReservation;
    private DefaultTableModel dtmReservation;
    private JScrollPane jsp;

    public AdminReservationListForm() {
        setLayout(null);

        jlblName = new JLabel("예약관리");
        jlblName.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        jlblName.setBounds(10, 10, 100, 25);
        add(jlblName);

        JLabel jlblSearch = new JLabel("회원아이디", JLabel.RIGHT);
        jlblSearch.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        jlblSearch.setBounds(410, 10, 85, 25);
        add(jlblSearch);

        jtfId = new JTextField();
        jtfId.setBounds(500, 10, 150, 25);
        add(jtfId);

        jbtnSearch = new JButton("검색");
        jbtnSearch.setBounds(660, 10, 90, 25);
        add(jbtnSearch);

        String[] colName = {
            "예약번호", "회원아이디", "정비사유", "예약일자",
            "상세내용", "확정정비일자", "정비허가"
        };

        dtmReservation = new DefaultTableModel(colName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jtReservation = new JTable(dtmReservation);
        jtReservation.setRowHeight(40);
        jtReservation.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < 4; i++) {
            jtReservation.getColumnModel()
                    .getColumn(i).setCellRenderer(centerRenderer);
        }

        jtReservation.getColumnModel().getColumn(4)
                .setCellRenderer(new StatusButtonRendererAdmin());

        jtReservation.getColumnModel().getColumn(5)
                .setCellRenderer(new StatusButtonRendererAdmin());

        jtReservation.getColumnModel().getColumn(6)
                .setCellRenderer(new StatusButtonRendererAdmin());

        jsp = new JScrollPane(jtReservation);
        jsp.setBounds(10, 50, 740, 450);
        add(jsp);

        AdminReservationListFormEvt evt =
                new AdminReservationListFormEvt(this);

        jbtnSearch.addActionListener(evt);
        jtfId.addActionListener(evt);
        jtReservation.addMouseListener(evt);

        evt.searchAllReservations();

        UiStyle.apply(this);
    }

    public JTextField getJtfId() {
        return jtfId;
    }

    public JButton getJbtnSearch() {
        return jbtnSearch;
    }

    public JTable getJtReservation() {
        return jtReservation;
    }

    public DefaultTableModel getDtmReservation() {
        return dtmReservation;
    }
}