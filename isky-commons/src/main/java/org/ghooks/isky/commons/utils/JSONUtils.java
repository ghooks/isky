package org.ghooks.isky.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * JSON工具类型
 *
 * Author: eason
 * Date: 2017/11/27 下午5:09
 */
public class JSONUtils {

    private static Logger log = LoggerFactory.getLogger(JSONUtils.class);

    public static void printJson(HttpServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            if (log.isDebugEnabled()) {
                log.debug(JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect));
            }
            out.print(JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect));
        }
        catch (Exception e) {
            log.error("输出信息时出错，原因：", e);
        }
        finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
