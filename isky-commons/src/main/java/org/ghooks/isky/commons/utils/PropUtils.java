package org.ghooks.isky.commons.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 属性文件操作工具类
 *
 * Author: eason
 * Date: 2017/11/24 下午5:20
 */
public class PropUtils {

    private static Logger log = LoggerFactory.getLogger(PropUtils.class);

    /**
     * 资源对象集合
     */
    private static Map<String, ResourceBundle> resource = new HashMap<String, ResourceBundle>();

    /**
     * 默认资源文件
     */
    private static final String DEFAULT_FILE = "conf/system";

    /**
     * 根据文件名获取资源对象
     *
     * @param fileName
     *            文件名称(相对路径)
     * @return
     */
    private static ResourceBundle getBundle(String fileName) {
        ResourceBundle bundle = resource.get(fileName);
        try {
            if (bundle == null) {
                bundle = ResourceBundle.getBundle(fileName, Locale.getDefault());
                resource.put(fileName, bundle);
            }
        }
        catch (Exception e) {
            log.error("Access properties file error, reason: ", e);
        }
        return bundle;
    }

    /**
     * 获取指定键的值
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return
     */
    public static String getString(String key, String defaultValue) {
        ResourceBundle bundle = getBundle(DEFAULT_FILE);
        if (bundle != null) {
            try {
                String value = bundle.getString(key);
                if (value != null) {
                    return value.trim();
                }
            }
            catch (MissingResourceException e) {
                // log.warn("Can't find resource of key " + key + ".");
            }
        }
        return defaultValue;
    }

    /**
     * 获取指定键的值
     *
     * @param key
     *            键
     * @return
     */
    public static String getString(String key) {
        return PropUtils.getString(key, null);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = PropUtils.getString(key, null);
        if (value == null) {
            return defaultValue;
        }
        return "true".equals(value);
    }

    public static boolean getBoolean(String key) {
        return PropUtils.getBoolean(key, false);
    }

    /**
     * 获取指定键的值
     *
     * @param key
     *            键
     * @param defaultValue
     *            文件名称
     * @return
     */
    public static Integer getInt(String key, Integer defaultValue) {
        String value = PropUtils.getString(key, null);
        if (value == null) {
            return defaultValue;
        }
        return NumberUtils.toInt(value);
    }

    /**
     * 获取指定键的值
     *
     * @param key
     *            键
     * @return
     */
    public static Integer getInt(String key) {
        return PropUtils.getInt(key, null);
    }

    /**
     * 获取指定键的值
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return
     */
    public static Long getLong(String key, Long defaultValue) {
        String value = PropUtils.getString(key, null);
        if (value == null) {
            return defaultValue;
        }
        return NumberUtils.toLong(value);
    }

    /**
     * 获取指定键的值
     *
     * @param key
     *            键
     * @return
     */
    public static Long getLong(String key) {
        return PropUtils.getLong(key, null);
    }

    /**
     * 获取指定文件所有的键
     *
     * @param fileName
     *            文件名称
     * @return
     */
    public static List<String> getKeys(String fileName) {
        List<String> listKey = new ArrayList<String>();
        ResourceBundle bundle = getBundle(fileName);
        if (bundle != null) {
            Enumeration<String> et = bundle.getKeys();
            while (et.hasMoreElements()) {
                listKey.add(et.nextElement());
            }
        }
        return listKey;
    }

    /**
     * 获取默认默认文件多有的键
     *
     * @return
     */
    public static List<String> getKeys() {
        return PropUtils.getKeys(DEFAULT_FILE);
    }
}
