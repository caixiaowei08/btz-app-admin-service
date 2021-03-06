package app.btz.function.testModule.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/6/18.
 */
public class ExerciseVo implements Serializable {

    private Integer id;

    private String typeName;

    private Integer typeShow;

    private Integer type;

    private String title;

    private String content;

    private String answer;

    private String answerKey;

    private Integer orderNo;

    private String set = "";

    /**
     * 题目是否已做
     */
    private Double done = 0d;
    /**
     * 题目是否收藏
     */
    private Integer get = 0;

    /**
     * 题目打分
     */
    private Double sb = 0d;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Double getDone() {
        return done;
    }

    public void setDone(Double done) {
        this.done = done;
    }

    public Integer getGet() {
        return get;
    }

    public void setGet(Integer get) {
        this.get = get;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public Double getSb() {
        return sb;
    }

    public void setSb(Double sb) {
        this.sb = sb;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTypeShow() {
        return typeShow;
    }

    public void setTypeShow(Integer typeShow) {
        this.typeShow = typeShow;
    }
}
