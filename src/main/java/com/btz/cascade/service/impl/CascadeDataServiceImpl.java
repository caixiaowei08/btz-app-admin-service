package com.btz.cascade.service.impl;

import com.btz.cascade.service.CascadeDataService;
import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.course.service.MainCourseService;
import com.btz.course.service.SubCourseService;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.utils.BelongToEnum;
import com.btz.video.live.entity.CourseLiveVideoEntity;
import com.btz.video.live.service.CourseLiveVideoService;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.service.CourseRecordedVideoService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by User on 2017/8/27.
 */
@Service("cascadeDataService")
@Transactional
public class CascadeDataServiceImpl extends BaseServiceImpl implements CascadeDataService {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CourseRecordedVideoService courseRecordedVideoService;

    @Autowired
    private CourseLiveVideoService courseLiveVideoService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SubCourseService subCourseService;

    @Autowired
    private MainCourseService mainCourseService;


    public void deleteExerciseDataBySubCourseId(SubCourseEntity subCourseEntity) {
        DetachedCriteria exerciseEntityDetachedCriteria = DetachedCriteria.forClass(ExerciseEntity.class);
        exerciseEntityDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseEntity.getId()));
        List<ExerciseEntity> exerciseEntityList = exerciseService.getListByCriteriaQuery(exerciseEntityDetachedCriteria);
        if (CollectionUtils.isNotEmpty(exerciseEntityList)) {
            for (ExerciseEntity exerciseEntity : exerciseEntityList) {
                exerciseService.delete(exerciseEntity);
            }
        }
    }

    public void deleteExerciseDataByChapterId(ChapterEntity chapterEntity) {
        DetachedCriteria exerciseEntityDetachedCriteria = DetachedCriteria.forClass(ExerciseEntity.class);
        exerciseEntityDetachedCriteria.add(Restrictions.eq("chapterId", chapterEntity.getId()));
        List<ExerciseEntity> exerciseEntityList = exerciseService.getListByCriteriaQuery(exerciseEntityDetachedCriteria);
        if (CollectionUtils.isNotEmpty(exerciseEntityList)) {
            for (ExerciseEntity exerciseEntity : exerciseEntityList) {
                exerciseService.delete(exerciseEntity);
            }
        }
    }

    public void deleteExerciseDataByModuleId(ModuleEntity moduleEntity) {
        DetachedCriteria exerciseEntityDetachedCriteria = DetachedCriteria.forClass(ExerciseEntity.class);
        exerciseEntityDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        List<ExerciseEntity> exerciseEntityList = exerciseService.getListByCriteriaQuery(exerciseEntityDetachedCriteria);
        if (CollectionUtils.isNotEmpty(exerciseEntityList)) {
            for (ExerciseEntity exerciseEntity : exerciseEntityList) {
                exerciseService.delete(exerciseEntity);
            }
        }
    }

    public void deleteRecordedVideoDataBySubCourseId(SubCourseEntity subCourseEntity) {
        DetachedCriteria recordedVideoDetachedCriteria = DetachedCriteria.forClass(CourseRecordedVideoEntity.class);
        recordedVideoDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseEntity.getId()));
        List<CourseRecordedVideoEntity> courseRecordedVideoEntityList = courseRecordedVideoService.getListByCriteriaQuery(recordedVideoDetachedCriteria);
        if (CollectionUtils.isNotEmpty(courseRecordedVideoEntityList)) {
            for (CourseRecordedVideoEntity courseRecordedVideoEntity : courseRecordedVideoEntityList) {
                courseRecordedVideoService.delete(courseRecordedVideoEntity);
            }
        }
    }

    public void deleteRecordedVideoDataByChapterId(ChapterEntity chapterEntity) {
        DetachedCriteria recordedVideoDetachedCriteria = DetachedCriteria.forClass(CourseRecordedVideoEntity.class);
        recordedVideoDetachedCriteria.add(Restrictions.eq("chapterId", chapterEntity.getId()));
        List<CourseRecordedVideoEntity> courseRecordedVideoEntityList = courseRecordedVideoService.getListByCriteriaQuery(recordedVideoDetachedCriteria);
        if (CollectionUtils.isNotEmpty(courseRecordedVideoEntityList)) {
            for (CourseRecordedVideoEntity courseRecordedVideoEntity : courseRecordedVideoEntityList) {
                courseRecordedVideoService.delete(courseRecordedVideoEntity);
            }
        }

    }

    public void deleteRecordedVideoDataByModuleId(ModuleEntity moduleEntity) {
        DetachedCriteria recordedVideoDetachedCriteria = DetachedCriteria.forClass(CourseRecordedVideoEntity.class);
        recordedVideoDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        List<CourseRecordedVideoEntity> courseRecordedVideoEntityList = courseRecordedVideoService.getListByCriteriaQuery(recordedVideoDetachedCriteria);
        if (CollectionUtils.isNotEmpty(courseRecordedVideoEntityList)) {
            for (CourseRecordedVideoEntity courseRecordedVideoEntity : courseRecordedVideoEntityList) {
                courseRecordedVideoService.delete(courseRecordedVideoEntity);
            }
        }
    }

    public void deleteLiveVideoDataBySubCourseId(SubCourseEntity subCourseEntity) {
        DetachedCriteria liveVideoDetachedCriteria = DetachedCriteria.forClass(CourseLiveVideoEntity.class);
        liveVideoDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseEntity.getId()));
        List<CourseLiveVideoEntity> courseLiveVideoEntityList = courseLiveVideoService.getListByCriteriaQuery(liveVideoDetachedCriteria);
        if (CollectionUtils.isNotEmpty(courseLiveVideoEntityList)) {
            for (CourseLiveVideoEntity courseLiveVideoEntity : courseLiveVideoEntityList) {
                courseRecordedVideoService.delete(courseLiveVideoEntity);
            }
        }


    }

    public void deleteLiveVideoDataByChapterId(ChapterEntity chapterEntity) {
        DetachedCriteria liveVideoDetachedCriteria = DetachedCriteria.forClass(CourseLiveVideoEntity.class);
        liveVideoDetachedCriteria.add(Restrictions.eq("chapterId", chapterEntity.getId()));
        List<CourseLiveVideoEntity> courseLiveVideoEntityList = courseLiveVideoService.getListByCriteriaQuery(liveVideoDetachedCriteria);
        if (CollectionUtils.isNotEmpty(courseLiveVideoEntityList)) {
            for (CourseLiveVideoEntity courseLiveVideoEntity : courseLiveVideoEntityList) {
                courseRecordedVideoService.delete(courseLiveVideoEntity);
            }
        }
    }

    public void deleteLiveVideoDataByModuleId(ModuleEntity moduleEntity) {
        DetachedCriteria liveVideoDetachedCriteria = DetachedCriteria.forClass(CourseLiveVideoEntity.class);
        liveVideoDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        List<CourseLiveVideoEntity> courseLiveVideoEntityList = courseLiveVideoService.getListByCriteriaQuery(liveVideoDetachedCriteria);
        if (CollectionUtils.isNotEmpty(courseLiveVideoEntityList)) {
            for (CourseLiveVideoEntity courseLiveVideoEntity : courseLiveVideoEntityList) {
                courseLiveVideoService.delete(courseLiveVideoEntity);
            }
        }
    }

    public void deleteChapterDataBySubCourseId(SubCourseEntity subCourseEntity) {
        DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterDetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterEntity : chapterEntityList) {
                chapterService.delete(chapterEntity);
            }
        }
    }

    public void deleteModuleDataBySubCourseId(SubCourseEntity subCourseEntity) {
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("subCourseId", subCourseEntity.getId()));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isNotEmpty(moduleEntityList)) {
            for (ModuleEntity moduleEntity : moduleEntityList) {
                moduleService.delete(moduleEntity);
            }
        }
    }

    public void deleteModuleDataByMainCourseId(MainCourseEntity mainCourseEntity) {
        DetachedCriteria moduleDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
        moduleDetachedCriteria.add(Restrictions.eq("mainCourseId", mainCourseEntity.getId()));
        List<ModuleEntity> moduleEntityList = moduleService.getListByCriteriaQuery(moduleDetachedCriteria);
        if (CollectionUtils.isNotEmpty(moduleEntityList)) {
            for (ModuleEntity moduleEntity : moduleEntityList) {
                moduleService.delete(moduleEntity);
            }
        }
    }

    public void deleteChapterDataByChapterId(ChapterEntity chapterEntity) {
        DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterDetachedCriteria.add(Restrictions.eq("fid", chapterEntity.getId()));
        chapterDetachedCriteria.add(Restrictions.ne("level", ConstantChapterLevel.ONE));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterDb : chapterEntityList) {
                courseRecordedVideoService.delete(chapterDb);
            }
        }
    }

    public void deleteChapterDataByModuleId(ModuleEntity moduleEntity) {
        DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterEntity : chapterEntityList) {
                chapterService.delete(chapterEntity);
            }
        }
    }

    //单个章节关联信息删除
    public void deleteChapterCascadeDataByChapterId(ChapterEntity chapterEntity) {
        if (chapterEntity.getModuleType().equals(BelongToEnum.LIVE_VIDEO.getIndex())) {
            deleteLiveVideoDataByChapterId(chapterEntity);
        } else if (chapterEntity.getModuleType().equals(BelongToEnum.RECORDED_VIDEO.getIndex())) {
            deleteRecordedVideoDataByChapterId(chapterEntity);
        } else {
            deleteExerciseDataByChapterId(chapterEntity);//删除题目
        }
    }

    //章节层级信息递归删除
    public void deleteChapterClassDataByChapterId(ChapterEntity chapterEntity) {
        if (chapterEntity.getLevel().equals(ConstantChapterLevel.ONE)) {
            DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
            chapterDetachedCriteria.add(Restrictions.eq("fid", chapterEntity.getId()));
            chapterDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.TWO));
            List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
            if (CollectionUtils.isNotEmpty(chapterEntityList)) {
                for (ChapterEntity chapter : chapterEntityList) {
                    deleteChapterClassDataByChapterId(chapter);
                }
            }
        } else if (chapterEntity.getLevel().equals(ConstantChapterLevel.TWO)) {
            DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
            chapterDetachedCriteria.add(Restrictions.eq("fid", chapterEntity.getId()));
            chapterDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.THREE));
            List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
            if (CollectionUtils.isNotEmpty(chapterEntityList)) {
                for (ChapterEntity chapter : chapterEntityList) {
                    deleteChapterClassDataByChapterId(chapter);
                }
            }
        } else if (chapterEntity.getLevel().equals(ConstantChapterLevel.THREE)) {
            //do nothing
        } else {
            //do nothing
        }
        deleteChapterCascadeDataByChapterId(chapterEntity);//删除关联的数据
        chapterService.delete(chapterEntity);//删除自己
    }

    public void deleteSubCourseBySubCourseId(SubCourseEntity subCourseEntity) {
        //删除课程下面的模块
        deleteModuleDataBySubCourseId(subCourseEntity);

        //删除课程下面的所有章节
        deleteChapterDataBySubCourseId(subCourseEntity);

        //删除课程下面的试题
        deleteExerciseDataBySubCourseId(subCourseEntity);

        //删除课程下面的试题
        deleteExerciseDataBySubCourseId(subCourseEntity);

        //删除课程下面的所有录播视频
        deleteRecordedVideoDataBySubCourseId(subCourseEntity);

        //删除课程下面的所有在线直播视频
        deleteLiveVideoDataBySubCourseId(subCourseEntity);

        subCourseService.delete(subCourseEntity);

    }


    public void deleteMainCourseByMainCourseId(MainCourseEntity mainCourseEntity) {
        DetachedCriteria subCourseDetachedCriteria = DetachedCriteria.forClass(SubCourseEntity.class);
        subCourseDetachedCriteria.add(Restrictions.eq("fid", mainCourseEntity.getId()));
        List<SubCourseEntity> subCourseEntityList = chapterService.getListByCriteriaQuery(subCourseDetachedCriteria);
        if (CollectionUtils.isNotEmpty(subCourseEntityList)) {
            for (SubCourseEntity subCourseEntity : subCourseEntityList) {
                deleteSubCourseBySubCourseId(subCourseEntity);
            }
        }
        mainCourseService.delete(mainCourseEntity);
    }
}
