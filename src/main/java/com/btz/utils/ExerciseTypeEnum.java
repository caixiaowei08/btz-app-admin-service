package com.btz.utils;

/**
 * Created by User on 2017/6/15.
 */
public enum ExerciseTypeEnum {

    S_SELECTION("单选题", 1), M_SELECTION("多选题", 2),JUDGE("判断题",3),
    FILLBLANKS("填空题", 4), JOURNALIZING_N("分录题(数字)", 5),JOURNALIZING("分录题",6),
    COMPOSITION("作文题", 7), UNDEFINED_TERM("不定项", 8);

    private String typeName;

    private int index;

    ExerciseTypeEnum(String typeName,int index){
        this.typeName = typeName;
        this.index = index;
    }

    // 普通方法
    public static String getTypeName(int index) {
        for (ExerciseTypeEnum c : ExerciseTypeEnum.values()) {
            if (c.getIndex() == index) {
                return c.typeName;
            }
        }
        return null;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
