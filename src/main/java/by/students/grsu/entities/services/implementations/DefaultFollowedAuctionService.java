package by.students.grsu.entities.services.implementations;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.FollowedAuction;
import by.students.grsu.entities.dao.interfaces.FollowedAuctionDao;
import by.students.grsu.entities.services.interfaces.FollowedAuctionService;
import by.students.grsu.entities.services.interfaces.followersAndObservers.FollowedAuctionFollower;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultFollowedAuctionService implements FollowedAuctionFollower, FollowedAuctionService {
    private FollowedAuctionDao followedAuctionDao;

    public DefaultFollowedAuctionService(FollowedAuctionDao followedAuctionDao){
        this.followedAuctionDao = followedAuctionDao;
    }
    @Override
    public void addFollowedAuction(String username, int aucId){
        try {
            followedAuctionDao.addFollowedAuction(username, aucId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteFollowedAuction(String username, int aucId){
        try {
            followedAuctionDao.deleteFollowedAuction(username, aucId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteFollowedAuctionsById(int aucId){
        try {
            followedAuctionDao.deleteFollowedAuctionsById(aucId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<FollowedAuction> getFollowedAuctionsByUser(String username){
        try {
            return followedAuctionDao.getFollowedAuctionsByUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public Auction getFollowedAuctionById(int aucId) throws Exception {
//
//    }
    @Override
    public void deleteFollowedAuctionByUser(String username){
        try {
            followedAuctionDao.deleteFollowedAuctionByUser(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean contains(String username, int aucId){
        try {
            return followedAuctionDao.contains(username, aucId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public List<FollowedAuction> auctionsAsFollowed(List<AuctionInfo> iAuctions, String username){
        List<FollowedAuction> auctions = new ArrayList<FollowedAuction>();
        for(AuctionInfo auc : iAuctions) auctions.add(new FollowedAuction(auc, contains(username, auc.getID())));
        return  auctions;
    }
    @Override
    public void auctionStarted(ActiveAuction auction){
        auction.joinFollowedAuctionFollower(this);
    }

    @Override
    public void auctionEnded(int id) {
        deleteFollowedAuctionsById(id);
    }
}
