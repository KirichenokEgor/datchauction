package by.students.grsu.entities.dao;

import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.item.Item;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    private Statement st;
//    public ItemDao(Statement st){
//        this.st = st;
//    }
    @Autowired
    public void setSt(Statement st) {
        this.st = st;
    }
    public Item addItem(String name, String description, String ownerName) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT id FROM items where name=\'<empty>\'");
        int id;
        if(rs.next()){
            id = rs.getInt("id");
            st.execute("UPDATE items SET name=\'"+name+"\', description=\'"+description+
                    "\',  status=\'FREE\', owner=\'"+ownerName+"\' WHERE id="+id);
        }else {
            rs = st.executeQuery("SELECT MAX(id) FROM items");
            rs.next();
            id = rs.getInt("MAX(id)")+1;
            st.execute("INSERT INTO items VALUES ("+id+", \'"+name+"\', \'"+description+"\', \'"+ownerName+"\', 0)");
        }
        return  getItemById(id);
    }
    public Item getItemById(int id) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM items WHERE id=" + id);
        if(rs.next() && !rs.getString("name").equals("<empty>")){
            return new Item(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("owner"),rs.getInt("lotid"));
        }else throw new AuctionException("Item not found",21);
    }
    public List<Item> getItemsByOwner(String ownerName) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM items WHERE owner=\'"+ownerName+"\'");
        if(!rs.next())throw new AuctionException("This user has no items",22);
        List<Item> itemList = new ArrayList<Item>();
        do{
            itemList.add(new Item(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("owner"),rs.getInt("lotid")));
        }while(rs.next());
        return itemList;
    }
    public void deleteItemById(int id) throws SQLException, AuctionException {
        if(st.executeQuery("SELECT * FROM items WHERE id="+id).next())
            st.execute("UPDATE items SET name=\'<empty>\', description=\'<empty>\', status=\'<empty>\', owner= \'<empty>\',lotid=0 WHERE id=" + id);
        else throw new AuctionException("Item not found",21);
    }
}
