package com.btz.common.filter;

import com.alibaba.fastjson.JSON;
import com.btz.admin.entity.AdminEntity;
import com.btz.utils.SessionConstants;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.FilterAjaxJson;
import org.framework.core.utils.ContextHolderUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 2017/7/28.
 */
public class ManagerLoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("--------ManagerLoginFilter---------");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        String url = req.getServletPath();
        FilterAjaxJson j = new FilterAjaxJson();
        AdminEntity adminEntity = (AdminEntity) ContextHolderUtils.getSession().getAttribute(SessionConstants.ADMIN_SESSION_CONSTANTS);
        if (adminEntity != null || url.equals("/admin/adminController.do")) {
            chain.doFilter(request, response);
        } else {
            rsp.setContentType("application/json");
            rsp.setHeader("Cache-Control", "no-store");
            rsp.setHeader("Content-type", "application/json;charset=UTF-8");
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录！");
            rsp.getWriter().write(JSON.toJSONString(j));
        }
    }

    public void destroy() {

    }
}
