package app.btz.function.video.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/7/21.
 */
public class ItemLiveVideoVo implements Serializable {

    private Integer id;

    private Integer subCourseId;

    private Integer chapterId;

    private String title;

    private String teacherName;

    private String videoUrl;

    private Integer status;

    private Boolean tryOut = false;

    private Integer orderNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getTryOut() {
        return tryOut;
    }

    public void setTryOut(Boolean tryOut) {
        this.tryOut = tryOut;
    }
}
