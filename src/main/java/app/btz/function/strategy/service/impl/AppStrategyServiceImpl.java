package app.btz.function.strategy.service.impl;

import app.btz.function.strategy.service.AppStrategyService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/8/26.
 */
@Service("appStrategyService")
@Transactional
public class AppStrategyServiceImpl extends BaseServiceImpl implements AppStrategyService {
}
