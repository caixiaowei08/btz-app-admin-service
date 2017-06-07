package com.btz.admin.controller;

import com.btz.admin.entity.AdminEntity;
import com.btz.admin.service.AdminService;
import com.btz.user.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.framework.core.utils.PasswordUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/adminController")
public class AdminController extends BaseController {
    @Autowired
    private AdminService adminService;

    @RequestMapping(params = "changeAdminPwd")
    @ResponseBody
    public AjaxJson changeAdminPwd(AdminEntity adminEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        Integer id = adminEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的用户ID！");
            return j;
        }
        AdminEntity t = adminService.get(AdminEntity.class, id);
        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("需要修改的用户不存在！");
            return j;
        }

        try {
            adminEntity.setAccountPwd(PasswordUtil.getMD5Encryp(adminEntity.getAccountPwd()));
            adminEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(adminEntity, t);
            adminService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改密码失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "datagrid")
    public void datagrid(AdminEntity adminEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AdminEntity.class);
        detachedCriteria.add(Restrictions.eq("accountId",adminEntity.getAccountId()));
        List adminEntityList = adminService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isNotEmpty(adminEntityList)){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("新增账户失败，账户ID已经被使用！");
            return j;
        }
        try {
            adminEntity.setUpdateTime(new Date());
            adminEntity.setCreateTime(new Date());
            adminEntity.setAccountPwd(PasswordUtil.getMD5Encryp(adminEntity.getAccountPwd()));
            adminService.save(adminEntity);
        } catch (Exception e) {
            e.printStackTrace();
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
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    adminEntity = adminService.get(AdminEntity.class,Integer.parseInt(id_array[i]));
                    adminService.delete(adminEntity);
                }
            }else{
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
        Integer id = adminEntity.getId();
        if(id == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的账户ID！");
            return j;
        }

        AdminEntity adminDb = adminService.get(AdminEntity.class, id);
        if(adminDb == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该账户不存在！");
            return j;
        }
        j.setContent(adminDb);
        return j;
    }
}
