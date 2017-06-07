package com.btz.newsBulletin.carousel.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/2.
 */
@Entity
@Table(name = "btz_app_ad_carousel", schema = "")
public class CarouselEntity implements Serializable{

    /**
     * 主键
     */
    private Integer id;
    /**
     * 图片
     */
    private String img;
    /**
     * 跳转链接
     */
    private String url;
    /**
     * 是否使用  1- 是 2 - 否
     */
    private Integer sfyn;
    /**
     * 顺序
     */
    private Integer orderNo;
    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
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

    @Column(name ="img",nullable=false)
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    @Column(name ="url",nullable=false)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name ="sfyn",nullable=false,length=11)
    public Integer getSfyn() {
        return sfyn;
    }

    public void setSfyn(Integer sfyn) {
        this.sfyn = sfyn;
    }

    @Column(name ="orderNo",nullable=true,length=11)
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
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
