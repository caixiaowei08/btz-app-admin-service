package org.framework.core.common.model.json;

import org.framework.core.common.constant.SortDirection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/5/27.
 */
public class DataGrid implements Serializable{

    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 每页显示记录数
     */
    private int rows = 10;// 每页显示记录数
    /**
     * 排序字段名
     */
    private String sort = "id";// 排序字段名
    /**
     * 按什么排序(asc,desc)
     */
    private SortDirection order = SortDirection.asc;
    /**
     * 查询返回字段
     */
    private String field ;
    /**
     * 树形数据表文本字段
     */
    private String treefield;
    /**
     *  结果集
     */
    private List results;
    /**
     * 总记录数
     */
    private int total;
    /**
     * 合计列
     */
    private String footer;
    /**
     * 合计列
     */
    private String sqlbuilder;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public SortDirection getOrder() {
        return order;
    }

    public void setOrder(SortDirection order) {
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTreefield() {
        return treefield;
    }

    public void setTreefield(String treefield) {
        this.treefield = treefield;
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getSqlbuilder() {
        return sqlbuilder;
    }

    public void setSqlbuilder(String sqlbuilder) {
        this.sqlbuilder = sqlbuilder;
    }
}
