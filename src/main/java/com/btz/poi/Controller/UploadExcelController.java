package com.btz.poi.Controller;

import com.btz.poi.pojo.ExerciseExcelPojo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/12.
 */
@Scope("prototype")
@Controller
@RequestMapping("/uploadExcelController")
public class UploadExcelController extends BaseController {

    @RequestMapping(params = "uploadExcel")
    @ResponseBody
    public AjaxJson uploadExcel(@RequestParam("file") CommonsMultipartFile file) {
        AjaxJson j = new AjaxJson();
        try {
            InputStream is = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        j.setMsg(file.getOriginalFilename());
        return j;
    }

    public HttpServletResponse downLaodExcel(HttpServletRequest request, HttpServletResponse response) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet("第一页");
        sheet.setColumnWidth((short) 0, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 1, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 2, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 3, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 4, (short) (35.7 * 250));
        sheet.setColumnWidth((short) 5, (short) (35.7 * 150));
        sheet.setColumnWidth((short) 6, (short) (35.7 * 150));
        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        // 创建两种字体
        Font f = wb.createFont();
        // 创建第一种字体样式
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.RED.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 设置第一种单元格的样式
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        List<ExerciseExcelPojo> list = new ArrayList<ExerciseExcelPojo>();
        for (int i = 0; i < 10000; i++) {
            ExerciseExcelPojo exerciseExcelPojo = new ExerciseExcelPojo();
            exerciseExcelPojo.setTitle("题目一" + i);
            list.add(exerciseExcelPojo);
        }
        // 创建第一行
        Row row = sheet.createRow((short) 0);
        // 创建列（每行里的单元格）
        Cell cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell.setCellStyle(cs);
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(list.get(i).getTitle());
            cell.setCellStyle(cs);
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
        response.setHeader("Content-Disposition", "attachment;filename=text.xls");

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
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {

                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }
}
