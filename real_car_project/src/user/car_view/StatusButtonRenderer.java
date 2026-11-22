package user.car_view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class StatusButtonRenderer extends JButton implements TableCellRenderer {

    public StatusButtonRenderer() {
        setOpaque(true);
    }	

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // 1. 셀에 들어있는 글자 가져오기
        String status = (value != null) ? value.toString() : "";
        setText(status);
        if(column==4) {
        	// 2. 글자 내용에 따라 버튼 색상 변경
            if ("예약대기".equals(status)) {
                setBackground(new Color(255, 193, 7)); // 노란색 계열
                setForeground(Color.BLACK);
            } else if ("예약확정".equals(status)) {
                setBackground(new Color(40, 167, 69)); // 초록색 계열
                setForeground(Color.WHITE);
            }
            
            else if ("예약취소".equals(status)) {
                setBackground(new Color(186, 156, 230)); // 초록색 계열
                setForeground(Color.WHITE);
                setEnabled(false);
            } 
            
            else {
                // 기본 색상
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
        }
        if(column==5) {
        	String reservationStatus =table.getValueAt(row, 4).toString();
        	
        	
        	if ("예약대기".equals(reservationStatus)) {
                setBackground(new Color(0, 140, 255)); // 노란색 계열
                setForeground(Color.BLACK);
                setEnabled(true);
            } else if ("예약확정".equals(reservationStatus)) {
                setBackground(Color.LIGHT_GRAY); // 초록색 계열
                setForeground(Color.DARK_GRAY);
                setEnabled(false);
            } 
            
            else if ("예약취소".equals(reservationStatus)) {
                setBackground(new Color(180, 30, 40)); // 초록색 계열
                setForeground(Color.DARK_GRAY);
                setEnabled(false);
            } else {
                // 기본 색상
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
        	
        }
       

        return this;
    }
}
