package by.students.grsu.entities.auction;

import by.students.grsu.entities.lot.ActiveLot;
import by.students.grsu.entities.lot.LotStatus;

import java.time.LocalTime;

public class AuctionThread extends Thread{
    private int tick; //in milliseconds;
    private ActiveAuction auction;
    private LocalTime endTime;
    public AuctionThread(ActiveAuction auction){
        this.auction = auction;
        tick = auction.getTick()*1000;
        endTime = auction.getBeginTime().plusMinutes(auction.getMaxDuration());
    }

    @Override
    public void run() {
        //System.out.println("\nNew auction started");
        auction.setStatus(AuctionStatus.Active);
        try {
        while(true){
            if(LocalTime.now().isAfter(endTime))break;
            sleep(tick);
            boolean allSold = true;
            for(ActiveLot lot : auction.getLots()) {
                if (lot.getStatus() == LotStatus.Registered) {
                    allSold = false;
                    if(lot.getCurrentPrice()<=lot.getMinPrice()){
                        lot.setStatus(LotStatus.End);
                        continue;
                    }
                    lot.setCurrentPrice(lot.getCurrentPrice()-lot.getPriceStep());
                }
            }
            if(allSold)break;
        }
        auction.setStatus(AuctionStatus.Done);
        //System.out.println("\nAuction ended");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
