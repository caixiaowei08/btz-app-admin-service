package app.btz.function.user.service;

import app.btz.function.user.vo.AppUserVo;
import com.btz.token.entity.UserTokenEntity;
import com.btz.user.entity.UserEntity;
import org.framework.core.common.model.json.AjaxJson;

/**
 * Created by User on 2017/6/13.
 */
public interface AppUserService {
    /**
     * token验证
     * @param tokenValue
     * @return
     */
    public UserTokenEntity checkUserToken(String tokenValue);


    public AppUserVo saveUserToken(UserEntity userEntity);
}
