//package by.students.grsu.entities.dao;
//
//import by.students.grsu.entities.services.AuctionException;
//import com.mysql.cj.jdbc.Driver;
//import java.sql.*;
//
//public class DBController {
//    Connection connection;
//    Statement st;
//    public DBController(String address,String username,String password) throws AuctionException {
//        try{
//        DriverManager.registerDriver(new Driver());
//        connection = DriverManager.getConnection("jdbc:mysql://"+address,username,password);
//        st = connection.createStatement();
//        st.execute("use datchauction");}
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//            throw new AuctionException("Internal error",0);
//        }
//    }
//    public AuctionDao getAuctionManager() throws AuctionException {
//        try{
//        return new AuctionDao(st);
//        }
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//            throw new AuctionException("Internal error",0);
//        }
//    }
//    public UserDao getUsersManager() throws AuctionException {
//        try {
//            return new UserDao(st);
//        }
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//            throw new AuctionException("Internal error",0);
//        }
//    }
//    public ItemDao getItemsManager() throws AuctionException {
//        try {
//            return new ItemDao(st);
//        }
//        catch (SQLException e){
//            System.out.println(e.getMessage());
//            throw new AuctionException("Internal error",0);
//        }
//    }
//}
