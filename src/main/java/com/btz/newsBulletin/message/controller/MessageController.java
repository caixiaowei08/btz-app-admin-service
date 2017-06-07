package com.btz.newsBulletin.message.controller;

import com.btz.newsBulletin.carousel.entity.CarouselEntity;
import com.btz.newsBulletin.message.entity.MessageEntity;
import com.btz.newsBulletin.message.service.MessageService;
import com.btz.user.entity.UserEntity;
import com.btz.utils.Constant;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.DatagridJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by User on 2017/6/3.
 */
@Scope("prototype")
@Controller
@RequestMapping("/messageController")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(params = "datagrid")
    public void datagrid(MessageEntity messageEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(MessageEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.installCriteria();
        DataGridReturn dataGridReturn = messageService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, MessageEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(MessageEntity messageEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            messageEntity.setUpdateTime(new Date());
            messageEntity.setCreateTime(new Date());
            messageService.save(messageEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "auditing")
    @ResponseBody
    public AjaxJson auditing(MessageEntity messageEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    MessageEntity t = messageService.get(MessageEntity.class, Integer.parseInt(id_array[i]));
                    if (t != null) {
                        if (t.getState() != Constant.STATE_ENTERED) {
                            j.setSuccess(AjaxJson.CODE_FAIL);
                            j.setMsg("请选择[已录入]状态的通知，进行审核！");
                            return j;
                        }
                        t.setState(Constant.STATE_BRELEASE);
                        messageService.saveOrUpdate(t);
                    }
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未输入需要审核的数据ID！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("冻结失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "sending")
    @ResponseBody
    public AjaxJson sending(MessageEntity messageEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    MessageEntity t = messageService.get(MessageEntity.class, Integer.parseInt(id_array[i]));
                    if (t != null) {
                        if (t.getState() == Constant.STATE_BRELEASE || t.getState() == Constant.STATE_RELEASED) {
                            j.setSuccess(AjaxJson.CODE_FAIL);
                            j.setMsg("请选择[待发布]、[已发布]的消息进行发送！");
                            return j;
                        }
                        //@TODO 消息发送

                    }else {
                        j.setSuccess(AjaxJson.CODE_FAIL);
                        j.setMsg("未选择消息进行发送！");
                        return j;

                    }
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未选择消息进行发送！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("发送消息失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(MessageEntity messageEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        MessageEntity t = messageService.get(MessageEntity.class, messageEntity.getId());
        if(t == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改的通知不存在！");
            return j;
        }
        try {
            messageEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(messageEntity, t);
            messageService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(MessageEntity messageEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    messageEntity = messageService.get(MessageEntity.class, Integer.parseInt(id_array[i]));
                    if (messageEntity != null) {
                        messageService.delete(messageEntity);
                    }
                }
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未输入需要删除的数据ID！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "get")
    @ResponseBody
    public AjaxJson get(MessageEntity messageEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = messageEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入需要查询的数据ID！");
            return j;
        }

        MessageEntity messageDb = messageService.get(MessageEntity.class, id);
        if (messageDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该数据不存在！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(messageDb);
        return j;
    }


}
