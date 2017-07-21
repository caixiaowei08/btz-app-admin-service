package com.btz.exercise.controller;

import com.btz.course.entity.MainCourseEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.course.vo.MainCourseVo;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.exercise.utils.PoiExcelExerciseUtils;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.poi.service.DownTestModuleExcel;
import com.btz.system.global.GlobalService;
import com.btz.utils.BelongToEnum;
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
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.btz.utils.BelongToEnum.ALL;
import static com.btz.utils.BelongToEnum.CHAPTER;

/**
 * Created by User on 2017/6/12.
 */
@Scope("prototype")
@Controller
@RequestMapping("/exerciseController")
public class ExerciseController extends BaseController {

    private static Logger logger = LogManager.getLogger(ExerciseController.class.getName());

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private DownTestModuleExcel downTestModuleExcel;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String subCourseId = request.getParameter("subCourseId");
        String chapterId = request.getParameter("chapterId");
        if(!StringUtils.hasText(subCourseId)){
            DatagridJsonUtils.datagrid(response, new DataGridReturn(0,new ArrayList()));
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(ExerciseEntity.class, dataGrid, null);
        criteriaQuery.installCriteria();
        DetachedCriteria detachedCriteria = criteriaQuery.getDetachedCriteria();
        detachedCriteria.add(Restrictions.eq("subCourseId",Integer.parseInt(subCourseId.substring(1,subCourseId.length()))));
        if(StringUtils.hasText(chapterId)&&!chapterId.contains("S")){
            detachedCriteria.add(Restrictions.eq("chapterId",Integer.parseInt(chapterId.substring(1,chapterId.length()))));
        }else{
            SubCourseEntity subCourseEntity = globalService.get(SubCourseEntity.class, Integer.parseInt(subCourseId.substring(1,subCourseId.length())));
            List<ExerciseExcelPojo> exerciseExcelPojoList = chapterService.getExcelTemplet(subCourseEntity, CHAPTER.getIndex());
            if(CollectionUtils.isNotEmpty(exerciseExcelPojoList)){
                List<Integer> params = new ArrayList<Integer>();
                for (ExerciseExcelPojo exerciseExcelPojo: exerciseExcelPojoList ) {
                    params.add(exerciseExcelPojo.getChapterId());
                }
                detachedCriteria.add(Restrictions.in("chapterId",params));
            }
        }
        DataGridReturn dataGridReturn = exerciseService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, ExerciseEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "downLoadExcel")
    public void downLoadExcel(ExerciseExcelPojo exerciseExcelPojo, HttpServletRequest request, HttpServletResponse response) {
        Integer subCourseId = exerciseExcelPojo.getSubCourseId();
        Integer moduleType = exerciseExcelPojo.getBelongTo();
        BelongToEnum belongToEnum = BelongToEnum.getBelongToEnum(moduleType);
        if (belongToEnum == null) {
            downTestModuleExcel.downTestModuleExcel(null,request,response,"模块类型输入错误！",ALL);
            return;
        }
        SubCourseEntity subCourseEntity = globalService.get(SubCourseEntity.class, subCourseId);
        List<ExerciseExcelPojo> exerciseExcelPojoList = chapterService.getExcelTemplet(subCourseEntity, belongToEnum.getIndex());
        downTestModuleExcel.downTestModuleExcel(exerciseExcelPojoList,request,response,subCourseEntity.getSubName(),belongToEnum);
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
        List<ExerciseEntity> exerciseEntityList = null;
        String postfix = org.framework.core.utils.StringUtils.getPostfix(newFileName);
        try {
            if(StringUtils.hasText(postfix)&&PoiConstant.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)){
                exerciseEntityList = downTestModuleExcel.readXlsxToExerciseEntityList(filelocal);//PoiExcelExerciseUtils.readXls(filelocal);
            }else{
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("仅支持 Excel xlsx 2007文件！");
                return j;
            }
        } catch (IOException e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("文档异常，请确认文档格式正确性！");
            return j;
        }catch (BusinessException be){
            logger.error(be.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg(be.getMessage());
            return j;
        }finally {
            //删除临时文件
            if (filelocal.isFile() && filelocal.exists()) {
                filelocal.delete();
            }
        }

        try {
            if(CollectionUtils.isNotEmpty(exerciseEntityList)){
                for (int i = 0; i < exerciseEntityList.size(); i++) {
                    ExerciseEntity exerciseEntity = exerciseEntityList.get(i);
                    DetachedCriteria moduleCourseDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
                    moduleCourseDetachedCriteria.add(Restrictions.eq("subCourseId",exerciseEntity.getSubCourseId()));
                    moduleCourseDetachedCriteria.add(Restrictions.eq("type",exerciseEntity.getModuleType()));
                    List<ModuleEntity> moduleEntityList = globalService.getListByCriteriaQuery(moduleCourseDetachedCriteria);
                    exerciseEntity.setModuleId(moduleEntityList.get(0).getId());
                    exerciseEntity.setModuleType(moduleEntityList.get(0).getType());
                    exerciseEntity.setCreateTime(new Date());
                    exerciseEntity.setUpdateTime(new Date());
                }
                exerciseService.batchExerciseSave(exerciseEntityList);
            }else{
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

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(ExerciseEntity exerciseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            exerciseEntity.setUpdateTime(new Date());
            exerciseEntity.setCreateTime(new Date());
            moduleService.updateModuleEntityVersion(exerciseEntity);
            exerciseService.save(exerciseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("新增失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(ExerciseEntity exerciseEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    exerciseEntity = exerciseService.get(ExerciseEntity.class, Integer.parseInt(id_array[i]));
                    moduleService.updateModuleEntityVersion(exerciseEntity);
                    exerciseService.delete(exerciseEntity);
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("请选择需要删除的数据！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
        }
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(ExerciseEntity exerciseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = exerciseEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        ExerciseEntity exerciseDb = exerciseService.get(ExerciseEntity.class, id);
        if (exerciseDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该数据不存在！");
            return j;
        }
        j.setContent(exerciseDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(ExerciseEntity exerciseEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        ExerciseEntity t = exerciseService.get(ExerciseEntity.class, exerciseEntity.getId());
        try {
            exerciseEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(exerciseEntity, t);
            moduleService.updateModuleEntityVersion(exerciseEntity);
            exerciseService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }
}
