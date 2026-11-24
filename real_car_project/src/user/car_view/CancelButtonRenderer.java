package user.car_view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

//1) 버튼 모양을 그려주는 렌더러
public class CancelButtonRenderer extends JButton implements TableCellRenderer {
 public CancelButtonRenderer() {
     setOpaque(true);
     setBackground(new Color(220, 53, 69)); // 빨간색 버튼
     setForeground(Color.WHITE);
 }

 @Override
 public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {
     setText((value == null) ? "예약취소" : value.toString());
     return this;
 }
}	
