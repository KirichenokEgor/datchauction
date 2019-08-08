package by.students.grsu.entities.lot;

import by.students.grsu.entities.item.ItemInfo;

import java.util.List;

public interface LotInfo {
    public String getName();

    public int getID();

    LotStatus getStatus();

    double getCurrentPrice();

    List<ItemInfo> getItems();
}
