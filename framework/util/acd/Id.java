package com.elphin.framework.util.acd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: clark
 * Date: 13-9-4
 * Time: 下午1:25
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    int value() default 0;
}
