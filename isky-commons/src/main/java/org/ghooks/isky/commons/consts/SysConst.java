package org.ghooks.isky.commons.consts;


import org.ghooks.isky.commons.utils.PropUtils;

/**
 * 系统常量
 *
 * Author: eason
 * Date: 2017/11/24 下午5:34
 */
public class SysConst {

    /**
     * 数据库字符集
     */
    public static final String DB_CHARSET = PropUtils.getString("db_charset");

    /**
     * 是否为oracle数据库
     */
    public static final boolean IS_ORACLE_DB = "oracle".equals(PropUtils.getString("db_type"));

    /**
     * 是否为正式版本
     */
    public static boolean IS_RELEASE_VERSION = PropUtils.getBoolean("is_release_version");

    /**
     * 连接池外部配置地址(优先采用)
     */
    public static final String POOL_FILE_PATH = PropUtils.getString("pool_file_path");

    /**
     * 文件版本号
     */
    public static final String FILE_VERSION = (System.currentTimeMillis() / 1000) + "";


}
