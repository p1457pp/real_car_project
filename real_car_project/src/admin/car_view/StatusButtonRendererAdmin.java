package admin.car_view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class StatusButtonRendererAdmin extends JButton implements TableCellRenderer {

    public StatusButtonRendererAdmin() {
        setOpaque(true);
       
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
    	
    	String cellText = (value!=null)?value.toString():"";

        setText(cellText);
        
        if(column==4) {
        	
        	setBackground(new Color(0, 140, 255));
        }
        
        if(column==5) {
        	if("미정".equals(cellText)) {
        		setBackground(new Color(255, 182, 193));
        	}else {
        		setBackground(new Color(245, 245, 245));
        	}
        	
        	
        }
        if(column==6) {
        	
        	if("예약대기".equals(cellText)) {
        		 setBackground(new Color(255, 193, 7)); // 노란색 계열 (사용자 화면과 동일)
                 setForeground(Color.BLACK);
                 setEnabled(true);
        	}  
        	if("예약확정".equals(cellText)) {
        		 setBackground(new Color(40, 167, 69)); // 초록색 계열 (사용자 화면과 동일)
                 setForeground(Color.WHITE);
                 setEnabled(true);
        	}
        	if("예약취소".equals(cellText)) {
        		setBackground(new Color(186, 156, 230)); // 연보라 (사용자 화면과 동일)
        	     setForeground(Color.WHITE);
        	     setEnabled(true);
        	}
        	
        }

        return this;
    }
}