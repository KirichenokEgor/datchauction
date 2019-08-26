package by.students.grsu.entities.auction;

public class FollowedAuction {
    private AuctionInfo auction;
    private boolean followed;

    public FollowedAuction(AuctionInfo auction, boolean followed) {
        this.auction = auction;
        this.followed = followed;
    }

    public AuctionInfo getAuction() {
        return auction;
    }

    public boolean isFollowed() {
        return followed;
    }
}
