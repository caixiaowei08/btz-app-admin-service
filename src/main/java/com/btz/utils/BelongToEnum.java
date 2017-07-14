package com.btz.utils;

/**
 * Created by User on 2017/6/16.
 */
public enum BelongToEnum {

    ALL("所有模块 ", 0),CHAPTER("章节练习 ", 1), CORE_POINT("核心考点", 2),SIMULATION_TEST("模拟考场", 3),EXAM_TIPS("考前押题", 4),
    RECORDED_VIDEO("录播视频", 5),LIVE_VIDEO("直播视频 ", 6);

    private String typeName;

    private int index;

    BelongToEnum(String typeName,int index){
        this.typeName = typeName;
        this.index = index;
    }

    // 普通方法
    public static BelongToEnum getBelongToEnum(int index) {
        for (BelongToEnum c : BelongToEnum.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
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
