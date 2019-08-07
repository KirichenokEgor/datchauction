package by.students.grsu.entities.dao;

import by.students.grsu.entities.lot.Lot;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotDao {
    private Statement st;
//    public LotDao(Statement st){
//        this.st = st;
//    }
    @Autowired
    public void setSt(Statement st) {
        this.st = st;
    }
    public int addLot(String name,double startPrice,double minPrice,String status,int auctionId) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE name=\'<empty>\'");
            int id;
            if(rs.next()){
                id = rs.getInt("id");
                st.execute("INSERT INTO lots VALUES("+id+", \'"+name+"\', "+startPrice+", "+minPrice+", \'"+status+"\', "+auctionId+")");
            }
            else{
                rs = st.executeQuery("SELECT MAX(id) FROM lots");
                rs.next();
                id = rs.getInt("MAX(id)");
                st.execute("INSERT INTO lots VALUES("+(id+1)+", \'"+name+"\', "+startPrice+", "+minPrice+", \'"+status+"\', "+auctionId+")");
            }
            return id;
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void deleteLot(int id) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE id="+id);
            if(rs.next()){
                st.execute("UPDATE lots SET name=\'<empty>\',startPrice=0,minPrice=0,status=\'<empty>\',auctionId=0 WHERE id="+id);
            }
            else throw new Exception("Lot not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void deleteLotsByAuction(int auctionId) throws Exception {
            try {
                ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE auctionId="+auctionId);
                if(rs.next()){
                    st.execute("UPDATE lots SET name=\'<empty>\',startPrice=0,minPrice=0,status=\'<empty>\',auctionId=0 WHERE auctionId="+auctionId);
                }
                else throw new Exception("No lots found");
            } catch (SQLException e) {
                throw new Exception(e.getMessage());
            }
    }
    public void setStatusByLotId(int id,String status) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE id="+id);
            if(rs.next()){
                st.execute("UPDATE lots SET status=\'"+status.toLowerCase()+"\' WHERE id="+id);
            }
            else throw new Exception("Lot not found");
        } catch (SQLException e) {
           throw new Exception(e.getMessage());
        }
    }
    public void setStatusByAuctionId(int auctionId,String status) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE auctionId="+auctionId);
            if(rs.next()){
                st.execute("UPDATE lots SET status=\'"+status.toLowerCase()+"\' WHERE auctionId="+auctionId);
            }
            else throw new Exception("Lots not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public Lot getLotById(int id) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE id="+id);
            if(rs.next()){
               return new Lot(rs.getInt("id"),rs.getString("name"),
                       rs.getDouble("startPrice"),rs.getDouble("minPrice"),
                       rs.getString("status"),rs.getInt("auctionId"));
            }
            else throw new Exception("Lot not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<Lot> getLotsByAuctionId(int auctionId) throws Exception {
        try {
            List<Lot> lotList = new ArrayList<Lot>();
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE auctionId="+auctionId);
            if(rs.next()){
                do {
                    lotList.add(new Lot(rs.getInt("id"), rs.getString("name"),
                            rs.getDouble("startPrice"), rs.getDouble("minPrice"),
                            rs.getString("status"), rs.getInt("auctionId")));
                }while (rs.next());
                return lotList;
            }
            else throw new Exception("Lot not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
}

