package admin;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import admin.car_view.AdminCarListForm;
import admin.car_view.AdminLoginForm;
import admin.car_view.AdminMaintenanceForm;
import admin.car_view.AdminMaintenanceRevenueForm;
import admin.car_view.AdminMemberForm;
import admin.car_view.AdminReservationListForm;
import car_util.UiStyle;

public class AdminMainTab extends JFrame {
	
	private JTabbedPane jtpAdmin;
	private JButton jbtnLogout;
	private Timer refreshTimer;

	/** 자동 새로고침 주기 (밀리초) */
	private static final int REFRESH_INTERVAL = 10000;

	public AdminMainTab() {
		super("관리자 차량 관리");

		// 상단 : 로그아웃
		JPanel jpTop = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 2));
		jpTop.setOpaque(false);

		JLabel jlblAdmin = new JLabel("관리자");
		jlblAdmin.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		jpTop.add(jlblAdmin);

		jbtnLogout = new JButton("로그아웃");
		jbtnLogout.setPreferredSize(new java.awt.Dimension(76, 20));
		jbtnLogout.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		jbtnLogout.setMargin(new java.awt.Insets(0, 0, 0, 0));
		jpTop.add(jbtnLogout);

		jtpAdmin = new JTabbedPane();
		jtpAdmin.setFont(new Font("맑은 고딕", Font.BOLD, 14));

		jtpAdmin.addTab("회원관리", new AdminMemberForm(this));
		jtpAdmin.addTab("예약관리", new AdminReservationListForm());
		jtpAdmin.addTab("차량관리", new AdminCarListForm(this, jtpAdmin));
		jtpAdmin.addTab("정비이력", new AdminMaintenanceForm(this));
		jtpAdmin.addTab("월말정산", new AdminMaintenanceRevenueForm(this));

		// 한 줄 : 탭 오른쪽 빈 공간에 이름 + 로그아웃을 겹쳐 배치
		JPanel jpRoot = new JPanel(null) {
			@Override
			public void doLayout() {
				jtpAdmin.setBounds(0, 0, getWidth(), getHeight());
				jpTop.setBounds(getWidth() - 340, 0, 340, 26);
			}
		};
		jpRoot.add(jpTop);
		jpRoot.add(jtpAdmin);
		setContentPane(jpRoot);

		jbtnLogout.addActionListener(e -> logout());

		// 탭을 누를 때 새로고침
		jtpAdmin.addChangeListener(e -> refreshSelectedTab());

		// 1초마다 DB를 확인해서, 데이터가 바뀌었을 때만 화면을 갱신
		refreshTimer = new Timer(REFRESH_INTERVAL, e -> refreshSelectedTab());
		refreshTimer.start();

		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UiStyle.apply(this);

		setVisible(true);
	}

	/**
	 * 선택된 탭이 목록 패널이면 DB를 다시 조회해 보고, 바뀐 게 있을 때만 갱신한다.
	 * 회원관리 수정 중일 때와 월말정산 탭은 건드리지 않는다.
	 */
	public void refreshSelectedTab() {
		int idx = jtpAdmin.getSelectedIndex();
		Component c = jtpAdmin.getSelectedComponent();

		if (c instanceof AdminMemberForm) {
			if (!((AdminMemberForm) c).isEditMode()) {
				replaceIfChanged(idx, c, new AdminMemberForm(this));
			}
		} else if (c instanceof AdminReservationListForm) {
			replaceIfChanged(idx, c, new AdminReservationListForm());
		} else if (c instanceof AdminCarListForm) {
			replaceIfChanged(idx, c, new AdminCarListForm(this, jtpAdmin));
		} else if (c instanceof AdminMaintenanceForm) {
			replaceIfChanged(idx, c, new AdminMaintenanceForm(this));
		}
	}

	/**
	 * 새 패널을 만들어서 표 데이터를 비교한다.
	 * 데이터가 같으면 아무것도 하지 않고, 바뀌었을 때만 갈아끼운다.
	 * 갈아끼울 때는 스크롤 위치와 선택한 행을 그대로 유지한다.
	 */
	private void replaceIfChanged(int idx, Component oldPanel, Component newPanel) {
		JTable oldTable = findFirst(oldPanel, JTable.class);
		JTable newTable = findFirst(newPanel, JTable.class);

		// 데이터가 같으면 화면을 건드리지 않는다
		if (oldTable != null && newTable != null && sameData(oldTable, newTable)) {
			return;
		}

		// 바꾸기 전에 스크롤 위치와 선택한 행을 기억한다
		JScrollPane oldScroll = findFirst(oldPanel, JScrollPane.class);
		Point viewPos = oldScroll != null ? oldScroll.getViewport().getViewPosition() : null;
		int selectedRow = oldTable != null ? oldTable.getSelectedRow() : -1;

		jtpAdmin.setComponentAt(idx, newPanel);

		// 새 패널이 화면에 배치된 뒤에 스크롤과 선택을 복원한다
		SwingUtilities.invokeLater(() -> {
			if (newTable != null && selectedRow >= 0 && selectedRow < newTable.getRowCount()) {
				newTable.setRowSelectionInterval(selectedRow, selectedRow);
			}
			JScrollPane newScroll = findFirst(newPanel, JScrollPane.class);
			if (newScroll != null && viewPos != null) {
				newScroll.validate();
				JViewport vp = newScroll.getViewport();
				int maxY = Math.max(0, vp.getViewSize().height - vp.getExtentSize().height);
				vp.setViewPosition(new Point(viewPos.x, Math.min(viewPos.y, maxY)));
			}
		});
	}

	/** 두 표의 행 수와 모든 칸의 값이 같은지 비교한다. */
	private boolean sameData(JTable a, JTable b) {
		int rows = a.getModel().getRowCount();
		int cols = a.getModel().getColumnCount();
		if (rows != b.getModel().getRowCount() || cols != b.getModel().getColumnCount()) {
			return false;
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (!Objects.equals(a.getModel().getValueAt(r, c), b.getModel().getValueAt(r, c))) {
					return false;
				}
			}
		}
		return true;
	}

	/** 패널 안에서 원하는 종류의 컴포넌트(JTable, JScrollPane)를 처음 하나 찾는다. */
	private static <T> T findFirst(Component root, Class<T> type) {
		if (type.isInstance(root)) {
			return type.cast(root);
		}
		if (root instanceof Container) {
			for (Component child : ((Container) root).getComponents()) {
				T found = findFirst(child, type);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		if (refreshTimer != null) {
			refreshTimer.stop();
		}
		super.dispose();
	}

	/** 로그아웃 : 관리자 로그인 화면으로 돌아간다. */
	public void logout() {
		new AdminLoginForm();
		dispose();
	}

	public JTabbedPane getJtpAdmin() {
		return jtpAdmin;
	}
}