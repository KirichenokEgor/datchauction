package by.students.grsu.entities.dao;

import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.item.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    private Statement statement;
    public ItemDao(Statement statement){
        this.statement = statement;
    }
    public Item addItem(String name, String description, String ownerName) throws SQLException, AuctionException {
        ResultSet rs = statement.executeQuery("SELECT id FROM items where name=\'<empty>\'");
        int id;
        if(rs.next()){
            id = rs.getInt("id");
            statement.execute("UPDATE items SET name=\'"+name+"\', description=\'"+description+
                    "\', owner=\'"+ownerName+"\', lotID=0 WHERE id="+id);
        }else {
            rs = statement.executeQuery("SELECT MAX(id) FROM items");
            rs.next();
            id = rs.getInt("MAX(id)")+1;
            statement.execute("INSERT INTO items VALUES ("+id+", \'"+name+"\', \'"+description+"\', \'"+ownerName+"\', 0)");
        }
        return  getItemById(id);
    }
    public Item getItemById(int id) throws SQLException, AuctionException {
        ResultSet rs = statement.executeQuery("SELECT * FROM items WHERE id=" + id);
        if(rs.next() && !rs.getString("name").equals("<empty>")){
            return new Item(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("owner"),rs.getInt("lotid"));
        }else throw new AuctionException("Item not found",21);
    }
    public List<Item> getItemsByOwner(String ownerName) throws Exception{
        ResultSet rs = statement.executeQuery("SELECT * FROM items WHERE owner=\'"+ownerName+"\'");
        if(!rs.next())throw new AuctionException("This user has no items",22);
        List<Item> itemList = new ArrayList<Item>();
        do{
            itemList.add(new Item(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("owner"),rs.getInt("lotid")));
        }while(rs.next());
        return itemList;
    }
    public void deleteItemById(int id) throws Exception {
        try {
            if(statement.executeQuery("SELECT * FROM items WHERE id="+id).next())
                statement.execute("UPDATE items SET name=\'<empty>\', description=\'<empty>\', owner= \'<empty>\',lotID=0 WHERE id=" + id);
            else throw new Exception("Item not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<Item> getItemsByLot(int lotId) throws Exception {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM items WHERE lotID="+lotId);
            List<Item> itemList = new ArrayList<Item>();
            while(rs.next())
                itemList.add(new Item(rs.getInt("ID"),rs.getString("name"),rs.getString("description"),rs.getString("owner"),rs.getInt("lotID")));
            return itemList;
        } catch (SQLException e) {
            throw new Exception(e.getMessage() + "Idao");
        }
    }
    public void setItemOnLot(int itemId,int lotId) throws Exception {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM items WHERE id=" + itemId);
            if(rs.next())
                statement.execute("UPDATE items SET lotID=" + lotId + " WHERE id=" + itemId);
            else throw new Exception("Item not found");
        }
        catch (SQLException e) {
            throw new Exception(e.getMessage());
        }

    }
    public void freeItemsByLot(int lotId) throws Exception {
        try {
            //ResultSet rs = statement.executeQuery("SELECT * FROM items WHERE id=" + itemId);
            //if(rs.next())
            statement.execute("UPDATE items SET lotID=0 WHERE lotID=" + lotId);
            //else throw new Exception("Item not found");
        }
        catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

}
