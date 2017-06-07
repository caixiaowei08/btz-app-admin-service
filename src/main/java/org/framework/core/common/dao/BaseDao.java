package org.framework.core.common.dao;

import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/5/24.
 */
public interface BaseDao {

    /**
     * 保存
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Serializable save(T entity);

    /**
     * 保存修改
     * @param entity
     * @param <T>
     */
    public <T> void saveOrUpdate(T entity);

    /**
     * 删除实体
     *
     * @param <T>
     *
     * @param <T>
     *
     * @param <T>
     * @param entitie
     */
    public <T> void delete(T entitie);

    /**
     * 根据实体名称和主键获取实体
     *
     * @param <T>
     * @param entityName
     * @param id
     * @return
     */
    public <T> T get(Class<T> entityName, Serializable id);

    /**
     * 返回easyui datagrid模型
     *
     * @param criteriaQuery
     * @return
     */
    public DataGridReturn getDataGridReturn(final CriteriaQuery criteriaQuery);

    public List getListByCriteriaQuery(DetachedCriteria cq);

}
