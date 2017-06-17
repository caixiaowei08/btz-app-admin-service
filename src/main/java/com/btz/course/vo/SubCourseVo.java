package com.btz.course.vo;
import java.io.Serializable;

/**
 * Created by User on 2017/6/9.
 */
public class SubCourseVo implements Serializable{

    private String id;

    private String name;

    private Boolean isLeaf = true;

    /**
     *状态 1-可以试用 2-禁止试用
     */
    private Integer state_s;

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

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Integer getState_s() {
        return state_s;
    }

    public void setState_s(Integer state_s) {
        this.state_s = state_s;
    }
}
