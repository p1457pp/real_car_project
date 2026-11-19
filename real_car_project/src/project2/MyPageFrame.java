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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class MyPageFrame extends JFrame {
    private JTextField txtId, txtName, txtEmail, txtPhone;

    public MyPageFrame() {
        setTitle("자동차 관리 - 마이페이지");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // 메인 패널 (테두리 박스)
        JPanel panel = new JPanel();
        panel.setBounds(30, 25, 375, 510);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setLayout(null);
        add(panel);

        // 상단 타이틀
        JLabel lblTitle = new JLabel("마이페이지 (회원정보 조회)", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setBounds(30, 15, 315, 30);
        panel.add(lblTitle);

        // 1. 프로필 카드 영역 (상단 패널)
        JPanel profileCard = new JPanel();
        profileCard.setBounds(30, 55, 315, 75);
        profileCard.setBackground(new Color(230, 240, 250));
        profileCard.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), 
                "프로필 카드", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("SansSerif", Font.PLAIN, 11), Color.DARK_GRAY
        ));
        profileCard.setLayout(null);
        panel.add(profileCard);

        // 프로필 이미지 아이콘 대체 레이블 (사람 모양 또는 텍스트)
        JLabel lblImg = new JLabel("👤", SwingConstants.CENTER);
        lblImg.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblImg.setBounds(15, 20, 40, 40);
        profileCard.add(lblImg);

        JLabel lblWelcome = new JLabel("홍길동님, 환영합니다.");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblWelcome.setBounds(65, 20, 230, 20);
        profileCard.add(lblWelcome);

        JLabel lblRole = new JLabel("일반 회원");
        lblRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRole.setForeground(Color.DARK_GRAY);
        lblRole.setBounds(65, 40, 230, 20);
        profileCard.add(lblRole);

        // 2. 정보 테이블 형태 배치
        int startY = 145;
        int height = 45;

        // 아이디
        panel.add(createRowLabel("아이디", 30, startY, 100, height));
        txtId = createReadOnlyField("1234", 130, startY, 215, height);
        panel.add(txtId);

        // 이름
        panel.add(createRowLabel("이름", 30, startY + height, 100, height));
        txtName = createReadOnlyField("홍길동", 130, startY + height, 215, height);
        panel.add(txtName);

        // 이메일
        panel.add(createRowLabel("이메일", 30, startY + (height * 2), 100, height));
        txtEmail = createReadOnlyField("1234@naver.com", 130, startY + (height * 2), 215, height);
        panel.add(txtEmail);

        // 연락처
        panel.add(createRowLabel("연락처", 30, startY + (height * 3), 100, height));
        txtPhone = createReadOnlyField("010-1234-1234", 130, startY + (height * 3), 215, height);
        panel.add(txtPhone);

        // 정보 수정 버튼 (우측 하단)
        JButton btnUpdate = new JButton("정보 수정");
        btnUpdate.setBounds(230, 360, 115, 30);
        btnUpdate.setBackground(new Color(0, 122, 255));
        btnUpdate.setForeground(Color.WHITE);
        panel.add(btnUpdate);

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "정보 수정 기능은 준비 중입니다!");
            }
        });
    }

    // 표 형식의 왼쪽 항목 라벨 생성 헬퍼 메서드
    private JLabel createRowLabel(String text, int x, int y, int width, int height) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setBounds(x, y, width, height);
        lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(245, 245, 245));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        return lbl;
    }

    // 표 형식의 우측 값 텍스트필드 생성 헬퍼 메서드
    private JTextField createReadOnlyField(String text, int x, int y, int width, int height) {
        JTextField tf = new JTextField(text);
        tf.setBounds(x, y, width, height);
        tf.setEditable(false); // 조회용이므로 수정 불가 설정 (추후 수정 모드에서 풀 수 있음)
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tf.setHorizontalAlignment(JTextField.CENTER);
        return tf;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyPageFrame().setVisible(true);
        });
    }
}