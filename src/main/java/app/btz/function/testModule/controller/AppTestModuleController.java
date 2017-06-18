package app.btz.function.testModule.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.ajax.AppRequestHeader;
import app.btz.function.testModule.service.AppTestModuleService;
import app.btz.function.testModule.vo.ExerciseVo;
import app.btz.function.testModule.vo.ListInfoVo;
import app.btz.function.testModule.vo.ModuleTestRequestVo;
import app.btz.function.testModule.vo.ModuleVo;
import com.btz.course.service.ChapterService;
import com.btz.exercise.service.ExerciseService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        ModuleVo<ExerciseVo,ListInfoVo> moduleVo = new ModuleVo<ExerciseVo,ListInfoVo>();
        moduleVo.setVersion(moduleEntities.get(0).getVersionNo());
        moduleVo.setExam(exerciseVoList);
        moduleVo.setList(listInfoVoList);
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(moduleVo);
        return j;
    }
}
