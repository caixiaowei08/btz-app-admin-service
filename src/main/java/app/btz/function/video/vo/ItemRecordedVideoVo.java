package app.btz.function.video.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/7/21.
 */
public class ItemRecordedVideoVo implements Serializable {

    private Integer id;

    private Integer subCourseId;

    private Integer chapterId;

    private Integer authId;

    private String title = "";

    private String videoUrl = "";

    private String lectureUrl = "";

    private Integer orderNo;

    private Boolean tryOut = false;

    private Integer done = 0;

    private Integer time = 0;

    private Integer down = 0;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLectureUrl() {
        return lectureUrl;
    }

    public void setLectureUrl(String lectureUrl) {
        this.lectureUrl = lectureUrl;
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

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }
}
