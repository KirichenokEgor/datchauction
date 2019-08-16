package by.students.grsu.entities.auction;

import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.users.Follower;
import by.students.grsu.entities.users.User;

import java.util.List;

public interface ActiveAuctionInterface {
    List<Lot> getLots();
    void buyLot(int id, User user) throws Exception;
    void join(Follower follower);
    void leave(Follower follower);

}
