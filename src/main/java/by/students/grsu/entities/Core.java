package by.students.grsu.entities;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class Core {
    private Map<Integer,Auction> auctions;
    private UsersManager UM;
    private ItemsManager IM;
    //private AuctionsManager AM;
    //private Logger logger;
    private Core() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:mysql://localhost","datchDBManager","ineedyourbase");
            Statement st = connection.createStatement();
            st.execute("use datchauction");
            IM = new ItemsManager(st);
            UM = new UsersManager(st);
            //      AM = new AuctionsManager(st);
            //     auctions = AM.getAuctions();
        } catch (SQLException e) {
            System.out.println("Initializing failed.");
            e.printStackTrace();
        }
    }
    public static Core Initialize(){
        //TODO firstrun??
        return new Core();
    }
    public User login(String email,String password) throws AuctionException{
        try {
            return UM.login(email,password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuctionException("Internal error",0);
        }
    }
    public User registration(String email,String password,String username) throws AuctionException{
        try {
            return UM.registerNew(email, password, username);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuctionException("Internal error",0);
        }
    }

    public ItemsManager getIM() {
        return IM;
    }
}
