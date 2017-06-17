package com.btz.course.service;

import com.btz.course.entity.SubCourseEntity;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.utils.BelongToEnum;
import org.framework.core.common.service.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 2017/6/8.
 */
public interface ChapterService extends BaseService {

    public List<ExerciseExcelPojo> getExcelTemplet(SubCourseEntity subCourseEntity, Integer moduleType);

    public void downLoadExcel(List<ExerciseExcelPojo> exerciseExcelPojos, HttpServletResponse response, String excelName,BelongToEnum belongToEnum);
}
