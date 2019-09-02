package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.auction.ActiveAuctionInterface;

public interface AuctionPlatform {
    ActiveAuctionInterface getActiveAuction(int id) throws Exception;
}
