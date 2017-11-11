package app.btz.function.user.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.constant.ApiURLConstant;
import app.btz.common.http.ApiHttpClient;
import app.btz.function.user.vo.*;
import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.utils.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/13.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/userController")
public class AppUserController extends BaseController {

    private static Logger logger = LogManager.getLogger(AppUserController.class.getName());

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
            params.add(new BasicNameValuePair("code",appUserPwdVo.getEmailCode()));
            params.add(new BasicNameValuePair("username",appUserPwdVo.getUserId()));
            params.add(new BasicNameValuePair("password",appUserPwdVo.getNewPwd()));
            params.add(new BasicNameValuePair("token",ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_UPDATE_PWD_EMIAL_CODE_URL,params);
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
            params.add(new BasicNameValuePair("username",appUserPwdVo.getUserId()));
            params.add(new BasicNameValuePair("password",appUserPwdVo.getNewPwd()));
            params.add(new BasicNameValuePair("old_password",appUserPwdVo.getOldPwd()));
            params.add(new BasicNameValuePair("token",ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_UPDATE_PWD_OLD_PWD_URL,params);
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
            params.add(new BasicNameValuePair("username",appUser.getUsername()));
            params.add(new BasicNameValuePair("password",appUser.getPassword()));
            params.add(new BasicNameValuePair("email",appUser.getEmail()));
            params.add(new BasicNameValuePair("token",ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_ADD_USER_URL,params);
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
            params.add(new BasicNameValuePair("username",appUser.getUsername()));
            params.add(new BasicNameValuePair("token",ApiURLConstant.BTZ_TOKEN));
            logger.info("BTZ请求数据:" + JSON.toJSONString(params));
            result = ApiHttpClient.doPost(ApiURLConstant.BTZ_ACTIVE_USER_URL,params);
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
}
