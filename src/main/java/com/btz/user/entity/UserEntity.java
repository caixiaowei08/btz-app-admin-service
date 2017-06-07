package com.btz.user.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/1.
 */
@Entity
@Table(name = "btz_user_info", schema = "", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId"})})
public class UserEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 账号ID
     */
    private String userId;
    /**
     * 账户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 密码
     */
    private String userPwd;
    /**
     * 账号状态 1-正常 2-禁用
     */
    private Integer state;
    /**
     * 账号来源 1- web系统API插入  2-APP系统录入
     */
    private Integer source;
    /**
     * 账号权限
     */
    private String authority;
    /**
     * 地区信息
     */
    private String area;
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
    @Column(name ="id",nullable=false,length=20)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name ="userId",nullable=false,length=50)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name ="userName",nullable=true,length=50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name ="phone",nullable=true,length=20)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name ="userPwd",nullable=false,length=50)
    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Column(name ="state",nullable=true,length=20)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name ="source",nullable=true,length=20)
    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Column(name ="authority",nullable=true,length=1024)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    @Column(name ="area",nullable=true,length=50)
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Column(name ="updateTime",nullable=true,length=20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name ="createTime",nullable=true,length=20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
