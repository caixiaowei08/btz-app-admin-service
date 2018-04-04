package com.btz.exercise.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/15.
 */
public class SubCourseVo implements Serializable{

    private Integer subCourseId;

    private String subName;

    private List<ChapterVo> chapterVoList;

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public List<ChapterVo> getChapterVoList() {
        return chapterVoList;
    }

    public void setChapterVoList(List<ChapterVo> chapterVoList) {
        this.chapterVoList = chapterVoList;
    }
}
