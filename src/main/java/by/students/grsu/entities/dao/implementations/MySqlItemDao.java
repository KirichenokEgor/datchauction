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
        jdbcTemplate.update("INSERT INTO item VALUES (NULL, ?, ?, ?, 0)",name,description,ownerName);
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM item", Integer.class);
    }

    @Override
    public Item getItemById(int id){
        return jdbcTemplate.queryForObject("SELECT * FROM item WHERE id=?",new Object[]{id}, new ItemRowMapper());
    }

    @Override
    public List<Item> getItemsByOwner(String ownerName){
        return jdbcTemplate.query("SELECT * FROM item WHERE owner=?",ps -> {ps.setString(1,ownerName);}, new ItemRowMapper());
    }

    @Override
    public void deleteItemById(int id){
        jdbcTemplate.update("DELETE FROM item WHERE id= ?", id);
    }

    @Override
    public void deleteItemsByLotId(int lotId){
        jdbcTemplate.update("DELETE FROM item WHERE lot_id= ?", lotId);
    }

    @Override
    public List<Item> getItemsByLot(int lotId){
        return jdbcTemplate.query("SELECT * FROM item WHERE lot_id= ?",ps -> {ps.setInt(1,lotId);}, new ItemRowMapper());
    }

    @Override
    public void setItemOnLot(int itemId,int lotId){
        jdbcTemplate.update("UPDATE item SET lot_id= ? WHERE id= ?",lotId,itemId);
    }

    @Override
    public void freeItemsByLot(int lotId){
        jdbcTemplate.update("UPDATE item SET lot_id=0 WHERE lot_id= ?", lotId);
    }

    @Override
    public List<Item> getAllFreeItems(){
        return jdbcTemplate.query("SELECT * FROM item WHERE lot_id=0", new ItemRowMapper());
    }
}