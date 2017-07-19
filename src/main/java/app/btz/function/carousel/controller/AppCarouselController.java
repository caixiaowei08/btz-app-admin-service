package app.btz.function.carousel.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.common.ajax.AppRequestHeader;
import app.btz.common.constant.SfynConstant;
import com.btz.course.entity.MainCourseEntity;
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

    @RequestMapping(params = "getCarousel")
    @ResponseBody
    public AppAjax getCarousel(HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria carouselDetachedCriteria = DetachedCriteria.forClass(CarouselEntity.class);
        carouselDetachedCriteria.add(Restrictions.eq("sfyn", SfynConstant.SFYN_Y));
        carouselDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<CarouselEntity> carouselEntityList = carouselService.getListByCriteriaQuery(carouselDetachedCriteria);
        j.setContent(carouselEntityList);
        return j;
    }


}
