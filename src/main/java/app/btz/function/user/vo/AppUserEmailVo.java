package app.btz.function.user.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/11/11.
 */
public class AppUserEmailVo implements Serializable {

    private String username;

    private String password;

    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
