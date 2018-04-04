package com.btz.strategy.service.impl;

import com.btz.common.constant.StrategyConstants;
import com.btz.strategy.entity.ItemStrategyEntity;
import com.btz.strategy.entity.MainStrategyEntity;
import com.btz.strategy.service.StrategyService;
import com.btz.system.global.GlobalService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/8/24.
 */
@Service("strategyService")
@Transactional
public class StrategyServiceImpl extends BaseServiceImpl implements StrategyService {

    @Autowired
    private GlobalService globalService;

    public MainStrategyEntity doUpdateMainStrategy(Integer subCourseId) {
        DetachedCriteria itemStrategyEntityDetachedCriteria = DetachedCriteria.forClass(ItemStrategyEntity.class);
        itemStrategyEntityDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseId));
        List<ItemStrategyEntity> itemStrategyEntityList = globalService.getListByCriteriaQuery(itemStrategyEntityDetachedCriteria);
        Double total = 0d;
        if (CollectionUtils.isNotEmpty(itemStrategyEntityList)) {
            for (ItemStrategyEntity itemStrategyEntity : itemStrategyEntityList) {
                total += itemStrategyEntity.getPoint() * itemStrategyEntity.getExamNo();
            }
        }
        DetachedCriteria mainStrategyEntityDetachedCriteria = DetachedCriteria.forClass(MainStrategyEntity.class);
        mainStrategyEntityDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseId));
        List<MainStrategyEntity> mainStrategyEntityList = globalService.getListByCriteriaQuery(mainStrategyEntityDetachedCriteria);
        MainStrategyEntity mainStrategyEntity = new MainStrategyEntity();
        if (CollectionUtils.isNotEmpty(mainStrategyEntityList)) {
            mainStrategyEntity = mainStrategyEntityList.get(0);
            mainStrategyEntity.setTotalPoint(total);
            mainStrategyEntity.setUpdateTime(new Date());
            globalService.saveOrUpdate(mainStrategyEntity);
        } else {
            mainStrategyEntity.setTotalPoint(total);
            mainStrategyEntity.setSubCourseId(subCourseId);
            mainStrategyEntity.setType(StrategyConstants.MAIN_STRATEGY);
            mainStrategyEntity.setTotalTime(120);
            mainStrategyEntity.setCreateTime(new Date());
            mainStrategyEntity.setUpdateTime(new Date());
            globalService.save(mainStrategyEntity);
        }
        return mainStrategyEntity;
    }
}
