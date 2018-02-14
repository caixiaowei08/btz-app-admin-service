package app.btz.function.user.service.impl;

import app.btz.common.ajax.AppAjax;
import app.btz.function.user.entity.PhoneSmsCodeEntity;
import app.btz.function.user.service.PhoneSmsCodeService;
import app.btz.function.user.vo.AppUserSmsVo;
import com.btz.system.global.GlobalService;
import com.btz.token.entity.UserTokenEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.PatternUtil;
import org.framework.core.utils.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("phoneSmsCodeService")
@Transactional
public class PhoneSmsCodeServiceImpl extends BaseServiceImpl implements PhoneSmsCodeService {
    private static Logger logger = LogManager.getLogger(PhoneSmsCodeServiceImpl.class.getName());

    @Autowired
    private GlobalService globalService;

    public AppAjax doSaveAndResetPhoneSmsInfo(AppUserSmsVo appUserSmsVo) {
        AppAjax j = new AppAjax();

        if (appUserSmsVo.getAppUserId() == null && appUserSmsVo.getAppUserId() < 100000) {
            logger.info("账号为空或者异常，不能存储短信验证信息!");
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("短息验证码发送失败，账号异常！");
            return j;
        }

        if (StringUtils.isEmpty(appUserSmsVo.getSmsCheckCode()) || !PatternUtil.isSixDigitCheckSmsCode(appUserSmsVo.getSmsCheckCode())) {
            logger.info("短息验证码错误:userId" + appUserSmsVo.getAppUserId() + "code:" + appUserSmsVo.getSmsCheckCode());
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("短息验证码错误，请联系管理员！");
            return j;
        }

        DetachedCriteria phoneSmsCodeEntityDetachedCriteria = DetachedCriteria.forClass(PhoneSmsCodeEntity.class);
        phoneSmsCodeEntityDetachedCriteria.add(Restrictions.eq("userId", appUserSmsVo.getAppUserId()));
        List<PhoneSmsCodeEntity> phoneSmsCodeEntityList = globalService.getListByCriteriaQuery(phoneSmsCodeEntityDetachedCriteria);



        if (CollectionUtils.isEmpty(phoneSmsCodeEntityList)) {

        } else {

        }

        return j;
    }
}
