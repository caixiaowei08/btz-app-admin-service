package api.btz.function.video.controller;

import api.btz.common.json.ApiJson;
import api.btz.function.video.json.ApiChapterJson;
import api.btz.function.video.json.ApiFaChapterJson;
import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.service.ChapterService;
import com.btz.utils.BelongToEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * Created by User on 2017/10/27.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api/videoController")
public class VideoController extends BaseController {

    private static Logger logger = LogManager.getLogger(VideoController.class.getName());

    @Autowired
    private ChapterService chapterService;

    @RequestMapping(params = "doGetChapterBySubCourseId")
    @ResponseBody
    public ApiJson doAdd(ApiChapterJson apiCharpterJson,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        ApiJson j = new ApiJson();
        if (apiCharpterJson.getSubCourseId() == null) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg(ApiJson.MSG_FAIL);
            j.setContent("请求参数错误，请输入课程编号！");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        detachedCriteria.add(Restrictions.eq("courseId", apiCharpterJson.getSubCourseId()));
        detachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.RECORDED_VIDEO.getIndex()));
        List<String> params = new ArrayList<String>();
        params.add(ConstantChapterLevel.ONE);
        params.add(ConstantChapterLevel.TWO);
        detachedCriteria.add(Restrictions.in("level", params));
        detachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntityList;
        try {
            chapterEntityList = chapterService.getListByCriteriaQuery(detachedCriteria);
        } catch (Exception e) {
            logger.error(e);
            j.setSuccess(ApiJson.FAIL);
            j.setMsg(ApiJson.MSG_FAIL);
            j.setContent("请求参数有误，请核实！");
            return j;
        }

        List<ApiFaChapterJson> apiFaChapterJsonList = new ArrayList<ApiFaChapterJson>();
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterEntity : chapterEntityList) {
                if (ConstantChapterLevel.ONE.equals(chapterEntity.getLevel())) {
                    ApiFaChapterJson apiFaChapterJson = new ApiFaChapterJson();
                    apiFaChapterJson.setChapterName(chapterEntity.getChapterName());
                    apiFaChapterJson.setSubCourseId(chapterEntity.getCourseId());
                    apiFaChapterJson.setChapterId(chapterEntity.getId());
                    apiFaChapterJsonList.add(apiFaChapterJson);
                }
            }
            if (CollectionUtils.isNotEmpty(apiFaChapterJsonList)) {
                for (ApiFaChapterJson apiFaChapterJson : apiFaChapterJsonList) {
                    for (ChapterEntity chapterEntity : chapterEntityList) {
                        if(apiFaChapterJson.getChapterId().equals(chapterEntity.getFid())){
                            ApiChapterJson apiCharpterJsonInfo = new ApiChapterJson();
                            apiCharpterJsonInfo.setChapterId(chapterEntity.getId());
                            apiCharpterJsonInfo.setChapterName(chapterEntity.getChapterName());
                            apiCharpterJsonInfo.setSubCourseId(chapterEntity.getCourseId());
                            apiFaChapterJson.getSubChapterList().add(apiCharpterJsonInfo);
                        }
                    }
                }
            }
        }
        j.setSuccess(ApiJson.SUCCESS);
        j.setMsg(ApiJson.MSG_SUCCESS);
        j.setContent(apiFaChapterJsonList);
        return j;
    }
}
