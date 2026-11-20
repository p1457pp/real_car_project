package car_view;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SettlementPanel extends JPanel{
	
	private JFrame owner;
	private JButton jbtnSearch;
    private JTextArea jta;
	
	public SettlementPanel(JFrame owner) {
		
		super(null);
		jta=new JTextArea();
		JScrollPane jsp=new JScrollPane();
		
		jbtnSearch=new JButton("정산");
		
		jbtnSearch.setBounds(400,400,50,50);
		
		jta.setBounds(180,0,400,300);
		add(jta);
		
		
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
	
	
}
