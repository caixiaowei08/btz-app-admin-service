package app.btz.function.video.vo;

import com.btz.video.live.vo.LiveVideoPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/21.
 */
public class ChapterLiveVideoVo implements Serializable {

    private Integer id;

    private String chapterName;

    private Integer orderNo;

    private List<ItemLiveVideoVo> children = new ArrayList<ItemLiveVideoVo>();

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

    public List<ItemLiveVideoVo> getChildren() {
        return children;
    }

    public void setChildren(List<ItemLiveVideoVo> children) {
        this.children = children;
    }
}
