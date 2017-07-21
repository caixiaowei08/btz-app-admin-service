package app.btz.function.video.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/21.
 */
public class ChapterRecordedVideoVo implements Serializable {

    private Integer id;

    private String chapterName;

    private Integer orderNo;

    private List<ItemRecordedVideoVo> children = new ArrayList<ItemRecordedVideoVo>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public List<ItemRecordedVideoVo> getChildren() {
        return children;
    }

    public void setChildren(List<ItemRecordedVideoVo> children) {
        this.children = children;
    }
}
