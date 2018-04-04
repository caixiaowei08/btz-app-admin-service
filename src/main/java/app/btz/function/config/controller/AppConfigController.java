package app.btz.function.config.controller;

import app.btz.common.ajax.AppAjax;
import com.btz.system.config.ConfigConstant;
import com.btz.system.config.entity.ConfigEntity;
import com.btz.system.config.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by User on 2017/11/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/configController")
public class AppConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(params = "getInAppPurchase")
    @ResponseBody
    public AppAjax getInAppPurchase() {
        AppAjax j = new AppAjax();
        ConfigEntity configEntity = configService.getConfigEntityByName(ConfigConstant.IOS_IN_APP_PURCHASE);
        if (configEntity == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("暂未设置系统参数IAP！");
            return j;
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(configEntity);
        return j;
    }
}
