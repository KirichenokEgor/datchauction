package by.students.grsu.websocket;

public class UserSessionService {
    private String nextUser;
    public UserSessionService(){}
    public void waitSession(String nextUser) throws Exception {
        if(this.nextUser==null)
        this.nextUser=nextUser;
        else throw new Exception("UserSession service is busy");
    }
    public String getNextUser() throws Exception {
        if(nextUser==null)throw new Exception("There is no users in queue");
        String temp = nextUser;
        nextUser = null;
        return temp;
    }
}
