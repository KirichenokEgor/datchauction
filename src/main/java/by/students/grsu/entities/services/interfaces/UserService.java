package by.students.grsu.entities.services.interfaces;

import by.students.grsu.entities.users.User;

import java.util.List;

public interface UserService {
    void setEmail(String username, String email);
    String getEmail(String username);
    void changeRole(String username, String newRole) throws Exception;
    void banUser(String username) throws Exception;
    void unbanUser(String username) throws Exception;
    List<User> searchUsers(String... words);
}
