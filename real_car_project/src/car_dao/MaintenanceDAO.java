	package car_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import car_dto.MaintenanceDTO;
import car_dto.MaintenanceDetailDTO;
import car_dto.RepairItemDTO;
import car_dto.SettlementDTO;
import car_util.DBConnection;

public class MaintenanceDAO {

    private static MaintenanceDAO mDAO;

    private MaintenanceDAO() {
    }

    public static MaintenanceDAO getInstance() {
        if (mDAO == null) {
            mDAO = new MaintenanceDAO();
        }
        return mDAO;
    }

    private Connection getConnection() throws SQLException {
        DBConnection db = DBConnection.getInstance();
        return db.getConn(db.getProp(null));
    }

    private void closeAll(ResultSet rs, PreparedStatement pstmt, Connection con) {
        try {
            DBConnection.getInstance().dbClose(rs, pstmt, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. [사용자] 로그인 회원의 보유 차량 목록 조회
    public List<String> selectUserCars(String userId) {
        List<String> carList = new ArrayList<>();
        String sql = "SELECT CAR_NUM, CAR_MODEL FROM CAR_INFO WHERE USER_ID = ? AND CAR_SCRAPPED = 'N' ORDER BY VEHICLE_ID";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String carNum = rs.getString("CAR_NUM");
                String model = rs.getString("CAR_MODEL");
                carList.add(model != null ? carNum + "(" + model + ")" : carNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return carList;
    }

    // 2. [사용자] 내 차량 정비 이력 목록 조회
    public List<MaintenanceDTO> selectUserMaintList(String userId, String period, String carNum) {
        List<MaintenanceDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.REPORT_NO, TO_CHAR(m.MAINT_DATE, 'YYYY.MM.DD') AS MAINT_DATE, ");
        sql.append("       c.CAR_NUM, c.CAR_MODEL, m.TOTAL_PRICE, m.STATUS, s.CURR_PROCESS ");
        sql.append("FROM MAINT_REPORTS m ");
        sql.append("JOIN RESERV r ON m.RESERVATION_CODE = r.RESERVATION_CODE ");
        sql.append("JOIN CAR_INFO c ON r.VEHICLE_ID = c.VEHICLE_ID ");
        sql.append("LEFT JOIN REPAIR_STATE s ON m.STATUS = s.PROCESS_CODE ");
        sql.append("WHERE c.USER_ID = ? ");

        if (carNum != null && !carNum.isEmpty() && !"전체 차량".equals(carNum)) {
            sql.append("AND c.CAR_NUM = ? ");
        }
        if ("최근 1개월".equals(period)) {
			sql.append("AND m.MAINT_DATE >= ADD_MONTHS(TRUNC(SYSDATE), -1) ");
			sql.append("AND m.MAINT_DATE < TRUNC(SYSDATE) + 1 ");
		} else if ("최근 3개월".equals(period)) {
			sql.append("AND m.MAINT_DATE >= ADD_MONTHS(TRUNC(SYSDATE), -3) ");
			sql.append("AND m.MAINT_DATE < TRUNC(SYSDATE) + 1 ");
		} else if ("최근 6개월".equals(period)) {
			sql.append("AND m.MAINT_DATE >= ADD_MONTHS(TRUNC(SYSDATE), -6) ");
			sql.append("AND m.MAINT_DATE < TRUNC(SYSDATE) + 1 ");
		}
        sql.append("ORDER BY m.MAINT_DATE DESC, m.REPORT_NO DESC");

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql.toString());
            int idx = 1;
            pstmt.setString(idx++, userId);
            if (carNum != null && !carNum.isEmpty() && !"전체 차량".equals(carNum)) {
                pstmt.setString(idx++, carNum);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MaintenanceDTO dto = new MaintenanceDTO();
                int reportNo = rs.getInt("REPORT_NO");
                dto.setReportNo(reportNo);
                dto.setMaintDate(rs.getString("MAINT_DATE"));
                dto.setCarNum(rs.getString("CAR_NUM"));
                dto.setCarModel(rs.getString("CAR_MODEL"));
                dto.setTotalPrice(rs.getInt("TOTAL_PRICE"));
                dto.setStatus(rs.getInt("STATUS"));
                dto.setStatusName(rs.getString("CURR_PROCESS"));
                dto.setSummaryItem(buildSummaryItem(reportNo));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return list;
    }

    // 2-1. 특정 차량의 정비 이력 조회
    public List<MaintenanceDTO> selectByVehicleId(int vehicleId) {
        List<MaintenanceDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.REPORT_NO, TO_CHAR(m.MAINT_DATE, 'YYYY.MM.DD') AS MAINT_DATE, ");
        sql.append("       c.CAR_NUM, c.CAR_MODEL, m.TOTAL_PRICE, m.STATUS, s.CURR_PROCESS ");
        sql.append("FROM MAINT_REPORTS m ");
        sql.append("JOIN RESERV r ON m.RESERVATION_CODE = r.RESERVATION_CODE ");
        sql.append("JOIN CAR_INFO c ON r.VEHICLE_ID = c.VEHICLE_ID ");
        sql.append("LEFT JOIN REPAIR_STATE s ON m.STATUS = s.PROCESS_CODE ");
        sql.append("WHERE c.VEHICLE_ID = ? ");
        sql.append("ORDER BY m.MAINT_DATE DESC, m.REPORT_NO DESC");

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, vehicleId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MaintenanceDTO dto = new MaintenanceDTO();
                int reportNo = rs.getInt("REPORT_NO");
                dto.setReportNo(reportNo);
                dto.setMaintDate(rs.getString("MAINT_DATE"));
                dto.setCarNum(rs.getString("CAR_NUM"));
                dto.setCarModel(rs.getString("CAR_MODEL"));
                dto.setTotalPrice(rs.getInt("TOTAL_PRICE"));
                dto.setStatus(rs.getInt("STATUS"));
                dto.setStatusName(rs.getString("CURR_PROCESS"));
                dto.setSummaryItem(buildSummaryItem(reportNo));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return list;
    }

    // 3. [관리자] 전체 차량 정비 이력 목록 조회
    public List<MaintenanceDTO> selectAllMaintenance(String startDate, String endDate, String keyword) {
        List<MaintenanceDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.REPORT_NO, TO_CHAR(m.MAINT_DATE, 'YY.MM.DD') AS MAINT_DATE, ");
        sql.append("       u.USER_NAME, c.CAR_NUM, c.CAR_MODEL, m.TOTAL_PRICE, m.STATUS, ");
        sql.append("       NVL(s.CURR_PROCESS, '대기') AS CURR_PROCESS ");
        sql.append("FROM MAINT_REPORTS m ");
        sql.append("LEFT JOIN RESERV r ON m.RESERVATION_CODE = r.RESERVATION_CODE ");
        sql.append("LEFT JOIN CAR_INFO c ON r.VEHICLE_ID = c.VEHICLE_ID ");
        sql.append("LEFT JOIN USERS u ON c.USER_ID = u.USER_ID ");
        sql.append("LEFT JOIN REPAIR_STATE s ON m.STATUS = s.PROCESS_CODE ");
        sql.append("WHERE NVL(u.IS_DELETED, 1) = 1 ");

        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            sql.append("AND TRUNC(m.MAINT_DATE) BETWEEN TO_DATE(?, 'YYYY.MM.DD') AND TO_DATE(?, 'YYYY.MM.DD') ");
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append("AND (c.CAR_NUM LIKE ? OR u.USER_NAME LIKE ?) ");
        }
        sql.append("ORDER BY m.REPORT_NO DESC");

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql.toString());
            int idx = 1;
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                pstmt.setString(idx++, startDate);
                pstmt.setString(idx++, endDate);
            }
            if (keyword != null && !keyword.isEmpty()) {
                pstmt.setString(idx++, "%" + keyword + "%");
                pstmt.setString(idx++, "%" + keyword + "%");
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MaintenanceDTO dto = new MaintenanceDTO();
                int reportNo = rs.getInt("REPORT_NO");
                dto.setReportNo(reportNo);
                dto.setMaintDate(rs.getString("MAINT_DATE"));
                dto.setUserName(rs.getString("USER_NAME"));
                dto.setCarNum(rs.getString("CAR_NUM"));
                dto.setCarModel(rs.getString("CAR_MODEL"));
                dto.setTotalPrice(rs.getInt("TOTAL_PRICE"));
                dto.setStatus(rs.getInt("STATUS"));
                dto.setStatusName(rs.getString("CURR_PROCESS"));
                dto.setSummaryItem(buildSummaryItem(reportNo));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return list;
    }

    // 4. 리포트 단건 상세 정보 조회
    public MaintenanceDTO selectMaintenanceDetail(int reportNo) {
        MaintenanceDTO dto = null;
        String sql = "SELECT m.REPORT_NO, TO_CHAR(m.MAINT_DATE, 'YYYY.MM.DD') AS MAINT_DATE, "
                   + "       m.CENTER_NAME, m.TOTAL_PRICE, m.STATUS, m.MECHANIC_MEMO, m.RESERVATION_CODE, "
                   + "       c.CAR_NUM, c.CAR_MODEL, u.USER_NAME "
                   + "FROM MAINT_REPORTS m "
                   + "LEFT JOIN RESERV r ON m.RESERVATION_CODE = r.RESERVATION_CODE "
                   + "LEFT JOIN CAR_INFO c ON r.VEHICLE_ID = c.VEHICLE_ID "
                   + "LEFT JOIN USERS u ON c.USER_ID = u.USER_ID "
                   + "WHERE m.REPORT_NO = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reportNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dto = new MaintenanceDTO();
                dto.setReportNo(rs.getInt("REPORT_NO"));
                dto.setMaintDate(rs.getString("MAINT_DATE"));
                dto.setCenterName(rs.getString("CENTER_NAME"));
                dto.setTotalPrice(rs.getInt("TOTAL_PRICE"));
                dto.setStatus(rs.getInt("STATUS"));
                dto.setMechanicMemo(rs.getString("MECHANIC_MEMO"));
                dto.setReservationCode(rs.getString("RESERVATION_CODE"));
                dto.setCarNum(rs.getString("CAR_NUM"));
                dto.setCarModel(rs.getString("CAR_MODEL"));
                dto.setUserName(rs.getString("USER_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return dto;
    }

    // 5. 리포트 번호에 속한 상세 품목 리스트 조회 (RESERV_PARTS + REPAIR_ITEM JOIN)
    public List<MaintenanceDetailDTO> selectMaintDetailsByReportNo(int reportNo) {
        List<MaintenanceDetailDTO> list = new ArrayList<>();
        String sql = "SELECT d.DETAIL_NO, d.REPORT_NO, "
                   + "       COALESCE(rp1.REPAIR_ITEM, rp2.REPAIR_ITEM, "
                   + "                (SELECT MAX(r3.REPAIR_ITEM) FROM REPAIR_ITEM r3 WHERE r3.UNIT_PRICE = d.ITEM_PRICE), "
                   + "                '정비 항목 #' || d.RN_ALL) AS ITEM_NAME, "
                   + "       d.ITEM_PRICE, "
                   + "       COALESCE(rp1.LAB_COST, rp2.LAB_COST, 0) AS LAB_COST, "
                   + "       d.QTY, "
                   + "       d.TOTAL_PRICE "
                   + "FROM ( "
                   + "    SELECT DETAIL_NO, REPORT_NO, ITEM_PRICE, QTY, TOTAL_PRICE, "
                   + "           ROW_NUMBER() OVER (PARTITION BY ITEM_PRICE ORDER BY DETAIL_NO) AS RN_PRICE, "
                   + "           ROW_NUMBER() OVER (ORDER BY DETAIL_NO) AS RN_ALL "
                   + "    FROM MAINT_DETAILS "
                   + "    WHERE REPORT_NO = ? "
                   + ") d "
                   + "LEFT JOIN ( "
                   + "    SELECT r.REPAIR_ITEM, r.UNIT_PRICE, r.LAB_COST, "
                   + "           ROW_NUMBER() OVER (PARTITION BY r.UNIT_PRICE ORDER BY rp.REPAIR_CODE) AS RN_PRICE "
                   + "    FROM MAINT_REPORTS m "
                   + "    JOIN RESERV_PARTS rp ON m.RESERVATION_CODE = rp.RESERVATION_CODE "
                   + "    JOIN REPAIR_ITEM r ON rp.REPAIR_CODE = r.REPAIR_CODE "
                   + "    WHERE m.REPORT_NO = ? "
                   + ") rp1 ON d.ITEM_PRICE = rp1.UNIT_PRICE AND d.RN_PRICE = rp1.RN_PRICE "
                   + "LEFT JOIN ( "
                   + "    SELECT r.REPAIR_ITEM, r.LAB_COST, "
                   + "           ROW_NUMBER() OVER (ORDER BY rp.REPAIR_CODE) AS RN_ALL "
                   + "    FROM MAINT_REPORTS m "
                   + "    JOIN RESERV_PARTS rp ON m.RESERVATION_CODE = rp.RESERVATION_CODE "
                   + "    JOIN REPAIR_ITEM r ON rp.REPAIR_CODE = r.REPAIR_CODE "
                   + "    WHERE m.REPORT_NO = ? "
                   + ") rp2 ON d.RN_ALL = rp2.RN_ALL "
                   + "ORDER BY d.DETAIL_NO";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reportNo);
            pstmt.setInt(2, reportNo);
            pstmt.setInt(3, reportNo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MaintenanceDetailDTO d = new MaintenanceDetailDTO();
                int itemPrice = rs.getInt("ITEM_PRICE");
                int qty = rs.getInt("QTY");
                int rowTotal = rs.getInt("TOTAL_PRICE");
                int defaultLabCost = rs.getInt("LAB_COST");
                int labCost = (qty > 0 && rowTotal >= itemPrice * qty)
                            ? (rowTotal - (itemPrice * qty))
                            : defaultLabCost;

                d.setDetailNo(rs.getInt("DETAIL_NO"));
                d.setReportNo(rs.getInt("REPORT_NO"));
                d.setItemName(rs.getString("ITEM_NAME"));
                d.setItemPrice(itemPrice);
                d.setLabCost(labCost);
                d.setQty(qty);
                d.setTotalPrice(rowTotal);
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return list;
    }

    // 6. [관리자] 차량번호로 고객명/차종 조회
    public String findCarOwnerInfo(String carNum) {
        String sql = "SELECT u.USER_NAME, c.CAR_MODEL FROM CAR_INFO c JOIN USERS u ON c.USER_ID = u.USER_ID WHERE c.CAR_NUM = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, carNum);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("USER_NAME") + " 고객님 (" + rs.getString("CAR_MODEL") + ")";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return null;
    }

    // 7. 표준 정비 항목 목록 조회 (정비코드, 항목명, 부품단가, 공임)
    public List<RepairItemDTO> selectAllRepairItems() {
        List<RepairItemDTO> list = new ArrayList<>();
        String sql = "SELECT REPAIR_CODE, REPAIR_ITEM, NVL(UNIT_PRICE, 0) AS UNIT_PRICE, NVL(LAB_COST, 0) AS LAB_COST, ADDTIONAL_DATE "
                   + "FROM REPAIR_ITEM ORDER BY REPAIR_CODE";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new RepairItemDTO(rs.getString("REPAIR_CODE"), rs.getString("REPAIR_ITEM"),
                        rs.getInt("UNIT_PRICE"), rs.getInt("LAB_COST"), rs.getDate("ADDTIONAL_DATE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return list;
    }

    // 8. [관리자] 신규 정비 이력 등록 및 정비완료(1) 처리
    public boolean insertMaintenance(String carNum, String maintDate, String centerName, int totalPrice, String memo, List<MaintenanceDetailDTO> details) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            String reservCode = getOrCreateReservationCode(con, carNum, maintDate);
            if (reservCode == null) {
                con.rollback();
                return false;
            }

            int newReportNo = 1;
            pstmt = con.prepareStatement("SELECT seq_MAINT_REPORTS.NEXTVAL FROM DUAL");
            rs = pstmt.executeQuery();
            if (rs.next()) newReportNo = rs.getInt(1);
            rs.close();
            pstmt.close();

            String insertReportSql = "INSERT INTO MAINT_REPORTS (REPORT_NO, MAINT_DATE, CENTER_NAME, TOTAL_PRICE, STATUS, MECHANIC_MEMO, REGIST_DATE, RESERVATION_CODE) "
                                   + "VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, 1, ?, SYSDATE, ?)";
            pstmt = con.prepareStatement(insertReportSql);
            pstmt.setInt(1, newReportNo);
            pstmt.setString(2, maintDate);
            pstmt.setString(3, centerName);
            pstmt.setInt(4, totalPrice);
            pstmt.setString(5, memo);
            pstmt.setString(6, reservCode);
            pstmt.executeUpdate();
            pstmt.close();

            insertDetails(con, newReportNo, details);
            syncReservParts(con, reservCode, details);

            con.commit();
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            closeAll(rs, pstmt, con);
        }
    }

    // 9. [관리자] 기존 정비 이력 수정 및 진행상태 '정비완료(1)'로 변경
    public boolean updateMaintenance(int reportNo, String carNum, String maintDate, int totalPrice, String memo, List<MaintenanceDetailDTO> details) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            String reservCode = null;
            pstmt = con.prepareStatement("SELECT RESERVATION_CODE FROM MAINT_REPORTS WHERE REPORT_NO = ?");
            pstmt.setInt(1, reportNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                reservCode = rs.getString("RESERVATION_CODE");
            }
            rs.close();
            pstmt.close();

            String updateSql = "UPDATE MAINT_REPORTS "
                             + "SET MAINT_DATE = TO_DATE(?, 'YYYY-MM-DD'), TOTAL_PRICE = ?, STATUS = 1, MECHANIC_MEMO = ? "
                             + "WHERE REPORT_NO = ?";
            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, maintDate);
            pstmt.setInt(2, totalPrice);
            pstmt.setString(3, memo);
            pstmt.setInt(4, reportNo);
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = con.prepareStatement("DELETE FROM MAINT_DETAILS WHERE REPORT_NO = ?");
            pstmt.setInt(1, reportNo);
            pstmt.executeUpdate();
            pstmt.close();

            insertDetails(con, reportNo, details);
            syncReservParts(con, reservCode, details);

            con.commit();
            return true;
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            closeAll(rs, pstmt, con);
        }
    }

    private void insertDetails(Connection con, int reportNo, List<MaintenanceDetailDTO> details) throws SQLException {
        String sql = "INSERT INTO MAINT_DETAILS (DETAIL_NO, TOTAL_PRICE, QTY, ITEM_PRICE, REPORT_NO) "
                   + "VALUES (seq_MAINT_DETAILS.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (MaintenanceDetailDTO d : details) {
                if (d.getItemPrice() <= 0 && d.getLabCost() <= 0) continue;
                int qty = d.getQty() <= 0 ? 1 : d.getQty();
                int rowTotal = (d.getItemPrice() * qty) + d.getLabCost();

                pstmt.setInt(1, rowTotal);
                pstmt.setInt(2, qty);
                pstmt.setInt(3, d.getItemPrice());
                pstmt.setInt(4, reportNo);
                pstmt.executeUpdate();
            }
        }
    }

    private void syncReservParts(Connection con, String reservCode, List<MaintenanceDetailDTO> details) throws SQLException {
        if (reservCode == null || reservCode.trim().isEmpty()) return;

        Set<String> repairCodes = new LinkedHashSet<>();
        String findCodeSql = "SELECT REPAIR_CODE FROM REPAIR_ITEM WHERE REPAIR_ITEM = ?";
        try (PreparedStatement psFind = con.prepareStatement(findCodeSql)) {
            for (MaintenanceDetailDTO d : details) {
                if (d.getItemName() == null || d.getItemName().trim().isEmpty()) continue;
                psFind.setString(1, d.getItemName().trim());
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) {
                        repairCodes.add(rs.getString("REPAIR_CODE"));
                    }
                }
            }
        }

        if (repairCodes.isEmpty()) return;

        try (PreparedStatement psDel = con.prepareStatement("DELETE FROM RESERV_PARTS WHERE RESERVATION_CODE = ?")) {
            psDel.setString(1, reservCode);
            psDel.executeUpdate();
        }

        try (PreparedStatement psIns = con.prepareStatement("INSERT INTO RESERV_PARTS (RESERVATION_CODE, REPAIR_CODE) VALUES (?, ?)")) {
            for (String code : repairCodes) {
                psIns.setString(1, reservCode);
                psIns.setString(2, code);
                psIns.executeUpdate();
            }
        }
    }

    private String getOrCreateReservationCode(Connection con, String carNum, String maintDate) throws SQLException {
        int vehicleId = -1;
        String userId = null;
        try (PreparedStatement ps = con.prepareStatement("SELECT VEHICLE_ID, USER_ID FROM CAR_INFO WHERE CAR_NUM = ?")) {
            ps.setString(1, carNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vehicleId = rs.getInt("VEHICLE_ID");
                    userId = rs.getString("USER_ID");
                }
            }
        }
        if (vehicleId == -1) return null;

        try (PreparedStatement ps = con.prepareStatement("SELECT RESERVATION_CODE FROM RESERV WHERE VEHICLE_ID = ? ORDER BY RESERVATION_CODE DESC")) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RESERVATION_CODE");
                }
            }
        }

        String newCode = null;
        try (PreparedStatement ps = con.prepareStatement("SELECT 'rs' || LPAD(seq_RESERV.NEXTVAL, 3, '0') FROM DUAL");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) newCode = rs.getString(1);
        }

        String insReserv = "INSERT INTO RESERV (RESERVATION_CODE, USER_RESERV_DATE, ADDTIONAL_REQUEST, MECHAN_RESERV_DATE, RESERVATION_PERMISSION, FINAL_DATE, VEHICLE_ID, USER_ID) "
                         + "VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), '현장 방문 정비', TO_DATE(?, 'YYYY-MM-DD'), 1, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insReserv)) {
            ps.setString(1, newCode);
            ps.setString(2, maintDate);
            ps.setString(3, maintDate);
            ps.setString(4, maintDate);
            ps.setInt(5, vehicleId);
            ps.setString(6, userId);
            ps.executeUpdate();
        }
        return newCode;
    }

    private String buildSummaryItem(int reportNo) {
        List<MaintenanceDetailDTO> details = selectMaintDetailsByReportNo(reportNo);
        if (details.isEmpty()) return "정비 내역";
        String first = details.get(0).getItemName();
        if (details.size() == 1) return first;
        return first + " 외 " + (details.size() - 1) + "건";
    }

    // [관리자] 월별 정산 : 해당 월(yyyy-mm)의 정비 건수와 총 금액 (기존 SettlementDAO.selectMonthTotal)
    public SettlementDTO selectAllRevenue(String yearMonth) {
        SettlementDTO sDTO = new SettlementDTO(0, 0);

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            StringBuilder sql = new StringBuilder();
            sql
            .append(" select count(*) cnt, nvl(sum(total_price), 0) total ")
            .append(" from maint_reports ")
            .append(" where to_char(maint_date, 'yyyy-mm') = ?");

            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, yearMonth);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                sDTO = new SettlementDTO(rs.getInt("cnt"), rs.getLong("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(rs, pstmt, con);
        }
        return sDTO;
    }

}
