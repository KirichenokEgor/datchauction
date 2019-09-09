package by.students.grsu.entities.auction;

import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.lot.LotStatus;
import by.students.grsu.entities.services.interfaces.followersAndObservers.*;
import by.students.grsu.entities.users.Follower;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ActiveAuction extends Thread implements ActiveAuctionInterface{
    private Auction auction;
    private List<Follower> userFollowers;
    private AuctionFollower auctionFollower;
    private LotFollower lotFollower;
    private SoldLotFollower soldLotFollower;
    private AuctionPlatformObserver auctionPlatformObserver;
    private FollowedAuctionFollower followedAuctionFollower;
    public ActiveAuction(Auction auction){
        auction.makeActive();
        this.auction=auction;
        userFollowers= new ArrayList<Follower>();
    }

    @Override
    public void run(){
        //DEBUG
        System.out.println("Auction " + auction.getID() + " started");
        while(true){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting observers");
            }
            if(auctionFollower == null) continue;
            if(lotFollower == null) continue;
            if(soldLotFollower == null) continue;
            if(auctionPlatformObserver == null) continue;
            if(followedAuctionFollower == null) continue;
            break;
        }
        int tick = auction.getTick()*1000;
        LocalTime endTime = LocalTime.now().plusMinutes(auction.getMaxDuration());
        //System.out.println("Tick time = "+tick);
        //System.out.println("End time = "+endTime);
        List<Lot> lots;
        boolean auctionEnd;
        while(endTime.isAfter(LocalTime.now())){
            try {
                sleep(tick);
            } catch (InterruptedException e) {
                System.out.println("ActiveAuction " + auction.getID() + " interrupted");
            }
            auctionEnd=true;
            lots = auction.getLots();
            for(Lot lot : lots)
                if (lot.getStatus() == LotStatus.Registered){
                    lot.makePriceStep();
                    //  System.out.println(lot.getCurrentPrice());
                    auctionEnd=false;
                }
            if(auctionEnd)break;
            tickHappened();
        }
        auctionEnded();
    }

    @Override
    public List<Lot> getLots() {
        return auction.getLots();
    }


    @Override
    public synchronized void buyLot(int lotId, String user) throws Exception {
        for(Lot lot : auction.getLots())
            if(lot.getID()==lotId){
                if(lot.getStatus()==LotStatus.Sold)throw new Exception("Lot already sold!");
                lot.setSold();
                lotSold(lotId,user,lot.getOwner(),lot.getCurrentPrice());
                break;
            }
    }

    public void joinAuctionFollower(AuctionFollower auctionFollower){
        this.auctionFollower = auctionFollower;
    }
    public void joinLotFollower(LotFollower lotFollower){
        this.lotFollower = lotFollower;
    }
    public void joinSoldLotFollower(SoldLotFollower soldLotFollower){
        this.soldLotFollower = soldLotFollower;
    }
    public void joinFollowedAuctionFollower(FollowedAuctionFollower followedAuctionFollower){
        this.followedAuctionFollower = followedAuctionFollower;
    }
    public void joinPlatformObserver(AuctionPlatformObserver platformObserver){
        auctionPlatformObserver=platformObserver;
    }
    @Override
    public void join(Follower follower) {
        userFollowers.add(follower);
    }
    public int getAuctionId(){
        return auction.getID();
    }
    @Override
    public void leave(Follower follower) {
        userFollowers.remove(follower);
    }

    private void tickHappened(){
        for(Follower follower : userFollowers)
            follower.tickHappened(auction.getID());
    }
    private void lotSold(int id,String user,String seller,double price){
        soldLotFollower.lotSold(user,seller,id,price);
        lotFollower.lotSold(id);
        for(Follower follower : userFollowers)
            follower.lotSold(auction.getID());
    }
    private void auctionEnded(){
        System.out.println("Auction " + auction.getID() + " ended");
        auctionFollower.auctionEnded(auction.getID());
        lotFollower.auctionEnded(auction.getID());
        followedAuctionFollower.auctionEnded(auction.getID());
        auctionPlatformObserver.auctionEnded(this);
        for(Follower follower : userFollowers)
            follower.auctionEnded(auction.getID());
    }
    @Override
    public Auction getAuction(){
        return auction;
    }
}
