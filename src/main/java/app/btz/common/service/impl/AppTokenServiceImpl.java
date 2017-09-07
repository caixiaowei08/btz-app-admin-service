package app.btz.common.service.impl;

import api.btz.function.user.json.ApiUserInfoJson;
import api.btz.function.user.json.AuthUserInfoJson;
import app.btz.common.authority.AuthorityPojo;
import app.btz.common.authority.CourseAuthorityPojo;
import app.btz.common.constant.ApiURLConstant;
import app.btz.common.http.ApiHttpClient;
import app.btz.common.service.AppTokenService;
import app.btz.function.user.controller.AppLoginController;
import app.btz.function.user.service.AppUserService;
import com.alibaba.fastjson.JSON;
import com.btz.token.entity.UserTokenEntity;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.constant.SystemConstant;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.TokenGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/17.
 */
@Service("appTokenService")
@Transactional
public class AppTokenServiceImpl implements AppTokenService {

    private static Logger logger = LogManager.getLogger(AppTokenServiceImpl.class.getName());

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserService userService;

    public AuthorityPojo getAuthorityPojoByToken(HttpServletRequest request) {
        String token = request.getParameter(TokenGeneratorUtil.TOKEN_FLAG);
        if (StringUtils.hasText(token)) {
            UserTokenEntity userTokenEntity = appUserService.checkUserToken(token);
            UserEntity appUserEntity = null;
            if (userTokenEntity != null) {
                appUserEntity = userService.get(UserEntity.class, userTokenEntity.getUserId());
            }
            if (appUserEntity == null) {
                return null;
            }
            String result = "";
            try {
                result = ApiHttpClient.doGet(ApiURLConstant.BTZ_USER_INFO_URL + appUserEntity.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
            }
            if (org.framework.core.utils.StringUtils.hasText(result) && !result.equals("null")) {
                ApiUserInfoJson apiUserInfoJson = JSON.parseObject(result, ApiUserInfoJson.class);
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
                AuthorityPojo authorityPojo = new AuthorityPojo();
                authorityPojo.setToken(userTokenEntity.getTokenValue());
                authorityPojo.setAuthority(courseAuthorityPojoList);
                authorityPojo.setUserEntity(null);
                return authorityPojo;
            }
        }
        return null;
    }

    public UserEntity getUserEntityByToken(HttpServletRequest request) {
        String token = request.getParameter(TokenGeneratorUtil.TOKEN_FLAG);
        if (StringUtils.hasText(token)) {
            UserTokenEntity userTokenEntity = appUserService.checkUserToken(token);
            if (userTokenEntity != null) {
                UserEntity appUserEntity = userService.get(UserEntity.class, userTokenEntity.getUserId());
                return appUserEntity;
            }
        }
        return null;
    }
}
