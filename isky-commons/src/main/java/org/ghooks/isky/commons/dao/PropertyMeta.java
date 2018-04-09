package org.ghooks.isky.commons.dao;


import org.ghooks.isky.commons.enums.comm.DataType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类属性信息
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class PropertyMeta {

    /**
     * 属性数据类型
     */
    private DataType dataType;
    /**
     * 属性对应的字段
     */
    private Field field;
    /**
     * 属性对应的方法
     */
    private Method method;

    public PropertyMeta() {

    }

    public PropertyMeta(DataType dataType, Field field, Method method) {
        this.dataType = dataType;
        this.field = field;
        this.method = method;
        this.method.setAccessible(true);
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
