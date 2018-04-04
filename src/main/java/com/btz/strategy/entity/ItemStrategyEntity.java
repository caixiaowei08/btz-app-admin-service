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
@Table(name = "btz_course_strategy_item_table")
public class ItemStrategyEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 课程主键
     */
    private Integer subCourseId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 题目类型
     */
    private Integer examType;
    /**
     * 单个分值
     */
    private Double point;
    /**
     * 题目个数
     */
    private Integer examNo;

    /**
     *排列顺序
     */
    private Integer orderNo;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 20)
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

    @Column(name = "type", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "examType", nullable = false)
    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    @Column(name = "point", nullable = false)
    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    @Column(name = "examNo", nullable = false)
    public Integer getExamNo() {
        return examNo;
    }

    public void setExamNo(Integer examNo) {
        this.examNo = examNo;
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

    @Column(name = "orderNo", nullable = false, length = 20)
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
