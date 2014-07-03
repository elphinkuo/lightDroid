package com.elphin.framework.util.acd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-25
 * Time: 下午10:37
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Binding {
    Id[] value() default {};

    ActionType type() default ActionType.ON_CLICK;
}
