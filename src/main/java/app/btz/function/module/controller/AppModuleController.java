package app.btz.function.module.controller;

import app.btz.common.ajax.AppAjax;
import app.btz.function.testModule.vo.ModuleTestRequestVo;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.BelongToEnum;
import com.btz.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 2017/7/21.
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/appModuleController")
public class AppModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    @RequestMapping(params = "getModuleBySubCourseIdAndModuleType")
    @ResponseBody
    public AppAjax getModuleBySubCourseIdAndModuleType(ModuleTestRequestVo moduleTestRequestVo, HttpServletRequest request, HttpServletResponse response) {
        AppAjax j = new AppAjax();
        Integer subCourseId = moduleTestRequestVo.getSubCourseId();
        Integer moduleType = moduleTestRequestVo.getModuleType();
        if (subCourseId == null || moduleType == null) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("请求参数不完整！");
            return j;
        }
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseId));
        moduleDetachedCriteria.add(Restrictions.eq("type", moduleType));
        moduleDetachedCriteria.add(Restrictions.eq("s_state", Constant.STATE_UNLOCK));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isEmpty(moduleEntityList)) {
            j.setReturnCode(AppAjax.FAIL);
            j.setMsg("模块被删除删除或者不存在！");
            return j;
        }
        ModuleEntity moduleEntity = moduleEntityList.get(0);
        if(StringUtils.isEmpty(moduleEntity.getAlias())){
            BelongToEnum belongToEnum = BelongToEnum.getBelongToEnum(moduleEntity.getType());
            moduleEntity.setAlias(belongToEnum.getTypeName());
        }

        j.setContent(moduleEntity);
        j.setReturnCode(AppAjax.SUCCESS);
        return j;
    }
}
