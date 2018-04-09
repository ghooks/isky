package org.ghooks.isky.commons.pool;

import org.ghooks.isky.commons.consts.SysConst;
import org.ghooks.isky.commons.utils.FileUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 连接池配置文件
 *
 * Author: eason
 * Date: 2017/11/27 上午10:52
 */
public class PoolProperty extends PropertiesFactoryBean {

    @Override
    protected Properties createProperties() throws IOException {
        // 如果路径存在，则启用外部配置文件
        if (FileUtils.exists(SysConst.POOL_FILE_PATH)) {
            Properties props = new Properties();
            InputStream in = new FileInputStream(SysConst.POOL_FILE_PATH);
            props.load(in);
            in.close();
            return props;
        }
        return null;
    }
}
