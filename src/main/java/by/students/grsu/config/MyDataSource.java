package by.students.grsu.config;

import by.students.grsu.entities.services.AuctionConfiguration;
import com.mysql.cj.jdbc.Driver;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class MyDataSource implements DataSource {

    @Override
    public Connection getConnection() throws SQLException {
        AuctionConfiguration configuration = new AuctionConfiguration();
        DriverManager.registerDriver(new Driver());
        Connection con = DriverManager.getConnection("jdbc:mysql://"+configuration.getDaoLocation(),configuration.getDaoUser(),configuration.getDaoPassword());
        return con;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        AuctionConfiguration configuration = new AuctionConfiguration();
        DriverManager.registerDriver(new Driver());
        Connection con = DriverManager.getConnection("jdbc:mysql://"+configuration.getDaoLocation(),username,password);
        return con;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
