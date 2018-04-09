package org.ghooks.isky.commons.dao.adapter;


import org.ghooks.isky.commons.enums.comm.SequenceType;
import org.ghooks.isky.commons.page.Page;

/**
 * ORACLE 适配器
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class OracleJdbcAdapter implements JdbcAdapter {

    @Override
    public String getSequenceSql(SequenceType sequenceType) {
        return "SELECT " + sequenceType.getNameValue() + ".NEXTVAL FROM DUAL";
    }

    @Override
    public String getPageSql(String sql, Page page) {
        int start = page.getStart();
        int end = start + page.getPageSize() + 1;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (SELECT T.*,ROWNUM NUM FROM ( ");
        sb.append(sql);
        sb.append(" ) T WHERE ROWNUM <= ").append(end);
        sb.append(") WHERE NUM > ").append(start);
        return sb.toString();
    }

    @Override
    public String getLimitSql(String sql, int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (");
        sb.append(sql);
        sb.append(") WHERE ROWNUM <= " + limit + "");
        return sb.toString();
    }
}
