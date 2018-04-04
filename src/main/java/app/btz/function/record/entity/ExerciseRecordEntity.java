package app.btz.function.record.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "btz_app_exercise_record_table")
public class ExerciseRecordEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 课程主键
     */
    private Integer subCourseId;

    /**
     * 模块编号
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

    @Column(name = "moduleType", nullable = false)
    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    @Column(name = "userId", nullable = false, length = 20)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "exerciseId", nullable = false, length = 20)
    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Column(name = "answer", nullable = true)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(name = "isCollect", nullable = true, length = 11)
    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    @Column(name = "checkState", nullable = true)
    public Double getCheckState() {
        return checkState;
    }

    public void setCheckState(Double checkState) {
        this.checkState = checkState;
    }

    @Column(name = "point", nullable = true)
    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    @Column(name = "updateTime", nullable = false, length = 20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "createTime", nullable = false, length = 20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
