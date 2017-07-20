package api.btz.function.token.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/7/19.
 */
public class ApiTokenVo implements Serializable {

    private String accountId;

    private String token;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
