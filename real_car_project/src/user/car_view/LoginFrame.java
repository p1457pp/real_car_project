package user.car_view;

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
import javax.swing.SwingUtilities;

import car_dao.UserDAO;

public class LoginFrame extends JFrame {
    private JTextField txtId;
    private JPasswordField txtPw;

    public LoginFrame() {
        setTitle("자동차 관리 - 로그인 페이지");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 중앙 메인 패널
        JPanel panel = new JPanel();
        panel.setBounds(60, 50, 320, 360);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(null);
        add(panel);

        // 상단 타이틀/로고 영역
        JLabel lblTitle = new JLabel("자동차 수리 접수 서비스", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setBounds(50, 20, 220, 30);
        panel.add(lblTitle);

        // 아이디 입력 필드
        txtId = new JTextField();
        txtId.setBounds(50, 80, 220, 30);
        panel.add(txtId);

        // 비밀번호 입력 필드
        txtPw = new JPasswordField();
        txtPw.setBounds(50, 125, 220, 30);
        panel.add(txtPw);

        // 로그인 버튼 (파란색 강조)
        JButton btnLogin = new JButton("로그인");
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(50, 175, 220, 40);
        panel.add(btnLogin);

        // 회원가입 버튼
        JButton btnRegister = new JButton("회원가입");
        btnRegister.setBounds(100, 235, 120, 25);
        panel.add(btnRegister);

        // [회원가입] 버튼 클릭 시 회원가입 창으로 이동
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);
                dispose(); // 로그인 창 닫기
            }
        });
        
        // [로그인] 버튼 클릭 이벤트 (DB 연동 적용)
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String pw = new String(txtPw.getPassword());
                
                if(id.isEmpty() || pw.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "아이디와 비밀번호를 입력해주세요.");
                    return;
                }

                UserDAO dao = new UserDAO();
                boolean isValid = dao.loginUser(id, pw);

                if(isValid) {
                    JOptionPane.showMessageDialog(null, "로그인 성공!");
                    
                    // 로그인 성공 시 마이페이지로 이동
                    new MyPageFrame().setVisible(true);
                    dispose(); // 로그인 창 닫기
                } else {
                    JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 일치하지 않습니다.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}