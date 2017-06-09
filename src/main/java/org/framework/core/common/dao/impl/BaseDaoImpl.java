package org.framework.core.common.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.constant.SortDirection;
import org.framework.core.common.dao.BaseDao;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.framework.core.easyui.hibernate.CriteriaQuery;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/5/24.
 */
@Repository("baseDao")
public class BaseDaoImpl implements BaseDao {
    private static Logger logger = LogManager.getLogger(BaseDaoImpl.class.getName());

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    public Session getSession() {
        // 事务必须是开启的(Required)，否则获取不到
        return sessionFactory.getCurrentSession();
    }

    public <T> Serializable save(T entity) {
        try {
            Serializable id = getSession().save(entity);
            if (logger.isDebugEnabled()) {
                logger.debug("保存实体成功," + entity.getClass().getName());
            }
            return id;
        } catch (RuntimeException e) {
            logger.error("保存实体异常", e);
            throw e;
        }
    }

    /**
     * 根据传入的实体添加或更新对象
     *
     * @param <T>
     *
     * @param entity
     */

    public <T> void saveOrUpdate(T entity) {
        try {
            getSession().saveOrUpdate(entity);
            getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("添加或更新成功," + entity.getClass().getName());
            }
        } catch (RuntimeException e) {
            logger.error("添加或更新异常", e);
            throw e;
        }
    }

    /**
     * 根据传入的实体删除对象
     */
    public <T> void delete(T entity) {
        try {
            getSession().delete(entity);
            getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("删除成功," + entity.getClass().getName());
            }
        } catch (RuntimeException e) {
            logger.error("删除异常", e);
            throw e;
        }
    }

    /**
     * 根据Id获取对象。
     */
    public <T> T get(Class<T> entityClass, final Serializable id) {
        return (T) getSession().get(entityClass, id);
    }

    public DataGridReturn getDataGridReturn(CriteriaQuery criteriaQuery) {
        Criteria criteria = criteriaQuery.getDetachedCriteria().getExecutableCriteria(getSession());
        final int allCounts = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        DataGrid dataGrid = criteriaQuery.getDataGrid();
        criteria.setProjection(null);
        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        //排序
        if(SortDirection.asc.equals(dataGrid.getOrder())){
            criteria.addOrder(Order.asc(dataGrid.getSort()));
        }else{
            criteria.addOrder(Order.desc(dataGrid.getSort()));
        }
        criteria.setFirstResult((dataGrid.getPage() - 1) * dataGrid.getRows());
        criteria.setMaxResults(dataGrid.getRows());
        List list = criteria.list();
        dataGrid.setResults(list);
        dataGrid.setTotal(allCounts);
        return new DataGridReturn(allCounts,list);
    }


    /**
     * 根据CriteriaQuery获取List
     *
     * @param cq
     * @return
     */
    @SuppressWarnings("unchecked")
    public List getListByCriteriaQuery(DetachedCriteria cq) {
        Criteria criteria = cq.getExecutableCriteria(getSession());
        return criteria.list();
    }

    /**
     * 加载全部实体
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T> List<T> loadAll(Class<T> entityClass) {
        return getSession().createCriteria(entityClass).list();
    }

}
