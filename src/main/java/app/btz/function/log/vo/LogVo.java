package app.btz.function.log.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/11/13.
 */
public class LogVo implements Serializable{

    private String userId;

    private String data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
