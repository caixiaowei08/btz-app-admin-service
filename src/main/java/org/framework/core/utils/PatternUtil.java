package org.framework.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    /**
     * <p>判断手机格式是否正确</p>
     */
    public static Boolean isPhone(String phoneNo) {
        String phoneNoPattern = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$";
        Pattern p = Pattern.compile(phoneNoPattern);
        Matcher m = p.matcher(phoneNo);
        return m.matches();
    }




    /**
     * <p>判断手机格式是否正确</p>
     */
    public static Boolean isSixDigitCheckSmsCode(String checkSmsCode) {
        String phoneNoPattern = "^\\d{6}$";
        Pattern p = Pattern.compile(phoneNoPattern);
        Matcher m = p.matcher(checkSmsCode);
        return m.matches();
    }
}
