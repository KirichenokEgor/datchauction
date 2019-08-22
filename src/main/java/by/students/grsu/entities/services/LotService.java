package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.dao.LotDao;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.lot.LotStatus;

import java.util.List;

public class LotService implements LotFollower,DealsFollower{
    private LotDao lotDao;
    private AuctionService auctionService;
    private ItemService itemService;
    public LotService(LotDao lotDao,AuctionService auctionService, ItemService itemService){
        this.lotDao = lotDao;
        this.auctionService = auctionService;
        this.itemService = itemService;
    }
    public LotInfo createLot(int auctionId, String name, double startPrice, double minPrice, int[] items) throws Exception {
        try {
            auctionService.addLot(auctionId);
            int newLotId = lotDao.addLot(name,startPrice,minPrice, LotStatus.Registered.toString(),auctionId);
            itemService.createLotWithItems(items,newLotId);
            return lotDao.getLotById(newLotId);
        } catch (Exception e) {
            throw new Exception(e.getMessage() + "service");
        }
    }

    public List<Lot> getLotsByAuctionId(int auctionId){
        List<Lot> lots = null;
        try {
            lots = lotDao.getLotsByAuctionId(auctionId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lots;
    }

    public Lot getLotById(int id){
        Lot lot = null;
        try {
            lot = lotDao.getLotById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lot;
    }

    public void deleteLotsByAuction(int auctionId){
        try {
            lotDao.deleteLotsByAuction(auctionId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    public void deleteLot(int id){
//        try {
//            lotDao.deleteLot(id);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public List<Lot> getAllLots(){
        try {
            return lotDao.getAllLots();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Lot> getLotsBySearch(String substr){
        try {
            return lotDao.getLotsBySearch(substr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void freeEndedLots(){
        List<Integer> lotsIndexes = lotDao.deleteEndedLots();
        for(int index : lotsIndexes)
            itemService.freeItemsByLot(index);
    }
    public void deleteLot(int lotId)/* throws Exception */{
        try {
            auctionService.deleteLot(lotDao.getLotById(lotId).getAuctionId());//decrement current lots
            lotDao.deleteLot(lotId);
            itemService.freeItemsByLot(lotId);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public List<Lot> getRegisteredLots() throws Exception {
        return lotDao.getRegisteredLots();
    }
    @Override
    public void lotSold(int lotId) {
        try {
            lotDao.setStatusByLotId(lotId,"sold");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void auctionEnded(int auctionId) {
        try {
            lotDao.setEndByAuctionId(auctionId);
            freeEndedLots();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void tickHappened() {

    }

    public void auctionStarted(ActiveAuction auction){
        auction.joinLotFollower(this);
    }

    @Override
    public void dealComplete(int lotId) {
        try {
            lotDao.deleteLot(lotId);
            itemService.deleteItemsByLotId(lotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
