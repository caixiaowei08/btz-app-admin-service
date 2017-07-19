package api.btz.function.user.service.impl;

import api.btz.function.user.service.ApiUserService;
import api.btz.function.user.vo.ApiUserVo;
import app.btz.function.user.vo.AppUserVo;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.entity.SystemTokenEntity;
import com.btz.token.entity.UserTokenEntity;
import com.btz.token.service.SystemAccountService;
import com.btz.token.service.SystemTokenService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.TokenGeneratorUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/7/18.
 */
@Service("apiUserService")
@Transactional
public class ApiUserServiceImpl extends BaseServiceImpl implements ApiUserService {

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private SystemTokenService systemTokenService;

    public SystemTokenEntity checkUserToken(String tokenValue) {
        DetachedCriteria userTokenDetachedCriteria = DetachedCriteria.forClass(SystemTokenEntity.class);
        userTokenDetachedCriteria.add(Restrictions.eq("tokenValue", tokenValue.trim()));
        List<SystemTokenEntity> systemTokenEntityList = systemTokenService.getListByCriteriaQuery(userTokenDetachedCriteria);
        if (CollectionUtils.isNotEmpty(systemTokenEntityList)) {
            return systemTokenEntityList.get(0);
        }
        return null;
    }


    public ApiUserVo saveSysToken(SystemAccountEntity systemAccountEntity) {
        String token = TokenGeneratorUtil.createTokenValue();
        SystemTokenEntity systemTokenEntity = new SystemTokenEntity();
        DetachedCriteria systemTokenDetachedCriteria = DetachedCriteria.forClass(SystemTokenEntity.class);
        systemTokenDetachedCriteria.add(Restrictions.eq("accountId", systemAccountEntity.getId()));
        List<SystemTokenEntity> systemTokenEntityList = systemTokenService.getListByCriteriaQuery(systemTokenDetachedCriteria);
        if(CollectionUtils.isNotEmpty(systemTokenEntityList)){
            SystemTokenEntity systemTokenDb = systemTokenEntityList.get(0);
            systemTokenDb.setTokenValue(token);
            systemTokenDb.setUpdateTime(new Date());
            systemTokenService.saveOrUpdate(systemTokenDb);
        }else{
            systemTokenEntity.setAccountId(systemAccountEntity.getId());
            systemTokenEntity.setTokenValue(token);
            systemTokenEntity.setCreateTime(new Date());
            systemTokenEntity.setUpdateTime(new Date());
            systemTokenService.save(systemTokenEntity);
        }
        ApiUserVo apiUserVo = new ApiUserVo();
        apiUserVo.setAccountId(systemAccountEntity.getAccountId());
        apiUserVo.setToken(token);
        return apiUserVo;
    }
}
