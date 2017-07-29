package com.btz.admin.controller;

import com.btz.admin.entity.AdminEntity;
import com.btz.admin.service.AdminService;
import com.btz.common.constant.AdminConstants;
import com.btz.utils.SessionConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.framework.core.utils.PasswordUtil;
import org.framework.web.service.SystemService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/5/25.
 */
@Scope("prototype")
@Controller
@RequestMapping("/admin/adminController")
public class AdminController extends BaseController {

    private static Logger logger = LogManager.getLogger(AdminController.class.getName());

    @Autowired
    private AdminService adminService;

    @Autowired
    private SystemService systemService;

    @RequestMapping(params = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson doLogin(AdminEntity adminEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AdminEntity.class);
        detachedCriteria.add(Restrictions.eq("accountId", adminEntity.getAccountId()));
        detachedCriteria.add(Restrictions.eq("accountPwd", PasswordUtil.getMD5Encryp(adminEntity.getAccountPwd())));
        List<AdminEntity> adminEntityList = adminService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(adminEntityList)) {
            ContextHolderUtils.getSession().setAttribute(SessionConstants.ADMIN_SESSION_CONSTANTS, adminEntityList.get(0));
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setMsg("登录成功!");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_FAIL);
        j.setMsg("账号或者密码错误！");
        return j;
    }

    @RequestMapping(params = "loginOff", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson loginOff(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        ContextHolderUtils.getSession().invalidate();
        return j;
    }

    @RequestMapping(params = "changeAdminPwd")
    @ResponseBody
    public AjaxJson changeAdminPwd(AdminEntity adminEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String password = request.getParameter("password");
        String newpassword = request.getParameter("newpassword");
        AdminEntity adminSession = systemService.getAdminEntityFromSession();
        if (adminSession == null) {
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AdminEntity.class);
        detachedCriteria.add(Restrictions.eq("accountId", adminSession.getAccountId()));
        detachedCriteria.add(Restrictions.eq("accountPwd", PasswordUtil.getMD5Encryp(password)));
        List<AdminEntity> adminEntityList = adminService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(adminEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("原始密码错误！");
            return j;
        }
        AdminEntity adminDb = adminEntityList.get(0);
        try {
            adminDb.setAccountPwd(PasswordUtil.getMD5Encryp(newpassword));
            adminDb.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(adminEntity, adminDb);
            adminService.saveOrUpdate(adminDb);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改密码失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "changeAdminPwdByAdmin")
    @ResponseBody
    public AjaxJson changeAdminPwdByAdmin(AdminEntity adminEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String accountPwd = request.getParameter("accountPwd");
        AdminEntity adminSession = systemService.getAdminEntityFromSession();
        if (adminSession == null) {
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录!");
            return j;
        }
        if (!adminSession.getType().equals(AdminConstants.ROLE_ADMIN)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("只有管理员才能做此操作!");
            return j;
        }
        AdminEntity adminDb = adminService.get(AdminEntity.class, adminEntity.getId());
        if (adminDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("用户不存在或已被删除！");
            return j;
        }
        try {
            adminDb.setAccountPwd(PasswordUtil.getMD5Encryp(accountPwd));
            adminDb.setUpdateTime(new Date());
            adminService.saveOrUpdate(adminDb);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改密码失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "dataGrid")
    public void dataGrid(AdminEntity adminEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(AdminEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        DataGridReturn dataGridReturn = adminService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, AdminEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(AdminEntity adminEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AdminEntity adminSession = systemService.getAdminEntityFromSession();
        if (adminSession == null) {
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录!");
            return j;
        }
        if (!adminSession.getType().equals(AdminConstants.ROLE_ADMIN)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("只有管理员才能做此操作!");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AdminEntity.class);
        detachedCriteria.add(Restrictions.eq("accountId", adminEntity.getAccountId()));
        List adminEntityList = adminService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(adminEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("新增账户失败，账户ID已经被使用！");
            return j;
        }
        try {
            adminEntity.setUpdateTime(new Date());
            adminEntity.setCreateTime(new Date());
            adminEntity.setState(AdminConstants.ROLE_OPR);
            adminEntity.setAccountPwd(PasswordUtil.getMD5Encryp(adminEntity.getAccountPwd()));
            adminService.save(adminEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(AdminEntity adminEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        AdminEntity adminSession = systemService.getAdminEntityFromSession();
        if (adminSession == null) {
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录!");
            return j;
        }
        if (!adminSession.getType().equals(AdminConstants.ROLE_ADMIN)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("只有管理员才能做此操作!");
            return j;
        }
        AdminEntity t = adminService.get(AdminEntity.class, adminEntity.getId());
        try {
            adminEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(adminEntity, t);
            adminService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(AdminEntity adminEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        AdminEntity adminSession = systemService.getAdminEntityFromSession();
        if (adminSession == null) {
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录!");
            return j;
        }
        if (!adminSession.getType().equals(AdminConstants.ROLE_ADMIN)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("只有管理员才能做此操作!");
            return j;
        }
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    adminEntity = adminService.get(AdminEntity.class, Integer.parseInt(id_array[i]));
                    adminService.delete(adminEntity);
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未输入需要删除的数据ID！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
        }
        return j;
    }

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(AdminEntity adminEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AdminEntity adminDb = adminService.get(AdminEntity.class, adminEntity.getId());
        if (adminDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该账户不存在！");
            return j;
        }
        j.setContent(adminDb);
        return j;
    }

    @RequestMapping(params = "getAdminEntityFromSession")
    @ResponseBody
    public AjaxJson getAdminEntityFromSession(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AdminEntity adminEntity = systemService.getAdminEntityFromSession();
        if (adminEntity == null) {
            j.setSuccess(AjaxJson.CODE_LOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(adminEntity);
        j.setMsg("操作成功！");
        return j;
    }
}
