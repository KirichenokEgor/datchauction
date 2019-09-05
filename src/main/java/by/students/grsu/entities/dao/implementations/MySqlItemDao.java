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

    @Override
    public synchronized int addItem(String name, String description, String ownerName){
        jdbcTemplate.update("INSERT INTO items VALUES (NULL, \'" + name + "\', \'" + description + "\', \'"
                + ownerName + "\', 0)");
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM items", Integer.class);
    }

    @Override
    public Item getItemById(int id){
        return jdbcTemplate.queryForObject("SELECT * FROM items WHERE id=" + id, new ItemRowMapper());
    }

    @Override
    public List<Item> getItemsByOwner(String ownerName){
        return jdbcTemplate.query("SELECT * FROM items WHERE owner=\'"+ownerName+"\'", new ItemRowMapper());
    }

    @Override
    public void deleteItemById(int id){
        jdbcTemplate.execute("DELETE FROM items WHERE id=" + id);
    }

    @Override
    public void deleteItemsByLotId(int lotId){
        jdbcTemplate.execute("DELETE FROM items WHERE lotID=" + lotId);
    }

    @Override
    public List<Item> getItemsByLot(int lotId){
        return jdbcTemplate.query("SELECT * FROM items WHERE lotID="+lotId, new ItemRowMapper());
    }

    @Override
    public void setItemOnLot(int itemId,int lotId){
        jdbcTemplate.update("UPDATE items SET lotID=" + lotId + " WHERE id=" + itemId);
    }

    @Override
    public void freeItemsByLot(int lotId){
        jdbcTemplate.update("UPDATE items SET lotID=0 WHERE lotID=" + lotId);
    }

    @Override
    public List<Item> getAllFreeItems(){
        return jdbcTemplate.query("SELECT * FROM items WHERE lotID=0", new ItemRowMapper());
    }
}