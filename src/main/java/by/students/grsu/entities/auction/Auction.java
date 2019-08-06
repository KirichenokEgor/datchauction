package by.students.grsu.entities.auction;

import by.students.grsu.entities.core.AuctionException;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.lot.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Auction implements ActiveAuction, AuctionInfo {
    private int ID;
    private String description;
    private AuctionStatus status;
    private List<Lot> lots;
    //private List<User> followers;
    private int maxLots;
    private int tick;   // in seconds
    private LocalTime beginTime;
    private int maxDuration;    //in minutes
    public Auction(int ID,String description,int maxLots,LocalTime beginTime, int maxDuration){
        this.ID = ID;
        this.description = description;
        status=AuctionStatus.Disabled;
        //lots = new ArrayList<Lot>();
     //   followers = new ArrayList<User>();
        this.maxLots = maxLots;
        this.tick = 0;
        this.beginTime = beginTime;
        this.maxDuration=maxDuration;
    }
    public boolean makePlaned(){
        if(status!=AuctionStatus.Planned) {
            lots = new ArrayList<Lot>();
            status = AuctionStatus.Planned;
            return true;
        }
        return false;
    }
    public void createLot(String name, double startPrice, double priceStep, double minPrice, List<ItemInfo> items) throws AuctionException {
        if(status!=AuctionStatus.Planned)throw new AuctionException("Auction is not planed yet or active",33);
        if(maxLots==lots.size())throw new AuctionException("Max lots are reached",32);
        lots.add(new Lot(name,startPrice, priceStep, minPrice, items));
        tick+=15;
    }
    //<ONLY FOR TESTS>
    public LotInfo createLot(String name,double startPrice, double priceStep, double minPrice) throws AuctionException {
        //if(status!=AuctionStatus.Planned)throw new AuctionException("Auction is not planed yet or active",33);
       // if(maxLots==lots.size())throw new AuctionException("Max lots are reached",32);
        Lot newLot = new Lot(name,startPrice, priceStep, minPrice);
        lots.add(newLot);
        tick+=15;
        return newLot;
    }
    //</ONLY FOR TESTS>
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    @Override
    public LocalTime getBeginTime() {
        return beginTime;
    }

    @Override
    public List<LotInfo> getILots() {
        List<LotInfo> infolist = new ArrayList<LotInfo>();
        if(lots!=null)
        for(int i=0;i<lots.size();i++)
            infolist.add(lots.get(i));
        return infolist;
    }

    @Override
    public List<Lot> getLots() {
        return lots;
    }

    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }

    @Override
    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    /*=======   DEBUG   =======*/

    public String getLotsInfo(){
        String log = "";
        int ID=1;
        if(lots.isEmpty()) {
            log += "\t<empty>\n";
            return log;
        }
        for(Lot lot : lots){
            log+="\t\t"+(ID++)+": curpr: "+lot.getCurrentPrice()+" stts: "+lot.getStatus()+"\n";
        }
        return log;
    }

    public int getMaxLots() {
        return maxLots;
    }
}
