package com.btz.course.controller;

import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.entity.SubCourseVo;
import com.btz.course.vo.MainSubCourseVo;
import com.btz.system.global.GlobalService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.TreeGrid;
import org.framework.core.utils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/cascadeMainSubCourseController")
public class CascadeMainSubCourseController extends BaseController {

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "doGetAllCourse")
    @ResponseBody
    public List<MainSubCourseVo> doGetAllCourse(HttpServletRequest request, HttpServletResponse response) {
        DetachedCriteria mainCourseDetachedCriteria = DetachedCriteria.forClass(MainCourseEntity.class);
        mainCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<MainCourseEntity> mainCourseEntities = globalService.getListByCriteriaQuery(mainCourseDetachedCriteria);
        List<MainSubCourseVo> mainSubCourseVos = new ArrayList<MainSubCourseVo>();
        if(CollectionUtils.isNotEmpty(mainCourseEntities)){
            for (MainCourseEntity mainCourseEntity : mainCourseEntities) {
                try {
                    MainSubCourseVo mainSubCourseVo = new MainSubCourseVo();
                    BeanUtils.copyBeanNotNull2Bean(mainCourseEntity, mainSubCourseVo);
                    DetachedCriteria mainSubCourseVoDetachedCriteria = DetachedCriteria.forClass(SubCourseEntity.class);
                    mainSubCourseVoDetachedCriteria.add(Restrictions.eq("fid",mainSubCourseVo.getId()));
                    mainSubCourseVoDetachedCriteria.addOrder(Order.asc("orderNo"));
                    List<SubCourseEntity> subCourseEntityList = globalService.getListByCriteriaQuery(mainSubCourseVoDetachedCriteria);
                    mainSubCourseVo.setSubCourseList(subCourseEntityList);
                    mainSubCourseVos.add(mainSubCourseVo);
                }catch (Exception e){
                    //@TODO 蔡晓伟 新增logger
                }
            }
        }
        return mainSubCourseVos;
    }


    @RequestMapping(params = "treeGrid")
    @ResponseBody
    public TreeGrid treeGrid(HttpServletRequest request, HttpServletResponse response) {
        List<SubCourseVo> subCourseVos = new ArrayList<SubCourseVo>();
        SubCourseVo subCourseVo1 = new SubCourseVo();
        subCourseVo1.setId(1);
        subCourseVo1.setName("第一章");


        List<SubCourseVo> children = new ArrayList<SubCourseVo>();

        SubCourseVo subCourseVo2 = new SubCourseVo();


        subCourseVo2.setId(2);
        subCourseVo2.setName("第一节");
        subCourseVo2.set_parentId(1);
        children.add(subCourseVo2);
        subCourseVos.add(subCourseVo1);

        subCourseVo1.setChildren(children);
        TreeGrid treeGrid = new TreeGrid();
        treeGrid.setRows(subCourseVos);
        treeGrid.setTotal(2);
        return treeGrid;
    }
}
