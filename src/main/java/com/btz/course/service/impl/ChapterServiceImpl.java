package com.btz.course.service.impl;

import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.exercise.vo.ChapterVo;
import com.btz.exercise.vo.SubCourseVo;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.system.global.GlobalService;
import com.btz.utils.BelongToEnum;
import com.btz.utils.ExerciseTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/8.
 */
@Service("chapterService")
@Transactional
public class ChapterServiceImpl extends BaseServiceImpl implements ChapterService {

    @Autowired
    private GlobalService globalService;

    public List<ExerciseExcelPojo> getExcelTemplet(SubCourseEntity subCourseEntity, Integer moduleType) {
        SubCourseVo subCourseVo = new SubCourseVo();
        subCourseVo.setSubCourseId(subCourseEntity.getId());
        subCourseVo.setSubName(subCourseEntity.getSubName());
        DetachedCriteria chapterEntityADetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterEntityADetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
        chapterEntityADetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.ONE));
        chapterEntityADetachedCriteria.add(Restrictions.eq("moduleType", moduleType));
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
                chapterEntityBDetachedCriteria.add(Restrictions.eq("moduleType", moduleType));
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
                        chapterEntityCDetachedCriteria.add(Restrictions.eq("moduleType", moduleType));
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

    public void downLoadExcel(List<ExerciseExcelPojo> exerciseExcelPojos, HttpServletResponse response, String excelName, BelongToEnum belongToEnum) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(excelName);

        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.YELLOW.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillBackgroundColor(HSSFColor.YELLOW.index);

        Row row = sheet.createRow(0);
        String [] columns ={"课程ID*","课程名称","章节ID*","章节名称","模块类型ID*","模块类型名称","题目类型ID*","题目类型","题干","内容","答案","解析","显示顺序"};
        int [] columnsColumnWidth ={2,3,2,3,2,3,2,3,7,7,1,10,2};
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
            cell.setCellValue(exerciseExcelPojo.getSubCourseId().toString());
            cell = row.createCell(1);
            cell.setCellValue(exerciseExcelPojo.getSubCourseName());
            cell = row.createCell(2);
            cell.setCellValue(exerciseExcelPojo.getChapterId().toString());
            cell = row.createCell(3);
            cell.setCellValue(exerciseExcelPojo.getChapterName());
            cell = row.createCell(4);
            cell.setCellValue(belongToEnum.getIndex()+"");
            cell = row.createCell(5);
            cell.setCellValue(belongToEnum.getTypeName());
            cell = row.createCell(6);
            cell.setCellValue(ExerciseTypeEnum.S_SELECTION.getIndex() + "");
            cell = row.createCell(7);
            cell.setCellValue(ExerciseTypeEnum.S_SELECTION.getTypeName());
            cell = row.createCell(8);
            cell.setCellValue("eg：会计是以货币为主要计量单位，核算和监督一个单位经济活动的一种（）。");
            cell = row.createCell(9);
            cell.setCellValue("A.方法<br/>B.手段<br/>C.信息工具<br/>D.经济管理工作");
            cell = row.createCell(10);
            cell.setCellValue("A");
            cell = row.createCell(11);
            cell.setCellValue("本题考核会计的概念。会计是以货币为主要计量单位，运用专门的方法，核算和监督一个单位经济活动的一种经济管理工作。");
            cell = row.createCell(12);
            cell.setCellValue(i + 1 + "");
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
