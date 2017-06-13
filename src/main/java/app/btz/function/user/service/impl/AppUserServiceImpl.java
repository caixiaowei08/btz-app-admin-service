package app.btz.function.user.service.impl;

import app.btz.function.user.service.AppUserService;
import app.btz.function.user.vo.AppUserVo;
import com.btz.course.entity.MainCourseEntity;
import com.btz.token.entity.UserTokenEntity;
import com.btz.token.service.UserTokenService;
import com.btz.user.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
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
        String token = TokenGeneratorUtil.createTokenValue();
        UserTokenEntity userTokenEntity = new UserTokenEntity();
        DetachedCriteria userTokenDetachedCriteria = DetachedCriteria.forClass(UserTokenEntity.class);
        userTokenDetachedCriteria.add(Restrictions.eq("userId", userEntity.getId()));
        List<UserTokenEntity> userTokenList = userTokenService.getListByCriteriaQuery(userTokenDetachedCriteria);
        if(CollectionUtils.isNotEmpty(userTokenList)){
            UserTokenEntity userTokenDb = userTokenList.get(0);
            userTokenDb.setTokenValue(token);
            userTokenDb.setUpdateTime(new Date());
            userTokenService.saveOrUpdate(userTokenDb);
        }else{
            userTokenEntity.setUserId(userEntity.getId());
            userTokenEntity.setTokenValue(token);
            userTokenEntity.setCreateTime(new Date());
            userTokenEntity.setUpdateTime(new Date());
            userTokenService.save(userTokenEntity);
        }
        AppUserVo appUserVo = new AppUserVo();
        appUserVo.setUserId(userEntity.getUserId());
        appUserVo.setUserName(userEntity.getUserName());
        appUserVo.setAuthority(userEntity.getAuthority());
        appUserVo.setToken(token);
        return appUserVo;
    }
}
