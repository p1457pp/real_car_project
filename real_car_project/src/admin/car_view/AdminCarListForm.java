package admin.car_view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.AdminMainTap;
import admin.car_evt.AdminCarListFormEvt;


/**
 * 회원 차량 정보 리스트 (관리자)
 */
public class AdminCarListForm extends JPanel {

    private JLabel jlblTitle;
    private JButton jbtnModify, jbtnDeleteState;
    
    private JTable jtCar;
    private DefaultTableModel dtmCar;
    private JScrollPane jsp;

    public AdminCarListForm(AdminMainTap amt, JTabbedPane jtpAdmin) {
        setLayout(null);

        jlblTitle = new JLabel("회원 차량 목록");
        jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        jlblTitle.setBounds(10, 10, 200, 25);
        add(jlblTitle);

        String[] columns = {"차량ID", "차량번호", "차종", "제조사", "연식", "회원ID"};
        dtmCar = new DefaultTableModel(columns, 0);

        jtCar = new JTable(dtmCar);
        jtCar.setRowHeight(30);
        jtCar.setDefaultEditor(Object.class, null); // 셀 직접 수정 막기

        // 셀 글자 가운데 정렬
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jtCar.getColumnCount(); i++) {
            jtCar.getColumnModel().getColumn(i).setCellRenderer(dtcr);
        }

        jsp = new JScrollPane(jtCar);
        jsp.setBounds(10, 50, 740, 450);
        add(jsp);

        // 하단 버튼
        jbtnModify = new JButton("수정");
        jbtnModify.setBounds(280, 545, 100, 30);
        add(jbtnModify);

        jbtnDeleteState = new JButton("삭제");
        jbtnDeleteState.setBounds(390, 545, 100, 30);
        add(jbtnDeleteState);
        
        //액션 이벤트 추가
        AdminCarListFormEvt evt = new  AdminCarListFormEvt(this, amt);
        
        jbtnModify .addActionListener(evt);
        jbtnDeleteState.addActionListener(evt);
    }

    public JButton getJbtnModify() {
        return jbtnModify;
    }

    public JButton getJbtnDeleteState() {
        return jbtnDeleteState;
    }

    public JTable getJtCar() {
        return jtCar;
    }

    public DefaultTableModel getDtmCar() {
        return dtmCar;
    }
}
