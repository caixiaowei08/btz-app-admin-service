package api.btz.function.user.json;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/8/22.
 */
public class AuthUserInfoJson implements Serializable {

    private Integer id;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date start;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date end;

    /**
     * 试题权限
     */
    private Integer exam_auth;

    /**
     * 视频权限
     */
    private Integer video_auth;

    /**
     * 视频子类权限
     */
    private List<Integer> video_class;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getExam_auth() {
        return exam_auth;
    }

    public void setExam_auth(Integer exam_auth) {
        this.exam_auth = exam_auth;
    }

    public Integer getVideo_auth() {
        return video_auth;
    }

    public void setVideo_auth(Integer video_auth) {
        this.video_auth = video_auth;
    }

    public List<Integer> getVideo_class() {
        return video_class;
    }

    public void setVideo_class(List<Integer> video_class) {
        this.video_class = video_class;
    }
}
