package com.btz.course.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/9.
 */
public class SubCourseVo implements Serializable{

    private Integer id;

    private String name;

    private Integer _parentId;

    private String state;

    private String iconCls;

    private Boolean checked;

    private List<SubCourseVo> children;

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

    public Integer get_parentId() {
        return _parentId;
    }

    public void set_parentId(Integer _parentId) {
        this._parentId = _parentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<SubCourseVo> getChildren() {
        return children;
    }

    public void setChildren(List<SubCourseVo> children) {
        this.children = children;
    }
}
