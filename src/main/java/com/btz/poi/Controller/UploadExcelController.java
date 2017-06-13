package com.btz.poi.Controller;

import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by User on 2017/6/12.
 */
@Scope("prototype")
@Controller
@RequestMapping("/uploadExcelController")
public class UploadExcelController extends BaseController{

    @RequestMapping(params = "uploadExcel")
    @ResponseBody
    public AjaxJson uploadExcel(@RequestParam("file") CommonsMultipartFile file){
        AjaxJson j = new AjaxJson();
        long  startTime=System.currentTimeMillis();
        System.out.println("fileName："+file.getOriginalFilename());
        try {
            InputStream is= file.getInputStream();
        }catch (IOException e) {
            e.printStackTrace();
        }

        j.setMsg(file.getOriginalFilename());
        return j;
    }
}
