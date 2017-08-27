package com.btz.strategy.controller;

import com.btz.common.constant.StrategyConstants;
import com.btz.strategy.entity.ItemStrategyEntity;
import com.btz.strategy.entity.MainStrategyEntity;
import com.btz.strategy.service.StrategyService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
import java.util.List;

/**
 * Created by User on 2017/8/24.
 */
@Scope("prototype")
@Controller
@RequestMapping("/admin/strategyController")
public class StrategyController extends BaseController {

    @Autowired
    private StrategyService strategyService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(ItemStrategyEntity itemStrategyEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        if (StringUtils.isEmpty(itemStrategyEntity.getSubCourseId())) {
            DatagridJsonUtils.datagrid(response, new DataGridReturn(0, new ArrayList()));
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(ItemStrategyEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.getDetachedCriteria().addOrder(Order.asc("examType"));
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("subCourseId", itemStrategyEntity.getSubCourseId()));
        DataGridReturn dataGridReturn = strategyService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, ItemStrategyEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doAddItem")
    @ResponseBody
    public AjaxJson doAddItem(ItemStrategyEntity itemStrategyEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria itemStrategyEntityDetachedCriteria = DetachedCriteria.forClass(ItemStrategyEntity.class);
        itemStrategyEntityDetachedCriteria.add(Restrictions.eq("subCourseId", itemStrategyEntity.getSubCourseId()));
        itemStrategyEntityDetachedCriteria.add(Restrictions.eq("examType", itemStrategyEntity.getExamType()));
        List<ItemStrategyEntity> itemStrategyEntityList = strategyService.getListByCriteriaQuery(itemStrategyEntityDetachedCriteria);
        if (CollectionUtils.isNotEmpty(itemStrategyEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("不能重复添加！");
            return j;
        }
        try {
            itemStrategyEntity.setType(StrategyConstants.ITEM_STRATEGY);
            itemStrategyEntity.setUpdateTime(new Date());
            itemStrategyEntity.setCreateTime(new Date());
            strategyService.save(itemStrategyEntity);
            strategyService.doUpdateMainStrategy(itemStrategyEntity.getSubCourseId());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("添加失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doDelItem")
    @ResponseBody
    public AjaxJson doDelItem(ItemStrategyEntity itemStrategyEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    itemStrategyEntity = strategyService.get(ItemStrategyEntity.class, Integer.parseInt(id_array[i]));
                    strategyService.delete(itemStrategyEntity);
                    strategyService.doUpdateMainStrategy(itemStrategyEntity.getSubCourseId());
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

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doUpdateItem")
    @ResponseBody
    public AjaxJson doUpdateItem(ItemStrategyEntity itemStrategyEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        ItemStrategyEntity t = strategyService.get(ItemStrategyEntity.class, itemStrategyEntity.getId());
        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("题型不存在或者已被删除！");
            return j;
        }
        try {
            itemStrategyEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(itemStrategyEntity, t);
            strategyService.saveOrUpdate(t);
            strategyService.doUpdateMainStrategy(t.getSubCourseId());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("编辑失败！");
            return j;
        }
        return j;
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doGetItem")
    @ResponseBody
    public AjaxJson doGetItem(ItemStrategyEntity itemStrategyEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        ItemStrategyEntity t = null;
        try {
            t = strategyService.get(ItemStrategyEntity.class, itemStrategyEntity.getId());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("获取信息有误！");
            return j;
        }
        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("题型不存在或者已被删除！");
            return j;
        }
        j.setContent(t);
        return j;
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doGetMain")
    @ResponseBody
    public AjaxJson doGetMain(MainStrategyEntity mainStrategyEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria mainStrategyEntityDetachedCriteria = DetachedCriteria.forClass(MainStrategyEntity.class);
        mainStrategyEntityDetachedCriteria.add(Restrictions.eq("subCourseId", mainStrategyEntity.getSubCourseId()));
        List<MainStrategyEntity> mainStrategyEntityList = strategyService.getListByCriteriaQuery(mainStrategyEntityDetachedCriteria);
        if (CollectionUtils.isNotEmpty(mainStrategyEntityList)) {
            mainStrategyEntity = mainStrategyEntityList.get(0);
            j.setContent(mainStrategyEntity);
            return j;
        }
        try {
            strategyService.doUpdateMainStrategy(mainStrategyEntity.getSubCourseId());
            mainStrategyEntityList = strategyService.getListByCriteriaQuery(mainStrategyEntityDetachedCriteria);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("获取信息有误！");
            return j;
        }
        if (CollectionUtils.isNotEmpty(mainStrategyEntityList)) {
            mainStrategyEntity = mainStrategyEntityList.get(0);
            j.setContent(mainStrategyEntity);
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doUpdateMain")
    @ResponseBody
    public AjaxJson doUpdateMain(MainStrategyEntity mainStrategyEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        MainStrategyEntity t = strategyService.get(MainStrategyEntity.class, mainStrategyEntity.getId());
        try {
            mainStrategyEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(mainStrategyEntity, t);
            strategyService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("设置时间失败！");
            return j;
        }
        return j;
    }


}
