package app.btz.function.record.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ExerciseRecordVo {

    /**
     * 主键
     */
    private Integer id;


    /**
     * token
     */
    private String token;

    /**
     * 课程主键
     */
    private Integer subCourseId;

    /**
     * 模块信息
     */
    private Integer moduleType;

    /**
     * app用户id
     */
    private Integer userId;

    /**
     * 试题主键
     */
    private Integer exerciseId;

    /**
     * 用户的答案
     */
    private String answer;

    /**
     * 收藏状态 0-未收藏 1-已收藏
     */
    private Integer isCollect;

    /**
     * 0-未判定 1-判定作答正确 2-判定作答错误 3-未判断分数
     */
    private Double checkState;

    /**
     * 题目分值
     */
    private Double point;

    /**
     * 修改事件
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Double getCheckState() {
        return checkState;
    }

    public void setCheckState(Double checkState) {
        this.checkState = checkState;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }
}
