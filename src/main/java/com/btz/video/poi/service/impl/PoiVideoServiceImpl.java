package com.btz.video.poi.service.impl;

import com.btz.contants.QuestionType;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.utils.BelongToEnum;
import com.btz.video.poi.service.PoiVideoService;
import com.btz.video.recorded.controller.CourseRecordedVideoController;
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
            List<RecordedVideoPojo> recordedVideoPojoList,
            HttpServletRequest request,
            HttpServletResponse response,
            String excelFileName
    ) {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "课程ID*", "课程名称", "章节ID*", "章节名称",
                "模块类型ID*", "模块类型名称", "录播视频名称", "录播视频地址",
                "讲义地址", "显示顺序"
        };
        int[] columnsColumnWidth = {
                2000, 4000, 2000, 4000,
                4000, 4000, 10000, 10000,
                10000, 4000
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
        if (CollectionUtils.isNotEmpty(recordedVideoPojoList)) {
            int rowNum = 1;
            for (int i = 0; i < recordedVideoPojoList.size(); i++) {
                RecordedVideoPojo recordedVideoPojo = recordedVideoPojoList.get(i);
                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(recordedVideoPojo.getSubCourseId());
                cell = row.createCell(1);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(recordedVideoPojo.getSubCourseName());
                cell = row.createCell(2);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(recordedVideoPojo.getChapterId());
                cell = row.createCell(3);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(recordedVideoPojo.getChapterName());
                cell = row.createCell(4);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(BelongToEnum.RECORDED_VIDEO.getIndex());
                cell = row.createCell(5);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(BelongToEnum.RECORDED_VIDEO.getTypeName());
                cell = row.createCell(6);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("录播视频名称");
                cell = row.createCell(7);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("录播视频地址");
                cell = row.createCell(8);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("录播视频讲义");
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(rowNum);
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
}
