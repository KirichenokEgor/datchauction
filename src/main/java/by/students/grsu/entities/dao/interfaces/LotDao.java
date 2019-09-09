package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.lot.Lot;

import java.util.List;

public interface LotDao {
    int addLot(String name,double startPrice,double minPrice,String status,int auctionId);
    void deleteLot(int id);
    //void deleteLotsByAuction(int auctionId) throws Exception;
    void setStatusByLotId(int id,String status) throws Exception;
    void setEndByAuctionId(int auctionId);
    Lot getLotById(int id) throws Exception;
    List<Lot> getLotsByAuctionId(int auctionId);
    List<Lot> getAllLots();
    List<Lot> getNotSoldLots();
    List<Lot> getLotsBySearch(String substr);
    List<Integer> deleteEndedLots();
    List<Lot> getLotsBySeller(String username);
    //List<Lot> getRegisteredLots() throws Exception;
}
