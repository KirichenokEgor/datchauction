package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.services.AuctionException;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.sql.SQLException;
import java.util.List;

public interface ItemService {
    int addItem(String name, String description, SecurityContextHolderAwareRequestWrapper contextHolder) throws Exception;
    ItemInfo getItemById(int id) throws SQLException, AuctionException;
    List<Item> getItemsByOwner(SecurityContextHolderAwareRequestWrapper contextHolder) throws Exception;
    void deleteItemById(int id) throws Exception;
    List<Item> getItemsByIds(int[] ids);
    List<Item> getFreeItemsByOwner(SecurityContextHolderAwareRequestWrapper contextHolder);
    List<Item> getAllFreeItems() throws Exception;
    void createLotWithItems(int[] itemsIds, int lotId) throws Exception;
    List<Item> getItemsByLot(int id);
    void freeItemsByLot(int lotId);
    void deleteItemsByLotId(int lotId);
}
