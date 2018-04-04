package com.btz.newsBulletin.carousel.controller;

import com.btz.newsBulletin.carousel.entity.CarouselEntity;
import com.btz.newsBulletin.carousel.service.CarouselService;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/2.
 */
@Scope("prototype")
@Controller
@RequestMapping("/admin/carouselController")
public class CarouselController extends BaseController {

    @Autowired
    private CarouselService carouselService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(CarouselEntity carouselEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(CarouselEntity.class, dataGrid, request.getParameterMap());
        String subCourseId = request.getParameter("subCourseId");
        String sfyn = request.getParameter("sfyn");
        if (StringUtils.isEmpty(subCourseId)) {
            DatagridJsonUtils.datagrid(response, new DataGridReturn(0, new ArrayList()));
            return;
        }
        criteriaQuery.getDetachedCriteria().add(
                Restrictions.or(
                        Restrictions.eq("subCourseId",Integer.parseInt(subCourseId)),
                        Restrictions.eq("flag",2)
                )
        );
        if (StringUtils.hasText(sfyn)) {
            criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sfyn", Integer.parseInt(sfyn)));
        }
        DataGridReturn dataGridReturn = carouselService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, CarouselEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doAddCourseCarousel")
    @ResponseBody
    public AjaxJson doAddCourseCarousel(@RequestBody CarouselEntity carouselEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            carouselEntity.setUpdateTime(new Date());
            carouselEntity.setCreateTime(new Date());
            carouselService.save(carouselEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doAddGlobalCarousel")
    @ResponseBody
    public AjaxJson doAddGlobalCarousel(@RequestBody CarouselEntity carouselEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            carouselEntity.setUpdateTime(new Date());
            carouselEntity.setCreateTime(new Date());
            carouselService.save(carouselEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(CarouselEntity carouselEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = carouselEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要查询的用户ID！");
            return j;
        }

        CarouselEntity carouselDb = carouselService.get(CarouselEntity.class, id);
        if (carouselDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该用户不存在！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(carouselDb);
        return j;
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(CarouselEntity carouselEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        CarouselEntity t = carouselService.get(CarouselEntity.class, carouselEntity.getId());
        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改的轮播信息不存在！");
            return j;
        }
        try {
            carouselEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(carouselEntity, t);
            carouselService.saveOrUpdate(t);
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
    public AjaxJson doDel(CarouselEntity carouselEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    carouselEntity = carouselService.get(CarouselEntity.class, Integer.parseInt(id_array[i]));
                    carouselService.delete(carouselEntity);
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
            return j;
        }
        return j;
    }

}
