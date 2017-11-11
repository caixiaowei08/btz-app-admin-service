package app.btz.function.user.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/11/11.
 */
public class AppUser implements Serializable{

    private String username;

    private String password;

    private String email;

    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
