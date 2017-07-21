package com.btz.exercise.service;
import com.btz.exercise.entity.ExerciseEntity;
import org.framework.core.common.service.BaseService;
import org.framework.core.common.system.BusinessException;

import java.util.List;

/**
 * Created by User on 2017/6/12.
 */
public interface ExerciseService extends BaseService {

    public void batchExerciseSave(List<ExerciseEntity> exerciseEntityList);

}
