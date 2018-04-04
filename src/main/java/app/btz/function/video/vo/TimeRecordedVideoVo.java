package app.btz.function.video.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/25.
 */
public class TimeRecordedVideoVo implements Serializable{

    private Integer id;

    private String time;

    private List<TitleRecordedVideoVo> list = new ArrayList<TitleRecordedVideoVo>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<TitleRecordedVideoVo> getList() {
        return list;
    }

    public void setList(List<TitleRecordedVideoVo> list) {
        this.list = list;
    }
}
