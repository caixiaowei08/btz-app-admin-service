package com.btz.course.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/4.
 */
@Entity
@Table(name = "btz_course_base_info")
public class MainCourseEntity implements Serializable{
    /**
     * 主键
     */
     private Integer id;
    /**
     *课程名称
     */
    private String name;
    /**
     * 显示顺序
     */
     private Integer orderNo;
    /**
     *状态 1-正常 2-禁用
     */
    private Integer state;
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
    @Column(name ="name",nullable = false,length=20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name ="orderNo",nullable=false,length=11)
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name ="state",nullable=false,length=11)
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
