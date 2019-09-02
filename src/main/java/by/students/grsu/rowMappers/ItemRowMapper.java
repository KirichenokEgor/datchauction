package by.students.grsu.rowMappers;

import by.students.grsu.entities.item.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRowMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Item(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("description"),resultSet.getString("owner"),resultSet.getInt("lotID"));
    }
}
