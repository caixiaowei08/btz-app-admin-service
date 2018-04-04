package app.btz.function.strategy.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/8/26.
 */
public class ItemStrategyVo implements Serializable {

    private Integer subCourseId;

    private Integer examType;

    private Double point;

    private Integer examNo;

    private Integer orderNo;

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public Integer getExamNo() {
        return examNo;
    }

    public void setExamNo(Integer examNo) {
        this.examNo = examNo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
