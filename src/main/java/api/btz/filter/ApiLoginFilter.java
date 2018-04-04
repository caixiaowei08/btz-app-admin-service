package api.btz.filter;

import api.btz.common.json.ApiJson;
import api.btz.function.token.service.ApiTokenService;
import com.alibaba.fastjson.JSON;
import com.btz.token.entity.SystemAccountEntity;
import com.btz.token.entity.SystemTokenEntity;
import com.btz.token.service.SystemAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.TokenGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 2017/7/18.
 */
public class ApiLoginFilter implements Filter {

    private static Logger logger = LogManager.getLogger(ApiLoginFilter.class.getName());

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private ApiTokenService apiTokenService;

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("---ApiLoginFilter--init--");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("-------ApiLoginFilter--------");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        String url = req.getServletPath();
        ApiJson j = new ApiJson();
        rsp.setContentType("application/json");
        rsp.setHeader("Cache-Control", "no-store");
        rsp.setHeader("Content-type", "text/html;charset=UTF-8");
        if (url.equals("/api/tokenController.do")) {
            chain.doFilter(request, response);
        } else {
            String token = request.getParameter(TokenGeneratorUtil.TOKEN_FLAG);
            if (StringUtils.hasText(token)) {
                SystemTokenEntity systemTokenEntity = apiTokenService.checkUserToken(token);
                if (systemTokenEntity != null) {
                    SystemAccountEntity systemAccountEntity = systemAccountService.get(SystemAccountEntity.class, systemTokenEntity.getAccountId());
                    if (systemAccountEntity != null) {
                        chain.doFilter(request, response);
                        return;
                    }
                } else {
                    j.setSuccess(ApiJson.FAIL);
                    j.setMsg("token失效或者错误，请重新获取！");
                    rsp.getWriter().write(JSON.toJSONString(j));
                    return;
                }
            } else {
                j.setSuccess(ApiJson.FAIL);
                j.setMsg("请求需要token验证码！");
                rsp.getWriter().write(JSON.toJSONString(j));
                return;
            }
        }
    }
    public void destroy() {
        logger.info("---ApiLoginFilter--destroy--");
    }
}
