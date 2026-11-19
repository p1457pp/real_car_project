package project2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegisterFrame extends JFrame {
    private JTextField txtId, txtName, txtEmail, txtPhone;
    private JPasswordField txtPw, txtPwConfirm;

    public RegisterFrame() {
        setTitle("자동차 관리 - 회원 가입");
        setSize(450, 630);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 중앙 패널
        JPanel panel = new JPanel();
        panel.setBounds(50, 25, 335, 535);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(null);
        add(panel);

        JLabel lblTitle = new JLabel("회원 가입", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setBounds(50, 15, 235, 30);
        panel.add(lblTitle);

        // 1. 아이디
        JLabel lblId = new JLabel("아이디");
        lblId.setBounds(50, 50, 235, 20);
        panel.add(lblId);
        
        txtId = new JTextField();
        txtId.setBounds(50, 70, 235, 25);
        panel.add(txtId);

        // 2. 비밀번호
        JLabel lblPw = new JLabel("비밀번호");
        lblPw.setBounds(50, 100, 235, 20);
        panel.add(lblPw);

        txtPw = new JPasswordField();
        txtPw.setBounds(50, 120, 235, 25);
        panel.add(txtPw);

        // 3. 비밀번호 확인
        JLabel lblPwConf = new JLabel("비밀번호 확인");
        lblPwConf.setBounds(50, 150, 235, 20);
        panel.add(lblPwConf);

        txtPwConfirm = new JPasswordField();
        txtPwConfirm.setBounds(50, 170, 235, 25);
        panel.add(txtPwConfirm);

        // 4. 이름
        JLabel lblName = new JLabel("이름");
        lblName.setBounds(50, 200, 235, 20);
        panel.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(50, 220, 235, 25);
        panel.add(txtName);

        // 5. 이메일
        JLabel lblEmail = new JLabel("이메일");
        lblEmail.setBounds(50, 250, 235, 20);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(50, 270, 235, 25);
        panel.add(txtEmail);

        // 6. 전화번호
        JLabel lblPhone = new JLabel("전화번호");
        lblPhone.setBounds(50, 300, 235, 20);
        panel.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(50, 320, 235, 25);
        panel.add(txtPhone);

        // 가입하기 버튼
        JButton btnSubmit = new JButton("가입하기");
        btnSubmit.setBackground(new Color(0, 122, 255));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBounds(50, 365, 235, 40);
        panel.add(btnSubmit);

        // [가입하기] 버튼 클릭 이벤트 (DB 연동 적용)
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String pw = new String(txtPw.getPassword());
                String pwConf = new String(txtPwConfirm.getPassword());
                String name = txtName.getText().trim();
                String email = txtEmail.getText().trim();
                String phone = txtPhone.getText().trim();

                if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "모든 항목을 입력해주세요.");
                    return;
                }

                if (!pw.equals(pwConf)) {
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
                    return;
                }

                UserDAO dao = new UserDAO();
                boolean success = dao.registerUser(id, pw, name, email, phone);

                if (success) {
                    JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다!");
                    new LoginFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "회원가입 실패 (아이디 중복 등을 확인해주세요).");
                }
            }
        });
    }
}