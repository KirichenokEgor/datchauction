package by.students.grsu.entities.services.implementations;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.dao.interfaces.SoldLotDao;
import by.students.grsu.entities.lot.SoldLot;
import by.students.grsu.entities.services.interfaces.SoldLotService;
import by.students.grsu.entities.services.interfaces.followersAndObservers.DealsFollower;
import by.students.grsu.entities.services.interfaces.followersAndObservers.SoldLotFollower;

import java.sql.SQLException;
import java.util.List;

public class DefaultSoldLotService implements SoldLotFollower, SoldLotService {
    private SoldLotDao soldLotDao;
    private DealsFollower dealsFollower;
    public DefaultSoldLotService(SoldLotDao soldLotDao, DealsFollower dealsFollower){
        this.soldLotDao=soldLotDao;
        this.dealsFollower=dealsFollower;
    }
    @Override
    public void addSoldLot(int lotId,String buyerUsername, String sellerName,double price){
        try {
            soldLotDao.addSoldLot(lotId,buyerUsername,sellerName,price);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void tradeComplete(int lotId){
        try {
            soldLotDao.deleteSoldLot(lotId);
            dealsFollower.dealComplete(lotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<SoldLot> getSoldLotsByUser(String username){
        try {
            return soldLotDao.getSoldLotsByUser(username);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    @Override
    public SoldLot getSoldLotById(int lotId) throws Exception{
        try {
            return soldLotDao.getSoldLotById(lotId);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
    @Override
    public void auctionStarted(ActiveAuction auction){
        auction.joinSoldLotFollower(this);
    }
    @Override
    public void lotSold(String buyerName,String sellerName, int lotId, double price) {
        addSoldLot(lotId,buyerName,sellerName,price);
    }
}
