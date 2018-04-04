package app.btz.function.log.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.function.feedback.controller.AppFeedbackController;
import app.btz.function.feedback.vo.FeedbackVo;
import app.btz.function.log.vo.LogVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/11/13.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/logController")
public class AppLogController extends BaseController {

    private static Logger logger = LogManager.getLogger(AppLogController.class.getName());

    @RequestMapping(params = "log")
    @ResponseBody
    public AppAjax log(LogVo logVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        logger.info("logVo:"+logVo.getUserId()+":"+logVo.getData());
        return j;
    }



}
