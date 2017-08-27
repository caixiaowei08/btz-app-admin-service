package com.btz.exercise.pojo;

import com.btz.course.entity.SubCourseEntity;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.module.entity.ModuleEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/8/18.
 */
public class ExcelExercisePojo implements Serializable {

    private SubCourseEntity subCourseEntity;

    private ModuleEntity moduleEntity;

    private List<ExerciseEntity> exerciseEntityList;

    public SubCourseEntity getSubCourseEntity() {
        return subCourseEntity;
    }

    public void setSubCourseEntity(SubCourseEntity subCourseEntity) {
        this.subCourseEntity = subCourseEntity;
    }

    public ModuleEntity getModuleEntity() {
        return moduleEntity;
    }

    public void setModuleEntity(ModuleEntity moduleEntity) {
        this.moduleEntity = moduleEntity;
    }

    public List<ExerciseEntity> getExerciseEntityList() {
        return exerciseEntityList;
    }

    public void setExerciseEntityList(List<ExerciseEntity> exerciseEntityList) {
        this.exerciseEntityList = exerciseEntityList;
    }
}
