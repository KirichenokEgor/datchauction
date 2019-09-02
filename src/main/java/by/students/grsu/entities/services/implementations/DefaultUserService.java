package by.students.grsu.entities.services.implementations;

import by.students.grsu.entities.dao.interfaces.UserDao;
import by.students.grsu.entities.services.interfaces.UserService;

public class DefaultUserService implements UserService {
    private UserDao userDao;
    public DefaultUserService(UserDao userDao){
        this.userDao=userDao;
    }
    @Override
    public void setEmail(String username, String email){
        userDao.setEmail(username, email);
    }
    @Override
    public String getEmail(String username){
        return userDao.getEmail(username);
    }
}
