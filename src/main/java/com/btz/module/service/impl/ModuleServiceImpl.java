package com.btz.module.service.impl;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.system.global.GlobalService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.common.system.BusinessException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by User on 2017/6/16.
 */
@Service("moduleService")
@Transactional
public class ModuleServiceImpl extends BaseServiceImpl implements ModuleService {

    @Autowired
    private GlobalService globalService;

    public ModuleEntity updateModuleEntityVersion(ExerciseEntity exerciseEntity) throws BusinessException {
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", exerciseEntity.getSubCourseId()));
        moduleDetachedCriteria.add(Restrictions.eq("type", exerciseEntity.getType()));
        List<ModuleEntity> moduleEntityList = globalService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            throw new BusinessException("题目模块已被删除，出错题目：" + exerciseEntity.getTitle());
        }
        ModuleEntity moduleEntity = moduleEntityList.get(0);
        moduleEntity.setVersionNo(moduleEntity.getVersionNo() + 1);//提升版本号
        globalService.saveOrUpdate(moduleEntity);
        return moduleEntity;
    }
}
