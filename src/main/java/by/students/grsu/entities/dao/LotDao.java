package by.students.grsu.entities.dao;

import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotDao {
    private Statement st;
    private ItemDao itemDao;
    public LotDao(Statement st,ItemDao itemDao){
        this.st = st;
        this.itemDao=itemDao;
    }
    public int addLot(String name,double startPrice,double minPrice,String status,int auctionId) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE name=\'<empty>\'");
            int id;
            if(rs.next()){
                id = rs.getInt("id");
                st.execute("INSERT INTO lots VALUES("+id+", \'"+name+"\', "+startPrice+", "+minPrice+", \'"+status.toLowerCase()+"\', "+auctionId+")");
            }
            else{
                rs = st.executeQuery("SELECT MAX(id) FROM lots");
                rs.next();
                id = rs.getInt("MAX(id)") + 1;
                st.execute("INSERT INTO lots VALUES("+(id)+", \'"+name+"\', "+startPrice+", "+minPrice+", \'"+status.toLowerCase()+"\', "+auctionId+")");
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
            List<Item> items = itemDao.getItemsByLot(auctionId);
            List<Lot> lotList = new ArrayList<Lot>();
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE auctionId="+auctionId);
           // if(rs.next())
            while (rs.next()){

                    lotList.add(new Lot(rs.getInt("id"), rs.getString("name"),
                            rs.getDouble("startPrice"), rs.getDouble("minPrice"),
                            rs.getString("status"), rs.getInt("auctionId"),items));
                }
            //    else throw new Exception("Lots not found");
                return lotList;
        } catch (SQLException e) {
            throw new Exception(e.getMessage() + "Ldao");
        }
    }
    public Lot getLotWithItems(int auctionId) throws Exception {
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM lots WHERE auctionId="+auctionId);
            if(rs.next())
                return new Lot(rs.getInt("ID"),rs.getString("name"),
                        rs.getDouble("startPrice"),rs.getDouble("minPrice"),
                        rs.getString("status"),rs.getInt("auctionId"),itemDao.getItemsByLot(auctionId));
            else throw new Exception("Lot not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
}

