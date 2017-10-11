package app.btz.common.constant;

/**
 * Created by User on 2017/8/22.
 */
public class ApiURLConstant {
    /**
     * 用户信息获取API
     */
    public final static String BTZ_USER_INFO_URL = "http://api.baitizhan.com/index.php?c=User&a=info&token=3ZI3Bt2CyMpimPzC&username=";

    /**
     * 发送邮件信息
     */
    public final static String BTZ_SEND_REST_EMAIL_URL = "http://api.baitizhan.com/index.php?c=User&a=resetEmail&token=3ZI3Bt2CyMpimPzC&username=USERNAME";

    /**
     * 使用邮箱修改密码
     */
    public final static String BTZ_UPDATE_PWD_EMIAL_CODE_URL = "http://api.baitizhan.com/index.php?c=User&a=reset&token=3ZI3Bt2CyMpimPzC&username=USERNAME&password=PWD&code=CODE";

    /**
     * 使用旧密码修改密码
     */
    public final static String BTZ_UPDATE_PWD_OLD_PWD_URL = "http://api.baitizhan.com/index.php?c=User&a=reset&token=3ZI3Bt2CyMpimPzC&username=USERNAME&password=PWD&old_password=OLD";




}
