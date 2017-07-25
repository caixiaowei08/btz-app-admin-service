package com.btz.course.controller;

import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.course.vo.ChapterPojo;
import com.btz.course.vo.MainCourseVo;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.BelongToEnum;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/chapterController")
public class ChapterController extends BaseController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ModuleService moduleService;


    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(ChapterPojo chapterPojo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer type = -1;
        try {
            type = Integer.parseInt(request.getParameter("moduleType"));
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("参数有误，模块参数错误！");
            return j;
        }
        DetachedCriteria moduleEntityDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleEntityDetachedCriteria.add(Restrictions.eq("subCourseId", chapterPojo.getCourseId()));
        moduleEntityDetachedCriteria.add(Restrictions.eq("type", type));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("模块已被删除！");
            return j;
        }
        ModuleEntity moduleEntity = moduleEntityList.get(0);
        ChapterEntity chapterEntity = new ChapterEntity();
        try {
            chapterEntity.setModuleId(moduleEntity.getId());
            chapterEntity.setModuleType(moduleEntity.getType());
            chapterEntity.setCourseId(chapterPojo.getCourseId());
            chapterEntity.setFid(chapterPojo.getFid());
            String level = chapterPojo.getLevel();
            if (level.equals(ConstantChapterLevel.SUBCOURSE)) {
                chapterEntity.setLevel(ConstantChapterLevel.ONE);
            } else if (level.equals(ConstantChapterLevel.ONE)) {
                chapterEntity.setLevel(ConstantChapterLevel.TWO);
            } else if (level.equals(ConstantChapterLevel.TWO)) {
                chapterEntity.setLevel(ConstantChapterLevel.THREE);
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("章节层级不能超过三层，新增失败！");
                return j;
            }
            chapterEntity.setOrderNo(chapterPojo.getOrderNo());
            chapterEntity.setChapterName(chapterPojo.getChapterName());
            chapterEntity.setCreateTime(new Date());
            chapterEntity.setUpdateTime(new Date());
            chapterService.save(chapterEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doAddPaper")
    @ResponseBody
    public AjaxJson doAddPaper(ChapterPojo chapterPojo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer type = -1;
        try {
            type = Integer.parseInt(request.getParameter("moduleType"));
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("参数有误，模块参数错误！");
            return j;
        }
        DetachedCriteria moduleEntityDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleEntityDetachedCriteria.add(Restrictions.eq("subCourseId", chapterPojo.getCourseId()));
        moduleEntityDetachedCriteria.add(Restrictions.eq("type", type));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("模块已被删除！");
            return j;
        }
        ModuleEntity moduleEntity = moduleEntityList.get(0);
        ChapterEntity chapterEntity = new ChapterEntity();
        try {
            chapterEntity.setModuleId(moduleEntity.getId());
            chapterEntity.setModuleType(moduleEntity.getType());
            chapterEntity.setCourseId(chapterPojo.getCourseId());
            chapterEntity.setFid(chapterPojo.getFid());
            String level = chapterPojo.getLevel();
            if (level.equals(ConstantChapterLevel.SUBCOURSE)) {
                chapterEntity.setLevel(ConstantChapterLevel.ONE);
            } else{
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("只能在课程下添加试卷！");
                return j;
            }
            chapterEntity.setOrderNo(chapterPojo.getOrderNo());
            chapterEntity.setChapterName(chapterPojo.getChapterName());
            chapterEntity.setCreateTime(new Date());
            chapterEntity.setUpdateTime(new Date());
            chapterService.save(chapterEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(ChapterEntity chapterEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = chapterEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("需要查询的数据ID！");
            return j;
        }
        ChapterEntity chapterDb = chapterService.get(ChapterEntity.class, id);
        if (chapterDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该记录不存在！");
            return j;
        }
        j.setContent(chapterDb);
        return j;
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(ChapterEntity chapterEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        ChapterEntity t = chapterService.get(ChapterEntity.class, chapterEntity.getId());

        if (t == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("需要修改的数据不存在！");
            return j;
        }
        try {
            chapterEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(chapterEntity, t);
            chapterService.saveOrUpdate(t);
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
    public AjaxJson doDel(ChapterEntity chapterEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    chapterEntity = chapterService.get(ChapterEntity.class, Integer.parseInt(id_array[i].substring(1)));
                    chapterService.delete(chapterEntity);
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
}