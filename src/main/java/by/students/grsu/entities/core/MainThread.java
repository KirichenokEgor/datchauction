package by.students.grsu.entities.core;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStatus;
import by.students.grsu.entities.auction.AuctionThread;

import java.time.LocalTime;
import java.util.List;

class MainThread extends Thread {
    private List<Auction> auctions;
    public MainThread(AuctionsCore core){
       auctions = core.getAuctionsForThread();
    }
    @Override
    public void run() {
        try {
            while (true) {
                //System.out.println("\nMain thread: tick");
                for (Auction auction : auctions) {
                    if (auction.getStatus() == AuctionStatus.Planned && auction.getBeginTime().isBefore(LocalTime.now())) {
                        new AuctionThread(auction).start();
                    }
                }
                sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

