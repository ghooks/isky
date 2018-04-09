package org.ghooks.isky.commons.dao;


import org.ghooks.isky.commons.consts.SysConst;
import org.ghooks.isky.commons.dao.adapter.JdbcAdapter;
import org.ghooks.isky.commons.dao.adapter.MysqlJdbcAdapter;
import org.ghooks.isky.commons.dao.adapter.OracleJdbcAdapter;
import org.ghooks.isky.commons.enums.comm.DataType;
import org.ghooks.isky.commons.enums.comm.SequenceType;
import org.ghooks.isky.commons.meta.Column;
import org.ghooks.isky.commons.meta.DBType;
import org.ghooks.isky.commons.utils.GenericsUtils;
import org.ghooks.isky.commons.utils.ReflectUtils;
import org.ghooks.isky.commons.utils.SqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库处理基类
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class BaseDao<T> {

    private Logger log = LoggerFactory.getLogger(BaseDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Class<T> entityClass;
    private JdbcAdapter jdbcAdapter;
    private Map<String, PropertyMeta> propsMap;
    private boolean isCglibProxy;

    public BaseDao() {
        isCglibProxy = AopUtils.isCglibProxy(this);
        if (isCglibProxy) {
            return;
        }
        this.init();
    }

    protected void init() {
        entityClass = GenericsUtils.getSuperClassGenericType(getClass());
        if (SysConst.IS_ORACLE_DB) {
            jdbcAdapter = new OracleJdbcAdapter();
        } else {
            jdbcAdapter = new MysqlJdbcAdapter();
        }

        initPropsMap();
    }

    private void initPropsMap() {
        propsMap = new HashMap<String, PropertyMeta>();
        Field[] fields = entityClass.getDeclaredFields();
        Column column = null;
        PropertyMeta property = null;
        Method method = null;
        DataType dataType = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(DBType.class)) {
                dataType = field.getAnnotation(DBType.class).value();
            } else {
                dataType = ReflectUtils.getDataType(field);
            }
            if (dataType == null) {
                continue;
            }
            method = ReflectUtils.getMethod(entityClass, field);
            if (method == null) {
                continue;
            }
            property = new PropertyMeta(dataType, field, method);
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                propsMap.put(column.value().toUpperCase(), property);
            }
            else {
                propsMap.put(getMappingName(field.getName()), property);
            }
        }
    }

    /**
     * 获取属性对应的字段名称(例如：userId -> USER_ID)
     *
     * @param fieldName
     *            属性名称
     * @return
     */
    private String getMappingName(String fieldName) {
        Pattern p = Pattern.compile("[A-Z]+");
        Matcher m = p.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "_" + m.group());
        }
        m.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    /**
     * 获取SQL语句
     *
     * @param key
     * @return
     */
    protected String loadSql(String key) {
        if (isCglibProxy) {
            return null;
        }
        if (key.toLowerCase().startsWith("proc")) {
            return key;
        }
        String sql = SqlUtils.getSql(key);
        if (sql == null) {
            log.info("Can't find sql for " + key + ".");
        }
        return sql;
    }

    /**
     * 获取主键
     *
     * @return
     * @throws Exception
     */
    protected Long getPrimaryKey(SequenceType sequenceType) throws SQLException {
        return getLong(jdbcAdapter.getSequenceSql(sequenceType));
    }

    /**
     * 获取Long类型数据
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected Long getLong(String sql, Object... params) throws SQLException {
        List<Long> list = findObject(sql, DataType.LONG, params);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取Integer类型数据
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected Integer getInteger(String sql, Object... params) throws SQLException {
        List<Integer> list = findObject(sql, DataType.INT, params);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取String类型数据
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected String getString(String sql, Object... params) throws Exception {
        List<String> list = findObject(sql, params);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取T类型数据
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected T getObject(String sql, Object... params) throws Exception {
        List<T> list = find(sql, params);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取T类型数据
     *
     * @param rowMapper
     *            结果映射对象
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected T getObject(RowMapper<T> rowMapper, String sql, Object... params) throws Exception {
        List<T> list = find(rowMapper, sql, params);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取T类型数据列表
     *
     * @param rowMapper
     *            结果映射对象
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected List<T> find(RowMapper<T> rowMapper, String sql, Object... params) throws Exception {
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    /**
     * 获取T类型数据列表
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected List<T> find(String sql, Object... params) throws Exception {
        return find(new BaseRowMapper<T>(entityClass, propsMap), sql, params);
    }

    /**
     * 获取T类型数据列表
     *
     * @param sql
     *            SQL
     * @param values
     *            参数集合
     * @return
     * @throws Exception
     */
    protected List<T> find(String sql, List<Object> values) throws Exception {
        return find(new BaseRowMapper<T>(entityClass, propsMap), sql, values.toArray());
    }

    /**
     * 获取V类型集合(只查询一个字段)
     *
     * @param sql
     *            SQL
     * @param dataType
     *            数据类型枚举
     * @param params
     *            参数列表
     * @return
     */
    protected <V> List<V> findObject(String sql, final DataType dataType, Object... params) throws SQLException {
        RowMapper<V> rowMapper = new RowMapper<V>() {

            public V mapRow(ResultSet rs, int i) throws SQLException {
                Object value = null;
                switch (dataType) {
                    case STRING:
                        value = rs.getString(1);
                        break;
                    case INT:
                        value = rs.getInt(1);
                        break;
                    case LONG:
                        value = rs.getLong(1);
                        break;
                    case DOUBLE:
                        value = rs.getDouble(1);
                        break;
                    case FLOAT:
                        value = rs.getFloat(1);
                        break;
                    case DATE:
                        Timestamp ts = rs.getTimestamp(1);
                        if (ts != null)
                            value = new Date(ts.getTime());
                        else
                            value = null;
                        break;
                    default:
                        value = rs.getObject(1);
                        break;
                }
                return (V) value;
            }
        };
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    /**
     * 获取字符串集合(只查询一个字段)
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected List<String> findObject(String sql, Object... params) throws Exception {
        return findObject(sql, DataType.STRING, params);
    }

    /**
     * 获取T类型数据列表(限制条数)
     *
     * @param rowMapper
     *            结果映射对象
     * @param sql
     *            SQL
     * @param limit
     *            限制条数
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected List<T> findByLimit(RowMapper<T> rowMapper, String sql, int limit, Object... params) throws Exception {
        sql = jdbcAdapter.getLimitSql(sql, limit);
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    /**
     * 获取T类型数据列表(限制条数)
     *
     * @param sql
     *            SQL
     * @param limit
     *            限制条数
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     */
    protected List<T> findByLimit(String sql, int limit, Object... params) throws Exception {
        return findByLimit(new BaseRowMapper<T>(entityClass, propsMap), sql, limit, params);
    }

    /**
     * 新增、修改、删除
     *
     * @param sql
     *            SQL
     * @param params
     *            参数列表
     * @throws Exception
     */
    protected int execute(String sql, Object... params) throws Exception {
        return jdbcTemplate.update(sql, params);
    }

    /**
     * 新增、修改、删除
     *
     * @param sql
     *            SQL
     * @param params
     *            参数集合
     * @return
     * @throws Exception
     */
    protected int execute(String sql, List<Object> params) throws Exception {
        return execute(sql, params.toArray());
    }

    /**
     * 新增、修改、删除
     * @param sql
     * @param abstractLobCreatingPreparedStatementCallback
     * @return
     * @throws Exception
     */
    protected int execute(String sql, AbstractLobCreatingPreparedStatementCallback abstractLobCreatingPreparedStatementCallback) throws Exception {
        return jdbcTemplate.execute(sql, abstractLobCreatingPreparedStatementCallback);
    }

    /**
     * 批量新增、修改、删除
     *
     * @param sql
     *            SQL
     * @param pss
     *            批量参数对象
     * @return
     * @throws Exception
     */
    protected int[] batchExecute(String sql, BatchPreparedStatementSetter pss) throws Exception {
        return jdbcTemplate.batchUpdate(sql, pss);
    }

    /**
     * 拼接字符串条件
     *
     * @param values
     * @return
     */
    protected String join(String... values) {
        if (values.length == 0) {
            return null;
        }
        StringBuffer data = new StringBuffer();
        for (String value : values) {
            data.append("'" + value + "',");
        }
        return data.deleteCharAt(data.length() - 1).toString();
    }

    /**
     * 拼接数字条件
     *
     * @param values
     * @return
     */
    protected String join(Long... values) {
        if (values.length == 0) {
            return null;
        }
        StringBuffer data = new StringBuffer();
        for (Long value : values) {
            data.append(value + ",");
        }
        return data.deleteCharAt(data.length() - 1).toString();
    }

    /**
     * 拼接条件
     *
     * @param values
     * @return
     */
    protected String join(List<?> values) {
        if (values.size() == 0) {
            return null;
        }
        if (values.get(0) instanceof Long) {
            return join(values.toArray(new Long[0]));
        }
        return join(values.toArray(new String[0]));
    }

    protected Date toDate(Long date) {
        if (date == null) {
            return new Date();
        }
        return new Date(date);
    }

}
