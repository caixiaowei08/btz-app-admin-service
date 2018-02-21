package app.btz.function.user.service.impl;

import app.btz.common.ajax.AppAjax;
import app.btz.common.constant.ApiURLConstant;
import app.btz.common.constant.SfynConstant;
import app.btz.common.http.ApiHttpClient;
import app.btz.function.user.entity.PhoneSmsCodeEntity;
import app.btz.function.user.service.PhoneSmsCodeService;
import app.btz.function.user.vo.AppUserPwdReturnVo;
import app.btz.function.user.vo.AppUserSmsVo;
import com.alibaba.fastjson.JSON;
import com.btz.system.global.GlobalService;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.DateUtils;
import org.framework.core.utils.PatternUtil;
import org.framework.core.utils.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("phoneSmsCodeService")
@Transactional
public class PhoneSmsCodeServiceImpl extends BaseServiceImpl implements PhoneSmsCodeService {
    private static Logger logger = LogManager.getLogger(PhoneSmsCodeServiceImpl.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private UserService userService;

    public AppAjax doSaveAndResetPhoneCheckSmsInfo(AppUserSmsVo appUserSmsVo) {
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

        //查询
        DetachedCriteria phoneSmsCodeEntityDetachedCriteria = DetachedCriteria.forClass(PhoneSmsCodeEntity.class);
        phoneSmsCodeEntityDetachedCriteria.add(Restrictions.eq("userId", appUserSmsVo.getAppUserId()));
        List<PhoneSmsCodeEntity> phoneSmsCodeEntityList = globalService.getListByCriteriaQuery(phoneSmsCodeEntityDetachedCriteria);

        try {
            if (CollectionUtils.isNotEmpty(phoneSmsCodeEntityList)) {
                //db中存在该账号的验证码信息 reset
                PhoneSmsCodeEntity phoneSmsCodeEntity = phoneSmsCodeEntityList.get(0);
                phoneSmsCodeEntity.setActiveTime(DateUtils.addMinute(new Date(), 15));//有效时间为15分钟
                phoneSmsCodeEntity.setCheckCode(appUserSmsVo.getSmsCheckCode());
                phoneSmsCodeEntity.setPhoneNo(appUserSmsVo.getPhoneNo());
                phoneSmsCodeEntity.setState(SfynConstant.SFYN_S_Y);//有效
                phoneSmsCodeEntity.setUpdateTime(new Date());
                globalService.saveOrUpdate(phoneSmsCodeEntity);
            } else {
                //do save 验证码信息
                PhoneSmsCodeEntity phoneSmsCodeEntity = new PhoneSmsCodeEntity();
                phoneSmsCodeEntity.setUserId(appUserSmsVo.getAppUserId());
                phoneSmsCodeEntity.setPhoneNo(appUserSmsVo.getPhoneNo());
                phoneSmsCodeEntity.setCheckCode(appUserSmsVo.getSmsCheckCode());
                phoneSmsCodeEntity.setState(SfynConstant.SFYN_S_Y);
                phoneSmsCodeEntity.setActiveTime(DateUtils.addMinute(new Date(), 15));//有效时间为15分钟
                phoneSmsCodeEntity.setUpdateTime(new Date());
                phoneSmsCodeEntity.setCreateTime(new Date());
                globalService.save(phoneSmsCodeEntity);
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("验证码生成失败：db error ！");
            return j;
        }

        j.setReturnCode(AppAjax.SUCCESS);
        j.setMsg("短信验证码发送成功!");
        return j;
    }


    public AppAjax doSaveAndResetPhoneNoToDbAndWebService(AppUserSmsVo appUserSmsVo) {

        AppAjax j = new AppAjax();

        DetachedCriteria phoneSmsCodeEntityDetachedCriteria = DetachedCriteria.forClass(PhoneSmsCodeEntity.class);
        phoneSmsCodeEntityDetachedCriteria.add(Restrictions.eq("userId", appUserSmsVo.getAppUserId()));
        List<PhoneSmsCodeEntity> phoneSmsCodeEntityList = globalService.getListByCriteriaQuery(phoneSmsCodeEntityDetachedCriteria);

        if (CollectionUtils.isEmpty(phoneSmsCodeEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("未能获取验证码信息，请重新获取短息验证码！");
            return j;
        }

        PhoneSmsCodeEntity phoneSmsCodeEntity = phoneSmsCodeEntityList.get(0);

        if (!phoneSmsCodeEntity.getPhoneNo().equals(appUserSmsVo.getPhoneNo())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("输入的手机不匹配，请重试！");
            return j;
        }

        if (!phoneSmsCodeEntity.getCheckCode().equals(appUserSmsVo.getSmsCheckCode())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("短信验证码错误，请重新输入！");
            return j;
        }

        if (phoneSmsCodeEntity.getState().equals(SfynConstant.SFYN_S_N)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("该验证码已经被验证使用，请重新获取短信验证码！");
            return j;
        }

        if (DateUtils.compareTo(new Date(), phoneSmsCodeEntity.getActiveTime()) > 0) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("该验证码已过有效时间，请重新获取短信验证码！");
            return j;
        }

        //执行保存动作
        UserEntity userEntity = userService.get(UserEntity.class, phoneSmsCodeEntity.getUserId());
        if (userEntity == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("未能获取账户信息，请重新登录app！");
            return j;
        }

        //设置app用户的手机信息
        userEntity.setPhone(phoneSmsCodeEntity.getPhoneNo());
        userEntity.setUpdateTime(new Date());
        userService.save(userEntity);

        //更新状态码信息
        phoneSmsCodeEntity.setState(SfynConstant.SFYN_S_N);
        phoneSmsCodeEntity.setUpdateTime(new Date());
        globalService.saveOrUpdate(phoneSmsCodeEntity);

        String result = "";
        try {
            String URL = ApiURLConstant.BTZ_SAVE_PHONE_NO_URL.replace("USERNAME", userEntity.getUserId()).replace("PHONE_NO", phoneSmsCodeEntity.getPhoneNo());
            logger.info("保存用户手机信息到web服务器 BTZ请求数据:" + URL);
            result = ApiHttpClient.doGet(URL);
            logger.info("保存用户手机信息到web服务器 BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("百题斩服务器异常，保存用户手机号失败，请稍后重试！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserPwdReturnVo appUserPwdReturnVo = JSON.parseObject(result, AppUserPwdReturnVo.class);
                if (appUserPwdReturnVo.getResult()) {
                    j.setReturnCode(AppAjax.SUCCESS);
                    j.setMsg("手机号更新成功！");
                    return j;
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("百题斩服务器异常，手机号更新失败!");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("百题斩服务器异常，手机号更新失败!");
            return j;
        }
    }
}
