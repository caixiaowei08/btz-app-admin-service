package app.btz.function.testModule.controller;

import api.btz.function.user.controller.ApiUserController;
import app.btz.common.ajax.AppAjax;
import app.btz.common.ajax.AppRequestHeader;
import app.btz.function.testModule.service.AppTestModuleService;
import app.btz.function.testModule.vo.*;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.MainCourseService;
import com.btz.course.service.SubCourseService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.Constant;
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
 * Created by User on 2017/6/17.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/testModuleController")
public class AppTestModuleController extends BaseController {

    private static Logger logger = LogManager.getLogger(ApiUserController.class.getName());

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private AppTestModuleService appTestModuleService;

    @Autowired
    private MainCourseService mainCourseService;

    @Autowired
    private SubCourseService subCourseService;

    @RequestMapping(params = "getAllCourseInfoByToken")
    @ResponseBody
    public AppAjax getModuleByToken(AppRequestHeader requestHeader, HttpServletRequest request, HttpServletResponse response) {
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


    @RequestMapping(params = "getFirstModuleByToken")
    @ResponseBody
    public AppAjax getFirstModuleByToken(AppRequestHeader requestHeader, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        DetachedCriteria subCourseDetachedCriteria = DetachedCriteria.forClass(SubCourseEntity.class);
        subCourseDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<SubCourseEntity> subCourseAppVoList = subCourseService.getListByCriteriaQuery(subCourseDetachedCriteria);
        SubCourseAppVo subCourseAppVo = new SubCourseAppVo();
        SubCourseEntity subCourseEntity = subCourseAppVoList.get(0);
        subCourseAppVo.setSubCourseId(subCourseEntity.getId());
        subCourseAppVo.setSubCourseName(subCourseEntity.getSubName());
        subCourseAppVo.setOrderNo(subCourseEntity.getOrderNo());
        subCourseAppVo.setTryOut(subCourseEntity.getIsTryOut());
        j.setContent(subCourseAppVo);
        return j;
    }




}
