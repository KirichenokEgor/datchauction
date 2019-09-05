package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStartTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Queue;

public interface AuctionDao {
    Auction getAuctionById(int ID) throws Exception;
    int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration);
    List<Auction> getAuctions();
    void deleteAuction(int ID);
    void addLotToAuction(int ID,boolean delete);
    void setStatus(int ID,String newStatus) throws Exception;
    List<Auction> getAuctionsByStatus(String status) throws Exception;
    Auction getAuctionWithLots(int id) throws Exception;
    Queue<AuctionStartTime> getAuctionsQueue();
    void updateDoneAuctions();
}
