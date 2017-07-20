package api.btz.function.user.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/7/20.
 */
public class ApiUserVo implements Serializable {

    /**
     * 账号ID
     */
    private String userId;
    /**
     * 密码
     */
    private String userPwd;
    /**
     * 账户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 账号状态 1-正常 2-禁用
     */
    private Integer state;
    /**
     * 账号权限
     */
    private List<CourseAuthorityVo> authority;
    /**
     * 地区信息
     */
    private String area;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<CourseAuthorityVo> getAuthority() {
        return authority;
    }

    public void setAuthority(List<CourseAuthorityVo> authority) {
        this.authority = authority;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
