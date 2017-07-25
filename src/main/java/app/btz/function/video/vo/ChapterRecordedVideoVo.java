package app.btz.function.video.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/21.
 */
public class ChapterRecordedVideoVo implements Serializable {

    private Integer id;

    private String title;

    private Integer orderNo;

    private List<ItemRecordedVideoVo> list = new ArrayList<ItemRecordedVideoVo>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public List<ItemRecordedVideoVo> getList() {
        return list;
    }

    public void setList(List<ItemRecordedVideoVo> list) {
        this.list = list;
    }
}
