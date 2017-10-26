package app.btz.function.course.controller;

import api.btz.function.user.controller.ApiUserController;
import app.btz.common.ajax.AppAjax;
import app.btz.common.authority.AuthorityPojo;
import app.btz.common.authority.CourseAuthorityPojo;
import app.btz.common.service.AppTokenService;
import app.btz.function.testModule.vo.MainCourseAppVo;
import app.btz.function.testModule.vo.SubCourseAppVo;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.MainCourseService;
import com.btz.course.service.SubCourseService;
import com.btz.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.utils.TokenGeneratorUtil;
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
import java.util.*;

/**
 * Created by User on 2017/7/20.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/appSubCourseController")
public class AppSubCourseController extends BaseController {

    private static Logger logger = LogManager.getLogger(ApiUserController.class.getName());

    @Autowired
    private AppTokenService appTokenService;

    @Autowired
    private MainCourseService mainCourseService;

    @Autowired
    private SubCourseService subCourseService;

    @RequestMapping(params = "getTouristCourseInfo")
    @ResponseBody
    public AppAjax getTouristCourseInfo(HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria mainCourseDetachedCriteria = DetachedCriteria.forClass(MainCourseEntity.class);
        mainCourseDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_UNLOCK));
        mainCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<MainCourseEntity> mainCourseEntityList = mainCourseService.getListByCriteriaQuery(mainCourseDetachedCriteria);
        List<MainCourseAppVo> mainCourseAppVoList = new ArrayList<MainCourseAppVo>();
        if (CollectionUtils.isNotEmpty(mainCourseEntityList)) {
            for (MainCourseEntity mainCourseEntity : mainCourseEntityList) {
                MainCourseAppVo mainCourseAppVo = new MainCourseAppVo();
                mainCourseAppVo.setMainCourseId(mainCourseEntity.getId());
                mainCourseAppVo.setMainCourseAppName(mainCourseEntity.getName());
                mainCourseAppVo.setState(mainCourseEntity.getState());
                mainCourseAppVo.setOrderNo(mainCourseEntity.getOrderNo());
                mainCourseAppVoList.add(mainCourseAppVo);
            }
        }
        if (CollectionUtils.isNotEmpty(mainCourseAppVoList)) {
            for (MainCourseAppVo mainCourseAppVo : mainCourseAppVoList) {
                DetachedCriteria subCourseDetachedCriteria = DetachedCriteria.forClass(SubCourseEntity.class);
                subCourseDetachedCriteria.add(Restrictions.eq("fid", mainCourseAppVo.getMainCourseId()));
                subCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
                List<SubCourseEntity> subCourseEntityList = subCourseService.getListByCriteriaQuery(subCourseDetachedCriteria);
                List<SubCourseAppVo> subCourseAppVoList = new ArrayList<SubCourseAppVo>();
                if (CollectionUtils.isNotEmpty(subCourseEntityList)) {
                    for (SubCourseEntity subCourseEntity : subCourseEntityList) {
                        SubCourseAppVo subCourseAppVo = new SubCourseAppVo();
                        subCourseAppVo.setSubCourseId(subCourseEntity.getId());
                        subCourseAppVo.setSubCourseName(subCourseEntity.getSubName());
                        subCourseAppVo.setOrderNo(subCourseEntity.getOrderNo());
                        subCourseAppVo.setTryOut(subCourseEntity.getIsTryOut());
                        subCourseAppVo.setExpirationTime(new Date(0));
                        subCourseAppVoList.add(subCourseAppVo);
                    }
                }
                mainCourseAppVo.setChildren(subCourseAppVoList);
            }
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(mainCourseAppVoList);
        return j;
    }

    @RequestMapping(params = "getCourseInfoByToken")
    @ResponseBody
    public AppAjax getCourseInfoByToken(HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        String token = request.getParameter(TokenGeneratorUtil.TOKEN_FLAG);
        if(StringUtils.isEmpty(token)){ //游客登录
          return getTouristCourseInfo(request,response);
        }
        AuthorityPojo authorityPojo = appTokenService.getAuthorityPojoByToken(request);
        if (authorityPojo == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("登录信息无效或者账户已被注销！");
            return j;
        }
        List<CourseAuthorityPojo> courseAuthorityPojoList = authorityPojo.getAuthority();
        if (CollectionUtils.isEmpty(courseAuthorityPojoList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("您未购买任何课程！");
            return j;
        }
        Map<Integer, MainCourseAppVo> mainCourseMap = new HashMap<Integer, MainCourseAppVo>();
        for (CourseAuthorityPojo courseAuthorityPojo : courseAuthorityPojoList) {
            SubCourseEntity subCourseEntity = subCourseService.get(SubCourseEntity.class, courseAuthorityPojo.getSubCourseId());
            if (subCourseEntity == null) {
                continue;
            }
            if (mainCourseMap.get(subCourseEntity.getFid()) == null) {
                MainCourseEntity mainCourseEntity = mainCourseService.get(MainCourseEntity.class, subCourseEntity.getFid());
                if (mainCourseEntity == null || mainCourseEntity.getState().equals(Constant.STATE_LOCK)) {
                    continue;
                }
                MainCourseAppVo mainCourseAppVo = new MainCourseAppVo();
                mainCourseAppVo.setOrderNo(mainCourseEntity.getOrderNo());
                mainCourseAppVo.setState(mainCourseEntity.getState());
                mainCourseAppVo.setMainCourseAppName(mainCourseEntity.getName());
                mainCourseAppVo.setMainCourseId(mainCourseEntity.getId());
                SubCourseAppVo subCourseAppVo = new SubCourseAppVo();
                subCourseAppVo.setSubCourseId(subCourseEntity.getId());
                subCourseAppVo.setSubCourseName(subCourseEntity.getSubName());
                subCourseAppVo.setOrderNo(subCourseEntity.getOrderNo());
                subCourseAppVo.setTryOut(subCourseEntity.getIsTryOut());
                subCourseAppVo.setExpirationTime(courseAuthorityPojo.getEndTime());
                subCourseAppVo.setExamAuth(courseAuthorityPojo.getExamAuth());
                subCourseAppVo.setVideoAuth(courseAuthorityPojo.getVideoAuth());
                subCourseAppVo.setVideoClass(courseAuthorityPojo.getVideoClass());
                mainCourseAppVo.getChildren().add(subCourseAppVo);
                mainCourseMap.put(mainCourseEntity.getId(), mainCourseAppVo);
            } else {
                MainCourseAppVo mainCourseAppVo = mainCourseMap.get(subCourseEntity.getFid());
                SubCourseAppVo subCourseAppVo = new SubCourseAppVo();
                subCourseAppVo.setSubCourseId(subCourseEntity.getId());
                subCourseAppVo.setSubCourseName(subCourseEntity.getSubName());
                subCourseAppVo.setOrderNo(subCourseEntity.getOrderNo());
                subCourseAppVo.setTryOut(subCourseEntity.getIsTryOut());
                subCourseAppVo.setExpirationTime(courseAuthorityPojo.getEndTime());
                subCourseAppVo.setExamAuth(courseAuthorityPojo.getExamAuth());
                subCourseAppVo.setVideoAuth(courseAuthorityPojo.getVideoAuth());
                subCourseAppVo.setVideoClass(courseAuthorityPojo.getVideoClass());
                mainCourseAppVo.getChildren().add(subCourseAppVo);
            }
        }
        List<MainCourseAppVo> mainCourseAppVoList = new ArrayList<MainCourseAppVo>(mainCourseMap.values());
        Collections.sort(mainCourseAppVoList);
        for (MainCourseAppVo mainCourseAppVo : mainCourseAppVoList) {
            Collections.sort(mainCourseAppVo.getChildren());
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(mainCourseAppVoList);
        return j;
    }

}
