package app.btz.function.strategy.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/8/26.
 */
public class MainStrategyVo implements Serializable {

    private Integer subCourseId;

    private Double totalPoint;

    private Integer totalTime;

    private List<ItemStrategyVo> questions = new ArrayList<ItemStrategyVo>();

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public List<ItemStrategyVo> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ItemStrategyVo> questions) {
        this.questions = questions;
    }
}
