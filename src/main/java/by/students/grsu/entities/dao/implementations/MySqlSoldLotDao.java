package by.students.grsu.entities.dao.implementations;


import by.students.grsu.entities.dao.interfaces.SoldLotDao;
import by.students.grsu.entities.lot.SoldLot;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlSoldLotDao implements SoldLotDao {
    private JdbcTemplate jdbcTemplate;

    public MySqlSoldLotDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addSoldLot(int lotId,String buyerUsername,String sellerUsername,double price) throws Exception {
        synchronized (new Integer(3)) {
            statement.execute("INSERT INTO soldLots VALUES(" + lotId + ", \'" + buyerUsername + "\', " + price + ",\'" + sellerUsername + "\')");
        }
    }
    public void deleteSoldLot(int lotId) throws Exception {
        synchronized (new Integer(3)) {
            ResultSet rs = statement.executeQuery("SELECT * FROM soldLots WHERE lotId=" + lotId);
            if (rs.next()) statement.execute("DELETE FROM soldLots WHERE lotId=" + lotId);
        }
    }
    public List<SoldLot> getSoldLotsByUser(String username) throws SQLException {
        List<SoldLot> lotList = new ArrayList<SoldLot>();
        ResultSet rs = statement.executeQuery("SELECT * FROM soldLots WHERE buyer=\'"+username+"\'");
        while(rs.next())
            lotList.add(new SoldLot(rs.getNString("buyer"),rs.getNString("seller"),rs.getInt("lotId"),rs.getDouble("price")));
        return lotList;
    }
    public SoldLot getSoldLotById(int lotId) throws Exception {
        ResultSet rs = statement.executeQuery("SELECT * FROM soldLots WHERE lotId="+lotId);
        if(rs.next())return new SoldLot(rs.getString("buyer"),rs.getNString("seller"),rs.getInt("lotId"),rs.getDouble("price"));
        else throw new Exception("SoldLot not found");
    }
}
