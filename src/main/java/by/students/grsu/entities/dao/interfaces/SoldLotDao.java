package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.lot.SoldLot;

import java.util.List;

public interface SoldLotDao {
    void addSoldLot(int lotId,String buyerUsername,String sellerUsername,double price);
    void deleteSoldLot(int lotId);
    List<SoldLot> getSoldLotsByUser(String username);
    SoldLot getSoldLotById(int lotId) throws Exception;
}
