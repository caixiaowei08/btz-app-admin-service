package org.framework.core.utils;

import java.util.UUID;

/**
 * Created by User on 2017/6/13.
 */
public class TokenGeneratorUtil {

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
