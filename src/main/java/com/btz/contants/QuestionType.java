package com.btz.contants;

/**
 * Created by User on 2017/7/3.
 */
public enum QuestionType {
    Q1(1,"单选题"),
    Q2(2,"多选题"),
    Q3(3,"判断题"),
    Q4(4,"填空题"),
    Q5(5,"分录题(数字)"),
    Q6(6,"分录题(固定格式)"),
    Q7(7,"材料分析题(完形填空、阅读理解等)"),
    Q8(8,"长文本题(长文本题（综合题、作文题等)"),
    Q9(9,"短文本题(中译英、英译中)"),
    Q10(10,"公式题(中级计算题、综合练习题等)");

    private Integer code;

    private String desc;

    QuestionType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
