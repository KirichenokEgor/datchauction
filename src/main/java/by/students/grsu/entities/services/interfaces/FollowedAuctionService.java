package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.FollowedAuction;

import java.util.List;

public interface FollowedAuctionService {
    void addFollowedAuction(String username, int aucId);
    void deleteFollowedAuction(String username, int aucId);
    void deleteFollowedAuctionsById(int aucId);
    List<FollowedAuction> getFollowedAuctionsByUser(String username);
    void deleteFollowedAuctionByUser(String username);
    boolean contains(String username, int aucId);
    List<FollowedAuction> auctionsAsFollowed(List<AuctionInfo> iAuctions, String username);
    void auctionStarted(ActiveAuction auction);
}
