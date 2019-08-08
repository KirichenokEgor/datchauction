package by.students.grsu.entities.services;

import by.students.grsu.entities.dao.ItemDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.item.ItemInfo;
import by.students.grsu.entities.users.User;
import by.students.grsu.entities.users.UserRole;

import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private ItemDao IM;
    public Item addItem(String name, String description, User owner) throws SQLException, AuctionException {
        if(owner.getRole()== UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return IM.addItem(name, description,  owner.getUsername());
    }
    public ItemInfo getItemById(int id) throws SQLException, AuctionException {
        return IM.getItemById(id);
    }
    public List<Item> getItemsByOwner(User owner) throws SQLException, AuctionException {
        if(owner.getRole()==UserRole.Buyer)throw new AuctionException("Buyer can't make or have items",41);
        return IM.getItemsByOwner(owner.getUsername());
    }
    public void deleteItemById(int id) throws SQLException, AuctionException {
        IM.deleteItemById(id);
    }
}
