package com.btz.course.controller;

import com.btz.course.entity.MainCourseEntity;
import com.btz.course.service.MainCourseService;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
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
 * Created by User on 2017/6/4.
 */
@Scope("prototype")
@Controller
@RequestMapping("/mainCourseController")
public class MainCourseController extends BaseController {
    @Autowired
    private MainCourseService mainCourseService;

    @RequestMapping(params = "datagrid")
    public void datagrid(MainCourseEntity mainCourseEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(MainCourseEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        DataGridReturn dataGridReturn = mainCourseService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, MainCourseEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(MainCourseEntity mainCourseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            mainCourseEntity.setUpdateTime(new Date());
            mainCourseEntity.setCreateTime(new Date());
            mainCourseService.save(mainCourseEntity);
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
    public AjaxJson doUpdate(MainCourseEntity mainCourseEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        MainCourseEntity t = mainCourseService.get(MainCourseEntity.class, mainCourseEntity.getId());
        if(t == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("需要修改的数据不存在！");
            return j;
        }
        try {
            mainCourseEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(mainCourseEntity, t);
            mainCourseService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(MainCourseEntity mainCourseEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    mainCourseEntity = mainCourseService.get(MainCourseEntity.class, Integer.parseInt(id_array[i]));
                    mainCourseService.delete(mainCourseEntity);
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("请输入需要删除的数据ID！");
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
    public AjaxJson get(MainCourseEntity mainCourseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = mainCourseEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的账户ID！");
            return j;
        }

        MainCourseEntity mainCourseDb = mainCourseService.get(MainCourseEntity.class, id);
        if (mainCourseDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该账户不存在！");
            return j;
        }
        j.setContent(mainCourseDb);
        return j;
    }
}

