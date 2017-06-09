package org.framework.core.common.service;

import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/5/24.
 */
public interface BaseService {
    /**
     * 保存对象
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Serializable save(T entity);

    /**
     * 根据实体名称和主键获取实体
     *
     * @param <T>
     * @param
     * @param id
     * @return
     */
    public <T> T get(Class<T> class1, Serializable id);

    /**
     * 自定义条件查询
     * @param cq
     * @return
     */
    public List getListByCriteriaQuery(DetachedCriteria cq);

    public <T> void saveOrUpdate(T entity);

    public <T> void delete(T entity);

    /**
     * 返回easyui datagrid模型
     * @param criteriaQuery
     * @return
     */
    public DataGridReturn getDataGridReturn(final CriteriaQuery criteriaQuery);

    /**
     * 加载全部实体
     *
     * @param <T>
     * @param entityClass
     * @return
     */
    public <T> List<T> loadAll(final Class<T> entityClass);
}
