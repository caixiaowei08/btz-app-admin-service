package com.btz.system.config.service.impl;

import com.btz.strategy.entity.ItemStrategyEntity;
import com.btz.strategy.service.StrategyService;
import com.btz.system.config.entity.ConfigEntity;
import com.btz.system.config.service.ConfigService;
import com.btz.system.global.GlobalService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by User on 2017/11/10.
 */
@Service("configService")
@Transactional
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {


    @Autowired
    private GlobalService globalService;

    public ConfigEntity getConfigEntityByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        DetachedCriteria configEntityDetachedCriteria = DetachedCriteria.forClass(ConfigEntity.class);
        configEntityDetachedCriteria.add(Restrictions.eq("name", name));
        List<ConfigEntity> configEntityList = globalService.getListByCriteriaQuery(configEntityDetachedCriteria);
        if(CollectionUtils.isNotEmpty(configEntityList)){
            return configEntityList.get(0);
        }
        return null;
    }
}
