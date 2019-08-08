package by.students.grsu.entities.users;

//import java.util.ArrayList;

public class User {
    private UserRole role;
    private String username;
    private String email;
    //private String password;

    public User(String username,String email, String role){
        this.setUsername(username);
        this.setEmail(email);
        //this.password=password;
        if(role.equals("seller")) this.setRole(UserRole.Seller);
        else if(role.equals("admin")) this.setRole(UserRole.Admin);
        else this.setRole(UserRole.Buyer);
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String userInfo(){
        return "Username: " + username + "\nEmail: "+email + "\nRole: "+role.toString();
    }


}
