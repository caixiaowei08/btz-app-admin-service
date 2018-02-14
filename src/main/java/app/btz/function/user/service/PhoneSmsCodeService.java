package app.btz.function.user.service;

import app.btz.common.ajax.AppAjax;
import app.btz.function.user.vo.AppUserSmsVo;
import org.framework.core.common.service.BaseService;

public interface PhoneSmsCodeService extends BaseService {


    public AppAjax doSaveAndResetPhoneSmsInfo(AppUserSmsVo appUserSmsVo);

}
