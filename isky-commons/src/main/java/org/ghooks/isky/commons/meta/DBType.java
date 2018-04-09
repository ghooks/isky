package org.ghooks.isky.commons.meta;



import org.ghooks.isky.commons.enums.comm.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据类型元数据
 *
 * Author: eason
 * Date: 2017/11/27 上午10:57
 */
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DBType {

    DataType value();

}