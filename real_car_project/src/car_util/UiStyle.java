package car_util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * 모든 화면의 디자인(파란 둥근 버튼, 흰 배경, 표 스타일)을 한 곳에서 통일하는 도구 클래스.
 * 각 Form 의 생성자 끝에서 UiStyle.apply(this) 를 한 번 호출하면 된다. (화면 구조/위치는 변경하지 않음)
 */
public class UiStyle {

	public static final Color BLUE = new Color(88, 134, 196);
	public static final Color BLUE_DARK = new Color(68, 114, 176);
	public static final Color DISABLED = new Color(148, 163, 184);
	public static final Color LINE = new Color(226, 232, 240);
	public static final Color HEADER_BG = new Color(248, 250, 252);
	public static final Color HEADER_FG = new Color(71, 85, 105);
	public static final Color SELECT_BG = new Color(239, 246, 255);
	public static final Color TEXT = new Color(30, 41, 59);

	private static final Color DEFAULT_PANEL_BG = new JPanel().getBackground();

	private UiStyle() {
	}

	/** 창(JFrame/JDialog) 또는 패널 전체에 스타일을 적용한다. */
	public static void apply(Component root) {
		if (root instanceof RootPaneContainer) {
			Container cp = ((RootPaneContainer) root).getContentPane();
			if (cp.getBackground().equals(DEFAULT_PANEL_BG)) {
				cp.setBackground(Color.WHITE);
			}
			applyChildren(cp);
			return;
		}
		if (root instanceof JPanel && root.getBackground().equals(DEFAULT_PANEL_BG)) {
			root.setBackground(Color.WHITE);
		}
		if (root instanceof Container) {
			applyChildren((Container) root);
		}
	}

	private static void applyChildren(Container c) {
		for (Component child : c.getComponents()) {
			if (child instanceof JButton) {
				styleButton((JButton) child);
			} else if (child instanceof JTable) {
				styleTable((JTable) child);
			} else if (child instanceof JScrollPane) {
				JScrollPane sp = (JScrollPane) child;
				sp.getViewport().setBackground(Color.WHITE);
				sp.setBorder(BorderFactory.createLineBorder(LINE, 1));
				applyChildren(sp.getViewport());
			} else if (child instanceof JTabbedPane) {
				child.setBackground(Color.WHITE);
				applyChildren((Container) child);
			} else if (child instanceof JLabel) {
				// 하늘색(0,140,255) 제목 라벨 -> 공통 파랑으로 통일
				JLabel lbl = (JLabel) child;
				if (lbl.isOpaque() && new Color(0, 140, 255).equals(lbl.getBackground())) {
					lbl.setBackground(BLUE);
				}
			} else if (child instanceof JToggleButton) {
				// 라디오 버튼, 체크박스 : 회색 배경 대신 흰 배경
				child.setBackground(Color.WHITE);
				((JToggleButton) child).setOpaque(true);
			} else if (child instanceof JComboBox) {
				child.setBackground(Color.WHITE);
			} else if (child instanceof JPanel) {
				if (child.getBackground().equals(DEFAULT_PANEL_BG)) {
					child.setBackground(Color.WHITE);
				}
				applyChildren((Container) child);
			} else if (child instanceof Container && !(child instanceof AbstractButton)
					&& !(child instanceof JLabel)) {
				applyChildren((Container) child);
			}
		}
	}

	/** 파란 둥근 버튼 */
	public static void styleButton(JButton btn) {
		btn.setUI(new RoundedButtonUI());
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setOpaque(false);
		btn.setBackground(BLUE);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setHorizontalAlignment(SwingConstants.CENTER);
		btn.setVerticalAlignment(SwingConstants.CENTER);
		btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
	}

	/** 보조 버튼 : 흰 바탕 + 파란 글자/테두리 (회원가입 등) */
	public static void styleOutlineButton(JButton btn) {
		btn.setUI(new BasicButtonUI() {
			@Override
			public void paint(Graphics g, JComponent c) {
				AbstractButton b = (AbstractButton) c;
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(b.getModel().isRollover() ? SELECT_BG : Color.WHITE);
				g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 10, 10);
				g2.setColor(BLUE);
				g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 10, 10);
				g2.setFont(b.getFont());
				java.awt.FontMetrics fm = g2.getFontMetrics();
				int x = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
				int y = (c.getHeight() - (fm.getAscent() + fm.getDescent())) / 2 + fm.getAscent();
				g2.drawString(b.getText(), x, y);
				g2.dispose();
			}

			@Override
			protected void installDefaults(AbstractButton b) {
				super.installDefaults(b);
				b.setRolloverEnabled(true);
			}
		});
		btn.setForeground(BLUE);
	}

	/** 입력창 스타일 : 연한 테두리 + 안쪽 여백 (로그인/가입 화면용) */
	public static void styleField(javax.swing.text.JTextComponent tf) {
		tf.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		tf.setBackground(Color.WHITE);
		tf.setForeground(TEXT);
		tf.setCaretColor(TEXT);
		tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
				BorderFactory.createEmptyBorder(0, 10, 0, 10)));
	}

	/**
	 * 로그인 화면 공통 윗부분(파란 띠 + 제목 + 부제목). 사용자/관리자 로그인이 같은 모양이 되도록 한다.
	 * 
	 * @param width 화면 폭
	 */
	public static JPanel createLoginHeader(String title, String subTitle, int width) {
		JPanel header = new JPanel(null);
		header.setBackground(BLUE);
		header.setBounds(0, 0, width, 140);

		JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
		lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBounds(0, 40, width, 34);
		header.add(lblTitle);

		JLabel lblSub = new JLabel(subTitle, SwingConstants.CENTER);
		lblSub.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		lblSub.setForeground(new Color(224, 234, 248));
		lblSub.setBounds(0, 80, width, 22);
		header.add(lblSub);
		return header;
	}

	/** 작은 안내 라벨(입력창 위 이름표) */
	public static JLabel createFieldLabel(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl.setForeground(HEADER_FG);
		return lbl;
	}

	/** 표 스타일 : 연한 헤더, 가로선만, 선택행 연한 파랑 */
	public static void styleTable(JTable jt) {
		jt.setShowVerticalLines(false);
		jt.setGridColor(LINE);
		jt.setSelectionBackground(SELECT_BG);
		jt.setSelectionForeground(TEXT);
		jt.setBackground(Color.WHITE);

		JTableHeader th = jt.getTableHeader();
		if (th != null) {
			th.setFont(new Font("맑은 고딕", Font.BOLD, 13));
			th.setBackground(HEADER_BG);
			th.setForeground(HEADER_FG);
			th.setPreferredSize(new Dimension(0, 36));
		}
	}

	/** 지정한 열을 진행상태 배지(정비완료 초록, 정비중 파랑, 그 외 노랑)로 표시한다. */
	public static void setStatusBadge(JTable jt, int column) {
		jt.getColumnModel().getColumn(column).setCellRenderer(new StatusBadgeRenderer());
	}

	/** 둥근 파란 버튼 UI */
	private static class RoundedButtonUI extends BasicButtonUI {
		@Override
		public void paint(Graphics g, JComponent c) {
			AbstractButton b = (AbstractButton) c;
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Color bg = b.getBackground();
			if (!b.isEnabled()) {
				bg = DISABLED;
			} else if (b.getModel().isPressed()) {
				bg = BLUE_DARK;
			} else if (b.getModel().isRollover()) {
				bg = BLUE_DARK;
			}
			g2.setColor(bg);
			g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);

			if (b.getIcon() == null && b.getText() != null) {
				// 글자를 버튼 정중앙에 직접 그린다
				g2.setFont(b.getFont());
				java.awt.FontMetrics fm = g2.getFontMetrics();
				int x = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
				int y = (c.getHeight() - (fm.getAscent() + fm.getDescent())) / 2 + fm.getAscent();
				g2.setColor(b.isEnabled() ? b.getForeground() : Color.WHITE);
				g2.drawString(b.getText(), x, y);
				g2.dispose();
			} else {
				g2.dispose();
				super.paint(g, c);
			}
		}

		@Override
		protected void installDefaults(AbstractButton b) {
			super.installDefaults(b);
			b.setRolloverEnabled(true);
		}
	}

	/** 진행상태 배지 렌더러 */
	private static class StatusBadgeRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			String status = value != null ? value.toString() : "대기";
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
			panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

			JLabel badge = new JLabel(status, SwingConstants.CENTER) {
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
}
