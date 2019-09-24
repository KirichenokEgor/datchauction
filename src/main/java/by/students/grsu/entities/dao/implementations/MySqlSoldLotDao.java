package by.students.grsu.entities.dao.implementations;


import by.students.grsu.entities.dao.interfaces.SoldLotDao;
import by.students.grsu.entities.lot.SoldLot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.ArrayList;
import java.util.List;

public class MySqlSoldLotDao implements SoldLotDao {
    private JdbcTemplate template;
    private ResultSetExtractor<List<SoldLot>> soldLotListExtractor = rs -> {
        List<SoldLot> lotList = new ArrayList<>();
        while(rs.next()) {
            lotList.add(new SoldLot(rs.getNString("buyer"), rs.getNString("seller"), rs.getInt("lot_id"), rs.getDouble("price")));
        }
        return lotList;
    };
    private ResultSetExtractor<SoldLot> soldLotExtractor = rs -> {
        return rs.next() ? new SoldLot(rs.getString("buyer"), rs.getNString("seller"), rs.getInt("lot_id"), rs.getDouble("price")) : null;
    };

    public MySqlSoldLotDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void addSoldLot(int lotId, String buyerUsername, String sellerUsername, double price) {
        template.update("INSERT INTO sold_lot VALUES(?,?,?,?)",lotId,buyerUsername,price,sellerUsername);
    }

    @Override
    public void deleteSoldLot(int lotId) {
        template.update("DELETE FROM sold_lot WHERE lot_id= ?", lotId);
    }

    @Override
    public List<SoldLot> getSoldLotsByUser(String username){
        return template.query("SELECT * FROM sold_lot WHERE buyer= ?",ps -> {ps.setString(1,username);}, soldLotListExtractor);
    }

    @Override
    public SoldLot getSoldLotById(int lotId) throws Exception {
        SoldLot soldLot = template.query("SELECT * FROM sold_lot WHERE lot_id= ?",ps -> {ps.setInt(1,lotId);}, soldLotExtractor);
        if (soldLot != null) {
            return soldLot;
        } else {
            throw new Exception("SoldLot not found");
        }
    }
}
