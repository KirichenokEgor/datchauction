package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.dao.interfaces.LotDao;
import by.students.grsu.entities.item.Item;
import by.students.grsu.entities.lot.Lot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.ArrayList;
import java.util.List;

public class MySqlLotDao implements LotDao {
    private JdbcTemplate template;
    private ResultSetExtractor<Lot> lotWithoutItemsExtractor = rs ->{
        return rs.next() ? new Lot(rs.getInt("id"), rs.getString("lot_name"), rs.getDouble("start_price"), rs.getDouble("min_price"), rs.getString("status"), rs.getInt("auction_id")) : null;
    };
    private ResultSetExtractor<List<Lot>> lotListWithItemsExtractor = rs -> {
        List<Lot> lots = new ArrayList<>();
        int lotId = 0;
        Lot newLot = null;
        ArrayList items;
        for(items = null; rs.next(); items.add(new Item(rs.getInt("item.id"), rs.getString("name"), rs.getString("description"), rs.getString("owner"), rs.getInt("lot_id")))) {
            if (rs.getInt("lot.id") != lotId) {
                if (newLot != null) {
                    newLot.setItems(items);
                    lots.add(newLot);
                }

                newLot = new Lot(rs.getInt("lot.id"), rs.getString("lot_name"), rs.getDouble("start_price"), rs.getDouble("min_price"), rs.getString("status"), rs.getInt("auction_id"));
                items = new ArrayList();
                lotId = rs.getInt("lot.id");
            }
        }
        if (newLot != null) {
            newLot.setItems(items);
            lots.add(newLot);
        }
        return lots;
    };
    private ResultSetExtractor<List<Lot>> lotListWithoutItemsExtractor = rs -> {
        ArrayList lots = new ArrayList();

        while(rs.next()) {
            lots.add(new Lot(rs.getInt("lot.id"), rs.getString("lot_name"), rs.getDouble("start_price"), rs.getDouble("min_price"), rs.getString("status"), rs.getInt("auction_id")));
        }

        return lots;
    };
    private ResultSetExtractor<List<Integer>> lotsIdList = rs -> {
        ArrayList lotsIndexes = new ArrayList();

        while(rs.next()) {
            lotsIndexes.add(rs.getInt("id"));
        }

        return lotsIndexes;
    };

    public MySqlLotDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public synchronized int addLot(String name, double startPrice, double minPrice, String status, int auctionId){
//        template.update("INSERT INTO lot VALUES(NULL, '" + name + "', " + startPrice + ", " + minPrice + ", '" + status.toLowerCase() + "', " + auctionId + ")");
        template.update("INSERT INTO lot VALUES(NULL,? ,? ,? ,? ,?)",name,startPrice,minPrice,status.toLowerCase(),auctionId);
//        template.update("UPDATE auction SET current_lots = current_lots + 1 WHERE ID=" + auction_id);
        return template.queryForObject("SELECT MAX(id) FROM lot", Integer.class);
    }

    @Override
    public /*synchronized*/ void deleteLot(int id){
//        int auctionId = template.queryForObject("select auction_id from lot where id=" + id, Integer.class);
//        template.execute("DELETE FROM lot WHERE id=" + id);
        template.update("DELETE FROM lot WHERE id= ?", id);
//        template.update("UPDATE auctions SET current_lots = current_lots - 1 WHERE ID=" + auction_id);
    }

//    @Override
//    public void deleteLotsByAuction(int auctionId) {
//        template.update("UPDATE lots SET lot_name='<empty>',start_price=0,min_price=0,status='<empty>',auction_id=0 WHERE auction_id=" + auctionId);
//    }

    @Override
    public void setStatusByLotId(int id, String status) throws Exception {
//        if (template.update("UPDATE lot SET status='" + status + "', auction_id=0 WHERE id=" + id) == 0) {
        if (template.update("UPDATE lot SET status= ?, auction_id=0 WHERE id= ?",status, id) == 0) {
            throw new Exception("Lot not found");
        }
    }

    @Override
    public void setEndByAuctionId(int auctionId) {
//        template.update("UPDATE lot SET status='end' WHERE auction_id=" + auctionId + " and status='registered'");
        template.update("UPDATE lot SET status='end' WHERE auction_id= ? and status='registered'",auctionId);
    }

    @Override
    public Lot getLotById(int id) throws Exception {
//        Lot lot = template.query("SELECT * FROM lot WHERE id=" + id, lotWithoutItemsExtractor);
        Lot lot = template.query("SELECT * FROM lot WHERE id= ?", new Object[]{id}, lotWithoutItemsExtractor);
        if (lot == null) {
            throw new Exception("Lot not found");
        } else {
            return lot;
        }
    }

    @Override
    public List<Lot> getLotsByAuctionId(int auctionId) {
//        return template.query("SELECT * FROM lot LEFT OUTER JOIN item ON lot.id=item.lot_id WHERE auction_id=" + auctionId, lotListWithItemsExtractor);
        return template.query("SELECT * FROM lot LEFT OUTER JOIN item ON lot.id=item.lot_id WHERE auction_id= ?", ps -> {ps.setInt(1,auctionId);}, lotListWithItemsExtractor);
    }

    @Override
    public List<Lot> getAllLots() {
        return template.query("SELECT * FROM lot ORDER BY id", lotListWithoutItemsExtractor);
    }

    @Override
    public List<Lot> getNotSoldLots() {
        return template.query("SELECT * FROM lot WHERE status != 'sold' ORDER BY id", lotListWithoutItemsExtractor);
    }

    @Override
    public List<Lot> getLotsBySearch(String substr) {
        //return template.query("SELECT * FROM lot LEFT OUTER JOIN items ON lot.id=items.lotId WHERE MATCH (lot_name) AGAINST ('" + substr + "') ORDER BY id", lotListWithItemsExtractor);
//        return template.query("SELECT * FROM lot WHERE MATCH (lot_name) AGAINST ('" + substr + "') ORDER BY id", lotListWithoutItemsExtractor);
        return template.query("SELECT * FROM lot WHERE MATCH (lot_name) AGAINST ( ? ) ORDER BY id",ps -> {ps.setString(1,substr);}, lotListWithoutItemsExtractor);
    }

    @Override
    public List<Integer> deleteEndedLots() {
        List<Integer> lotsIndexes = template.query("SELECT id FROM lot WHERE status='end'", lotsIdList);
        template.execute("DELETE FROM lot WHERE status='end'");
        return lotsIndexes;
    }

    @Override
    public List<Lot> getLotsBySeller(String username) {
//        return template.query("select * from lot inner join sold_lot on lot.id = sold_lot.lot_id where seller = '" + username + "'", lotListWithoutItemsExtractor);
        return template.query("select * from lot inner join sold_lot on lot.id = sold_lot.lot_id where seller = ?",ps -> {ps.setString(1,username);}, lotListWithoutItemsExtractor);
    }

//    @Override
//    public List<Lot> getRegisteredLots() {
//        return template.query("SELECT * FROM lots LEFT OUTER JOIN items ON lots.id=items.lotId WHERE status='registered'", lotListWithItemsExtractor);
//    }
}