package com.btz.video.recorded.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/8/25.
 */
public class ClassPojo {

    private Integer id ;

    private String name;

    private List<ChapterPojo> chapters = new ArrayList<ChapterPojo>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChapterPojo> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterPojo> chapters) {
        this.chapters = chapters;
    }
}
