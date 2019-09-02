package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.AuctionStartTime;
import by.students.grsu.entities.services.interfaces.followersAndObservers.AuctionPlatformObserver;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Queue;

public interface AuctionService {
    int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException;
    AuctionInfo getAuctionInfo(int id);
    void setAuctionPlanned(int id);
    void addLot(int auctionId) throws Exception;
    void deleteLot(int auctionId) throws Exception;
    List<AuctionInfo> getPlannedAuctions();
    List<AuctionInfo> getActiveAuctions();
    List<AuctionInfo> getAllAuctions();
    void deleteAuction(int id);
    Auction getAuctionWithLots(int id);
    void auctionStarted(ActiveAuction activeAuction);
    void setPlatformObserver(AuctionPlatformObserver platformObserver);
    Queue<AuctionStartTime> getAuctionsQueue() throws Exception;
    void updateDoneAuctions();
}
