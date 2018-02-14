package app.btz.common.constant;

/**
 * Created by User on 2017/8/22.
 */
public class ApiURLConstant {

    /**
     * 百题斩TOKEN
     */
    public final static String BTZ_TOKEN = "3ZI3Bt2CyMpimPzC";


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
     * &username=USERNAME&password=PWD&code=CODE
     */
    public final static String BTZ_UPDATE_PWD_EMIAL_CODE_URL = "http://api.baitizhan.com/index.php?c=User&a=reset";

    /**
     * 使用旧密码修改密码
     * &username=USERNAME&password=PWD&old_password=OLD
     */
    public final static String BTZ_UPDATE_PWD_OLD_PWD_URL = "http://api.baitizhan.com/index.php?c=User&a=reset";

    /**
     * 百题斩新增用户 ios内购 POST请求
     * 创建用户 username=test_1234567
     * password=psw_1234567
     * email=qqqq@qq.com
     * 也是post
     */
    public final static String BTZ_ADD_USER_URL = "http://api.baitizhan.com/index.php?c=User&a=addUser";

    /**
     * 百题斩激活用户增加权限
     * 激活，post 参数 username
     */
    public final static String BTZ_ACTIVE_USER_URL = "http://api.baitizhan.com/index.php?c=User&a=active";


    /**
     * 发送手机验证码
     * mobile是手机号
     * 返回值：
     * 结果：false/true
     * 验证码：6位数字
     */
    public final static String BTZ_SEND_PHONE_SMS_URL = "http://api.baitizhan.com/index.php?c=Sms&a=sendCheckCode&token=3ZI3Bt2CyMpimPzC&mobile=PHONE_NO";


    /**
     * 保存手机号到web服务器
     * 用户名
     * 手机号
     */
    public final static String BTZ_SAVE_PHONE_NO_URL = "http://api.baitizhan.com/index.php?c=Sms&a=setNumber&token=3ZI3Bt2CyMpimPzC&username=USERNAME&tel=PHONE_NO";


}
