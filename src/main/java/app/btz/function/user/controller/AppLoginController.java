package app.btz.function.user.controller;

import app.btz.function.user.service.AppUserService;
import app.btz.function.user.vo.AppUserVo;
import com.btz.token.entity.UserTokenEntity;
import com.btz.token.service.UserTokenService;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.constant.SystemConstant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.PasswordUtil;
import org.framework.core.utils.TokenGeneratorUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/13.
 */
@Scope("prototype")
@Controller
@RequestMapping("/appLoginController")
public class AppLoginController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private AppUserService appUserService;

    @RequestMapping(params = "login")
    @ResponseBody
    public AjaxJson changeAdminPwd(UserEntity userEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria userEntityDetachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        if (userEntity.getUserId() == null || userEntity.getUserPwd() == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入账号或者密码！");
            return j;
        }
        userEntityDetachedCriteria.add(Restrictions.eq("userId", userEntity.getUserId()));
        userEntityDetachedCriteria.add(Restrictions.eq("userPwd", PasswordUtil.getMD5Encryp(userEntity.getUserPwd())));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(userEntityDetachedCriteria);

        if (CollectionUtils.isNotEmpty(userEntityList)) {
            UserEntity userDb = userEntityList.get(0);
            if (userDb.getState().equals(SystemConstant.YN_Y)) {
                try {
                    AppUserVo appUserVo = appUserService.saveUserToken(userDb);
                    j.setSuccess(AjaxJson.CODE_SUCCESS);
                    j.setContent(appUserVo);
                    return j;
                }catch (Exception e){
                    j.setSuccess(AjaxJson.CODE_FAIL);
                    j.setMsg("登录失败，请重新登录！");
                    return j;
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("该账户被冻结！");
                return j;
            }
        } else {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("账号或者密码不正确！");
            return j;
        }
    }
}
