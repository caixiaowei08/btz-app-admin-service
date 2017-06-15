package app.btz.filter;

import app.btz.function.user.service.AppUserService;
import com.alibaba.fastjson.JSON;
import com.btz.token.entity.UserTokenEntity;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import org.framework.core.common.constant.SystemConstant;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.TokenGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 2017/6/13.
 */
public class AppLoginFilter implements Filter {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserService userService;

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse)response;
        String url = req.getServletPath();
        AjaxJson j = new AjaxJson();
        rsp.setContentType("application/json");
        rsp.setHeader("Cache-Control", "no-store");
        rsp.setHeader("Content-type", "text/html;charset=UTF-8");
        if (url.equals("/appLoginController.app")) {
            chain.doFilter(request, response);
        } else {
            String token = request.getParameter(TokenGeneratorUtil.TOKEN_FLAG);
            if (StringUtils.hasText(token)) {
                UserTokenEntity userTokenEntity = appUserService.checkUserToken(token);
                if (userTokenEntity != null) {
                    UserEntity appUserEntity = userService.get(UserEntity.class, userTokenEntity.getUserId());
                    if (appUserEntity != null && appUserEntity.getState().equals(SystemConstant.YN_Y)) {
                        request.setAttribute(TokenGeneratorUtil.APP_USER_SESSION, appUserEntity);
                        chain.doFilter(request, response);
                        return;
                    }else{
                        j.setSuccess(AjaxJson.CODE_FAIL);
                        j.setMsg("该账户被冻结！");
                        rsp.getWriter().write(JSON.toJSONString(j));
                        return;
                    }
                }
            }
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请求需要正确的验证token值！");
            rsp.getWriter().write(JSON.toJSONString(j));
        }
    }

    public void destroy() {

    }
}
