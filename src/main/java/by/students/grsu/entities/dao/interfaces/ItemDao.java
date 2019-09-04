package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.item.Item;

import java.util.List;

public interface ItemDao {
    int addItem(String name, String description, String ownerName) throws Exception;
    Item getItemById(int id);
    List<Item> getItemsByOwner(String ownerName) throws Exception;
    void deleteItemById(int id) throws Exception;
    void deleteItemsByLotId(int lotId) throws Exception;
    List<Item> getItemsByLot(int lotId) throws Exception;
    void setItemOnLot(int itemId,int lotId) throws Exception;
    void freeItemsByLot(int lotId)throws Exception;
    List<Item> getAllFreeItems() throws Exception;
}
