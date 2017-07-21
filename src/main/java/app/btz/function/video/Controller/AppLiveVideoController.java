package app.btz.function.video.Controller;


import app.btz.common.ajax.AppAjax;
import app.btz.common.constant.SfynConstant;
import app.btz.function.testModule.vo.ModuleTestRequestVo;
import app.btz.function.video.vo.ChapterLiveVideoVo;
import app.btz.function.video.vo.ItemLiveVideoVo;
import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.service.ChapterService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.BelongToEnum;
import com.btz.video.live.entity.CourseLiveVideoEntity;
import com.btz.video.live.service.CourseLiveVideoService;
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
@RequestMapping("/app/appLiveVideoController")
public class AppLiveVideoController extends BaseController {

    @Autowired
    private CourseLiveVideoService courseLiveVideoService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping(params = "getLiveVideoListBySubCourseId")
    @ResponseBody
    public AppAjax getLiveVideoListBySubCourseId(ModuleTestRequestVo moduleTestRequestVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", moduleTestRequestVo.getSubCourseId()));
        moduleDetachedCriteria.add(Restrictions.eq("type", BelongToEnum.LIVE_VIDEO.getIndex()));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("该课程暂无直播！");
            return j;
        }
        ModuleEntity moduleEntity = moduleEntityList.get(0);
        DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        chapterDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.ONE));
        chapterDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
        List<ChapterLiveVideoVo>  chapterLiveVideoVoList = new ArrayList<ChapterLiveVideoVo>();
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterEntity : chapterEntityList) {
                ChapterLiveVideoVo chapterLiveVideoVo = new ChapterLiveVideoVo();
                chapterLiveVideoVo.setId(chapterEntity.getId());
                chapterLiveVideoVo.setChapterName(chapterEntity.getChapterName());
                chapterLiveVideoVo.setOrderNo(chapterEntity.getOrderNo());
                DetachedCriteria courseLiveDetachedCriteria = DetachedCriteria.forClass(CourseLiveVideoEntity.class);
                courseLiveDetachedCriteria.add(Restrictions.eq("chapterId", chapterEntity.getId()));
                courseLiveDetachedCriteria.add(Restrictions.eq("status", SfynConstant.SFYN_Y));
                courseLiveDetachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.LIVE_VIDEO.getIndex()));
                courseLiveDetachedCriteria.addOrder(Order.asc("orderNo"));
                List<CourseLiveVideoEntity> courseLiveVideoEntityList = courseLiveVideoService.getListByCriteriaQuery(courseLiveDetachedCriteria);
                if(CollectionUtils.isNotEmpty(courseLiveVideoEntityList)){
                    for (CourseLiveVideoEntity courseLiveVideoEntity :courseLiveVideoEntityList) {
                        ItemLiveVideoVo itemLiveVideoVo = new ItemLiveVideoVo();
                        itemLiveVideoVo.setId(courseLiveVideoEntity.getId());
                        itemLiveVideoVo.setTitle(courseLiveVideoEntity.getTitle());
                        itemLiveVideoVo.setTeacherName(courseLiveVideoEntity.getTeacherName());
                        itemLiveVideoVo.setVideoUrl(courseLiveVideoEntity.getVideoUrl());
                        itemLiveVideoVo.setOrderNo(courseLiveVideoEntity.getOrderNo());
                        itemLiveVideoVo.setStatus(courseLiveVideoEntity.getStatus());
                        itemLiveVideoVo.setChapterId(chapterEntity.getId());
                        itemLiveVideoVo.setSubCourseId(chapterEntity.getCourseId());
                        chapterLiveVideoVo.getChildren().add(itemLiveVideoVo);
                    }
                }
                chapterLiveVideoVoList.add(chapterLiveVideoVo);
            }
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(chapterLiveVideoVoList);
        return j;
    }


}
