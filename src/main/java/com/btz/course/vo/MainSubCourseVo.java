package com.btz.course.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.btz.course.entity.SubCourseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/8.
 */
public class MainSubCourseVo implements Serializable {
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

    /**
     * 课程列表
     */
    private List<SubCourseEntity> subCourseList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<SubCourseEntity> getSubCourseList() {
        return subCourseList;
    }

    public void setSubCourseList(List<SubCourseEntity> subCourseList) {
        this.subCourseList = subCourseList;
    }
}
