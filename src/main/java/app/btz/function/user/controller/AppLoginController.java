package app.btz.function.user.controller;

import api.btz.common.constant.SourceConstant;
import api.btz.function.user.controller.ApiUserController;
import api.btz.function.user.json.ApiUserInfoJson;
import api.btz.function.user.json.AuthUserInfoJson;
import app.btz.common.ajax.AppAjax;
import app.btz.common.authority.CourseAuthorityPojo;
import app.btz.common.constant.ApiURLConstant;
import app.btz.common.http.ApiHttpClient;
import app.btz.function.user.service.AppUserService;
import app.btz.function.user.vo.AppUserVo;
import com.alibaba.fastjson.JSON;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/13.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/loginController")
public class AppLoginController extends BaseController {

    private static Logger logger = LogManager.getLogger(AppLoginController.class.getName());

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserService userService;

    @RequestMapping(params = "login")
    @ResponseBody
    public AppAjax login(UserEntity userEntity, HttpServletRequest request) {
        AppAjax j = new AppAjax();
        DetachedCriteria userEntityDetachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        if (!StringUtils.hasText(userEntity.getUserId()) || !StringUtils.hasText(userEntity.getUserPwd())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请填写账号和密码！");
            return j;
        }

        UserEntity userDb = null;
        userEntityDetachedCriteria.add(Restrictions.eq("userId", userEntity.getUserId()));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(userEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            String result = ApiHttpClient.doGet(ApiURLConstant.BTZ_USER_INFO_URL + userEntity.getUserId());
            if (StringUtils.hasText(result) && !result.equals("null")) {
                ApiUserInfoJson apiUserInfoJson = JSON.parseObject(result, ApiUserInfoJson.class);
                userDb = new UserEntity();
                userDb.setUserId(apiUserInfoJson.getUsername());
                userDb.setUserPwd(PasswordUtil.getMD5Encryp(apiUserInfoJson.getPassword()));
                List<AuthUserInfoJson> authUserInfoJsonList = apiUserInfoJson.getAuth();
                List<CourseAuthorityPojo> courseAuthorityPojoList = new ArrayList<CourseAuthorityPojo>();
                if (CollectionUtils.isNotEmpty(authUserInfoJsonList)) {
                    for (AuthUserInfoJson authUserInfoJson : authUserInfoJsonList) {
                        CourseAuthorityPojo courseAuthorityPojo = new CourseAuthorityPojo();
                        courseAuthorityPojo.setSubCourseId(authUserInfoJson.getId());
                        courseAuthorityPojo.setStartTime(authUserInfoJson.getStart());
                        courseAuthorityPojo.setEndTime(authUserInfoJson.getEnd());
                        courseAuthorityPojoList.add(courseAuthorityPojo);
                    }
                }
                userDb.setAuthority(JSON.toJSONString(courseAuthorityPojoList));
                userDb.setSource(SourceConstant.SOURCE_WEB);
                userDb.setState(SystemConstant.YN_Y);
                userDb.setCreateTime(new Date());
                userDb.setUpdateTime(new Date());
                userService.save(userDb);
            }
        } else {
            userDb = userEntityList.get(0);
        }

        if (userDb == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("账号不存在！");
            return j;
        }

        if (userDb.getUserPwd().equals(PasswordUtil.getMD5Encryp(userEntity.getUserPwd()))
                && userDb.getUserId().equals(userEntity.getUserId())) {
            if (userDb.getState().equals(SystemConstant.YN_Y)) {
                AppUserVo appUserVo = appUserService.saveUserToken(userDb);
                j.setReturnCode(AppAjax.SUCCESS);
                j.setContent(appUserVo);
                return j;
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("该账户被冻结！");
                return j;
            }
        } else {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("账号或者密码不正确！");
            return j;
        }
    }
}
