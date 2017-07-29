package org.framework.core.common.model.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/28.
 */
public class FilterAjaxJson implements Serializable {

    private String success = AjaxJson.CODE_SUCCESS;

    private String msg = AjaxJson.MSG_SUCCESS;

    private List rows = new ArrayList();

    private Integer total = 0;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
