package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by Bomb on 16/4/18.
 *
 * 切面注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     *根据注解进行拦截,只拦截带有value()注解的bean的所有方法
     */
    Class<? extends Annotation> value();
}
