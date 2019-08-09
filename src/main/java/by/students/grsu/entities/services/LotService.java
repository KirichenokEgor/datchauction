package by.students.grsu.entities.services;

import by.students.grsu.entities.dao.LotDao;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.lot.LotStatus;

import java.util.List;

public class LotService {
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
            throw new Exception(e.getMessage());
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

    public void deleteLot(int id){
        try {
            lotDao.deleteLot(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Lot> getAllLots(){
        try {
            return lotDao.getAllLots();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
