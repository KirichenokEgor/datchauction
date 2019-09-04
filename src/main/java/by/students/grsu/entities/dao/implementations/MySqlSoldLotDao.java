package by.students.grsu.entities.dao.implementations;


import by.students.grsu.entities.dao.interfaces.SoldLotDao;
import by.students.grsu.entities.lot.SoldLot;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlSoldLotDao implements SoldLotDao {
    private JdbcTemplate template;
    private ResultSetExtractor<List<SoldLot>> soldLotListExtractor = new ResultSetExtractor<List<SoldLot>>() {
        public List<SoldLot> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<SoldLot> lotList = new ArrayList<>();
            while(rs.next()) {
                lotList.add(new SoldLot(rs.getNString("buyer"), rs.getNString("seller"), rs.getInt("lotId"), rs.getDouble("price")));
            }
            return lotList;
        }
    };
    private ResultSetExtractor<SoldLot> soldLotExtractor = new ResultSetExtractor<SoldLot>() {
        public SoldLot extractData(ResultSet rs) throws SQLException, DataAccessException {
            return rs.next() ? new SoldLot(rs.getString("buyer"), rs.getNString("seller"), rs.getInt("lotId"), rs.getDouble("price")) : null;
        }
    };

    public MySqlSoldLotDao(JdbcTemplate template) {
        this.template = template;
    }

    public void addSoldLot(int lotId, String buyerUsername, String sellerUsername, double price) {
        template.execute("INSERT INTO soldLots VALUES(" + lotId + ", '" + buyerUsername + "', " + price + ",'" + sellerUsername + "')");
    }

    public void deleteSoldLot(int lotId) {
        template.execute("DELETE FROM soldLots WHERE lotId=" + lotId);
    }

    public List<SoldLot> getSoldLotsByUser(String username) throws SQLException {
        return template.query("SELECT * FROM soldLots WHERE buyer='" + username + "'", soldLotListExtractor);
    }

    public SoldLot getSoldLotById(int lotId) throws Exception {
        SoldLot soldLot = template.query("SELECT * FROM soldLots WHERE lotId=" + lotId, soldLotExtractor);
        if (soldLot != null) {
            return soldLot;
        } else {
            throw new Exception("SoldLot not found");
        }
    }
}
