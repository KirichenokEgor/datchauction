package by.students.grsu.entities.auction;

import by.students.grsu.entities.lot.LotInfo;


import java.time.LocalTime;
import java.util.List;

public interface AuctionInfo {
    int getID();

    String getDescription();

    LocalTime getBeginTime();

    List<LotInfo> getILots();

    public int getMaxDuration();
}
