package by.students.grsu.entities.dao;


import by.students.grsu.entities.lot.SoldLot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SoldLotDao {
    private Statement statement;

    public SoldLotDao(Statement statement) {
        try {
            statement.execute("use datchauction");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.statement = statement;
    }
    public void addSoldLot(int lotId,String buyerUsername,double price) throws Exception {
        statement.execute("INSERT INTO soldLots VALUES("+lotId+", \'"+buyerUsername+"\', "+price+")");
    }
    public void deleteSoldLot(int lotId) throws Exception {
        ResultSet rs = statement.executeQuery("SELECT * FROM soldLots WHERE lotId="+lotId);
        if(rs.next())statement.execute("DELETE FROM soldLots WHERE lotId="+lotId);
    }
    public List<SoldLot> getSoldLotsByUser(String username) throws SQLException {
        List<SoldLot> lotList = new ArrayList<SoldLot>();
        ResultSet rs = statement.executeQuery("SELECT * FROM soldLots WHERE buyer=\'"+username+"\'");
        while(rs.next())
            lotList.add(new SoldLot(rs.getNString("buyer"),rs.getInt("lotId"),rs.getDouble("price")));
        return lotList;
    }
    public SoldLot getSoldLotById(int lotId) throws Exception {
        ResultSet rs = statement.executeQuery("SELECT * FROM soldLots WHERE lotId="+lotId);
        if(rs.next())return new SoldLot(rs.getString("buyer"),rs.getInt("lotId"),rs.getDouble("price"));
        else throw new Exception("SoldLot not found");
    }
}
