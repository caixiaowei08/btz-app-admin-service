package org.framework.web.service;

import com.btz.admin.entity.AdminEntity;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/5/24.
 */
public interface SystemService extends BaseService{

    public AdminEntity getAdminEntityFromSession();

    public Boolean isAdminEntityLogin();



}
