package by.students.grsu.entities.core;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionInfo;
import by.students.grsu.entities.auction.AuctionStatus;
import by.students.grsu.entities.database.AuctionsManager;
import by.students.grsu.entities.database.DBController;
import by.students.grsu.entities.database.ItemsManager;
import by.students.grsu.entities.database.UsersManager;
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
    private List<Auction> auctions;
    private UsersManager UM;
    private ItemsManager IM;
    private AuctionsManager AM;
    private MainThread mThread;
    //TODO LOGGER
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
    public int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException {
        Auction newAuction = AM.addAuction(description, maxLots, beginTime, maxDuration);
        auctions.add(newAuction);
        return newAuction.getID();
    }

    public List<AuctionInfo> getAuctions() {
        List<AuctionInfo> auctionsinfo = new ArrayList<AuctionInfo>();
        auctionsinfo.addAll(auctions);
        return auctionsinfo;
    }

    public AuctionInfo getAuctionInfo(int ID){
        int index=0;
        for(int i=0;i<auctions.size();i++){
            if(auctions.get(i).getID()==ID)index=i;
        }
        return auctions.get(index);
    }
    /*
    public LotInfo getLotInfoById(int ID){

    }
    */
    public void makeAuctionPlanned(int ID){
        auctions.get(ID-1).makePlaned();
    }
    public void createLot(int auctionId,String name,double startPrice, double priceStep, double minPrice, List<ItemInfo> items) throws AuctionException {
        auctions.get(auctionId-1).createLot(name,startPrice, priceStep, minPrice, items);
    }
    //<ONLY FOR TESTS>
    public LotInfo createLot(int auctionId,String name, double startPrice,double minPrice) throws AuctionException {
        int index=0;
        for(int i=0;i<auctions.size();i++){
            if(auctions.get(i).getID()==auctionId)index=i;
        }
        return auctions.get(index).createLot(name,startPrice, 0.5, minPrice);
    }
    //</ONLY FOR TESTS>
    public Item addItem(String name, String description, User owner) throws SQLException, AuctionException {
        if(owner.getRole()== UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return IM.addItem(name, description, owner.getUsername());
    }
    public ItemInfo getItemById(int ID) throws SQLException, AuctionException {
        return IM.getItemById(ID);
    }
    public List<Item> getItemsByOwner(User owner) throws SQLException, AuctionException {
        if(owner.getRole()==UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return IM.getItemsByOwner(owner.getUsername());
    }
    public List<AuctionInfo> getPlannedAuctions(){
        List<AuctionInfo> list = new ArrayList<AuctionInfo>();
        for(Auction auction : auctions)
            if(auction.getStatus()==AuctionStatus.Planned)
                list.add(auction);
            return list;
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
    public void deleteAuction(int ID){
        try {
            AM.deleteAuction(ID);
            auctions.remove(ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void changeAuction(int ID,String description, int tick, int maxLots, LocalTime beginTime, int maxDuration) throws SQLException, AuctionException {
        auctions.set(ID,AM.replace(new Auction(ID,description,maxLots,beginTime,maxDuration)));
    }

    @Override
    public List<Auction> getAuctionsForThread() {
        return auctions;
    }

    /*=======   DEBUG   =======*/

    public String getAuctionsInfo(){
        String log = "";
        if(auctions.isEmpty()){
            log+="<no auctions>\n";
            return log;
        }
        for(Auction auction : auctions){
            log+=auction.getID()+": description="+ auction.getDescription() + " BTime="+auction.getBeginTime()+" Status="+auction.getStatus()+"\n";
            if(auction.getStatus() != AuctionStatus.Disabled) {
                log += auction.getLotsInfo();
            }
        }
        return log;
    }


}
