package org.ghooks.isky.commons.dao.adapter;


import org.ghooks.isky.commons.enums.comm.SequenceType;
import org.ghooks.isky.commons.page.Page;

/**
 * MYSQL 适配器
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class MysqlJdbcAdapter implements JdbcAdapter {

    @Override
    public String getSequenceSql(SequenceType sequenceType) {
        return "SELECT FN_NEXTVAL('" + sequenceType.getNameValue() + "')";
    }

    @Override
    public String getPageSql(String sql, Page page) {
        return sql + " LIMIT " + page.getStart() + "," + page.getPageSize() + 1;
    }

    @Override
    public String getLimitSql(String sql, int limit) {
        return sql + " LIMIT " + limit;
    }
}
