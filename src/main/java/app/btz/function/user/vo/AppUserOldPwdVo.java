package app.btz.function.user.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/11/11.
 */
public class AppUserOldPwdVo implements Serializable {

    private String username;

    private String password;

    private String old_password;

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

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
