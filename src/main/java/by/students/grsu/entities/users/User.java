package by.students.grsu.entities.users;

public class User {
    private String role;
    private String username;
    private String email;
    private boolean enabled = true;

    public User(String username,String email, String role){
        this.username = username;
        this.email = email;
        this.role = role;
    }
    public User(String username, String role){
        this.username = username;
        this.role = role.split("_")[1];
        this.email = "<hidden>";
    }
    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
