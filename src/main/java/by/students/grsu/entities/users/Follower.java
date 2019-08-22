package by.students.grsu.entities.users;

public interface Follower {
    void tickHappened(int auctionId);
    void auctionEnded(int auctionId);
    void lotSold(int auctionId);
}
