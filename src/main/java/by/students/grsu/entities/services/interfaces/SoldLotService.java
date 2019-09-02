package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.lot.SoldLot;

import java.util.List;

public interface SoldLotService {
    void addSoldLot(int lotId,String buyerUsername, String sellerName,double price);
    void tradeComplete(int lotId);
    List<SoldLot> getSoldLotsByUser(String username);
    SoldLot getSoldLotById(int lotId) throws Exception;
    void auctionStarted(ActiveAuction auction);
}
