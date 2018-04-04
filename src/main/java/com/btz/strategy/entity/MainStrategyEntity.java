package com.btz.strategy.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/8/23.
 */
@Entity
@Table(name = "btz_course_strategy_main_table")
public class MainStrategyEntity implements Serializable {
    /**
     *主键
     */
    private Integer id;
    /**
     *课程主键
     */
    private Integer subCourseId;
    /**
     *试卷总分
     */
    private Double totalPoint;
    /**
     *考试时间
     */
    private Integer totalTime;
    /**
     *类型
     */
    private Integer type;
    /**
     *创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     *更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    @Column(name = "subCourseId", nullable = false)
    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }
    @Column(name = "totalPoint", nullable = false)
    public Double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
    }

    @Column(name = "totalTime", nullable = false)
    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "createTime", nullable = false, length = 20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "updateTime", nullable = false, length = 20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
