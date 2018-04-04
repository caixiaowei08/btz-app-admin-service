package com.btz.poi.service;

import com.btz.course.entity.SubCourseEntity;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.pojo.ExcelExercisePojo;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.utils.BelongToEnum;
import org.framework.core.common.system.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by User on 2017/7/3.
 */
public interface DownTestModuleExcel {

    public void downTestModuleExcel(SubCourseEntity subCourseEntity,
                                    List<ExerciseExcelPojo> exerciseExcelPojoList,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    String excelFileName,
                                    BelongToEnum belongToEnum
                                    );

    public ExcelExercisePojo readXlsxToExerciseEntityList(File file) throws IOException, BusinessException;

}
