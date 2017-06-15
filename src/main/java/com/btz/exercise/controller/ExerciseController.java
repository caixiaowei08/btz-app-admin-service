package com.btz.exercise.controller;

import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.exercise.utils.PoiExcelExerciseUtils;
import com.btz.exercise.vo.ChapterVo;
import com.btz.exercise.vo.SubCourseVo;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.system.global.GlobalService;
import com.btz.utils.ExerciseTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.framework.core.common.constant.PoiConstant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by User on 2017/6/12.
 */
@Scope("prototype")
@Controller
@RequestMapping("/exerciseController")
public class ExerciseController extends BaseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "downLoadExcel")
    public void downLoadExcel(ExerciseExcelPojo exerciseExcelPojo, HttpServletRequest request, HttpServletResponse response) {
        Integer subCourseId = exerciseExcelPojo.getSubCourseId();
        SubCourseEntity subCourseEntity = globalService.get(SubCourseEntity.class, subCourseId);
        List<ExerciseExcelPojo> exerciseExcelPojoList = getExcelTemplet(subCourseEntity);
        downLoadExcel(exerciseExcelPojoList,response,subCourseEntity.getSubName());
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

        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("文件格式有误，请选择正确文件上传！");
            return j;
        }


        List<ExerciseEntity> exerciseEntityList = null;
        String postfix = "xls";//org.framework.core.utils.StringUtils.getPostfix();
        try {
            if (StringUtils.hasText(postfix) && PoiConstant.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                exerciseEntityList = PoiExcelExerciseUtils.readXls(filelocal);
            } else if (StringUtils.hasText(postfix) && PoiConstant.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                // exerciseEntityList =  PoiExcelExerciseUtils.readXls(is);
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("上传文件必须是EXCEL文件！");
                return j;
            }
        } catch (IOException e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("文档格式不正确，请修正之后，再上传！");
            return j;
        }

        try {
            for (int i = 0; i < exerciseEntityList.size(); i++) {
                ExerciseEntity exerciseEntity = exerciseEntityList.get(i);
                exerciseEntity.setVersionNo(1);
                exerciseEntity.setSubCourseId(17);
                exerciseEntity.setChapterId(2);
                exerciseEntity.setType(1);
                exerciseEntity.setBelongTo(1);
                exerciseEntity.setCreateTime(new Date());
                exerciseEntity.setUpdateTime(new Date());
            }
        } catch (Exception e) {

        }
        exerciseService.batchSave(exerciseEntityList);
        return j;
    }


    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(ExerciseEntity exerciseEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            exerciseEntity.setUpdateTime(new Date());
            exerciseEntity.setCreateTime(new Date());
            exerciseService.save(exerciseEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
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
            exerciseService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }


    private List<ExerciseExcelPojo> getExcelTemplet(SubCourseEntity subCourseEntity){
        SubCourseVo subCourseVo = new SubCourseVo();
        subCourseVo.setSubCourseId(subCourseEntity.getId());
        subCourseVo.setSubName(subCourseEntity.getSubName());
        DetachedCriteria chapterEntityADetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterEntityADetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
        chapterEntityADetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.ONE));
        chapterEntityADetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntitiesA = globalService.getListByCriteriaQuery(chapterEntityADetachedCriteria);
        List<ExerciseExcelPojo> exerciseExcelPojoList = new ArrayList<ExerciseExcelPojo>();
        if (CollectionUtils.isNotEmpty(chapterEntitiesA)) {
            List<ChapterVo> aList = new ArrayList<ChapterVo>();
            for (ChapterEntity chapterA : chapterEntitiesA) {
                ChapterVo chapterVoA = new ChapterVo();
                chapterVoA.setChapterId(chapterA.getId());
                chapterVoA.setChapterName(chapterA.getChapterName());
                aList.add(chapterVoA);
                DetachedCriteria chapterEntityBDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                chapterEntityBDetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
                chapterEntityBDetachedCriteria.add(Restrictions.eq("fid", chapterA.getId()));
                chapterEntityBDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.TWO));
                chapterEntityBDetachedCriteria.addOrder(Order.asc("orderNo"));
                List<ChapterEntity> chapterEntitiesB = globalService.getListByCriteriaQuery(chapterEntityBDetachedCriteria);
                if (CollectionUtils.isNotEmpty(chapterEntitiesB)) {
                    List<ChapterVo> bList = new ArrayList<ChapterVo>();
                    chapterVoA.setChildren(bList);
                    for (ChapterEntity chapterB : chapterEntitiesB) {
                        ChapterVo chapterVoB = new ChapterVo();
                        chapterVoB.setChapterId(chapterB.getId());
                        chapterVoB.setChapterName(chapterB.getChapterName());
                        bList.add(chapterVoB);
                        DetachedCriteria chapterEntityCDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                        chapterEntityCDetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
                        chapterEntityCDetachedCriteria.add(Restrictions.eq("fid", chapterB.getId()));
                        chapterEntityCDetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.THREE));
                        chapterEntityCDetachedCriteria.addOrder(Order.asc("orderNo"));
                        List<ChapterEntity> chapterEntitiesC = globalService.getListByCriteriaQuery(chapterEntityCDetachedCriteria);
                        if (CollectionUtils.isNotEmpty(chapterEntitiesC)) {
                            List<ChapterVo> cList = new ArrayList<ChapterVo>();
                            chapterVoB.setChildren(cList);
                            for (ChapterEntity chapterC : chapterEntitiesC) {
                                ChapterVo chapterVoC = new ChapterVo();
                                chapterVoC.setChapterId(chapterC.getId());
                                chapterVoC.setChapterName(chapterC.getChapterName());
                                cList.add(chapterVoC);
                            }
                        }
                    }
                }
            }
            subCourseVo.setChapterVoList(aList);
            List<ChapterVo> chapterVoListA = subCourseVo.getChapterVoList();
            if (CollectionUtils.isNotEmpty(chapterVoListA)) {
                for (ChapterVo chapterVoA : chapterVoListA) {
                    List<ChapterVo> chapterVoListB = chapterVoA.getChildren();
                    if (CollectionUtils.isNotEmpty(chapterVoListB)) {
                        for (ChapterVo chapterVoB : chapterVoListB) {
                            List<ChapterVo> chapterVoListC = chapterVoB.getChildren();
                            if (CollectionUtils.isNotEmpty(chapterVoListC)) {
                                for (ChapterVo chapterVoC : chapterVoListC) {
                                    addExerciseExcelPojo(exerciseExcelPojoList, chapterVoC,subCourseVo);
                                }
                            } else {
                                addExerciseExcelPojo(exerciseExcelPojoList, chapterVoB,subCourseVo);
                            }
                        }
                    } else{
                        addExerciseExcelPojo(exerciseExcelPojoList, chapterVoA, subCourseVo);
                    }
                }
            }
        }
        return exerciseExcelPojoList;
    }

    private void addExerciseExcelPojo(List<ExerciseExcelPojo> exerciseExcelPojoList,
                                      ChapterVo chapterVo,
                                      SubCourseVo subCourseVo) {
        ExerciseExcelPojo exerciseExcelPojo = new ExerciseExcelPojo();
        exerciseExcelPojo.setSubCourseId(subCourseVo.getSubCourseId());
        exerciseExcelPojo.setSubCourseName(subCourseVo.getSubName());
        exerciseExcelPojo.setChapterId(chapterVo.getChapterId());
        exerciseExcelPojo.setChapterName(chapterVo.getChapterName());
        exerciseExcelPojoList.add(exerciseExcelPojo);
    }

    public void downLoadExcel(List<ExerciseExcelPojo> exerciseExcelPojos, HttpServletResponse response,String excelName) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(excelName);

        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.YELLOW.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillBackgroundColor(HSSFColor.YELLOW.index);

        Row row = sheet.createRow(0);
        String [] columns ={"课程ID*","课程名称","章节ID*","章节名称","题目类型ID*","题目类型","题干","内容","答案","解析","显示顺序"};
        int [] columnsColumnWidth ={2,5,2,5,3,5,7,7,2,7,2};
        Cell cell = null;
        for (int i = 0; i <columnsColumnWidth.length ; i++) {
            sheet.setColumnWidth(i,(columnsColumnWidth[i] * 1500));
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(style);
        }
        for (int i = 0; i < exerciseExcelPojos.size(); i++) {
            ExerciseExcelPojo exerciseExcelPojo = exerciseExcelPojos.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(exerciseExcelPojo.getSubCourseId());
            cell = row.createCell(1);
            cell.setCellValue(exerciseExcelPojo.getSubCourseName());
            cell = row.createCell(2);
            cell.setCellValue(exerciseExcelPojo.getChapterId());
            cell = row.createCell(3);
            cell.setCellValue(exerciseExcelPojo.getChapterName());
            cell = row.createCell(4);
            cell.setCellValue(ExerciseTypeEnum.S_SELECTION.getIndex());
            cell = row.createCell(5);
            cell.setCellValue(ExerciseTypeEnum.S_SELECTION.getTypeName());
            cell = row.createCell(6);
            cell.setCellValue("eg：会计是以货币为主要计量单位，核算和监督一个单位经济活动的一种（）。");
            cell = row.createCell(7);
            cell.setCellValue("A.方法<br />B.手段<br />C.信息工具<br />D.经济管理工作");
            cell = row.createCell(8);
            cell.setCellValue("A");
            cell = row.createCell(9);
            cell.setCellValue("本题考核会计的概念。会计是以货币为主要计量单位，运用专门的方法，核算和监督一个单位经济活动的一种经济管理工作。");
            cell = row.createCell(10);
            cell.setCellValue("1");
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try{
            response.setHeader("Content-Disposition",
                    "attachment;filename="+new String(excelName.getBytes("utf-8"),"iso-8859-1").toString()+".xls");
        }catch (UnsupportedEncodingException e){

        }
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (Exception e) {

        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
        } finally {
            if (bis != null){
                try {
                    bis.close();
                }catch (IOException e){

                }
            }
            if (bos  != null){
                try {
                    bos .close();
                }catch (IOException e){

                }
            }
        }
    }
}
