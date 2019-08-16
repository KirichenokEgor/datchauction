package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.ActiveAuction;
import by.students.grsu.entities.auction.ActiveAuctionInterface;
import by.students.grsu.entities.auction.AuctionStartTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AuctionPlatform extends Thread implements AuctionPlatformObserver{

    private Queue<AuctionStartTime> auctionsQueue;
    private AuctionService auctionService;
    private LotService lotService;
    private SoldLotService soldLotService;

    private List<ActiveAuction> activeAuctions;
    public AuctionPlatform(AuctionService auctionService, SoldLotService soldLotService, LotService lotService){
        this.auctionService = auctionService;
        this.lotService=lotService;
        this.soldLotService = soldLotService;
        auctionService.setPlatformObserver(this);
        activeAuctions = new ArrayList<ActiveAuction>();
        createQueue();
        this.start();
    }
    @Override
    public void run() {
        while(true){
            try {
                if(!auctionsQueue.isEmpty())
                    if(auctionsQueue.peek().getBeginTime().isBefore(LocalTime.now())){
                        ActiveAuction newActiveAuction = new ActiveAuction(auctionService.getAuctionWithLots(auctionsQueue.remove().getAuctionId()));
                        activeAuctions.add(newActiveAuction);
                        newActiveAuction.joinPlatformObserver(this);
                        auctionService.auctionStarted(newActiveAuction);
                        lotService.auctionStarted(newActiveAuction);
                        soldLotService.auctionStarted(newActiveAuction);
                        continue;
                    }
                sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Main thread Interrupted");
            }
        }
    }
    private void createQueue(){
        try {
            auctionsQueue = auctionService.getAuctionsQueue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public ActiveAuctionInterface getActiveAuction(int id) throws Exception {
        for(ActiveAuction auction : activeAuctions){
            if(auction.getAuctionId()==id)return auction;
        }
        throw new Exception("Active auction not found");
    }
    @Override
    public void auctionsChanged() {
        createQueue();
    }

    @Override
    public void auctionEnded(ActiveAuction auction) {
        activeAuctions.remove(auction);
    }

}
