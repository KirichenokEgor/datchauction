package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.lot.SoldLot;

import java.sql.SQLException;
import java.util.List;

public interface SoldLotDao {
    void addSoldLot(int lotId,String buyerUsername,String sellerUsername,double price) throws Exception;
    void deleteSoldLot(int lotId) throws Exception;
    List<SoldLot> getSoldLotsByUser(String username) throws SQLException;
    SoldLot getSoldLotById(int lotId) throws Exception;
}
