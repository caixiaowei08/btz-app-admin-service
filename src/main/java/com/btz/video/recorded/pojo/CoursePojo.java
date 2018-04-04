package com.btz.video.recorded.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/8/25.
 */
public class CoursePojo {

    private Integer id;

    private String name;

    private List<YearPojo> years = new ArrayList<YearPojo>();

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

    public List<YearPojo> getYears() {
        return years;
    }

    public void setYears(List<YearPojo> years) {
        this.years = years;
    }
}
