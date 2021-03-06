package com.btz.video.poi.service;

import com.btz.video.live.vo.LiveVideoPojo;
import com.btz.video.recorded.pojo.CoursePojo;
import com.btz.video.recorded.vo.RecordedVideoPojo;
import org.framework.core.common.service.BaseService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 2017/7/15.
 */
public interface PoiVideoService extends BaseService {

    public void downLoadCourseRecordedVideoExcel(CoursePojo coursePojo,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 String excelFileName);

    public void downLoadCourseLiveVideoExcel(List<LiveVideoPojo> liveVideoPojoList,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 String excelFileName);


}
