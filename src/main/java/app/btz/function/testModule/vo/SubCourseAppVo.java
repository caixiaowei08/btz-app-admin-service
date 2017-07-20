package app.btz.function.testModule.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/6/19.
 */
public class SubCourseAppVo implements Serializable,Comparable<SubCourseAppVo>{

    /**
     * 课程主键
     */
     private Integer subCourseId;

    /**
     * 课程名称
     */
    private String subCourseName;

    /**
     * 排列序号
     */
     private Integer orderNo;

    /**
     * 是否试用
     */
    private Integer tryOut;

    public int compareTo(SubCourseAppVo o) {
        return this.orderNo.compareTo(o.getOrderNo());
    }

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public String getSubCourseName() {
        return subCourseName;
    }

    public void setSubCourseName(String subCourseName) {
        this.subCourseName = subCourseName;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTryOut() {
        return tryOut;
    }

    public void setTryOut(Integer tryOut) {
        this.tryOut = tryOut;
    }
}
