package app.btz.function.user.vo;

import java.io.Serializable;

public class AppUserSmsVo implements Serializable {

    private String token;

    private String phoneNo;

    private String smsCheckCode;

    private String userId;

    private Integer appUserId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSmsCheckCode() {
        return smsCheckCode;
    }

    public void setSmsCheckCode(String smsCheckCode) {
        this.smsCheckCode = smsCheckCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Integer appUserId) {
        this.appUserId = appUserId;
    }
}
