package by.students.grsu.entities.services;

import by.students.grsu.entities.dao.UserDao;
import by.students.grsu.entities.users.User;
import by.students.grsu.entities.users.UserRole;

import java.sql.SQLException;

public class UserService {
    private UserDao UserDao;
    public UserService(UserDao userDao){
        this.UserDao=userDao;
    }
    public User login(String email, String password) throws AuctionException{
        try {
            return UserDao.login(email,password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuctionException("Internal error",0);
        }
    }
    public User registration(String email, String password, String username) throws AuctionException{
        try {
            return UserDao.registerNew(email, password, username);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuctionException("Internal error",0);
        }
    }
    public void setRole(User user, String role){
        switch (role){
            case "admin":{
                user.setRole(UserRole.Admin);
                break;
            }
            case "buyer":{
                user.setRole(UserRole.Buyer);
                break;
            }
            case "seller":{
                user.setRole(UserRole.Seller);
                break;
            }
        }
    }
}
