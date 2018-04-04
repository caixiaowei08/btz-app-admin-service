package com.btz.video.live.service;

import com.btz.course.entity.SubCourseEntity;
import com.btz.video.live.entity.CourseLiveVideoEntity;
import com.btz.video.live.vo.LiveVideoPojo;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import org.framework.core.common.service.BaseService;
import org.framework.core.common.system.BusinessException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by User on 2017/7/14.
 */
public interface CourseLiveVideoService extends BaseService {

    public List<LiveVideoPojo> getExcelTemplet(SubCourseEntity subCourseEntity);

    public List<CourseLiveVideoEntity>  readXlsxToCourseLiveVideoEntity(File file) throws IOException, BusinessException;


}
