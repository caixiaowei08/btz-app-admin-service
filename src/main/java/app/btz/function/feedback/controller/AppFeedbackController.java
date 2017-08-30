package app.btz.function.feedback.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.constant.FeedbackConstant;
import app.btz.common.constant.NotesConstant;
import app.btz.common.service.AppTokenService;
import app.btz.function.feedback.vo.FeedbackVo;
import app.btz.function.notes.entity.NotesEntity;
import app.btz.function.notes.vo.NotesVo;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.course.service.SubCourseService;
import com.btz.exercise.controller.ExerciseController;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.feedback.entity.FeedbackEntity;
import com.btz.feedback.service.FeedbackService;
import com.btz.user.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by User on 2017/7/25.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/feedbackController")
public class AppFeedbackController extends BaseController {

    private static Logger logger = LogManager.getLogger(AppFeedbackController.class.getName());

    @Autowired
    private AppTokenService appTokenService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SubCourseService subCourseService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AppAjax doAdd(@RequestBody FeedbackVo feedbackVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        UserEntity userEntity = appTokenService.getUserEntityByToken(request);
        if (userEntity == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请先登录,再反馈问题！");
            return j;
        }
        if (StringUtils.isEmpty(feedbackVo.getContent())) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("反馈内容不能为空！");
            return j;
        }

        ExerciseEntity exerciseEntity = exerciseService.get(ExerciseEntity.class, feedbackVo.getExerciseId());
        if (exerciseEntity == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("题目已被删除！");
            return j;
        }
        try {
            SubCourseEntity subCourseEntity = subCourseService.get(SubCourseEntity.class, exerciseEntity.getSubCourseId());
            ChapterEntity chapterEntity = chapterService.get(ChapterEntity.class, exerciseEntity.getChapterId());
            FeedbackEntity feedbackEntity = new FeedbackEntity();
            feedbackEntity.setSubCourseId(exerciseEntity.getSubCourseId());
            feedbackEntity.setChapterId(exerciseEntity.getChapterId());
            feedbackEntity.setModuleId(exerciseEntity.getModuleId());
            feedbackEntity.setModuleType(exerciseEntity.getModuleType());
            feedbackEntity.setStatus(FeedbackConstant.PENDING);
            feedbackEntity.setExerciseId(exerciseEntity.getId());
            feedbackEntity.setResume("*课程：" + subCourseEntity.getSubName()
                    + "   *章节：" + chapterEntity.getChapterName()
                    + "   *题目：" + exerciseEntity.getTitle().substring(0, exerciseEntity.getTitle().length() > 10 ? 10 : exerciseEntity.getTitle().length())
            );

            feedbackEntity.setContent(feedbackVo.getContent());
            feedbackEntity.setUserId(userEntity.getId());
            feedbackEntity.setUserName(userEntity.getUserId());
            feedbackEntity.setCreateTime(new Date());
            feedbackEntity.setUpdateTime(new Date());
            feedbackService.save(feedbackEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("问题反馈失败！");
            return j;
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setMsg("问题反馈成功！");
        return j;
    }




}
