package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.dao.interfaces.ItemDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.rowMappers.ItemRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MySqlItemDao implements ItemDao {
    private JdbcTemplate jdbcTemplate;
    public MySqlItemDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public synchronized int addItem(String name, String description, String ownerName) throws Exception {
        jdbcTemplate.update("INSERT INTO items VALUES (NULL, \'" + name + "\', \'" + description + "\', \'"
                + ownerName + "\', 0)");
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM items", Integer.class);
    }

    public Item getItemById(int id){
        return jdbcTemplate.queryForObject("SELECT * FROM items WHERE id=" + id, new ItemRowMapper());
    }

    public List<Item> getItemsByOwner(String ownerName) throws Exception {
        return jdbcTemplate.query("SELECT * FROM items WHERE owner=\'"+ownerName+"\'", new ItemRowMapper());
    }
    public void deleteItemById(int id) throws Exception {
        jdbcTemplate.execute("DELETE FROM items WHERE id=" + id);
    }
    public void deleteItemsByLotId(int lotId) throws Exception {
        jdbcTemplate.execute("DELETE FROM items WHERE lotID=" + lotId);
    }
    public List<Item> getItemsByLot(int lotId) throws Exception {
        return jdbcTemplate.query("SELECT * FROM items WHERE lotID="+lotId, new ItemRowMapper());
    }
    public void setItemOnLot(int itemId,int lotId) throws Exception {
        jdbcTemplate.update("UPDATE items SET lotID=" + lotId + " WHERE id=" + itemId);
    }
    public void freeItemsByLot(int lotId)throws Exception {
        jdbcTemplate.update("UPDATE items SET lotID=0 WHERE lotID=" + lotId);
    }
    public List<Item> getAllFreeItems() throws Exception {
        return jdbcTemplate.query("SELECT * FROM items WHERE lotID=0", new ItemRowMapper());
    }
}