package com.btz.user.controller;

import com.btz.admin.entity.AdminEntity;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import com.btz.utils.Constant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.framework.core.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by User on 2017/6/1.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(params = "datagrid")
    public void datagrid(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(UserEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        DataGridReturn dataGridReturn = userService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, UserEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = userEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的用户ID！");
            return j;
        }

        UserEntity userDb = userService.get(UserEntity.class, id);
        if (userDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该用户不存在！");
            return j;
        }

        j.setContent(userDb);
        return j;
    }

    @RequestMapping(params = "doLock")
    @ResponseBody
    public AjaxJson lockALLSelect(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response){
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    UserEntity t = userService.get(UserEntity.class,Integer.parseInt(id_array[i]));
                    userEntity.setState(Constant.STATE_LOCK);
                    userEntity.setUpdateTime(new Date());
                    BeanUtils.copyBeanNotNull2Bean(userEntity, t);
                    userService.saveOrUpdate(t);
                }
            }else{
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未输入需要冻结的数据ID！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("冻结失败！");
        }
        return j;
    }

    @RequestMapping(params = "doUnLock")
    @ResponseBody
    public AjaxJson unLockALLSelect(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response){
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    UserEntity t = userService.get(UserEntity.class,Integer.parseInt(id_array[i]));
                    userEntity.setState(Constant.STATE_UNLOCK);
                    userEntity.setUpdateTime(new Date());
                    BeanUtils.copyBeanNotNull2Bean(userEntity, t);
                    userService.saveOrUpdate(t);
                }
            }else{
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未输入需要激活的数据ID！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("激活失败！");
        }
        return j;
    }

    @RequestMapping(params = "changeAdminPwd")
    @ResponseBody
    public AjaxJson changeAdminPwd(UserEntity userEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        Integer id = userEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的用户ID！");
            return j;
        }
        UserEntity t = userService.get(UserEntity.class, id);
        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("需要修改的用户不存在！");
            return j;
        }

        try {
            userEntity.setUserPwd(PasswordUtil.getMD5Encryp(userEntity.getUserPwd()));
            userEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(userEntity, t);
            userService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改密码失败！");
            return j;
        }
        return j;
    }

}
