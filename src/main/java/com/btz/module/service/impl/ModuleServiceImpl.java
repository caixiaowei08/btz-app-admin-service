package com.btz.module.service.impl;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.system.global.GlobalService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/16.
 */
@Service("moduleService")
@Transactional
public class ModuleServiceImpl extends BaseServiceImpl implements ModuleService {

    @Autowired
    private GlobalService globalService;

    public ModuleEntity updateModuleEntityVersion(ExerciseEntity exerciseEntity){
        ModuleEntity moduleEntity = globalService.get(ModuleEntity.class, exerciseEntity.getModuleId());
        moduleEntity.setVersionNo(moduleEntity.getVersionNo() + 1);//提升版本号
        globalService.saveOrUpdate(moduleEntity);
        return moduleEntity;
    }
}
