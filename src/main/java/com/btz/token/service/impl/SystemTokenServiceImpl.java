package com.btz.token.service.impl;

import com.btz.token.service.SystemTokenService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/13.
 */
@Service("systemTokenService")
@Transactional
public class SystemTokenServiceImpl extends BaseServiceImpl implements SystemTokenService{
}
