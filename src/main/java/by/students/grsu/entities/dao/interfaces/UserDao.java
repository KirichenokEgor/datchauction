package by.students.grsu.entities.dao.interfaces;

public interface UserDao {
    void setEmail(String username, String email);
    String getEmail(String username);
}
