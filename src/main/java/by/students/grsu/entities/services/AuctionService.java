package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.AuctionStatus;
import by.students.grsu.entities.dao.AuctionDao;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionService {
    private AuctionDao auctionDao;
    public AuctionService(AuctionDao auctionDao){
        this.auctionDao = auctionDao;
    }
    public int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException {
        Auction newAuction = auctionDao.addAuction(description, maxLots, beginTime, maxDuration);
        return newAuction.getID();
    }

    public AuctionInfo getAuctionInfo(int id){
        try {
            return  auctionDao.getAuctionById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void setAuctionPlanned(int id){
        try {
            auctionDao.setStatus(id,AuctionStatus.Planned.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setAuctionDisabled(int id){
        try {
            auctionDao.setStatus(id,AuctionStatus.Disabled.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setAuctionActive(int id){
        try {
            auctionDao.setStatus(id,AuctionStatus.Active.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setAuctionDone(int id){
        try {
            auctionDao.setStatus(id,AuctionStatus.Done.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void addLot(int auctionId) throws Exception {
        Auction auction = auctionDao.getAuctionById(auctionId);
        if(auction.hasFreeLots())auctionDao.addLotToAuction(auctionId,false);
        else throw new Exception("Max lots already reached");
    }
    public void deleteLot(int auctionId) throws Exception {
        Auction auction = auctionDao.getAuctionById(auctionId);
        if(auction.hasAnyLots())auctionDao.addLotToAuction(auctionId,true);
        else throw new Exception("Auction has no lots");
    }
    public List<AuctionInfo> getPlannedAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        try {
            for(Auction auction : auctionDao.getAuctionsByStatus("planned"))
                list.add(auction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public List<AuctionInfo> getActiveAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        try {
            for(Auction auction : auctionDao.getAuctionsByStatus("active"))
                list.add(auction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public List<AuctionInfo> getDoneAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        try {
            for(Auction auction : auctionDao.getAuctionsByStatus("done"))
                list.add(auction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public List<AuctionInfo> getDisabledAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        try {
            for(Auction auction : auctionDao.getAuctionsByStatus("disabled"))
                list.add(auction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public List<AuctionInfo> getAllAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        try {
            for(Auction auction : auctionDao.getAuctions())
                list.add(auction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    public void deleteAuction(int id){
        try {
            auctionDao.deleteAuction(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Auction getAuctionWithLots(int id){
        try {
            return auctionDao.getAuctionWithLots(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
