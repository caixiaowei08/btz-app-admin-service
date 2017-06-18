package app.btz.common.ajax;

import java.io.Serializable;

/**
 * Created by User on 2017/6/17.
 */
public class AppRequestHeader implements Serializable{

    /**
     * 统一校验token
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
