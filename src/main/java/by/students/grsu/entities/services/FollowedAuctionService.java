package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.FollowedAuction;
import by.students.grsu.entities.dao.FollowedAuctionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowedAuctionService {
    private FollowedAuctionDao followedAuctionDao;

    public FollowedAuctionService(FollowedAuctionDao followedAuctionDao){
        this.followedAuctionDao = followedAuctionDao;
    }

    public void addFollowedAuction(String username, int aucId){
        try {
            followedAuctionDao.addFollowedAuction(username, aucId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteFollowedAuction(String username, int aucId){
        try {
            followedAuctionDao.deleteFollowedAuction(username, aucId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteFollowedAuctionsById(int aucId){
        try {
            followedAuctionDao.deleteFollowedAuctionsById(aucId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
    public void deleteFollowedAuctionByUser(String username){
        try {
            followedAuctionDao.deleteFollowedAuctionByUser(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String username, int aucId){
        try {
            return followedAuctionDao.contains(username, aucId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FollowedAuction> auctionsAsFollowed(List<AuctionInfo> iAuctions, String username){
        List<FollowedAuction> auctions = new ArrayList<FollowedAuction>();
        for(AuctionInfo auc : iAuctions) auctions.add(new FollowedAuction(auc, contains(username, auc.getID())));
        return  auctions;
    }
}
