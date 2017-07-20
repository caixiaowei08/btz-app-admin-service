package api.btz.function.user.controller;

import api.btz.common.constant.SourceConstant;
import api.btz.common.json.ApiJson;
import api.btz.function.token.controller.ApiTokenController;
import api.btz.function.user.vo.ApiUserVo;
import api.btz.function.user.vo.CourseAuthorityVo;
import app.btz.common.constant.SfynConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.btz.admin.entity.AdminEntity;
import com.btz.user.entity.UserEntity;
import com.btz.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.PasswordUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 2017/7/20.
 */
@Scope("prototype")
@Controller
@RequestMapping("/api/userController")
public class ApiUserController extends BaseController {

    private static Logger logger = LogManager.getLogger(ApiUserController.class.getName());

    @Autowired
    private UserService userService;

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public ApiJson doAdd(@RequestBody ApiUserVo apiUserVo, HttpServletRequest request, HttpServletResponse response) {
        ApiJson j = new ApiJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("userId", apiUserVo.getUserId()));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(userEntityList)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("该账户已存在！");
            return j;
        }
        if (StringUtils.isEmpty(apiUserVo.getUserId())
                || StringUtils.isEmpty(apiUserVo.getUserPwd())) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("用户id和密码是必须输入项！");
            return j;
        }

        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(apiUserVo.getUserId());
            userEntity.setUserName(apiUserVo.getUserName());
            userEntity.setPhone(apiUserVo.getPhone());
            userEntity.setUserPwd(PasswordUtil.getMD5Encryp(apiUserVo.getUserPwd()));
            userEntity.setState(apiUserVo.getState() == null ? SfynConstant.SFYN_Y : apiUserVo.getState());
            userEntity.setSource(SourceConstant.SOURCE_WEB);
            userEntity.setAuthority(JSON.toJSONString(apiUserVo.getAuthority()));
            userEntity.setArea(apiUserVo.getArea());
            userEntity.setUpdateTime(new Date());
            userEntity.setCreateTime(new Date());
            userService.save(userEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("新增用户失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doGetUserInfoByUserId")
    @ResponseBody
    public ApiJson doGetUserInfoByUserId(ApiUserVo apiUserVo, HttpServletRequest request, HttpServletResponse response) {
        ApiJson j = new ApiJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("userId", apiUserVo.getUserId()));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("查询账户不存在！");
            return j;
        }
        UserEntity userEntity = userEntityList.get(0);
        apiUserVo.setUserId(userEntity.getUserId());
        apiUserVo.setUserName(userEntity.getUserName());
        apiUserVo.setPhone(userEntity.getPhone());
        apiUserVo.setState(userEntity.getState());
        apiUserVo.setArea(userEntity.getArea());
        apiUserVo.setAuthority(JSON.parseArray(userEntity.getAuthority(), CourseAuthorityVo.class));
        j.setSuccess(ApiJson.SUCCESS);
        j.setContent(apiUserVo);
        return j;
    }

    @RequestMapping(params = "doAddOrModifyCourseAuthorityByUserId")
    @ResponseBody
    public ApiJson doAddOrModifyCourseAuthorityByUserId(@RequestBody ApiUserVo apiUserVo, HttpServletRequest request, HttpServletResponse response) {
        ApiJson j = new ApiJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("userId", apiUserVo.getUserId()));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("用户账户不存在！");
            return j;
        }
        List<CourseAuthorityVo> courseAuthorityVoListReq = apiUserVo.getAuthority();
        if (CollectionUtils.isEmpty(courseAuthorityVoListReq)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("请输入需要修改或者新增的用户权限信息！");
            return j;
        }
        List<CourseAuthorityVo> courseAuthorityVoList = new ArrayList<CourseAuthorityVo>();
        UserEntity userEntity = userEntityList.get(0);
        if (StringUtils.hasText(userEntity.getAuthority())) {
            courseAuthorityVoList = JSON.parseArray(userEntity.getAuthority(), CourseAuthorityVo.class);
        }
        if (CollectionUtils.isNotEmpty(courseAuthorityVoList)) {
            for (CourseAuthorityVo courseAuthorityVo : courseAuthorityVoListReq) {
                if (courseAuthorityVoList.contains(courseAuthorityVo)) {
                    courseAuthorityVoList.remove(courseAuthorityVo);
                    courseAuthorityVoList.add(courseAuthorityVo);
                } else {
                    courseAuthorityVoList.add(courseAuthorityVo);
                }
            }
        }
        try {
            userEntity.setAuthority(JSON.toJSONString(courseAuthorityVoList));
            userEntity.setUpdateTime(new Date());
            userService.saveOrUpdate(userEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("用户课程权限修改失败!");
            return j;
        }
        j.setSuccess(ApiJson.SUCCESS);
        return j;
    }

    @RequestMapping(params = "doDelByUserId")
    @ResponseBody
    public ApiJson doDel(ApiUserVo apiUserVo, HttpServletRequest request, HttpServletResponse response) {
        ApiJson j = new ApiJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("userId", apiUserVo.getUserId()));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("userId对应用户不存在或已被删除！");
            return j;
        }
        UserEntity userEntity = userEntityList.get(0);
        try {
            userService.delete(userEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("删除失败！");
            return j;
        }
        j.setSuccess(ApiJson.SUCCESS);
        j.setMsg("删除成功！");
        return j;
    }

    @RequestMapping(params = "doUpdateByUserId")
    @ResponseBody
    public ApiJson doUpdateByUserId(@RequestBody ApiUserVo apiUserVo, HttpServletRequest request, HttpServletResponse response) {
        ApiJson j = new ApiJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("userId", apiUserVo.getUserId()));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("userId对应用户不存在或已被删除！");
            return j;
        }
        UserEntity userEntity = userEntityList.get(0);
        try {
            if (StringUtils.hasText(apiUserVo.getUserPwd())) {
                userEntity.setUserPwd(PasswordUtil.getMD5Encryp(apiUserVo.getUserPwd()));
            }
            if (StringUtils.hasText(apiUserVo.getUserName())) {
                userEntity.setUserName(apiUserVo.getUserName());
            }
            if (StringUtils.hasText(apiUserVo.getPhone())) {
                userEntity.setPhone(apiUserVo.getPhone());
            }
            if (apiUserVo.getState() != null
                    && apiUserVo.getState().intValue() > 0
                    && apiUserVo.getState().intValue() < 3) {
                userEntity.setState(apiUserVo.getState().intValue());
            }
            if(CollectionUtils.isNotEmpty(apiUserVo.getAuthority())){
                userEntity.setAuthority(JSON.toJSONString(apiUserVo.getAuthority()));
            }
            if(StringUtils.hasText(apiUserVo.getArea())){
                userEntity.setArea(apiUserVo.getArea());
            }
            userService.saveOrUpdate(userEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(ApiJson.FAIL);
            j.setMsg("删除失败！");
            return j;
        }
        j.setSuccess(ApiJson.SUCCESS);
        j.setMsg("删除成功！");
        return j;
    }


}
