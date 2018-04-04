package com.btz.exercise.service.impl;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.system.global.GlobalService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.common.system.BusinessException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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

    public void doDelAllBySubCourseIdAndModuleId(ExerciseEntity exerciseEntity) {
        DetachedCriteria exerciseEntityDetachedCriteria = DetachedCriteria.forClass(ExerciseEntity.class);
        exerciseEntityDetachedCriteria.add(Restrictions.eq("subCourseId", exerciseEntity.getSubCourseId()));
        exerciseEntityDetachedCriteria.add(Restrictions.eq("moduleType", exerciseEntity.getModuleType()));
        List<ExerciseEntity> exerciseEntityList = globalService.getListByCriteriaQuery(exerciseEntityDetachedCriteria);
        if(CollectionUtils.isNotEmpty(exerciseEntityList)){
            for (ExerciseEntity e:exerciseEntityList) {
                globalService.delete(e);
            }
            moduleService.updateModuleEntityVersion(exerciseEntityList.get(0));
        }
    }
}
