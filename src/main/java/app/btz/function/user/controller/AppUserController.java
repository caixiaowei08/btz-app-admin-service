package app.btz.function.user.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.constant.ApiURLConstant;
import app.btz.common.http.ApiHttpClient;
import app.btz.function.user.service.AppUserService;
import app.btz.function.user.service.PhoneSmsCodeService;
import app.btz.function.user.vo.*;
import com.alibaba.fastjson.JSON;
import com.btz.token.entity.UserTokenEntity;
import com.btz.user.service.UserService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.utils.PatternUtil;
import org.framework.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2017/6/13.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/userController")
public class AppUserController extends BaseController {

    private static Logger logger = LogManager.getLogger(AppUserController.class.getName());


    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhoneSmsCodeService phoneSmsCodeService;

    @RequestMapping(params = "sendEmail")
    @ResponseBody
    public AppAjax sendEmail(AppUserVo appUserVo, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        if (StringUtils.isEmpty(appUserVo.getUserId())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写登录账号！");
            return j;
        }

        String result = "";
        try {
            String URL = ApiURLConstant.BTZ_SEND_REST_EMAIL_URL.replace("USERNAME", appUserVo.getUserId());
            logger.info("BTZ请求数据:" + URL);
            result = ApiHttpClient.doGet(URL);
            logger.info("BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("W:邮件发送失败！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserPwdReturnVo appUserPwdReturnVo = JSON.parseObject(result, AppUserPwdReturnVo.class);
                if (appUserPwdReturnVo.getResult()) {
                    j.setReturnCode(AppAjax.SUCCESS);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("W:邮件发送失败！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("W:邮件发送失败！");
            return j;
        }

    }

    @RequestMapping(params = "doUpdatePwdByEmailCodePost")
    @ResponseBody
    public AppAjax doUpdatePwdByEmailCodePost(@RequestBody AppUserPwdVo appUserPwdVo, HttpServletRequest request) {
        return doUpdatePwdByEmailCode(appUserPwdVo, request);
    }

    @RequestMapping(params = "doUpdatePwdByEmailCode")
    @ResponseBody
    public AppAjax doUpdatePwdByEmailCode(AppUserPwdVo appUserPwdVo, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        if (StringUtils.isEmpty(appUserPwdVo.getUserId())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写登录账号！");
            return j;
        }

        if (StringUtils.isEmpty(appUserPwdVo.getNewPwd())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写新的登录密码！");
            return j;
        }

        if (StringUtils.isEmpty(appUserPwdVo.getEmailCode())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写邮箱验证码！");
            return j;
        }

        String result = "";
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("code", appUserPwdVo.getEmailCode()));
            params.add(new BasicNameValuePair("username", appUserPwdVo.getUserId()));
            params.add(new BasicNameValuePair("password", appUserPwdVo.getNewPwd()));
            params.add(new BasicNameValuePair("token", ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_UPDATE_PWD_EMIAL_CODE_URL, params);
            logger.info("BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("账号服务器异常！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserPwdReturnVo appUserPwdReturnVo = JSON.parseObject(result, AppUserPwdReturnVo.class);
                if (appUserPwdReturnVo.getResult()) {
                    j.setReturnCode(AppAjax.SUCCESS);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("服务器异常,请稍后重试！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("密码修改失败,请重试！");
            return j;
        }

    }

    @RequestMapping(params = "doUpdatePwdByOldPwdPost")
    @ResponseBody
    public AppAjax doUpdatePwdByOldPwdPost(@RequestBody AppUserPwdVo appUserPwdVo, HttpServletRequest request) {
        return doUpdatePwdByOldPwd(appUserPwdVo, request);
    }


    @RequestMapping(params = "doUpdatePwdByOldPwd")
    @ResponseBody
    public AppAjax doUpdatePwdByOldPwd(AppUserPwdVo appUserPwdVo, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        if (StringUtils.isEmpty(appUserPwdVo.getUserId())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写登录账号！");
            return j;
        }

        if (StringUtils.isEmpty(appUserPwdVo.getOldPwd())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写原始密码！");
            return j;
        }

        if (StringUtils.isEmpty(appUserPwdVo.getNewPwd())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写新密码！");
            return j;
        }

        String result = "";
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("username", appUserPwdVo.getUserId()));
            params.add(new BasicNameValuePair("password", appUserPwdVo.getNewPwd()));
            params.add(new BasicNameValuePair("old_password", appUserPwdVo.getOldPwd()));
            params.add(new BasicNameValuePair("token", ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_UPDATE_PWD_OLD_PWD_URL, params);
            logger.info("BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("BTZ:服务器异常，请联系客服！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserPwdReturnVo appUserPwdReturnVo = JSON.parseObject(result, AppUserPwdReturnVo.class);
                if (appUserPwdReturnVo.getResult()) {
                    j.setReturnCode(AppAjax.SUCCESS);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("服务器异常,请稍后重试！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("密码修改失败,请重试！");
            return j;
        }
    }


    @RequestMapping(params = "doRegisterUserPost")
    @ResponseBody
    public AppAjax doRegisterUserPost(@RequestBody AppUser appUser, HttpServletRequest request) {
        return doRegisterUser(appUser, request);
    }

    @RequestMapping(params = "doRegisterUser")
    @ResponseBody
    public AppAjax doRegisterUser(AppUser appUser, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        if (StringUtils.isEmpty(appUser.getUsername())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写账户名称！");
            return j;
        }

        if (StringUtils.isEmpty(appUser.getEmail())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写邮箱！");
            return j;
        }

        if (StringUtils.isEmpty(appUser.getPassword())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写密码！");
            return j;
        }

        String result = "";
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("username", appUser.getUsername()));
            params.add(new BasicNameValuePair("password", appUser.getPassword()));
            params.add(new BasicNameValuePair("email", appUser.getEmail()));
            params.add(new BasicNameValuePair("token", ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_ADD_USER_URL, params);
            logger.info("BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("BTZ:服务器异常，请联系客服！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserPwdReturnVo appUserPwdReturnVo = JSON.parseObject(result, AppUserPwdReturnVo.class);
                if (appUserPwdReturnVo.getResult()) {
                    j.setReturnCode(AppAjax.SUCCESS);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("服务器异常,请稍后重试！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("创建账户失败,请重试！");
            return j;
        }
    }

    @RequestMapping(params = "doActiveUser")
    @ResponseBody
    public AppAjax doActiveUser(AppUser appUser, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        if (StringUtils.isEmpty(appUser.getUsername())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写账户名称！");
            return j;
        }

        String result = "";
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("username", appUser.getUsername()));
            params.add(new BasicNameValuePair("token", ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_ACTIVE_USER_URL, params);
            logger.info("BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("BTZ:服务器异常，请联系客服！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserPwdReturnVo appUserPwdReturnVo = JSON.parseObject(result, AppUserPwdReturnVo.class);
                if (appUserPwdReturnVo.getResult()) {
                    j.setReturnCode(AppAjax.SUCCESS);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg(appUserPwdReturnVo.getMsg());
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("服务器异常,请稍后重试！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("激活用户失败,请重试！");
            return j;
        }
    }

    /**
     * 发送短信信息 接口
     * 1、获取登录账号信息 根据登录token值获取 --》判断登录token的有效性
     * 2、验证手机号码是否符合11位数字  验证手机号的基本性质
     * 3、发送给web服务端  获取发送短息 在app存储当前的生成验证码 并计算验证码的有效时间 和 生成时间
     * 注解：游客没有此功能
     */
    @RequestMapping(params = "doSendSmsCodeByTokenAndPhoneNo")
    @ResponseBody
    public AppAjax doSendSmsCodeByTokenAndPhoneNo(@RequestBody AppUserSmsVo appUserSmsVo, HttpServletRequest request) {
        AppAjax j = new AppAjax();

        //验证token
        if (StringUtils.isEmpty(appUserSmsVo.getToken())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("游客登录，不能发送手机短信！");
            return j;
        }

        UserTokenEntity userTokenEntity = appUserService.checkUserToken(appUserSmsVo.getToken());

        //验证用户
        if (userTokenEntity == null) {
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录失效，请重新登录！");
            return j;
        }

        //验证手机号
        if (StringUtils.isEmpty(appUserSmsVo.getPhoneNo())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请输入手机号码！");
            return j;
        }

        if (!PatternUtil.isPhone(appUserSmsVo.getPhoneNo())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("手机号输入格式有误，请核对！");
            return j;
        }


        String result = "";
        try {
            String URL = ApiURLConstant.BTZ_SEND_PHONE_SMS_URL.replace("PHONE_NO", appUserSmsVo.getPhoneNo());
            logger.info("发送手机短信 BTZ请求数据:" + URL);
            result = ApiHttpClient.doGet(URL);
            logger.info("发送手机短信 BTZ服务器返回:" + result);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("百题斩服务器异常，验证码发送失败，请稍后重试！");
            return j;
        }

        try {
            if (StringUtils.hasText(result) && !result.equals("null")) {
                AppUserSmsReturnVo appUserSmsReturnVo = JSON.parseObject(result, AppUserSmsReturnVo.class);
                if (appUserSmsReturnVo.getResult()) {
                    //在本服务器保存记录
                    appUserSmsVo.setAppUserId(userTokenEntity.getUserId());
                    appUserSmsVo.setSmsCheckCode(appUserSmsReturnVo.getCode());
                    return phoneSmsCodeService.doSaveAndResetPhoneCheckSmsInfo(appUserSmsVo);
                } else {
                    j.setReturnCode(AppAjax.FAIL);
                    j.setMsg("验证码发送失败，请稍后重试！");
                    return j;
                }
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("短信验证码发送失败！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("系统异常：短信验证码发送失败！");
            return j;
        }
    }


    /**
     * 验证app端输入的手机号码 验证短信码 和 用户登录token的有效值 验证通过保存到本地和web服务器
     * 1、获取登录账号信息 根据登录token值获取 --》判断登录token的有效性
     * 2、验证手机号 和 短信 验证码是否一致 状态 时间
     * 3、保存app 服务器并 发送给web服务器保存
     */
    @RequestMapping(params = "doCheckSmsCodeAndSetPhoneNoByTokenAndPhoneNoAndSmsCode")
    @ResponseBody
    public AppAjax doCheckSmsCodeAndSetPhoneNoByTokenAndPhoneNoAndSmsCode(@RequestBody AppUserSmsVo appUserSmsVo, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        //验证token
        if (StringUtils.isEmpty(appUserSmsVo.getToken())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("游客登录，不能发送手机短信！");
            return j;
        }

        UserTokenEntity userTokenEntity = appUserService.checkUserToken(appUserSmsVo.getToken());
        //验证用户
        if (userTokenEntity == null) {
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录失效，请重新登录！");
            return j;
        }

        //验证手机号
        if (StringUtils.isEmpty(appUserSmsVo.getPhoneNo())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请输入手机号码！");
            return j;
        }

        if (!PatternUtil.isPhone(appUserSmsVo.getPhoneNo())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("输入手机号有误，请重新输入！");
            return j;
        }

        //验证手机号
        if (StringUtils.isEmpty(appUserSmsVo.getSmsCheckCode())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请输入验证码！");
            return j;
        }

        if (!PatternUtil.isSixDigitCheckSmsCode(appUserSmsVo.getSmsCheckCode())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请输入6位数字短息验证码！");
            return j;
        }


        try {
            appUserSmsVo.setAppUserId(userTokenEntity.getUserId());
            return phoneSmsCodeService.doSaveAndResetPhoneNoToDbAndWebService(appUserSmsVo);
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("系统异常，请稍后重试！");
            return j;
        }

    }
}
