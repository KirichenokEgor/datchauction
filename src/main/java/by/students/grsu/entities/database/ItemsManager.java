package by.students.grsu.entities.database;

import by.students.grsu.entities.core.AuctionException;
import by.students.grsu.entities.item.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemsManager {
    private Statement st;
    public ItemsManager(Statement st) throws SQLException {
        this.st = st;
    }
    public Item addItem(String name, String description, String ownerName) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT ID FROM items where name=\'<empty>\'");
        int ID;
        if(rs.next()){
            ID = rs.getInt("ID");
            st.execute("UPDATE items SET name=\'"+name+"\', description=\'"+description+
                    "\', owner=\'"+ownerName+"\' WHERE ID="+ID);
        }else {
            rs = st.executeQuery("SELECT MAX(ID) FROM items");
            rs.next();
            ID = rs.getInt("MAX(ID)")+1;
            st.execute("INSERT INTO items VALUES ("+ID+", \'"+name+"\', \'"+description+"\', \'FREE\', \'"+ownerName+"\')");
        }
        return getItemById(ID);
    }
    public Item getItemById(int ID) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM items WHERE ID=" + ID);
        if(rs.next() && !rs.getString("name").equals("<empty>")){
            return new Item(rs.getInt("ID"),rs.getString("name"),rs.getString("description"),rs.getString("owner"));
        }else throw new AuctionException("Item not found",21);
    }
    public List<Item> getItemsByOwner(String ownerName) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM items WHERE owner=\'"+ownerName+"\'");
        if(!rs.next())throw new AuctionException("This user has no items",22);
        List<Item> itemList = new ArrayList<Item>();
        do{
            itemList.add(new Item(rs.getInt("ID"),rs.getString("name"),rs.getString("description"),rs.getString("owner")));
        }while(rs.next());
        return itemList;
    }
    public void deleteItemById(int ID) throws SQLException, AuctionException {
        if(st.executeQuery("SELECT * FROM items WHERE ID="+ID).next())
            st.execute("UPDATE items SET name=\'<empty>\', description=\'<empty>\', owner= \'<empty>\' WHERE ID=" + ID);
        else throw new AuctionException("Item not found",21);
    }
}
