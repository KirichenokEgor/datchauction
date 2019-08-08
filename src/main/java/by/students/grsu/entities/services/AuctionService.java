package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.AuctionStatus;
import by.students.grsu.entities.dao.AuctionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionService {
    private AuctionDao AM;
    public int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException {
        Auction newAuction = AM.addAuction(description, maxLots, beginTime, maxDuration);
        //auctions.add(newAuction);
        return newAuction.getID();
    }
    public AuctionInfo getAuctionInfo(int id){
        int index=0;
        for(int i=0;i<auctions.size();i++){
            if(auctions.get(i).getID()==id)index=i;
        }
        return auctions.get(index);
    }
    public void makeAuctionPlanned(int id){
        auctions.get(id-1).makePlaned();
    }
    public List<AuctionInfo> getAuctions() {
        List<AuctionInfo> auctionsinfo = new ArrayList<AuctionInfo>();
        auctionsinfo.addAll(auctions);
        return auctionsinfo;
    }
    public List<AuctionInfo> getPlannedAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        for(Auction auction : auctions)
            if(auction.getStatus()== AuctionStatus.Planned)
                list.add(auction);
        return list;
    }
    public List<AuctionInfo> getActiveAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        for(Auction auction : auctions)
            if(auction.getStatus()==AuctionStatus.Active)
                list.add(auction);
        return list;
    }
    public List<AuctionInfo> getAllAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        for(Auction auction : auctions)
            list.add(auction);
        return list;
    }
    public List<AuctionInfo> getDoneAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        for(Auction auction : auctions)
            if(auction.getStatus()==AuctionStatus.Done)
                list.add(auction);
        return list;
    }
    public List<AuctionInfo> getDisabledAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        for(Auction auction : auctions)
            if(auction.getStatus()==AuctionStatus.Disabled)
                list.add(auction);
        return list;
    }
    public void deleteAuction(int id){
        try {
            AM.deleteAuction(id);
            auctions.remove(id);//index in list does not match with id
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void changeAuction(int id,String description, int tick, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException, AuctionException {
        auctions.set(id,AM.replace(new Auction(id,description,maxLots,beginTime,maxDuration)));
    }
}
