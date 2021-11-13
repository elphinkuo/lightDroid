package com.elphin.framework.app.fpstack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Page Register Annotation
 *
 * @author elphin
 * @version 1.0
 * @date 13-6-29 5:26pm
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterPage {

    /**
     * Page需要注册到的Task
     *
     * @return Task 类名
     */
    public String taskName();

}
