package app.btz.function.user.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "btz_app_user_sms_check_code_table", schema = "", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId"})})
public class PhoneSmsCodeEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * app用户id
     */
    private Integer userId;

    /**
     * 手机号码 默认11位
     */
    private String phoneNo;
    /**
     * 短信验证码 默认6位
     */
    private String checkCode;

    /**
     * 状态 Y-待验证 N-已验证失效
     */
    private String state;

    /**
     * 验证码有效时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date activeTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 20)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "userId", nullable = false, length = 20)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "phoneNo", nullable = false, length = 20)
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Column(name = "checkCode", nullable = false, length = 20)
    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Column(name = "state", nullable = false, length = 2)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "activeTime", nullable = false, length = 20)
    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    @Column(name = "updateTime", nullable = false, length = 20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "createTime", nullable = false, length = 20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
