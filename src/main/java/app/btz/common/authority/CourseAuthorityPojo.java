package app.btz.common.authority;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/7/19.
 */
public class CourseAuthorityPojo implements Serializable{

    private Integer subCourseId;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date endTime;

    /**
     * 试题权限
     */
    private Integer examAuth = 0;

    /**
     * 视频权限
     */
    private Integer videoAuth = 0;

    private List<Integer> videoClass = new ArrayList<Integer>();

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getExamAuth() {
        return examAuth;
    }

    public void setExamAuth(Integer examAuth) {
        this.examAuth = examAuth;
    }

    public Integer getVideoAuth() {
        return videoAuth;
    }

    public void setVideoAuth(Integer videoAuth) {
        this.videoAuth = videoAuth;
    }

    public List<Integer> getVideoClass() {
        return videoClass;
    }

    public void setVideoClass(List<Integer> videoClass) {
        this.videoClass = videoClass;
    }
}
