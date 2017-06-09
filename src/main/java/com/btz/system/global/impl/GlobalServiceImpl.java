package com.btz.system.global.impl;

import com.btz.system.global.GlobalService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/8.
 */
@Service("globalService")
@Transactional
public class GlobalServiceImpl extends BaseServiceImpl implements GlobalService{
}
