package com.btz;

import com.btz.admin.entity.AdminEntity;
import org.framework.core.easyui.hibernate.CriteriaQuery;

/**
 * Created by User on 2017/5/28.
 */
public class MainTest {
    public static void main(String[] args) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(AdminEntity.class,null,null);
        //criteriaQuery.installHql();


    }
}
