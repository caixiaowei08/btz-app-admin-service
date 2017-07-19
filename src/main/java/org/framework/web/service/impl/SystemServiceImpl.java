package org.framework.web.service.impl;

import com.btz.admin.entity.AdminEntity;
import com.btz.utils.SessionConstants;
import org.framework.core.common.service.BaseService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.web.service.SystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/5/24.
 */
@Service("systemService")
@Transactional
public class SystemServiceImpl extends BaseServiceImpl implements SystemService {

    public AdminEntity getAdminEntityFromSession() {
        return (AdminEntity) ContextHolderUtils.getSession().getAttribute(SessionConstants.ADMIN_SESSION_CONSTANTS);
    }

    public Boolean isAdminEntityLogin() {
        return getAdminEntityFromSession() != null;
    }
}
