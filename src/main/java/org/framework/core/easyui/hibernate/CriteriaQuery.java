package org.framework.core.easyui.hibernate;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.model.json.DataGrid;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * DataGrid查询条件封装
 * Created by User on 2017/5/28.
 */
public class CriteriaQuery {

    /**
     * 时间查询符号
     */
    private static final String END = "_end";

    private static final String BEGIN = "_begin";

    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    /**
     * 查询映射对象
     */
    private Class entityClass;
    /**
     * easyui 分页条件封装器
     */
    private DataGrid dataGrid;
    /**
     * 输入查询条件
     */
    private Map<String, String[]> params;
    /**
     * hibernate离线查询条件封装对象
     */
    private DetachedCriteria detachedCriteria;

    private ProjectionList projectionList;

    /**
     * @param entityClass
     * @param dataGrid
     * @param params
     */
    public CriteriaQuery(Class entityClass, DataGrid dataGrid, Map params) {
        this.entityClass = entityClass;
        this.dataGrid = dataGrid;
        this.params = params;
        this.detachedCriteria = DetachedCriteria.forClass(entityClass);
    }

    public void installCriteria() {
        //条件组合
        try {
            installHqlParams();
            installProjectionList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void installProjectionList() {
        if (StringUtils.hasText(dataGrid.getField())) {
            String[] columns = dataGrid.getField().split(",");
            ProjectionList projectionList = Projections.projectionList();
            for (String column : columns) {
                projectionList.add(Projections.property(column));
            }
           this.projectionList = projectionList;
        }
    }

    /**
     * 条件组合
     *
     * @throws Exception
     */
    public void installHqlParams() throws Exception {
        if (params != null && params.size() > 0) {
            PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(entityClass);
            String name, type;
            for (int i = 0; i < origDescriptors.length; i++) {
                name = origDescriptors[i].getName();
                type = origDescriptors[i].getPropertyType().toString();
                String value;
                if (type.contains("class java.lang.String")) {
                    if (params.containsKey(name)) {
                        value = params.get(name)[0].trim();
                        if (StringUtils.hasText(value)) {
                            detachedCriteria.add(Restrictions.like(name, "%"+value+"%"));
                        } else {
                            continue;
                        }
                    }
                } else if (type.contains("java.lang.Integer")) {
                    if (params.containsKey(name)) {
                        value = params.get(name)[0].trim();
                        if (StringUtils.hasText(value)) {
                            detachedCriteria.add(Restrictions.eq(name, Integer.parseInt(value)));
                        } else {
                            continue;
                        }
                    }
                } else if (type.contains("class java.util.Date")) {
                    String beginDateValue = null;
                    String endDateValue = null;
                    if (params.containsKey(name + BEGIN)) {
                        beginDateValue = params.get(name + BEGIN)[0].trim();
                    }
                    if (params.containsKey(name + END)) {
                        endDateValue = params.get(name + END)[0].trim();
                    }

                    if (StringUtils.hasText(beginDateValue) && StringUtils.hasText(endDateValue)) {
                        if (beginDateValue.length() == 10) {
                            detachedCriteria.add(Restrictions.ge(name, time.parse(beginDateValue + " 00:00:00")));
                        } else if (beginDateValue.length() == 19) {
                            detachedCriteria.add(Restrictions.ge(name, time.parse(beginDateValue)));
                        }

                        if (endDateValue.length() == 10) {
                            detachedCriteria.add(Restrictions.le(name, time.parse(endDateValue + " 23:23:59")));
                        } else if (endDateValue.length() == 19) {
                            detachedCriteria.add(Restrictions.le(name, time.parse(endDateValue)));
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    public DataGrid getDataGrid() {
        return dataGrid;
    }

    public void setDataGrid(DataGrid dataGrid) {
        this.dataGrid = dataGrid;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public void setParams(Map<String, String[]> params) {
        this.params = params;
    }

    public DetachedCriteria getDetachedCriteria() {
        return detachedCriteria;
    }

    public void setDetachedCriteria(DetachedCriteria detachedCriteria) {
        this.detachedCriteria = detachedCriteria;
    }

    public ProjectionList getProjectionList() {
        return projectionList;
    }

    public void setProjectionList(ProjectionList projectionList) {
        this.projectionList = projectionList;
    }
}
