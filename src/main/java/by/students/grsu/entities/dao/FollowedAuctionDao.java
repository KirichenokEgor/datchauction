package by.students.grsu.entities.dao;

import by.students.grsu.entities.auction.FollowedAuction;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FollowedAuctionDao {
    private Statement statement;
    private AuctionDao auctionDao;

    public FollowedAuctionDao(Statement statement) {
        try {
            statement.execute("use datchauction");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.statement = statement;
    }

    @Autowired
    public void setAuctionDao(AuctionDao auctionDao){
        this.auctionDao = auctionDao;
    }

    public boolean contains(String username, int aucId) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE aucId="+aucId+" and username=\'"+username+"\'");
        if(rs.next()) return true;
        return false;
    }

    public void addFollowedAuction(String username, int aucId) throws Exception {
        if(!contains(username, aucId))
            statement.execute("INSERT INTO followedAuctions VALUES(\'"+username+"\', "+aucId+")");
    }
    public void deleteFollowedAuction(String username, int aucId) throws Exception {
        if(contains(username, aucId))
            statement.execute("DELETE FROM followedAuctions WHERE aucId="+aucId+"and username=\'"+username+"\'");
    }
    public void deleteFollowedAuctionsById(int aucId) throws Exception {
        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE aucId="+aucId);
        if(rs.next())statement.execute("DELETE FROM followedAuctions WHERE aucId="+aucId);
    }
    public List<FollowedAuction> getFollowedAuctionsByUser(String username) throws SQLException {
        List<FollowedAuction> aucList = new ArrayList<FollowedAuction>();
        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE username=\'"+username+"\'");
        try {
            while (rs.next())
                aucList.add(new FollowedAuction(auctionDao.getAuctionById(rs.getInt("aucId")), true));//new Auction(rs.getNString("username"),rs.getInt("aucId")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return aucList;
    }
//    public Auction getFollowedAuctionById(int aucId) throws Exception {
//        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE aucId="+aucId);
//        if(rs.next())return auctionDao.getAuctionById(rs.getInt("aucId"));
//        else throw new Exception("FollowedAuction not found");
//    }
    public void deleteFollowedAuctionByUser(String username) throws Exception {
        ResultSet rs = statement.executeQuery("SELECT * FROM followedAuctions WHERE username=\'"+username+"\'");
        if(rs.next())statement.execute("DELETE FROM followedAuctions WHERE username=\'"+username+"\'");
    }
}