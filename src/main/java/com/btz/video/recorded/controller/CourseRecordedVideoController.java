package com.btz.video.recorded.controller;

import com.btz.course.entity.SubCourseEntity;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.system.global.GlobalService;
import com.btz.user.entity.UserEntity;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.service.CourseRecordedVideoService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.DatagridJsonUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.btz.utils.BelongToEnum.CHAPTER;

/**
 * Created by User on 2017/7/14.
 */
@Scope("prototype")
@Controller
@RequestMapping("/courseRecordedVideoController")
public class CourseRecordedVideoController extends BaseController {

    @Autowired
    private CourseRecordedVideoService courseRecordedVideoService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(CourseRecordedVideoEntity courseRecordedVideoEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(CourseRecordedVideoEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        DataGridReturn dataGridReturn = courseRecordedVideoService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, CourseRecordedVideoEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }




}
