package app.btz.function.record.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.function.record.entity.ExerciseRecordEntity;
import app.btz.function.record.service.ExerciseRecordService;
import app.btz.function.record.vo.ExerciseRecordVo;
import com.btz.token.entity.UserTokenEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.utils.StringUtils;
import org.framework.core.utils.TokenGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * app客户做题记录数据在线同步
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/exerciseRecordController")
public class ExerciseRecordController {

    private static Logger logger = LogManager.getLogger(ExerciseRecordController.class.getName());

    @Autowired
    private ExerciseRecordService exerciseRecordService;


    /**
     * 做题记录提交条件检验
     *
     * @param exerciseRecordVo
     * @return
     */
    private Boolean checkSaveQuestionRecordCondition(ExerciseRecordVo exerciseRecordVo) {
        /**
         *题目编号
         */
        Integer exerciseId = exerciseRecordVo.getExerciseId();
        if (exerciseId == null || exerciseId < 100000) {
            return false;
        }

        /**
         *课程编号
         */
        Integer subCourseId = exerciseRecordVo.getSubCourseId();
        if (subCourseId == null || subCourseId < 100000) {
            return false;
        }

        /**
         * 所属模块
         */
        Integer moduleType = exerciseRecordVo.getModuleType();
        if (moduleType == null) {
            return false;
        }

        if (moduleType.equals(1) ||//章节练习
                moduleType.equals(2) ||//核心考点
                moduleType.equals(4) ||//考前押题
                moduleType.equals(7)//历年真题
                ) {
            return true;
        }

        return false;
    }


    /**
     * 单个做题记录保存
     */
    @RequestMapping(params = "doSaveSingleQuestionRecordByAppTokenAndExerciseId")
    @ResponseBody
    public AppAjax doSaveSingleQuestionRecordByAppTokenAndExerciseId(@RequestBody ExerciseRecordVo exerciseRecordVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();

        //不满足提交条件 不能提交
        if (!checkSaveQuestionRecordCondition(exerciseRecordVo)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("做题记录保存失败!");
            return j;
        }

        //上传数据校验 不能让空数据替代原始数据
        try {
            if (StringUtils.hasText(exerciseRecordVo.getToken())) {
                ExerciseRecordEntity exerciseRecordEntity = new ExerciseRecordEntity();
                exerciseRecordEntity.setExerciseId(exerciseRecordVo.getExerciseId());
                exerciseRecordEntity.setModuleType(exerciseRecordVo.getModuleType());
                exerciseRecordEntity.setSubCourseId(exerciseRecordVo.getSubCourseId());

                if (StringUtils.hasText(exerciseRecordVo.getAnswer())) {
                    exerciseRecordEntity.setAnswer(exerciseRecordVo.getAnswer());
                } else {
                    exerciseRecordEntity.setAnswer(null);
                }
                //提交试题的时候 只提交收藏的试题
                if (exerciseRecordVo.getIsCollect() != null && exerciseRecordVo.getIsCollect() > 0) {
                    exerciseRecordEntity.setIsCollect(1);
                } else {
                    exerciseRecordEntity.setIsCollect(null);
                }

                if (exerciseRecordVo.getCheckState() != null && exerciseRecordVo.getCheckState() > 0) {
                    exerciseRecordEntity.setCheckState(exerciseRecordVo.getCheckState());
                } else {
                    exerciseRecordEntity.setCheckState(null);
                }

                if (exerciseRecordVo.getPoint() != null && exerciseRecordVo.getPoint() > 0) {
                    exerciseRecordEntity.setPoint(exerciseRecordVo.getPoint());
                } else {
                    exerciseRecordEntity.setPoint(null);
                }
                j = exerciseRecordService.doSaveSingleQuestionRecordByAppTokenAndExerciseId(exerciseRecordEntity, exerciseRecordVo.getToken());
            }
        } catch (Exception e) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("做题记录保存失败!");
            logger.error("save single error:" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量数据提交包括保存
     */
    @RequestMapping(params = "doBatchSaveQuestionRecordByAppTokenAndExerciseList")
    @ResponseBody
    public AppAjax doBatchSaveQuestionRecordByAppTokenAndExerciseList(List<ExerciseRecordEntity> exerciseRecordEntityList, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        try {
            if (CollectionUtils.isNotEmpty(exerciseRecordEntityList)) {
                for (ExerciseRecordEntity exerciseRecordEntity : exerciseRecordEntityList) {
                    if (exerciseRecordEntity.getIsCollect() == null || exerciseRecordEntity.getIsCollect() < 1) {//批量提交 防止丢失之后提交 覆盖已收藏的题目
                        exerciseRecordEntity.setIsCollect(null);
                    }
                    if (StringUtils.isEmpty(exerciseRecordEntity.getAnswer())) {//防止没填写答案覆盖  只有done>0 才提交 有答案可以覆盖
                        exerciseRecordEntity.setAnswer(null);
                        exerciseRecordEntity.setCheckState(null);
                    }
                }
            }
            j = exerciseRecordService.doBatchSaveQuestionRecordByAppTokenAndExerciseList(exerciseRecordEntityList, request.getParameter("token"));
        } catch (Exception e) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("做题记录保存失败!");
            logger.error("save single error:" + e.getMessage());
        }
        return j;
    }

    /**
     * 题目收藏
     */
    @RequestMapping(params = "doSaveCollectQuestionRecordByAppTokenAndExerciseId")
    @ResponseBody
    public AppAjax doSaveCollectQuestionRecordByAppTokenAndExerciseId(@RequestBody ExerciseRecordVo exerciseRecordVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();

        if (!checkSaveQuestionRecordCondition(exerciseRecordVo)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("题目收藏参数不全！");
            return j;
        }

        try {
            ExerciseRecordEntity exerciseRecordEntity = new ExerciseRecordEntity();
            exerciseRecordEntity.setSubCourseId(exerciseRecordEntity.getSubCourseId());
            exerciseRecordEntity.setExerciseId(exerciseRecordVo.getExerciseId());
            exerciseRecordEntity.setModuleType(exerciseRecordVo.getModuleType());
            exerciseRecordEntity.setIsCollect(exerciseRecordVo.getIsCollect());
            exerciseRecordEntity.setAnswer(null);
            exerciseRecordEntity.setCheckState(null);
            exerciseRecordEntity.setPoint(null);
            j = exerciseRecordService.doSaveCollectQuestionRecordByAppTokenAndExerciseId(exerciseRecordEntity, exerciseRecordVo.getToken());
        } catch (Exception e) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("题目收藏失败！");
            logger.error("题目收藏失败！:" + e.getMessage());
        }
        return j;
    }

    /**
     * 获取做题记录
     */
    @RequestMapping(params = "doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType")
    @ResponseBody
    public AppAjax doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(@RequestBody ExerciseRecordVo exerciseRecordVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        if (StringUtils.isEmpty(exerciseRecordVo.getToken())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("无用户信息！");
            return j;
        }

        UserTokenEntity userTokenEntity = exerciseRecordService.doGetAppUserInfoByAppToken(exerciseRecordVo.getToken());
        if (userTokenEntity == null) {
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录无效，请重新登录!");
            return j;
        }

        if (exerciseRecordVo.getSubCourseId() == null || exerciseRecordVo.getModuleType() == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请求参数无效!");
            return j;
        }

        try {
            List<ExerciseRecordEntity> exerciseRecordEntityList = exerciseRecordService.doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(exerciseRecordVo, userTokenEntity);
            if (CollectionUtils.isNotEmpty(exerciseRecordEntityList)) {
                j.setReturnCode(AppAjax.SUCCESS);
                j.setContent(exerciseRecordEntityList);
                return j;
            } else {
                j.setReturnCode(AppAjax.FAIL);
                j.setMsg("该模块无做题记录保存！");
                return j;
            }
        } catch (Exception e) {
            logger.error("获取做题记录失败：" + userTokenEntity.getUserId() + ":" + e.getMessage());
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("获取做题记录错误！");
            return j;
        }
    }

    /**
     * 清除某个模块的做题记录 重置做题记录
     */
    @RequestMapping(params = "doClearAllQuestionRecordListByAppTokenAndSubCourseIdAndModuleType")
    @ResponseBody
    public AppAjax doClearAllQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(@RequestBody ExerciseRecordVo exerciseRecordVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        if (exerciseRecordVo.getSubCourseId() == null ||
                exerciseRecordVo.getModuleType() == null ||
                StringUtils.isEmpty(exerciseRecordVo.getToken())
                ) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请求参数不全！");
            return j;
        }


        try {
            return exerciseRecordService.doClearAllQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(exerciseRecordVo);
        } catch (Exception e) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("清除做题记录错误！");
            logger.error("清除做题记录错误！:" + e.getMessage());
            return j;
        }
    }


}
