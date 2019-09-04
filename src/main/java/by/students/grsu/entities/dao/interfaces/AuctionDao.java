package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStartTime;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Queue;

public interface AuctionDao {
    Auction getAuctionById(int ID) throws Exception;
    int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException;
    List<Auction> getAuctions() throws SQLException;
    void deleteAuction(int ID) throws SQLException;
    void addLotToAuction(int ID,boolean delete) throws Exception;
    void setStatus(int ID,String newStatus) throws Exception;
    List<Auction> getAuctionsByStatus(String status) throws Exception;
    Auction getAuctionWithLots(int id) throws Exception;
    Queue<AuctionStartTime> getAuctionsQueue() throws Exception;
    void updateDoneAuctions();
}
