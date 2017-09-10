package com.btz.exercise.pojo;

import com.btz.course.entity.SubCourseEntity;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.vo.MainExcerciseVo;
import com.btz.exercise.vo.SubExerciseVo;
import com.btz.module.entity.ModuleEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/8/18.
 */
public class ExcelExercisePojo implements Serializable {

    private SubCourseEntity subCourseEntity;

    private ModuleEntity moduleEntity;

    private List<ExerciseEntity> exerciseEntityList = new ArrayList<ExerciseEntity>();

    private List<MainExcerciseVo> mainExcerciseVoList;

    private Map<Integer,List<SubExerciseVo>> subExerciseEntityList;

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

    public List<MainExcerciseVo> getMainExcerciseVoList() {
        return mainExcerciseVoList;
    }

    public void setMainExcerciseVoList(List<MainExcerciseVo> mainExcerciseVoList) {
        this.mainExcerciseVoList = mainExcerciseVoList;
    }

    public Map<Integer, List<SubExerciseVo>> getSubExerciseEntityList() {
        return subExerciseEntityList;
    }

    public void setSubExerciseEntityList(Map<Integer, List<SubExerciseVo>> subExerciseEntityList) {
        this.subExerciseEntityList = subExerciseEntityList;
    }
}
