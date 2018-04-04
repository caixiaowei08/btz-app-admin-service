package app.btz.function.exercise.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.function.record.entity.ExerciseRecordEntity;
import app.btz.function.record.service.ExerciseRecordService;
import app.btz.function.testModule.service.AppTestModuleService;
import app.btz.function.testModule.vo.ExerciseVo;
import app.btz.function.testModule.vo.ListInfoVo;
import app.btz.function.testModule.vo.ModuleTestRequestVo;
import app.btz.function.testModule.vo.ModuleVo;
import com.btz.contants.QuestionType;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.token.entity.UserTokenEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.utils.StringUtils;
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
 * Created by User on 2017/7/21.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/appExerciseController")
public class AppExerciseController extends BaseController {
    private static Logger logger = LogManager.getLogger(AppExerciseController.class.getName());

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private AppTestModuleService appTestModuleService;

    @Autowired
    private ExerciseRecordService exerciseRecordService;


    /**
     * 不包含后台做题记录的数据
     * @param moduleTestRequestVo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "getModuleExerciseByCourseIdAndModuleType")
    @ResponseBody
    public AppAjax getModuleExerciseByCourseIdAndModuleType(ModuleTestRequestVo moduleTestRequestVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        Integer subCourseId = moduleTestRequestVo.getSubCourseId();
        Integer moduleType = moduleTestRequestVo.getModuleType();
        if (subCourseId == null || moduleType == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请求参数不完整！");
            return j;
        }
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseId));
        moduleDetachedCriteria.add(Restrictions.eq("type", moduleType));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("模块已被删除！");
            return j;
        }
        List<ListInfoVo> listInfoVoList = appTestModuleService.getListInfoVoByModuleEntity(moduleEntityList.get(0));
        List<ExerciseVo> exerciseVoList = appTestModuleService.getExerciseVoListByListInfoVo(listInfoVoList);
        for (ExerciseVo exerciseVo : exerciseVoList) {
            QuestionType questionType = QuestionType.getExamTypeByExamType(exerciseVo.getType());
            if (questionType == null) {
                exerciseVo.setTypeName("未知题型");
                exerciseVo.setTypeShow(0);
            }
            exerciseVo.setTypeShow(questionType.getExamShow());
            exerciseVo.setTypeName(questionType.getExamName());
        }
        ModuleVo<ExerciseVo, ListInfoVo> moduleVo = new ModuleVo<ExerciseVo, ListInfoVo>();
        moduleVo.setVersion(moduleEntityList.get(0).getVersionNo());
        moduleVo.setExam(exerciseVoList);
        moduleVo.setList(listInfoVoList);
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(moduleVo);
        return j;
    }

    //包含做题记录的题目数据
    @RequestMapping(params = "getModuleExerciseByCourseIdAndModuleTypeAndTokenWithRecord")
    @ResponseBody
    public AppAjax getModuleExerciseByCourseIdAndModuleTypeWithRecord(ModuleTestRequestVo moduleTestRequestVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();

        Integer subCourseId = moduleTestRequestVo.getSubCourseId();
        Integer moduleType = moduleTestRequestVo.getModuleType();
        if (subCourseId == null || moduleType == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请求参数不完整！");
            return j;
        }
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseId));
        moduleDetachedCriteria.add(Restrictions.eq("type", moduleType));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("模块已被删除！");
            return j;
        }
        List<ListInfoVo> listInfoVoList = appTestModuleService.getListInfoVoByModuleEntity(moduleEntityList.get(0));
        List<ExerciseVo> exerciseVoList = appTestModuleService.getExerciseVoListByListInfoVo(listInfoVoList);
        for (ExerciseVo exerciseVo : exerciseVoList) {
            QuestionType questionType = QuestionType.getExamTypeByExamType(exerciseVo.getType());
            if (questionType == null) {
                exerciseVo.setTypeName("未知题型");
                exerciseVo.setTypeShow(0);
            }
            exerciseVo.setTypeShow(questionType.getExamShow());
            exerciseVo.setTypeName(questionType.getExamName());
        }


        //有token值
        if (StringUtils.hasText(moduleTestRequestVo.getToken())) {
            UserTokenEntity userTokenEntity = exerciseRecordService.doGetAppUserInfoByAppToken(moduleTestRequestVo.getToken());
            if (userTokenEntity == null) {
                j.setReturnCode(AppAjax.LOGNIN_INVALID);
                j.setMsg("登录失效，请重新登录！");
                return j;
            }
            //给题目合做题记录
            List<ExerciseRecordEntity> exerciseRecordEntityList = exerciseRecordService.doGetQuestionRecordListByTokenAndSubCourseIdAndModuleType(moduleTestRequestVo.getToken(), moduleTestRequestVo.getSubCourseId(), moduleTestRequestVo.getModuleType());
            if (CollectionUtils.isNotEmpty(exerciseRecordEntityList)) {
                for (ExerciseRecordEntity exerciseRecordEntity : exerciseRecordEntityList) {
                    for (ExerciseVo exerciseVo : exerciseVoList) {
                        if (exerciseRecordEntity.getExerciseId().equals(exerciseVo.getId())) {
                            exerciseVo.setSet(exerciseRecordEntity.getAnswer());
                            exerciseVo.setDone(exerciseRecordEntity.getCheckState());
                            exerciseVo.setGet(exerciseRecordEntity.getIsCollect());
                            exerciseVo.setSb(exerciseRecordEntity.getPoint());
                            break;
                        }
                    }
                }
            }

        }

        ModuleVo<ExerciseVo, ListInfoVo> moduleVo = new ModuleVo<ExerciseVo, ListInfoVo>();
        moduleVo.setVersion(moduleEntityList.get(0).getVersionNo());
        moduleVo.setExam(exerciseVoList);
        moduleVo.setList(listInfoVoList);
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(moduleVo);
        return j;
    }


    @RequestMapping(params = "doGetExerciseByExerciseId")
    @ResponseBody
    public AppAjax doGetExerciseByExerciseId(ExerciseEntity exerciseEntity, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        ExerciseEntity exerciseDb = exerciseService.get(ExerciseEntity.class, exerciseEntity.getId());
        if (exerciseDb == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("题目不存在或者已被删除！");
            return j;
        }
        ExerciseVo exerciseVo = new ExerciseVo();
        try {
            QuestionType questionType = QuestionType.getExamTypeByExamType(exerciseDb.getType());
            exerciseVo.setId(exerciseDb.getId());
            exerciseVo.setTypeName(questionType.getExamName());
            exerciseVo.setTypeShow(questionType.getExamShow());
            exerciseVo.setType(exerciseDb.getType());
            exerciseVo.setTitle(exerciseDb.getTitle());
            exerciseVo.setContent(exerciseDb.getContent());
            exerciseVo.setAnswer(exerciseDb.getAnswer());
            exerciseVo.setAnswerKey(exerciseDb.getAnswerKey());
            exerciseVo.setOrderNo(exerciseDb.getOrderNo());
        } catch (Exception e) {
            logger.error(e);
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("未能获取题目详细信息！");
            return j;
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(exerciseVo);
        return j;
    }


}
