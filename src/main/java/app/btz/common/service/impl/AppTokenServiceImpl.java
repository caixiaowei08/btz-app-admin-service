package app.btz.common.service.impl;

import app.btz.common.authority.AuthorityPojo;
import app.btz.common.authority.CourseAuthorityPojo;
import app.btz.common.service.AppTokenService;
import app.btz.function.user.service.AppUserService;
import com.alibaba.fastjson.JSON;
import com.btz.token.entity.UserTokenEntity;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import org.framework.core.common.constant.SystemConstant;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.TokenGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by User on 2017/6/17.
 */
@Service("appTokenService")
@Transactional
public class AppTokenServiceImpl implements AppTokenService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserService userService;

    public AuthorityPojo getAuthorityPojoByToken(HttpServletRequest request) {
        String token = request.getParameter(TokenGeneratorUtil.TOKEN_FLAG);
        if (StringUtils.hasText(token)) {
            UserTokenEntity userTokenEntity = appUserService.checkUserToken(token);
            if (userTokenEntity != null) {
                UserEntity appUserEntity = userService.get(UserEntity.class, userTokenEntity.getUserId());
                if (appUserEntity != null && appUserEntity.getState().equals(SystemConstant.YN_Y)) {
                    AuthorityPojo authorityPojo = new AuthorityPojo();
                    authorityPojo.setToken(userTokenEntity.getTokenValue());
                    authorityPojo.setAuthority(JSON.parseArray(appUserEntity.getAuthority(), CourseAuthorityPojo.class));
                    authorityPojo.setUserEntity(appUserEntity);
                    return authorityPojo;
                }
            }
        }
        return null;
    }

}
