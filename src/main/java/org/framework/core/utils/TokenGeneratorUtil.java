package org.framework.core.utils;

import java.util.UUID;

/**
 * Created by User on 2017/6/13.
 */
public class TokenGeneratorUtil {

    public final static String TOKEN_FLAG = "token";

    public final static String APP_USER_SESSION = "app_user_session";
    /**
     *token唯一值生成器
     * @return
     */
    public static String createTokenValue(){
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        token = token.toLowerCase();
        return token.replaceAll("-", "");
    }

}
