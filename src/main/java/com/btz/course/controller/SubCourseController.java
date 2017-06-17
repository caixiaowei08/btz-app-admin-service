package com.btz.course.controller;

import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.SubCourseService;
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
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 2017/6/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/subCourseController")
public class SubCourseController extends BaseController {

    @Autowired
    private SubCourseService subCourseService;

    @RequestMapping(params = "datagrid")
    public void datagrid(SubCourseEntity subCourseEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

        String fid = "";
        try {
            fid = request.getParameterMap().get("fid")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtils.hasText(fid)) {
            CriteriaQuery criteriaQuery = new CriteriaQuery(SubCourseEntity.class, dataGrid, request.getParameterMap());
            criteriaQuery.installCriteria();
            DataGridReturn dataGridReturn = subCourseService.getDataGridReturn(criteriaQuery);
            DatagridJsonUtils.listToObj(dataGridReturn, SubCourseEntity.class, dataGrid.getField());
            DatagridJsonUtils.datagrid(response, dataGridReturn);
        }else{
            DatagridJsonUtils.datagrid(response, new DataGridReturn(0,new ArrayList()));
        }
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(SubCourseEntity subCourseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            subCourseEntity.setUpdateTime(new Date());
            subCourseEntity.setCreateTime(new Date());
            subCourseService.save(subCourseEntity);
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
    public AjaxJson doUpdate(SubCourseEntity subCourseEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        SubCourseEntity t = subCourseService.get(SubCourseEntity.class, subCourseEntity.getId());
        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("需要修改的数据不存在！");
            return j;
        }
        try {
            subCourseEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(subCourseEntity, t);
            subCourseService.saveOrUpdate(t);
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
    public AjaxJson doDel(SubCourseEntity subCourseEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    subCourseEntity = subCourseService.get(SubCourseEntity.class, Integer.parseInt(id_array[i]));
                    subCourseService.delete(subCourseEntity);
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
    public AjaxJson get(SubCourseEntity subCourseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = subCourseEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的账户ID！");
            return j;
        }

        SubCourseEntity subCourseDb = subCourseService.get(SubCourseEntity.class, id);
        if (subCourseDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该账户不存在！");
            return j;
        }
        j.setContent(subCourseDb);
        return j;
    }
}
