package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.AuctionStatus;
import by.students.grsu.entities.dao.AuctionDao;
import by.students.grsu.entities.dao.DBController;
import by.students.grsu.entities.dao.ItemDao;
import by.students.grsu.entities.dao.UserDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.users.User;
import by.students.grsu.entities.users.UserRole;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class Core implements AuctionsCore {

    private Core() {
        try {
            DBController DBC = new DBController("localhost","datchDBManager","ineedyourbase");
            IM = DBC.getItemsManager();
            UM = DBC.getUsersManager();
            AM = DBC.getAuctionManager();
            auctions = AM.getAuctions();
            mThread = new MainThread(this);
            mThread.start();
        //} catch (SQLException e) {

         //   e.printStackTrace();
        } catch (AuctionException | SQLException e){
            System.out.println(e.getMessage());
            System.out.println("Initializing failed.");
        }
    }
    public static Core Initialize(){
        //TODO firstrun(???)
        return new Core();
    }

  //  public void createLot(int auctionId,String name,double startPrice, double minPrice, List<Item> items) throws AuctionException {
   //     auctions.get(auctionId-1).createLot(name,startPrice, minPrice, items);
  //  }

    //<LotService>
    public LotInfo createLot(int auctionId,String name, double startPrice,double minPrice) throws AuctionException {
      int index=0;
      for(int i=0;i<auctions.size();i++){
          if(auctions.get(i).getID()==auctionId)index=i;
      }
      return auctions.get(index).createLot(name,startPrice, 0.5, minPrice);
  }
    //</LotService>

   //<ItemService>
    private ItemDao IM;
    public Item addItem(String name, String description, User owner) throws SQLException, AuctionException {
        if(owner.getRole()== UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return IM.addItem(name, description,  owner.getUsername());
    }
    public ItemInfo getItemById(int id) throws SQLException, AuctionException {
        return IM.getItemById(id);
    }
    public List<Item> getItemsByOwner(User owner) throws SQLException, AuctionException {
        if(owner.getRole()==UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return IM.getItemsByOwner(owner.getUsername());
    }
    public void deleteItemById(int id) throws SQLException, AuctionException {
        IM.deleteItemById(id);
    }
    //</ItemService>

    // <AuctionService>
    private AuctionDao AM;
    public int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException {
        Auction newAuction = AM.addAuction(description, maxLots, beginTime, maxDuration);
        auctions.add(newAuction);
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
            if(auction.getStatus()==AuctionStatus.Planned)
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
    // </AuctionService>

    //<UserService>
    private UserDao UM;
    public User login(String email, String password) throws AuctionException{
        try {
            return UM.login(email,password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuctionException("Internal error",0);
        }
    }
    public User registration(String email, String password, String username) throws AuctionException{
        try {
            return UM.registerNew(email, password, username);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuctionException("Internal error",0);
        }
    }
    public void setRole(User user, String role){
        switch (role){
            case "admin":{
                user.setRole(UserRole.Admin);
                break;
            }
            case "buyer":{
                user.setRole(UserRole.Buyer);
                break;
            }
            case "seller":{
                user.setRole(UserRole.Seller);
                break;
            }
        }
    }
    //</UserService>

    //<AuctionPlatform>
    private MainThread mThread;
    private List<Auction> auctions;
    public List<Auction> getAuctionsForThread() {
        return auctions;
    }
    //</AuctionPlatform>
}
