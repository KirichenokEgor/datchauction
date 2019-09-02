package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.auction.FollowedAuction;

import java.sql.SQLException;
import java.util.List;

public interface FollowedAuctionDao {
    boolean contains(String username, int aucId) throws SQLException;
    void addFollowedAuction(String username, int aucId) throws Exception;
    void deleteFollowedAuction(String username, int aucId) throws Exception;
    void deleteFollowedAuctionsById(int aucId) throws Exception;
    List<FollowedAuction> getFollowedAuctionsByUser(String username) throws SQLException;
    void deleteFollowedAuctionByUser(String username) throws Exception;
}
