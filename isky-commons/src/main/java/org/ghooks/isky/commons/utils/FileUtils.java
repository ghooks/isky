package org.ghooks.isky.commons.utils;

import java.io.File;

/**
 * 文件操作工具类型
 *
 * Author: eason
 * Date: 2017/11/27 上午10:54
 */
public class FileUtils {

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        return new File(path).exists();
    }
}
