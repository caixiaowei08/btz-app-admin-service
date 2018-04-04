package com.btz.video.recorded.pojo;

import com.btz.video.recorded.entity.CourseRecordedVideoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/8/25.
 */
public class ChapterPojo {

    private Integer id;

    private String name;

    private List<CourseRecordedVideoEntity> recordedVideoEntityList = new ArrayList<CourseRecordedVideoEntity>();

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

    public List<CourseRecordedVideoEntity> getRecordedVideoEntityList() {
        return recordedVideoEntityList;
    }

    public void setRecordedVideoEntityList(List<CourseRecordedVideoEntity> recordedVideoEntityList) {
        this.recordedVideoEntityList = recordedVideoEntityList;
    }
}
