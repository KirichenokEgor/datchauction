package by.students.grsu.entities.database;

import by.students.grsu.entities.core.AuctionException;
import by.students.grsu.entities.users.User;


import java.sql.*;

public class UsersManager {
    private Statement st;
    public UsersManager(Statement st) throws SQLException {
        this.st = st;
    }
    public User login(String email,String password) throws AuctionException, SQLException {
        ResultSet rs = st.executeQuery("SELECT * FROM users WHERE email=\'"+email+"\'");
        if(!rs.next())throw new AuctionException("User not found",11);
        if(rs.getString("password").equals(password))
            return new User(rs.getString("username"),rs.getString("email"),rs.getString("role"));
        else throw new AuctionException("Wrong password",12);
    }
    public User registerNew(String email, String password, String username) throws AuctionException, SQLException {
        ResultSet rs = st.executeQuery("SELECT * FROM users WHERE email=\'"+email+"\'");
        if(rs.next())throw new AuctionException("This email already using", 13);
        rs = st.executeQuery("SELECT * FROM users WHERE username=\'"+username+"\'");
        if(rs.next())throw new AuctionException("This nickname already using", 14);
        try {
            st.execute("INSERT INTO users (username,email,password,role) VALUES (\'"+
                    username+"\', \'"+email+"\', \'"+password+"\', \'buyer\')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return login(email,password);
    }
    public void changeRole(String username,String role) throws Exception {
        try {
            if(st.executeQuery("select * from users where username=\'"+username+"\'").next())
                st.execute("update users set role=\'"+role+"\' where username=\'"+username+"\'"); //UPDATE SET role='new_role' WHERE username='username'
            else throw new AuctionException("User not found",11);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
