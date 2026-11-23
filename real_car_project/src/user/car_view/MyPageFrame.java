package user.car_view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import car_dao.UserDAO;

public class MyPageFrame extends JFrame {
    private String loginUserId;
    private String currentPassword = "";
    private String currentName = "";
    private String currentEmail = "";
    private String currentPhone = "";

    private JLabel lblWelcome;
    private JLabel lblRole;
    private JTextField txtId, txtName, txtEmail, txtPhone;
    private JButton btnUpdate;

    // 단독 실행이나 기본 호출용 생성자
    public MyPageFrame() {
        this("ydh");
    }

    // 로그인한 아이디를 받아 내 정보를 조회·출력하는 생성자
    public MyPageFrame(String userId) {
        this.loginUserId = userId;

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

        // 프로필 이미지 아이콘 대체 레이블
        JLabel lblImg = new JLabel("👤", SwingConstants.CENTER);
        lblImg.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblImg.setBounds(15, 20, 40, 40);
        profileCard.add(lblImg);

        lblWelcome = new JLabel("회원님, 환영합니다.");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblWelcome.setBounds(65, 20, 230, 20);
        profileCard.add(lblWelcome);

        lblRole = new JLabel("일반 회원");
        lblRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRole.setForeground(Color.DARK_GRAY);
        lblRole.setBounds(65, 40, 230, 20);
        profileCard.add(lblRole);

        // 2. 정보 테이블 형태 배치
        int startY = 145;
        int height = 45;

        // 아이디
        panel.add(createRowLabel("아이디", 30, startY, 100, height));
        txtId = createReadOnlyField("", 130, startY, 215, height);
        panel.add(txtId);

        // 이름
        panel.add(createRowLabel("이름", 30, startY + height, 100, height));
        txtName = createReadOnlyField("", 130, startY + height, 215, height);
        panel.add(txtName);

        // 이메일
        panel.add(createRowLabel("이메일", 30, startY + (height * 2), 100, height));
        txtEmail = createReadOnlyField("", 130, startY + (height * 2), 215, height);
        panel.add(txtEmail);

        // 연락처
        panel.add(createRowLabel("연락처", 30, startY + (height * 3), 100, height));
        txtPhone = createReadOnlyField("", 130, startY + (height * 3), 215, height);
        panel.add(txtPhone);

        // 정보 수정 버튼 (우측 하단) -> 별도 수정 창 열기
        btnUpdate = new JButton("정보 수정");
        btnUpdate.setBounds(230, 360, 115, 30);
        btnUpdate.setBackground(new Color(0, 122, 255));
        btnUpdate.setForeground(Color.WHITE);
        panel.add(btnUpdate);

        // DB에서 현재 로그인한 계정 정보 불러와 화면에 출력
        loadUserInfo();

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditDialog();
            }
        });
    }

    // DB에서 로그인한 회원 정보 조회 및 화면 반영
    private void loadUserInfo() {
        UserDAO dao = new UserDAO();
        // 반환 순서: [0]USER_ID, [1]PASSWORD, [2]USER_NAME, [3]EMAIL, [4]PHONE, [5]USER_GRADE
        String[] info = dao.getUserInfo(loginUserId);

        if (info != null) {
            String id = info[0] != null ? info[0] : "";
            currentPassword = info[1] != null ? info[1] : "";
            currentName = info[2] != null ? info[2] : "";
            currentEmail = info[3] != null ? info[3] : "";
            currentPhone = info[4] != null ? info[4] : "";
            String grade = (info.length > 5 && info[5] != null && !info[5].isEmpty())
                    ? info[5] : "일반 회원";

            txtId.setText(id);
            txtName.setText(currentName);
            txtEmail.setText(currentEmail);
            txtPhone.setText(currentPhone);

            lblWelcome.setText(currentName + "님, 환영합니다.");
            lblRole.setText(grade);
        } else {
            JOptionPane.showMessageDialog(this, "회원 정보를 불러올 수 없습니다.");
        }
    }

    // [정보 수정] 클릭 시 열리는 별도 수정 다이얼로그
    private void openEditDialog() {
        JDialog editDialog = new JDialog(this, "회원 정보 수정", true);
        editDialog.setSize(400, 400);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(null);

        int labelX = 35;
        int fieldX = 125;
        int fieldW = 220;
        int fieldH = 30;
        int startY = 30;
        int gapY = 48;

        // 아이디 (수정 불가)
        JLabel lblDlgId = new JLabel("아이디");
        lblDlgId.setBounds(labelX, startY, 80, fieldH);
        editDialog.add(lblDlgId);

        JTextField tfDlgId = new JTextField(loginUserId);
        tfDlgId.setBounds(fieldX, startY, fieldW, fieldH);
        tfDlgId.setEditable(false);
        tfDlgId.setBackground(new Color(235, 235, 235));
        editDialog.add(tfDlgId);

        // 비밀번호
        JLabel lblDlgPw = new JLabel("비밀번호");
        lblDlgPw.setBounds(labelX, startY + gapY, 80, fieldH);
        editDialog.add(lblDlgPw);

        JPasswordField pfDlgPw = new JPasswordField(currentPassword);
        pfDlgPw.setBounds(fieldX, startY + gapY, fieldW, fieldH);
        editDialog.add(pfDlgPw);

        // 이름
        JLabel lblDlgName = new JLabel("이름");
        lblDlgName.setBounds(labelX, startY + gapY * 2, 80, fieldH);
        editDialog.add(lblDlgName);

        JTextField tfDlgName = new JTextField(currentName);
        tfDlgName.setBounds(fieldX, startY + gapY * 2, fieldW, fieldH);
        editDialog.add(tfDlgName);

        // 이메일
        JLabel lblDlgEmail = new JLabel("이메일");
        lblDlgEmail.setBounds(labelX, startY + gapY * 3, 80, fieldH);
        editDialog.add(lblDlgEmail);

        JTextField tfDlgEmail = new JTextField(currentEmail);
        tfDlgEmail.setBounds(fieldX, startY + gapY * 3, fieldW, fieldH);
        editDialog.add(tfDlgEmail);

        // 연락처
        JLabel lblDlgPhone = new JLabel("연락처");
        lblDlgPhone.setBounds(labelX, startY + gapY * 4, 80, fieldH);
        editDialog.add(lblDlgPhone);

        JTextField tfDlgPhone = new JTextField(currentPhone);
        tfDlgPhone.setBounds(fieldX, startY + gapY * 4, fieldW, fieldH);
        editDialog.add(tfDlgPhone);

        // 저장 버튼
        JButton btnSave = new JButton("저장");
        btnSave.setBounds(115, 290, 80, 34);
        btnSave.setBackground(new Color(0, 122, 255));
        btnSave.setForeground(Color.WHITE);
        editDialog.add(btnSave);

        // 취소 버튼
        JButton btnCancel = new JButton("취소");
        btnCancel.setBounds(210, 290, 80, 34);
        editDialog.add(btnCancel);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPw = new String(pfDlgPw.getPassword()).trim();
                String newName = tfDlgName.getText().trim();
                String newEmail = tfDlgEmail.getText().trim();
                String newPhone = tfDlgPhone.getText().trim();

                if (newPw.isEmpty() || newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, "비밀번호, 이름, 이메일, 연락처를 모두 입력해주세요.");
                    return;
                }

                UserDAO dao = new UserDAO();
                boolean updated = dao.updateUser(loginUserId, newPw, newName, newEmail, newPhone);

                if (updated) {
                    JOptionPane.showMessageDialog(editDialog, "회원 정보가 수정되었습니다.");
                    editDialog.dispose();
                    loadUserInfo(); // 마이페이지 화면도 최신 DB 값으로 갱신
                } else {
                    JOptionPane.showMessageDialog(editDialog, "회원 정보 수정에 실패했습니다.");
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });

        editDialog.setVisible(true);
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
        tf.setEditable(false);
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