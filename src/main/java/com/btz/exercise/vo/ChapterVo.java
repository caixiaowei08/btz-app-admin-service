package com.btz.exercise.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/15.
 */
public class ChapterVo  implements Serializable{

    private Integer chapterId;

    private String chapterName;

    private List<ChapterVo> children;

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<ChapterVo> getChildren() {
        return children;
    }

    public void setChildren(List<ChapterVo> children) {
        this.children = children;
    }
}
