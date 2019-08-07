package by.students.grsu.entities.dao;

import by.students.grsu.entities.services.AuctionException;
import by.students.grsu.entities.auction.Auction;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionDao {
    private Statement st;
    //private int aCount = 0;
//    public AuctionDao(Statement st) {
//        this.st = st;
//    }
    public Auction getAuctionById(int ID) throws SQLException, AuctionException {
        ResultSet rs = st.executeQuery("SELECT * FROM auctions WHERE ID="+ID);
        if(rs.next())
            return new Auction(ID,rs.getString("description"),rs.getInt("maxLots"),rs.getTime("beginTime").toLocalTime(),rs.getInt("maxDuration"));
        else throw new AuctionException("Auction not found",31);
    }
    public Auction addAuction(String description, int maxLots, LocalTime beginTime,int maxDuration) throws SQLException {
        int idCounter=1;
        ResultSet rs = st.executeQuery("SELECT ID FROM auctions ORDER BY ID");
        while(rs.next()){
            if(idCounter!=rs.getInt("ID"))break;
            idCounter++;
        }
        st.execute("INSERT INTO auctions VALUES ("+idCounter+", \'"+ description + "\', "+maxLots+", \'"+beginTime+"\', "+maxDuration +", 0)");

        return new Auction(idCounter,description,maxLots,beginTime,maxDuration);
    }
    public List<Auction> getAuctions() throws SQLException {
        ResultSet rs = st.executeQuery("SELECT * FROM auctions ORDER BY ID");
        List<Auction> auctionMap = new ArrayList<Auction>();
        while (rs.next()){
            Auction auction = new Auction(rs.getInt("ID"),rs.getString("description"),
                    rs.getInt("maxLots"),
                    rs.getTime("beginTime").toLocalTime(),rs.getInt("maxDuration"));
            if(rs.getBoolean("active"))auction.makePlaned();
            auctionMap.add(auction);
        }
        return auctionMap;
    }
   /* public void saveAuctions(List<Auction> auctions) throws SQLException {
        String sql = "";
        for(Auction auction : auctions){
            sql+="UPDATE auctions SET description=\'"+auction.getDescription()
                    +"\', tick="+auction.getTick()
                    +", maxLots="+auction.getMaxLots()
                    +", beginTime=\'"+auction.getBeginTime()
                    +"\', maxDuration="+auction.getMaxDuration()
                    +") WHERE ID="+auction.getID()+" ;\n";
        }
        st.execute(sql);
    }*/
    public void makeAutoPlanned(int ID) throws SQLException, AuctionException {
        if(st.executeQuery("SELECT * FROM auctions WHERE ID="+ID).next()){
            st.execute("UPDATE auctions SET active=1 WHERE ID="+ID);
        }
        else throw new AuctionException("Auction not found",31);
    }
    public void makeManualPlanned(int ID) throws SQLException, AuctionException {
        if(st.executeQuery("SELECT * FROM auctions WHERE ID="+ID).next()){
            st.execute("UPDATE auctions SET active=0 WHERE ID="+ID);
        }
        else throw new AuctionException("Auction not found",31);
    }
    public void deleteAuction(int ID) throws SQLException {
        if(st.executeQuery("SELECT * FROM auctions WHERE ID="+ID).next())
            st.execute("DELETE FROM auctions WHERE ID="+ID);
    }
    public Auction replace(Auction auction) throws SQLException, AuctionException {
        if(st.executeQuery("SELECT * FROM auctions where ID="+auction.getID()).next())
            st.execute("UPDATE auctions SET description=\'"+auction.getDescription()
                    +"\', maxLots="+auction.getMaxLots()
                    +", beginTime=\'"+auction.getBeginTime()
                    +"\', maxDuration="+auction.getMaxDuration()
                    +", active=0"
                    +" WHERE ID="+auction.getID());
        else throw new AuctionException("Auction not found",31);
        return auction;
    }
    @Autowired
    public void setSt(Statement st) {
        this.st = st;
    }
}
