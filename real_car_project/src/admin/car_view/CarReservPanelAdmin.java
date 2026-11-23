
package admin.car_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.car_evt.CarReservEvtAdmin;

public class CarReservPanelAdmin extends JPanel {

	private JTable reservationTable;

	private JTextField tfId;

	private JButton btnSearch;

	private DefaultTableModel dtm;
	private JScrollPane jsp;

	public CarReservPanelAdmin() {

		// =========================
		// 전체 패널
		// =========================

		setLayout(new BorderLayout(0, 5));
		setBackground(Color.WHITE);

		// 외곽 여백 축소
		setBorder(new EmptyBorder(10, 10, 10, 10));

		// =========================
		// 위쪽 패널 : 예약 내역
		// =========================

		JPanel topPanel = new JPanel(null);
		topPanel.setBackground(Color.WHITE);

		// jtable에 열추가하기

		String[] colName = { "예약번호", "회원아이디", "정비사유", "예약일자", "상세내용", "확정정비일자", "정비허가" };

		dtm = new DefaultTableModel(colName, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {

				return false;

			}

		};

		// 처음에 데이터를 넣어줘야한다

		reservationTable = new JTable(dtm);
		reservationTable.setRowHeight(30);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < 4; i++) {
			reservationTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		reservationTable.getColumnModel().getColumn(5).setCellRenderer(new StatusButtonRendererAdmin());
		reservationTable.getColumnModel().getColumn(6).setCellRenderer(new StatusButtonRendererAdmin());

		
		reservationTable.getColumn("상세내용")
	    .setCellRenderer(new StatusButtonRendererAdmin());
		
		
		jsp = new JScrollPane(reservationTable);
		jsp.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		topPanel.add(jsp);

		// 패널 크기에 맞춰 JTable 영역 중앙 배치
		topPanel.setPreferredSize(new Dimension(0, 310));

		// =========================
		// 아래쪽 패널 : 아이디 검색
		// =========================

		JPanel bottomPanel = new JPanel(null);
		bottomPanel.setBackground(Color.WHITE);

		// 아이디 JLabel

		JLabel lblId = new JLabel("아이디", SwingConstants.CENTER);
		lblId.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		lblId.setOpaque(true);
		lblId.setBackground(new Color(0, 140, 255));
		lblId.setForeground(Color.WHITE);
		lblId.setFont(new Font("맑은 고딕", Font.BOLD, 14));

		bottomPanel.add(lblId);

		// 아이디 JTextField

		tfId = new JTextField();

		tfId.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

		bottomPanel.add(tfId);

		// 검색 JButton

		btnSearch = new JButton("검색");
		btnSearch.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		btnSearch.setBackground(new Color(0, 140, 255));
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		btnSearch.setFocusPainted(false);

		bottomPanel.add(btnSearch);

		// =========================
		// 패널 배치
		// =========================

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.CENTER);

		// 액션리스너 설정하기
		CarReservEvtAdmin crea = new CarReservEvtAdmin(this);

		btnSearch.addActionListener(crea);
		reservationTable.addMouseListener(crea);

		// =========================
		// 컴포넌트 위치 조정
		// =========================

		topPanel.addComponentListener(new java.awt.event.ComponentAdapter() {

			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {

				int w = topPanel.getWidth();
				int h = topPanel.getHeight();

				jsp.setBounds(20, 15, w - 40, h - 30);
			}
		});

		bottomPanel.addComponentListener(new java.awt.event.ComponentAdapter() {

			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {

				int w = bottomPanel.getWidth();
				int h = bottomPanel.getHeight();

				int centerY = h / 2;

				// 검색 영역 전체를 중앙에 배치
				int startX = (w - 430) / 2;

				lblId.setBounds(startX, centerY - 20, 100, 40);

				tfId.setBounds(startX + 115, centerY - 20, 160, 40);

				btnSearch.setBounds(startX + 300, centerY - 25, 110, 50);
			}
		});

	}

	// 검색 버튼 Getter

	public JTable getReservationTable() {
		return reservationTable;
	}

	public void setReservationTable(JTable reservationTable) {
		this.reservationTable = reservationTable;
	}

	public JTextField getTfId() {
		return tfId;
	}

	public void setTfId(JTextField tfId) {
		this.tfId = tfId;
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public void setDtm(DefaultTableModel dtm) {
		this.dtm = dtm;
	}

	public JScrollPane getJsp() {
		return jsp;
	}

	public void setJsp(JScrollPane jsp) {
		this.jsp = jsp;
	}

	public void setBtnSearch(JButton btnSearch) {
		this.btnSearch = btnSearch;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	// 아이디 입력값 Getter

	public String getId() {
		return tfId.getText().trim();
	}

}
