package com.btz.course.vo;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/9.
 */
public class ChapterVo implements Serializable{

     private String id;

     private String name;

     private Integer orderNo;

     private String  level;

    /**
     *是否是叶子节点
     */
    private Boolean isLeaf = false;

     private List<ChapterVo> children;

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

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public List<ChapterVo> getChildren() {
        return children;
    }

    public void setChildren(List<ChapterVo> children) {
        this.children = children;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getLeaf() {
        return CollectionUtils.isEmpty(children);
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
