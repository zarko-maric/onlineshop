package zarko.maric.onlineshop;

public class User {
    public User(String username, String email, String password, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setIsAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

    private String username;
    private String email;
    private String password;
    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }
    public String getPassword(){
        return password;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }

}
