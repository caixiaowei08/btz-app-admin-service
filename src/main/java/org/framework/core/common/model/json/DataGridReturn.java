package org.framework.core.common.model.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/5/28.
 */
public class DataGridReturn implements Serializable{
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 总记录列表
     */
    private List rows;

    public DataGridReturn(Integer total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
