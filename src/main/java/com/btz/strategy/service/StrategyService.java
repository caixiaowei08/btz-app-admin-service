package com.btz.strategy.service;

import com.btz.strategy.entity.MainStrategyEntity;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/8/23.
 */
public interface StrategyService extends BaseService{

    public MainStrategyEntity doUpdateMainStrategy(Integer subCourseId);

}
