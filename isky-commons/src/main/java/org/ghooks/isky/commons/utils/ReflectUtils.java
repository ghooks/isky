package org.ghooks.isky.commons.utils;



import org.ghooks.isky.commons.enums.comm.DataType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具类
 *
 * Author: eason
 * Date: 2017/11/27 上午10:50
 */
public class ReflectUtils {

    private static Map<String, DataType> typeMap = new HashMap<String, DataType>();

    static {
        typeMap.put("int", DataType.INT);
        typeMap.put("java.lang.Integer", DataType.INT);
        typeMap.put("long", DataType.LONG);
        typeMap.put("java.lang.Long", DataType.LONG);
        typeMap.put("float", DataType.FLOAT);
        typeMap.put("java.lang.Float", DataType.FLOAT);
        typeMap.put("double", DataType.DOUBLE);
        typeMap.put("java.lang.Double", DataType.DOUBLE);
        typeMap.put("java.lang.String", DataType.STRING);
        typeMap.put("java.util.Date", DataType.TIMESTAMP);
        typeMap.put("java.sql.Date", DataType.DATE);
    }

    /**
     * 获取方法对象
     *
     * @param classz
     * @param field
     * @return
     */
    public static Method getMethod(Class<?> claz, Field field) {
        try {
            String fieldName = field.getName();
            String methodName = "set" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
            return claz.getMethod(methodName, field.getType());
        }
        catch (NoSuchMethodException nsee) {
            return null;
        }
    }

    /**
     * 获取字段数据类型
     *
     * @param field
     * @return
     */
    public static DataType getDataType(Field field) {
        Class<?> cls = field.getType();
        if (cls.isEnum()) {
            return DataType.ENUM;
        }
        return typeMap.get(cls.getName());
    }
}
