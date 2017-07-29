package com.btz.feedback.controller;

import app.btz.common.constant.FeedbackConstant;
import com.btz.feedback.entity.FeedbackEntity;
import com.btz.feedback.service.FeedbackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * Created by User on 2017/7/25.
 */
@Scope("prototype")
@Controller
@RequestMapping("/admin/feedbackController")
public class FeedbackController extends BaseController {

    private static Logger logger = LogManager.getLogger(FeedbackController.class.getName());

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(FeedbackEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        DataGridReturn dataGridReturn = feedbackService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, FeedbackEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doPass")
    @ResponseBody
    public AjaxJson doPass(FeedbackEntity feedbackEntity,HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    FeedbackEntity t = feedbackService.get(FeedbackEntity.class,Integer.parseInt(id_array[i]));
                    feedbackEntity.setStatus(FeedbackConstant.PASS);
                    feedbackEntity.setUpdateTime(new Date());
                    feedbackEntity.setDealTime(new Date());
                    BeanUtils.copyBeanNotNull2Bean(feedbackEntity, t);
                    feedbackService.saveOrUpdate(t);
                }
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("处理失败！");
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("处理完成!");
        return j;
    }

    @RequestMapping(params = "doReject")
    @ResponseBody
    public AjaxJson doReject(FeedbackEntity feedbackEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    FeedbackEntity t = feedbackService.get(FeedbackEntity.class,Integer.parseInt(id_array[i]));
                    feedbackEntity.setStatus(FeedbackConstant.REJECT);
                    feedbackEntity.setUpdateTime(new Date());
                    feedbackEntity.setDealTime(new Date());
                    BeanUtils.copyBeanNotNull2Bean(feedbackEntity, t);
                    feedbackService.saveOrUpdate(t);
                }
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("反馈有误失败！");
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("处理成功!");
        return j;
    }


}
