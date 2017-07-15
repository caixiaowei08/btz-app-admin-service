package com.btz.video.recorded.controller;

import com.btz.course.entity.ChapterEntity;
import com.btz.course.service.ChapterService;
import com.btz.exercise.controller.ExerciseController;
import com.btz.system.global.GlobalService;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.service.CourseRecordedVideoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.DatagridJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by User on 2017/7/14.
 */
@Scope("prototype")
@Controller
@RequestMapping("/courseRecordedVideoController")
public class CourseRecordedVideoController extends BaseController {

    private static Logger logger = LogManager.getLogger(CourseRecordedVideoController.class.getName());

    @Autowired
    private CourseRecordedVideoService courseRecordedVideoService;

    @Autowired
    private ChapterService chapterService;

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

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(CourseRecordedVideoEntity courseRecordedVideoEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        ChapterEntity chapterEntity = chapterService.get(ChapterEntity.class, courseRecordedVideoEntity.getChapterId());
        if (chapterEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("视频章节不存在或者已被删除！");
            return j;
        }
        courseRecordedVideoEntity.setSubCourseId(chapterEntity.getCourseId());
        courseRecordedVideoEntity.setModuleId(chapterEntity.getModuleId());
        courseRecordedVideoEntity.setModuleType(chapterEntity.getModuleType());
        courseRecordedVideoEntity.setChapterId(chapterEntity.getId());
        courseRecordedVideoEntity.setUpdateTime(new Date());
        courseRecordedVideoEntity.setCreateTime(new Date());
        try {
            courseRecordedVideoService.save(courseRecordedVideoEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("新增失败！");
        }
        return j;
    }


}
