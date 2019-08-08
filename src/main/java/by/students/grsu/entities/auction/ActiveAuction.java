package by.students.grsu.entities.auction;

import by.students.grsu.entities.lot.Lot;

import java.time.LocalTime;
import java.util.List;

public interface ActiveAuction {
    LocalTime getBeginTime();

    List<Lot> getLots();

    int getTick();

    int getMaxDuration();

    void makeDone();
    void makeActive();
}
