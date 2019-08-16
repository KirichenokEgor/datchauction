package by.students.grsu.entities.auction;

import java.time.LocalTime;

public class AuctionStartTime {
    private int auctionId;
    private LocalTime beginTime;
    public AuctionStartTime(int auctionId,LocalTime beginTime){
        this.auctionId=auctionId;
        this.beginTime=beginTime;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }
}
