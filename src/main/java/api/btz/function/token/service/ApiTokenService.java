package api.btz.function.token.service;

import api.btz.function.token.vo.ApiTokenVo;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.entity.SystemTokenEntity;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/7/18.
 */
public interface ApiTokenService extends BaseService{

    /**
     * token验证
     * @param tokenValue
     * @return
     */
    public SystemTokenEntity checkUserToken(String tokenValue);


    public ApiTokenVo saveSysToken(SystemAccountEntity systemAccountEntity);


}
