package com.btz.video.recorded.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/8/25.
 */
public class YearPojo {

    private Integer id ;

    private String name;

    private List<ClassPojo> classes = new ArrayList<ClassPojo>();

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

    public List<ClassPojo> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassPojo> classes) {
        this.classes = classes;
    }
}
