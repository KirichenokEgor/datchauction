package by.students.grsu.entities.services;

import by.students.grsu.entities.dao.ItemDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.users.User;
import by.students.grsu.entities.users.UserRole;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemService {
    private ItemDao itemDao;
    public ItemService(ItemDao itemDao){
        this.itemDao=itemDao;
    }
    public Item addItem(String name, String description, User owner) throws SQLException, AuctionException {
        if(owner.getRole()== UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return itemDao.addItem(name, description,  owner.getUsername());
    }
    public ItemInfo getItemById(int id) throws SQLException, AuctionException {
        return itemDao.getItemById(id);
    }
    public List<Item> getItemsByOwner(User owner) throws Exception {
        if(owner.getRole()==UserRole.Buyer)throw new Exception("Buyer can't make or have items");
        return itemDao.getItemsByOwner(owner.getUsername());
    }
    public void deleteItemById(int id) throws Exception {
        try {
            itemDao.deleteItemById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
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
    public List<Item> getFreeItemsByOwner(User owner){
        try {
            List<Item> itemList = getItemsByOwner(owner);
            for(int i = 0;i<itemList.size();i++)
                if(itemList.get(i).isOnLot())continue;
                else itemList.remove(i);
            return itemList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void createLotWithItems(int[] itemsIds, int lotId) throws Exception {
        for (int id : itemsIds)
            itemDao.setItemOnLot(id, lotId);
    }
}

