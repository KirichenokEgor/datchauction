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
            jdbcTemplate.queryForObject("SELECT auction_id FROM followed_auction WHERE auction_id= ? and username= ? ",new Object[]{aucId,username},Integer.class);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    @Override
    public void addFollowedAuction(String username, int aucId){
            if (!contains(username, aucId))
                jdbcTemplate.update("INSERT INTO followed_auction VALUES(?,?)", username,aucId);
    }

    @Override
    public void deleteFollowedAuction(String username, int aucId){
            if (contains(username, aucId))
                jdbcTemplate.update("DELETE FROM followed_auction WHERE auction_id= ? and username= ?",aucId,username);
    }

    @Override
    public void deleteFollowedAuctionsById(int aucId){
        jdbcTemplate.update("DELETE FROM followed_auction WHERE auction_id= ?", aucId);
    }

    @Override
    public List<FollowedAuction> getFollowedAuctionsByUser(String username){
        List<FollowedAuction> aucList = jdbcTemplate.query("SELECT * FROM followed_auction RIGHT OUTER JOIN auction" +
                " ON followed_auction.auction_id = auction.id WHERE followed_auction.username = ?",
                ps ->{ ps.setString(1,username);},
                new FollowedAuctionRowMapper());
        //todo mb check
        return aucList;
    }

    @Override
    public void deleteFollowedAuctionByUser(String username){
        jdbcTemplate.update("DELETE FROM followed_auction WHERE username= ?",username);
    }
}