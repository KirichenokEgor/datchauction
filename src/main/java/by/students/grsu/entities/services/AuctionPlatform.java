package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.Auction;

import java.util.List;

public class AuctionPlatform {
    private MainThread mThread;
    private List<Auction> auctions;
    public List<Auction> getAuctionsForThread() {
        return auctions;
    }

}
