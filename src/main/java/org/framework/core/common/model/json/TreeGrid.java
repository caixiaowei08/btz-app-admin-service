package org.framework.core.common.model.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/9.
 */
public class TreeGrid implements Serializable{

    private Integer total;

    private List rows;

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
