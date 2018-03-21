package app.btz.function.record.service.impl;


import app.btz.common.ajax.AppAjax;
import app.btz.function.record.controller.ExerciseRecordController;
import app.btz.function.record.entity.ExerciseRecordEntity;
import app.btz.function.record.service.ExerciseRecordService;
import app.btz.function.record.vo.ExerciseRecordVo;
import app.btz.function.user.service.AppUserService;
import com.alibaba.fastjson.JSON;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.system.global.GlobalService;
import com.btz.token.entity.UserTokenEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("exerciseRecordService")
@Transactional
public class ExerciseRecordServiceImpl extends BaseServiceImpl implements ExerciseRecordService {

    private static Logger logger = LogManager.getLogger(ExerciseRecordController.class.getName());

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private ExerciseService exerciseService;

    /**
     * 通过token信息获取用户信息
     * 用户主键
     *
     * @param token
     * @return
     */
    public UserTokenEntity doGetAppUserInfoByAppToken(String token) {
        if (StringUtils.hasText(token)) {
            return appUserService.checkUserToken(token);
        }
        return null;
    }

    /**
     * 保存单个的题目信息
     * 核心逻辑
     *
     * @param exerciseRecordEntity
     * @param userTokenEntity
     * @return
     */
    public AppAjax doSaveSingleQuestionRecordByUserInfoAndExerciseRecordEntity(ExerciseRecordEntity exerciseRecordEntity, UserTokenEntity userTokenEntity) {
        AppAjax j = new AppAjax();
        exerciseRecordEntity.setUserId(userTokenEntity.getUserId());
        ExerciseRecordEntity exerciseRecordEntityDb = doGetExerciseRecordEntitydByUserIdoAndExerciseId(exerciseRecordEntity);

        logger.info("save single record：" + JSON.toJSON(exerciseRecordEntity));

        /**
         * 判断是否有存储记录
         */
        if (exerciseRecordEntityDb == null) {//第一次保存
            ExerciseEntity exerciseEntity = exerciseService.get(ExerciseEntity.class, exerciseRecordEntity.getExerciseId());
            /**
             * 确保版本信息和模块信息正确 若是题目被删除只能通过app传递数据存储
             */
            if (exerciseEntity != null) {
                exerciseRecordEntity.setSubCourseId(exerciseEntity.getSubCourseId());
                exerciseRecordEntity.setModuleType(exerciseEntity.getModuleType());
            }

            if (StringUtils.isEmpty(exerciseRecordEntity.getAnswer())) {
                exerciseRecordEntity.setAnswer("");
            }

            if (exerciseRecordEntity.getIsCollect() != null) {
                if (exerciseRecordEntity.getIsCollect() > 0) {//设置收藏
                    exerciseRecordEntity.setIsCollect(1);
                } else {
                    exerciseRecordEntity.setIsCollect(0);//取消收藏
                }
            } else {
                exerciseRecordEntity.setIsCollect(0);//默认没有收藏
            }

            if (exerciseRecordEntity.getCheckState() == null) {
                exerciseRecordEntity.setCheckState(0d);//默认值
            }

            if (exerciseRecordEntity.getPoint() == null) {
                exerciseRecordEntity.setPoint(0d);//默认值
            }

            exerciseRecordEntity.setUpdateTime(new Date());
            exerciseRecordEntity.setCreateTime(new Date());
            globalService.save(exerciseRecordEntity);
        } else {
            //只要不是null 就覆盖
            if (StringUtils.hasText(exerciseRecordEntity.getAnswer())) {//有就可以覆盖
                exerciseRecordEntityDb.setAnswer(exerciseRecordEntity.getAnswer());
            }

            if (exerciseRecordEntity.getIsCollect() != null) {
                if (exerciseRecordEntity.getIsCollect() > 0) {
                    exerciseRecordEntityDb.setIsCollect(1);
                } else {
                    exerciseRecordEntityDb.setIsCollect(0);
                }
            }

            if (exerciseRecordEntity.getCheckState() != null) {
                exerciseRecordEntityDb.setCheckState(exerciseRecordEntity.getCheckState());
            }

            if (exerciseRecordEntity.getPoint() != null) {
                exerciseRecordEntityDb.setPoint(exerciseRecordEntity.getPoint());
            }
            exerciseRecordEntityDb.setUpdateTime(new Date());
            globalService.saveOrUpdate(exerciseRecordEntityDb);
        }
        return j;
    }


    /**
     * 保存单个题目信息
     *
     * @param exerciseRecordEntity
     * @param token
     * @return
     */
    public AppAjax doSaveSingleQuestionRecordByAppTokenAndExerciseId(ExerciseRecordEntity exerciseRecordEntity, String token) {
        AppAjax j = new AppAjax();
        /**
         * 获取用户信息进行验证
         */
        UserTokenEntity userTokenEntity = doGetAppUserInfoByAppToken(token);
        //判断登录信息是否有效
        if (userTokenEntity != null && userTokenEntity.getUserId() > 100000) {
            //单个保存逻辑
            return doSaveSingleQuestionRecordByUserInfoAndExerciseRecordEntity(exerciseRecordEntity, userTokenEntity);
        } else {
            //无效
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录失效，请重新登录！");
        }
        return j;

    }

    public ExerciseRecordEntity doGetExerciseRecordEntitydByUserIdoAndExerciseId(ExerciseRecordEntity exerciseRecordEntity) {
        DetachedCriteria exerciseRecordDetachedCriteria = DetachedCriteria.forClass(ExerciseRecordEntity.class);
        exerciseRecordDetachedCriteria.add(Restrictions.eq("userId", exerciseRecordEntity.getUserId()));
        exerciseRecordDetachedCriteria.add(Restrictions.eq("exerciseId", exerciseRecordEntity.getExerciseId()));
        List<ExerciseRecordEntity> exerciseRecordEntityList = globalService.getListByCriteriaQuery(exerciseRecordDetachedCriteria);
        if (CollectionUtils.isNotEmpty(exerciseRecordEntityList)) {
            return exerciseRecordEntityList.get(0);
        }
        return null;
    }

    public AppAjax doBatchSaveQuestionRecordByAppTokenAndExerciseList(List<ExerciseRecordEntity> exerciseRecordEntity, String token) {
        AppAjax j = new AppAjax();
        /**
         * 获取用户信息进行验证
         */
        UserTokenEntity userTokenEntity = doGetAppUserInfoByAppToken(token);
        //判断登录信息是否有效
        if (userTokenEntity != null && userTokenEntity.getUserId() > 100000) {
            //单个保存逻辑
            if (CollectionUtils.isNotEmpty(exerciseRecordEntity)) {
                for (ExerciseRecordEntity exerciseRecorditem : exerciseRecordEntity) {
                    doSaveSingleQuestionRecordByUserInfoAndExerciseRecordEntity(exerciseRecorditem, userTokenEntity);
                }
            }
        } else {
            //无效
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录失效，请重新登录！");
        }
        return j;
    }

    public AppAjax doSaveCollectQuestionRecordByAppTokenAndExerciseId(ExerciseRecordEntity exerciseRecordEntity, String token) {
        AppAjax j = new AppAjax();
        /**
         * 获取用户信息进行验证
         */
        UserTokenEntity userTokenEntity = doGetAppUserInfoByAppToken(token);
        //判断登录信息是否有效
        if (userTokenEntity != null && userTokenEntity.getUserId() > 100000) {
            //单个保存逻辑
            return doSaveSingleQuestionRecordByUserInfoAndExerciseRecordEntity(exerciseRecordEntity, userTokenEntity);
        } else {
            //无效
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录失效，请重新登录！");
        }
        return j;
    }

    public List<ExerciseRecordEntity> doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(ExerciseRecordVo exerciseRecordVo, UserTokenEntity userTokenEntity) {
        if (userTokenEntity != null) {
            DetachedCriteria exerciseRecordDetachedCriteria = DetachedCriteria.forClass(ExerciseRecordEntity.class);
            exerciseRecordDetachedCriteria.add(Restrictions.eq("userId", userTokenEntity.getUserId()));
            exerciseRecordDetachedCriteria.add(Restrictions.eq("subCourseId", exerciseRecordVo.getSubCourseId()));
            exerciseRecordDetachedCriteria.add(Restrictions.eq("moduleType", exerciseRecordVo.getModuleType()));
            return globalService.getListByCriteriaQuery(exerciseRecordDetachedCriteria);
        } else {
            return null;
        }
    }

    public AppAjax doClearAllQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(ExerciseRecordVo exerciseRecordVo) {
        AppAjax j = new AppAjax();
        UserTokenEntity userTokenEntity = doGetAppUserInfoByAppToken(exerciseRecordVo.getToken());
        //用户登录信息校验
        if (userTokenEntity == null || userTokenEntity.getUserId() < 100000) {
            j.setReturnCode(AppAjax.LOGNIN_INVALID);
            j.setMsg("登录失效，请重新登录！");
            return j;
        }
        logger.info("清空做题记录：userId" + userTokenEntity.getUserId());
        List<ExerciseRecordEntity> exerciseRecordEntityList = doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(exerciseRecordVo, userTokenEntity);
        if (CollectionUtils.isNotEmpty(exerciseRecordEntityList)) {
            for (ExerciseRecordEntity exerciseRecordEntity : exerciseRecordEntityList) {
                exerciseRecordEntity.setAnswer("");
                exerciseRecordEntity.setPoint(0d);
                exerciseRecordEntity.setCheckState(0d);
                exerciseRecordEntity.setUpdateTime(new Date());//更新时间
                globalService.saveOrUpdate(exerciseRecordEntity);
            }
        }
        return j;
    }

    public List<ExerciseRecordEntity> doGetQuestionRecordListByTokenAndSubCourseIdAndModuleType(String token, Integer subCourseId, Integer moduleType) {
        ExerciseRecordVo exerciseRecordVo = new ExerciseRecordVo();
        exerciseRecordVo.setToken(token);
        exerciseRecordVo.setSubCourseId(subCourseId);
        exerciseRecordVo.setModuleType(moduleType);

        UserTokenEntity userTokenEntity = doGetAppUserInfoByAppToken(exerciseRecordVo.getToken());
        //用户登录信息校验
        if (userTokenEntity == null || userTokenEntity.getUserId() < 100000) {
            return null;
        }
        return doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(exerciseRecordVo, userTokenEntity);
    }
}
