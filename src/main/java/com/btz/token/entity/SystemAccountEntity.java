package com.btz.token.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/13.
 */
@Entity
@Table(name = "btz_system_account_info")
public class SystemAccountEntity implements Serializable {

    /**
     *主键
     */
    private Integer id;
    /**
     *账户
     */
    private String accountId;
    /**
     *密码
     */
    private String pwd;
    /**
     *状态 1-正常 2-禁用
     */
    private Integer state;
    /**
     *创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     *修改时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id",nullable=false,length=20)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name ="accountId",nullable=false,length=50)
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Column(name ="pwd",nullable = false,length=50)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Column(name ="state",nullable=false,length=11,columnDefinition="INT default 1")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name ="createTime",nullable=true,length=20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name ="updateTime",nullable=true,length=20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
