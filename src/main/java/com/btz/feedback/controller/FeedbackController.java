package com.btz.feedback.controller;

import app.btz.common.constant.FeedbackConstant;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.feedback.entity.FeedbackEntity;
import com.btz.feedback.service.FeedbackService;
import com.btz.feedback.vo.FeedbackVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.hibernate.criterion.Order;
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

    @Autowired
    private ExerciseService exerciseService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(FeedbackEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("createTime"));
        DataGridReturn dataGridReturn = feedbackService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, FeedbackEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doPass")
    @ResponseBody
    public AjaxJson doPass(FeedbackEntity feedbackEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    FeedbackEntity t = feedbackService.get(FeedbackEntity.class, Integer.parseInt(id_array[i]));
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
            return j;
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
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    FeedbackEntity t = feedbackService.get(FeedbackEntity.class, Integer.parseInt(id_array[i]));
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
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("处理成功!");
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(FeedbackEntity feedbackEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            FeedbackEntity feedback = feedbackService.get(FeedbackEntity.class, feedbackEntity.getId());
            if (feedback == null) {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("问题反馈不存在或者已被删除！");
                return j;
            }
            ExerciseEntity exercise = exerciseService.get(ExerciseEntity.class, feedback.getExerciseId());
            if (exercise == null) {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("问题反馈对应题目已被删除");
                return j;
            }
            FeedbackVo feedbackVo = new FeedbackVo();
            feedbackVo.setExerciseEntity(exercise);
            feedbackVo.setFeedbackEntity(feedback);
            j.setContent(feedbackVo);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("问题反馈不存在或者已被删除！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("获取问题反馈成功！");
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(FeedbackEntity feedbackEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            feedbackEntity.setDealTime(new Date());
            feedbackEntity.setStatus(FeedbackConstant.PASS);
            FeedbackEntity t = feedbackService.get(FeedbackEntity.class, feedbackEntity.getId());
            BeanUtils.copyBeanNotNull2Bean(feedbackEntity, t);
            feedbackService.saveOrUpdate(t);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("问题回复失败！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("问题回复完成!");
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(FeedbackEntity feedbackEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    feedbackEntity = feedbackService.get(FeedbackEntity.class, Integer.parseInt(id_array[i]));
                    feedbackService.delete(feedbackEntity);
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("请选择需要删除的数据！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
        }
        return j;
    }


}
