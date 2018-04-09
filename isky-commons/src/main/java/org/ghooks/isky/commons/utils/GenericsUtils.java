package org.ghooks.isky.commons.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 *
 * Author: eason
 * Date: 2017/11/27 上午11:04
 */
public class GenericsUtils {

    /**
     * 通过反射获得定义Class时声明的父类的范型参数的类型
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenericType(Class clazz) {
        return GenericsUtils.getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射获得定义Class时声明的父类的范型参数的类型
     *
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenericType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }
}
