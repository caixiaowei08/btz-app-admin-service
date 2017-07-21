package com.btz.module.service;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.module.entity.ModuleEntity;
import org.framework.core.common.service.BaseService;
import org.framework.core.common.system.BusinessException;

/**
 * Created by User on 2017/6/16.
 */
public interface ModuleService extends BaseService {

    public ModuleEntity updateModuleEntityVersion(ExerciseEntity exerciseEntity) throws BusinessException;

}
