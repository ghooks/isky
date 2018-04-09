package org.ghooks.isky.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie工具类
 *
 * Author: eason
 * Date: 2017/12/27 下午4:44
 */
public class CookieUtils {

    private static Logger log = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 设置Cookie
     *
     * @param response
     * @param key
     * @param value
     * @param expiry
     * @param path
     * @param domain
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiry, String path,
                                 String domain) {
        try {
            if (value != null) {
                value = URLEncoder.encode(value, "utf-8");
            }
        }
        catch (UnsupportedEncodingException uee) {
            log.error("设置Cookie信息出错，原因：", uee);
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        if (path != null) {
            cookie.setPath(path);
        }
        if (domain != null) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    /**
     * 设置Cookie
     *
     * @param response
     * @param key
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String key, String value) {
        CookieUtils.setCookie(response, key, value, -1, "/", null);
    }

    /**
     * 设置Cookie
     *
     * @param response
     * @param key
     * @param value
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiry) {
        CookieUtils.setCookie(response, key, value, expiry, "/", null);
    }

    /**
     * 设置Cookie
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     */
    public static void setCookie(HttpServletResponse response, String key, String value, String domain) {
        CookieUtils.setCookie(response, key, value, -1, "/", domain);
    }

    /**
     * 获取Cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || key == null || key.length() == 0) {
            return null;
        }
        String value = null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(key)) {
                value = cookies[i].getValue();
                try {
                    value = URLDecoder.decode(value, "utf-8");
                }
                catch (UnsupportedEncodingException uee) {
                    log.error("获取Cookie信息出错，原因：", uee);
                }
                return value;
            }
        }
        return null;
    }

    /**
     * 删除Cookie
     *
     * @param response
     * @param key
     * @param path
     * @param domain
     */
    public static void deleteCookie(HttpServletResponse response, String key, String path, String domain) {
        CookieUtils.setCookie(response, key, null, 0, path, domain);
    }

    /**
     * 删除Cookie
     *
     * @param response
     * @param key
     */
    public static void deleteCookie(HttpServletResponse response, String key) {
        CookieUtils.setCookie(response, key, null, 0);
    }

    /**
     * 删除Cookie信息
     *
     * @param response
     * @param key
     * @param domain
     */
    public static void deleteCookie(HttpServletResponse response, String key, String domain) {
        CookieUtils.setCookie(response, key, null, 0, "/", domain);
    }
}
