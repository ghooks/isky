package org.ghooks.isky.commons.dao.adapter;


import org.ghooks.isky.commons.enums.comm.SequenceType;
import org.ghooks.isky.commons.page.Page;

/**
 * 数据库操作通用适配器
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public interface JdbcAdapter {

    /**
     * 获取生成序列SQL语句
     *
     * @param sequenceType
     *            序列枚举
     * @return
     */
    public String getSequenceSql(SequenceType sequenceType);

    /**
     * 获取分页SQL
     *
     * @param sql
     *            SQL
     * @param page
     *            分页对象
     * @return
     */
    public String getPageSql(String sql, Page page);

    /**
     * 获取返回限制记录数SQL
     *
     * @param sql
     *            SQL
     * @param limit
     *            限制记录数
     * @return
     */
    public String getLimitSql(String sql, int limit);
}
