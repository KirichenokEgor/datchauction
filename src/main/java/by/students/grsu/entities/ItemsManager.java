package by.students.grsu.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsManager {
    private Statement st;
    public ItemsManager(Statement st) throws SQLException {
        this.st = st;
    }
    public Item addItem(String name, String description, String status, String ownerName) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT ID FROM items where name=\'<empty>\'");
        int ID;
        if(rs.next()){
            ID = rs.getInt("ID");
            st.execute("UPDATE items SET name=\'"+name+"\', description=\'"+description+
                    "\',  status=\'"+status+"\', owner=\'"+ownerName+"\' WHERE ID="+ID);
        }else {
            rs = st.executeQuery("SELECT MAX(ID) FROM items");
            rs.next();
            ID = rs.getInt("MAX(ID)")+1;
            st.execute("INSERT INTO items VALUES ("+ID+", \'"+name+"\', \'"+description+"\', \'"+status+"\', \'"+ownerName+"\')");
        }
        return  getItemById(ID);
    }
    public Item getItemById(int ID) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM items WHERE ID=" + ID);
        if(rs.next() && !rs.getString("name").equals("<empty>")){
            return new Item(rs.getInt("ID"), rs.getString("name"), rs.getString("description"), rs.getString("status"), rs.getString("owner"));
        }else throw new AuctionException("Item not found",21);
    }
    public List<Item> getItemsByOwner(String ownerName) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM items WHERE owner=\'"+ownerName+"\'");
        if(!rs.next())throw new AuctionException("This user has no items",22);
        //Map<Integer,Item> itemMap = new HashMap<Integer, Item>();
        List<Item> items = new ArrayList<Item>();
        do{
            items.add(new Item(rs.getInt("ID"), rs.getString("name"), rs.getString("description"), rs.getString("status"), rs.getString("owner")));
            //itemMap.put(rs.getInt("ID"), new Item(rs.getInt("ID"), rs.getString("name"), rs.getString("description"), rs.getString("status"), rs.getString("owner")));
        }while(rs.next());
        return items;
    }
    public void deleteItemById(int ID) throws SQLException, AuctionException {
        if(st.executeQuery("SELECT * FROM items WHERE ID="+ID).next())
            st.execute("UPDATE items SET name=\'<empty>\', description=\'<empty>\', status=\'<empty>\', owner= \'<empty>\' WHERE ID=" + ID);//////////////////////нужно ли занулять ID? мб в минфриид писать свободный ид из базы?
        else throw new AuctionException("Item not found",21);
    }
}
