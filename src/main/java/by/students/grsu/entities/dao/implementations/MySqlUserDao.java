package by.students.grsu.entities.dao.implementations;

import by.students.grsu.entities.dao.interfaces.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlUserDao implements UserDao {
    private JdbcTemplate jdbcTemplate;
    public MySqlUserDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public void setEmail(String username, String email){
        synchronized (username) {
            try {
                st.execute("update users set email = \'" + email + "\' where username = \'" + username + "\'");
            } catch (SQLException e) {
                System.out.println("Error setting email to user in db." + e.getMessage());
            }
        }
    }
    public String getEmail(String username){
        try {
            ResultSet rs =  st.executeQuery("select email from users where username = \'" + username + "\'");
            if(rs.next()) return rs.getString("email");
        } catch (SQLException e) {
            System.out.println("Error setting email to user in db." + e.getMessage());
        }
        return null;
    }
}
