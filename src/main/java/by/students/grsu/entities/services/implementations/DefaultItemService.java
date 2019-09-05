package by.students.grsu.entities.services.implementations;

import by.students.grsu.entities.dao.interfaces.ItemDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.services.interfaces.ItemService;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.util.ArrayList;
import java.util.List;

public class DefaultItemService implements ItemService {
    private ItemDao itemDao;
    public DefaultItemService(ItemDao itemDao){
        this.itemDao=itemDao;
    }
    @Override
    public int addItem(String name, String description, SecurityContextHolderAwareRequestWrapper contextHolder){
        return itemDao.addItem(name, description, contextHolder.getRemoteUser());
    }
    @Override
    public ItemInfo getItemById(int id){
        return itemDao.getItemById(id);
    }
    @Override
    public List<Item> getItemsByOwner(SecurityContextHolderAwareRequestWrapper contextHolder){
        return itemDao.getItemsByOwner(contextHolder.getRemoteUser());
    }
    @Override
    public void deleteItemById(int id) throws Exception {
        try {
            itemDao.deleteItemById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public List<Item> getItemsByIds(int[] ids){
        try {
            List<Item> itemsList = new ArrayList<Item>();
            for (int id : ids) {
                itemsList.add(itemDao.getItemById(id));
            }
            return itemsList;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public List<Item> getFreeItemsByOwner(SecurityContextHolderAwareRequestWrapper contextHolder){
        try {
            List<Item> itemList = getItemsByOwner(contextHolder);
            for(int i = 0; i < itemList.size(); i++)
                if(!itemList.get(i).isOnLot())continue;
                else {
                    itemList.remove(i);
                    i--;
                }
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<Item> getAllFreeItems(){
        return itemDao.getAllFreeItems();
    }
    @Override
    public void createLotWithItems(int[] itemsIds, int lotId){
        for (int id : itemsIds)
            itemDao.setItemOnLot(id, lotId);
    }
    @Override
    public List<Item> getItemsByLot(int id) {
        try {
            return itemDao.getItemsByLot(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public void freeItemsByLot(int lotId){
        try {
            itemDao.freeItemsByLot(lotId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void deleteItemsByLotId(int lotId){
        try {
            itemDao.deleteItemsByLotId(lotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

