package org.ghooks.isky.commons.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.ghooks.isky.commons.consts.SysConst;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * SQL语句加载工具类
 *
 * Author: eason
 * Date: 2017/11/27 上午10:27
 */
public class SqlUtils {

    private static Log log = LogFactory.getLog(SqlUtils.class);

    private static Properties properties = null;

    /**
     * 获取SQL语句
     *
     * @param key
     * @return
     */
    public static String getSql(String key) {
        if (properties == null) {
            synchronized (SqlUtils.class) {
                properties = new Properties();
                SqlUtils.loadSql();
            }
        }
        String value = properties.getProperty(key);
        if (value == null) {
            String prefix = SysConst.IS_ORACLE_DB ? "oracle." : "mysql.";
            return properties.getProperty(prefix + key);
        }
        return value;
    }

    /**
     * 清空SQL语句
     */
    public static void clear() {
        if (properties == null) {
            return;
        }
        properties.clear();
        properties = null;
    }

    /**
     * 加载sql目录下所有SQL配置文件(包含子目录)
     */
    private static void loadSql() {
        URL dir = SqlUtils.class.getClassLoader().getResource("sql");
        if (dir == null) {
            return;
        }
        List<File> list = new ArrayList<File>();
        SqlUtils.readFile(new File(dir.getPath()), list);
        for (File file : list) {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(file);
                Element root = document.getRootElement();
                List<Element> elements = root.elements();
                for (Element element : elements) {
                    SqlUtils.properties.put(element.attributeValue("id"), element.getTextTrim());
                }
            }
            catch (Exception ioe) {
                log.error("加载SQL配置文件[" + file.getAbsolutePath() + "]失败！");
            }
        }
        log.info("Sql file load completed, total load " + list.size() + " files.");
    }

    /**
     * 递归读取SQL配置文件
     *
     * @param file
     * @param list
     */
    private static void readFile(File file, List<File> list) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    SqlUtils.readFile(f, list);
                }
                else {
                    list.add(f);
                }
            }
        }
        else {
            list.add(file);
        }
    }
}
