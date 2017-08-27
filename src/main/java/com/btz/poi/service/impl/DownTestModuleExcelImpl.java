package com.btz.poi.service.impl;

import com.btz.contants.QuestionType;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.course.service.SubCourseService;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.pojo.ExcelExercisePojo;
import com.btz.module.entity.ModuleEntity;
import com.btz.module.service.ModuleService;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.poi.service.DownTestModuleExcel;
import com.btz.utils.BelongToEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFontFormatting;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.system.BusinessException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/7/3.
 */
@Service("downTestModuleExcel")
@Transactional
public class DownTestModuleExcelImpl implements DownTestModuleExcel {

    private static Logger logger = LogManager.getLogger(DownTestModuleExcelImpl.class.getName());

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SubCourseService subCourseService;

    @Autowired
    private ChapterService chapterService;

    public void downTestModuleExcel(SubCourseEntity subCourseEntity,
                                    List<ExerciseExcelPojo> exerciseExcelPojoList,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    String excelFileName,
                                    BelongToEnum belongToEnum
    ) {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "章节名称", "题目类型", "题干", "内容",
                "答案", "解析", "显示顺序"
        };
        int[] columnsColumnWidth = {
                10000, 10000, 10000, 10000,
                10000, 10000, 10000
        };
        //表头样式
        CellStyle headStyle = workBook.createCellStyle();
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setBorderTop(CellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderLeft(CellStyle.BORDER_THIN); // 左边边框
        headStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        headStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        headStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边边框颜色
        headStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headStyle.setWrapText(true);
        //课程模块样式
        CellStyle courseStyle = workBook.createCellStyle();
        courseStyle.setAlignment(CellStyle.ALIGN_CENTER);
        courseStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        courseStyle.setBorderTop(CellStyle.BORDER_THIN);
        courseStyle.setTopBorderColor(IndexedColors.RED.getIndex());
        courseStyle.setBorderLeft(CellStyle.BORDER_THIN); // 左边边框
        courseStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        courseStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        courseStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边边框颜色
        courseStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        courseStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        courseStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        courseStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        courseStyle.setWrapText(true);

        Cell cell = null;
        /**
         * 表头标题
         */
        Row row = sheet.createRow(0);
        for (int i = 0; i < columnsColumnWidth.length; i++) {
            sheet.setColumnWidth(i, columnsColumnWidth[i]);
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headStyle);
        }
        /**
         *题目类型 课程id 模块类型
         */
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellType(CellType.STRING);
        cell.setCellValue("课程编号：");
        cell.setCellStyle(courseStyle);

        cell = row.createCell(1);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(subCourseEntity.getId());
        cell.setCellStyle(courseStyle);

        cell = row.createCell(2);
        cell.setCellType(CellType.STRING);
        cell.setCellValue("模块编号：");
        cell.setCellStyle(courseStyle);

        cell = row.createCell(3);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(belongToEnum.getIndex());
        cell.setCellStyle(courseStyle);

        //填充数据
        if (CollectionUtils.isNotEmpty(exerciseExcelPojoList)) {
            int rowNum = 2;
            for (int i = 0; i < exerciseExcelPojoList.size(); i++) {
                ExerciseExcelPojo exerciseExcelPojo = exerciseExcelPojoList.get(i);
                for (int j = 0; j < QuestionType.values().length; j++) {
                    row = sheet.createRow(rowNum);
                    cell = row.createCell(0);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(exerciseExcelPojo.getChapterName().trim());
                    cell = row.createCell(1);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(QuestionType.values()[j].getExamName().trim());
                    cell = row.createCell(2);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------题干--------");
                    cell = row.createCell(3);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------内容-</br></br>-题目分割-<br/>-题目内容分割------");
                    cell = row.createCell(4);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------答案-</br></br>----小题答案分割-------");
                    cell = row.createCell(5);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------解析-</br></br>----小题解析分割------");
                    cell = row.createCell(6);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(rowNum - 1);
                    rowNum++;
                }
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workBook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((excelFileName + "-" + belongToEnum.getTypeName()).getBytes("utf-8"), "iso-8859-1").toString() + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.fillInStackTrace());
        }
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
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
            logger.error(e.fillInStackTrace());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error(e.fillInStackTrace());
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error(e.fillInStackTrace());
                }
            }
        }
    }

    public ExcelExercisePojo readXlsxToExerciseEntityList(File file) throws IOException, BusinessException {
        ExcelExercisePojo excelExercisePojo = new ExcelExercisePojo();
        FileInputStream excelXlsxFile = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelXlsxFile);
        ExerciseEntity exerciseEntity = null;
        List<ExerciseEntity> exerciseEntityList = new ArrayList<ExerciseEntity>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            XSSFRow xssfRow = xssfSheet.getRow(1);
            //课程信息解析
            XSSFCell subCourseId = xssfRow.getCell(1);
            XSSFCell moduleType = xssfRow.getCell(3);

            SubCourseEntity subCourseEntity = null;
            //获取课程信息
            try {
                subCourseEntity = subCourseService.get(SubCourseEntity.class, new Double(subCourseId.getNumericCellValue()).intValue());
            } catch (Exception e) {
                throw new BusinessException("课程编号有误，请核实！");
            }
            if (subCourseEntity == null) {
                throw new BusinessException("课程编号有误，请核实！");
            }
            excelExercisePojo.setSubCourseEntity(subCourseEntity);

            //模块信息
            List<ModuleEntity> moduleEntityList = null;
            try {
                DetachedCriteria moduleCourseDetachedCriteria = DetachedCriteria.forClass(ModuleEntity.class);
                moduleCourseDetachedCriteria.add(Restrictions.eq("subCourseId", new Double(subCourseId.getNumericCellValue()).intValue()));
                moduleCourseDetachedCriteria.add(Restrictions.eq("type", new Double(moduleType.getNumericCellValue()).intValue()));
                moduleEntityList = moduleService.getListByCriteriaQuery(moduleCourseDetachedCriteria);
            } catch (Exception e) {
                throw new BusinessException("模块类型有误，请核实！");
            }
            if (CollectionUtils.isEmpty(moduleEntityList)) {
                throw new BusinessException("模块类型有误，请核实！");
            }
            ModuleEntity moduleEntity = moduleEntityList.get(0);
            excelExercisePojo.setModuleEntity(moduleEntity);

            for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    XSSFCell chapterName = xssfRow.getCell(0);
                    XSSFCell examTypeName = xssfRow.getCell(1);
                    XSSFCell title = xssfRow.getCell(2);
                    XSSFCell content = xssfRow.getCell(3);
                    XSSFCell answer = xssfRow.getCell(4);
                    XSSFCell answerKey = xssfRow.getCell(5);
                    XSSFCell orderNo = xssfRow.getCell(6);

                    if (
                            (chapterName == null || StringUtils.isEmpty(chapterName.getStringCellValue().trim())) &&
                                    (examTypeName == null || StringUtils.isEmpty(examTypeName.getStringCellValue().trim())) &&
                                    (title == null || StringUtils.isEmpty(title.getStringCellValue().trim())) &&
                                    (content == null || StringUtils.isEmpty(content.getStringCellValue().trim())) &&
                                    (answer == null || StringUtils.isEmpty(answer.getStringCellValue().trim())) &&
                                    (answerKey == null || StringUtils.isEmpty(answerKey.getStringCellValue().trim())) &&
                                    (orderNo == null)
                            ) {
                        continue;
                    }

                    String chapterNameValue = null;
                    try {
                        chapterNameValue = chapterName == null ? "" : chapterName.getStringCellValue().trim();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，章节名称有误，请核实！");
                    }
                    String examTypeNameValue = null;
                    try {
                        examTypeNameValue = examTypeName == null ? "" : examTypeName.getStringCellValue().trim();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，题目类型有误，请核实！");
                    }

                    String titleValue = null;
                    try {
                        titleValue = title == null ? "" : title.getStringCellValue().trim();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，题干有误，请核实！");
                    }
                    if (StringUtils.isEmpty(titleValue)) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，题干不能为空，请核实！");
                    }
                    String contentValue = null;
                    try {
                        contentValue = content == null ? "" : content.getStringCellValue().trim();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，题目内容有误，请核实！");
                    }
                    String answerValue = null;
                    try {
                        answerValue = answer == null ? "" : answer.getStringCellValue().trim();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，答案有误，请核实！");
                    }
                    String answerKeyValue = null;
                    try {
                        answerKeyValue = answerKey == null ? "" : answerKey.getStringCellValue().trim();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，答案解析有误，请核实！");
                    }
                    Integer orderNoValue = null;
                    try {
                        orderNoValue = orderNo == null ? 0 : new Double(orderNo.getNumericCellValue()).intValue();
                    } catch (Exception e) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，题目显示序号有误，请核实！");
                    }
                    if (orderNoValue == null) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，题目显示序号不能为空，请核实！");
                    }
                    //获取章节信息
                    DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                    chapterDetachedCriteria.add(Restrictions.eq("chapterName", chapterNameValue));
                    chapterDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
                    List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
                    if (CollectionUtils.isEmpty(chapterEntityList)) {
                        throw new BusinessException("第" + (rowNum + 1) + "行，章节名称有误，请核实！");
                    }
                    ChapterEntity chapterEntity = chapterEntityList.get(0);
                    //根据题目类型名称获取题目类型
                    QuestionType questionType = QuestionType.getExamTypeByExamName(examTypeNameValue);

                    exerciseEntity = new ExerciseEntity();
                    exerciseEntity.setSubCourseId(chapterEntity.getCourseId());
                    exerciseEntity.setChapterId(chapterEntity.getId());
                    exerciseEntity.setType(questionType.getExamType());
                    exerciseEntity.setModuleId(chapterEntity.getModuleId());
                    exerciseEntity.setModuleType(chapterEntity.getModuleType());
                    exerciseEntity.setTitle(titleValue);
                    exerciseEntity.setContent(contentValue);
                    exerciseEntity.setAnswer(answerValue);
                    exerciseEntity.setAnswerKey(answerKeyValue);
                    exerciseEntity.setOrderNo(orderNoValue);
                    exerciseEntityList.add(exerciseEntity);
                }
            }
        }
        excelExercisePojo.setExerciseEntityList(exerciseEntityList);
        return excelExercisePojo;
    }
}
