package by.students.grsu.entities.dao;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStartTime;
import by.students.grsu.entities.lot.Lot;
import by.students.grsu.entities.services.AuctionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AuctionDao {
    private Statement statement;
    private LotDao lotDao;
    public AuctionDao(Statement statement,LotDao lotDao){
        try {
            statement.execute("use datchauction");
            this.statement = statement;
            this.lotDao = lotDao;
        }catch (Exception e){
            System.out.println("AuctionDao: Error: " + e.getMessage());
        }
    }
    public Auction getAuctionById(int ID) throws Exception {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE ID="+ID);
            if(rs.next())
                return new Auction(ID,rs.getString("description"),rs.getInt("maxLots"),
                        rs.getTime("beginTime").toLocalTime(),rs.getInt("maxDuration"),rs.getString("status"),rs.getInt("currentLots"));
            else throw new AuctionException("Auction not found",31);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    public Auction addAuction(String description, int maxLots, LocalTime beginTime,int maxDuration) throws SQLException {
        int idCounter=1;
        ResultSet rs = statement.executeQuery("SELECT ID FROM auctions ORDER BY ID");
        while(rs.next()){
            if(idCounter!=rs.getInt("ID"))break;
            idCounter++;
        }
        statement.execute("INSERT INTO auctions VALUES ("+idCounter+", \'"+ description + "\', "+maxLots+", \'"+beginTime+"\', "+maxDuration +", \'disabled\',0)");

        return new Auction(idCounter,description,maxLots,beginTime,maxDuration,"disabled",0);
    }
    public List<Auction> getAuctions() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM auctions ORDER BY ID");
        List<Auction> auctionMap = new ArrayList<Auction>();
        while (rs.next()){
            Auction auction = new Auction(rs.getInt("ID"),rs.getString("description"),
                    rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(),
                    rs.getInt("maxDuration"),rs.getString("status"),
                    rs.getInt("currentLots"));
            auctionMap.add(auction);
        }
        return auctionMap;
    }
    public void deleteAuction(int ID) throws SQLException {
        if(statement.executeQuery("SELECT * FROM auctions WHERE ID="+ID).next())
            statement.execute("DELETE FROM auctions WHERE ID="+ID);
    }
    public void addLotToAuction(int ID,boolean delete) throws Exception {
        try{
            ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE ID="+ID);
            if(rs.next()) {
                statement.execute("UPDATE auctions SET currentLots=" + (rs.getInt("currentLots")+(!delete?1:-1)) + " WHERE ID=" + ID);
            }
            else throw new Exception("Auction not found");
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }
    public void setStatus(int ID,String newStatus) throws Exception {
        try{
            newStatus=newStatus.toLowerCase();
            if(!newStatus.equals("disabled") && !newStatus.equals("active") && !newStatus.equals("planned") && !newStatus.equals("done"))
                throw new Exception("Wrong status word");
            ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE ID="+ID);
            if(rs.next()) {
                statement.execute("UPDATE auctions SET status=\'" + newStatus + "\',currentLots=0 WHERE ID=" + ID);
            }
            else throw new Exception("Auction not found");
        }catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }
    public List<Auction> getAuctionsByStatus(String status) throws Exception {
        status=status.toLowerCase();
        if(!status.equals("disabled") && !status.equals("active") && !status.equals("planned") && !status.equals("done"))
            throw new Exception("Wrong status word");
        try {

            List<Auction> list = new ArrayList<Auction>();
            ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE status = \'"+status+"\'");
            while(rs.next()){
                list.add(new Auction(rs.getInt("ID"),rs.getString("description"),
                        rs.getInt("maxLots"),rs.getTime("beginTime").toLocalTime(),rs.getInt("maxDuration"),status,rs.getInt("currentLots")));
                //Auction(int id,String description,int maxLots,LocalTime beginTime, int maxDuration)
            }
            return list;
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public Auction getAuctionWithLots(int id) throws Exception {
        try {
            List<Lot> auctionLots = lotDao.getLotsByAuctionId(id);
            ResultSet rs = statement.executeQuery("SELECT * FROM auctions where ID="+id);
            if(rs.next())
                return new Auction(rs.getInt("ID"),rs.getString("description"),
                        rs.getInt("maxLots"),rs.getTime("beginTime").toLocalTime(),
                        rs.getInt("maxDuration"),rs.getString("status"),auctionLots);
            else throw new Exception("Auction not found");
        } catch (SQLException e) {
            throw new Exception(e.getMessage() + "AuctionDao");
        }
    }
    public Auction replace(Auction auction) throws SQLException, AuctionException {
        if(statement.executeQuery("SELECT * FROM auctions where ID="+auction.getID()).next())
            statement.execute("UPDATE auctions SET description=\'"+auction.getDescription()
                    +"\', maxLots="+auction.getMaxLots()
                    +", beginTime=\'"+auction.getBeginTime()
                    +"\', maxDuration="+auction.getMaxDuration()
                    +" WHERE ID="+auction.getID());
        else throw new AuctionException("Auction not found",31);
        return auction;
    }
    public Queue<AuctionStartTime> getAuctionsQueue() throws Exception {
        try {
            Queue<AuctionStartTime> queue = new LinkedList<AuctionStartTime>();
            ResultSet rs = statement.executeQuery("SELECT ID,beginTime FROM auctions WHERE status=\'planned\' OR status=\'active\' ORDER BY beginTime");
            while(rs.next()){
                queue.add(new AuctionStartTime(rs.getInt("ID"),rs.getTime("beginTime").toLocalTime()));
            }
            return queue;
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void updateDoneAuctions(){
        try {
            statement.execute("UPDATE auctions SET status=\'planned\' WHERE status=\'done\'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}