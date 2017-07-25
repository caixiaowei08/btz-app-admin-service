package app.btz.function.notes.controller;

import app.btz.common.constant.NotesConstant;
import app.btz.common.service.AppTokenService;
import app.btz.function.exercise.controller.AppExerciseController;
import app.btz.function.notes.entity.NotesEntity;
import app.btz.function.notes.entity.ThumbsUpEntity;
import app.btz.function.notes.service.NotesService;
import app.btz.function.notes.vo.NotesVo;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.system.global.GlobalService;
import com.btz.user.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
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
 * Created by User on 2017/7/22.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/appNotesController")
public class AppNotesController extends BaseController {

    private static Logger logger = LogManager.getLogger(AppExerciseController.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private NotesService notesService;

    @Autowired
    private ExerciseService exerciseService;


    @Autowired
    private AppTokenService appTokenService;

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(NotesVo notesVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = appTokenService.getUserEntityByToken(request);
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("登录失效！");
            return j;
        }
        if (StringUtils.isEmpty(notesVo.getNotes())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("笔记内容不能为空！");
            return j;
        }
        ExerciseEntity exerciseEntity = exerciseService.get(ExerciseEntity.class, notesVo.getExerciseId());
        if (exerciseEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("题目已被删除！");
            return j;
        }
        NotesEntity notesEntity = new NotesEntity();
        notesEntity.setSubCourseId(exerciseEntity.getSubCourseId());
        notesEntity.setChapterId(exerciseEntity.getChapterId());
        notesEntity.setModuleId(exerciseEntity.getModuleId());
        notesEntity.setModuleType(exerciseEntity.getModuleType());
        notesEntity.setExerciseId(exerciseEntity.getId());
        notesEntity.setNotes(notesVo.getNotes());
        notesEntity.setUserId(userEntity.getId());
        notesEntity.setThumbsUp(0);
        notesEntity.setUserName(userEntity.getUserId());
        notesEntity.setStatus(NotesConstant.PENDING);
        notesEntity.setCreateTime(new Date());
        notesEntity.setUpdateTime(new Date());
        try {
            notesService.save(notesEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("添加笔记失败！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("添加笔记成功！");
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(NotesVo notesVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = appTokenService.getUserEntityByToken(request);
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("登录失效！");
            return j;
        }
        NotesEntity notesEntity = notesService.get(NotesEntity.class,notesVo.getId());
        if (notesEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("题目已被删除！");
            return j;
        }

        if(!userEntity.getId().equals(notesEntity.getUserId())){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("用户只能删除自己的笔记！");
            return j;
        }
        try {
            notesService.delete(notesEntity);
        }catch (Exception e){
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除笔记失败！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("删除笔记成功！");
        return j;
    }

    @RequestMapping(params = "doGetAllNotesByExerciseId")
    @ResponseBody
    public AjaxJson doGetNotesByExerciseId(NotesVo notesVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NotesEntity.class);
        detachedCriteria.add(Restrictions.eq("status",NotesConstant.PASS));
        detachedCriteria.add(Restrictions.eq("exerciseId",notesVo.getExerciseId()));
        List<NotesEntity> notesEntityList = notesService.getListByCriteriaQuery(detachedCriteria);
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(notesEntityList);
        return j;
    }

    @RequestMapping(params = "doGetNotesByExerciseIdAndToken")
    @ResponseBody
    public AjaxJson doGetNotesByExerciseIdAndToken(NotesVo notesVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = appTokenService.getUserEntityByToken(request);
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("登录失效！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NotesEntity.class);
        detachedCriteria.add(Restrictions.eq("exerciseId",notesVo.getExerciseId()));
        detachedCriteria.add(Restrictions.eq("userId",userEntity.getId()));
        List<NotesEntity> notesEntityList = notesService.getListByCriteriaQuery(detachedCriteria);
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(notesEntityList);
        return j;
    }

    @RequestMapping(params = "doGetNotesByTokenAndSubCourseId")
    @ResponseBody
    public AjaxJson doGetNotesByToken(NotesVo notesVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = appTokenService.getUserEntityByToken(request);
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("登录失效！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NotesEntity.class);
        detachedCriteria.add(Restrictions.eq("subCourseId",notesVo.getSubCourseId()));
        detachedCriteria.add(Restrictions.eq("userId",userEntity.getId()));
        detachedCriteria.addOrder(Order.desc("createTime"));
        List<NotesEntity> notesEntityList = notesService.getListByCriteriaQuery(detachedCriteria);
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(notesEntityList);
        return j;
    }

    @RequestMapping(params = "doClickThumbsUp")
    @ResponseBody
    public AjaxJson doClickThumbsUp(NotesVo notesVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = appTokenService.getUserEntityByToken(request);
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("登录失效！");
            return j;
        }
        DetachedCriteria thumbsUpDetachedCriteria = DetachedCriteria.forClass(ThumbsUpEntity.class);
        thumbsUpDetachedCriteria.add(Restrictions.eq("notesId",notesVo.getId()));
        thumbsUpDetachedCriteria.add(Restrictions.eq("userId",userEntity.getId()));
        List<ThumbsUpEntity> thumbsUpEntityList = globalService.getListByCriteriaQuery(thumbsUpDetachedCriteria);



        NotesEntity notesEntity = notesService.get(NotesEntity.class,notesVo.getId());
        if(CollectionUtils.isNotEmpty(thumbsUpEntityList)){
            j.setContent(notesEntity.getThumbsUp());
            return j;
        }
        notesEntity.setThumbsUp(notesEntity.getThumbsUp()+1);
        ThumbsUpEntity thumbsUpEntity = new ThumbsUpEntity();
        thumbsUpEntity.setNotesId(notesVo.getId());
        thumbsUpEntity.setUserId(userEntity.getId());
        try {
            globalService.save(thumbsUpEntity);
            notesService.saveOrUpdate(notesEntity);
        }catch (Exception e){
            logger.error(e.fillInStackTrace());
        }
       j.setContent(notesEntity.getThumbsUp());
        return j;
    }




}
