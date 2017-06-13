package com.btz.token.service.impl;

import com.btz.token.service.SystemAccountService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/13.
 */
@Service("systemAccountService")
@Transactional
public class SystemAccountServiceImpl extends BaseServiceImpl implements SystemAccountService{
}
