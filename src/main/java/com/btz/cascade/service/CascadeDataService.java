package com.btz.cascade.service;

import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.module.entity.ModuleEntity;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/8/27.
 */
public interface CascadeDataService extends BaseService {

    public void deleteExerciseDataBySubCourseId(SubCourseEntity subCourseEntity);

    public void deleteExerciseDataByChapterId(ChapterEntity chapterEntity);

    public void deleteExerciseDataByModuleId(ModuleEntity moduleEntity);

    public void deleteRecordedVideoDataBySubCourseId(SubCourseEntity subCourseEntity);

    public void deleteRecordedVideoDataByChapterId(ChapterEntity chapterEntity);

    public void deleteRecordedVideoDataByModuleId(ModuleEntity moduleEntity);

    public void deleteLiveVideoDataBySubCourseId(SubCourseEntity subCourseEntity);

    public void deleteLiveVideoDataByChapterId(ChapterEntity chapterEntity);

    public void deleteLiveVideoDataByModuleId(ModuleEntity moduleEntity);

    public void deleteChapterDataBySubCourseId(SubCourseEntity subCourseEntity);

    public void deleteChapterDataByChapterId(ChapterEntity chapterEntity);

    public void deleteChapterDataByModuleId(ModuleEntity moduleEntity);

    public void deleteModuleDataBySubCourseId(SubCourseEntity subCourseEntity);

    public void deleteModuleDataByMainCourseId(MainCourseEntity mainCourseEntity);

    public void deleteChapterCascadeDataByChapterId(ChapterEntity chapterEntity);

    public void deleteChapterClassDataByChapterId(ChapterEntity chapterEntity);


    public void deleteSubCourseBySubCourseId(SubCourseEntity subCourseEntity);

    public void deleteMainCourseByMainCourseId(MainCourseEntity mainCourseEntity);






}
