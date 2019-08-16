package by.students.grsu.entities.services;

public interface LotFollower {
    void lotSold(int lotId);
    void auctionEnded(int auctionId);
    void tickHappened();

}
