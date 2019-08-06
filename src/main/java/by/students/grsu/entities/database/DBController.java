package by.students.grsu.entities.database;

import by.students.grsu.entities.core.AuctionException;
import com.mysql.cj.jdbc.Driver;
import java.sql.*;

public class DBController {
    Connection connection;
    Statement st;
    public DBController(String address,String username,String password) throws AuctionException {
        try{
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection("jdbc:mysql://"+address,username,password);
        st = connection.createStatement();
        st.execute("use datchauction");}
        catch (SQLException e){
            System.out.println(e.getMessage());
            throw new AuctionException("Internal error",0);
        }
    }
    public AuctionsManager getAuctionManager() throws AuctionException {
        try{
        return new AuctionsManager(st);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            throw new AuctionException("Internal error",0);
        }
    }
    public UsersManager getUsersManager() throws AuctionException {
        try {
            return new UsersManager(st);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            throw new AuctionException("Internal error",0);
        }
    }
    public ItemsManager getItemsManager() throws AuctionException {
        try {
            return new ItemsManager(st);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            throw new AuctionException("Internal error",0);
        }
    }
}
