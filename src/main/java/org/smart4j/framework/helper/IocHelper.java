package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.MapUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Bomb on 16/4/13.
 */
public final class IocHelper {

    static {
        /**
         * 借助BeanHelper获得包含所有Bean的Map
         */
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();

        if (!MapUtil.isEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取Bean类定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (!ArrayUtil.isEmpty(beanFields)) {
                    for (Field beanField : beanFields) {
                        //根据@Inject注解判断是否需要依赖注入
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            /**
                             * 进行依赖注入
                             */
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null){
                                ReflectionUtil.setFiled(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}