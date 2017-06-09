package com.btz.course.controller;

import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.MainCourseEntity;
import com.btz.course.service.ChapterService;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/chapterController")
public class ChapterController extends BaseController {

    @Autowired
    private ChapterService chapterService;

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(ChapterEntity chapterEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = chapterEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要修改的账户ID！");
            return j;
        }
        ChapterEntity chapterDb = chapterService.get(ChapterEntity.class, id);
        if (chapterDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该账户不存在！");
            return j;
        }
        j.setContent(chapterDb);
        return j;
    }
}
