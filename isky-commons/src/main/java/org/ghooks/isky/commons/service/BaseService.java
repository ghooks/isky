package org.ghooks.isky.commons.service;

import org.springframework.aop.support.AopUtils;

/**
 * 业务处理基类
 *
 * Author: eason
 * Date: 2017/11/27 上午11:39
 */
public class BaseService {

    public BaseService() {
        if (AopUtils.isCglibProxy(this)) {
            return;
        }
        this.init();
    }

    protected void init() {

    }
}
