package api.btz.function.user.service;

import api.btz.function.user.vo.ApiUserVo;
import app.btz.function.user.vo.AppUserVo;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.entity.SystemTokenEntity;
import com.btz.token.entity.UserTokenEntity;
import com.btz.user.entity.UserEntity;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/7/18.
 */
public interface ApiUserService extends BaseService{

    /**
     * token验证
     * @param tokenValue
     * @return
     */
    public SystemTokenEntity checkUserToken(String tokenValue);


    public ApiUserVo saveSysToken(SystemAccountEntity systemAccountEntity);


}
