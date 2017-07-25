package app.btz.function.video.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/25.
 */
public class TitleRecordedVideoVo implements Serializable {

    private String title;

    private String teach;

    private List<ChapterRecordedVideoVo> list = new ArrayList<ChapterRecordedVideoVo>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeach() {
        return teach;
    }

    public void setTeach(String teach) {
        this.teach = teach;
    }

    public List<ChapterRecordedVideoVo> getList() {
        return list;
    }

    public void setList(List<ChapterRecordedVideoVo> list) {
        this.list = list;
    }
}
