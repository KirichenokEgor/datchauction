package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.util.List;

public interface ItemService {
    int addItem(String name, String description, SecurityContextHolderAwareRequestWrapper contextHolder);
    ItemInfo getItemById(int id);
    List<Item> getItemsByOwner(SecurityContextHolderAwareRequestWrapper contextHolder);
    void deleteItemById(int id) throws Exception;
    List<Item> getItemsByIds(int[] ids);
    List<Item> getFreeItemsByOwner(SecurityContextHolderAwareRequestWrapper contextHolder);
    List<Item> getAllFreeItems();
    void createLotWithItems(int[] itemsIds, int lotId);
    List<Item> getItemsByLot(int id);
    void freeItemsByLot(int lotId);
    void deleteItemsByLotId(int lotId);
}
