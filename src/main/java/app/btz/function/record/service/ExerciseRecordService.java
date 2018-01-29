package app.btz.function.record.service;

import app.btz.common.ajax.AppAjax;
import app.btz.function.record.entity.ExerciseRecordEntity;
import app.btz.function.record.vo.ExerciseRecordVo;
import com.btz.token.entity.UserTokenEntity;
import org.framework.core.common.service.BaseService;

import java.util.List;

public interface ExerciseRecordService extends BaseService {

    /**
     * 通过token值获取app的用户信息
     */
    public UserTokenEntity doGetAppUserInfoByAppToken(String token);


    /**
     * 保存当个题目信息
     *
     * @param exerciseRecordEntity
     * @param token
     * @return
     */
    public AppAjax doSaveSingleQuestionRecordByAppTokenAndExerciseId(ExerciseRecordEntity exerciseRecordEntity, String token);


    /**
     * 保存题目信息
     *
     * @param exerciseRecordEntity
     * @param userTokenEntity
     * @return
     */
    public AppAjax doSaveSingleQuestionRecordByUserInfoAndExerciseRecordEntity(ExerciseRecordEntity exerciseRecordEntity, UserTokenEntity userTokenEntity);


    /**
     * 通过用户ID和题目id获取题目信息
     *
     * @param exerciseRecordEntity
     * @return
     */
    public ExerciseRecordEntity doGetExerciseRecordEntitydByUserIdoAndExerciseId(ExerciseRecordEntity exerciseRecordEntity);

    /**
     * 批量保存
     */
    public AppAjax doBatchSaveQuestionRecordByAppTokenAndExerciseList(List<ExerciseRecordEntity> exerciseRecordEntity, String token);

    /**
     * 收藏题目
     */
    public AppAjax doSaveCollectQuestionRecordByAppTokenAndExerciseId(ExerciseRecordEntity exerciseRecordEntity, String token);


    /**
     * 根据token 课程id 模块编号获取做题存储记录
     */
    public List<ExerciseRecordEntity> doGetQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(ExerciseRecordVo exerciseRecordVo, UserTokenEntity userTokenEntity);


    /**
     * 根据token 课程id 模块编号获取做题存储记录
     */
    public List<ExerciseRecordEntity> doGetQuestionRecordListByTokenAndSubCourseIdAndModuleType(String token, Integer subCourseId, Integer moduleType);


    /**
     * 删除做题记录 实际不是删除而是将答案 做题状态 分数重置
     *
     * @param exerciseRecordVo
     * @return
     */
    public AppAjax doClearAllQuestionRecordListByAppTokenAndSubCourseIdAndModuleType(ExerciseRecordVo exerciseRecordVo);


}
