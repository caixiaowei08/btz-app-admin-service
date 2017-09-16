package com.btz.feedback.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/7/25.
 */
@Entity
@Table(name = "btz_exercise_feedback_table")
public class FeedbackEntity implements Serializable{

    /**
     *主键
     */
    private Integer id;
    /**
     *课程主键
     */
    private Integer subCourseId;
    /**
     *章节主键
     */
    private Integer chapterId;
    /**
     *模块主键
     */
    private Integer moduleId;
    /**
     *模块类型
     */
    private Integer moduleType;
    /**
     *练习题主键
     */
    private Integer exerciseId;
    /**
     *题目简述
     */
    private String  resume;
    /**
     *反馈内容
     */
    private String content;

    /**
     *问题反馈回复
     */
    private String answer;

    /**
     *用户ID
     */
    private Integer userId;
    /**
     *用户名称
     */
    private String userName;

    /**
     * 处理状态  1-未处理 2-已回复
     */
    private Integer status;

    /**
     * 查看标识
     */
    private Integer flag;

    /**
     *审核时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date dealTime;

    /**
     *创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     *更新时间
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

    @Column(name = "chapterId", nullable = false, length = 20)
    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    @Column(name = "moduleId", nullable = false, length = 20)
    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    @Column(name = "moduleType", nullable = false, length = 11)
    public Integer getModuleType() {
        return moduleType;
    }


    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }
    @Column(name = "exerciseId", nullable = false, length = 20)
    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Column(name = "resume", nullable = false)
    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "userId", nullable = false, length = 20)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "userName", nullable = true, length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "status", nullable = false, length = 11)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "dealTime", nullable = true, length = 20)
    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
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

    @Column(name = "answer", nullable = true, length = 20)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(name = "flag", nullable = false, length = 11)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
