package com.btz.exercise.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/12.
 */
@Entity
@Table(name = "btz_exercise_question_info")
public class ExerciseEntity implements Serializable {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 所属课程主键
     */
    private Integer subCourseId;
    /**
     * 章节主键
     */
    private Integer chapterId;
    /**
     * 题目类型 1-单选题 2-多选题 3-判断题 4-分录题 5-不定项分析题 6-长文本题 7-短文本题 8-公式题 9-不定项表格
     */
    private Integer type;
    /**
     * 模块主键
     */
    private Integer moduleId;
    /**
     * 模块类型
     */
    private Integer moduleType;
    /**
     * 题干
     */
    private String title;
    /**
     * 题目内容
     */
    private String content;
    /**
     * 答案
     */
    private String answer;
    /**
     * 答案解析
     */
    private String answerKey;
    /**
     * 显示顺序
     */
    private Integer orderNo;

    /**
     * 题目分值
     */
    private Double point;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
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

    @Column(name = "moduleId", nullable = false, length = 11)
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

    @Column(name = "type", nullable = false, length = 11)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "answer", nullable = false)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(name = "answerKey", nullable = false)
    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    @Column(name = "orderNo", nullable = false, length = 11)
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "point", nullable = true)
    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
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


}
