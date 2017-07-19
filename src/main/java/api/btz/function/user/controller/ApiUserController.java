package api.btz.function.user.controller;

import api.btz.function.user.service.ApiUserService;
import api.btz.function.user.vo.ApiUserVo;
import app.btz.common.ajax.AppAjax;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.service.SystemAccountService;
import org.apache.commons.collections.CollectionUtils;
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
@RequestMapping("/api/userController")
public class ApiUserController extends BaseController {

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private SystemAccountService systemAccountService;

    @RequestMapping(params = "login")
    @ResponseBody
    public AppAjax login(SystemAccountEntity systemAccountEntity, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        if (!StringUtils.hasText(systemAccountEntity.getAccountId()) || !StringUtils.hasText(systemAccountEntity.getPwd())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写账号和密码！");
            return j;
        }
        DetachedCriteria systemAccountEntityDetachedCriteria = DetachedCriteria.forClass(SystemAccountEntity.class);
        systemAccountEntityDetachedCriteria.add(Restrictions.eq("accountId", systemAccountEntity.getAccountId()));
        systemAccountEntityDetachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(systemAccountEntity.getPwd())));
        List<SystemAccountEntity> systemAccountEntityList = systemAccountService.getListByCriteriaQuery(systemAccountEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(systemAccountEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("账号或者密码错误！");
            return j;
        }
        SystemAccountEntity systemAccountDb = systemAccountEntityList.get(0);
        if (systemAccountDb.getState().equals(SystemConstant.YN_Y)) {
            try {
                ApiUserVo apiUserVo = apiUserService.saveSysToken(systemAccountDb);
                j.setReturnCode(AppAjax.SUCCESS);
                j.setContent(apiUserVo);
                return j;
            } catch (Exception e) {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("登录失败，请重新登录！");
                return j;
            }
        } else {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("账号已被冻结！");
            return j;
        }
    }
}
