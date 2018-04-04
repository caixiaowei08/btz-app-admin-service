package api.btz.function.user.json;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/8/22.
 */
public class ApiUserInfoJson implements Serializable {

    private String username;

    private String password;

    private Boolean result;

    private String tel;

    private List<AuthUserInfoJson> auth;

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

    public List<AuthUserInfoJson> getAuth() {
        return auth;
    }

    public void setAuth(List<AuthUserInfoJson> auth) {
        this.auth = auth;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
