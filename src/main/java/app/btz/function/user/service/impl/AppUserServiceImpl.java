package app.btz.function.user.service.impl;

import app.btz.function.user.service.AppUserService;
import app.btz.function.user.vo.AppUserVo;
import com.btz.course.entity.MainCourseEntity;
import com.btz.token.entity.UserTokenEntity;
import com.btz.token.service.UserTokenService;
import com.btz.user.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.utils.PasswordUtil;
import org.framework.core.utils.TokenGeneratorUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/13.
 */
@Service("appUserService")
@Transactional
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private UserTokenService userTokenService;

    public UserTokenEntity checkUserToken(String tokenValue) {
        DetachedCriteria userTokenDetachedCriteria = DetachedCriteria.forClass(UserTokenEntity.class);
        userTokenDetachedCriteria.add(Restrictions.eq("tokenValue", tokenValue.trim()));
        List<UserTokenEntity> userTokenList = userTokenService.getListByCriteriaQuery(userTokenDetachedCriteria);
        if (CollectionUtils.isNotEmpty(userTokenList)) {
            return userTokenList.get(0);
        }
        return null;
    }

    public AppUserVo saveUserToken(UserEntity userEntity) {
        //保存token信息
        UserTokenEntity userTokenEntity = new UserTokenEntity();
        String token = TokenGeneratorUtil.createTokenValue();
        DetachedCriteria userTokenEntityDetachedCriteria = DetachedCriteria.forClass(UserTokenEntity.class);
        userTokenEntityDetachedCriteria.add(Restrictions.eq("userId", userEntity.getId()));
        List<UserTokenEntity> userTokenEntityList = userTokenService.getListByCriteriaQuery(userTokenEntityDetachedCriteria);
        if(CollectionUtils.isNotEmpty(userTokenEntityList)){
            userTokenEntity = userTokenEntityList.get(0);
            userTokenEntity.setTokenValue(token);
            userTokenEntity.setUpdateTime(new Date());
            userTokenService.saveOrUpdate(userTokenEntity);
        }else{
            userTokenEntity.setUserId(userEntity.getId());
            userTokenEntity.setTokenValue(token);
            userTokenEntity.setCreateTime(new Date());
            userTokenEntity.setUpdateTime(new Date());
            userTokenService.save(userTokenEntity);
        }
        //返回token给App登录
        AppUserVo appUserVo = new AppUserVo();
        appUserVo.setToken(token);
        appUserVo.setUserId(userEntity.getUserId());
        appUserVo.setUserName(userEntity.getUserName());
        appUserVo.setPhoneNo(userEntity.getPhone());
        appUserVo.setAuthority(userEntity.getAuthority());
        return appUserVo;
    }
}
