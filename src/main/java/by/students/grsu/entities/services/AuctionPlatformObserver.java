package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.ActiveAuction;

public interface AuctionPlatformObserver {
    void auctionsChanged();
    void auctionEnded(ActiveAuction auction);
}
