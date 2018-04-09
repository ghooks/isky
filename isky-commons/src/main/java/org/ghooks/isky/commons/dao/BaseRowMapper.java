package org.ghooks.isky.commons.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ghooks.isky.commons.consts.SysConst;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据结果集映射类
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class BaseRowMapper<T> implements RowMapper<T> {

    private static Log log = LogFactory.getLog(BaseRowMapper.class);

    private final Class<T> entityClass;
    private final Map<String, PropertyMeta> propsMap;

    public BaseRowMapper(Class<T> entityClass, Map<String, PropertyMeta> propsMap) {
        this.entityClass = entityClass;
        this.propsMap = propsMap;
    }

    public T mapRow(ResultSet rs, int row) throws SQLException {
        T object = null;
        try {
            object = entityClass.newInstance();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String columnLabel;
            PropertyMeta property = null;
            for (int i = 1; i <= count; i++) {
                columnLabel = rsmd.getColumnLabel(i).toUpperCase();
                property = propsMap.get(columnLabel);
                if (property == null) {
                    if (!"NUM".equals(columnLabel)) {
                        log.debug("未找到" + columnLabel + "对应的属性，实体类：" + object.getClass().getName());
                    }
                    continue;
                }
                invoke(object, property, rs, columnLabel);
            }
        }
        catch (SQLException sqle) {
            throw sqle;
        }
        catch (Exception e) {
            log.error("设置数据结果集失败，原因：", e);
            throw new SQLException(e);
        }
        return object;
    }

    /**
     * 根据字段名称设置值
     *
     * @param object
     *            当前实体类
     * @param property
     *            属性对象
     * @param rs
     *            结果集
     * @param columnLabel
     *            字段名称
     * @throws Exception
     */
    private void invoke(Object object, PropertyMeta property, ResultSet rs, String columnLabel) throws Exception {
        Object value = null;
        switch (property.getDataType()) {
            case INT:
                value = rs.getInt(columnLabel);
                break;
            case LONG:
                value = rs.getLong(columnLabel);
                if (rs.wasNull()) {
                    value = null;
                }
                break;
            case DOUBLE:
                value = rs.getDouble(columnLabel);
                break;
            case FLOAT:
                value = rs.getFloat(columnLabel);
                break;
            case STRING:
                value = rs.getString(columnLabel);
                break;
            case ENUM:
                Class<?> cls = property.getField().getType();
                value = cls.getMethod("get", int.class).invoke(cls.getEnumConstants()[0], rs.getInt(columnLabel));
                break;
            case TIMESTAMP:
                value = rs.getTimestamp(columnLabel);
                break;
            case DATE:
                value = rs.getDate(columnLabel);
                break;
            case BLOB:
                Blob blob = rs.getBlob(columnLabel);
                if (blob != null) {
                    value = new String(blob.getBytes((long)1, (int)blob.length()), SysConst.DB_CHARSET);
                }
                break;
        }
        if (rs.wasNull()) {
            value = null;
        }
        if (value != null) {
            property.getMethod().invoke(object, value);
        }
    }
}
