package com.btz.module.service.impl;

import com.btz.module.service.ModuleService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/16.
 */
@Service("moduleService")
@Transactional
public class ModuleServiceImpl extends BaseServiceImpl implements ModuleService {
}
