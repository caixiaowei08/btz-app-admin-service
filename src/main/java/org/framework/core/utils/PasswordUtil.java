package org.framework.core.utils;

import java.security.MessageDigest;

/**
 * Created by User on 2017/5/29.
 */
public class PasswordUtil {
    /**
     * MD5加码 生成32位md5码
     * @param password
     * @return
     */
    public static String getMD5Encryp(String password) {
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];//ASCII码编码值
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16){
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
