package app.btz.function.notes.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/7/22.
 */
@Entity
@Table(name = "btz_exercise_notes_table")
public class NotesEntity implements Serializable{
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
     *笔记内容
     */
    private String notes;
    /**
     *用户ID
     */
    private Integer userId;
    /**
     *用户名称
     */
    private String userName;
    /**
     *1-待审核  2-审核通过  3-审核不通过
     */
    private Integer status;
    /**
     *审核时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
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

    @Column(name = "notes", nullable = false)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    @Column(name = "checkTime", nullable = true, length = 20)
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Column(name = "createTime", nullable = false, length = 20)
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
