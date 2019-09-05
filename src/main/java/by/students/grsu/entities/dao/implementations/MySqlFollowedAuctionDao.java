package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.auction.FollowedAuction;
import by.students.grsu.entities.dao.interfaces.FollowedAuctionDao;
import by.students.grsu.rowMappers.FollowedAuctionRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MySqlFollowedAuctionDao implements FollowedAuctionDao {
    private JdbcTemplate jdbcTemplate;

    public MySqlFollowedAuctionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean contains(String username, int aucId){
        try {
            jdbcTemplate.queryForObject("SELECT aucId FROM followedAuctions WHERE aucId=" + aucId + " and username=\'" + username + "\'", Integer.class);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    @Override
    public void addFollowedAuction(String username, int aucId){
            if (!contains(username, aucId))
                jdbcTemplate.execute("INSERT INTO followedAuctions VALUES(\'" + username + "\', " + aucId + ")");
    }

    @Override
    public void deleteFollowedAuction(String username, int aucId){
            if (contains(username, aucId))
                jdbcTemplate.execute("DELETE FROM followedAuctions WHERE aucId=" + aucId + " and username=\'" + username + "\'");
    }

    @Override
    public void deleteFollowedAuctionsById(int aucId){
        jdbcTemplate.execute("DELETE FROM followedAuctions WHERE aucId=" + aucId);
    }

    @Override
    public List<FollowedAuction> getFollowedAuctionsByUser(String username){
        List<FollowedAuction> aucList = jdbcTemplate.query("select * from followedAuctions right outer join auctions" +
                " on followedAuctions.aucId = auctions.id where followedAuctions.username =\'" +
                username + "\'", new FollowedAuctionRowMapper());
        //todo mb check
        return aucList;
    }

    @Override
    public void deleteFollowedAuctionByUser(String username){
        jdbcTemplate.execute("DELETE FROM followedAuctions WHERE username=\'" + username + "\'");
    }
}