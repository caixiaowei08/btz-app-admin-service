package com.btz.contants;

/**
 * Created by User on 2017/7/3.
 */
public enum QuestionType {
    Q1(1,"单选题"),
    Q2(2,"多选题"),
    Q3(3,"判断题"),
    Q4(4,"分录题"),
    Q5(5,"不定项分析题"),
    Q6(6,"长文本题"),
    Q7(7,"短文本题"),
    Q8(8,"公式题"),
    Q9(9,"不定项表格");
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
