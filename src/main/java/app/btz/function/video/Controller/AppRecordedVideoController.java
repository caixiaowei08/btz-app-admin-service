package app.btz.function.video.Controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.constant.SfynConstant;
import app.btz.common.constant.TryOutConstant;
import app.btz.function.testModule.vo.ModuleTestRequestVo;
import app.btz.function.video.vo.*;
import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.service.ChapterService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.BelongToEnum;
import com.btz.video.live.entity.CourseLiveVideoEntity;
import com.btz.video.live.service.CourseLiveVideoService;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.service.CourseRecordedVideoService;
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
 * Created by User on 2017/7/21.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/appRecordedVideoController")
public class AppRecordedVideoController extends BaseController {

    @Autowired
    private CourseRecordedVideoService courseRecordedVideoService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping(params = "getRecordedVideoListBySubCourseId")
    @ResponseBody
    public AppAjax getLiveVideoListBySubCourseId(ModuleTestRequestVo moduleTestRequestVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", moduleTestRequestVo.getSubCourseId()));
        moduleDetachedCriteria.add(Restrictions.eq("type", BelongToEnum.RECORDED_VIDEO.getIndex()));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("该课程暂无录播视频！");
            return j;
        }
        ModuleEntity moduleEntity = moduleEntityList.get(0);
        DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterDetachedCriteria.add(Restrictions.eq("courseId", moduleEntity.getSubCourseId()));
        chapterDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        chapterDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
        List<TimeRecordedVideoVo> timeRecordedVideoVoList = new ArrayList<TimeRecordedVideoVo>();
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterA : chapterEntityList) {
                if (chapterA.getLevel().equals(ConstantChapterLevel.ONE)) {
                    TimeRecordedVideoVo timeRecordedVideoVo = new TimeRecordedVideoVo();
                    timeRecordedVideoVo.setId(chapterA.getId());
                    timeRecordedVideoVo.setTime(chapterA.getChapterName());
                    for (ChapterEntity chapterB : chapterEntityList) {
                        if (chapterB.getLevel().equals(ConstantChapterLevel.TWO) &&
                                chapterA.getId().equals(chapterB.getFid())) {
                            int tryOut = TryOutConstant.APP_RECORDED_VIDEO_TRY_OUT;
                            TitleRecordedVideoVo titleRecordedVideoVo = new TitleRecordedVideoVo();
                            titleRecordedVideoVo.setTitle(chapterB.getChapterName());
                            titleRecordedVideoVo.setTeach(chapterB.getChapterName());
                            titleRecordedVideoVo.setChapterId(chapterB.getId());
                            timeRecordedVideoVo.getList().add(titleRecordedVideoVo);
                            for (ChapterEntity chapterC : chapterEntityList) {
                                if (chapterC.getLevel().equals(ConstantChapterLevel.THREE) &&
                                        chapterB.getId().equals(chapterC.getFid())) {
                                    ChapterRecordedVideoVo chapterRecordedVideoVo = new ChapterRecordedVideoVo();
                                    chapterRecordedVideoVo.setTitle(chapterC.getChapterName());
                                    DetachedCriteria courseRecordedDetachedCriteria = DetachedCriteria.forClass(CourseRecordedVideoEntity.class);
                                    courseRecordedDetachedCriteria.add(Restrictions.eq("chapterId", chapterC.getId()));
                                    courseRecordedDetachedCriteria.add(Restrictions.eq("moduleId", chapterC.getModuleId()));
                                    courseRecordedDetachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.RECORDED_VIDEO.getIndex()));
                                    courseRecordedDetachedCriteria.addOrder(Order.asc("orderNo"));
                                    List<CourseRecordedVideoEntity> courseRecordedVideoEntityList = courseRecordedVideoService.getListByCriteriaQuery(courseRecordedDetachedCriteria);
                                    if (CollectionUtils.isNotEmpty(courseRecordedVideoEntityList)) {
                                        for (CourseRecordedVideoEntity courseRecordedVideoEntity : courseRecordedVideoEntityList) {
                                            ItemRecordedVideoVo itemRecordedVideoVo = new ItemRecordedVideoVo();
                                            itemRecordedVideoVo.setId(courseRecordedVideoEntity.getId());
                                            itemRecordedVideoVo.setTitle(courseRecordedVideoEntity.getTitle());
                                            itemRecordedVideoVo.setVideoUrl(courseRecordedVideoEntity.getVideoUrl());
                                            itemRecordedVideoVo.setLectureUrl(courseRecordedVideoEntity.getLectureUrl());
                                            itemRecordedVideoVo.setOrderNo(courseRecordedVideoEntity.getOrderNo());
                                            itemRecordedVideoVo.setChapterId(chapterC.getId());
                                            itemRecordedVideoVo.setAuthId(chapterB.getId());
                                            itemRecordedVideoVo.setSubCourseId(chapterC.getCourseId());
                                            if (tryOut > 0) { //试用设置
                                                itemRecordedVideoVo.setTryOut(true);
                                                tryOut--;
                                            }
                                            chapterRecordedVideoVo.getList().add(itemRecordedVideoVo);
                                        }
                                    }
                                    titleRecordedVideoVo.getList().add(chapterRecordedVideoVo);
                                }
                            }
                        }
                    }
                    timeRecordedVideoVoList.add(timeRecordedVideoVo);
                }
            }
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(timeRecordedVideoVoList);
        return j;
    }
}
