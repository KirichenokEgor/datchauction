package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.lot.Lot;

import java.util.List;

public interface LotDao {
    int addLot(String name,double startPrice,double minPrice,String status,int auctionId) throws Exception;
    void deleteLot(int id) throws Exception;
    void deleteLotsByAuction(int auctionId) throws Exception;
    void setStatusByLotId(int id,String status) throws Exception;
    void setEndByAuctionId(int auctionId) throws Exception;
    Lot getLotById(int id) throws Exception;
    List<Lot> getLotsByAuctionId(int auctionId) throws Exception;
    List<Lot> getAllLots() throws Exception;
    List<Lot> getLotsBySearch(String substr) throws Exception;
    List<Integer> deleteEndedLots();
    List<Lot> getRegisteredLots() throws Exception;
}
