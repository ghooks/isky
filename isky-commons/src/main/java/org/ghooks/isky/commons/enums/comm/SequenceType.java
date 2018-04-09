package org.ghooks.isky.commons.enums.comm;

/**
 * 数据类序列枚举
 *
 * Author: eason
 * Date: 2017/11/24 下午5:08
 */
public enum SequenceType {

    SYS {
        @Override
        public String getNameValue() {
            return "SEQ_SYS";
        }
    };

    public abstract String getNameValue();
}
