package admin.car_view;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import admin.car_evt.SettlementEvt;

public class SettlementPanel extends JPanel{
	
	private JFrame owner;
	private JButton jbtnSearch;
    private JTextArea jta;
    private JComboBox<String> jcbYear, jcbMonth;
	
	public SettlementPanel(JFrame owner) {
		
		super(null);
        this.owner = owner;

        jcbYear = new JComboBox<String>(new String[] {"2025", "2026", "2027"});
        jcbMonth = new JComboBox<String>(new String[] {
            "01","02","03","04","05","06","07","08","09","10","11","12"});
        jbtnSearch = new JButton("정산 조회");

        JLabel lblYear = new JLabel("년");
        JLabel lblMonth = new JLabel("월");

        jcbYear.setBounds(20, 15, 80, 30);
        lblYear.setBounds(105, 15, 20, 30);
        jcbMonth.setBounds(130, 15, 60, 30);
        lblMonth.setBounds(195, 15, 20, 30);
        jbtnSearch.setBounds(230, 15, 100, 30);

        jta = new JTextArea();
        jta.setEditable(false);
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setBounds(20, 60, 740, 250);

        add(jcbYear);  add(lblYear);
        add(jcbMonth); add(lblMonth);
        add(jbtnSearch);
        add(jsp);

        jbtnSearch.addActionListener(new SettlementEvt(this));
		
	}

	public JButton getJbtnSearch() {
		return jbtnSearch;
	}

	public void setJbtnSearch(JButton jbtnSearch) {
		this.jbtnSearch = jbtnSearch;
	}

	public JTextArea getJta() {
		return jta;
	}

	public void setJta(JTextArea jta) {
		this.jta = jta;
	}

	 public JComboBox<String> getJcbYear() { return jcbYear; }
	    public JComboBox<String> getJcbMonth() { return jcbMonth; }
	
}
