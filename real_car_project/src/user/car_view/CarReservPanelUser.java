
package user.car_view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import user.car_evt.CarReservEvt;

public class CarReservPanelUser extends JPanel {
	
	private JLabel lblUserName;

    private JTable reservationTable;

    private JTextField tfReservationDate;

    private JComboBox<String> cbRepairItem;

    private JTextArea taRequest;

    private JButton btnReservation;
    private JScrollPane jsp;

    //예약신청 및 확인 버튼
    private JToggleButton jtbApply;
    private JToggleButton jtbCheck;

    //카드레이아웃
    private CardLayout cl;
    private JPanel jpCard;
    
    //예약신청에 있는 테이블
    DefaultTableModel dtm;

    //예약확인 변수들
    private DefaultTableModel dfm_check;
    private JTable jt_check;
    private JScrollPane jsp_check;


    public CarReservPanelUser() {

        // 최상위 패널
        setLayout(new BorderLayout(10, 15));
        setBackground(Color.WHITE);


        // =========================
        // 왼쪽 패널
        // =========================

        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(Color.WHITE);

        // JTable 데이터 지정
        String[] colName = {
            "차량번호", "예약일자", "차량종류", "정비항목"
        };

        dtm = new DefaultTableModel(colName, 0) {
        	 @Override
        	    public boolean isCellEditable(int row, int column) {
        	        return false;
        	    }
        };

        // JTable 생성
        reservationTable = new JTable(dtm);

        // JScrollPane 생성
        jsp = new JScrollPane(reservationTable);
        jsp.setBounds(20, 80, 280, 350);
        jsp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));


        // 사용자 이름
        lblUserName = createLabel("사용자 이름");
        lblUserName.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );
        lblUserName.setBounds(20, 20, 130, 40);

        leftPanel.add(lblUserName);
        leftPanel.add(jsp);


        // =========================
        // 예약신청 / 예약확인 버튼
        // =========================

        jtbApply = new JToggleButton("예약신청", true);
        jtbCheck = new JToggleButton("예약확인");

        styleTabButton(jtbApply);
        styleTabButton(jtbCheck);

        jtbApply.setBackground(new Color(0, 140, 255));

        ButtonGroup bg = new ButtonGroup();
        bg.add(jtbApply);
        bg.add(jtbCheck);


        // =========================
        // 오른쪽 패널
        // =========================

        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);


        // 예약일자

        JLabel lblReservationDate = createLabel("예약일자");
        lblReservationDate.setBounds(30, 80, 120, 40);
        lblReservationDate.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );

        tfReservationDate = new JTextField();
        tfReservationDate.setBounds(170, 80, 170, 40);

        rightPanel.add(lblReservationDate);
        rightPanel.add(tfReservationDate);


        // 정비항목

        JLabel lblRepairItem = createLabel("정비항목");
        lblRepairItem.setBounds(30, 150, 120, 40);
        lblRepairItem.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );

        String[] repairItems = {
            "엔진오일",
            "미션오일",
            "배터리교환",
            "와이퍼교환"
        };

        cbRepairItem = new JComboBox<>(repairItems);
        cbRepairItem.setBounds(170, 150, 170, 40);

        rightPanel.add(lblRepairItem);
        rightPanel.add(cbRepairItem);


        // 요청사항

        JLabel lblRequest = createLabel("요청사항");
        lblRequest.setBounds(30, 220, 120, 40);
        lblRequest.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );

        taRequest = new JTextArea();
        taRequest.setLineWrap(true);
        taRequest.setWrapStyleWord(true);
        taRequest.setFont(
            new Font("맑은 고딕", Font.PLAIN, 13)
        );
        taRequest.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );
        taRequest.setBounds(170, 220, 170, 100);

        rightPanel.add(lblRequest);
        rightPanel.add(taRequest);


        // 예약하기 버튼

        btnReservation = new JButton("예약하기");
        btnReservation.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );
        btnReservation.setBounds(170, 360, 170, 45);
        btnReservation.setBackground(new Color(0, 140, 255));
        btnReservation.setForeground(Color.WHITE);
        btnReservation.setFocusPainted(false);

        rightPanel.add(btnReservation);


        // =========================
        // 좌우 패널을 담을 contentPanel
        // =========================

        //통합하기 카드 패널로
        cl = new CardLayout();
        jpCard = new JPanel(cl);

        JPanel jpApply = new JPanel(new BorderLayout(10, 0));
        jpApply.add(leftPanel, BorderLayout.CENTER);
        jpApply.add(rightPanel, BorderLayout.EAST);
        
        // =========================
        // 이벤트 리스너
        // =========================

        CarReservEvt cre = new CarReservEvt(this);


        //예약확인 패널 만들기
        JPanel jpCheck = new JPanel();

        // 테이블 컬럼명
        String[] checkColumn = {
            "예약번호",
            "차량번호",
            "예약날짜",
            "정비항목",
            "정비허가",
            "예약취소"
        };

        dfm_check = new DefaultTableModel(checkColumn, 0) {
        	@Override
            public boolean isCellEditable(int row, int column) {
                // 2번 열("추가")과 3번 열("삭제") 모두 클릭 가능하도록 허용
                return column == 5;
            }
        };
        
        
        
        jt_check = new JTable(dfm_check);
        //테이블 행 높이 지정하기
        jt_check.setRowHeight(30);
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i = 0; i < 4; i++) {
            jt_check.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }	
        
        //버튼 랜더러 밑 에디터

        
        jt_check.getColumn("예약취소").setCellRenderer(new StatusButtonRenderer());
        jt_check.getColumn("정비허가").setCellRenderer(new StatusButtonRenderer());
       

        
        
        jsp_check = new JScrollPane(jt_check);
        jsp_check.setPreferredSize(new Dimension(700, 450));
        jsp_check.getViewport().setBackground(Color.WHITE);

        jpCheck.add(jsp_check, BorderLayout.CENTER);

        //카드에 추가하기
        jpCard.add(jpApply, "apply");
        jpCard.add(jpCheck, "check");


        rightPanel.setPreferredSize(
            new Dimension(380, 500)
        );


        // =========================
        // 최상위 하단 버튼 패널
        // =========================

        JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        bottomPanel.setBackground(Color.WHITE);

        // 버튼 아래쪽 여백 15px 추가
        bottomPanel.setBorder(
            new EmptyBorder(30, 20, 15, 0)
        );

        jtbApply.setPreferredSize(
            new Dimension(100, 40)
        );

        jtbCheck.setPreferredSize(
            new Dimension(100, 40)
        );

        bottomPanel.add(jtbApply);
        bottomPanel.add(jtbCheck);


        // =========================
        // 최상위 패널에 추가
        // =========================

        add(jpCard, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


       
        jt_check.addMouseListener(cre);
        btnReservation.addActionListener(cre);
        jtbApply.addActionListener(cre);
        jtbCheck.addActionListener(cre);

    }


    // =========================
    // JLabel 생성 메서드
    // =========================

    public DefaultTableModel getDfm_check() {
        return dfm_check;
    }

    public void setDfm_check(DefaultTableModel dfm_check) {
        this.dfm_check = dfm_check;
    }

    public JTable getJt_check() {
        return jt_check;
    }

    public void setJt_check(JTable jt_check) {
        this.jt_check = jt_check;
    }

    public JScrollPane getJsp_check() {
        return jsp_check;
    }

    public void setJsp_check(JScrollPane jsp_check) {
        this.jsp_check = jsp_check;
    }


    private JLabel createLabel(String text) {

        JLabel label = new JLabel(
            text, SwingConstants.CENTER
        );

        label.setOpaque(true);
        label.setBackground(new Color(0, 140, 255));
        label.setForeground(Color.WHITE);

        label.setFont(
            new Font("맑은 고딕", Font.BOLD, 14)
        );

        return label;
    }


    // =========================
    // 토글 버튼 스타일
    // =========================

    private void styleTabButton(JToggleButton btn) {

        btn.setFont(
            new Font("맑은 고딕", Font.BOLD, 13)
        );

        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);

        btn.setBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1)
        );

        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setFocusPainted(false);

    }


    // =========================
    // Getter / Setter
    // =========================
    
    
    public JButton getBtnReservation() {
        return btnReservation;
    }

    public JLabel getLblUserName() {
		return lblUserName;
	}


	public void setLblUserName(JLabel lblUserName) {
		this.lblUserName = lblUserName;
	}


	public DefaultTableModel getDtm() {
		return dtm;
	}


	public void setDtm(DefaultTableModel dtm) {
		this.dtm = dtm;
	}


	public CardLayout getCl() {
        return cl;
    }

    public void setCl(CardLayout cl) {
        this.cl = cl;
    }

    public JPanel getJpCard() {
        return jpCard;
    }

    public void setJpCard(JPanel jpCard) {
        this.jpCard = jpCard;
    }

    public JScrollPane getJsp() {
        return jsp;
    }

    public void setJsp(JScrollPane jsp) {
        this.jsp = jsp;
    }

    public JToggleButton getJtbApply() {
        return jtbApply;
    }

    public void setJtbApply(JToggleButton jtbApply) {
        this.jtbApply = jtbApply;
    }

    public JToggleButton getJtbCheck() {
        return jtbCheck;
    }

    public void setJtbCheck(JToggleButton jtbCheck) {
        this.jtbCheck = jtbCheck;
    }

    public JTable getReservationTable() {
        return reservationTable;
    }

    public void setReservationTable(JTable reservationTable) {
        this.reservationTable = reservationTable;
    }

    public void setBtnReservation(JButton btnReservation) {
        this.btnReservation = btnReservation;
    }

    public JTextField getTfReservationDate() {
        return tfReservationDate;
    }

    public void setTfReservationDate(JTextField tfReservationDate) {
        this.tfReservationDate = tfReservationDate;
    }

    public JComboBox<String> getCbRepairItem() {
        return cbRepairItem;
    }

    public void setCbRepairItem(JComboBox<String> cbRepairItem) {
        this.cbRepairItem = cbRepairItem;
    }

    public JTextArea getTaRequest() {
        return taRequest;
    }

    public void setTaRequest(JTextArea taRequest) {
        this.taRequest = taRequest;
    }

}
