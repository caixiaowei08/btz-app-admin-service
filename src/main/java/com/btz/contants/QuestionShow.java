package com.btz.contants;

/**
 * Created by User on 2017/8/18.
 * 显示格式
 */
public enum QuestionShow {

    T1(1,"单个题目单个选择","单个题目单个选择(单选题，判断题)"),
    T2(2,"单个题目多个选择","单个题目单个选择(多选题)");

    private Integer showType;

    private String showName;

    private String showDesc;

    QuestionShow(Integer showType, String showName, String showDesc) {
        this.showType = showType;
        this.showName = showName;
        this.showDesc = showDesc;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowDesc() {
        return showDesc;
    }

    public void setShowDesc(String showDesc) {
        this.showDesc = showDesc;
    }
}
