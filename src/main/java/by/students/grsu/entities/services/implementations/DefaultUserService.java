package by.students.grsu.entities.services.implementations;

import by.students.grsu.entities.dao.interfaces.UserDao;
import by.students.grsu.entities.services.interfaces.UserService;
import by.students.grsu.entities.users.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultUserService implements UserService {
    private UserDao userDao;
    public DefaultUserService(UserDao userDao){
        this.userDao=userDao;
    }
    @Override
    public void setEmail(String username, String email){
        try {
            userDao.setEmail(username, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getEmail(String username){
        return userDao.getEmail(username);
    }
    public void changeRole(String username, String newRole) throws Exception {
        try {
            userDao.setRole(username,newRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void banUser(String username) throws Exception {
        try {
            userDao.banUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void unbanUser(String username) throws Exception {
        try {
            userDao.unbanUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<User> searchUsers(String... words){
        List<User> userList = new ArrayList<>();
        try {
            userList = userDao.searchUsers(words);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}
