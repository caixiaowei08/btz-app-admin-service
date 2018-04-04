package app.btz.function.testModule.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 2017/6/19.
 */
public class MainCourseAppVo implements Serializable,Comparable<MainCourseAppVo> {

    /**
     * 大类主键
     */
    private Integer mainCourseId;

    /**
     * 大类名称
     */
    private String mainCourseAppName;

    /**
     * 排序序号
     */
    private Integer orderNo;

    /**
     * 状态
     */
    private Integer state;


    public int compareTo(MainCourseAppVo o) {
        return this.orderNo.compareTo(o.getOrderNo());
    }

    /**
     * 课程列表
     */
    private List<SubCourseAppVo> children = new ArrayList<SubCourseAppVo>();

    public Integer getMainCourseId() {
        return mainCourseId;
    }

    public void setMainCourseId(Integer mainCourseId) {
        this.mainCourseId = mainCourseId;
    }

    public String getMainCourseAppName() {
        return mainCourseAppName;
    }

    public void setMainCourseAppName(String mainCourseAppName) {
        this.mainCourseAppName = mainCourseAppName;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<SubCourseAppVo> getChildren() {
        return children;
    }

    public void setChildren(List<SubCourseAppVo> children) {
        this.children = children;
    }
}
