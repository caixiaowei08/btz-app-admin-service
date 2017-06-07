package com.btz.admin.service.impl;

import com.btz.admin.service.AdminService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/5/26.
 */
@Service("adminService")
@Transactional
public class AdminServiceImpl extends BaseServiceImpl implements AdminService {
}
