package com.btz.poi.service.impl;

import com.btz.contants.QuestionType;
import com.btz.exercise.entity.ExerciseEntity;
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
import org.framework.core.common.system.BusinessException;
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

    public void downTestModuleExcel(List<ExerciseExcelPojo> exerciseExcelPojoList,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    String excelFileName,
                                    BelongToEnum belongToEnum
    ) {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "课程ID*", "课程名称", "章节ID*", "章节名称",
                "模块类型ID*", "模块类型名称", "题目类型", "类型名称",
                "题干", "内容", "答案", "解析",
                "显示顺序"
        };
        int[] columnsColumnWidth = {
                2000, 4000, 2000, 4000,
                4000, 4000, 3000, 4000,
                6000, 10000, 10000, 10000,
                4000
        };
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
        Cell cell = null;
        Row row = sheet.createRow(0); //表头
        for (int i = 0; i < columnsColumnWidth.length; i++) {
            sheet.setColumnWidth(i, columnsColumnWidth[i]);
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headStyle);
        }
        //填充数据
        if (CollectionUtils.isNotEmpty(exerciseExcelPojoList)) {
            int rowNum = 1;
            for (int i = 0; i < exerciseExcelPojoList.size(); i++) {
                ExerciseExcelPojo exerciseExcelPojo = exerciseExcelPojoList.get(i);
                for (int j = 0; j < QuestionType.values().length; j++) {//系统支持的题型
                    row = sheet.createRow(rowNum);
                    cell = row.createCell(0);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(exerciseExcelPojo.getSubCourseId());
                    cell = row.createCell(1);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(exerciseExcelPojo.getSubCourseName());
                    cell = row.createCell(2);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(exerciseExcelPojo.getChapterId());
                    cell = row.createCell(3);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(exerciseExcelPojo.getChapterName());
                    cell = row.createCell(4);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(belongToEnum.getIndex());
                    cell = row.createCell(5);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(belongToEnum.getTypeName());
                    cell = row.createCell(6);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(QuestionType.values()[j].getCode());
                    cell = row.createCell(7);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(QuestionType.values()[j].getDesc());
                    cell = row.createCell(8);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------题干--------");
                    cell = row.createCell(9);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------内容-</br>----分割选项------");
                    cell = row.createCell(10);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------答案-</br>----分割选项------");
                    cell = row.createCell(11);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("--------解析-</br>----分割选项------");
                    cell = row.createCell(12);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(rowNum);
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

    public List<ExerciseEntity> readXlsxToExerciseEntityList(File file) throws IOException, BusinessException {
        FileInputStream excelXlsxFile = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelXlsxFile);
        ExerciseEntity exerciseEntity = null;
        List<ExerciseEntity> exerciseEntityList = new ArrayList<ExerciseEntity>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    XSSFCell subCourseId = xssfRow.getCell(0);
                    XSSFCell chapterId = xssfRow.getCell(2);
                    XSSFCell moduleType = xssfRow.getCell(4);
                    XSSFCell type = xssfRow.getCell(6);
                    XSSFCell title = xssfRow.getCell(8);
                    XSSFCell content = xssfRow.getCell(9);
                    XSSFCell answer = xssfRow.getCell(10);
                    XSSFCell answerKey = xssfRow.getCell(11);
                    XSSFCell orderNo = xssfRow.getCell(12);
                    exerciseEntity = new ExerciseEntity();
                    //subCourseIdValue
                    if (subCourseId == null) {
                        throw new BusinessException("第" + rowNum + "行课程ID错误，请核实！");
                    }
                    try {
                        Integer subCourseIdValue  = new Double(subCourseId.getNumericCellValue()).intValue();
                        exerciseEntity.setSubCourseId(subCourseIdValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行课程ID错误，请核实！");
                    }
                    //chapterIdValue
                    if (chapterId == null) {
                        throw new BusinessException("第" + rowNum + "行章节ID错误，请核实！");
                    }
                    try {
                        Integer chapterIdValue  = new Double(chapterId.getNumericCellValue()).intValue();
                        exerciseEntity.setChapterId(chapterIdValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行章节ID错误，请核实！");
                    }
                   //moduleType
                    if (moduleType == null) {
                        throw new BusinessException("第" + rowNum + "行模块ID错误，请核实！");
                    }
                    try {
                        Integer moduleTypeValue  = new Double(moduleType.getNumericCellValue()).intValue();
                        exerciseEntity.setModuleType(moduleTypeValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行模块ID错误，请核实！");
                    }
                    //type
                    if (type == null) {
                        throw new BusinessException("第" + rowNum + "行题目类型ID错误，请核实！");
                    }
                    try {
                        Integer typeValue  = new Double(type.getNumericCellValue()).intValue();
                        exerciseEntity.setType(typeValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行题目类型ID错误，请核实！");
                    }
                    //title
                    if (title == null) {
                        throw new BusinessException("第" + rowNum + "行题干错误，请核实！");
                    }

                    if(StringUtils.hasText(title.getStringCellValue())){
                        exerciseEntity.setTitle(title.getStringCellValue());
                    }else{
                        throw new BusinessException("第" + rowNum + "行题干不能为空，请核实！");
                    }
                    //orderNo
                    if (orderNo == null) {
                        throw new BusinessException("第" + rowNum + "行显示顺序错误，请核实！");
                    }
                    try {
                        Integer orderNoValue  = new Double(orderNo.getNumericCellValue()).intValue();
                        exerciseEntity.setOrderNo(orderNoValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行显示顺序错误，请核实！");
                    }
                    exerciseEntity.setContent(content.getStringCellValue());
                    exerciseEntity.setAnswer(answer.getStringCellValue());
                    exerciseEntity.setAnswerKey(answerKey.getStringCellValue());
                    exerciseEntityList.add(exerciseEntity);
                }
            }
        }
        return exerciseEntityList;
    }
}
