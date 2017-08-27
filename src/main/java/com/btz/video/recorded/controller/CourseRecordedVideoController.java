package com.btz.video.recorded.controller;

import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.module.entity.ModuleEntity;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.system.global.GlobalService;
import com.btz.utils.BelongToEnum;
import com.btz.video.poi.service.PoiVideoService;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.pojo.CoursePojo;
import com.btz.video.recorded.service.CourseRecordedVideoService;
import com.btz.video.recorded.vo.RecordedVideoPojo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.constant.PoiConstant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.common.system.BusinessException;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by User on 2017/7/14.
 */
@Scope("prototype")
@Controller
@RequestMapping("/admin/courseRecordedVideoController")
public class CourseRecordedVideoController extends BaseController {

    private static Logger logger = LogManager.getLogger(CourseRecordedVideoController.class.getName());

    @Autowired
    private CourseRecordedVideoService courseRecordedVideoService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PoiVideoService poiVideoService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String subCourseId = request.getParameter("subCourseId");
        String chapterId = request.getParameter("chapterId");
        if (StringUtils.isEmpty(subCourseId)) {
            DatagridJsonUtils.datagrid(response, new DataGridReturn(0, new ArrayList()));
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(CourseRecordedVideoEntity.class, dataGrid, null);
        criteriaQuery.installCriteria();
        DetachedCriteria detachedCriteria = criteriaQuery.getDetachedCriteria();
        detachedCriteria.add(Restrictions.eq("subCourseId", Integer.parseInt(subCourseId.substring(1, subCourseId.length()))));
        if (StringUtils.hasText(chapterId) && chapterId.contains("C")) {
            detachedCriteria.add(Restrictions.eq("chapterId", Integer.parseInt(chapterId.substring(1, chapterId.length()))));
        }
        detachedCriteria.addOrder(Order.asc("chapterId"));
        detachedCriteria.addOrder(Order.asc("orderNo"));
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

    /**
     * @param
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(CourseRecordedVideoEntity courseRecordedVideoEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        CourseRecordedVideoEntity t =
                courseRecordedVideoService.get(CourseRecordedVideoEntity.class, courseRecordedVideoEntity.getId());
        try {
            courseRecordedVideoEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(courseRecordedVideoEntity, t);
            courseRecordedVideoService.saveOrUpdate(t);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("编辑失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(CourseRecordedVideoEntity courseRecordedVideoEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    courseRecordedVideoEntity = courseRecordedVideoService.get(CourseRecordedVideoEntity.class, Integer.parseInt(id_array[i]));
                    if (courseRecordedVideoEntity != null) {
                        courseRecordedVideoService.delete(courseRecordedVideoEntity);
                    }
                }
            } else {
                logger.error("未输入需要删除的数据ID！");
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未输入需要删除的数据ID！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(CourseRecordedVideoEntity courseRecordedVideoEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = courseRecordedVideoEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的数据ID！");
            return j;
        }

        CourseRecordedVideoEntity courseRecordedVideoDb = courseRecordedVideoService.get(CourseRecordedVideoEntity.class, id);
        if (courseRecordedVideoDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该视频信息不存在或者被删除！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(courseRecordedVideoDb);
        return j;
    }

    @RequestMapping(params = "downLoadExcel")
    public void downLoadExcel(CourseRecordedVideoEntity courseRecordedVideoEntity, HttpServletRequest request, HttpServletResponse response) {
        SubCourseEntity subCourseEntity = globalService.get(SubCourseEntity.class, courseRecordedVideoEntity.getSubCourseId());
        CoursePojo coursePojo = courseRecordedVideoService.getExcelTemplet(subCourseEntity);
        poiVideoService.downLoadCourseRecordedVideoExcel(coursePojo, request, response, subCourseEntity.getSubName());
    }

    @RequestMapping(params = "uploadExcel")
    @ResponseBody
    public AjaxJson uploadExcel(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {
        AjaxJson j = new AjaxJson();
        String realPath = request.getSession().getServletContext().getRealPath("/files/upload/loanData");
        String newFileName = UUID.randomUUID() + file.getOriginalFilename();
        File filelocal = new File(realPath, newFileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(), filelocal);
        if (file == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择上传文件！");
            return j;
        }
        List<CourseRecordedVideoEntity> courseRecordedVideoEntityList = null;
        String postfix = org.framework.core.utils.StringUtils.getPostfix(newFileName);
        try {
            if (StringUtils.hasText(postfix) && PoiConstant.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                courseRecordedVideoEntityList = courseRecordedVideoService.readXlsxToCourseRecordedVideoEntity(filelocal);
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("仅支持 Excel xlsx 2007文件！");
                return j;
            }
        } catch (IOException e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("文档异常，请确认文档格式正确性！");
            return j;
        } catch (BusinessException be) {
            logger.error(be.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg(be.getMessage());
            return j;
        } finally {
            if (filelocal.isFile() && filelocal.exists()) {
                filelocal.delete();
            }
        }

        try {
            if (CollectionUtils.isNotEmpty(courseRecordedVideoEntityList)) {
                courseRecordedVideoService.batchSave(courseRecordedVideoEntityList);
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("数据保存错误，请检查数据是否完成整！");
                return j;
            }
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据保存错误，请检查数据是否完成整！");
            return j;
        }
        return j;
    }


}
