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

    @RequestMapping(params = "getLiveVideoListBySubCourseId")
    @ResponseBody
    public AppAjax getLiveVideoListBySubCourseId(ModuleTestRequestVo moduleTestRequestVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria courseLiveDetachedCriteria = DetachedCriteria.forClass(CourseLiveVideoEntity.class);
        courseLiveDetachedCriteria.add(Restrictions.eq("subCourseId", moduleTestRequestVo.getSubCourseId()));
        courseLiveDetachedCriteria.add(Restrictions.eq("status", SfynConstant.SFYN_Y));
        courseLiveDetachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.LIVE_VIDEO.getIndex()));
        courseLiveDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<CourseLiveVideoEntity> courseLiveVideoEntityList = courseLiveVideoService.getListByCriteriaQuery(courseLiveDetachedCriteria);
        List<ItemLiveVideoVo> itemLiveVideoVoList = new ArrayList<ItemLiveVideoVo>();
        if (CollectionUtils.isNotEmpty(courseLiveVideoEntityList)) {
            for (CourseLiveVideoEntity courseLiveVideoEntity : courseLiveVideoEntityList) {
                ItemLiveVideoVo itemLiveVideoVo = new ItemLiveVideoVo();
                itemLiveVideoVo.setId(courseLiveVideoEntity.getId());
                itemLiveVideoVo.setTitle(courseLiveVideoEntity.getTitle());
                itemLiveVideoVo.setTeacherName(courseLiveVideoEntity.getTeacherName());
                itemLiveVideoVo.setVideoUrl(courseLiveVideoEntity.getVideoUrl());
                itemLiveVideoVo.setOrderNo(courseLiveVideoEntity.getOrderNo());
                itemLiveVideoVo.setStatus(courseLiveVideoEntity.getStatus());
                itemLiveVideoVo.setTryOut(courseLiveVideoEntity.getTryOut().intValue() == 1 ? true : false);
                itemLiveVideoVo.setSubCourseId(courseLiveVideoEntity.getSubCourseId());
                itemLiveVideoVoList.add(itemLiveVideoVo);
            }
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(itemLiveVideoVoList);
        return j;
    }
}
