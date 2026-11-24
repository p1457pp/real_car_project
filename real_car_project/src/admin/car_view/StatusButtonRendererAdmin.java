
package admin.car_view;

import car_util.UiStyle;

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
			setBackground(UiStyle.BLUE);
			setForeground(Color.WHITE);
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
				setBackground(UiStyle.BLUE); // 노란색 계열
				setForeground(Color.WHITE);
                 setEnabled(true);
        	}  
        	if("예약확정".equals(cellText)) {
        		 setBackground(Color.LIGHT_GRAY); // 초록색 계열
                 setForeground(Color.DARK_GRAY);
                 setEnabled(true);
        	}
        	if("예약취소".equals(cellText)) {
        		setBackground(new Color(220, 53, 69)); // 빨간색 버튼
        	     setForeground(Color.WHITE);
        	     setEnabled(true);
        	}
        	
        }

        return this;
    }
}
