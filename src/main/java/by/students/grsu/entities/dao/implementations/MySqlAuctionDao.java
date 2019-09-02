package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.auction.Auction;
import by.students.grsu.entities.auction.AuctionStartTime;
import by.students.grsu.entities.dao.interfaces.AuctionDao;
import by.students.grsu.entities.dao.interfaces.LotDao;
import by.students.grsu.rowMappers.AuctionRowMapper;
import by.students.grsu.rowMappers.AuctionStartTimeRowMapper;
import by.students.grsu.rowMappers.AuctionWithLotsResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MySqlAuctionDao implements AuctionDao {
    private JdbcTemplate jdbcTemplate;
    private LotDao lotDao;
    public MySqlAuctionDao(JdbcTemplate jdbcTemplate, LotDao lotDao){
            this.jdbcTemplate = jdbcTemplate;
            this.lotDao = lotDao;
    }
//    @Override
    public Auction getAuctionById(int ID){
//        try {
            //ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE ID="+ID);
            return jdbcTemplate.queryForObject("SELECT * FROM auctions WHERE ID="+ID, new AuctionRowMapper());
//            if(rs.next())
//                return new Auction(ID,rs.getString("description"),rs.getInt("maxLots"),
//                        rs.getTime("beginTime").toLocalTime(),rs.getInt("maxDuration"),rs.getString("status"),rs.getInt("currentLots"));
//            else throw new AuctionException("Auction not found",31);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new Exception(e.getMessage());
//        }
    }
    public Auction addAuction(String description, int maxLots, LocalTime beginTime,int maxDuration){
        Integer id = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM auctions ORDER BY ID", Integer.class);
        jdbcTemplate.execute("INSERT INTO auctions VALUES (" + ++id + ", \'" + description + "\', " + maxLots + ", \'" + beginTime + "\', " + maxDuration + ", \'disabled\',0)");
        return new Auction(id,description,maxLots,beginTime,maxDuration,"disabled",0);
    }
    public List<Auction> getAuctions(){
        List<Auction> aucList = jdbcTemplate.query("SELECT * FROM auctions ORDER BY ID", new AuctionRowMapper());
        //List<Auction> auctionMap = new ArrayList<Auction>();
//        while (rs.next()){
//            Auction auction = new Auction(rs.getInt("ID"),rs.getString("description"),
//                    rs.getInt("maxLots"), rs.getTime("beginTime").toLocalTime(),
//                    rs.getInt("maxDuration"),rs.getString("status"),
//                    rs.getInt("currentLots"));
//            auctionMap.add(auction);
//        }
        return aucList;
    }
    public void deleteAuction(int ID){
//        if (statement.executeQuery("SELECT * FROM auctions WHERE ID=" + ID).next())
//            statement.execute("DELETE FROM auctions WHERE ID=" + ID);
        jdbcTemplate.execute("DELETE FROM auctions WHERE ID=" + ID);
    }
    public void addLotToAuction(int ID,boolean delete) throws Exception {
//            try {
//                ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE ID=" + ID);
//                if (rs.next()) {
//                    statement.execute("UPDATE auctions SET currentLots=" + (rs.getInt("currentLots") + (!delete ? 1 : -1)) + " WHERE ID=" + ID);
//                } else throw new Exception("Auction not found");
//            } catch (SQLException e) {
//                throw new Exception(e.getMessage());
//            }
            Auction auction = jdbcTemplate.queryForObject("SELECT * FROM auctions WHERE ID=" + ID, new AuctionRowMapper());
            jdbcTemplate.update("UPDATE auctions SET currentLots=" + (auction.getCurrentLots() + (!delete ? 1 : -1)) + " WHERE ID=" + ID);
    }
    public void setStatus(int ID,String newStatus) throws Exception {
//            try {
                newStatus = newStatus.toLowerCase();
                if (!newStatus.equals("disabled") && !newStatus.equals("active") && !newStatus.equals("planned") && !newStatus.equals("done"))
                    throw new Exception("Wrong status word");
                jdbcTemplate.update("UPDATE auctions SET status=\'" + newStatus + "\',currentLots=0 WHERE ID=" + ID);
//                ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE ID=" + ID);
//                //System.out.println("111111111111111");
//                if (rs.next()) {
//                    //System.out.println("2222222222222222");
//                    statement.execute("UPDATE auctions SET status=\'" + newStatus + "\',currentLots=0 WHERE ID=" + ID);
//                    //System.out.println("3333333333333333");
//                } else throw new Exception("Auction not found");
//            } catch (SQLException e) {
//                throw new Exception(e.getMessage());
//            }
    }
    public List<Auction> getAuctionsByStatus(String status) throws Exception {
        status=status.toLowerCase();
        if(!status.equals("disabled") && !status.equals("active") && !status.equals("planned") && !status.equals("done"))
            throw new Exception("Wrong status word");
//        try {
//
//            List<Auction> list = new ArrayList<Auction>();
//            ResultSet rs = statement.executeQuery("SELECT * FROM auctions WHERE status = \'"+status+"\'");
//            while(rs.next()){
//                list.add(new Auction(rs.getInt("ID"),rs.getString("description"),
//                        rs.getInt("maxLots"),rs.getTime("beginTime").toLocalTime(),rs.getInt("maxDuration"),status,rs.getInt("currentLots")));
//                //Auction(int id,String description,int maxLots,LocalTime beginTime, int maxDuration)
//            }
//            return list;
//        } catch (SQLException e) {
//            throw new Exception(e.getMessage());
//        }
        List<Auction> aucList = jdbcTemplate.query("SELECT * FROM auctions WHERE status = \'"+status+"\'", new AuctionRowMapper());
        return aucList;
    }
    public Auction getAuctionWithLots(int id){
//        try {
//            List<Lot> auctionLots = lotDao.getLotsByAuctionId(id);
//            ResultSet rs = statement.executeQuery("SELECT * FROM auctions where ID="+id);
//            if(rs.next())
//                return new Auction(rs.getInt("ID"),rs.getString("description"),
//                        rs.getInt("maxLots"),rs.getTime("beginTime").toLocalTime(),
//                        rs.getInt("maxDuration"),rs.getString("status"),auctionLots);
//            else throw new Exception("Auction not found");
//        } catch (SQLException e) {
//            throw new Exception(e.getMessage() + "AuctionDao");
//        }
        Auction auction = jdbcTemplate.query("select * from auctions left outer join lots on auctions.ID = lots.auctionId left outer join items on lots.id = items.lotid where auctions.id = "+id, new AuctionWithLotsResultSetExtractor());
        return auction;
    }
    public Auction replace(Auction auction){
//        synchronized (auction) {
//            if (statement.executeQuery("SELECT * FROM auctions where ID=" + auction.getID()).next())
//                statement.execute("UPDATE auctions SET description=\'" + auction.getDescription()
//                        + "\', maxLots=" + auction.getMaxLots()
//                        + ", beginTime=\'" + auction.getBeginTime()
//                        + "\', maxDuration=" + auction.getMaxDuration()
//                        + " WHERE ID=" + auction.getID());
//            else throw new AuctionException("Auction not found", 31);
//        }
        jdbcTemplate.update("UPDATE auctions SET description=\'" + auction.getDescription()
                        + "\', maxLots=" + auction.getMaxLots()
                        + ", beginTime=\'" + auction.getBeginTime()
                        + "\', maxDuration=" + auction.getMaxDuration()
                        + " WHERE ID=" + auction.getID());
        return auction;//really?
    }
    public Queue<AuctionStartTime> getAuctionsQueue(){
//        try {
            List<AuctionStartTime> list = jdbcTemplate.query("SELECT ID,beginTime FROM auctions WHERE status=\'planned\' OR status=\'active\' ORDER BY beginTime", new AuctionStartTimeRowMapper());
            Queue<AuctionStartTime> queue = new LinkedList<AuctionStartTime>(list);
            return  queue;
//            ResultSet rs = statement.executeQuery("SELECT ID,beginTime FROM auctions WHERE status=\'planned\' OR status=\'active\' ORDER BY beginTime");
//            while(rs.next()){
//                queue.add(new AuctionStartTime(rs.getInt("ID"),rs.getTime("beginTime").toLocalTime()));
//            }
//            return queue;
//        } catch (SQLException e) {
//            throw new Exception(e.getMessage());
//        }
    }
    public void updateDoneAuctions(){
//        synchronized (new Integer(3)) {
//            try {
//                statement.execute("UPDATE auctions SET status=\'planned\' WHERE status=\'done\'");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        jdbcTemplate.update("UPDATE auctions SET status=\'planned\' WHERE status=\'done\'");
    }
}