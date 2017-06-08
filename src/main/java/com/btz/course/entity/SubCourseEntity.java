package com.btz.course.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/8.
 */
@Entity
@Table(name = "btz_sub_course_info")
public class SubCourseEntity implements Serializable {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 主课程ID
     */
    private Integer fid;
    /**
     * 子课程名称
     */
    private String subName;
    /**
     * 显示顺序
     */
    private Integer orderNo;
    /**
     * 是否允许试用 1- 是 2- 否
     */
    private Integer isTryOut;
    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
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

    @Column(name ="fid",nullable = false,length=20)
    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    @Column(name ="subName",nullable = false,length=50)
    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Column(name ="orderNo",nullable=false,length=11)
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name ="isTryOut",nullable=false,length=11)
    public Integer getIsTryOut() {
        return isTryOut;
    }

    public void setIsTryOut(Integer isTryOut) {
        this.isTryOut = isTryOut;
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
