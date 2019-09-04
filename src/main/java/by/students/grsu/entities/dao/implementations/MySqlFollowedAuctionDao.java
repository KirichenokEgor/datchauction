package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.auction.FollowedAuction;
import by.students.grsu.entities.dao.interfaces.FollowedAuctionDao;
import by.students.grsu.rowMappers.FollowedAuctionRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class MySqlFollowedAuctionDao implements FollowedAuctionDao {
    private JdbcTemplate jdbcTemplate;

    public MySqlFollowedAuctionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean contains(String username, int aucId){
        try {
            jdbcTemplate.queryForObject("SELECT aucId FROM followedAuctions WHERE aucId=" + aucId + " and username=\'" + username + "\'", Integer.class);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public void addFollowedAuction(String username, int aucId){
            if (!contains(username, aucId))
                //statement.execute("INSERT INTO followedAuctions VALUES(\'" + username + "\', " + aucId + ")");
                jdbcTemplate.execute("INSERT INTO followedAuctions VALUES(\'" + username + "\', " + aucId + ")");
    }
    public void deleteFollowedAuction(String username, int aucId){
            if (contains(username, aucId))
//                statement.execute("DELETE FROM followedAuctions WHERE aucId=" + aucId + " and username=\'" + username + "\'");
                jdbcTemplate.execute("DELETE FROM followedAuctions WHERE aucId=" + aucId + " and username=\'" + username + "\'");
    }
    public void deleteFollowedAuctionsById(int aucId){
//            ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE aucId=" + aucId);
//            if (rs.next()) statement.execute("DELETE FROM followedAuctions WHERE aucId=" + aucId);
        //todo mb check for null is needed???
        jdbcTemplate.execute("DELETE FROM followedAuctions WHERE aucId=" + aucId);
    }
    public List<FollowedAuction> getFollowedAuctionsByUser(String username) throws SQLException {
//        List<FollowedAuction> aucList = new ArrayList<FollowedAuction>();
//        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE username=\'"+username+"\'");
//        try {
//            while (rs.next())
//                aucList.add(new FollowedAuction(auctionDao.getAuctionById(rs.getInt("aucId")), true));//new Auction(rs.getNString("username"),rs.getInt("aucId")));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        List<FollowedAuction> aucList = jdbcTemplate.query("select * from followedAuctions right outer join auctions" +
                " on followedAuctions.aucId = auctions.id where followedAuctions.username =\'" +
                username + "\'", new FollowedAuctionRowMapper());
        //todo mb check
        return aucList;
    }
//    public Auction getFollowedAuctionById(int aucId) throws Exception {
//        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE aucId="+aucId);
//        if(rs.next())return auctionDao.getAuctionById(rs.getInt("aucId"));
//        else throw new Exception("FollowedAuction not found");
//    }
    public void deleteFollowedAuctionByUser(String username) throws Exception {
//        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE username=\'" + username + "\'");
//        if (rs.next()) statement.execute("DELETE FROM followedAuctions WHERE username=\'" + username + "\'");
        jdbcTemplate.execute("DELETE FROM followedAuctions WHERE username=\'" + username + "\'");
    }
}