package com.btz.poi.pojo;

import java.io.Serializable;

/**
 * Created by User on 2017/6/15.
 */
public class ExerciseExcelPojo implements Serializable{

    /**
     * 课程id
     */
    private Integer subCourseId;

    /**
     * 课程名称
     */
    private String  subCourseName;

    /**
     * 章节id
     */
    private Integer chapterId;

    /**
     * 章节名称
     */
    private String  chapterName;

    /**
     * 归属 1、章节练习 2、核心考点 3、考前押题
     */
    private Integer belongTo;

    /**
     * 归属 1、章节练习 2、核心考点 3、考前押题
     */
    private String belongToName;

    /**
     * 试题类别
     */
    private Integer type;

    /**
     * 试题类别名称
     */
    private String typeName;

    /**
     * 题干
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 答案
     */
    private String answer;

    /**
     * 解析
     */
    private String answerKey;

    /**
     * 显示顺序
     */
    private Integer orderNo;

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public String getSubCourseName() {
        return subCourseName;
    }

    public void setSubCourseName(String subCourseName) {
        this.subCourseName = subCourseName;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Integer belongTo) {
        this.belongTo = belongTo;
    }

    public String getBelongToName() {
        return belongToName;
    }

    public void setBelongToName(String belongToName) {
        this.belongToName = belongToName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
