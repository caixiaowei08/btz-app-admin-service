package com.btz.admin.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by User on 2017/5/26.
 */
@Entity
@Table(name = "btz_administrator_account", schema = "", uniqueConstraints = {@UniqueConstraint(columnNames = {"accountId"})})
@SuppressWarnings("serial")
public class AdminEntity {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 账号
     */
    private String accountId;
    /**
     * 登录密码
     */
    private String accountPwd;
    /**
     * 姓名
     */
    private String accountName;
    /**
     * 类型 1-管理员 2-录入员
     */
    private Integer type;
    /**
     * 状态 1-有效 2-无效
     */
    private Integer state;
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

    /**
     * 方法: 取得java.lang.Integer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 20)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "accountId", nullable = true, length = 50)
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Column(name = "accountPwd", nullable = true, length = 50)
    public String getAccountPwd() {
        return accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    @Column(name = "accountName", nullable = true, length = 50)
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Column(name = "type", length = 20)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "state", length = 20)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "updateTime", nullable = true, length = 20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "createTime", nullable = true, length = 20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
