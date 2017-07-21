package com.btz.module.controller;

import com.alibaba.fastjson.JSON;
import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.course.service.SubCourseService;
import com.btz.course.vo.MainCourseVo;
import com.btz.course.vo.SubCourseVo;
import com.btz.exercise.controller.ExerciseController;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.TreeGrid;
import org.framework.core.utils.BeanUtils;
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
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/16.
 */
@Scope("prototype")
@Controller
@RequestMapping("/moduleController")
public class ModuleController extends BaseController {

    private static Logger logger = LogManager.getLogger(ModuleController.class.getName());

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SubCourseService subCourseService;


    @RequestMapping(params = "treeGrid")
    @ResponseBody
    public TreeGrid mainCourseTreeGrid(ModuleEntity moduleEntity, HttpServletRequest request, HttpServletResponse response) {
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", moduleEntity.getSubCourseId()));
        moduleDetachedCriteria.addOrder(Order.asc("type"));
        List<ModuleEntity> mainCourseEntities = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        TreeGrid treeGrid = new TreeGrid();
        treeGrid.setRows(mainCourseEntities);
        return treeGrid;
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(ModuleEntity moduleEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        //重复校验
        DetachedCriteria moduleEntityDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleEntityDetachedCriteria.add(Restrictions.eq("subCourseId", moduleEntity.getSubCourseId()));
        moduleEntityDetachedCriteria.add(Restrictions.eq("type", moduleEntity.getType()));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleEntityDetachedCriteria);
        if (CollectionUtils.isNotEmpty(moduleEntityList)) {
            logger.warn("不能重复新建模块:" + JSON.toJSONString(moduleEntityList.get(0)));
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("不能重复新建模块！");
            return j;
        }

        SubCourseEntity subCourseEntity = subCourseService.get(SubCourseEntity.class, moduleEntity.getSubCourseId());

        if (subCourseEntity == null) {
            logger.warn("课程已被删除，不能新建模块:" + moduleEntity.getSubCourseId());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("课程已被删除，不能新建模块！");
            return j;
        }
        try {
            moduleEntity.setVersionNo(0);
            moduleEntity.setMainCourseId(subCourseEntity.getFid());
            moduleEntity.setUpdateTime(new Date());
            moduleEntity.setCreateTime(new Date());
            moduleService.save(moduleEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(ModuleEntity moduleEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    moduleEntity = moduleService.get(ModuleEntity.class, Integer.parseInt(id_array[i]));
                    moduleService.delete(moduleEntity);
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

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(ModuleEntity moduleEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = moduleEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        ModuleEntity moduleEntityDb = moduleService.get(ModuleEntity.class, id);
        if (moduleEntityDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该数据不存在！");
            return j;
        }
        j.setContent(moduleEntityDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(ModuleEntity moduleEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        ModuleEntity t = moduleService.get(ModuleEntity.class, moduleEntity.getId());
        try {
            moduleEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(moduleEntity, t);
            moduleService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }
}
