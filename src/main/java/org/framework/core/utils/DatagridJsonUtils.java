package org.framework.core.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.PropertyUtils;
import org.framework.core.common.model.json.DataGrid;
import org.framework.core.common.model.json.DataGridReturn;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/5/28.
 */
public class DatagridJsonUtils {

    /**
     * easyui datagrid 返回结果集
     *
     * @param response
     * @param dataGridReturn
     */
    public static void datagrid(HttpServletResponse response, DataGridReturn dataGridReturn) {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            PrintWriter pw = response.getWriter();
            pw.write(JSON.toJSONString(dataGridReturn));
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatagridJsonUtils() {
    }

    public static DataGridReturn listToObj(DataGridReturn dataGridReturn, Class clasz, String fields) {
        try {
            if (StringUtils.hasText(fields) && dataGridReturn != null && !CollectionUtils.isEmpty(dataGridReturn.getRows())) {
                List<List> rows = dataGridReturn.getRows();
                List entity_rows = new ArrayList();
                String fieldsArray[] = fields.split(",");
                for (int i = 0; i < rows.size(); i++) {
                    Object newObj = clasz.newInstance();
                    Object obj = rows.get(i);
                    for (int j = 0; j < fieldsArray.length; j++) {
                        Field f = clasz.getDeclaredField(fieldsArray[j]);
                        PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clasz);
                        Method writeMethod = pd.getWriteMethod();
                        Method readMethod = pd.getReadMethod();
                        writeMethod.invoke(newObj, readMethod.invoke(obj));
                    }
                    entity_rows.add(newObj);
                }
                dataGridReturn.setRows(entity_rows);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataGridReturn;
    }
}
