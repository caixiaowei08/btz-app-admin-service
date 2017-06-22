package app.btz.function.testModule.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.ajax.AppRequestHeader;
import app.btz.function.testModule.service.AppTestModuleService;
import app.btz.function.testModule.vo.*;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.MainCourseService;
import com.btz.course.service.SubCourseService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
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
 * Created by User on 2017/6/17.
 */
@Scope("prototype")
@Controller
@RequestMapping("/appTestModuleController")
public class AppTestModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private AppTestModuleService appTestModuleService;

    @Autowired
    private MainCourseService mainCourseService;

    @Autowired
    private SubCourseService subCourseService;

    /**
     * 根据课程号subCourseId 和 模块类型id 以及token值获取层级信息
     */
    @RequestMapping(params = "getModuleByToken")
    @ResponseBody
    public AppAjax getModuleByToken(AppRequestHeader requestHeader, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        //TODO token这块是和用户权限相关 所以 在获取课程列表时 获取自己权限有的列表
        DetachedCriteria mainCourseDetachedCriteria = DetachedCriteria.forClass(MainCourseEntity.class);
        mainCourseDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_UNLOCK));
        mainCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<MainCourseEntity> mainCourseEntities = mainCourseService.getListByCriteriaQuery(mainCourseDetachedCriteria);
        List<MainCourseAppVo> mainCourseAppVoList = new ArrayList<MainCourseAppVo>();
        if (CollectionUtils.isNotEmpty(mainCourseEntities)) {
            for (MainCourseEntity mainCourseEntity : mainCourseEntities) {
                MainCourseAppVo mainCourseAppVo = new MainCourseAppVo();
                mainCourseAppVo.setMainCourseId(mainCourseEntity.getId());
                mainCourseAppVo.setMainCourseAppName(mainCourseEntity.getName());
                mainCourseAppVo.setState(mainCourseEntity.getState());
                mainCourseAppVo.setOrderNo(mainCourseEntity.getOrderNo());
                mainCourseAppVoList.add(mainCourseAppVo);
            }
        }

        if (CollectionUtils.isNotEmpty(mainCourseAppVoList)) {
            for (MainCourseAppVo mainCourseAppVo : mainCourseAppVoList) {
                DetachedCriteria subCourseDetachedCriteria = DetachedCriteria.forClass(SubCourseEntity.class);
                subCourseDetachedCriteria.add(Restrictions.eq("fid", mainCourseAppVo.getMainCourseId()));
                subCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
                List<SubCourseEntity> subCourseEntityList = subCourseService.getListByCriteriaQuery(subCourseDetachedCriteria);
                List<SubCourseAppVo> subCourseAppVoList = new ArrayList<SubCourseAppVo>();
                if (CollectionUtils.isNotEmpty(subCourseEntityList)) {
                    for (SubCourseEntity subCourseEntity : subCourseEntityList) {
                        SubCourseAppVo subCourseAppVo = new SubCourseAppVo();
                        subCourseAppVo.setSubCourseId(subCourseEntity.getId());
                        subCourseAppVo.setSubCourseName(subCourseEntity.getSubName());
                        subCourseAppVo.setOrderNo(subCourseEntity.getOrderNo());
                        subCourseAppVo.setTryOut(subCourseEntity.getIsTryOut());
                        subCourseAppVoList.add(subCourseAppVo);
                    }
                }
                mainCourseAppVo.setChildren(subCourseAppVoList);
            }
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(mainCourseAppVoList);
        return j;
    }


    /**
     * 根据课程号subCourseId 和 模块类型id 以及token值获取层级信息
     */
    @RequestMapping(params = "getModuleTest")
    @ResponseBody
    public AppAjax getModuleTest(ModuleTestRequestVo moduleTestRequestVo, AppRequestHeader requestHeader, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        Integer subCourseId = moduleTestRequestVo.getSubCourseId();
        Integer moduleType = moduleTestRequestVo.getModuleType();
        if (subCourseId == null || moduleType == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("缺少请求参数课程主键或者模块类型！");
            return j;
        }
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseId));
        moduleDetachedCriteria.add(Restrictions.eq("type", moduleType));
        List<ModuleEntity> moduleEntities = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntities)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("该模块不存在！");
            return j;
        }
        List<ListInfoVo> listInfoVoList = appTestModuleService.getListInfoVoByModuleEntity(moduleEntities.get(0));
        List<ExerciseVo> exerciseVoList = appTestModuleService.getExerciseVoListByListInfoVo(listInfoVoList);
        ModuleVo<ExerciseVo, ListInfoVo> moduleVo = new ModuleVo<ExerciseVo, ListInfoVo>();
        moduleVo.setVersion(moduleEntities.get(0).getVersionNo());
        moduleVo.setExam(exerciseVoList);
        moduleVo.setList(listInfoVoList);
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(moduleVo);
        return j;
    }
}
