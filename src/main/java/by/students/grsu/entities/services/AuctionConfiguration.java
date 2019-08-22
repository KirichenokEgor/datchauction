package by.students.grsu.entities.services;

public class AuctionConfiguration {
    private String daoLocation = "localhost/datchauction";
    private String daoUser = "datchDBManager";
    private String daoPassword = "ineedyourbase";

    public String getDaoLocation() {
        return daoLocation;
    }

    public String getDaoUser() {
        return daoUser;
    }

    public String getDaoPassword() {
        return daoPassword;
    }
}
