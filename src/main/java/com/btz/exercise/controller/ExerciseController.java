package com.btz.exercise.controller;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.exercise.utils.PoiExcelExerciseUtils;
import org.apache.commons.io.FileUtils;
import org.framework.core.common.constant.PoiConstant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @RequestMapping(params = "uploadExcel")
    @ResponseBody
    public AjaxJson uploadExcel(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request) throws IOException{
        AjaxJson j = new AjaxJson();
        String realPath = request.getSession().getServletContext().getRealPath("/files/upload/loanData");
        String newFileName = UUID.randomUUID()+ file.getOriginalFilename();
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


        List<ExerciseEntity>  exerciseEntityList = null;
        String postfix = "xls";//org.framework.core.utils.StringUtils.getPostfix();
        try {
            if (StringUtils.hasText(postfix) && PoiConstant.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                exerciseEntityList =  PoiExcelExerciseUtils.readXls(filelocal);
            } else if (StringUtils.hasText(postfix) && PoiConstant.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
               // exerciseEntityList =  PoiExcelExerciseUtils.readXls(is);
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("上传文件必须是EXCEL文件！");
                return j;
            }
        }catch (IOException e){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("文档格式不正确，请修正之后，再上传！");
            return j;
        }

        try{
            for (int i = 0; i <exerciseEntityList.size() ; i++) {
                ExerciseEntity exerciseEntity = exerciseEntityList.get(i);
                exerciseEntity.setVersionNo(1);
                exerciseEntity.setSubCourseId(17);
                exerciseEntity.setChapterId(2);
                exerciseEntity.setType(1);
                exerciseEntity.setBelongTo(1);
                exerciseEntity.setCreateTime(new Date());
                exerciseEntity.setUpdateTime(new Date());
            }
        }catch (Exception e){

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

}
