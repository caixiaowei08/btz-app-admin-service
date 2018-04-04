package api.btz.function.token.service.impl;

import api.btz.function.token.service.ApiTokenService;
import api.btz.function.token.vo.ApiTokenVo;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.entity.SystemTokenEntity;
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
@Service("apiTokenService")
@Transactional
public class ApiTokenServiceImpl extends BaseServiceImpl implements ApiTokenService {

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


    public ApiTokenVo saveSysToken(SystemAccountEntity systemAccountEntity) {
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
        ApiTokenVo apiTokenVo = new ApiTokenVo();
        apiTokenVo.setAccountId(systemAccountEntity.getAccountId());
        apiTokenVo.setToken(token);
        return apiTokenVo;
    }
}
