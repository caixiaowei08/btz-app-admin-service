package com.btz.notes.controller;

import app.btz.common.constant.NotesConstant;
import app.btz.function.notes.entity.NotesEntity;
import app.btz.function.notes.service.NotesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
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

/**
 * Created by User on 2017/7/23.
 */
@Scope("prototype")
@Controller
@RequestMapping("/admin/notesController")
public class NotesController extends BaseController {

    private static Logger logger = LogManager.getLogger(NotesController.class.getName());

    @Autowired
    private NotesService notesService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(NotesEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        criteriaQuery.getDetachedCriteria().add(Restrictions.ne("status", NotesConstant.SELF));
        DataGridReturn dataGridReturn = notesService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, NotesEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doPass")
    @ResponseBody
    public AjaxJson doPass(NotesEntity notesEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    NotesEntity t = notesService.get(NotesEntity.class, Integer.parseInt(id_array[i]));
                    notesEntity.setStatus(NotesConstant.PASS);
                    notesEntity.setCheckTime(new Date());
                    notesEntity.setUpdateTime(new Date());
                    BeanUtils.copyBeanNotNull2Bean(notesEntity, t);
                    notesService.saveOrUpdate(t);
                }
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("审核失败！");
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("审核通过成功!");
        return j;
    }

    @RequestMapping(params = "doReject")
    @ResponseBody
    public AjaxJson doReject(NotesEntity notesEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    NotesEntity t = notesService.get(NotesEntity.class, Integer.parseInt(id_array[i]));
                    notesEntity.setStatus(NotesConstant.REJECT);
                    notesEntity.setUpdateTime(new Date());
                    notesEntity.setCheckTime(new Date());
                    BeanUtils.copyBeanNotNull2Bean(notesEntity, t);
                    notesService.saveOrUpdate(t);
                }
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("审核失败！");
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("审核驳回成功!");
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(NotesEntity notesEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    NotesEntity t = notesService.get(NotesEntity.class, Integer.parseInt(id_array[i]));
                    if (t != null) {
                        notesService.delete(t);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("删除成功!");
        return j;
    }


}
