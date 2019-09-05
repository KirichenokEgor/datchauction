package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.item.Item;

import java.util.List;

public interface ItemDao {
    int addItem(String name, String description, String ownerName);
    Item getItemById(int id);
    List<Item> getItemsByOwner(String ownerName);
    void deleteItemById(int id);
    void deleteItemsByLotId(int lotId);
    List<Item> getItemsByLot(int lotId);
    void setItemOnLot(int itemId,int lotId);
    void freeItemsByLot(int lotId);
    List<Item> getAllFreeItems();
}
