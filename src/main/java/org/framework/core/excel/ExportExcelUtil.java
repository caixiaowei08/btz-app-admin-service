package org.framework.core.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2017/6/15.
 */
public class ExportExcelUtil {

    public static void export(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        String fileName = System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try{









        }catch (Exception e){

        }






    }

}
