package by.students.grsu.entities.services;

import by.students.grsu.entities.auction.Auction;

import java.util.List;

public interface AuctionsCore {
   List<Auction> getAuctionsForThread();
}
