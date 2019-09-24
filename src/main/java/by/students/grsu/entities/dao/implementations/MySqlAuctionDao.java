package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStartTime;
import by.students.grsu.entities.dao.interfaces.AuctionDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MySqlAuctionDao implements AuctionDao {
    private JdbcTemplate template;
    private ResultSetExtractor<Auction> auctionWithLotsExtractor = (rs) -> {
        List<Lot> lots = new ArrayList<>();
        Auction auction = null;
        int lotId = 0;
        Lot newLot = null;
        ArrayList items = null;
        while(rs.next()) {
            if (rs.getInt("lot.id") != lotId) {
                if (newLot != null) {
                    newLot.setItems(items);
                    lots.add(newLot);
                }
                try {
                    newLot = new Lot(rs.getInt("lot.id"), rs.getString("lot_name"),
                            rs.getDouble("start_price"), rs.getDouble("min_price"),
                            rs.getString("lot.status"), rs.getInt("auction_id"));
                } catch (NullPointerException e) {
                    auction = new Auction(rs.getInt("auction.id"),
                            rs.getString("auction.description"),
                            rs.getInt("max_lots"), rs.getTime("begin_time").toLocalTime(),
                            rs.getInt("max_duration"), rs.getString("auction.status"), lots);
                    break;
                }

                items = new ArrayList();
                lotId = rs.getInt("lot.id");
            }

            try {
                items.add(new Item(rs.getInt("item.id"), rs.getString("name"), rs.getString("item.description"), rs.getString("owner"), rs.getInt("lot_id")));
            } catch (NullPointerException e) {
                auction = new Auction(rs.getInt("auction.id"), rs.getString("auction.description"), rs.getInt("max_lots"), rs.getTime("begin_time").toLocalTime(), rs.getInt("max_duration"), rs.getString("auction.status"), lots);
                break;
            }

            if (auction == null) {
                auction = new Auction(rs.getInt("auction.id"), rs.getString("auction.description"), rs.getInt("max_lots"), rs.getTime("begin_time").toLocalTime(), rs.getInt("max_duration"), rs.getString("auction.status"), lots);
            }
        }

        if (newLot != null) {
            newLot.setItems(items);
            lots.add(newLot);
        }

        return auction;
    };
    private ResultSetExtractor<List<Auction>> auctionListExtractor = (rs) -> {
        List<Auction> auctionList = new ArrayList<>();
        while(rs.next()) {
            Auction auction = new Auction(rs.getInt("id"), rs.getString("description"),
                    rs.getInt("max_lots"), rs.getTime("begin_time").toLocalTime(),
                    rs.getInt("max_duration"), rs.getString("status"),
                    rs.getInt("current_lots"));
            auctionList.add(auction);
        }
        return auctionList;
    };
    private ResultSetExtractor<Auction> auctionExtractor = (rs) -> {
        return rs.next() ? new Auction(rs.getInt("id"), rs.getString("description"), rs.getInt("max_lots"), rs.getTime("begin_time").toLocalTime(), rs.getInt("max_duration"), rs.getString("status"), rs.getInt("current_lots")) : null;
    };
    private ResultSetExtractor<Queue<AuctionStartTime>> auctionQueueExtractor = (rs) -> {
        LinkedList queue = new LinkedList();
        while(rs.next()) {
            queue.add(new AuctionStartTime(rs.getInt("id"), rs.getTime("begin_time").toLocalTime()));
        }
        return queue;
    };

    public MySqlAuctionDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Auction getAuctionById(int id) throws Exception {
        Auction auction = template.query("SELECT * FROM auction WHERE id= ? ",ps -> {ps.setInt(1,id);}, auctionExtractor);
        if (auction == null) {
            throw new Exception("Auction not found");
        } else {
            return auction;
        }
    }

    @Override
    public synchronized int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration){
        template.update("INSERT INTO auction VALUES (NULL, ?, ?, ?, ?, \'disabled\', 0)",description,maxLots,beginTime,maxDuration);
            int id = template.queryForObject("SELECT MAX(id) FROM auction", Integer.class);
        return id;
    }

    @Override
    public List<Auction> getAuctions(){
        return template.query("SELECT * FROM auction ORDER BY id", auctionListExtractor);
    }

    @Override
    public void deleteAuction(int id){
        template.update("DELETE FROM auction WHERE id= ?",id);
    }

    @Override
    public void addLotToAuction(int id, boolean delete){
        template.update("UPDATE auction SET current_lots = current_lots" + (delete ? "-" : "+") + "1 WHERE id= ?",id);
    }

    @Override
    public void setStatus(int id, String newStatus) throws Exception {
        newStatus = newStatus.toLowerCase();
        if (!newStatus.equals("disabled") && !newStatus.equals("active") && !newStatus.equals("planned") && !newStatus.equals("done")) {
            throw new Exception("Wrong status word");
        } else if (template.update("UPDATE auction SET status= ? ,current_lots=0 WHERE id= ?",newStatus,id) == 0) {
            throw new Exception("Auction not found");
        }
    }

    @Override
    public List<Auction> getAuctionsByStatus(String status) throws Exception {
        String finalStatus = status.toLowerCase();
        if (!status.equals("disabled") && !status.equals("active") && !status.equals("planned") && !status.equals("done")) {
            throw new Exception("Wrong status word");
        } else {
             return template.query("SELECT * FROM auction WHERE status = ?", ps -> ps.setString(1,finalStatus), this.auctionListExtractor);
        }
    }

    @Override
    public Auction getAuctionWithLots(int id) throws Exception {
        Auction auction = (Auction)this.template.query("SELECT * FROM auction LEFT OUTER JOIN lot " +
                "ON auction.id=lot.auction_id LEFT OUTER JOIN item ON lot.id=item.lot_id " +
                "WHERE auction.id= ? ORDER BY lot.id",ps -> {ps.setInt(1,id);}, this.auctionWithLotsExtractor);
        if (auction == null) {
            throw new Exception("Auction not found");
        } else {
            return auction;
        }
    }

    @Override
    public Queue<AuctionStartTime> getAuctionsQueue(){
        return template.query("SELECT id,begin_time FROM auction WHERE status='planned' OR status='active' ORDER BY begin_time", this.auctionQueueExtractor);
    }

    @Override
    public void updateDoneAuctions() {
        this.template.execute("UPDATE auction SET status='planned' WHERE status='done'");
    }
}
