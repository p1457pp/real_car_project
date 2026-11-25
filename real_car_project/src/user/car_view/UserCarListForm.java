package user.car_view;

import car_util.UiStyle;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import user.UserMainTab;
import user.car_evt.UserCarListFormEvt;



public class UserCarListForm extends JPanel {

	private JLabel jlblTitle;
	private JButton jbtnAddCar, jbtnCarInfoEdit, jbtShowCarMaintenance, jbtnCarDelete, jbtnReservation;

	private JTable jtUserCarList;
	private DefaultTableModel dtmCarList;
	private JScrollPane jsp;
	public UserCarListForm(UserMainTab umt, JTabbedPane jtpUser, String userId) {
		setLayout(null);

		jlblTitle = new JLabel("내 차량");
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		jlblTitle.setBounds(10, 10, 100, 25);
		add(jlblTitle);

		jbtnAddCar = new JButton("차량추가");
		jbtnAddCar.setBounds(660, 10, 90, 25);
		add(jbtnAddCar);

		String[] columns = { "차량ID", "사진", "차량번호", "차종", "연식", "폐차" };
		dtmCarList = new DefaultTableModel(columns, 0);

		jtUserCarList = new JTable(dtmCarList);
		jtUserCarList.setRowHeight(40);
		
		// 표 안의 글자를 가운데 정렬
		DefaultTableCellRenderer center = (DefaultTableCellRenderer) jtUserCarList.getDefaultRenderer(Object.class);
		center.setHorizontalAlignment(JLabel.CENTER);
		
		jtUserCarList.setDefaultEditor(Object.class, null); // 셀 직접 수정 막기
		jtUserCarList.removeColumn(jtUserCarList.getColumnModel().getColumn(0)); // 컬럼의 0번쨰 Vehicle Id셀 숨기기

		jsp = new JScrollPane(jtUserCarList);
		jsp.setBounds(10, 50, 740, 450);
		add(jsp);

		// 하단 버튼
		jbtnReservation = new JButton("예약하기");
		jbtnReservation.setBounds(165, 545, 100, 30);
		add(jbtnReservation);

		jbtShowCarMaintenance = new JButton("정비이력");
		jbtShowCarMaintenance.setBounds(275, 545, 100, 30);
		add(jbtShowCarMaintenance);

		jbtnCarInfoEdit = new JButton("수정");
		jbtnCarInfoEdit.setBounds(385, 545, 100, 30);
		add(jbtnCarInfoEdit);

		jbtnCarDelete = new JButton("삭제");
		jbtnCarDelete.setBounds(495, 545, 100, 30);
		add(jbtnCarDelete);

		// 액션 이벤트 추가
		UserCarListFormEvt evt = new UserCarListFormEvt(this, umt, jtpUser, userId);

		jbtnAddCar.addActionListener(evt);
		jbtnCarInfoEdit.addActionListener(evt);
		jbtShowCarMaintenance.addActionListener(evt);
		jbtnCarDelete.addActionListener(evt);
		jbtnReservation.addActionListener(evt); // 예약하기 이벤트 연결

		UiStyle.apply(this);

	}

	public JButton getJbtnAddCar() {
		return jbtnAddCar;
	}

	public JButton getJbtnCarInfoEdit() {
		return jbtnCarInfoEdit;
	}

	public JButton getJbtShowCarMaintenance() {
		return jbtShowCarMaintenance;
	}

	public JButton getJbtnCarDelete() {
		return jbtnCarDelete;
	}

	public JTable getJtUserCarList() {
		return jtUserCarList;
	}

	public DefaultTableModel getDtmCarList() {
		return dtmCarList;
	}

	public JButton getJbtnReservation() {
		return jbtnReservation;
	}

}