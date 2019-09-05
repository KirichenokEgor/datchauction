package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.auction.FollowedAuction;

import java.util.List;

public interface FollowedAuctionDao {
    boolean contains(String username, int aucId);
    void addFollowedAuction(String username, int aucId);
    void deleteFollowedAuction(String username, int aucId);
    void deleteFollowedAuctionsById(int aucId);
    List<FollowedAuction> getFollowedAuctionsByUser(String username);
    void deleteFollowedAuctionByUser(String username);
}
