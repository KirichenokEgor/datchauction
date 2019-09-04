package by.students.grsu.entities.dao.interfaces;

import by.students.grsu.entities.users.User;

import java.util.List;

public interface UserDao {
    void setEmail(String username, String email);
    String getEmail(String username);
    public void setRole(String username, String newRole) throws Exception;
    public void banUser(String username) throws Exception;
    void unbanUser(String username) throws Exception;
    List<User> searchUsers(String... words);
}
