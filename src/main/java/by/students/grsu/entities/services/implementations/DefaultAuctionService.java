package by.students.grsu.entities.services.implementations;

import by.students.grsu.entities.auction.*;
import by.students.grsu.entities.dao.interfaces.AuctionDao;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.services.interfaces.AuctionService;
import by.students.grsu.entities.services.interfaces.ItemService;
import by.students.grsu.entities.services.interfaces.LotService;
import by.students.grsu.entities.services.interfaces.followersAndObservers.AuctionFollower;
import by.students.grsu.entities.services.interfaces.followersAndObservers.AuctionPlatformObserver;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DefaultAuctionService implements AuctionFollower, AuctionService {
    private AuctionDao auctionDao;
    private ItemService itemService;
    private LotService lotService;
    private AuctionPlatformObserver auctionPlatformObserver;
    public DefaultAuctionService(AuctionDao auctionDao, ItemService itemService/*, LotService lotService*/){
        this.auctionDao = auctionDao;
        this.itemService = itemService;
        //this.itemService = itemService;
        //this.lotService = lotService;
    }

    @Override
    public void setLotService(LotService lotService){
        this.lotService = lotService;
    }

    @Override
    public int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration){
        int id = auctionDao.addAuction(description, maxLots, beginTime, maxDuration);
        auctionPlatformObserver.auctionsChanged();
        return id;
    }

    @Override
    public AuctionInfo getAuctionInfo(int id){
        try {
            return  auctionDao.getAuctionById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    @Override
    public void setAuctionPlanned(int id){
        try {
            auctionDao.setStatus(id, AuctionStatus.Planned.toString());
            auctionPlatformObserver.auctionsChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
//    public void setAuctionDisabled(int id){
//        try {
//
//            auctionDao.setStatus(id, AuctionStatus.Disabled.toString());
//            auctionPlatformObserver.auctionsChanged();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    public void setAuctionActive(int id){
//        try {
//            auctionDao.setStatus(id, AuctionStatus.Active.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    public void setAuctionDone(int id){
//        try {
//            auctionDao.setStatus(id, AuctionStatus.Done.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
    @Override
    public void addLot(int auctionId) throws Exception {
        Auction auction = auctionDao.getAuctionById(auctionId);
        if(auction.hasFreeLots())auctionDao.addLotToAuction(auctionId,false);
        else throw new Exception("Max lots already reached");
    }
    @Override
    public void deleteLot(int auctionId) throws Exception {
        Auction auction = auctionDao.getAuctionById(auctionId);
        if(auction.hasAnyLots())auctionDao.addLotToAuction(auctionId,true);
        else throw new Exception("Auction has no lots");
    }
    @Override
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
    @Override
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
//    public List<AuctionInfo> getDoneAuctions(){
//        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
//        try {
//            for(Auction auction : auctionDao.getAuctionsByStatus("done"))
//                list.add(auction);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return list;
//    }
//    public List<AuctionInfo> getDisabledAuctions(){
//        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
//        try {
//            for(Auction auction : auctionDao.getAuctionsByStatus("disabled"))
//                list.add(auction);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return list;
//    }
    @Override
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
    @Override
    public void deleteAuction(int id){
        try {
            Auction auction = auctionDao.getAuctionWithLots(id);
            for(Lot lot : auction.getLots())
                itemService.freeItemsByLot(lot.getID());
            auctionDao.deleteAuction(id);
            lotService.deleteLotsByAuction(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void auctionEnded(int id) {
        try {
            auctionDao.setStatus(id, AuctionStatus.Done.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void auctionStarted(ActiveAuction activeAuction){
        try {
            auctionDao.setStatus(activeAuction.getAuctionId(), AuctionStatus.Active.toString());
            activeAuction.joinAuctionFollower(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Auction getAuctionWithLots(int id){
        try {
            return auctionDao.getAuctionWithLots(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void setPlatformObserver(AuctionPlatformObserver platformObserver){
        auctionPlatformObserver = platformObserver;
    }
    @Override
    public Queue<AuctionStartTime> getAuctionsQueue(){
        return auctionDao.getAuctionsQueue();
    }
    @Override
    public void updateDoneAuctions(){
        auctionDao.updateDoneAuctions();
    }
}