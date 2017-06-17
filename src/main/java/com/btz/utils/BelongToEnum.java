package com.btz.utils;

/**
 * Created by User on 2017/6/16.
 */
public enum BelongToEnum {

    CHAPTER("章节练习 ", 1), CORE_POINT("核心考点", 2);

    private String typeName;

    private int index;

    BelongToEnum(String typeName,int index){
        this.typeName = typeName;
        this.index = index;
    }

    // 普通方法
    public static String getTypeName(int index) {
        for (BelongToEnum c : BelongToEnum.values()) {
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
