package com.btz.course.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/9.
 */
public class MainCourseVo implements Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     *课程名称
     */
    private String name;
    /**
     *状态 1-正常 2-禁用
     */
    private Integer state_s;
    /**
     * 文件状态
     */
    private String state = "closed";
    /**
     *是否是叶子节点
     */
    private Boolean isLeaf = false;

    /**
     * 子目录
     */
    private List<SubCourseVo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCourseVo> getChildren() {
        return children;
    }

    public void setChildren(List<SubCourseVo> children) {
        this.children = children;
    }

    public Integer getState_s() {
        return state_s;
    }

    public void setState_s(Integer state_s) {
        this.state_s = state_s;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
