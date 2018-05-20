package app.btz.function.carousel.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.ajax.AppRequestHeader;
import app.btz.common.constant.SfynConstant;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.exercise.vo.SubCourseVo;
import com.btz.newsBulletin.carousel.entity.CarouselEntity;
import com.btz.newsBulletin.carousel.service.CarouselService;
import com.btz.utils.Constant;
import com.btz.video.live.vo.LiveVideoPojo;
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
 * Created by User on 2017/7/19.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/carouselController")
public class AppCarouselController extends BaseController {

    @Autowired
    private CarouselService carouselService;

    /**
     * 1、【过时 由于实现时 图片数据从数据库拉取 数据较大 性能问题】由getCarouselForFix替代
     * 2、为了维护老版本app 则保留该接口
     * @param carouselEntity
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @RequestMapping(params = "getCarousel")
    @ResponseBody
    public AppAjax getCarousel(CarouselEntity carouselEntity, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        List<CarouselEntity> carouselEntityList = new ArrayList<CarouselEntity>();
        j.setContent(carouselEntityList);
        return j;
    }


    /**
     * 优化版本 20180518
     * @param carouselEntity
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "getCarouselForNewVersion")
    @ResponseBody
    public AppAjax getCarouselForNewVersion(CarouselEntity carouselEntity, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria carouselDetachedCriteria = DetachedCriteria.forClass(CarouselEntity.class);
        carouselDetachedCriteria.add(Restrictions.eq("sfyn", SfynConstant.SFYN_Y));
        carouselDetachedCriteria.add(
                Restrictions.or(
                        Restrictions.eq("subCourseId",carouselEntity.getSubCourseId()),
                        Restrictions.eq("flag",2)
                )
        );
        carouselDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<CarouselEntity> carouselEntityList = carouselService.getListByCriteriaQuery(carouselDetachedCriteria);
        j.setContent(carouselEntityList);
        return j;
    }

}
