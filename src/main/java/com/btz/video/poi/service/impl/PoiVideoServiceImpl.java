package com.btz.video.poi.service.impl;

import com.btz.contants.QuestionType;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.utils.BelongToEnum;
import com.btz.video.live.vo.LiveVideoPojo;
import com.btz.video.poi.service.PoiVideoService;
import com.btz.video.recorded.controller.CourseRecordedVideoController;
import com.btz.video.recorded.pojo.ChapterPojo;
import com.btz.video.recorded.pojo.ClassPojo;
import com.btz.video.recorded.pojo.CoursePojo;
import com.btz.video.recorded.pojo.YearPojo;
import com.btz.video.recorded.vo.RecordedVideoPojo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by User on 2017/7/15.
 */
@Service("poiVideoService")
@Transactional
public class PoiVideoServiceImpl extends BaseServiceImpl implements PoiVideoService {

    private static Logger logger = LogManager.getLogger(PoiVideoServiceImpl.class.getName());

    public void downLoadCourseRecordedVideoExcel(
            CoursePojo coursePojo,
            HttpServletRequest request,
            HttpServletResponse response,
            String excelFileName
    ) {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象

        String[] columns = {
                "章节名称", "视频名称", "视频播放链接", "视频讲义链接",
                "显示顺序", "", "",""
        };

        int[] columnsColumnWidth = {
                10000, 10000, 10000, 10000,
                10000, 10000, 10000, 10000,
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

        int count = 1;
        List<YearPojo> yearPojoList = coursePojo.getYears();
        if (CollectionUtils.isNotEmpty(yearPojoList)) {
            for (YearPojo yearPojo : yearPojoList) {
                List<ClassPojo> classPojoList = yearPojo.getClasses();
                if (CollectionUtils.isNotEmpty(classPojoList)) {
                    for (ClassPojo classPojo : classPojoList) {
                        List<ChapterPojo> chapterPojoList = classPojo.getChapters();
                        if (CollectionUtils.isNotEmpty(chapterPojoList)) {
                            row = sheet.createRow(count);
                            cell = row.createCell(0);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("课程名称：");
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(1);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(coursePojo.getName());
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(2);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("视频年份：");
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(3);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(yearPojo.getName());
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(4);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("视频班次：");
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(5);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(classPojo.getName());
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(6);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("视频班次编号：");
                            cell.setCellStyle(courseStyle);

                            cell = row.createCell(7);
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(classPojo.getId());
                            cell.setCellStyle(courseStyle);

                            count++;

                            for (int i = 0; i < chapterPojoList.size(); i++) {
                                ChapterPojo chapterPojo = chapterPojoList.get(i);

                                row = sheet.createRow(count);
                                cell = row.createCell(0);
                                cell.setCellType(CellType.STRING);
                                cell.setCellValue(chapterPojo.getName());

                                cell = row.createCell(1);
                                cell.setCellType(CellType.STRING);
                                cell.setCellValue("----视频名称----");

                                cell = row.createCell(2);
                                cell.setCellType(CellType.STRING);
                                cell.setCellValue("----视频播放链接----");

                                cell = row.createCell(3);
                                cell.setCellType(CellType.STRING);
                                cell.setCellValue("----视频讲义链接----");

                                cell = row.createCell(4);
                                cell.setCellType(CellType.NUMERIC);
                                cell.setCellValue(i + 1);

                                count++;
                            }
                        }
                    }
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
                    "attachment;filename=" + new String((excelFileName + "-" + BelongToEnum.RECORDED_VIDEO.getTypeName()).getBytes("utf-8"), "iso-8859-1").toString() + ".xlsx");
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

    public void downLoadCourseLiveVideoExcel(List<LiveVideoPojo> liveVideoPojoList, HttpServletRequest request, HttpServletResponse response, String excelFileName) {

        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "课程ID*", "课程名称", "直播类别ID*", "模块类型名称",
                "直播名称", "讲师名称", "直播视频地址", "状态--1：开启 2：关闭",
                "显示顺序"
        };
        int[] columnsColumnWidth = {
                2000, 4000, 4000, 4000,
                4000, 4000, 4000, 10000,
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
        if (CollectionUtils.isNotEmpty(liveVideoPojoList)) {
            int rowNum = 1;
            for (int i = 0; i < liveVideoPojoList.size(); i++) {
                LiveVideoPojo liveVideoPojo = liveVideoPojoList.get(i);
                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(liveVideoPojo.getSubCourseId());
                cell = row.createCell(1);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(liveVideoPojo.getSubCourseName());
                cell = row.createCell(2);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(liveVideoPojo.getChapterId());
                cell = row.createCell(3);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(liveVideoPojo.getChapterName());
                cell = row.createCell(4);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("直播名称");
                cell = row.createCell(5);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("讲师名称");
                cell = row.createCell(6);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("直播视频地址");
                cell = row.createCell(7);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(1);
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(1);
                rowNum++;
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
                    "attachment;filename=" + new String((excelFileName + "-" + BelongToEnum.LIVE_VIDEO.getTypeName()).getBytes("utf-8"), "iso-8859-1").toString() + ".xlsx");
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
}
