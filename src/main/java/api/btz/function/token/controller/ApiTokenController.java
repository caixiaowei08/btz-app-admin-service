package api.btz.function.token.controller;

import api.btz.common.json.ApiJson;
import api.btz.function.token.service.ApiTokenService;
import api.btz.function.token.vo.ApiTokenVo;
import app.btz.common.ajax.AppAjax;
import com.alibaba.fastjson.JSON;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.service.SystemAccountService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.constant.SystemConstant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.utils.PasswordUtil;
import org.framework.core.utils.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by User on 2017/7/18.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api/tokenController")
public class ApiTokenController extends BaseController {

    private static Logger logger = LogManager.getLogger(ApiTokenController.class.getName());

    @Autowired
    private ApiTokenService apiTokenService;

    @Autowired
    private SystemAccountService systemAccountService;

    @RequestMapping(params = "getSysToken")
    @ResponseBody
    public ApiJson getSysToken(SystemAccountEntity systemAccountEntity, HttpServletRequest request) {
        logger.info("--获取系统交互 token--getSysToken--start--" + JSON.toJSONString(systemAccountEntity));
        ApiJson j = new ApiJson();
        if (!StringUtils.hasText(systemAccountEntity.getAccountId()) || !StringUtils.hasText(systemAccountEntity.getPwd())) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("请填写账号和密码！");
            return j;
        }
        DetachedCriteria systemAccountEntityDetachedCriteria = DetachedCriteria.forClass(SystemAccountEntity.class);
        systemAccountEntityDetachedCriteria.add(Restrictions.eq("accountId", systemAccountEntity.getAccountId()));
        systemAccountEntityDetachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(systemAccountEntity.getPwd())));
        List<SystemAccountEntity> systemAccountEntityList = systemAccountService.getListByCriteriaQuery(systemAccountEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(systemAccountEntityList)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("账号或者密码错误！");
            return j;
        }
        SystemAccountEntity systemAccountDb = systemAccountEntityList.get(0);
        if (systemAccountDb.getState().equals(SystemConstant.YN_Y)) {
            try {
                ApiTokenVo apiTokenVo = apiTokenService.saveSysToken(systemAccountDb);
                j.setSuccess(ApiJson.SUCCESS);
                j.setContent(apiTokenVo);
                logger.info("--获取系统交互 token--getSysToken--return--" + JSON.toJSONString(apiTokenVo));
                return j;
            } catch (Exception e) {
                logger.error(e.fillInStackTrace());
                j.setSuccess(AppAjax.FAIL);
                j.setMsg("登录失败，请重新登录！");
                return j;
            }
        } else {
            j.setSuccess(AppAjax.FAIL);
            j.setMsg("账号已被冻结！");
            return j;
        }
    }
}
