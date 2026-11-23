package car_service;

import java.sql.SQLException;

import car_dao.SettlementDAO;
import car_dto.SettlementDTO;

public class SettlementService {

    public SettlementDTO searchMonthTotal(String yearMonth) {
        SettlementDTO sDTO = new SettlementDTO(0, 0);
        try {
            sDTO = SettlementDAO.getInstance().selectMonthTotal(yearMonth);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sDTO;
    }
}