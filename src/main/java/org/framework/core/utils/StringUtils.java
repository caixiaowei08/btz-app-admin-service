package org.framework.core.utils;


import org.framework.core.common.constant.PoiConstant;

/**
 * Created by User on 2017/6/12.
 */
public class StringUtils extends org.springframework.util.StringUtils {

    /**
     * get postfix of the path
     *
     * @param path
     * @return
     */
    public static String getPostfix(String path) {
        if (!hasText(path)) {
            return null;
        }
        if (path.contains(PoiConstant.POINT)) {
            return path.substring(path.lastIndexOf(PoiConstant.POINT) + 1, path.length());
        }
        return null;
    }
}
