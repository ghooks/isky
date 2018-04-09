package org.ghooks.isky.commons.web.controller;

import com.alibaba.fastjson.JSONObject;

import org.ghooks.isky.commons.consts.CookieConst;
import org.ghooks.isky.commons.utils.CookieUtils;
import org.ghooks.isky.commons.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: eason
 * Date: 2017/11/27 上午11:50
 */
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    private Long loginId;

    protected JSONObject writeSuccess() {
        JSONObject result = new JSONObject();
        result.put("ret", 1);
        return result;
    }

    protected JSONObject writeSuccess(JSONObject object) {
        JSONObject result = new JSONObject();
        result.put("ret", 1);
        result.putAll(object);
        return result;
    }

    protected JSONObject writeSuccess(String key, Object value) {
        JSONObject result = new JSONObject();
        result.put("ret", 1);
        result.put(key, value);
        return result;
    }

    protected JSONObject writeFailure(String msg) {
        JSONObject result = new JSONObject();
        result.put("ret", 0);
        result.put("msg", msg);
        return result;
    }

    protected JSONObject writeFailure(String msg, int errcode) {
        JSONObject result = new JSONObject();
        result.put("ret", 0);
        result.put("msg", msg);
        result.put("errcode", errcode);
        return result;
    }

    /**
     * 获取当前登录用户ID
     *
     * @return
     */
    protected Long getLoginId() {
        if (loginId != null) {
            return loginId;
        }
        String value = CookieUtils.getCookie(request, CookieConst.LOGIN_UID);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        loginId = new Long(value);
        return loginId;
    }

    protected boolean isLogined() {
        return getLoginId() != null;
    }
}
