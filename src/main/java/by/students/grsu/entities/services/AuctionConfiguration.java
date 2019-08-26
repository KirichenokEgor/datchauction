package by.students.grsu.entities.services;

public class AuctionConfiguration {
    private String daoLocation = "localhost/datchauction";
    private String daoUser = "default";
    private String daoPassword = "1111";

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
