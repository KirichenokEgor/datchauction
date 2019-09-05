package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;

import java.util.List;

public interface LotService {
    LotInfo createLot(int auctionId, String name, double startPrice, double minPrice, int[] items) throws Exception;
    List<Lot> getLotsByAuctionId(int auctionId);
    Lot getLotById(int id);
    void deleteLotsByAuction(int auctionId);
    List<Lot> getAllLots();
    List<Lot> getNotSoldLots();
    List<Lot> getLotsBySearch(String substr);
    void deleteLot(int lotId);
    void auctionStarted(ActiveAuction auction);
}
