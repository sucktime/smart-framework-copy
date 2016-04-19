package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Bomb on 16/4/13.
 */
public final class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 根据Class对象创建Object对象
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance;
        try{
            instance = cls.newInstance();
        } catch (Exception e){
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 触发某对象的某个方法
     * @param object
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object object, Method method, Object...args){
        Object result;
        try{
            method.setAccessible(true);
            result = method.invoke(object, args);
        } catch (Exception e){
            LOGGER.error("invoke method failure.", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置某对象的某个属性的值
     * @param object
     * @param field
     * @param value
     */
    public static void setFiled(Object object, Field field, Object value){
        try{
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e){
            LOGGER.error("set field failure.", e);
            throw new RuntimeException(e);
        }
    }
}
