package by.students.grsu.entities.services;

import by.students.grsu.entities.dao.ItemDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.lot.Lot;
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
    public int addItem(String name, String description, User owner) throws Exception {
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
            for(int i = 0; i < itemList.size(); i++)
                if(!itemList.get(i).isOnLot())continue;
                else {
                    itemList.remove(i);
                    i--;
                }
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
    public List<Item> getItemsByLot(int id) {
        try {
            return itemDao.getItemsByLot(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void freeItemsByLot(int lotId){
        try {
            itemDao.freeItemsByLot(lotId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteItemsByLotId(int lotId){
        try {
            itemDao.deleteItemsByLotId(lotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

