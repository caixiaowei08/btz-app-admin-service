package com.btz.video.recorded.service;

import com.btz.course.entity.SubCourseEntity;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.vo.RecordedVideoPojo;
import org.framework.core.common.service.BaseService;
import org.framework.core.common.system.BusinessException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by User on 2017/7/14.
 */
public interface CourseRecordedVideoService extends BaseService{

   public List<RecordedVideoPojo> getExcelTemplet(SubCourseEntity subCourseEntity);

   public List<CourseRecordedVideoEntity>  readXlsxToCourseRecordedVideoEntity(File file) throws IOException, BusinessException;

}
