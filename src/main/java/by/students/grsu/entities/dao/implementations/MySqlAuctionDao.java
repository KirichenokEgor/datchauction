package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStartTime;
import by.students.grsu.entities.dao.interfaces.AuctionDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

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
            if (rs.getInt("lots.id") != lotId) {
                if (newLot != null) {
                    newLot.setItems(items);
                    lots.add(newLot);
                }
                try {
                    newLot = new Lot(rs.getInt("lots.id"), rs.getString("lotName"),
                            rs.getDouble("startPrice"), rs.getDouble("minPrice"),
                            rs.getString("lots.status"), rs.getInt("auctionId"));
                } catch (NullPointerException e) {
                    auction = new Auction(rs.getInt("auctions.id"),
                            rs.getString("auctions.description"),
                            rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(),
                            rs.getInt("maxDuration"), rs.getString("auctions.status"), lots);
                    break;
                }

                items = new ArrayList();
                lotId = rs.getInt("lots.id");
            }

            try {
                items.add(new Item(rs.getInt("items.id"), rs.getString("name"), rs.getString("items.description"), rs.getString("owner"), rs.getInt("lotId")));
            } catch (NullPointerException e) {
                auction = new Auction(rs.getInt("auctions.id"), rs.getString("auctions.description"), rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(), rs.getInt("maxDuration"), rs.getString("auctions.status"), lots);
                break;
            }

            if (auction == null) {
                auction = new Auction(rs.getInt("auctions.id"), rs.getString("auctions.description"), rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(), rs.getInt("maxDuration"), rs.getString("auctions.status"), lots);
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
            Auction auction = new Auction(rs.getInt("ID"), rs.getString("description"),
                    rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(),
                    rs.getInt("maxDuration"), rs.getString("status"),
                    rs.getInt("currentLots"));
            auctionList.add(auction);
        }
        return auctionList;
    };
    private ResultSetExtractor<Auction> auctionExtractor = (rs) -> {
        return rs.next() ? new Auction(rs.getInt("ID"), rs.getString("description"), rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(), rs.getInt("maxDuration"), rs.getString("status"), rs.getInt("currentLots")) : null;
    };
    private ResultSetExtractor<Queue<AuctionStartTime>> auctionQueueExtractor = (rs) -> {
        LinkedList queue = new LinkedList();
        while(rs.next()) {
            queue.add(new AuctionStartTime(rs.getInt("ID"), rs.getTime("beginTime").toLocalTime()));
        }
        return queue;
    };

    public MySqlAuctionDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Auction getAuctionById(int ID) throws Exception {
        Auction auction = (Auction)this.template.query("SELECT * FROM auctions WHERE ID=" + ID, auctionExtractor);
        if (auction == null) {
            throw new Exception("Auction not found");
        } else {
            return auction;
        }
    }

    @Override
    public synchronized int addAuction(String description, int maxLots, LocalTime beginTime, int maxDuration){
            template.update("INSERT INTO auctions VALUES (NULL, \'" + description + "\', "
                + maxLots + ", \'" + beginTime + "\', " + maxDuration
                + ", \'disabled\', 0)");
            int id = template.queryForObject("SELECT MAX(ID) FROM auctions", Integer.class);
        return id;
    }

    @Override
    public List<Auction> getAuctions(){
        return template.query("SELECT * FROM auctions ORDER BY ID", auctionListExtractor);
    }

    @Override
    public void deleteAuction(int ID){
        template.update("DELETE FROM auctions WHERE ID=" + ID);
    }

    @Override
    public void addLotToAuction(int ID, boolean delete){
        template.update("UPDATE auctions SET currentLots = currentLots" + (delete ? "-" : "+") + "1 WHERE ID=" + ID);
    }

    @Override
    public void setStatus(int ID, String newStatus) throws Exception {
        newStatus = newStatus.toLowerCase();
        if (!newStatus.equals("disabled") && !newStatus.equals("active") && !newStatus.equals("planned") && !newStatus.equals("done")) {
            throw new Exception("Wrong status word");
        } else if (template.update("UPDATE auctions SET status='" + newStatus + "',currentLots=0 WHERE ID=" + ID) == 0) {
            throw new Exception("Auction not found");
        }
    }

    @Override
    public List<Auction> getAuctionsByStatus(String status) throws Exception {
        status = status.toLowerCase();
        if (!status.equals("disabled") && !status.equals("active") && !status.equals("planned") && !status.equals("done")) {
            throw new Exception("Wrong status word");
        } else {
            return template.query("SELECT * FROM auctions WHERE status = '" + status + "'", this.auctionListExtractor);
        }
    }

    @Override
    public Auction getAuctionWithLots(int id) throws Exception {
        Auction auction = (Auction)this.template.query("SELECT * FROM auctions LEFT OUTER JOIN lots ON auctions.id=lots.auctionId LEFT OUTER JOIN items ON lots.id=items.lotId WHERE auctions.id=" + id + " ORDER BY lots.id", this.auctionWithLotsExtractor);
        if (auction == null) {
            throw new Exception("Auction not found");
        } else {
            return auction;
        }
    }

    @Override
    public Queue<AuctionStartTime> getAuctionsQueue(){
        return template.query("SELECT ID,beginTime FROM auctions WHERE status='planned' OR status='active' ORDER BY beginTime", this.auctionQueueExtractor);
    }

    @Override
    public void updateDoneAuctions() {
        this.template.execute("UPDATE auctions SET status='planned' WHERE status='done'");
    }
}
