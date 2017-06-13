package com.btz.token.service.impl;

import com.btz.token.service.UserTokenService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/13.
 */
@Service("userTokenService")
@Transactional
public class UserTokenServiceImpl extends BaseServiceImpl implements UserTokenService{

    public String getTest() {
        return "测试哟！";
    }

}
