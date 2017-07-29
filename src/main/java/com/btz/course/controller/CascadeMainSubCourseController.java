package com.btz.course.controller;

import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.vo.ChapterVo;
import com.btz.course.vo.MainCourseVo;
import com.btz.course.vo.SubCourseVo;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.system.global.GlobalService;
import com.btz.utils.BelongToEnum;
import com.btz.utils.Constant;
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
import org.springframework.util.StringUtils;
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
@RequestMapping("/admin/cascadeMainSubCourseController")
public class CascadeMainSubCourseController extends BaseController {

    @Autowired
    private GlobalService globalService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping(params = "mainCourseTreeGrid")
    @ResponseBody
    public TreeGrid mainCourseTreeGrid(HttpServletRequest request, HttpServletResponse response) {
        Integer type = null;
        TreeGrid treeGrid = new TreeGrid();
        try {
            type = Integer.parseInt(request.getParameter("type"));
        }catch (Exception e){
            return treeGrid;
        }
        DetachedCriteria mainCourseDetachedCriteria = DetachedCriteria.forClass(MainCourseEntity.class);
        mainCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<MainCourseEntity> mainCourseEntities = globalService.getListByCriteriaQuery(mainCourseDetachedCriteria);
        List<ModuleEntity> moduleEntities = null;
        if(type != null){
            DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
            if(!type.equals(BelongToEnum.ALL.getIndex())){
                moduleDetachedCriteria.add(Restrictions.eq("type",type));
            }
            moduleDetachedCriteria.add(Restrictions.eq("s_state",Constant.STATE_UNLOCK));
            if(CollectionUtils.isNotEmpty(mainCourseEntities)){
                List<Integer> params = new ArrayList<Integer>();
                for (MainCourseEntity mainCourseEntity: mainCourseEntities) {
                    params.add(mainCourseEntity.getId());
                }
                moduleDetachedCriteria.add(Restrictions.in("mainCourseId",params));
            }
            moduleEntities = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        }

        List<MainCourseVo> mainSubCourseVos = new ArrayList<MainCourseVo>();
        if (CollectionUtils.isNotEmpty(mainCourseEntities)) {
            for (MainCourseEntity mainCourseEntity : mainCourseEntities) {
                MainCourseVo mainCourseVo = new MainCourseVo();
                mainCourseVo.setId("M" + mainCourseEntity.getId());
                mainCourseVo.setName(mainCourseEntity.getName());
                mainCourseVo.setState_s(mainCourseEntity.getState());
                DetachedCriteria mainSubCourseVoDetachedCriteria = DetachedCriteria.forClass(SubCourseEntity.class);
                mainSubCourseVoDetachedCriteria.add(Restrictions.eq("fid", mainCourseEntity.getId()));
                mainSubCourseVoDetachedCriteria.addOrder(Order.asc("orderNo"));
                if(!type.equals(BelongToEnum.ALL.getIndex())){
                    if(CollectionUtils.isNotEmpty(moduleEntities)){
                        List<Integer> params = new ArrayList<Integer>();
                        for (ModuleEntity moduleEntity: moduleEntities) {
                            params.add(moduleEntity.getSubCourseId());
                        }
                        mainSubCourseVoDetachedCriteria.add(Restrictions.in("id",params));
                    }else{
                        mainSubCourseVoDetachedCriteria.add(Restrictions.eq("id",-1));
                    }
                }
                List<SubCourseEntity> subCourseEntityList = globalService.getListByCriteriaQuery(mainSubCourseVoDetachedCriteria);
                List<SubCourseVo> children = new ArrayList<SubCourseVo>();
                if (CollectionUtils.isNotEmpty(subCourseEntityList)) {
                    for (SubCourseEntity subCourseEntity : subCourseEntityList) {
                        SubCourseVo subCourseVo = new SubCourseVo();
                        subCourseVo.setId("S" + subCourseEntity.getId());
                        subCourseVo.setName(subCourseEntity.getSubName());
                        subCourseVo.setState_s(subCourseEntity.getIsTryOut());
                        children.add(subCourseVo);
                    }
                    mainCourseVo.setChildren(children);
                    mainSubCourseVos.add(mainCourseVo);
                }
            }
        }
        treeGrid.setRows(mainSubCourseVos);
        return treeGrid;
    }

    @RequestMapping(params = "chapterTreeGrid")
    @ResponseBody
    public TreeGrid chapterTreeGrid(HttpServletRequest request, HttpServletResponse response) {
        TreeGrid treeGrid = new TreeGrid();
        String subCourseId = "";
        String moduleType = "";
        try {
            subCourseId = request.getParameterMap().get("subCourseId")[0];
            moduleType = request.getParameterMap().get("moduleType")[0];
        } catch (Exception e) {
            return treeGrid;
        }
        List<ChapterVo> s = new ArrayList<ChapterVo>();
        try {
            if (StringUtils.hasText(subCourseId)) {
                Integer sid = Integer.parseInt(subCourseId.substring(1));
                SubCourseEntity subCourseEntity = globalService.get(SubCourseEntity.class, sid);
                ChapterVo vos = new ChapterVo(); //第一级 课程
                vos.setId(ConstantChapterLevel.SUBCOURSE + subCourseEntity.getId());
                vos.setName(subCourseEntity.getSubName());
                vos.setOrderNo(subCourseEntity.getOrderNo());
                vos.setLevel(ConstantChapterLevel.SUBCOURSE);
                DetachedCriteria chapterEntityADetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                chapterEntityADetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
                chapterEntityADetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.ONE));
                chapterEntityADetachedCriteria.add(Restrictions.eq("moduleType",Integer.parseInt(moduleType)));
                chapterEntityADetachedCriteria.addOrder(Order.asc("orderNo"));
                List<ChapterEntity> chapterEntitiesA = globalService.getListByCriteriaQuery(chapterEntityADetachedCriteria);
                if (CollectionUtils.isNotEmpty(chapterEntitiesA)) {
                    List<ChapterVo> aList = new ArrayList<ChapterVo>();
                    for (ChapterEntity chapterA : chapterEntitiesA) {
                        ChapterVo chapterVoA = new ChapterVo();
                        chapterVoA.setId(ConstantChapterLevel.ONE + chapterA.getId());
                        chapterVoA.setName(chapterA.getChapterName());
                        chapterVoA.setOrderNo(chapterA.getOrderNo());
                        chapterVoA.setLevel(ConstantChapterLevel.ONE);
                        DetachedCriteria chapterEntityBDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                        chapterEntityBDetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
                        chapterEntityBDetachedCriteria.add(Restrictions.eq("fid", chapterA.getId()));
                        chapterEntityBDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.TWO));
                        chapterEntityBDetachedCriteria.add(Restrictions.eq("moduleType",Integer.parseInt(moduleType)));
                        chapterEntityBDetachedCriteria.addOrder(Order.asc("orderNo"));
                        List<ChapterEntity> chapterEntitiesB = globalService.getListByCriteriaQuery(chapterEntityBDetachedCriteria);
                        if (CollectionUtils.isNotEmpty(chapterEntitiesB)) {
                            List<ChapterVo> bList = new ArrayList<ChapterVo>();
                            for (ChapterEntity chapterB : chapterEntitiesB) {
                                ChapterVo chapterVoB = new ChapterVo();
                                chapterVoB.setId(ConstantChapterLevel.TWO + chapterB.getId());
                                chapterVoB.setName(chapterB.getChapterName());
                                chapterVoB.setOrderNo(chapterB.getOrderNo());
                                chapterVoB.setLevel(ConstantChapterLevel.TWO);
                                DetachedCriteria chapterEntityCDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                                chapterEntityCDetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
                                chapterEntityCDetachedCriteria.add(Restrictions.eq("fid", chapterB.getId()));
                                chapterEntityCDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.THREE));
                                chapterEntityCDetachedCriteria.add(Restrictions.eq("moduleType",Integer.parseInt(moduleType)));
                                chapterEntityCDetachedCriteria.addOrder(Order.asc("orderNo"));
                                List<ChapterEntity> chapterEntitiesC = globalService.getListByCriteriaQuery(chapterEntityCDetachedCriteria);
                                if (CollectionUtils.isNotEmpty(chapterEntitiesC)) {
                                    List<ChapterVo> cList = new ArrayList<ChapterVo>();
                                    for (ChapterEntity chapterC : chapterEntitiesC) {
                                        ChapterVo chapterVoC = new ChapterVo();
                                        chapterVoC.setId(ConstantChapterLevel.THREE + chapterC.getId());
                                        chapterVoC.setName(chapterC.getChapterName());
                                        chapterVoC.setOrderNo(chapterC.getOrderNo());
                                        chapterVoC.setLevel(ConstantChapterLevel.THREE);
                                        cList.add(chapterVoC);
                                    }
                                    chapterVoB.setChildren(cList);
                                }
                                bList.add(chapterVoB);
                            }
                            chapterVoA.setChildren(bList);
                        }
                        aList.add(chapterVoA);
                    }
                    vos.setChildren(aList);
                }
                s.add(vos);
            }
        } catch (Exception e) {
            return treeGrid;
        }
        treeGrid.setRows(s);
        return treeGrid;
    }

}
