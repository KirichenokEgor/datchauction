package by.students.grsu.entities.auction;

import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotInfo;
import by.students.grsu.entities.lot.LotStatus;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Auction implements AuctionInfo {
    private int ID;
    private String description;
    private AuctionStatus status;
    private List<Lot> lots;
    private int currentLots;
    private int maxLots;
    private int tick;   // in seconds
    private LocalTime beginTime;
    private int maxDuration;    //in minutes
    public Auction(int id,String description,int maxLots,LocalTime beginTime, int maxDuration, String status, int currentLots){
        this.ID = id;
        this.description = description;
        switch (status){
            case "disabled":{this.status= AuctionStatus.Disabled; break;}
            case "planned":{this.status= AuctionStatus.Planned; break;}
            case "done":{this.status= AuctionStatus.Done;  break;}
            case "active":{this.status= AuctionStatus.Active; break;}
        }
        //Disabled, Planned, Active, Done
        this.maxLots = maxLots;
        this.tick = 0;
        this.beginTime = beginTime;
        this.maxDuration=maxDuration;
        this.currentLots=currentLots;
    }
    public Auction(int id,String description,int maxLots,LocalTime beginTime, int maxDuration, String status,List<Lot> lotList){
        this.ID = id;
        this.description = description;
        //Disabled, Planned, Active, Done
        this.lots = lotList;
        this.currentLots = lotList.size();
        this.maxLots = maxLots;
        this.tick = currentLots*5;
        this.beginTime = beginTime;
        this.maxDuration=maxDuration;
        switch (status){
            case "disabled":{this.status= AuctionStatus.Disabled; break;}
            case "planned":{this.status= AuctionStatus.Planned; break;}
            case "done":{this.status= AuctionStatus.Done;  break;}
            case "active":{
                this.status= AuctionStatus.Active;
                //makeActive();
                break;}
        }
    }
    public boolean hasFreeLots(){
        if(currentLots<maxLots)return true;
        else return false;
    }
    public boolean hasAnyLots(){
        if(currentLots>0)return true;
        else return false;
    }

    public int getID() {
        return ID;
    }


    public String getDescription() {
        return description;
    }


    public LocalTime getBeginTime() {
        return beginTime;
    }


    public List<LotInfo> getILots() {
        List<LotInfo> infolist = new ArrayList<LotInfo>();
        if(lots!=null)
            for(int i=0;i<lots.size();i++)
                infolist.add(lots.get(i));
        return infolist;
    }

    @Override
    public String getStringStatus() {
        return status.toString();
    }


    public List<Lot> getLots() {
        return lots;
    }


    public int getTick() {
        return tick;
    }

    public int getMaxDuration() {
        return maxDuration;
    }


    public void makeDone() {
        for(Lot lot : lots)
            if(lot.getStatus()== LotStatus.Registered)
                lot.makeEnded();
        status= AuctionStatus.Done;
    }

    public void makeActive() {
        for(Lot lot : lots)
            lot.calculatePriceStep(maxDuration*60/tick);
        status= AuctionStatus.Active;
    }

    public int getMaxLots() {
        return maxLots;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public int getCurrentLots() {
        return currentLots;
    }
}
