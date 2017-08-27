package app.btz.function.strategy.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.function.strategy.service.AppStrategyService;
import app.btz.function.strategy.vo.ItemStrategyVo;
import app.btz.function.strategy.vo.MainStrategyVo;
import com.btz.strategy.entity.ItemStrategyEntity;
import com.btz.strategy.entity.MainStrategyEntity;
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
import java.util.List;

/**
 * Created by User on 2017/8/26.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/strategyController")
public class AppStrategyController extends BaseController {

    @Autowired
    private AppStrategyService appStrategyService;

    @RequestMapping(params = "doGetAllNotesByExerciseId")
    @ResponseBody
    public AppAjax doGetStrategyBySubCourseId(
            MainStrategyVo mainStrategyVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        MainStrategyVo t = new MainStrategyVo();
        if (mainStrategyVo.getSubCourseId() == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("参数错误，未输入课程编号！");
            return j;
        }
        DetachedCriteria mainStrategyEntityDetachedCriteria = DetachedCriteria.forClass(MainStrategyEntity.class);
        mainStrategyEntityDetachedCriteria.add(Restrictions.eq("subCourseId", mainStrategyVo.getSubCourseId()));
        List<MainStrategyEntity> mainStrategyEntityList = appStrategyService.getListByCriteriaQuery(mainStrategyEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(mainStrategyEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("系统未设置该课程的试题策略!");
            return j;
        }
        MainStrategyEntity mainStrategyEntity = mainStrategyEntityList.get(0);
        t.setTotalTime(mainStrategyEntity.getTotalTime());
        t.setTotalPoint(mainStrategyEntity.getTotalPoint());
        t.setSubCourseId(mainStrategyEntity.getSubCourseId());
        DetachedCriteria itemStrategyEntityDetachedCriteria = DetachedCriteria.forClass(ItemStrategyEntity.class);
        itemStrategyEntityDetachedCriteria.add(Restrictions.eq("subCourseId", mainStrategyVo.getSubCourseId()));
        itemStrategyEntityDetachedCriteria.addOrder(Order.asc("examType"));
        List<ItemStrategyEntity> itemStrategyEntityList = appStrategyService.getListByCriteriaQuery(itemStrategyEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(itemStrategyEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("系统未设置该课程试卷题目组合策略!");
            return j;
        }
        for (ItemStrategyEntity itemStrategyEntity : itemStrategyEntityList) {
            ItemStrategyVo itemStrategyVo = new ItemStrategyVo();
            itemStrategyVo.setSubCourseId(itemStrategyEntity.getSubCourseId());
            itemStrategyVo.setExamNo(itemStrategyEntity.getExamNo());
            itemStrategyVo.setExamType(itemStrategyEntity.getExamType());
            itemStrategyVo.setPoint(itemStrategyEntity.getPoint());
            t.getQuestions().add(itemStrategyVo);
        }
        j.setReturnCode(AppAjax.SUCCESS);
        j.setContent(t);
        return j;
    }
}
