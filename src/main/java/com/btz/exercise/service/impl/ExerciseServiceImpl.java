package com.btz.exercise.service.impl;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.module.service.ModuleService;
import com.btz.system.global.GlobalService;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.common.system.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by User on 2017/6/12.
 */
@Service("exerciseService")
@Transactional
public class ExerciseServiceImpl extends BaseServiceImpl implements ExerciseService {

    @Autowired
    private GlobalService globalService;

    @Autowired
    private ModuleService moduleService;

    public void batchExerciseSave(List<ExerciseEntity> exerciseEntityList) {

        for (ExerciseEntity exerciseEntity : exerciseEntityList) {
            moduleService.updateModuleEntityVersion(exerciseEntity);
            globalService.save(exerciseEntity);
        }

    }
}
