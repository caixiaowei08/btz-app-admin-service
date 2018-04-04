package com.btz.module.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/16.
 */
@Entity
@Table(name = "btz_sub_course_class_info")
public class ModuleEntity implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 课程id
     */
    private Integer mainCourseId;

    /**
     * 课程id
     */
    private Integer subCourseId;
    /**
     * 模块题目版本序列
     */
    private Integer versionNo;
    /**
     * 状态 1-有效 2-禁用
     */
    private Integer s_state;

    /**
     * 模块别名 主要用于考前押题 取另外的名称
     */
    private String alias;

    /**
     * 预设值链接
     */
    private String url;

    /**
     * 模块类型 1-章节练习 2 -核心考点 3-考前押题 4-授课视频 5-直播视频
     */
    private Integer type;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
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

    @Column(name = "subCourseId", nullable = false, length = 20)
    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    @Column(name = "versionNo", nullable = false, length = 11)
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Column(name = "state", nullable = false, length = 11)
    public Integer getS_state() {
        return s_state;
    }

    public void setS_state(Integer s_state) {
        this.s_state = s_state;
    }


    @Column(name = "type", nullable = false, length = 11)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "createTime", nullable = true, length = 20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "updateTime", nullable = true, length = 20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "mainCourseId", nullable = false, length = 20)
    public Integer getMainCourseId() {
        return mainCourseId;
    }

    public void setMainCourseId(Integer mainCourseId) {
        this.mainCourseId = mainCourseId;
    }

    @Column(name = "alias", nullable = true, length = 50)
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "url", nullable = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
